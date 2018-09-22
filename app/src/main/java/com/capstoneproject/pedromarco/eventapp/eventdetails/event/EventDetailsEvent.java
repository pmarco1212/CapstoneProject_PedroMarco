package com.capstoneproject.pedromarco.eventapp.eventdetails.event;

import com.capstoneproject.pedromarco.eventapp.entities.Event;

/**
 * EventBus event model of the package
 */
public class EventDetailsEvent {
    public final static int onAddedToFavourites = 0;
    public final static int onRemovedFromFavourites = 1;
    public final static int onSetFavouriteError = 2;
    public final static int onIsFavourite = 3;
    public final static int onAssistingAdded = 4;
    public final static int onAssistingRemoved = 5;
    public final static int onSetAssistingError = 6;
    public final static int onIsAssisting = 7;
    public final static int onEventFullError = 8;
    public final static int onImHereSucess = 9;
    public final static int onImHereError = 10;
    public final static int onRatingSucess = 11;
    public final static int onRatingError = 12;
    public final static int onAlreadyRated = 13;
    public final static int onEventDetailsObtained = 14;
    public final static int onCommentSucessfullySent = 15;

    private int eventType;
    private String errorMesage;
    private Event event;


    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMesage() {
        return errorMesage;
    }

    public void setErrorMesage(String errorMesage) {
        this.errorMesage = errorMesage;
    }
}
