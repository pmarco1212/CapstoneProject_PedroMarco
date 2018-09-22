package com.capstoneproject.pedromarco.eventapp.createevent.di;


import com.capstoneproject.pedromarco.eventapp.createevent.CreateEventInteractor;
import com.capstoneproject.pedromarco.eventapp.createevent.CreateEventInteractorImpl;
import com.capstoneproject.pedromarco.eventapp.createevent.CreateEventPresenter;
import com.capstoneproject.pedromarco.eventapp.createevent.CreateEventPresenterImpl;
import com.capstoneproject.pedromarco.eventapp.createevent.CreateEventRepository;
import com.capstoneproject.pedromarco.eventapp.createevent.CreateEventRepositoryImpl;
import com.capstoneproject.pedromarco.eventapp.createevent.ui.CreateEventView;
import com.capstoneproject.pedromarco.eventapp.domain.AlgoliaAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module providing all the create event package dependencies
 */
@Module
public class CreateEventModule {
    CreateEventView createEventView;

    public CreateEventModule(CreateEventView createEventView) {
        this.createEventView = createEventView;
    }

    @Provides
    @Singleton
    public CreateEventView providesCreateEventView() {
        return this.createEventView;
    }

    @Provides
    @Singleton
    public CreateEventPresenter providesCreateEventPresenter(CreateEventView createEventView, CreateEventInteractor createEventInteractor, EventBus eventBus) {
        return new CreateEventPresenterImpl(createEventView, createEventInteractor, eventBus);
    }

    @Provides
    @Singleton
    public CreateEventInteractor providesCreateEventInteractor(CreateEventRepository createEventRepository) {
        return new CreateEventInteractorImpl(createEventRepository);
    }

    @Provides
    @Singleton
    public CreateEventRepository providesCreateEventRepository(EventBus eventBus, FirebaseAPI firebaseAPI, AlgoliaAPI algoliaAPI, ImageStorage imageStorage) {
        return new CreateEventRepositoryImpl(eventBus, firebaseAPI, algoliaAPI, imageStorage);
    }
}
