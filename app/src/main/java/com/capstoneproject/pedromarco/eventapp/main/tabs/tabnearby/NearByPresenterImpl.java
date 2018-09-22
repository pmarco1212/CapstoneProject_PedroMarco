package com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby;

import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.main.events.NearbyQueryEvent;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.EventListView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Presenter class implementing the presenter interface
 */
public class NearByPresenterImpl implements NearByPresenter {
    EventListView eventListView;
    EventBus eventBus;
    NearByInteractor interactor;

    public NearByPresenterImpl(EventListView eventListView, EventBus eventBus, NearByInteractor interactor) {
        this.eventListView = eventListView;
        this.eventBus = eventBus;
        this.interactor = interactor;
    }

    /**
     * Tell the interactor to get the Nearby events
     */
    @Override
    public void getNearByEvents() {
        interactor.getNearByEvents();
    }

    /**
     * Listens for eventbus events and update the UI accordingly
     *
     * @param event
     */
    @Override
    @Subscribe
    public void onEventMainThread(NearbyQueryEvent event) {
        if (this.eventListView != null) {
            switch (event.getEventType()) {
                case NearbyQueryEvent.onFailQuery:
                    eventListView.showSearchError();
                    break;
                case NearbyQueryEvent.onNoResultsQuery:
                    eventListView.showNoEventsFound();
                    break;
                case NearbyQueryEvent.onSucefullQuery:
                    eventListView.showEventList();
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
