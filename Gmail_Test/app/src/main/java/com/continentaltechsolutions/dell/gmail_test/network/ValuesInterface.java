package com.continentaltechsolutions.dell.gmail_test.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by DELL on 23-Oct-17.
 */

public interface ValuesInterface {
    @GET("/api/values/GetValuesRoute")
    Call<ResponseBody> getValues(@Header("Authorization") String authHeader);
}
