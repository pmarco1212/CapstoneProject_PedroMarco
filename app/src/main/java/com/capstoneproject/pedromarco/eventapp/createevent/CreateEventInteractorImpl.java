package com.capstoneproject.pedromarco.eventapp.createevent;

/**
 * Interactor class executing the requests in the repository. Implements the interactor interface.
 */
public class CreateEventInteractorImpl implements CreateEventInteractor {
    CreateEventRepository repository;

    public CreateEventInteractorImpl(CreateEventRepository repository) {
        this.repository = repository;
    }

    /**
     * Execute the method of the repository to create the event
     *
     * @param eventName
     * @param description
     * @param category
     * @param maxPeople
     * @param date
     * @param time
     * @param location
     * @param latitude
     * @param longitude
     * @param picturePath
     */
    @Override
    public void execute(String eventName, String description, String category, int maxPeople, String date, String time, String location, double latitude, double longitude, String picturePath) {
        repository.createEvent(eventName, description, category, maxPeople, date, time, location, latitude, longitude, picturePath);
    }
}
