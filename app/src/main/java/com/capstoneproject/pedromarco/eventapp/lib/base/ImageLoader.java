package com.capstoneproject.pedromarco.eventapp.lib.base;

import android.widget.ImageView;

/**
 * Interface of the image loader with its methods
 */
public interface ImageLoader {
    void load(ImageView imageView, String URL);

    void loadSmall(ImageView imageView, String URL);
}
