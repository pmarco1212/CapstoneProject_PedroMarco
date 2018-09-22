package com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.ui;

import com.capstoneproject.pedromarco.eventapp.entities.User;

/**
 * Interface stating the methods of the view
 */
public interface EditUserView {
    void editUserSuccess();

    void editUserError(String errorMessage);

    void emptyFieldError();

    void onImageUploadError();

    void fillUserDetails(User user);
}
