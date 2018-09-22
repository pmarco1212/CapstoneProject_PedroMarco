package com.capstoneproject.pedromarco.eventapp.userdetails;

import com.capstoneproject.pedromarco.eventapp.userdetails.events.UserDetailsEvent;

/**
 * Interface stating the methods of the userdetails presenter
 */
public interface UserDetailsPresenter {
    void onCreate();

    void onDestroy();

    void getUserData(String username);

    void onEventMainThread(UserDetailsEvent event);
}
