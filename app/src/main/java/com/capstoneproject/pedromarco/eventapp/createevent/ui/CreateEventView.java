package com.capstoneproject.pedromarco.eventapp.createevent.ui;

/**
 * Interface stating the methods of the view
 */
public interface CreateEventView {
    void returnToMainactivity();

    void setNameEmptyError();

    void setDescriptionEmptyError();

    void setDateError();

    void setTimeEmptyError();

    void setLocationError();

    void setPictureError();

    void createEventError();

    void enableCreateButton();

    void showProgress();

    void hideProgress();
}
