package com.capstoneproject.pedromarco.eventapp.settings.edituserprofile;

import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseSingleValueEventListenerCallback;
import com.capstoneproject.pedromarco.eventapp.entities.User;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorage;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorageFinishedListener;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.events.EditUserEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.File;

/**
 * Repository class, handling all the model-side data of the user edition
 */
public class EditUserRepositoryImpl implements EditUserRespository {
    FirebaseAPI firebaseAPI;
    EventBus eventBus;
    ImageStorage imageStorage;
    User user;

    public EditUserRepositoryImpl(FirebaseAPI firebaseAPI, EventBus eventBus, ImageStorage imageStorage) {
        this.firebaseAPI = firebaseAPI;
        this.eventBus = eventBus;
        this.imageStorage = imageStorage;
    }

    /**
     * Update an user in firebase with the given data
     *
     * @param firstname         firstname of the user
     * @param surname           surname of the user
     * @param description       description of the user
     * @param profilepictureURL URL pic of the user
     */
    @Override
    public void editUser(final String firstname, final String surname, final String description, final String profilepictureURL) {
        if (user != null) {
            if (firstname.isEmpty() || surname.isEmpty() || description.isEmpty()) { //If some fields empty, send error
                post(EditUserEvent.onEmptyFieldError);
            } else {
                if (profilepictureURL != null) { //If a new picture was selected, upload it and update the user picture url
                    imageStorage.upload(new File(profilepictureURL), user.getId(), new ImageStorageFinishedListener() {
                        @Override
                        public void onSuccess() {
                            final String url = imageStorage.getImageUrl(user.getId());
                            user.setProfilePhotoURL(url);
                        }

                        @Override
                        public void onError(String error) {
                            post(EditUserEvent.onImageUploadError);
                        }
                    });
                }
                user.setFirstname(firstname);
                user.setSurname(surname);
                user.setDescription(description);
                firebaseAPI.updateUser(user);
                post(EditUserEvent.onUserUpdatedSucess);
            }
        } else {
            post(EditUserEvent.onUserUpdatedError);
        }
    }

    /**
     * Get the user details of the current user from firebase
     */
    @Override
    public void getUserDetails() {
        firebaseAPI.getSingleUserFromUID(firebaseAPI.getUserUID(), new FirebaseSingleValueEventListenerCallback() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    DataSnapshot snapshot = dataSnapshot.getChildren().iterator().next();
                    user = snapshot.getValue(User.class);
                    post(EditUserEvent.onUserDetailsObtainedSucess, user);
                } else {
                    post(EditUserEvent.onUserUpdatedError);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                post(EditUserEvent.onUserUpdatedError, databaseError.toString());
            }
        });
    }


    void post(int type) {
        post(type, null, null);
    }

    void post(int type, User user) {
        post(type, user, null);
    }

    void post(int type, String errormessage) {
        post(type, null, errormessage);
    }

    /**
     * Post an event to be received in the presenter
     *
     * @param type         type
     * @param user         user
     * @param errorMessage errormessage
     */
    void post(int type, User user, String errorMessage) {
        EditUserEvent event = new EditUserEvent();
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
