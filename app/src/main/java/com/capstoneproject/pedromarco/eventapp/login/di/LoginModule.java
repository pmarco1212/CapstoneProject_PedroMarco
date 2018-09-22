package com.capstoneproject.pedromarco.eventapp.login.di;

import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.login.FirebaseLoginInteractor;
import com.capstoneproject.pedromarco.eventapp.login.FirebaseLoginInteractorImpl;
import com.capstoneproject.pedromarco.eventapp.login.FirebaseSignUpInteractor;
import com.capstoneproject.pedromarco.eventapp.login.FirebaseSignUpInteractorImpl;
import com.capstoneproject.pedromarco.eventapp.login.LoginPresenter;
import com.capstoneproject.pedromarco.eventapp.login.LoginPresenterImpl;
import com.capstoneproject.pedromarco.eventapp.login.LoginRepository;
import com.capstoneproject.pedromarco.eventapp.login.LoginRepositoryImpl;
import com.capstoneproject.pedromarco.eventapp.login.UserIsRegisteredInteractor;
import com.capstoneproject.pedromarco.eventapp.login.UserIsRegisteredInteractorImpl;
import com.capstoneproject.pedromarco.eventapp.login.ui.LoginView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module providing all the login package dependencies
 */
@Module
public class LoginModule {
    LoginView view;

    public LoginModule(LoginView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    LoginView providesLoginView() {
        return this.view;
    }

    @Provides
    @Singleton
    LoginPresenter providesLoginPresenter(EventBus eventBus, LoginView loginView, FirebaseLoginInteractor loginInteractor, FirebaseSignUpInteractor signUpInteractor, UserIsRegisteredInteractor userIsRegisteredInteractor) {
        return new LoginPresenterImpl(eventBus, loginView, loginInteractor, signUpInteractor, userIsRegisteredInteractor);
    }

    @Provides
    @Singleton
    FirebaseLoginInteractor providesLoginInteractor(LoginRepository repository) {
        return new FirebaseLoginInteractorImpl(repository);
    }

    @Provides
    @Singleton
    FirebaseSignUpInteractor providesSignupInteractor(LoginRepository repository) {
        return new FirebaseSignUpInteractorImpl(repository);
    }

    @Provides
    @Singleton
    UserIsRegisteredInteractor providesUserIsRegisteredInteractor(LoginRepository repository) {
        return new UserIsRegisteredInteractorImpl(repository);
    }

    //Adding the view to the repository to handle the Facebook callback
    @Provides
    @Singleton
    LoginRepository providesLoginRepository(FirebaseAPI firebase, EventBus eventBus) {
        return new LoginRepositoryImpl(firebase, eventBus);
    }
}
