package com.capstoneproject.pedromarco.eventapp.createevent;

/**
 * Interactor interface of the  create event
 */
public interface CreateEventInteractor {
    void execute(String eventName, String description, String category, int maxPeople, String date, String time, String location, double latitude, double longitude, String picturePath);
}
