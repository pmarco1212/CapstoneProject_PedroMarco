package com.capstoneproject.pedromarco.eventapp.lib.di;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.capstoneproject.pedromarco.eventapp.lib.CloudinaryImageStorage;
import com.capstoneproject.pedromarco.eventapp.lib.GlideImageLoader;
import com.capstoneproject.pedromarco.eventapp.lib.GreenRobotEventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageLoader;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module with all the dependencies of the Libs class
 */
@Module
public class LibsModule {
    private Fragment fragment;

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }


    @Provides
    @Singleton
    EventBus providesEventBus() {
        return new GreenRobotEventBus();
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader(Fragment fragment) {
        GlideImageLoader imageLoader = new GlideImageLoader();
        if (fragment != null) {
            imageLoader.setLoaderContext(fragment);
        }
        return imageLoader;
    }

    @Provides
    @Singleton
    ImageStorage providesImageStorage(Context context) {
        ImageStorage imageStorage = new CloudinaryImageStorage(context);
        return imageStorage;
    }

    @Provides
    @Singleton
    Fragment providesFragment() {
        return this.fragment;
    }

}
