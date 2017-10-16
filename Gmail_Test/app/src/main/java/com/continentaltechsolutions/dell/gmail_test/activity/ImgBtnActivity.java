package com.continentaltechsolutions.dell.gmail_test.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.continentaltechsolutions.dell.gmail_test.R;
import com.continentaltechsolutions.dell.gmail_test.model.NotificationConfig;
import com.continentaltechsolutions.dell.gmail_test.network.ApiClient;
import com.continentaltechsolutions.dell.gmail_test.network.NotificationConfigInterface;
import com.continentaltechsolutions.dell.gmail_test.network.VersionInterface;

import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImgBtnActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_btn);

        getVersion();
    }

    private void getVersion() {
        VersionInterface apiService =
                ApiClient.getClient().create(VersionInterface.class);
        Call<Double> call = apiService.getVersion();
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                Toast.makeText(getApplicationContext(), "Recevd Data", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
