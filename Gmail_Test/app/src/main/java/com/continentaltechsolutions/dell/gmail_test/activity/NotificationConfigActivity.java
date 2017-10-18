package com.continentaltechsolutions.dell.gmail_test.activity;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.continentaltechsolutions.dell.gmail_test.R;
import com.continentaltechsolutions.dell.gmail_test.helper.Constants;
import com.continentaltechsolutions.dell.gmail_test.helper.TimePickerFragment;
import com.continentaltechsolutions.dell.gmail_test.model.NotificationConfig;
import com.continentaltechsolutions.dell.gmail_test.model.NotificationTypes;
import com.continentaltechsolutions.dell.gmail_test.network.ApiClient;
import com.continentaltechsolutions.dell.gmail_test.network.NotificationConfigInterface;
import com.continentaltechsolutions.dell.gmail_test.network.NotificationTypesInterface;
import com.continentaltechsolutions.dell.libmultispinner.MultiSelectionSpinner;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationConfigActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener, android.view.View.OnClickListener, AdapterView.OnItemSelectedListener{

    MultiSelectionSpinner mssarrayDaysOfWeek;
    private Spinner spinnerDOW, spinnerEnabledNotifications;
    final HashMap<String, String> spinnerMapEnabledNotifications = new HashMap<String, String>();
    private TextView tvFromTime, tvToTime;
    private Button btnAdd;
    private TimePickerFragment mTimePickerFragment;
    private int EventID, counterDOW = 0;
    private String EnabledNotifications, EnabledNotificationsID;
    final Integer[] finalDOWList = {0, 0, 0, 0, 0, 0, 0}; //TODO NOT REQUIRED
    public String result1 = null; //TODO CHANGE TO SENSIBLE NAME

    private static final String TAG = "NotificationConfig";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_config);

        spinnerEnabledNotifications = (Spinner) findViewById(R.id.spinnerEnabledNotifications);
        spinnerDOW = (Spinner) findViewById(R.id.spinnerDaysOfWeek);
        tvFromTime = (TextView) findViewById(R.id.tvFromTime);
        tvToTime = (TextView) findViewById(R.id.tvToTime);
        btnAdd = (Button) findViewById(R.id.buttonAdd);
        String[] arrayDaysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        MultiSelectionSpinner mssarrayServiceCategory = (MultiSelectionSpinner) findViewById(R.id.spinnerDaysOfWeek);
        mssarrayServiceCategory.setItems(arrayDaysOfWeek);
        mssarrayServiceCategory.setListener(this);

        //Receiving from Intent
        EventID = 4;

        tvFromTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mTimePickerFragment = new TimePickerFragment();
                mTimePickerFragment.setFlag(TimePickerFragment.FLAG_FROM_TIME);
                mTimePickerFragment.show(getFragmentManager(),"TimePicker");
            }
        });

        tvToTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mTimePickerFragment = new TimePickerFragment();
                mTimePickerFragment.setFlag(TimePickerFragment.FLAG_TO_TIME);
                mTimePickerFragment.show(getFragmentManager(),"TimePicker");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationConfig notificationConfig = new NotificationConfig();

                //Check Validations
                if (validateNotificationConfig()) {
                    return;
                } else {
                    //Check Existing Time Conflicts and From Time To Time conflicts
                    if(!true){

                        return;
                    }
                    else {
                        retrieveDOW();

                        //Get EventID
                        notificationConfig.setEventID(EventID);
                        //Get EnabledNotifications
                        notificationConfig.setEnabledNotifications(spinnerEnabledNotifications.getSelectedItem().toString());
                        //Get DaysOfWeek
                        notificationConfig.setDaysOfWeek(String.valueOf(counterDOW)); //TODO Convert to Int
                        //Get FromTime
                        notificationConfig.setFromTime(tvFromTime.getText().toString());
                        //Get ToTime
                        notificationConfig.setToTime(tvToTime.getText().toString());
                    }
                }
            }
        });

        populateNotificationTypes();
    }

    private Boolean validateNotificationConfig(){
        boolean done = false;
        //DaysOfWeek
        if (TextUtils.isEmpty(result1)) {
            View selectedViewS2 = spinnerDOW.getSelectedView();
            if (selectedViewS2 != null && selectedViewS2 instanceof TextView) {
                TextView selectedTextViewS2 = (TextView) selectedViewS2;
                done = true;
                Toast.makeText(NotificationConfigActivity.this, "Please Select atleast one day of the week", Toast.LENGTH_LONG).show();
                selectedTextViewS2.setError("Please Select atleast one day of the week");
            }
        }

        //EnabledNotifications
        View selectedView = spinnerEnabledNotifications.getSelectedView();
        if (spinnerEnabledNotifications.getSelectedItem().toString().equals("Please Select Notification Type...")) {
            TextView selectedTextView = (TextView) selectedView;
            done = true;
            Toast.makeText(NotificationConfigActivity.this, "Please Select Notification Type...", Toast.LENGTH_LONG).show();
            selectedTextView.setError("Please Select Notification Type...");
        }

        //FromTime
        if (tvFromTime.getText().toString().length() == 0) {
            done = true;
            Toast.makeText(getApplicationContext(), "Please Set From Time", Toast.LENGTH_LONG).show();
            tvFromTime.setError("Please Set From Time");
        }

        //ToTime
        if (tvToTime.getText().toString().length() == 0) {
            done = true;
            Toast.makeText(getApplicationContext(), "Please Set To Time", Toast.LENGTH_LONG).show();
            tvToTime.setError("Please Set To Time");
        }

        return done;
    }

    private void retrieveDOW() {
        EnabledNotifications = spinnerEnabledNotifications.getSelectedItem().toString();  //TODO NOT REQUIRED
        EnabledNotificationsID = spinnerMapEnabledNotifications.get(EnabledNotifications);
        if (spinnerEnabledNotifications.getSelectedItem().toString() != "Please Select Notification Type...") {

        }
        if (result1 != null) {
            String[] Result1Array = result1.split(",");
            for (int i = 0; i < Result1Array.length; i++) {
                switch (Result1Array[i].trim())//remove white space
                {//SETTING value to 0 or 1 if selected from spinner
                    case "Sunday":
                        finalDOWList[0] = 1;
                        counterDOW += 1;
                        break;
                    case "Monday":
                        finalDOWList[1] = 1;
                        counterDOW += 2;
                        break;
                    case "Tuesday":
                        finalDOWList[2] = 1;
                        counterDOW += 4;
                        break;
                    case "Wednesday":
                        finalDOWList[3] = 1;
                        counterDOW += 8;
                        break;
                    case "Thursday":
                        finalDOWList[4] = 1;
                        counterDOW += 16;
                        break;
                    case "Friday":
                        finalDOWList[5] = 1;
                        counterDOW += 32;
                        break;
                    case "Saturday":
                        finalDOWList[6] = 1;
                        counterDOW += 64;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void populateNotificationTypes() {
        //TODO: Add ProgressBar
        NotificationTypesInterface apiService =
                ApiClient.getClient().create(NotificationTypesInterface.class);

        Call<List<NotificationTypes>> call = apiService.getNotificationTypes(Constants.JWT_TOKEN);
        call.enqueue(new Callback<List<NotificationTypes>>() {
            @Override
            public void onResponse(Call<List<NotificationTypes>> call, Response<List<NotificationTypes>> response) {

                if(response.body() != null){
                    String[] spinnerArrayEnabledNotifications = new String[response.body().size() + 1];
                    Log.e(TAG, "NotificationTypes.size() " + String.valueOf(response.body().size()));
                    Log.e(TAG, "spinnerArrayEnabledNotifications.length " + String.valueOf(spinnerArrayEnabledNotifications.length));
                    spinnerMapEnabledNotifications.put("Please Select Notification Type...", "0");
                    spinnerArrayEnabledNotifications[0] = "Please Select Notification Type...";
                    Log.e(TAG, "spinnerArray[0] " + spinnerArrayEnabledNotifications[0]);
                    for (NotificationTypes notificationTypes : response.body()) {

                        //notificationConfigList.add(notificationConfig);
                        for (int i = 0; i < response.body().size(); i++) {
                            spinnerMapEnabledNotifications.put(response.body().get(i).NotificationType.toString(), String.valueOf(response.body().get(i).ID));
                            spinnerArrayEnabledNotifications[i + 1] = response.body().get(i).NotificationType.toString();
                            Log.e(TAG, "spinnerArray[" + i + "]" + spinnerArrayEnabledNotifications[i]);
                        }

                        ArrayAdapter<String> dataAdapterClients = new ArrayAdapter<String>(NotificationConfigActivity.this, android.R.layout.simple_list_item_1, spinnerArrayEnabledNotifications);
                        dataAdapterClients.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                        spinnerEnabledNotifications.setAdapter(dataAdapterClients);

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not Found... Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotificationTypes>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        spinnerEnabledNotifications.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void selectedIndices(List<Integer> indices) {
        List<Integer> index = indices;
    }

    @Override
    public String selectedStrings(List<String> strings) {
        List<String> Newstrings = strings;

        String result = ("" + strings.toString().
                replaceAll("(^.|.$)", "  ").replace(", ", "  , "));

        result1 = result;
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
