package com.capstoneproject.pedromarco.eventapp.eventdetails;

import android.util.Log;

import com.capstoneproject.pedromarco.eventapp.domain.AlgoliaAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseActionListenerCallback;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseSingleValueEventListenerCallback;
import com.capstoneproject.pedromarco.eventapp.entities.Comment;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.entities.User;
import com.capstoneproject.pedromarco.eventapp.eventdetails.event.EventDetailsEvent;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Repository class, handling all the model-side data of the event details
 */
public class EventDetailsRepositoryImpl implements EventDetailsRepository {
    EventBus eventBus;
    FirebaseAPI firebaseAPI;
    AlgoliaAPI algoliaAPI;
    LinkedHashMap<String, Comment> hashMapComments;
    Event event;

    public EventDetailsRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI, AlgoliaAPI algoliaAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
        this.algoliaAPI = algoliaAPI;
        hashMapComments = new LinkedHashMap<String, Comment>();
    }

    /**
     * Get an event from Firebase with the given ID
     *
     * @param eventID event id of the event to get
     */
    @Override
    public void getEventDetails(final String eventID) {
        //Get all the event details from the database
        firebaseAPI.getSingleEventFromEventID(eventID, new FirebaseSingleValueEventListenerCallback() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) { //send the event (and fill the comment list)
                    event = dataSnapshot.getChildren().iterator().next().getValue(Event.class);
                    if (event.getComments() != null) {
                        hashMapComments = new LinkedHashMap<>(event.getComments());
                    }
                    post(EventDetailsEvent.onEventDetailsObtained, event);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(getClass().getCanonicalName(), databaseError.getMessage());
            }
        });

        final String userID = firebaseAPI.getUserUID();
        //Check if its marked as favourite
        firebaseAPI.getUserSingleFavourite(userID, eventID, new FirebaseSingleValueEventListenerCallback() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) { //is marked as favourite
                    post(EventDetailsEvent.onIsFavourite);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(getClass().getCanonicalName(), databaseError.getMessage());
            }
        });

        //Check if the user has the event marked as "assisting"
        firebaseAPI.getUserAssistingToEvent(userID, eventID, new FirebaseSingleValueEventListenerCallback() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) { //is marked as assisting
                    post(EventDetailsEvent.onIsAssisting);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(getClass().getCanonicalName(), databaseError.getMessage());
            }
        });

        //Check if the user has already rated the event
        firebaseAPI.getUserRatedEvent(userID, eventID, new FirebaseSingleValueEventListenerCallback() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) { //is already rated
                    post(EventDetailsEvent.onAlreadyRated);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(getClass().getCanonicalName(), databaseError.getMessage());
            }
        });
    }

    /**
     * Check if the current location and the event location are near
     * (0.001 degrees of margin in latitude and longitude, about 130meter margin)
     *
     * @param longitude        longitude of the event
     * @param latitude         latitude of the event
     * @param currentLongitude longitud of the user
     * @param currentLatitude  latitude of the user
     */
    @Override
    public void setIAmHere(double longitude, double latitude, double currentLongitude, double currentLatitude) {
        if (Math.abs(currentLatitude - latitude) <= 0.002 && Math.abs(currentLongitude - longitude) <= 0.002) { //is close to the event location, enable rating
            post(EventDetailsEvent.onImHereSucess);
        } else {
            post(EventDetailsEvent.onImHereError);
        }
    }

    /**
     * Rate an event in Firebase. Update the creator's average rating.
     *
     * @param rating  rating to set
     * @param creator creator of the event
     * @param eventID ID of the event to rate
     */
    @Override
    public void rateEvent(float rating, String creator, String eventID) {
        //Update the average rating of the user with the new rating
        firebaseAPI.getSingleUserFromUsername(creator, new FirebaseSingleValueEventListenerCallback() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    User creator = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    int numOfRatings = creator.getNumOfRatings();
                    float avgRating = creator.getAvgRating();
                    numOfRatings += 1;
                    //Calculate the new average with the formula newAvg= oldAvg + (newRating-oldAvg)/numRatings
                    avgRating = avgRating + (rating - avgRating) / numOfRatings;
                    creator.setAvgRating(avgRating);
                    creator.setNumOfRatings(numOfRatings);
                    firebaseAPI.updateUser(creator);
                    String userID = firebaseAPI.getUserUID();
                    firebaseAPI.getSingleUserFromUID(userID, new FirebaseSingleValueEventListenerCallback() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                updateUserAssisted(userID);
                                firebaseAPI.updateRated(userID, eventID);
                                post(EventDetailsEvent.onRatingSucess);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            post(EventDetailsEvent.onRatingError);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                post(EventDetailsEvent.onRatingError);
            }
        });
    }

    /**
     * Write a comment in the event
     *
     * @param eventID id of the event
     * @param comment comment to write
     */
    @Override
    public void writeComment(String eventID, String comment) {
        if (comment != null && !comment.isEmpty() && !comment.equals("")) {
            firebaseAPI.getSingleEventFromEventID(eventID, new FirebaseSingleValueEventListenerCallback() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        Event event = dataSnapshot.getChildren().iterator().next().getValue(Event.class);
                        firebaseAPI.getSingleUserFromUID(firebaseAPI.getUserUID(), new FirebaseSingleValueEventListenerCallback() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() > 0) {
                                    //Add the comment to the hashmap, then update the event in the database
                                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                                    hashMapComments.put(firebaseAPI.create(), new Comment(user.getUsername(), comment, user.getProfilePhotoURL(), String.valueOf(new Date().getTime())));
                                    event.setComments(hashMapComments);
                                    firebaseAPI.updateEvent(event);
                                    post(EventDetailsEvent.onCommentSucessfullySent, event);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    /**
     * Toogle the favourite value fo an event in firebase (add or remove the event to the user's favourites)
     *
     * @param eventID id of the event
     */
    @Override
    public void toogleFavourite(final String eventID) {
        final String userID = firebaseAPI.getUserUID();
        firebaseAPI.getUserSingleFavourite(userID, eventID, new FirebaseSingleValueEventListenerCallback() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) { //Is already a favourite, remove it
                    firebaseAPI.removeFavourite(userID, eventID, new FirebaseActionListenerCallback() {
                        @Override
                        public void onSuccess() {
                            post(EventDetailsEvent.onRemovedFromFavourites);
                        }

                        @Override
                        public void onError(FirebaseError error) {
                            post(EventDetailsEvent.onSetFavouriteError);
                        }
                    });
                } else { //Is not in favourites, add it
                    firebaseAPI.updateFavourite(userID, eventID);
                    post(EventDetailsEvent.onAddedToFavourites);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                post(EventDetailsEvent.onSetFavouriteError);
            }
        });
    }

    /**
     * Toogle the assisting value of an event in firebase (add or remove the event to the user's assisting)
     *
     * @param eventID id of the event
     */
    @Override
    public void toogleAssiting(final String eventID) {
        final String userID = firebaseAPI.getUserUID();
        firebaseAPI.getUserAssistingToEvent(userID, eventID, new FirebaseSingleValueEventListenerCallback() { //check if its currently marked as assisting
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) { //Is already assisting, remove it
                    firebaseAPI.getSingleEventFromEventID(eventID, new FirebaseSingleValueEventListenerCallback() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                final Event event = dataSnapshot.getChildren().iterator().next().getValue(Event.class);
                                Integer currentpeople = event.getCurrentPeople();
                                currentpeople -= 1;
                                event.setCurrentPeople(currentpeople);
                                firebaseAPI.removeAssisting(userID, eventID, new FirebaseActionListenerCallback() {
                                    @Override
                                    public void onSuccess() { //Update the event in Firabase and Algolia
                                        firebaseAPI.updateEvent(event);
                                        algoliaAPI.addorUpdateEvent(event);
                                        post(EventDetailsEvent.onAssistingRemoved, event);
                                    }

                                    @Override
                                    public void onError(FirebaseError error) {
                                        post(EventDetailsEvent.onSetAssistingError);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            post(EventDetailsEvent.onSetAssistingError);
                        }
                    });
                } else { //Is not assisting, check if there is room in the event for more people. If there is, add 1 and add it the user database
                    firebaseAPI.getSingleEventFromEventID(eventID, new FirebaseSingleValueEventListenerCallback() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() > 0) {
                                Event event = dataSnapshot.getChildren().iterator().next().getValue(Event.class);
                                Integer maxpeople = event.getMaxPeople();
                                Integer currentpeople = event.getCurrentPeople();
                                if (currentpeople < maxpeople || maxpeople == 0) {
                                    currentpeople += 1;
                                    event.setCurrentPeople(currentpeople);
                                    //Update the currentpeople in the event
                                    firebaseAPI.updateEvent(event);
                                    algoliaAPI.addorUpdateEvent(event);
                                    //Update the assisting table of the user
                                    firebaseAPI.updateAssisting(userID, eventID);
                                    post(EventDetailsEvent.onAssistingAdded, event);
                                } else {
                                    post(EventDetailsEvent.onEventFullError);
                                }
                            } else {
                                post(EventDetailsEvent.onSetAssistingError);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            post(EventDetailsEvent.onSetAssistingError);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                post(EventDetailsEvent.onSetAssistingError);
            }
        });


    }

    /**
     * Update the number of the event assisted by the user
     *
     * @param userID id of the user
     */
    private void updateUserAssisted(String userID) {
        firebaseAPI.getSingleUserFromUID(userID, new FirebaseSingleValueEventListenerCallback() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    int eventsAssisted = user.getNumEventsAssisted();
                    user.setNumEventsAssisted(eventsAssisted + 1); //add 1 to the num of events assisted
                    firebaseAPI.updateUser(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    void post(int type) {
        post(type, null);
    }

    void post(int type, Event event) {
        post(type, event, null);
    }

    /**
     * Post an event to be catched in the rpesenter
     *
     * @param type
     * @param event_updated
     * @param errorMessage
     */
    void post(int type, Event event_updated, String errorMessage) {
        EventDetailsEvent event = new EventDetailsEvent();
        if (errorMessage != null) {
            event.setErrorMesage(errorMessage);
        }
        if (event_updated != null) {
            event.setEvent(event_updated);
        }
        event.setEventType(type);
        eventBus.post(event);
    }
}
