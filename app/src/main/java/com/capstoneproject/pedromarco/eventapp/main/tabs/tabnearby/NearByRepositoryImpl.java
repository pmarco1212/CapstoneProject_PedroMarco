package com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby;

import com.capstoneproject.pedromarco.eventapp.domain.AlgoliaAPI;
import com.capstoneproject.pedromarco.eventapp.domain.AlgoliaCompletionListener;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.main.events.NearbyQueryEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Repository class, handling all the model-side data of the Nearby tab package
 */
public class NearByRepositoryImpl implements NearByRepository {
    EventBus eventBus;
    AlgoliaAPI algoliaAPI;
    LinkedHashMap<String, Event> hashMapEvents;

    public NearByRepositoryImpl(EventBus eventBus, AlgoliaAPI algoliaAPI) {
        this.eventBus = eventBus;
        hashMapEvents = new LinkedHashMap<>();
        this.algoliaAPI = algoliaAPI;
    }

    /**
     * Gets the Nearby events form Algolia and post the result with eventbus
     */
    @Override
    public void getNearbyEvents() {
        algoliaAPI.getNearByEvents(new AlgoliaCompletionListener() {
            @Override
            public void requestCompleted(LinkedHashMap<String, Event> searchResult) {
                if (searchResult != null) {
                    if (!searchResult.isEmpty()) {
                        post(NearbyQueryEvent.onSucefullQuery, new ArrayList<Event>(searchResult.values()));
                    } else {
                        post(NearbyQueryEvent.onNoResultsQuery);
                    }
                } else {
                    post(NearbyQueryEvent.onFailQuery);
                }
            }
        });
    }

    private void post(int type) {
        post(type, null);
    }

    /**
     * Post eventbus event with results
     *
     * @param type
     * @param data
     */
    private void post(int type, List<Event> data) {
        NearbyQueryEvent event = new NearbyQueryEvent();
        event.setEventType(type);
        if (data != null) {
            event.setEventList(data);
        }
        eventBus.post(event);
    }
}

