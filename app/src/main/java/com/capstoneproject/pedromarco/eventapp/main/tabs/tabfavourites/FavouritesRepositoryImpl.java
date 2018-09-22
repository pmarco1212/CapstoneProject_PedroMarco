package com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites;

import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseEventListenerCallback;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseSingleValueEventListenerCallback;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.main.events.FavouritesQueryEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Repository class, handling all the model-side data of the Favourites
 */
public class FavouritesRepositoryImpl implements FavouritesRepository {
    EventBus eventBus;
    FirebaseAPI firebaseAPI;
    LinkedHashMap<String, Event> hashMapEvents;

    public FavouritesRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
        hashMapEvents = new LinkedHashMap<>();
    }

    /**
     * Subscribe to firebase to get the favourite events of the user, and post en event with the restuls
     */
    @Override
    public void getFavouriteEvents() {
        firebaseAPI.suscribeToFavouritesFromUser(firebaseAPI.getUserUID(), new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot) {
                //Check if the value is not null (the value should be the ID of the favourite event)
                if (dataSnapshot.getValue() != null) {
                    final String eventID = dataSnapshot.getValue().toString();
                    firebaseAPI.getSingleEventFromEventID(eventID, new FirebaseSingleValueEventListenerCallback() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                Event event = dataSnapshot.getChildren().iterator().next().getValue(Event.class);
                                //event.setId(dataSnapshot.getKey());
                                hashMapEvents.put(event.getId(), event);
                                post(FavouritesQueryEvent.onSucefullQuery, new ArrayList<Event>(hashMapEvents.values()));
                            } else {
                                post(FavouritesQueryEvent.onNoResultsQuery);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            post(FavouritesQueryEvent.onFailQuery);
                        }
                    });

                }
            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String eventID = dataSnapshot.getValue().toString();
                    hashMapEvents.remove(eventID);
                    post(FavouritesQueryEvent.onSucefullQuery, new ArrayList<Event>(hashMapEvents.values()));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void post(int type) {
        post(type, null);
    }

    /**
     * post an event to be read at the presenter
     *
     * @param type
     * @param data
     */
    private void post(int type, List<Event> data) {
        FavouritesQueryEvent event = new FavouritesQueryEvent();
        event.setEventType(type);
        if (data != null) {
            event.setEventList(data);
        }
        eventBus.post(event);
    }
}
