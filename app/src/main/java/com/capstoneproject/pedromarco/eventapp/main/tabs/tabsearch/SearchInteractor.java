package com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch;

/**
 * Interactor interface of the search
 */
public interface SearchInteractor {
    void getEvents(String keywords, int distance, String dateFrom, String dateTo, String category);
}
