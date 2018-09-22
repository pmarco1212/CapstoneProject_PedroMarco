package com.capstoneproject.pedromarco.eventapp.eventdetails;

import com.capstoneproject.pedromarco.eventapp.eventdetails.event.EventDetailsEvent;

/**
 * Interface stating the methods of the presenter
 */
public interface EventDetailsPresenter {
    void onCreate();

    void onDestroy();

    void getEventDetails(String eventID);

    void toogleAssiting(String eventID);

    void setIAmHere(double longitude, double latitude, double currentLongitude, double currentLatitude);

    void rateEvent(float rating, String creator, String eventID);

    void toogleFavourite(String eventID);

    void writeComment(String eventID, String comment);

    void onEventMainThread(EventDetailsEvent event);
}
