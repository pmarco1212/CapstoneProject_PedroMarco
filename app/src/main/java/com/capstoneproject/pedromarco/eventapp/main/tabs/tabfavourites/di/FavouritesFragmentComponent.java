package com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.di;

import com.capstoneproject.pedromarco.eventapp.EventAppModule;
import com.capstoneproject.pedromarco.eventapp.domain.di.DomainModule;
import com.capstoneproject.pedromarco.eventapp.lib.di.LibsModule;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.ui.FavouritesFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component of the favourites package
 */
@Singleton
@Component(modules = {FavouritesFragmentModule.class, DomainModule.class, LibsModule.class, EventAppModule.class})
public interface FavouritesFragmentComponent {
    void inject(FavouritesFragment fragment);
}
