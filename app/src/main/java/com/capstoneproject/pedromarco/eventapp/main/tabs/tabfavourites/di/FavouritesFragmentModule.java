package com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.di;

import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageLoader;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.FavouritesInteractor;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.FavouritesInteractorImpl;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.FavouritesPresenter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.FavouritesPresenterImpl;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.FavouritesRepository;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.FavouritesRepositoryImpl;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.EventListView;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.FragmentListRecyclerviewAdapter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.OnEventClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module providing all the tabfavourites package dependencies
 */
@Module
public class FavouritesFragmentModule {
    EventListView eventListView;
    OnEventClickListener onEventClickListener;

    public FavouritesFragmentModule(EventListView eventListView, OnEventClickListener onEventClickListener) {
        this.eventListView = eventListView;
        this.onEventClickListener = onEventClickListener;
    }

    @Provides
    @Singleton
    EventListView providesEventListView() {
        return this.eventListView;
    }

    @Provides
    @Singleton
    FavouritesPresenter providesFavouritesPresenter(EventListView view, EventBus eventBus, FavouritesInteractor interactor) {
        return new FavouritesPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    FavouritesInteractor providesFavouritesInteractor(FavouritesRepository repository) {
        return new FavouritesInteractorImpl(repository);
    }

    @Provides
    @Singleton
    FavouritesRepository providesFavouritesRepository(EventBus eventBus, FirebaseAPI firebaseAPI) {
        return new FavouritesRepositoryImpl(eventBus, firebaseAPI);
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
