package com.capstoneproject.pedromarco.eventapp.settings.edituserprofile;

import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.events.EditUserEvent;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.ui.EditUserView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Presenter class implementing the presenter interface
 */
public class EditUserPresenterImpl implements EditUserPresenter {

    EditUserView view;
    EditUserInteractor interactor;
    EventBus eventBus;

    public EditUserPresenterImpl(EditUserView view, EditUserInteractor interactor, EventBus eventBus) {
        this.view = view;
        this.interactor = interactor;
        this.eventBus = eventBus;
    }

    /**
     * Register eventbus and gets the user details
     */
    @Override
    public void onCreate() {
        eventBus.register(this);
        interactor.getUserDetails();
    }

    /**
     * Unregister Eventbus
     */
    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    /**
     * Listen form event form the repository and execute the appropiate view method
     *
     * @param event event form the REpository
     */
    @Override
    @Subscribe
    public void onEventMainThread(EditUserEvent event) {
        int type = event.getEventType();
        switch (type) {
            case EditUserEvent.onUserDetailsObtainedSucess:
                view.fillUserDetails(event.getUser());
                break;
            case EditUserEvent.onUserUpdatedSucess: //User created correctly
                view.editUserSuccess();
                break;
            case EditUserEvent.onUserUpdatedError: //An error related to the database or cloudinary has ocurred, send message
                view.editUserError(event.getErrorMesage());
                break;
            case EditUserEvent.onEmptyFieldError: //Some of the fileds are empty
                view.emptyFieldError();
                break;
            case EditUserEvent.onImageUploadError:
                view.onImageUploadError();
                break;
        }

    }

    @Override
    public void editUser(String firstname, String surname, String description, String profilepictureURL) {
        interactor.execute(firstname, surname, description, profilepictureURL);
    }
}
