package com.capstoneproject.pedromarco.eventapp.domain;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Interface with the callback methods for firebase single values checks
 */
public interface FirebaseSingleValueEventListenerCallback {
    void onDataChange(DataSnapshot dataSnapshot);

    void onCancelled(DatabaseError databaseError);
}
