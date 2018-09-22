package com.capstoneproject.pedromarco.eventapp.newuser;

/**
 * Repository interface stating the methods of the repository
 */
public interface NewUserRespository {
    void addNewUser(String username, String firstname, String surname, String description, String profilepictureURL);
}
