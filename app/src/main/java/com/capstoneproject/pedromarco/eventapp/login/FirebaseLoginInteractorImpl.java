package com.capstoneproject.pedromarco.eventapp.login;

/**
 * Interactor class executing login request in the repository. Implements the interactor interface.
 */
public class FirebaseLoginInteractorImpl implements FirebaseLoginInteractor {
    LoginRepository loginRepository;

    public FirebaseLoginInteractorImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    /**
     * Execute the login request in the repository
     *
     * @param email    user email
     * @param password user password
     */
    @Override
    public void execute(String email, String password) {
        loginRepository.loginFirebase(email, password);
    }
}
