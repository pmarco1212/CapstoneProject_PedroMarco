package com.capstoneproject.pedromarco.eventapp.settings.edituserprofile;

/**
 * Repository interface stating the methods of the edit user repository
 */
public interface EditUserRespository {
    void editUser(String firstname, String surname, String description, String profilepictureURL);

    void getUserDetails();
}
