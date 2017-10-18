package com.continentaltechsolutions.dell.gmail_test.helper;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.continentaltechsolutions.dell.gmail_test.R;

import java.util.Calendar;

/**
 * Created by DELL on 16-Oct-17.
 */
//Help for Time taken from https://android--examples.blogspot.in/2015/04/timepickerdialog-in-android.html
//Help for Date taken from https://android--code.blogspot.in/2015/08/android-datepickerdialog-example.html
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    public static final int FLAG_FROM_TIME = 0;
    public static final int FLAG_TO_TIME = 1;
    private int flag = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void setFlag(int i) {
        flag = i;
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        //TextView tv = (TextView) getActivity().findViewById(R.id.tv);
        //Set a message for user
        //tv.setText("Your chosen time is...\n\n");
        //Display the user changed time on TextView
        //tv.setText(tv.getText()+ "Hour : " + String.valueOf(hourOfDay)+ "\nMinute : " + String.valueOf(minute) + "\n");

        String hr, min;
        if(hourOfDay < 10)
            hr = "0" + hourOfDay;
        else
            hr = String.valueOf(hourOfDay);

        if(minute < 10)
            min = "0" + minute;
        else
            min = String.valueOf(minute);

        if (flag == FLAG_FROM_TIME) {
            TextView editTextFromTime = (TextView) getActivity().findViewById(R.id.tvFromTime);
            editTextFromTime.setText(hr + " : " + min);
        } else if (flag == FLAG_TO_TIME) {
            TextView editTextToTime = (TextView) getActivity().findViewById(R.id.tvToTime);
            editTextToTime.setText(hr + " : " + min);
        }
    }
}
