package com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites;

import com.capstoneproject.pedromarco.eventapp.main.events.FavouritesQueryEvent;

/**
 * Interface stating the methods of the presenter
 */
public interface FavouritesPresenter {
    void getFavouriteEvents();

    void onEventMainThread(FavouritesQueryEvent event);

    void onCreate();

    void onDestroy();
}

