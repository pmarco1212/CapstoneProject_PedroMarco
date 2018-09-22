package com.capstoneproject.pedromarco.eventapp.userdetails.di;

import com.capstoneproject.pedromarco.eventapp.EventAppModule;
import com.capstoneproject.pedromarco.eventapp.domain.di.DomainModule;
import com.capstoneproject.pedromarco.eventapp.lib.di.LibsModule;
import com.capstoneproject.pedromarco.eventapp.userdetails.ui.UserDetailsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component of the userdetails package
 */
@Singleton
@Component(modules = {UserDetailsModule.class, LibsModule.class, DomainModule.class, EventAppModule.class})
public interface UserDetailsComponent {
    void inject(UserDetailsActivity activity);
}
