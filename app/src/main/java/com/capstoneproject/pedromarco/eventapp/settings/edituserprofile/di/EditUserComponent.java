package com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.di;

import com.capstoneproject.pedromarco.eventapp.EventAppModule;
import com.capstoneproject.pedromarco.eventapp.domain.di.DomainModule;
import com.capstoneproject.pedromarco.eventapp.lib.di.LibsModule;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.ui.EditUserActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component of the edit user package
 */
@Singleton
@Component(modules = {EditUserModule.class, DomainModule.class, LibsModule.class, EventAppModule.class})
public interface EditUserComponent {
    void inject(EditUserActivity activity);
}
