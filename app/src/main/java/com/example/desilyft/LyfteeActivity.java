package com.example.desilyft;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class LyfteeActivity extends AppCompatActivity {

    CardView btnDatePicker, btnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int currentYear, currentMonth, currentDate, currentHour, currentMin;
    Button scheduleButton;
    Place sourcePlace, destPlace;
    TextView txtDate,txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyftee);

        getSupportActionBar().hide();
        txtDate=(TextView) findViewById(R.id.dateText);
        txtTime=(TextView) findViewById(R.id.timeText);
        scheduleButton = (Button)findViewById(R.id.schedule_button);

        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDate = c.get(Calendar.DATE);
        currentHour = c.get(Calendar.HOUR);
        currentMin = c.get(Calendar.MINUTE);
        txtDate.setText(currentDate + "-" + (currentMonth + 1) + "-" + currentYear);
        txtTime.setText(currentHour + ":" + currentMin);
        btnDatePicker=(CardView) findViewById(R.id.idCardView_date);
        btnTimePicker=(CardView) findViewById(R.id.idCardView_time);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LyfteeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(LyfteeActivity.this ,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String hour_str = String.format("%02d", hourOfDay);
                                String minute_str = String.format("%02d", minute);
                                txtTime.setText(hour_str + ":" + minute_str);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        PlaceAutocompleteFragment autocompleteFragmentSource = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_source);

        /*AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        autocompleteFragment.setFilter(filter);*/
        autocompleteFragmentSource.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                sourcePlace = place;
                Toast.makeText(getApplicationContext(),"Source Set to "+place.getName(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Status status) {
                Log.d("DEBUG",status.toString());
            }
        });

        ((EditText)autocompleteFragmentSource.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Source");


        PlaceAutocompleteFragment autocompleteFragmentDestination = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_destination);

        /*AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        autocompleteFragment.setFilter(filter);*/
        autocompleteFragmentDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destPlace = place;
                Toast.makeText(getApplicationContext(),"Source Set to "+place.getName(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Status status) {
                Log.d("DEBUG",status.toString());
            }
        });

        ((EditText)autocompleteFragmentDestination.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Destination");


        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendScheduleRequest();
            }
        });
    }

    private void sendScheduleRequest() {
        String endPoint = "/api/lyftee-schedules";

        String datetime = txtDate.getText().toString() + " "+txtTime.getText().toString();
        HashMap<String, Object> dataToBackend = new HashMap<>();
        dataToBackend.put("source_lat", sourcePlace.getLatLng().latitude);
        dataToBackend.put("source_long", sourcePlace.getLatLng().longitude);
        dataToBackend.put("destination_lat", destPlace.getLatLng().latitude);
        dataToBackend.put("destination_long", destPlace.getLatLng().longitude);
        dataToBackend.put("scheduled_time", datetime);

        Http.hit(endPoint, new JSONObject(dataToBackend), new LyfteeScheduleResponseHandler());
    }


    class LyfteeScheduleResponseHandler implements Callback {
        @Override
        public void handleResponse(HashMap<String, Object> response) {
            Toast.makeText(getApplicationContext(),"Response : " + response.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
