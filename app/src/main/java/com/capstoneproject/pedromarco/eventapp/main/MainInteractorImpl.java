package com.capstoneproject.pedromarco.eventapp.main;

/**
 * Interactor class executing the requests in the repository. Implements the interactor interface.
 */
public class MainInteractorImpl implements MainInteractor {
    MainRepository mainRepository;

    public MainInteractorImpl(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    /**
     * Tell the repository to logout
     */
    @Override
    public void logout() {
        mainRepository.logout();
    }
}
