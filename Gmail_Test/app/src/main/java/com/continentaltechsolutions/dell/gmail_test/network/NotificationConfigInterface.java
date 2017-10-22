package com.continentaltechsolutions.dell.gmail_test.network;

import com.continentaltechsolutions.dell.gmail_test.model.NotificationConfig;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by DELL on 15-Oct-17.
 */

public interface NotificationConfigInterface {
    @GET("/api/Device/GetNotificationConfigByDeviceID/")
    Call<List<NotificationConfig>> getNotificationConfig(@QueryMap LinkedHashMap<String, String> params, @Header("Authorization") String authHeader);

    @PUT("/api/Device/PutNotificationConfigByDeviceIDAdd/")
    Call<ResponseBody> putNotificationConfigAdd(@QueryMap LinkedHashMap<String, String> params, @Body NotificationConfig notificationConfig, @Header("Authorization") String authHeader);

    @PUT("/api/Device/PutNotificationConfigByDeviceIDDelete/")
    Call<ResponseBody> putNotificationConfigDelete(@QueryMap LinkedHashMap<String, String> params, @Body List<NotificationConfig> notificationConfigs, @Header("Authorization") String authHeader);

}
