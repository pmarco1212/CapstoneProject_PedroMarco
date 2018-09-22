package com.capstoneproject.pedromarco.eventapp.domain.di;

import android.content.Context;
import android.location.Geocoder;

import com.algolia.search.saas.Client;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.domain.AlgoliaAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.domain.Util;
import com.firebase.client.Firebase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module providing all the domain package dependencies
 */
@Module
public class DomainModule {
    private final static String FIREBASE_URL = "https://eventapp-3f1a4.firebaseio.com/";

    @Provides
    @Singleton
    FirebaseAPI providesFirebaseAPI(Firebase firebase) {
        return new FirebaseAPI(firebase);
    }

    @Provides
    @Singleton
    Firebase providesFirebase() {
        return new Firebase(FIREBASE_URL);
    }

    @Provides
    @Singleton
    AlgoliaAPI providesAlgoliaAPI(Client client) {
        return new AlgoliaAPI(client);
    }

    @Provides
    @Singleton
    Client providesAlgoliaClient(Context context) {
        return new Client(context.getString(R.string.ALGOLIA_APLICATION_ID), context.getString(R.string.ALGOLIA_API_KEY));
    }

    @Provides
    @Singleton
    Util providesUtil() {
        return new Util();
    }

    @Provides
    @Singleton
    Geocoder providesGeocoder(Context context) {
        return new Geocoder(context);
    }
}
