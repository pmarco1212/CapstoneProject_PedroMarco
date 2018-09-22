package com.capstoneproject.pedromarco.eventapp.login.ui;

/**
 * Interface of the login view
 */
public interface LoginView {

    void enableInput();

    void disableInput();

    void showProgress();

    void hideProgress();

    void loginSucessfull();

    void newUserSuccess();

    void navigateToMainScreen();

    void navigateToNewUserScreen();

    void setUserEmail(String email);

    void loginErrorFirebase(String error);

    void newUserErrorFirebase(String error);
}
