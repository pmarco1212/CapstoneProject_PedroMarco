package com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites;

import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.main.events.FavouritesQueryEvent;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.EventListView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Presenter class implementing the presenter interface
 */
public class FavouritesPresenterImpl implements FavouritesPresenter {
    EventListView eventListView;
    EventBus eventBus;
    FavouritesInteractor interactor;

    public FavouritesPresenterImpl(EventListView eventListView, EventBus eventBus, FavouritesInteractor interactor) {
        this.eventListView = eventListView;
        this.eventBus = eventBus;
        this.interactor = interactor;
    }

    /**
     * Tell the interactor to get the favourites
     */
    @Override
    public void getFavouriteEvents() {
        interactor.getFavouriteEvents();
    }

    /**
     * Listens for events and update the view accordingly
     *
     * @param event
     */
    @Override
    @Subscribe
    public void onEventMainThread(FavouritesQueryEvent event) {
        if (this.eventListView != null) {
            switch (event.getEventType()) {
                case FavouritesQueryEvent.onFailQuery:
                    eventListView.showSearchError();
                    break;
                case FavouritesQueryEvent.onNoResultsQuery:
                    eventListView.showNoEventsFound();
                    break;
                case FavouritesQueryEvent.onSucefullQuery:
                    eventListView.updateEventList(event.getEventList());
                    break;
            }
        }
    }

    /**
     * Registers eventbus
     */
    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    /**
     * Unregisters eventbus
     */
    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }
}