package com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.di;


import com.capstoneproject.pedromarco.eventapp.EventAppModule;
import com.capstoneproject.pedromarco.eventapp.domain.di.DomainModule;
import com.capstoneproject.pedromarco.eventapp.lib.di.LibsModule;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.ui.NearByFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component of the Neaby tab package
 */
@Singleton
@Component(modules = {NearByFragmentModule.class, DomainModule.class, LibsModule.class, EventAppModule.class})
public interface NearByFragmentComponent {
    void inject(NearByFragment fragment);
}
