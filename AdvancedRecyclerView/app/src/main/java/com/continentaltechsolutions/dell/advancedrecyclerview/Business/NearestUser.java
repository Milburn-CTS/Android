package com.continentaltechsolutions.dell.advancedrecyclerview.Business;

/**
 * Created by DELL on 24-Oct-17.
 */

public class NearestUser implements Comparable<NearestUser> {

    public int UID;
    public String Name;
    public double Distance;
    public NearestUser(int UID, String Name, double Distance) {
        this.UID = UID;
        this.Name = Name;
        this.Distance = Distance;
    }

    public double getDistance() {
        return Distance;
    }
    public String getName() {
        return Name;
    }
    public  int getUID()

    {
        return UID;
    }

    @Override
    public int compareTo(NearestUser nearestuser) {
        return (this.Distance < nearestuser.getDistance() ? -1 :
                (this.getDistance() == nearestuser.getDistance() ? 0 : 1));
    }

}