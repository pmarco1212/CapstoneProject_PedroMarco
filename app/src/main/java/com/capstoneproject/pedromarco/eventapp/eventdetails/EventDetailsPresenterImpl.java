package com.capstoneproject.pedromarco.eventapp.eventdetails;

import com.capstoneproject.pedromarco.eventapp.eventdetails.event.EventDetailsEvent;
import com.capstoneproject.pedromarco.eventapp.eventdetails.ui.EventDetailsView;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Presenter class implementing the presenter interface
 */
public class EventDetailsPresenterImpl implements EventDetailsPresenter {
    EventBus eventBus;
    EventDetailsView view;
    EventDetailsInteractor interactor;

    public EventDetailsPresenterImpl(EventBus eventBus, EventDetailsView view, EventDetailsInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
    }

    /**
     * Registers Eventbus
     */
    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    /**
     * Unregisters eventbus
     */
    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    /**
     * Tell interactor to get event details
     */
    @Override
    public void getEventDetails(String eventID) {
        interactor.executegetEventDetails(eventID);
    }

    /**
     * Tell interactor to toogle assistance
     */
    @Override
    public void toogleAssiting(String eventID) {
        interactor.executeToogleAssiting(eventID);
    }

    /**
     * Tell interactor to set the im here
     */
    @Override
    public void setIAmHere(double longitude, double latitude, double currentLongitude, double currentLatitude) {
        interactor.executeSetIAmHere(longitude, latitude, currentLongitude, currentLatitude);
    }


    /**
     * Tell interactor to rate event
     */
    @Override
    public void rateEvent(float rating, String creator, String eventID) {
        interactor.executeRateEvent(rating, creator, eventID);
    }

    /**
     * Tell interactor to toogle favourite
     */
    @Override
    public void toogleFavourite(String eventID) {
        interactor.executeToogleFavourite(eventID);
    }

    /**
     * Tell interactor to write a comment
     */
    @Override
    public void writeComment(String eventID, String comment) {
        interactor.executeWriteComment(eventID, comment);
    }

    /**
     * Catch events and execute the appropiate methods in the view
     *
     * @param event event received
     */
    @Subscribe
    @Override
    public void onEventMainThread(EventDetailsEvent event) {
        if (event != null) {
            switch (event.getEventType()) {
                case EventDetailsEvent.onAddedToFavourites:
                    view.toogleFavourite(true);
                    break;
                case EventDetailsEvent.onRemovedFromFavourites:
                    view.toogleFavourite(false);
                    break;
                case EventDetailsEvent.onSetFavouriteError:
                    view.setFavouriteError();
                    break;
                case EventDetailsEvent.onIsFavourite:
                    view.setIsFavourite();
                    break;
                case EventDetailsEvent.onAssistingAdded:
                    view.toogleAssisting(true, event.getEvent());
                    break;
                case EventDetailsEvent.onAssistingRemoved:
                    view.toogleAssisting(false, event.getEvent());
                    break;
                case EventDetailsEvent.onIsAssisting:
                    view.setIsAssisting();
                    break;
                case EventDetailsEvent.onSetAssistingError:
                    view.setAssistingError();
                    break;
                case EventDetailsEvent.onEventFullError:
                    view.setEventFullError();
                    break;
                case EventDetailsEvent.onImHereSucess:
                    view.iAmHereCheckedAndAllowRating();
                    break;
                case EventDetailsEvent.onImHereError:
                    view.setImHereError();
                    break;
                case EventDetailsEvent.onRatingError:
                    view.ratingError();
                    break;
                case EventDetailsEvent.onRatingSucess:
                    view.ratingSucess();
                    view.setAlreadyRated();
                    break;
                case EventDetailsEvent.onAlreadyRated:
                    view.setAlreadyRated();
                    break;
                case EventDetailsEvent.onEventDetailsObtained:
                    view.fillLayout(event.getEvent());
                    break;
                case EventDetailsEvent.onCommentSucessfullySent:
                    view.clearEditTextAndCloseKeyboard();
                    view.updateComments(new ArrayList<>(event.getEvent().getComments().values()));
                    break;

            }
        }
    }
}
