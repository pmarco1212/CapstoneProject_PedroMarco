package com.capstoneproject.pedromarco.eventapp.eventdetails.ui;

import com.capstoneproject.pedromarco.eventapp.entities.Comment;
import com.capstoneproject.pedromarco.eventapp.entities.Event;

import java.util.List;

/**
 * Interface stating the methods of the view
 */
public interface EventDetailsView {
    void setIsFavourite();

    void setIsAssisting();

    void setEventStarted();

    void iAmHereCheckedAndAllowRating();

    void toogleFavourite(Boolean isFavourite);

    void setFavouriteError();

    void toogleAssisting(Boolean isAssisting, Event event);

    void setAssistingError();

    void setEventFullError();

    void setImHereError();

    void setAlreadyRated();

    void ratingSucess();

    void ratingError();

    void fillLayout(Event event);

    void clearEditTextAndCloseKeyboard();

    void updateComments(List<Comment> comments);
}
