package com.capstoneproject.pedromarco.eventapp.userdetails.di;


import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.userdetails.UserDetailsInteractor;
import com.capstoneproject.pedromarco.eventapp.userdetails.UserDetailsInteractorImpl;
import com.capstoneproject.pedromarco.eventapp.userdetails.UserDetailsPresenter;
import com.capstoneproject.pedromarco.eventapp.userdetails.UserDetailsPresenterImpl;
import com.capstoneproject.pedromarco.eventapp.userdetails.UserDetailsRepository;
import com.capstoneproject.pedromarco.eventapp.userdetails.UserDetailsRepositoryImpl;
import com.capstoneproject.pedromarco.eventapp.userdetails.ui.UserDetailsView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module providing all the userdetails package dependencies
 */
@Module
public class UserDetailsModule {
    UserDetailsView userDetailsView;

    public UserDetailsModule(UserDetailsView userDetailsView) {
        this.userDetailsView = userDetailsView;
    }

    @Singleton
    @Provides
    UserDetailsView providesUserDetailsView() {
        return this.userDetailsView;
    }

    @Singleton
    @Provides
    UserDetailsPresenter providesUserDetailsPresenter(UserDetailsView view, UserDetailsInteractor interactor, EventBus eventBus) {
        return new UserDetailsPresenterImpl(view, interactor, eventBus);
    }

    @Singleton
    @Provides
    UserDetailsInteractor providesUserDetailsInteractor(UserDetailsRepository repository) {
        return new UserDetailsInteractorImpl(repository);
    }

    @Singleton
    @Provides
    UserDetailsRepository providesUserDetailsRepository(EventBus eventBus, FirebaseAPI firebaseAPI) {
        return new UserDetailsRepositoryImpl(eventBus, firebaseAPI);
    }
}
