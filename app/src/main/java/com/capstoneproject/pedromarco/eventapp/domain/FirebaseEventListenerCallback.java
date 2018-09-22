package com.capstoneproject.pedromarco.eventapp.domain;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * The Method of the callback interface when subscribing to updates in the firebase database
 */
public interface FirebaseEventListenerCallback {
    void onChildAdded(DataSnapshot dataSnapshot);

    void onChildRemoved(DataSnapshot dataSnapshot);

    void onChildChanged(DataSnapshot dataSnapshot);

    void onCancelled(DatabaseError error);
}
