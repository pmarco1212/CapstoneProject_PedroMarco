package com.capstoneproject.pedromarco.eventapp.settings.di;

import com.capstoneproject.pedromarco.eventapp.EventAppModule;
import com.capstoneproject.pedromarco.eventapp.settings.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component of the settings package
 */
@Singleton
@Component(modules = {SettingsModule.class, EventAppModule.class})
public interface SettingsComponent {
    void inject(SettingsActivity activity);
}
