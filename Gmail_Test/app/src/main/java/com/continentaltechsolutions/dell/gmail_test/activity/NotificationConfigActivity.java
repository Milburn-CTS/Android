package com.continentaltechsolutions.dell.gmail_test.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//CheckedTextView Help taken from http://www.wingnity.com/blog/use-checkedtextview-android/, http://abhiandroid.com/ui/checkedtextview

public class NotificationConfigActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener, android.view.View.OnClickListener, AdapterView.OnItemSelectedListener {

    private List<NotificationConfig> notificationConfigList = new ArrayList<>();
    MultiSelectionSpinner mssarrayDaysOfWeek;
    private Spinner spinnerDOW, spinnerEnabledNotifications;
    final HashMap<String, String> spinnerMapEnabledNotifications = new HashMap<String, String>();
    private TextView tvFromTime, tvToTime;
    private Button btnAdd;
    private TimePickerFragment mTimePickerFragment;
    private int EventID, counterDOW = 0;
    private String selectedDOW = null, incomingstrDOW = null;
    private View mProgressView;
    private View mNotificationConfigFormView;
    private CheckedTextView checkedTextView;

    private static final String TAG = "NotificationConfig";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_config);

        mNotificationConfigFormView = findViewById(R.id.notification_config_form);
        mProgressView = findViewById(R.id.notification_config_progress);
        spinnerEnabledNotifications = (Spinner) findViewById(R.id.spinnerEnabledNotifications);
        spinnerDOW = (Spinner) findViewById(R.id.spinnerDaysOfWeek);
        tvFromTime = (TextView) findViewById(R.id.tvFromTime);
        tvToTime = (TextView) findViewById(R.id.tvToTime);
        btnAdd = (Button) findViewById(R.id.buttonAdd);
        checkedTextView = (CheckedTextView)findViewById(R.id.checkedTextView);
        checkedTextView.setCheckMarkDrawable(null);
        checkedTextView.setChecked(false);

        String[] arrayDaysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        MultiSelectionSpinner mssarrayServiceCategory = (MultiSelectionSpinner) findViewById(R.id.spinnerDaysOfWeek);
        mssarrayServiceCategory.setItems(arrayDaysOfWeek);
        mssarrayServiceCategory.setListener(this);

        //Receiving from Intent
        Intent i = getIntent();
        EventID = i.getIntExtra("EventID", 0);
        if (EventID == 0) {
            Toast.makeText(getApplicationContext(), "Previous page did not load correctly. Please wait till the page completely loads and then Add again", Toast.LENGTH_LONG).show();
            finish();
        }
        notificationConfigList = (List<NotificationConfig>) i.getSerializableExtra("NotificationConfigList");

        tvFromTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mTimePickerFragment = new TimePickerFragment();
                mTimePickerFragment.setFlag(TimePickerFragment.FLAG_FROM_TIME);
                mTimePickerFragment.show(getFragmentManager(), "TimePicker");
            }
        });

        tvToTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mTimePickerFragment = new TimePickerFragment();
                mTimePickerFragment.setFlag(TimePickerFragment.FLAG_TO_TIME);
                mTimePickerFragment.show(getFragmentManager(), "TimePicker");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check Validations
                if (validateNotificationConfig()) {
                    return;
                } else {
                    retrieveDOW();
                    //Check Existing Time Conflicts and From Time To Time conflicts
                    //Check From Time To Time conflicts
                    try {
                        String incomingstrFromTime = tvFromTime.getText().toString();
                        String incomingstrToTime = tvToTime.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        //sdf.setTimeZone(TimeZone.getTimeZone("IST"));
                        Date incomingFromTime = sdf.parse(incomingstrFromTime);
                        Date incomingToTime = sdf.parse(incomingstrToTime);
                        if (incomingFromTime.after(incomingToTime)) {
                            // Start time cannot be greater than or equal to end time
                            Toast.makeText(getApplicationContext(), "From time cannot be greater than or equal to To time", Toast.LENGTH_LONG).show();
                            tvToTime.setError("From time cannot be greater than or equal to To time");
                            return;
                        }

                        if (incomingFromTime.before(incomingToTime)) {
                            // Continue and Check for Existing Time Conflicts
                            for (NotificationConfig nc : notificationConfigList) {
                                if (nc.getEnabledNotifications().equalsIgnoreCase(spinnerEnabledNotifications.getSelectedItem().toString())) { //Check for EnabledNotifications eg. APP, EMAIL etc
                                    //Check for DOW
                                    String existingstrdow = nc.getDaysOfWeek();
                                    String[] existingdowList = existingstrdow.split("\\s*,\\s*");
                                    String[] incomingdowList = incomingstrDOW.split("\\s*,\\s*");
                                    for (String edow : existingdowList) {
                                        for (String idow : incomingdowList) {

                                            if (edow.equalsIgnoreCase(idow)) {
                                                //Toast.makeText(getApplicationContext(), "Contains" + _dow, Toast.LENGTH_LONG).show();
                                                String existingstrFromTime = nc.getFromTime();
                                                String existingstrToTime = nc.getToTime();
                                                Date existingFromTime = sdf.parse(existingstrFromTime);
                                                Date existingToTime = sdf.parse(existingstrToTime);

                                                if (incomingFromTime.after(existingFromTime) & incomingFromTime.after(existingToTime)) {
                                                    Toast.makeText(getApplicationContext(), "Conflict in existing time. Please try with different time", Toast.LENGTH_LONG).show();
                                                    tvFromTime.setError("Conflict in existing time. Please try with different time");
                                                    return;
                                                }
                                                if (incomingToTime.after(existingFromTime) & incomingToTime.after(existingToTime)) {
                                                    Toast.makeText(getApplicationContext(), "Conflict in existing time. Please try with different time", Toast.LENGTH_LONG).show();
                                                    tvFromTime.setError("Conflict in existing time. Please try with different time");
                                                    return;
                                                }
                                                if (existingFromTime.after(incomingFromTime) & existingFromTime.after(incomingToTime)) {
                                                    Toast.makeText(getApplicationContext(), "Conflict in existing time. Please try with different time", Toast.LENGTH_LONG).show();
                                                    tvToTime.setError("Conflict in existing time. Please try with different time");
                                                    return;
                                                }
                                                if (existingToTime.after(incomingFromTime) & existingToTime.after(incomingToTime)) {
                                                    Toast.makeText(getApplicationContext(), "Conflict in existing time. Please try with different time", Toast.LENGTH_LONG).show();
                                                    tvToTime.setError("Conflict in existing time. Please try with different time");
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (incomingFromTime.equals(incomingToTime)) {
                            // Start time cannot be equal to end time
                            Toast.makeText(getApplicationContext(), "From time cannot be equal to To time", Toast.LENGTH_LONG).show();
                            tvToTime.setError("From time cannot be equal to To time");
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Exception: " + e, Toast.LENGTH_LONG).show();
                    }
                    if (!true) {

                        return;
                    } else {
                        //Toast.makeText(getApplicationContext(), "Continue....", Toast.LENGTH_LONG).show();
                        NotificationConfig notificationConfig = new NotificationConfig();

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

                        addNotificationConfig(notificationConfig);
                    }
                }
            }
        });

        populateNotificationTypes();

        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedTextView.isChecked()) {
                    checkedTextView.setCheckMarkDrawable(null);
                    checkedTextView.setChecked(false);
                    checkedTextView.setText("Click here to select All Day");
                    tvFromTime.setText("");
                    tvFromTime.setEnabled(true);
                    tvToTime.setText("");
                    tvToTime.setEnabled(true);
                }
                else {
                    checkedTextView.setCheckMarkDrawable(R.drawable.ic_add_black_24dp);
                    checkedTextView.setChecked(true);
                    checkedTextView.setText("All Day Selected");
                    tvFromTime.setText("12:00:00");
                    tvFromTime.setEnabled(false);
                    tvToTime.setText("23:59:59");
                    tvToTime.setEnabled(false);
                }
            }
        });

    }

    //Rest Call to add notificationConfig
    private void addNotificationConfig(NotificationConfig notificationConfig) {
        showProgress(true);
        NotificationConfigInterface apiService =
                ApiClient.getClient().create(NotificationConfigInterface.class);

        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("id", String.valueOf(Constants.DEVICE_ID));
        Call<ResponseBody> call = apiService.putNotificationConfigAdd(params, notificationConfig, Constants.JWT_TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    showProgress(false);
                    Toast.makeText(getApplicationContext(), response.body().string(), Toast.LENGTH_LONG).show(); //"Added Notification Config Successfully"
                    finish();
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Falied to Add Notification Config. Please try again. " + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private Boolean validateNotificationConfig() {
        boolean done = false;

        //EnabledNotifications
        View selectedViewEnabledNotifications = spinnerEnabledNotifications.getSelectedView();
        if (spinnerEnabledNotifications.getSelectedItem().toString().equals("Please Select Notification Type...")) {
            TextView selectedTextView = (TextView) selectedViewEnabledNotifications;
            done = true;
            Toast.makeText(NotificationConfigActivity.this, "Please Select Notification Type...", Toast.LENGTH_LONG).show();
            selectedTextView.setError("Please Select Notification Type...");
        }

        //DaysOfWeek
        if (TextUtils.isEmpty(selectedDOW)) {
            View selectedViewDOW = spinnerDOW.getSelectedView();
            if (selectedViewDOW != null && selectedViewDOW instanceof TextView) {
                TextView selectedTextViewS2 = (TextView) selectedViewDOW;
                done = true;
                Toast.makeText(NotificationConfigActivity.this, "Please Select atleast one day of the week", Toast.LENGTH_LONG).show();
                selectedTextViewS2.setError("Please Select atleast one day of the week");
            }
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
        if (selectedDOW != null) {
            String[] Result1Array = selectedDOW.split(",");
            incomingstrDOW = "";
            for (int i = 0; i < Result1Array.length; i++) {
                switch (Result1Array[i].trim())//remove white space
                {//SETTING value to 0 or 1 if selected from spinner
                    case "Sunday":
                        incomingstrDOW += "SU, ";
                        counterDOW += 1;
                        break;
                    case "Monday":
                        incomingstrDOW += "MO, ";
                        counterDOW += 2;
                        break;
                    case "Tuesday":
                        incomingstrDOW += "TU, ";
                        counterDOW += 4;
                        break;
                    case "Wednesday":
                        incomingstrDOW += "WE, ";
                        counterDOW += 8;
                        break;
                    case "Thursday":
                        incomingstrDOW += "TH, ";
                        counterDOW += 16;
                        break;
                    case "Friday":
                        incomingstrDOW += "FR, ";
                        counterDOW += 32;
                        break;
                    case "Saturday":
                        incomingstrDOW += "SA, "; //Comma not required
                        counterDOW += 64;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void populateNotificationTypes() {
        //TODO: Filter SMS
        showProgress(true);
        NotificationTypesInterface apiService =
                ApiClient.getClient().create(NotificationTypesInterface.class);

        Call<List<NotificationTypes>> call = apiService.getNotificationTypes(Constants.JWT_TOKEN);
        call.enqueue(new Callback<List<NotificationTypes>>() {
            @Override
            public void onResponse(Call<List<NotificationTypes>> call, Response<List<NotificationTypes>> response) {

                if (response.body() != null) {
                    String[] spinnerArrayEnabledNotifications = new String[response.body().size() + 1];
                    Log.w(TAG, "NotificationTypes.size() " + String.valueOf(response.body().size()));
                    Log.w(TAG, "spinnerArrayEnabledNotifications.length " + String.valueOf(spinnerArrayEnabledNotifications.length));
                    spinnerMapEnabledNotifications.put("Please Select Notification Type...", "0");
                    spinnerArrayEnabledNotifications[0] = "Please Select Notification Type...";
                    Log.w(TAG, "spinnerArray[0] " + spinnerArrayEnabledNotifications[0]);
                    for (NotificationTypes notificationTypes : response.body()) {
                        //notificationConfigList.add(notificationConfig);
                        for (int i = 0; i < response.body().size(); i++) {
                            spinnerMapEnabledNotifications.put(response.body().get(i).NotificationType.toString(), String.valueOf(response.body().get(i).ID));
                            spinnerArrayEnabledNotifications[i + 1] = response.body().get(i).NotificationType.toString();
                            Log.w(TAG, "spinnerArray[" + i + "]" + spinnerArrayEnabledNotifications[i]);
                        }

                        ArrayAdapter<String> dataAdapterClients = new ArrayAdapter<String>(NotificationConfigActivity.this, android.R.layout.simple_list_item_1, spinnerArrayEnabledNotifications);
                        dataAdapterClients.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                        spinnerEnabledNotifications.setAdapter(dataAdapterClients);
                        showProgress(false);
                    }
                } else {
                    showProgress(false);
                    Toast.makeText(getApplicationContext(), "Notification Types not found... Please try again", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<NotificationTypes>> call, Throwable t) {
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
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
        String result = ("" + strings.toString().
                replaceAll("(^.|.$)", "  ").replace(", ", "  , "));

        selectedDOW = result;
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Shows the progress UI and hides the Notification Config form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mNotificationConfigFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
