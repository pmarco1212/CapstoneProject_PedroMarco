package com.capstoneproject.pedromarco.eventapp.createevent.di;


import com.capstoneproject.pedromarco.eventapp.EventAppModule;
import com.capstoneproject.pedromarco.eventapp.createevent.ui.CreateEventActivity;
import com.capstoneproject.pedromarco.eventapp.domain.di.DomainModule;
import com.capstoneproject.pedromarco.eventapp.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component of the package
 */
@Singleton
@Component(modules = {CreateEventModule.class, LibsModule.class, DomainModule.class, EventAppModule.class})
public interface CreateEventComponent {
    void inject(CreateEventActivity createEventActivity);
}
