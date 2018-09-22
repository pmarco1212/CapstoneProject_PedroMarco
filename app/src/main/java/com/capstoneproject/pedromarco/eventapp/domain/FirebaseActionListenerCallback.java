package com.capstoneproject.pedromarco.eventapp.domain;

import com.firebase.client.FirebaseError;

/**
 * Listener interface to listen for completion of Firebase actions
 */
public interface FirebaseActionListenerCallback {
    void onSuccess();

    void onError(FirebaseError error);
}
