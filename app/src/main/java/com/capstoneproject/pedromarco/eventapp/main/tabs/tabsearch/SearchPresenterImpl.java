package com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch;

import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.main.events.SearchQueryEvent;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.ui.SearchView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Presenter class implementing the presenter interface
 */
public class SearchPresenterImpl implements SearchPresenter {
    SearchView view;
    EventBus eventBus;
    SearchInteractor interactor;

    public SearchPresenterImpl(SearchView view, EventBus eventBus, SearchInteractor interactor) {
        this.view = view;
        this.eventBus = eventBus;
        this.interactor = interactor;
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

    /**
     * Close the search results in the view
     */
    @Override
    public void closeSearch() {
        view.hideEventList();
        view.showSearchLayout();
    }

    /**
     * Teel the interactor so search for events with the given params
     *
     * @param keywords   keywords to search
     * @param distanceKm max distance to the event
     * @param dateFrom   start date
     * @param dateTo     end date
     * @param category   category of the event
     */
    @Override
    public void searchEvents(String keywords, int distanceKm, String dateFrom, String dateTo, String category) {
        interactor.getEvents(keywords, distanceKm, dateFrom, dateTo, category);
    }

    /**
     * Listens for event and update the view accordingly
     *
     * @param event
     */
    @Subscribe
    @Override
    public void onEventMainThread(SearchQueryEvent event) {
        if (view != null) {
            switch (event.getEventType()) {
                case SearchQueryEvent.onFailQuery:
                    view.showSearchError();
                    break;
                case SearchQueryEvent.onNoResultsQuery:
                    view.showNoEventsFound();
                    break;
                case SearchQueryEvent.onSucefullQuery:
                    view.hideSearchLayout();
                    view.showEventList();
                    view.updateEventList(event.getEventList());
                    break;
            }
        }
    }

}
