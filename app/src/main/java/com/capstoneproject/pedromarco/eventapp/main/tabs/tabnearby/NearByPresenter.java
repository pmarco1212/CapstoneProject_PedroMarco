package com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby;

import com.capstoneproject.pedromarco.eventapp.main.events.NearbyQueryEvent;

/**
 * Interface stating the methods of the presenter
 */
public interface NearByPresenter {
    void getNearByEvents();

    void onEventMainThread(NearbyQueryEvent event);

    void onCreate();

    void onDestroy();
}
