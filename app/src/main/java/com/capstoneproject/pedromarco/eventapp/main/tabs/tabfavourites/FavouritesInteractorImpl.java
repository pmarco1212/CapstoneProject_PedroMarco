package com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites;

/**
 * Interactor class executing the requests in the repository. Implements the interactor interface.
 */
public class FavouritesInteractorImpl implements FavouritesInteractor {
    FavouritesRepository repository;

    public FavouritesInteractorImpl(FavouritesRepository repository) {
        this.repository = repository;
    }

    /**
     * Tells the repository to get the favourite events
     */
    @Override
    public void getFavouriteEvents() {
        repository.getFavouriteEvents();
    }

}
