package com.capstoneproject.pedromarco.eventapp.userdetails.events;

import com.capstoneproject.pedromarco.eventapp.entities.User;

/**
 * EventBus event model of the usedetails package
 */
public class UserDetailsEvent {
    public final static int onUserReadSucess = 0;
    public final static int onUserReadError = 1;
    public final static int onUserReadStart = 2;

    private int eventType;
    private String errorMesage;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
