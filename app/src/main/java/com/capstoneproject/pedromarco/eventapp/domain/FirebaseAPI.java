package com.capstoneproject.pedromarco.eventapp.domain;

import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.entities.User;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import com.pedromarco.myphotofeed.entities.Photo;

/**
 * Class handling all the Firebase logic
 */
public class FirebaseAPI {
    private Firebase firebase;
    private DatabaseReference mEventDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private ChildEventListener eventListener;
    private ChildEventListener favouritesListener;
    private ChildEventListener searchListener;
    private static final String EVENTS_TABLE = "events";
    private static final String USERS_TABLE = "users";
    private static final String FAVOURITES_SUBTABLE = "favourite_events";
    private static final String ASSISTING_SUBTABLE = "assisting_events";
    private static final String RATED_SUBTABLE = "rated_events";
    private static final String CREATED_SUBTABLE = "created_events";

    public FirebaseAPI(Firebase firebase) {
        this.firebase = firebase;
        mEventDatabaseReference = FirebaseDatabase.getInstance().getReference(EVENTS_TABLE);
        mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference(USERS_TABLE);
    }

    /**
     * Subscribe to the Favourite table of an user the database for updates
     *
     * @param uid      id of the user to check
     * @param listener listener for callbacks
     */
    public void suscribeToFavouritesFromUser(String uid, final FirebaseEventListenerCallback listener) {
        if (favouritesListener == null) {
            favouritesListener = new ChildEventListener() {
                @Override
                public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    listener.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    listener.onChildChanged(dataSnapshot);
                }

                @Override
                public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                    listener.onChildRemoved(dataSnapshot);
                }

                @Override
                public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onCancelled(databaseError);
                }
            };
            mUsersDatabaseReference.child(uid).child(FAVOURITES_SUBTABLE).addChildEventListener(favouritesListener);
        }
    }

    /**
     * Search for an Event in the database with a given event ID
     *
     * @param eventID  the event ID
     * @param listener the listener for callbacks
     */
    public void getSingleEventFromEventID(String eventID, final FirebaseSingleValueEventListenerCallback listener) {
        mEventDatabaseReference.orderByChild("id").equalTo(eventID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError);
            }
        });
    }

    /**
     * Searches for an User in the database with a given Username
     *
     * @param username the username
     * @param listener the listener for callbacks
     */
    public void getSingleUserFromUsername(String username, final FirebaseSingleValueEventListenerCallback listener) {
        mUsersDatabaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError);
            }
        });
    }

    /**
     * Searches for an user with the given UID in the database
     *
     * @param uid      the user ID
     * @param listener the listener for callbacks
     */
    public void getSingleUserFromUID(String uid, final FirebaseSingleValueEventListenerCallback listener) {
        mUsersDatabaseReference.orderByChild("id").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError);
            }
        });
    }

    /**
     * Check if an event is marked as favourite by a given user
     *
     * @param uid      the user ID
     * @param eventID  the event ID
     * @param listener the listener for callbacks
     */
    public void getUserSingleFavourite(String uid, String eventID, final FirebaseSingleValueEventListenerCallback listener) {
        mUsersDatabaseReference.child(uid).child(FAVOURITES_SUBTABLE).orderByValue().equalTo(eventID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError);
            }
        });
    }

    /**
     * Check if an event is marked as assisting by a given user
     *
     * @param uid      the user ID
     * @param eventID  the event ID
     * @param listener the listener for callbacks
     */
    public void getUserAssistingToEvent(String uid, String eventID, final FirebaseSingleValueEventListenerCallback listener) {
        mUsersDatabaseReference.child(uid).child(ASSISTING_SUBTABLE).orderByValue().equalTo(eventID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError);
            }
        });
    }

    /**
     * Check if an event has already been rated by a given user
     *
     * @param uid      the UID
     * @param eventID  the event ID
     * @param listener the listener for callbacks
     */
    public void getUserRatedEvent(String uid, String eventID, final FirebaseSingleValueEventListenerCallback listener) {
        mUsersDatabaseReference.child(uid).child(RATED_SUBTABLE).orderByValue().equalTo(eventID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError);
            }
        });
    }

    /**
     * Get a new key to insert into firebase
     *
     * @return a new key
     */
    public String create() {
        return firebase.push().getKey();
    }

    /**
     * Add or update a given event in the database
     *
     * @param event the event do update
     */
    public void updateEvent(Event event) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(EVENTS_TABLE).child(event.getId()).setValue(event);
    }

    /**
     * Add or updateser a given user in the database
     *
     * @param user the user to add or update
     */
    public void updateUser(User user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(USERS_TABLE).child(user.getId()).setValue(user);
    }

    /**
     * Add a favourite event to a given user
     *
     * @param userID  the UID
     * @param eventID the eventID
     */
    public void updateFavourite(String userID, String eventID) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(USERS_TABLE).child(userID).child(FAVOURITES_SUBTABLE).child(eventID).setValue(eventID);
    }

    /**
     * Remove a favourite event from a given user
     *
     * @param userID   the UID
     * @param eventID  the event ID
     * @param listener the listener for callbacks
     */
    public void removeFavourite(String userID, String eventID, FirebaseActionListenerCallback listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(USERS_TABLE).child(userID).child(FAVOURITES_SUBTABLE).child(eventID).removeValue();
        listener.onSuccess();
    }

    /**
     * Add assisting of a given user to a given event
     *
     * @param userID  the UID
     * @param eventID the EventID
     */
    public void updateAssisting(String userID, String eventID) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(USERS_TABLE).child(userID).child(ASSISTING_SUBTABLE).child(eventID).setValue(eventID);
    }

    /**
     * Remove assisting form a given user to a given event
     *
     * @param userID   the UID
     * @param eventID  the eventID
     * @param listener the listeners for callbacks
     */
    public void removeAssisting(String userID, String eventID, FirebaseActionListenerCallback listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(USERS_TABLE).child(userID).child(ASSISTING_SUBTABLE).child(eventID).removeValue();
        listener.onSuccess();
    }

    /**
     * Add a given event to the list of an User's rated events
     *
     * @param userID  the UID
     * @param eventID the EventID
     */
    public void updateRated(String userID, String eventID) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(USERS_TABLE).child(userID).child(RATED_SUBTABLE).child(eventID).setValue(eventID);
    }

    /**
     * Gets the email of the currently authenticated user
     *
     * @return the email if found, or null otherwise
     */
    public String getAuthEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }

    /**
     * Get the UID of the current user signed in Firebase
     *
     * @return the UID or null if not found
     */
    public String getUserUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    /**
     * Check if there is an active sesion in Firebase
     *
     * @param listener lister for the callbacks
     */
    public void checkForSession(FirebaseActionListenerCallback listener) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            listener.onSuccess();
        } else {
            listener.onError(null);
        }
    }

    /**
     * Logout from Firebase
     */
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }
}

