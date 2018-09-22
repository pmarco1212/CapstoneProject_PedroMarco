package com.capstoneproject.pedromarco.eventapp.eventdetails;

/**
 * Interactor interface of the event details
 */
public interface EventDetailsInteractor {
    void executegetEventDetails(String eventID);

    void executeToogleAssiting(String eventID);

    void executeSetIAmHere(double longitude, double latitude, double currentLongitude, double currentLatitude);

    void executeRateEvent(float rating, String creator, String eventID);

    void executeToogleFavourite(String eventID);

    void executeWriteComment(String eventID, String comment);
}
