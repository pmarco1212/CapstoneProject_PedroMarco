package com.capstoneproject.pedromarco.eventapp.createevent;

import com.capstoneproject.pedromarco.eventapp.createevent.event.CreateEventEvent;

/**
 * Interface stating the methods of the createevent presenter
 */
public interface CreateEventPresenter {
    void onCreate();

    void onDestroy();

    void onEventMainThread(CreateEventEvent event);

    void createEvent(String eventName, String description, String category, int maxPeople, String date, String time, String location, double latitude, double longitude, String picturePath);
}
