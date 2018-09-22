package com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.di;

import com.capstoneproject.pedromarco.eventapp.domain.AlgoliaAPI;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageLoader;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.SearchInteractor;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.SearchInteractorImpl;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.SearchPresenter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.SearchPresenterImpl;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.SearchRepository;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.SearchRepositoryImpl;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.ui.SearchView;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.FragmentListRecyclerviewAdapter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.OnEventClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module providing all the package dependencies
 */
@Module
public class SearchFragmentModule {
    SearchView searchView;
    OnEventClickListener onEventClickListener;

    public SearchFragmentModule(SearchView searchView, OnEventClickListener onEventClickListener) {
        this.searchView = searchView;
        this.onEventClickListener = onEventClickListener;
    }

    @Provides
    @Singleton
    SearchView providesEventListView() {
        return this.searchView;
    }

    @Provides
    @Singleton
    SearchPresenter providesSearchPresenter(SearchView view, EventBus eventBus, SearchInteractor interactor) {
        return new SearchPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    SearchInteractor providesSearchInteractor(SearchRepository repository) {
        return new SearchInteractorImpl(repository);
    }

    @Provides
    @Singleton
    SearchRepository providesSearchRepository(EventBus eventBus, AlgoliaAPI algoliaAPI) {
        return new SearchRepositoryImpl(eventBus, algoliaAPI);
    }

    @Provides
    @Singleton
    FragmentListRecyclerviewAdapter providesAdapter(List<Event> events, ImageLoader imageLoader, OnEventClickListener onEventClickListener) {
        return new FragmentListRecyclerviewAdapter(events, imageLoader, onEventClickListener);
    }

    @Provides
    @Singleton
    OnEventClickListener providesClickListener() {
        return this.onEventClickListener;
    }

    @Provides
    @Singleton
    List<Event> providesEventList() {
        return new ArrayList<Event>();
    }

}
