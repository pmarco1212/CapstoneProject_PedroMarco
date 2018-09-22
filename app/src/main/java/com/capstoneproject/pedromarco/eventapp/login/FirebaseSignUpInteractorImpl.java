package com.capstoneproject.pedromarco.eventapp.login;

/**
 * Interactor class executing the signup requests in the repository. Implements the sign up interactor interface.
 */
public class FirebaseSignUpInteractorImpl implements FirebaseSignUpInteractor {
    LoginRepository loginRepository;

    public FirebaseSignUpInteractorImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    /**
     * Execute the sign up request in the repository
     *
     * @param email    user email
     * @param password user password
     */
    @Override
    public void execute(String email, String password) {
        loginRepository.signUpFirebase(email, password);
    }
}
