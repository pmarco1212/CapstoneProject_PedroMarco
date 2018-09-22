package com.capstoneproject.pedromarco.eventapp.eventdetails;

/**
 * Interactor class executing the requests in the repository. Implements the interactor interface.
 */
public class EventDetailsInteractorImpl implements EventDetailsInteractor {
    EventDetailsRepository repository;

    public EventDetailsInteractorImpl(EventDetailsRepository repository) {
        this.repository = repository;
    }

    /**
     * execute the get details in the repository
     *
     * @param eventID event ID
     */
    @Override
    public void executegetEventDetails(String eventID) {
        repository.getEventDetails(eventID);
    }

    /**
     * execute the toogle assisting in the repository
     *
     * @param eventID evnet id
     */
    @Override
    public void executeToogleAssiting(String eventID) {
        repository.toogleAssiting(eventID);
    }

    /**
     * execute the Im here in the repository
     *
     * @param longitude
     * @param latitude
     * @param currentLongitude
     * @param currentLatitude
     */
    @Override
    public void executeSetIAmHere(double longitude, double latitude, double currentLongitude, double currentLatitude) {
        repository.setIAmHere(longitude, latitude, currentLongitude, currentLatitude);
    }

    /**
     * Execute the event rating in the repository
     *
     * @param rating
     * @param creator
     * @param eventID
     */
    @Override
    public void executeRateEvent(float rating, String creator, String eventID) {
        repository.rateEvent(rating, creator, eventID);
    }

    /**
     * Execute the toogle rating in the repository
     *
     * @param eventID
     */
    @Override
    public void executeToogleFavourite(String eventID) {
        repository.toogleFavourite(eventID);
    }

    /**
     * Execute a write comment in the repository
     *
     * @param eventID
     * @param comment
     */
    @Override
    public void executeWriteComment(String eventID, String comment) {
        repository.writeComment(eventID, comment);
    }
}
