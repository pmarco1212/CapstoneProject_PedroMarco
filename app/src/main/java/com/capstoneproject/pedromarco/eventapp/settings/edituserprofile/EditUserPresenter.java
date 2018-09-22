package com.capstoneproject.pedromarco.eventapp.settings.edituserprofile;

import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.events.EditUserEvent;

/**
 * Interface stating the methods of the presenter
 */
public interface EditUserPresenter {
    void onCreate();

    void onDestroy();

    void onEventMainThread(EditUserEvent event);

    void editUser(String firstname, String surname, String description, String profilepictureURL);
}
