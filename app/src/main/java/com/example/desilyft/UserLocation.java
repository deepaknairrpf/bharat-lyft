package com.example.desilyft;

import android.view.ViewDebug;

/**
 * Created by sriablaze on 18/01/19.
 */

public class UserLocation {

    private Double latitude;
    private Double longitude;

    public UserLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }
}
