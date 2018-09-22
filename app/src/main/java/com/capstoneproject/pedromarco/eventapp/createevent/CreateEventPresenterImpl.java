package com.capstoneproject.pedromarco.eventapp.createevent;

import com.capstoneproject.pedromarco.eventapp.createevent.event.CreateEventEvent;
import com.capstoneproject.pedromarco.eventapp.createevent.ui.CreateEventView;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Presenter class implementing the presenter interface
 */
public class CreateEventPresenterImpl implements CreateEventPresenter {
    CreateEventView view;
    CreateEventInteractor interactor;
    EventBus eventBus;

    public CreateEventPresenterImpl(CreateEventView view, CreateEventInteractor interactor, EventBus eventBus) {
        this.view = view;
        this.interactor = interactor;
        this.eventBus = eventBus;
    }

    /**
     * Registers eventbus
     */
    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    /**
     * Unegisters eventbus
     */
    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    /**
     * Listens for event and execute the appropiate view methods
     *
     * @param event event obtained
     */
    @Subscribe
    @Override
    public void onEventMainThread(CreateEventEvent event) {
        int type;
        if (event != null) {
            type = event.getEventType();
            switch (type) {
                case CreateEventEvent.onEmptyNameError:
                    view.setNameEmptyError();
                    view.enableCreateButton();
                    view.hideProgress();
                    break;
                case CreateEventEvent.onEmptyDescriptionError:
                    view.setDescriptionEmptyError();
                    view.enableCreateButton();
                    view.hideProgress();
                    break;
                case CreateEventEvent.onInvalidDateError:
                    view.setDateError();
                    view.enableCreateButton();
                    view.hideProgress();
                    break;
                case CreateEventEvent.onEmptyTimeError:
                    view.setTimeEmptyError();
                    view.enableCreateButton();
                    view.hideProgress();
                    break;
                case CreateEventEvent.onInvalidLocationError:
                    view.setLocationError();
                    view.enableCreateButton();
                    view.hideProgress();
                    break;
                case CreateEventEvent.onPictureUploadError:
                    view.setPictureError();
                    view.enableCreateButton();
                    view.hideProgress();
                    break;
                case CreateEventEvent.onEventCreatedError:
                    view.createEventError();
                    view.enableCreateButton();
                    view.hideProgress();
                    break;
                case CreateEventEvent.onEventCreatedSuccess:
                    view.returnToMainactivity();
                    break;
            }
        }
    }

    /**
     * Tell the interactor to create an event with the given values
     *
     * @param eventName
     * @param description
     * @param category
     * @param maxPeople
     * @param date
     * @param time
     * @param location
     * @param latitude
     * @param longitude
     * @param picturePath
     */
    @Override
    public void createEvent(String eventName, String description, String category, int maxPeople, String date, String time, String location, double latitude, double longitude, String picturePath) {
        view.showProgress();
        interactor.execute(eventName, description, category, maxPeople, date, time, location, latitude, longitude, picturePath);
    }
}
