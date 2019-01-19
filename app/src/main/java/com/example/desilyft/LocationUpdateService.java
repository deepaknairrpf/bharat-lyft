package com.example.desilyft;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LocationUpdateService extends IntentService {

    final static String UPDATE_LOCATION_INFO = "UPDATE_LOCATION_INFO";
    public LocationUpdateService(String name) {
        super(UPDATE_LOCATION_INFO);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        updateLocationInFirebaseDB();
    }

    private void updateLocationInFirebaseDB() {

    }


}
