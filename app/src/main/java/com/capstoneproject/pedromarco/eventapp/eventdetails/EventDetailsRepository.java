package com.capstoneproject.pedromarco.eventapp.eventdetails;

/**
 * Repository interface stating the methods of the event details repository
 */
public interface EventDetailsRepository {
    void getEventDetails(String eventID);

    void toogleAssiting(String eventID);

    void setIAmHere(double longitude, double latitude, double currentLongitude, double currentLatitude);

    void rateEvent(float rating, String creator, String eventID);

    void toogleFavourite(String eventID);

    void writeComment(String eventID, String comment);
}
