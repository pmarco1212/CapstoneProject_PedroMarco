package com.capstoneproject.pedromarco.eventapp.settings.edituserprofile;

/**
 * Interactor class executing the requests in the repository. Implements the interactor interface.
 */
public class EditUserIteractorImpl implements EditUserInteractor {

    EditUserRespository respository;

    public EditUserIteractorImpl(EditUserRespository respository) {
        this.respository = respository;
    }

    /**
     * Execute the method in the repository to update the user
     *
     * @param firstname         first name of the user
     * @param surname           surname of the user
     * @param description       description of the user
     * @param profilepictureURL pic URL of the user
     */
    @Override
    public void execute(String firstname, String surname, String description, String profilepictureURL) {
        respository.editUser(firstname, surname, description, profilepictureURL);
    }

    /**
     * Execute the method to get the details of the user
     */
    @Override
    public void getUserDetails() {
        respository.getUserDetails();
    }
}
