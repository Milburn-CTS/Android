package com.continentaltechsolutions.dell.advancedrecyclerview.Business;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by DELL on 14-Oct-17.
 */

public class Helper {
    Context mContext;

    public Helper(Context mContext) {
        this.mContext = mContext;
    }

    public Activity activity;
    Activity mActivity;
    private static final int PERMISSION_REQUEST_CODE = 1;

    public final boolean isInternetOn() {
        // this.activity=act;
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public final boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        if (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
            return false;
        else
            return true;
    }
    public final double sixdecimalplaces(double value)
    {
        DecimalFormat newFormat= new DecimalFormat("#.######");
        double twoDecimal =  Double.valueOf(newFormat.format(value));
        return twoDecimal;
    }

    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {

        String strAdd = "";
        // String str_array[]=new int char[];
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("loction address", "" + strReturnedAddress.toString());
            } else {
                Log.e("loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("loction address", "Canont get Address!");
        }
        String address_array[] = strAdd.split(",");
        return address_array[0];
    }


    public Helper(Context context, Activity mActivity) {
        this.mContext = context;
        this.mActivity = mActivity;
    }



    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    public void requestPermission(){


        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.ACCESS_FINE_LOCATION)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
            alertDialog.setMessage("GPS permission allows us to access location data. Please allow in App Settings for additional functionality!!");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mActivity.finish();
                }
            });
            AlertDialog alert = alertDialog.create();
        } else {

            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    public static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public final double twodecimalplaces(double value)
    {
        DecimalFormat newFormat= new DecimalFormat("#.##");
        double twoDecimal =  Double.valueOf(newFormat.format(value));
        return twoDecimal;
    }
}
