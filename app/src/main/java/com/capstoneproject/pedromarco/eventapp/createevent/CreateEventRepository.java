package com.capstoneproject.pedromarco.eventapp.createevent;

/**
 * Repository interface stating the methods of the repository
 */
public interface CreateEventRepository {
    void createEvent(String eventName, String description, String category, int maxPeople, String date, String time, String location, double latitude, double longitude, String picturePath);
}
