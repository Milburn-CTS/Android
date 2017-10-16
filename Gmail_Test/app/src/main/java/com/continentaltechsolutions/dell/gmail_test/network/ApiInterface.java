package com.continentaltechsolutions.dell.gmail_test.network;

import com.continentaltechsolutions.dell.gmail_test.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by DELL on 14-Oct-17.
 */

public interface ApiInterface {
    @GET("inbox.json")
    Call<List<Message>> getInbox();
}
