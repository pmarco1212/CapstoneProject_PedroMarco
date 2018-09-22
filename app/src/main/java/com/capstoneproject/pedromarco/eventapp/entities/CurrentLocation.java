package com.capstoneproject.pedromarco.eventapp.entities;

/**
 * Class with static "global" variables to store the current location of the user
 * contains the Latitude and the Longitude
 */
public class CurrentLocation {
    private static double latitude = 0.0;
    private static double longitude = 0.0;

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        CurrentLocation.longitude = longitude;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        CurrentLocation.latitude = latitude;
    }
}
