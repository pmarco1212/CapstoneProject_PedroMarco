package com.capstoneproject.pedromarco.eventapp.createevent.event;

/**
 * EventBus event model of the create event package
 */
public class CreateEventEvent {
    public final static int onEventCreatedSuccess = 0;
    public final static int onEventCreatedError = 1;
    public final static int onPictureUploadError = 2;
    public final static int onInvalidLocationError = 3;
    public final static int onInvalidDateError = 4;
    public final static int onEmptyNameError = 5;
    public final static int onEmptyDescriptionError = 6;
    public final static int onEmptyTimeError = 7;


    private int eventType;
    private String errorMesage;

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
