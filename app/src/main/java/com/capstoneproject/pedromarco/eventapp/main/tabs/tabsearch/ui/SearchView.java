package com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.ui;

import com.capstoneproject.pedromarco.eventapp.entities.Event;

import java.util.List;

/**
 * Interface stating the methods of the search view
 */
public interface SearchView {
    void hideSearchLayout();

    void showSearchLayout();

    void hideEventList();

    void showEventList();

    void showNoEventsFound();

    void showSearchError();

    void updateEventList(List<Event> eventList);
}
