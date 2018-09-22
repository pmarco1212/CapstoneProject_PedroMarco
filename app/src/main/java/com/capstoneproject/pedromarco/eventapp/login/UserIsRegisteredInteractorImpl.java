package com.capstoneproject.pedromarco.eventapp.login;


/**
 * Interactor class executing the check if user registered requests in the repository. Implements the interactor interface.
 */
public class UserIsRegisteredInteractorImpl implements UserIsRegisteredInteractor {
    LoginRepository repository;

    public UserIsRegisteredInteractorImpl(LoginRepository repository) {
        this.repository = repository;
    }

    /**
     * Execute the user already registered check request in the repository
     */
    @Override
    public void execute() {
        repository.checkIfUserIsRegisteredInDatabase();
    }
}
