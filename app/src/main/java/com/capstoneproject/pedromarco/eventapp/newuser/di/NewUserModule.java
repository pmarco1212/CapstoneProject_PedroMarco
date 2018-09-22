package com.capstoneproject.pedromarco.eventapp.newuser.di;

import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorage;
import com.capstoneproject.pedromarco.eventapp.newuser.NewUserInteractor;
import com.capstoneproject.pedromarco.eventapp.newuser.NewUserIteractorImpl;
import com.capstoneproject.pedromarco.eventapp.newuser.NewUserPresenter;
import com.capstoneproject.pedromarco.eventapp.newuser.NewUserPresenterImpl;
import com.capstoneproject.pedromarco.eventapp.newuser.NewUserRepositoryImpl;
import com.capstoneproject.pedromarco.eventapp.newuser.NewUserRespository;
import com.capstoneproject.pedromarco.eventapp.newuser.ui.NewUserView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module providing all the newuser package dependencies
 */
@Module
public class NewUserModule {
    NewUserView view;

    public NewUserModule(NewUserView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    NewUserView providesNewUserView() {
        return this.view;
    }

    @Provides
    @Singleton
    NewUserPresenter providesNewUserPresenter(NewUserView newUserView, NewUserInteractor newUserInteractor, EventBus eventBus) {
        return new NewUserPresenterImpl(newUserView, newUserInteractor, eventBus);
    }

    @Provides
    @Singleton
    NewUserInteractor providesNewUserInteractor(NewUserRespository newUserRespository) {
        return new NewUserIteractorImpl(newUserRespository);
    }

    @Provides
    @Singleton
    NewUserRespository providesNewUserRepository(FirebaseAPI firebaseAPI, EventBus eventBus, ImageStorage imageStorage) {
        return new NewUserRepositoryImpl(firebaseAPI, eventBus, imageStorage);
    }


}
