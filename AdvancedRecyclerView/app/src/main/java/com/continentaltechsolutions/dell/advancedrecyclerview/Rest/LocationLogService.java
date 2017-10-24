package com.continentaltechsolutions.dell.advancedrecyclerview.Rest;

import com.continentaltechsolutions.dell.advancedrecyclerview.Business.LocationLog;
import com.continentaltechsolutions.dell.advancedrecyclerview.Business.LocationLogMin;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by DELL on 24-Oct-17.
 */

public interface LocationLogService {

    @GET("/api/LocationLog/")
    public void viewmap(@Header("Authorization") String authHeader,Callback<List<LocationLogMin>> callback);

}
