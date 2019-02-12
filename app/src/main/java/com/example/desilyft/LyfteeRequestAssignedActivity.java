package com.example.desilyft;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class LyfteeRequestAssignedActivity extends AppCompatActivity {

    HashMap<String, Object> myRequests;
    View cardview;
    TextView nameText,ratingText, phonenumText,vehicleNumText,dateTimeText;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyftee_request_history);
        cardview = findViewById(R.id.idCardView);
        nameText = cardview.findViewById(R.id.nameTextView);
        ratingText = cardview.findViewById(R.id.ratingTextView);
        phonenumText = cardview.findViewById(R.id.phoneTextView);
        vehicleNumText = cardview.findViewById(R.id.vehicle_num);
        dateTimeText = cardview.findViewById(R.id.dateTimeTextView);


        Button acceptButton = (Button) findViewById(R.id.go_button);
        acceptButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takeToGoogleMaps();
            }
        });
        getLatestAssignedRide();
    }


    private void getLatestAssignedRide() {
        String endPoint = "/api/pool-ride/get-latest-assigned-ride";
//        HashMap<String, Object> dataToBackend = new HashMap<>();
        Http.hit(endPoint, null, new GetLatestAssignedRideResponseHandler());
    }

    private void takeToGoogleMaps() {
        String waypointSourceLat = myRequests.get("pickup_point_lat").toString();
        String wayPointSourceLong = myRequests.get("pickup_point_long").toString();
        String locationString = "geo:" + waypointSourceLat + "," + wayPointSourceLong;

        Uri gmmIntentUri = Uri.parse(locationString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    class GetLatestAssignedRideResponseHandler implements Callback {
        @Override
        public void handleResponse(HashMap<String, Object> response) {

            HashMap<String, Object> lyfterService = (HashMap<String, Object>)response.get("lyfter_service");
            HashMap<String, Object> lyfteeSchedule = (HashMap<String, Object>)response.get("lyftee_schedule");
            HashMap<String, Object> userData = (HashMap<String, Object>)lyfterService.get("user_details");
            String firstName = userData.get("first_name").toString();
            nameText.setText(firstName);
            Log.d("time", lyfteeSchedule.get("scheduled_time").toString());
            dateTimeText.setText(lyfteeSchedule.get("scheduled_time").toString());
            myRequests = response;
        }
    }
}
