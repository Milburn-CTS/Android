package com.continentaltechsolutions.dell.advancedrecyclerview.Rest;

import android.util.Log;

/**
 * Created by DELL on 24-Oct-17.
 */

public class RestLocationLogService {

    private retrofit.RestAdapter restAdapter;
    private LocationLogService apiService;
    private static final String TAG = RestLocationLogService.class.getSimpleName();

    public RestLocationLogService(String URL) {
        try{
            restAdapter = new retrofit.RestAdapter.Builder()
                    .setEndpoint(URL)
                    .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                    .build();

            apiService = restAdapter.create(LocationLogService.class);
        }
        catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    public LocationLogService getService() {
        return apiService;
    }
}
