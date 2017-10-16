package com.continentaltechsolutions.dell.gmail_test.network;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by DELL on 15-Oct-17.
 */

public interface VersionInterface {
    @GET("/api/version/")
    Call<Double> getVersion();
}
