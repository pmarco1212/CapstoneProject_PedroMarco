package com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby;

/**
 * Interactor class executing the requests in the repository. Implements the interactor interface.
 */
public class NearByInteractorImpl implements NearByInteractor {
    NearByRepository repository;

    public NearByInteractorImpl(NearByRepository repository) {
        this.repository = repository;
    }

    /**
     * Tells the repository to get the nearby events
     */
    @Override
    public void getNearByEvents() {
        repository.getNearbyEvents();
    }
}
