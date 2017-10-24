package com.continentaltechsolutions.dell.advancedrecyclerview.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.continentaltechsolutions.dell.advancedrecyclerview.Business.Helper;
import com.continentaltechsolutions.dell.advancedrecyclerview.Business.LocationLogMin;
import com.continentaltechsolutions.dell.advancedrecyclerview.Business.NearestUser;
import com.continentaltechsolutions.dell.advancedrecyclerview.Business.NearestUserSorter;
import com.continentaltechsolutions.dell.advancedrecyclerview.Business.TrackGPS;
import com.continentaltechsolutions.dell.advancedrecyclerview.R;
import com.continentaltechsolutions.dell.advancedrecyclerview.Rest.RestLocationLogService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class AttendanceDetails extends AppCompatActivity {

    private double maplatitude, maplongitude;
    private boolean isMockON, mockprovider;
    private String filterValue, Usrname, url;
    private TrackGPS gps;

    private int Usrid;
    public float[] results;
    private EditText filteredittxt, location;
    double Chk_in_longitude, distance;
    double Chk_in_latitude;
    private Helper help, helper;
    private String Chk_in_address;
    // String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6Ik1pbGJ1cm4iLCJuYmYiOjE1MDU5NzkzMDYsImV4cCI6MTUwNTk4MDUwNiwiaWF0IjoxNTA1OTc5MzA2fQ.2L-FUX7lXD63asOJFKPqnKs0fWMc5vsm7wJqAYT6Hyk";
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        help = new Helper(this);
        /*Intent I = getIntent();
        Usrid = I.getIntExtra("usrid", 0);
        Usrname = I.getStringExtra("usrname");*/
        url = getString(R.string.URL); //I.getStringExtra("ip");

        helper = new Helper(this, AttendanceDetails.this);
        token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6InN1ZGhpckBvZHhlbC5jb20iLCJuYmYiOjE1MDc3MjEwMDIsImV4cCI6MTUxNTYwNTAwMiwiaWF0IjoxNTA3NzIxMDAyfQ.R9LgFf8XadRi_LtgkDL-sLXFL3rxeQrLQNWC9UVDkT4";
        final RestLocationLogService restLocationLogService = new RestLocationLogService(url);
        results = new float[1];
        //  filteredittxt = (EditText) findViewById(R.id.filtertxteedit);
        //final TableLayout headertable = (TableLayout) findViewById(R.id.header);
        // location=(EditText)findViewById(R.id.locationedittxt);
        //Button searchbtn = (Button) findViewById(R.id.btnSearch);
        if (!helper.checkPermission()) {
            helper.requestPermission();
        }
        isMockON = help.isMockSettingsON(getApplicationContext());
        gps = new TrackGPS(AttendanceDetails.this);

        if (!gps.canGetLocation()) {
            gps.showSettingsAlert();
        }
        if (!isMockON) {
            try {
                if (!help.isInternetOn()) {
                    Toast.makeText(getApplicationContext(), "No Internet connection detected, Please try again later", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    // final LocationLogMin loclogmin = new LocationLogMin();
                    //  loclogmin.UName = username.getText().toString();
                    // loclogmin.Location_Address = location.getText().toString();
                    setGPSCordinates();
               /* filterValue = filteredittxt.getText().toString();
                if (filterValue.toString().trim().length() == 0) {
                    filterValue = "";
                }*/
                    restLocationLogService.getService().viewmap(token, new Callback<List<LocationLogMin>>() {
                        @Override
                        public void success(List<LocationLogMin> locationLogMins, Response response) {
                            Log.e("in sucess", "of viewmap");
                            ArrayList<NearestUser> NearestUsersList = new ArrayList<>();
                            for (int i = 0; i < locationLogMins.size(); i++) {
                                double lat1 = locationLogMins.get(i).Location_Lat;
                                double long1 = locationLogMins.get(i).Location_Long;
                                Location.distanceBetween(locationLogMins.get(i).Location_Lat, locationLogMins.get(i).Location_Long, Chk_in_latitude, Chk_in_longitude, results);

                                distance = results[0] / 1000;
                                NearestUser nearuser = new NearestUser(Integer.valueOf(locationLogMins.get(i).UID), locationLogMins.get(i).UName.toString(), helper.twodecimalplaces(distance));
                                NearestUsersList.add(nearuser);
                                NearestUserSorter nearestUserSorter = new NearestUserSorter(NearestUsersList);
                                ArrayList<NearestUser> sortedNearestUser = nearestUserSorter.getSortedNearestUserByDistance();
                                //ListView lv = (ListView) findViewById(R.id.MAPlistView);
                                //AttendanceMapAdapter customAdapter = new AttendanceMapAdapter(AttendanceDetails.this, R.layout.activity_attendance_grid, sortedNearestUser);
                                //lv.setAdapter(customAdapter);
                            }


                            // headertable.setVisibility(View.VISIBLE);
                            // GridView gv = (GridView) findViewById(R.id.gridviewforattendance);
                            // AttendanceAdapter customAdapter = new AttendanceAdapter(ViewUserlogDetails.this, R.layout.attendancelistview, locLogListViews);
                            //   gv.setLongClickable(true);

                            //   gv.setAdapter(new AttenGridAdapter(locationLogMins));

                        }

                        public void failure(RetrofitError error) {
                            try {
                                if (((TypedByteArray) error.getResponse().getBody()).getBytes() != null) {
                                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes()); //Sometimes returns null. Chk reqd
                                    Toast.makeText(AttendanceDetails.this, json, Toast.LENGTH_LONG).show();


                                } else if (error.getMessage().toString() != null) {
                                    Toast.makeText(AttendanceDetails.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(AttendanceDetails.this, "Error. Please try again...", Toast.LENGTH_LONG).show();

                                }
                            } catch (Exception e) {

                                Toast.makeText(AttendanceDetails.this, "Error. Please try again...", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                                //network Exception is throw here
                            }
                        }

                    });

        /*    searchbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // final LocationLogMin loclogmin = new LocationLogMin();
                   // loclogmin.UName = username.getText().toString();
                   // loclogmin.Location_Address = location.getText().toString();
                    filterValue=filteredittxt.getText().toString();
                    restLocationLogService.getService().viewmap(filterValue, token, new Callback<List<LocationLogMin>>() {
                        @Override
                        public void success(List<LocationLogMin> locationLogMins, Response response) {
                            GridView gv = (GridView) findViewById(R.id.gridviewforattendance);
                            // AttendanceAdapter customAdapter = new AttendanceAdapter(ViewUserlogDetails.this, R.layout.attendancelistview, locLogListViews);
                            gv.setLongClickable(true);

                            gv.setAdapter(new AttenGridAdapter(locationLogMins));

                        }

                        public void failure(RetrofitError error) {
                            try {
                                if (((TypedByteArray) error.getResponse().getBody()).getBytes() != null) {
                                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes()); //Sometimes returns null. Chk reqd
                                    Toast.makeText(AttendanceDetails.this, json, Toast.LENGTH_LONG).show();


                                } else if (error.getMessage().toString() != null) {
                                    Toast.makeText(AttendanceDetails.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(AttendanceDetails.this, "Error. Please try again...", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e("Login Fail Catch:", e.toString());
                                Toast.makeText(AttendanceDetails.this, "Error. Please try again...", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                                //network Exception is throw here
                            }
                        }

                    });
                }
            });*/
                }
            } catch (Exception e) {
                throw e;
            }
        } else {
            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AttendanceDetails.this);
            alertDialog.setMessage("Mock settings enabled.Kindly disable!!");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    AttendanceDetails.this.finish();
                }
            });
            android.app.AlertDialog alert = alertDialog.create();
            alert.show();
        }
    }

    public void setGPSCordinates() {
        gps = new TrackGPS(AttendanceDetails.this);


        if (gps.canGetLocation())

        {

            //  LocationLog LocLog = new LocationLog();
            // RestLocationLogService restLocationLogService = new RestLocationLogService(url);

            //shared prefs saving datetime
            // helper.savePreferences("datetime", CheckinDate);
            Chk_in_longitude = help.sixdecimalplaces(gps.getLongitude());
            Log.e("long", String.valueOf(Chk_in_longitude));
            Toast.makeText(getApplicationContext(), "Longitude" + String.valueOf(Chk_in_longitude), Toast.LENGTH_SHORT).show();
            Chk_in_latitude = help.sixdecimalplaces(gps.getLatitude());
            Log.e("lat", String.valueOf(Chk_in_latitude));
            Toast.makeText(getApplicationContext(), "Latitude"
                    + String.valueOf(Chk_in_latitude), Toast.LENGTH_SHORT).show();
            Chk_in_address = help.getCompleteAddressString(Chk_in_latitude, Chk_in_longitude);
            Log.e("address", String.valueOf(Chk_in_address));
            Toast.makeText(getApplicationContext(), "Address" + Chk_in_address, Toast.LENGTH_SHORT).show();
        }
    }


}
