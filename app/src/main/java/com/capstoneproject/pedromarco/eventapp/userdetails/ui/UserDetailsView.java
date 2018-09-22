package com.capstoneproject.pedromarco.eventapp.userdetails.ui;

import com.capstoneproject.pedromarco.eventapp.entities.User;

/**
 * Interface stating the methods of the userdetails view
 */
public interface UserDetailsView {
    void showProgress();

    void hideProgress();

    void userDetailsError(String error);

    void setLayoutFields(User user);
}
