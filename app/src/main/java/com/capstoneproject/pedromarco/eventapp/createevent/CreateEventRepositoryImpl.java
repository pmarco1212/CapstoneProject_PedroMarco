package com.capstoneproject.pedromarco.eventapp.createevent;

import android.util.Log;

import com.capstoneproject.pedromarco.eventapp.createevent.event.CreateEventEvent;
import com.capstoneproject.pedromarco.eventapp.domain.AlgoliaAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseSingleValueEventListenerCallback;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.entities.User;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorage;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorageFinishedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.File;

/**
 * Repository class, handling all the model-side data of the user creation
 */
public class CreateEventRepositoryImpl implements CreateEventRepository {
    EventBus eventBus;
    FirebaseAPI firebaseAPI;
    AlgoliaAPI algoliaAPI;
    ImageStorage imageStorage;

    public CreateEventRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI, AlgoliaAPI algoliaAPI, ImageStorage imageStorage) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
        this.imageStorage = imageStorage;
        this.algoliaAPI = algoliaAPI;
    }

    /**
     * Create an event with the given parameters
     *
     * @param eventName   name
     * @param description description
     * @param category    category
     * @param maxPeople   max num of people
     * @param date        date
     * @param time        time
     * @param location    location
     * @param latitude    latitude
     * @param longitude   longitude
     * @param picturePath path of the picture
     */
    @Override
    public void createEvent(final String eventName, final String description, final String category, final int maxPeople, final String date, final String time, final String location, final double latitude, final double longitude, final String picturePath) {
        if (!eventName.isEmpty()) {
            if (!description.isEmpty()) {
                if (!date.isEmpty()) {
                    if (!time.isEmpty()) {
                        if (!location.isEmpty() && longitude != 0 && latitude != 0) {
                            if (!picturePath.isEmpty()) {
                                //Get an ID fro the event and pciture, and try to upload the picture to cloudinary
                                final String eventID = firebaseAPI.create();
                                Log.d("Uploadingg", picturePath);
                                imageStorage.upload(new File(picturePath), eventID, new ImageStorageFinishedListener() {
                                    @Override
                                    public void onSuccess() {
                                        //If the image was correctly uploaded, get the image URL. Then get the current username from the database
                                        final String url = imageStorage.getImageUrl(eventID);
                                        firebaseAPI.getSingleUserFromUID(firebaseAPI.getUserUID(), new FirebaseSingleValueEventListenerCallback() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.getChildrenCount() > 0) {
                                                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                                                    String creatorUsername = user.getUsername();
                                                    //Insert the Event to the Firebase database
                                                    Event event = new Event(date, time, eventName, description, 0, category, location, latitude, longitude, url, maxPeople, 0, creatorUsername, null);
                                                    event.setId(eventID);
                                                    //Add the event to Algolia database as well
                                                    if (algoliaAPI.addorUpdateEvent(event)) { //If added sucesfully to algolia, insert to firebase and update user info and send event
                                                        firebaseAPI.updateEvent(event);
                                                        updateUserEventsCreated(user);
                                                        post(CreateEventEvent.onEventCreatedSuccess);
                                                    } else { //there was an error while adding to algolia
                                                        Log.e("CREATE EVENT REPOSITORY", "Error while adding event to algolia");
                                                        post(CreateEventEvent.onEventCreatedError);
                                                    }
                                                } else {
                                                    Log.e("CREATE EVENT REPOSITORY", "Username not found > " + firebaseAPI.getUserUID());
                                                    post(CreateEventEvent.onEventCreatedError);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                post(CreateEventEvent.onEventCreatedError);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String error) {
                                        Log.e("ERRORRR", error);
                                        post(CreateEventEvent.onPictureUploadError);
                                    }
                                });
                            } else {
                                post(CreateEventEvent.onPictureUploadError);
                            }
                        } else {
                            post(CreateEventEvent.onInvalidLocationError);
                        }
                    } else {
                        post(CreateEventEvent.onEmptyTimeError);
                    }
                } else {
                    post(CreateEventEvent.onInvalidDateError);
                }
            } else {
                post(CreateEventEvent.onEmptyDescriptionError);
            }
        } else {
            post(CreateEventEvent.onEmptyNameError);
        }
    }

    /**
     * Update the number of created events of a given user (add 1)
     *
     * @param user user to update
     */
    void updateUserEventsCreated(User user) {
        Integer eventsCreated = user.getNumEventCreated();
        eventsCreated += 1;
        user.setNumEventCreated(eventsCreated);
        firebaseAPI.updateUser(user);
    }

    void post(int type) {
        post(type, null);
    }

    /**
     * Post an event
     *
     * @param type
     * @param errorMessage
     */
    void post(int type, String errorMessage) {
        CreateEventEvent event = new CreateEventEvent();
        if (errorMessage != null) {
            event.setErrorMesage(errorMessage);
        }
        event.setEventType(type);
        eventBus.post(event);
    }
}
