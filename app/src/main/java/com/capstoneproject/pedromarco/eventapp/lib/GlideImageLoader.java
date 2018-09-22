package com.capstoneproject.pedromarco.eventapp.lib;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageLoader;

/**
 * Class with the Glide Image Loader logic
 */
public class GlideImageLoader implements ImageLoader {
    private RequestManager glideRequestManager;

    public void setLoaderContext(Fragment fragment) {
        this.glideRequestManager = Glide.with(fragment);
    }

    /**
     * Method to load a image form a URL into a Imageview
     *
     * @param imageView imageview to load the imag einto
     * @param URL       url of the source image
     */
    @Override
    public void load(ImageView imageView, String URL) {
        glideRequestManager
                .load(URL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.override(800, 800)
                .centerCrop()
                .into(imageView);
    }

    /**
     * Method to load a image form a URL into a Imageview with small size
     *
     * @param imageView imageview to load the imag einto
     * @param URL       url of the source image
     */
    @Override
    public void loadSmall(ImageView imageView, String URL) {
        glideRequestManager
                .load(URL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(60, 60)
                .into(imageView);
    }

    /**
     * Method to load a image form a URL into a Imageview with a given context
     *
     * @param imageView imageview to load the imag einto
     * @param URL       url of the source image
     * @param context   current context
     */
    public static void loadWithContext(ImageView imageView, String URL, Context context) {
        Glide.with(context)
                .load(URL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.override(800, 800)
                .centerCrop()
                .into(imageView);
    }

    /**
     * Method to load a image form a URL into a Imageview with small size and a given context
     *
     * @param imageView imageview to load the imag einto
     * @param URL       url of the source image
     * @param context   current context
     */
    public static void loadWithContextSmall(ImageView imageView, String URL, Context context) {
        Glide.with(context)
                .load(URL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(60, 60)
                .into(imageView);
    }

}
