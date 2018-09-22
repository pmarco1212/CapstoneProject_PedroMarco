package com.capstoneproject.pedromarco.eventapp.lib.base;

/**
 * Interface with the listener of the Image Storage
 */
public interface ImageStorageFinishedListener {
    void onSuccess();

    void onError(String error);
}
