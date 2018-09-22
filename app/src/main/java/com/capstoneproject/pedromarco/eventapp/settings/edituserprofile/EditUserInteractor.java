package com.capstoneproject.pedromarco.eventapp.settings.edituserprofile;

/**
 * Interactor interface of the edit user
 */
public interface EditUserInteractor {
    void execute(String firstname, String surname, String description, String profilepictureURL);

    void getUserDetails();
}
