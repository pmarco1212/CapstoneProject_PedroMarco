package com.capstoneproject.pedromarco.eventapp.newuser.di;

import com.capstoneproject.pedromarco.eventapp.EventAppModule;
import com.capstoneproject.pedromarco.eventapp.domain.di.DomainModule;
import com.capstoneproject.pedromarco.eventapp.lib.di.LibsModule;
import com.capstoneproject.pedromarco.eventapp.newuser.ui.NewUserActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component of the newuser package
 */
@Singleton
@Component(modules = {NewUserModule.class, DomainModule.class, LibsModule.class, EventAppModule.class})
public interface NewUserComponent {
    void inject(NewUserActivity activity);
}
