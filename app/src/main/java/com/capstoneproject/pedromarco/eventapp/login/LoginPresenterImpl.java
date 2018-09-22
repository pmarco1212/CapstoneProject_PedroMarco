package com.capstoneproject.pedromarco.eventapp.login;

import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.login.events.LoginEvent;
import com.capstoneproject.pedromarco.eventapp.login.ui.LoginView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Presenter class implementing the presenter interface
 */
public class LoginPresenterImpl implements LoginPresenter {
    LoginView loginView;
    FirebaseLoginInteractor firebaseLoginInteractor;
    FirebaseSignUpInteractor firebaseSignUpInteractor;
    UserIsRegisteredInteractor userIsRegisteredInteractor;
    EventBus eventBus;

    public LoginPresenterImpl(EventBus eventBus, LoginView loginView, FirebaseLoginInteractor firebaseLoginInteractor, FirebaseSignUpInteractor signUpInteractor, UserIsRegisteredInteractor userIsRegisteredInteractor) {
        this.loginView = loginView;
        this.firebaseLoginInteractor = firebaseLoginInteractor;
        this.firebaseSignUpInteractor = signUpInteractor;
        this.userIsRegisteredInteractor = userIsRegisteredInteractor;
        this.eventBus = eventBus;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        loginView = null;
    }

    @Override
    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        switch (event.getEventType()) {
            case LoginEvent.onSignInError:
                onSignInError(event.getErrorMesage());
                break;
            case LoginEvent.onFirebaseSignInSuccess:
                onSignInSuccess(event.getLoggedUserEmail());
                break;
            case LoginEvent.onSignUpError:
                onSignUpError(event.getErrorMesage());
                break;
            case LoginEvent.onFirebaseSignUpSuccess:
                onSignUpSuccess();
                break;
            case LoginEvent.onFailedToRecoverSession:
                onFailedToRecoverSession();
                break;
            case LoginEvent.onSessionRecovered:
                onSessionRecovered(event.getLoggedUserEmail());
                break;
            case LoginEvent.onUserIsRegistered:
                userIsRegistered();
                break;
            case LoginEvent.onUserIsNotRegistered:
                userIsNotRegistered();
                break;
        }

    }

    private void onSignInSuccess(String email) {
        if (loginView != null) {
            loginView.setUserEmail(email);
            loginView.loginSucessfull();
        }
    }

    private void onSignUpSuccess() {
        if (loginView != null) {
            loginView.newUserSuccess();
        }
    }

    private void onSignInError(String error) {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.enableInput();
            loginView.loginErrorFirebase(error);
        }
    }

    private void onSignUpError(String error) {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.enableInput();
            loginView.newUserErrorFirebase(error);
        }
    }

    private void onFailedToRecoverSession() {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.enableInput();
        }
    }

    //The user was already logged in, session recovered
    private void onSessionRecovered(String email) {
        if (loginView != null) {
            loginView.setUserEmail(email);
            loginView.navigateToMainScreen();
        }
    }

    @Override
    public void loginFirebase(String email, String password) {
        loginView.showProgress();
        loginView.disableInput();
        firebaseLoginInteractor.execute(email, password);
    }

    @Override
    public void signUpFirebase(String email, String password) {
        loginView.showProgress();
        loginView.disableInput();
        firebaseSignUpInteractor.execute(email, password);
    }

    @Override
    public void loginSucessfull() {
        userIsRegisteredInteractor.execute();
    }


    //The user is already registered in the database, sent him to the main screen
    public void userIsRegistered() {
        loginView.navigateToMainScreen();
    }

    //The user is not registered in the database, sent him to the activity to create the user
    public void userIsNotRegistered() {
        loginView.navigateToNewUserScreen();
    }
}
