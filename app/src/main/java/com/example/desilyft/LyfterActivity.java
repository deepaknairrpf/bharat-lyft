package com.example.desilyft;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.libizo.CustomEditText;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class LyfterActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Place destination;
    private LatLng source;
    private HashMap<String, Object> lifteeAssignmentDetails;
    private HashMap<String, Object> lifterService;
    private static final int  REQUEST_FINE_LOCATION = 101;
    private static final int ZOOM_LEVEL = 15;
    private static final int TILT_LEVEL = 0;
    private static final int BEARING_LEVEL = 0;

    private void createLyfterService(LatLng source, LatLng destination) {
        String endPoint = "/api/lyfter-services";
        HashMap<String, Object> dataToBackend = new HashMap<>();
        dataToBackend.put("lyftee_max_limit", 1);
        dataToBackend.put("source_lat", source.latitude);
        dataToBackend.put("source_long", source.longitude);
        dataToBackend.put("destination_lat", destination.latitude);
        dataToBackend.put("destination_long", destination.longitude);
        Http.hit(endPoint, new JSONObject(dataToBackend), new LyfterServiceResponseHandler());
    }

    private void assignLyftees(Integer lyfterServiceId) {
        String endPoint = "/api/lyfter-services/" + lyfterServiceId + "/find-lyftee";
        Log.d("assign_endpoint", endPoint);
        Http.hit(endPoint, null, new AssignLyfteesResponseHandler());

    }

    class LyfterServiceResponseHandler implements Callback {
        @Override
        public void handleResponse(HashMap<String, Object> response) {
            Log.d("lyfter_service", response.toString());
            lifterService = response;
            assignLyftees(Integer.parseInt(response.get("id").toString()));
        }
    }
    class AssignLyfteesResponseHandler implements Callback {
        @Override
        public void handleResponse(HashMap<String, Object> response) {
            lifteeAssignmentDetails = response;
            Log.d("liftee_assignm", lifteeAssignmentDetails.toString());
            showLyfteeCard(response);
        }
    }


    private void takeToGoogleMaps() {
        String originLat = lifterService.get("source_lat").toString();
        String originLong = lifterService.get("source_long").toString();
        String destinationLat = lifterService.get("destination_lat").toString();
        String destinationLong = lifterService.get("destination_long").toString();
        String waypointSourceLat = lifteeAssignmentDetails.get("pickup_point_lat").toString();
        String wayPointSourceLong = lifteeAssignmentDetails.get("pickup_point_long").toString();
        String wayPointDestinationLat = lifteeAssignmentDetails.get("drop_point_lat").toString();
        String wayPointDestinationLong = lifteeAssignmentDetails.get("drop_point_long").toString();
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+ originLat + "," + originLong + "&destination=" + destinationLat + "," + destinationLong + "&waypoints=" + waypointSourceLat + "," + wayPointSourceLong + "|" + wayPointDestinationLat + "," + wayPointDestinationLong + "&travelmode=driving");
        Log.d("uri", gmmIntentUri.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showLyfteeCard(HashMap<String, Object> lifteeDetails) {
        HashMap<String, Object> lyfteeSchedule = (HashMap<String, Object>)lifteeDetails.get("lyftee_schedule");
        HashMap<String, Object> userData = (HashMap<String, Object>)lyfteeSchedule.get("user_details");
        String name = userData.get("first_name").toString();
        View parent = findViewById(R.id.liftee_card);
        parent.setVisibility(View.VISIBLE);
        TextView nameTextView = (TextView)parent.findViewById(R.id.nameTextView);
        nameTextView.setText(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyfter);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        /*AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        autocompleteFragment.setFilter(filter);*/
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destination = place;
                // Get the
                createLyfterService(source, destination.getLatLng());
                Toast.makeText(getApplicationContext(),"Destination Set to "+place.getName(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Status status) {
                Log.d("DEBUG",status.toString());
            }
        });

        Button acceptButton = (Button) findViewById(R.id.accept_button);
        acceptButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takeToGoogleMaps();
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(checkPermissions())
            mMap.setMyLocationEnabled(true);
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            REQUEST_FINE_LOCATION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_FINE_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("123", "Setting location");
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        source = latLng;
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }
}
