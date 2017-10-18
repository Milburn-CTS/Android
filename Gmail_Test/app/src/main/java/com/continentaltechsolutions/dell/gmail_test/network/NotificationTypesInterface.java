package com.continentaltechsolutions.dell.gmail_test.network;

import com.continentaltechsolutions.dell.gmail_test.model.NotificationConfig;
import com.continentaltechsolutions.dell.gmail_test.model.NotificationTypes;

import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.QueryMap;

/**
 * Created by DELL on 18-Oct-17.
 */

public interface NotificationTypesInterface {
    @GET("/api/NotificationTypes/")
    Call<List<NotificationTypes>> getNotificationTypes(@Header("Authorization") String authHeader);
}
