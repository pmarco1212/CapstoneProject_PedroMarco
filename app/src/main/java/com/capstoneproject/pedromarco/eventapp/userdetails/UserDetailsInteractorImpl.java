package com.capstoneproject.pedromarco.eventapp.userdetails;

/**
 * Interactor class executing the requests in the user details repository. Implements the interactor interface.
 */
public class UserDetailsInteractorImpl implements UserDetailsInteractor {
    UserDetailsRepository repository;

    public UserDetailsInteractorImpl(UserDetailsRepository repository) {
        this.repository = repository;
    }

    /**
     * Execute the method in the repository to get the user info
     *
     * @param username username
     */
    @Override
    public void executeGetUserData(String username) {
        repository.getUserData(username);
    }
}
