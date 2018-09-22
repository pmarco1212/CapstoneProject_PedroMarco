package com.capstoneproject.pedromarco.eventapp.newuser;

/**
 * Interactor interface of the new user
 */
public interface NewUserInteractor {
    void execute(String username, String firstname, String surname, String description, String profilepictureURL);
}
