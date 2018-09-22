package com.capstoneproject.pedromarco.eventapp.lib;

import android.content.Context;
import android.os.AsyncTask;

import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorage;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorageFinishedListener;
import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.util.Map;

/**
 * Class with the Image storage logic
 */
public class CloudinaryImageStorage implements ImageStorage {

    private Cloudinary cloudinary;

    public CloudinaryImageStorage(Context context) {
        this.cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(context));
    }

    /**
     * Get an URL for a new image with a given ID
     *
     * @param id id of the event/user used to generate the image URL
     * @return the generated URL
     */
    @Override
    public String getImageUrl(String id) {
        return cloudinary.url().generate(id);
    }

    /**
     * Upload a given image to Cloudinary
     *
     * @param file     file to upload
     * @param id       Id of the image to upload
     * @param listener listener to listen for success/error
     */
    @Override
    public void upload(final File file, final String id, final ImageStorageFinishedListener listener) {
        new AsyncTask<Void, Void, Void>() {
            boolean success = false;

            @Override
            protected Void doInBackground(Void... voids) {
                Map params = ObjectUtils.asMap("public_id", id);

                try {
                    cloudinary.uploader().upload(file, params);
                    success = true;
                } catch (Exception e) {
                    listener.onError(e.getMessage());
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (success) {
                    listener.onSuccess();
                }
            }
        }.execute();
    }


}
