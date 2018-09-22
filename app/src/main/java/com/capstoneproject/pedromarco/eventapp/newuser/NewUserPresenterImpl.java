package com.capstoneproject.pedromarco.eventapp.newuser;

import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.newuser.events.NewUserEvent;
import com.capstoneproject.pedromarco.eventapp.newuser.ui.NewUserView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Presenter class of the newuser package mplementing the presenter interface
 */
public class NewUserPresenterImpl implements NewUserPresenter {

    NewUserView view;
    NewUserInteractor interactor;
    EventBus eventBus;

    public NewUserPresenterImpl(NewUserView view, NewUserInteractor interactor, EventBus eventBus) {
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
     * unregisters eventbus
     */
    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    /**
     * Catch the Neuserevents and execute the corresponding view methods
     *
     * @param event event obtained
     */
    @Override
    @Subscribe
    public void onEventMainThread(NewUserEvent event) {
        int type = event.getEventType();
        switch (type) {
            case NewUserEvent.onUserCreatedSucess: //User created correctly
                view.newUserSuccess();
                break;
            case NewUserEvent.onUserCreatedError: //An error related to the database or cloudinary has ocurred, send message
                view.hideProgress();
                view.newUserError(event.getErrorMesage());
                break;
            case NewUserEvent.onEmptyFieldError: //Some of the fileds are empty
                view.hideProgress();
                view.emptyFieldError();
                break;
            case NewUserEvent.onUsernameAlreadyInUse: //The choosen username is already in use
                view.hideProgress();
                view.usernameAlreadyInUseError();
                break;
            case NewUserEvent.onImageUploadError:
                view.hideProgress();
                view.onImageUploadError();
                break;
        }

    }

    @Override
    public void addNewUser(String username, String firstname, String surname, String description, String profilepictureURL) {
        view.showProgress();
        interactor.execute(username, firstname, surname, description, profilepictureURL);
    }
}
