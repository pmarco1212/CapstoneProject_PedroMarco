package com.capstoneproject.pedromarco.eventapp.userdetails;

import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.userdetails.events.UserDetailsEvent;
import com.capstoneproject.pedromarco.eventapp.userdetails.ui.UserDetailsView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Presenter class od the userrdetails implementing the presenter interface
 */
public class UserDetailsPresenterImpl implements UserDetailsPresenter {
    UserDetailsView view;
    UserDetailsInteractor interactor;
    EventBus eventBus;

    public UserDetailsPresenterImpl(UserDetailsView view, UserDetailsInteractor interactor, EventBus eventBus) {
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
     * unregister eventbus
     */
    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    /**
     * Tell the interactor to get the user details
     *
     * @param username username
     */
    @Override
    public void getUserData(String username) {
        interactor.executeGetUserData(username);
    }

    /**
     * Receive the events sent from the repository
     *
     * @param event event sent form the repository
     */
    @Subscribe
    @Override
    public void onEventMainThread(UserDetailsEvent event) {
        if (event != null) {
            switch (event.getEventType()) {
                case UserDetailsEvent.onUserReadError:
                    String errormessage = event.getErrorMesage();
                    if (errormessage != null) {
                        view.userDetailsError(event.getErrorMesage());
                    } else {
                        view.userDetailsError("");
                    }
                    break;
                case UserDetailsEvent.onUserReadStart:
                    view.showProgress();
                    break;
                case UserDetailsEvent.onUserReadSucess:
                    view.hideProgress();
                    view.setLayoutFields(event.getUser());
                    break;
            }
        }
    }
}
