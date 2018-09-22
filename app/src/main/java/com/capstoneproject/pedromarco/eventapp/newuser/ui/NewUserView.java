package com.capstoneproject.pedromarco.eventapp.newuser.ui;

/**
 * Interface stating the methods of the newuser view
 */
public interface NewUserView {
    void newUserSuccess();

    void newUserError(String errorMessage);

    void usernameAlreadyInUseError();

    void emptyFieldError();

    void onImageUploadError();

    void navigateToMainScreen();

    void handleAddUser();

    void showProgress();

    void hideProgress();
}
