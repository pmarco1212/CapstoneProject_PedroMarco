package com.capstoneproject.pedromarco.eventapp.lib.base;

import java.io.File;

/**
 * Interface of the image storage with its methods
 */
public interface ImageStorage {
    String getImageUrl(String id);

    void upload(File file, String id, ImageStorageFinishedListener listener);
}
