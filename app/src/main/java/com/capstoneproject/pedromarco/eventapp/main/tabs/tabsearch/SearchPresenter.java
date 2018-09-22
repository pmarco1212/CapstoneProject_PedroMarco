package com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch;

import com.capstoneproject.pedromarco.eventapp.main.events.SearchQueryEvent;

/**
 * Interface stating the methods of the presenter
 */
public interface SearchPresenter {
    void searchEvents(String keywords, int distance, String dateFrom, String dateTo, String category);

    void onEventMainThread(SearchQueryEvent event);

    void onCreate();

    void onDestroy();

    void closeSearch();
}
