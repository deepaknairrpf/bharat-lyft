package com.example.desilyft;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class LyfteeRequestAssignedActivity extends AppCompatActivity {

    View cardview;
    TextView nameText,ratingText,phonenumText,vehicleNumText,dateTimeText;
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


        getLatestAssignedRide();
    }


    private void getLatestAssignedRide() {
        String endPoint = "/api/token/";
        HashMap<String, Object> dataToBackend = new HashMap<>();
        Http.hit(endPoint, null, new GetLatestAssignedRideResponseHandler());
    }

    class GetLatestAssignedRideResponseHandler implements Callback {
        @Override
        public void handleResponse(HashMap<String, Object> response) {

            Toast.makeText(getApplicationContext(),"dummpy text",Toast.LENGTH_LONG).show();
        }
    }
}
