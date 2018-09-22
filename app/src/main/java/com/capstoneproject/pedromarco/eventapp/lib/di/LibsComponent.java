package com.capstoneproject.pedromarco.eventapp.lib.di;

import com.capstoneproject.pedromarco.eventapp.EventAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Interface dagger component of the libs package
 */
@Singleton
@Component(modules = {LibsModule.class, EventAppModule.class})
public interface LibsComponent {
}
