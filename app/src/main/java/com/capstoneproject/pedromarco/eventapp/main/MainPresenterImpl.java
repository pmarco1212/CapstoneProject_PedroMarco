package com.capstoneproject.pedromarco.eventapp.main;

import com.capstoneproject.pedromarco.eventapp.main.ui.MainView;

/**
 * Presenter class implementing the presenter interface
 */
public class MainPresenterImpl implements MainPresenter {
    MainView mainView;
    MainInteractor mainInteractor;


    public MainPresenterImpl(MainView mainView, MainInteractor mainInteractor) {
        this.mainView = mainView;
        this.mainInteractor = mainInteractor;
    }

    /**
     * Tells the interactor to logout
     */
    @Override
    public void logout() {
        mainInteractor.logout();
    }
}
