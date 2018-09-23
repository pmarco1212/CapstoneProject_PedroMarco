package com.capstoneproject.pedromarco.eventapp.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseActionListenerCallback;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseSingleValueEventListenerCallback;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.login.events.LoginEvent;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Repository class, handling all the model-side data of the login
 */
public class LoginRepositoryImpl implements LoginRepository {
    FirebaseAPI firebase;
    EventBus eventBus;

    public LoginRepositoryImpl(FirebaseAPI firebaseAPI, EventBus eventBus) {
        this.firebase = firebaseAPI;
        this.eventBus = eventBus;
    }

    /**
     * Method that tries to sign up an user into firebase.
     * Post the corresponding event of success/fail with eventbus to be caught in the presenter
     *
     * @param email    the user email
     * @param password the user password
     */
    @Override
    public void signUpFirebase(final String email, final String password) {
        if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            post(LoginEvent.onFirebaseSignUpSuccess);
                            loginFirebase(email, password);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            post(LoginEvent.onSignUpError, e.getMessage());
                        }
                    });
        }
    }

    /**
     * Method to check if the current user is already registered in the User database (with the UID)
     * Post the corresponding event of success/fail with eventbus to be caught in the presenter
     */
    @Override
    public void checkIfUserIsRegisteredInDatabase() {
        firebase.getSingleUserFromUID(firebase.getUserUID(), new FirebaseSingleValueEventListenerCallback() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) { //the user is already registered
                    post(LoginEvent.onUserIsRegistered);
                } else { //The user is not registered yet
                    post(LoginEvent.onUserIsNotRegistered);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                post(LoginEvent.onSignInError);
            }
        });
    }

    /**
     * Method that tries to sign up an user into firebase. If the provided email and password are empty or null, check if there are any active sessions in Firebase.
     * Post the corresponding event of success/fail with eventbus to be caught in the presenter.
     *
     * @param email    the email of the user
     * @param password the password of the user
     */
    @Override
    public void loginFirebase(String email, String password) {
        //If its not
        if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
            try {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                String email = firebase.getAuthEmail();
                                post(LoginEvent.onFirebaseSignInSuccess, null, email);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                post(LoginEvent.onSignInError, e.getMessage());
                            }
                        });
            } catch (Exception e) {
                post(LoginEvent.onSignInError, e.getMessage());
            }
        } else {
            firebase.checkForSession(new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    String email = firebase.getAuthEmail();
                    post(LoginEvent.onSessionRecovered, null, email);
                }

                @Override
                public void onError(FirebaseError error) {
                    post(LoginEvent.onFailedToRecoverSession);
                }
            });
        }
    }

    private void post(int type) {
        post(type, null);
    }

    private void post(int type, String errorMessage) {
        post(type, errorMessage, null);
    }

    /**
     * Method to post an eventbus event (to be read in the presenter) with the appropriate parameters.
     *
     * @param type            type of the event ot post
     * @param errorMessage    errormessage to send
     * @param loggedUserEmail email of the logged in user to send with the eventbus event
     */
    private void post(int type, String errorMessage, String loggedUserEmail) {
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setEventType(type);
        if (errorMessage != null) {
            loginEvent.setErrorMesage(errorMessage);
        }
        loginEvent.setLoggedUserEmail(loggedUserEmail);
        eventBus.post(loginEvent);
    }
}
