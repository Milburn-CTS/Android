package com.continentaltechsolutions.dell.gmail_test.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.continentaltechsolutions.dell.gmail_test.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent int1 = new Intent(getApplicationContext(), GmailActivity.class);
        startActivity(int1);
    }
}
