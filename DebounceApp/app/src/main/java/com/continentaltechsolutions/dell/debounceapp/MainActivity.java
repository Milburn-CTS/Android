package com.continentaltechsolutions.dell.debounceapp;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btn, btn2;
    private TextView textView;
    // variable to track event time
    private long mLastClickTime = 0;
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);
        btn.setOnClickListener(new DebouncedOnClickListener(5000){
            public void onDebouncedClick(View v){
                Random r = new Random();
                //Toast.makeText(MainActivity.this, "Button Clicked..." + r.nextInt(45 - 28) + 28, Toast.LENGTH_LONG).show();
                int no = r.nextInt(45 - 28) + 28;
                Log.w(TAG, "Here: " + no);
                textView.setText("" + no);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                // Preventing multiple clicks, using threshold of 1 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
                    return;
                }
                else
                {
                    Random r = new Random();
                    int no = r.nextInt(45 - 28) + 28;
                    Log.w(TAG, "Here: " + no);
                    textView.setText("" + no);
                }
                mLastClickTime = SystemClock.elapsedRealtime();
            }
        });

    }
}
