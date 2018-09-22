package com.capstoneproject.pedromarco.eventapp.eventdetails.di;

import com.capstoneproject.pedromarco.eventapp.EventAppModule;
import com.capstoneproject.pedromarco.eventapp.domain.di.DomainModule;
import com.capstoneproject.pedromarco.eventapp.eventdetails.ui.EventDetailsActivity;
import com.capstoneproject.pedromarco.eventapp.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component of the eventdetails package
 */
@Singleton
@Component(modules = {EventDetailsModule.class, LibsModule.class, DomainModule.class, EventAppModule.class})
public interface EventDetailsComponent {
    void inject(EventDetailsActivity eventDetailsActivity);
}
