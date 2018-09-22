package com.capstoneproject.pedromarco.eventapp.newuser;

import android.util.Log;

import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseSingleValueEventListenerCallback;
import com.capstoneproject.pedromarco.eventapp.entities.User;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorage;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorageFinishedListener;
import com.capstoneproject.pedromarco.eventapp.newuser.events.NewUserEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.File;

/**
 * Repository class, handling all the model-side data of the
 */
public class NewUserRepositoryImpl implements NewUserRespository {
    FirebaseAPI firebaseAPI;
    EventBus eventBus;
    ImageStorage imageStorage;

    public NewUserRepositoryImpl(FirebaseAPI firebaseAPI, EventBus eventBus, ImageStorage imageStorage) {
        this.firebaseAPI = firebaseAPI;
        this.eventBus = eventBus;
        this.imageStorage = imageStorage;
    }

    /**
     * Add a new user to firebase with the given parameters
     *
     * @param username          user name
     * @param firstname         first name
     * @param surname           surname
     * @param description       description
     * @param profilepictureURL URL of the profile pic
     */
    @Override
    public void addNewUser(final String username, final String firstname, final String surname, final String description, final String profilepictureURL) {
        if (username != null && !username.isEmpty() && firstname != null && !firstname.isEmpty() && surname != null && !surname.isEmpty() && description != null && !description.isEmpty()) {
            if (profilepictureURL != null && !profilepictureURL.isEmpty()) {
                //Check if the username is already in use and postes the corresponding event
                firebaseAPI.getSingleUserFromUsername(username, new FirebaseSingleValueEventListenerCallback() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) { //the username is already taken
                            post(NewUserEvent.onUsernameAlreadyInUse);
                            Log.d("REPOSITORY", "errrrr Username already in use");
                        } else { //The username is not taken
                            //Get an ID fro the event and pciture, and try to upload the picture to cloudinary
                            final String userID = firebaseAPI.getUserUID();
                            Log.d("Uploadingg", profilepictureURL);
                            imageStorage.upload(new File(profilepictureURL), userID, new ImageStorageFinishedListener() {
                                @Override
                                public void onSuccess() {
                                    final String url = imageStorage.getImageUrl(userID);
                                    firebaseAPI.updateUser(new User(userID, username, firstname, surname, description, url, 0, 0, 0, 0, null, null, null));
                                    post(NewUserEvent.onUserCreatedSucess);
                                    Log.d("REPOSITORY", "Username not in use, user added");
                                }

                                @Override
                                public void onError(String error) {
                                    post(NewUserEvent.onImageUploadError);
                                    Log.e("Cloudinary", error);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        post(NewUserEvent.onUserCreatedError, databaseError.getMessage());
                    }
                });
            } else {
                post(NewUserEvent.onImageUploadError);
            }
        } else { //Send error of empty field in event
            post(NewUserEvent.onEmptyFieldError);
        }
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
        NewUserEvent event = new NewUserEvent();
        if (errorMessage != null) {
            event.setErrorMesage(errorMessage);
        }
        event.setEventType(type);
        eventBus.post(event);
    }
}
