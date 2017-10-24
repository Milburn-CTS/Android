package com.continentaltechsolutions.dell.advancedrecyclerview.Business;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by DELL on 24-Oct-17.
 */

public class NearestUserSorter {
    ArrayList<NearestUser> nearestUsers = new ArrayList<>();
    public NearestUserSorter(ArrayList<NearestUser> nearestUsers) {
        this.nearestUsers = nearestUsers;
    }
    public ArrayList<NearestUser> getSortedNearestUserByDistance() {
        Collections.sort(nearestUsers);
        return nearestUsers;
    }
}
