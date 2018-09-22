package com.capstoneproject.pedromarco.eventapp.login;

/**
 * Repository interface stating the methods of the Login repository
 */
public interface LoginRepository {
    void loginFirebase(String email, String password);

    void signUpFirebase(String email, String password);

    void checkIfUserIsRegisteredInDatabase();
}
