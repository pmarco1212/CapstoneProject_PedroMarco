package com.capstoneproject.pedromarco.eventapp.login;

import com.capstoneproject.pedromarco.eventapp.login.events.LoginEvent;

/**
 * Interface stating the methods of the login presenter
 */
public interface LoginPresenter {
    void onCreate();

    void onDestroy();

    void onEventMainThread(LoginEvent event);

    void loginFirebase(String email, String password);

    void signUpFirebase(String email, String password);

    void loginSucessfull();
}
