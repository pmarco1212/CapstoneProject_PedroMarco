package com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch;

import android.util.Log;

import com.capstoneproject.pedromarco.eventapp.domain.AlgoliaAPI;
import com.capstoneproject.pedromarco.eventapp.domain.AlgoliaCompletionListener;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.main.events.SearchQueryEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Repository class, handling all the model-side data of the Search
 */
public class SearchRepositoryImpl implements SearchRepository {
    EventBus eventBus;
    LinkedHashMap<String, Event> hashMapEvents;
    AlgoliaAPI algoliaAPI;

    public SearchRepositoryImpl(EventBus eventBus, AlgoliaAPI algoliaAPI) {
        this.eventBus = eventBus;
        this.algoliaAPI = algoliaAPI;
        hashMapEvents = new LinkedHashMap<>();
    }

    /**
     * Search for events in the Algolia database with the given params, and post results accordintly
     *
     * @param keywords keywords to search
     * @param distance max distance to the event
     * @param dateFrom start date
     * @param dateTo   end date
     * @param category category of the event
     */
    @Override
    public void searchAndGetEvents(String keywords, int distance, String dateFrom, String dateTo, String category) {
        algoliaAPI.searchEvents(keywords, distance, dateFrom, dateTo, category, new AlgoliaCompletionListener() {
            @Override
            public void requestCompleted(LinkedHashMap<String, Event> searchResult) {
                if (searchResult != null) {
                    if (!searchResult.isEmpty()) {
                        Log.d("AAAAAA", "" + searchResult.size());
                        post(SearchQueryEvent.onSucefullQuery, new ArrayList<Event>(searchResult.values()));
                    } else {
                        post(SearchQueryEvent.onNoResultsQuery);
                    }
                } else {
                    post(SearchQueryEvent.onFailQuery);
                }
            }
        });
    }

    private void post(int type) {
        post(type, null);
    }

    /**
     * Post an event
     *
     * @param type
     * @param data
     */
    private void post(int type, List<Event> data) {
        SearchQueryEvent event = new SearchQueryEvent();
        event.setEventType(type);
        if (data != null) {
            event.setEventList(data);
        }
        eventBus.post(event);
    }
}
