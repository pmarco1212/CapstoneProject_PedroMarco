package com.capstoneproject.pedromarco.eventapp.newuser;

/**
 * Interactor class executing the requests in the repository. Implements the interactor interface.
 */
public class NewUserIteractorImpl implements NewUserInteractor {

    NewUserRespository respository;

    public NewUserIteractorImpl(NewUserRespository respository) {
        this.respository = respository;
    }

    /**
     * Execute the addition of an user in the repository
     *
     * @param username
     * @param firstname
     * @param surname
     * @param description
     * @param profilepictureURL
     */
    @Override
    public void execute(String username, String firstname, String surname, String description, String profilepictureURL) {
        respository.addNewUser(username, firstname, surname, description, profilepictureURL);
    }
}
