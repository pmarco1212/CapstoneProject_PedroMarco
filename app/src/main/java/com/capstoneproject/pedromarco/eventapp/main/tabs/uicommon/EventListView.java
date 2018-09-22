package com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon;

import com.capstoneproject.pedromarco.eventapp.entities.Event;

import java.util.List;

/**
 * Interface containing the common methods of the Event List views
 */
public interface EventListView {
    void updateEventList(List<Event> eventList);

    void showSearchError();

    void showNoEventsFound();

    void showEventList();
}
