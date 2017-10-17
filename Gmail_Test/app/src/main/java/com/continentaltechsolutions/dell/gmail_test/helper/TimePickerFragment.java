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

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        TextView tv = (TextView) getActivity().findViewById(R.id.tv);
        //Set a message for user
        tv.setText("Your chosen time is...\n\n");
        //Display the user changed time on TextView
        tv.setText(tv.getText()+ "Hour : " + String.valueOf(hourOfDay)
                + "\nMinute : " + String.valueOf(minute) + "\n");
    }
}
