package com.capstoneproject.pedromarco.eventapp.newuser.events;


/**
 * EventBus event model of the newuser package
 */
public class NewUserEvent {
    public final static int onUserCreatedSucess = 0;
    public final static int onUserCreatedError = 1;
    public final static int onEmptyFieldError = 2;
    public final static int onUsernameAlreadyInUse = 3;
    public final static int onImageUploadError = 4;

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
