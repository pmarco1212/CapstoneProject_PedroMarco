package com.capstoneproject.pedromarco.eventapp.login.di;

import com.capstoneproject.pedromarco.eventapp.EventAppModule;
import com.capstoneproject.pedromarco.eventapp.domain.di.DomainModule;
import com.capstoneproject.pedromarco.eventapp.lib.di.LibsModule;
import com.capstoneproject.pedromarco.eventapp.login.ui.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component of the login package
 */
@Singleton
@Component(modules = {LoginModule.class, DomainModule.class, LibsModule.class, EventAppModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
