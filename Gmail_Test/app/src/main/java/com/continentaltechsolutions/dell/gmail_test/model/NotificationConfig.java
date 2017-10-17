package com.continentaltechsolutions.dell.gmail_test.model;

/**
 * Created by DELL on 15-Oct-17.
 */

public class NotificationConfig {
    private int EventID;
    private String EnabledNotifications;
    private String DaysOfWeek;
    private String FromTime;
    private String ToTime;
    private int color = -1;

    public  NotificationConfig(){}

    public int getEventID() {
        return EventID;
    }

    public void setEventID(int EventID) {
        this.EventID = EventID;
    }

    public String getEnabledNotifications() {
        return EnabledNotifications;
    }

    public void setEnabledNotifications(String EnabledNotifications) {
        this.EnabledNotifications = EnabledNotifications;
    }

    public String getDaysOfWeek() {
        int DOW = Integer.parseInt(DaysOfWeek);
        String strDOW = "";

        if ((DOW & 1) == 1)
            strDOW += "SU, ";
        if ((DOW & 2) == 2)
            strDOW += "MO, ";
        if ((DOW & 4) == 4)
            strDOW += "TU, ";
        if ((DOW & 8) == 8)
            strDOW += "WE, ";
        if ((DOW & 16) == 16)
            strDOW += "TH, ";
        if ((DOW & 32) == 32)
            strDOW += "FR, ";
        if ((DOW & 64) == 64)
            strDOW += "SA";
        return strDOW;
    }

    public void setDaysOfWeek(String DaysOfWeek) {
        this.DaysOfWeek = DaysOfWeek;
    }

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String FromTime) {
        this.FromTime = FromTime;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String ToTime) {
        this.ToTime = ToTime;
    }

    public String getFrom() {
        return String.valueOf(EventID);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
