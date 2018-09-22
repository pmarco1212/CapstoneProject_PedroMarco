package com.capstoneproject.pedromarco.eventapp.login.events;

/**
 * EventBus event model of the login class
 */
public class LoginEvent {
    public final static int onSignInError = 0;
    public final static int onSignUpError = 1;
    public final static int onFirebaseSignInSuccess = 2;
    public final static int onFirebaseSignUpSuccess = 3;
    public final static int onFacebookSignInSuccess = 4;
    public final static int onTwitterSignInSuccess = 5;
    public final static int onSessionRecovered = 7;
    public final static int onFailedToRecoverSession = 6;
    public final static int onUserIsRegistered = 8;
    public final static int onUserIsNotRegistered = 9;

    private int eventType;
    private String errorMesage;
    private String loggedUserEmail;

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

    public String getLoggedUserEmail() {
        return loggedUserEmail;
    }

    public void setLoggedUserEmail(String loggedUserEmail) {
        this.loggedUserEmail = loggedUserEmail;
    }
}
