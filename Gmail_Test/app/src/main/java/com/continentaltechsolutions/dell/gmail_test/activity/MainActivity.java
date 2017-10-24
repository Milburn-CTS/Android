package com.continentaltechsolutions.dell.gmail_test.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.continentaltechsolutions.dell.gmail_test.R;
import com.continentaltechsolutions.dell.gmail_test.helper.Constants;
import com.continentaltechsolutions.dell.gmail_test.model.NotificationConfig;
import com.continentaltechsolutions.dell.gmail_test.network.ApiClient;
import com.continentaltechsolutions.dell.gmail_test.network.NotificationConfigInterface;
import com.continentaltechsolutions.dell.gmail_test.network.ValuesInterface;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent int1 = new Intent(getApplicationContext(), GmailActivity.class);
        //startActivity(int1);
        getValues();
    }

    /**
     * Fetches mail NotificationConfig by making HTTP request
     */
    private void getValues() {

        ValuesInterface apiService =
                ApiClient.getClient().create(ValuesInterface.class);


        Call<ResponseBody> call = apiService.getValues(Constants.JWT_TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // TODO - avoid looping
                // the loop was performed to add colors to each message
                /*if (response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Response Body Null", Toast.LENGTH_LONG).show();
                }*/

                if (response.isSuccessful()) {
                    // Do awesome stuff
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    // Handle unauthorized
                    Toast.makeText(getApplicationContext(), "Unauthorized :(", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    // Handle Internal Server Error
                    Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
                } else {
                    // Handle other responses
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
