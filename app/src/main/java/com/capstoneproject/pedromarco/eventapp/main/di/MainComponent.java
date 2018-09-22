package com.capstoneproject.pedromarco.eventapp.main.di;

import com.capstoneproject.pedromarco.eventapp.EventAppModule;
import com.capstoneproject.pedromarco.eventapp.domain.di.DomainModule;
import com.capstoneproject.pedromarco.eventapp.lib.di.LibsModule;
import com.capstoneproject.pedromarco.eventapp.main.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Dagger component of the main package
 */
@Singleton
@Component(modules = {MainModule.class, DomainModule.class, LibsModule.class, EventAppModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
