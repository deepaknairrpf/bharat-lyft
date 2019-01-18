package com.example.desilyft;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;

/**
 * Created by sriablaze on 18/01/19.
 */

public class Utils {

    public static void getLocationFromPlace(String address, Context context) {
        Geocoder geoCoder = new Geocoder(context);
        if (address != null && !address.isEmpty()) {
            try {
                List<Address> addressList = geoCoder.getFromLocationName(address, 1);
                if (addressList != null && addressList.size() > 0) {
                    double lat = addressList.get(0).getLatitude();
                    double lng = addressList.get(0).getLongitude();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
