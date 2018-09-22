package com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.events;

import com.capstoneproject.pedromarco.eventapp.entities.User;


/**
 * EventBus event model of the edituser package
 */
public class EditUserEvent {
    public final static int onUserUpdatedSucess = 0;
    public final static int onUserUpdatedError = 1;
    public final static int onEmptyFieldError = 2;
    public final static int onUsernameAlreadyInUse = 3;
    public final static int onImageUploadError = 4;
    public final static int onUserDetailsObtainedSucess = 5;

    private int eventType;
    private String errorMesage;
    private User user;


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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
