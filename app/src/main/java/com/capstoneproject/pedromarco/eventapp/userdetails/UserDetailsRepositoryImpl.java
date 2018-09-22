package com.capstoneproject.pedromarco.eventapp.userdetails;

import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseSingleValueEventListenerCallback;
import com.capstoneproject.pedromarco.eventapp.entities.User;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.userdetails.events.UserDetailsEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Repository class, handling all the model-side data of the user details
 */
public class UserDetailsRepositoryImpl implements UserDetailsRepository {
    EventBus eventBus;
    FirebaseAPI firebaseAPI;

    public UserDetailsRepositoryImpl(EventBus eventBus, FirebaseAPI firebaseAPI) {
        this.eventBus = eventBus;
        this.firebaseAPI = firebaseAPI;
    }

    /**
     * Get an user form firebase witht he given username
     *
     * @param userName
     */
    @Override
    public void getUserData(String userName) {
        if (userName != null && !userName.isEmpty()) {
            post(UserDetailsEvent.onUserReadStart);
            firebaseAPI.getSingleUserFromUsername(userName, new FirebaseSingleValueEventListenerCallback() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        DataSnapshot snapshot = dataSnapshot.getChildren().iterator().next();
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            post(UserDetailsEvent.onUserReadSucess, user);
                        } else {
                            post(UserDetailsEvent.onUserReadError);
                        }
                    } else {
                        post(UserDetailsEvent.onUserReadError);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    post(UserDetailsEvent.onUserReadError, databaseError.getMessage());
                }
            });
        } else {
            post(UserDetailsEvent.onUserReadError);
        }
    }


    void post(int type) {
        post(type, null, null);
    }

    void post(int type, User user) {
        post(type, user, null);
    }

    void post(int type, String error) {
        post(type, null, error);
    }

    /**
     * post an event to be caught in the presenter
     *
     * @param type         type of the event
     * @param user         user object
     * @param errorMessage error message
     */
    void post(int type, User user, String errorMessage) {
        UserDetailsEvent event = new UserDetailsEvent();
        if (errorMessage != null) {
            event.setErrorMesage(errorMessage);
        }
        if (user != null) {
            event.setUser(user);
        }
        event.setEventType(type);
        eventBus.post(event);
    }
}
