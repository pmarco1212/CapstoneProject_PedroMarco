package com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.di;

import com.capstoneproject.pedromarco.eventapp.EventAppModule;
import com.capstoneproject.pedromarco.eventapp.domain.di.DomainModule;
import com.capstoneproject.pedromarco.eventapp.lib.di.LibsModule;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.ui.SearchFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component of the search tab package
 */
@Singleton
@Component(modules = {SearchFragmentModule.class, DomainModule.class, LibsModule.class, EventAppModule.class})
public interface SearchFragmentComponent {
    void inject(SearchFragment fragment);
}
