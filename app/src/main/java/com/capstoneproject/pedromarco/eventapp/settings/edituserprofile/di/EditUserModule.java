package com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.di;

import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageStorage;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.EditUserInteractor;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.EditUserIteractorImpl;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.EditUserPresenter;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.EditUserPresenterImpl;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.EditUserRepositoryImpl;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.EditUserRespository;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.ui.EditUserView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module providing all the edit user package dependencies
 */
@Module
public class EditUserModule {
    EditUserView view;

    public EditUserModule(EditUserView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    EditUserView providesEditUserView() {
        return this.view;
    }

    @Provides
    @Singleton
    EditUserPresenter providesEditUserPresenter(EditUserView editUserView, EditUserInteractor editUserInteractor, EventBus eventBus) {
        return new EditUserPresenterImpl(editUserView, editUserInteractor, eventBus);
    }

    @Provides
    @Singleton
    EditUserInteractor providesEditUserInteractor(EditUserRespository editUserRespository) {
        return new EditUserIteractorImpl(editUserRespository);
    }

    @Provides
    @Singleton
    EditUserRespository providesEditUserRepository(FirebaseAPI firebaseAPI, EventBus eventBus, ImageStorage imageStorage) {
        return new EditUserRepositoryImpl(firebaseAPI, eventBus, imageStorage);
    }


}
