package com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch;

/**
 * Interactor class executing the requests in the repository. Implements the interactor interface.
 */
public class SearchInteractorImpl implements SearchInteractor {
    SearchRepository repository;

    public SearchInteractorImpl(SearchRepository repository) {
        this.repository = repository;
    }

    /**
     * Tell the repository to search for events with the given params
     *
     * @param keywords keywords to search
     * @param distance max distance to the event
     * @param dateFrom start date
     * @param dateTo   end date
     * @param category category of the event
     */
    @Override
    public void getEvents(String keywords, int distance, String dateFrom, String dateTo, String category) {
        repository.searchAndGetEvents(keywords, distance, dateFrom, dateTo, category);
    }
}
