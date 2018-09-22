package com.capstoneproject.pedromarco.eventapp.newuser;

import com.capstoneproject.pedromarco.eventapp.newuser.events.NewUserEvent;

/**
 * Interface stating the methods of the presenter
 */
public interface NewUserPresenter {
    void onCreate();

    void onDestroy();

    void onEventMainThread(NewUserEvent event);

    void addNewUser(String username, String firstname, String surname, String description, String profilepictureURL);
}
