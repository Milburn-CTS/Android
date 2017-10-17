package com.continentaltechsolutions.dell.gmail_test.activity;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.continentaltechsolutions.dell.gmail_test.R;
import com.continentaltechsolutions.dell.gmail_test.helper.TimePickerFragment;
import com.continentaltechsolutions.dell.libmultispinner.MultiSelectionSpinner;

import java.util.List;

public class NotificationConfigActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener, android.view.View.OnClickListener{

    MultiSelectionSpinner mssarrayDaysOfWeek;
    private Spinner spinnerDOW, spinnerEnabledNotifications;
    private TextView tvFromTime, tvToTime;
    private int EventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_config);

        spinnerEnabledNotifications = (Spinner) findViewById(R.id.spinnerEnabledNotifications);
        spinnerDOW = (Spinner) findViewById(R.id.spinnerDaysOfWeek);
        tvFromTime = (TextView) findViewById(R.id.tvFromTime);
        tvToTime = (TextView) findViewById(R.id.tvToTime);
        String[] arrayServiceCategory = {"Sunday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        MultiSelectionSpinner mssarrayServiceCategory = (MultiSelectionSpinner) findViewById(R.id.spinnerServiceCategory);
        mssarrayServiceCategory.setItems(arrayServiceCategory);
        /*if (_reportID != 0) {
            mssarrayDaysOfWeek.setSelection(indexcat);
        }*/
        mssarrayServiceCategory.setListener(this);

        tvFromTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });

        tvToTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public String selectedStrings(List<String> strings) {
        return null;
    }
}
