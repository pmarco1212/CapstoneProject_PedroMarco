package com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.di;

import com.capstoneproject.pedromarco.eventapp.domain.AlgoliaAPI;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageLoader;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.NearByInteractor;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.NearByInteractorImpl;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.NearByPresenter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.NearByPresenterImpl;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.NearByRepository;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.NearByRepositoryImpl;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.EventListView;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.FragmentListRecyclerviewAdapter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.OnEventClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module providing all the Nearby package dependencies
 */
@Module
public class NearByFragmentModule {
    EventListView eventListView;
    OnEventClickListener onEventClickListener;

    public NearByFragmentModule(EventListView eventListView, OnEventClickListener onEventClickListener) {
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
    NearByPresenter providesNearByPresenter(EventListView view, EventBus eventBus, NearByInteractor interactor) {
        return new NearByPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    NearByInteractor providesNearByInteractor(NearByRepository repository) {
        return new NearByInteractorImpl(repository);
    }

    @Provides
    @Singleton
    NearByRepository providesNearByRepository(EventBus eventBus, AlgoliaAPI algoliaAPI) {
        return new NearByRepositoryImpl(eventBus, algoliaAPI);
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
