package com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch;

/**
 * Repository interface stating the methods of the repository
 */
public interface SearchRepository {
    void searchAndGetEvents(String keywords, int distance, String dateFrom, String dateTo, String category);
}
