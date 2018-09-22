package com.capstoneproject.pedromarco.eventapp.eventdetails.di;

import android.content.Context;

import com.capstoneproject.pedromarco.eventapp.domain.AlgoliaAPI;
import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.entities.Comment;
import com.capstoneproject.pedromarco.eventapp.eventdetails.EventDetailsInteractor;
import com.capstoneproject.pedromarco.eventapp.eventdetails.EventDetailsInteractorImpl;
import com.capstoneproject.pedromarco.eventapp.eventdetails.EventDetailsPresenter;
import com.capstoneproject.pedromarco.eventapp.eventdetails.EventDetailsPresenterImpl;
import com.capstoneproject.pedromarco.eventapp.eventdetails.EventDetailsRepository;
import com.capstoneproject.pedromarco.eventapp.eventdetails.EventDetailsRepositoryImpl;
import com.capstoneproject.pedromarco.eventapp.eventdetails.ui.CommentListRecyclerviewAdapter;
import com.capstoneproject.pedromarco.eventapp.eventdetails.ui.EventDetailsView;
import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module providing all the eventdetails package dependencies
 */
@Module
public class EventDetailsModule {
    EventDetailsView eventDetailsView;

    public EventDetailsModule(EventDetailsView eventDetailsView) {
        this.eventDetailsView = eventDetailsView;
    }

    @Singleton
    @Provides
    EventDetailsView providesSingleEventView() {
        return this.eventDetailsView;
    }

    @Singleton
    @Provides
    EventDetailsPresenter providesSingleEventPresenter(EventDetailsView view, EventDetailsInteractor interactor, EventBus eventBus) {
        return new EventDetailsPresenterImpl(eventBus, view, interactor);
    }

    @Singleton
    @Provides
    EventDetailsInteractor providesSingleEventInteractor(EventDetailsRepository repository) {
        return new EventDetailsInteractorImpl(repository);
    }

    @Singleton
    @Provides
    EventDetailsRepository providesSingleEventRepository(EventBus eventBus, FirebaseAPI firebaseAPI, AlgoliaAPI algoliaAPI) {
        return new EventDetailsRepositoryImpl(eventBus, firebaseAPI, algoliaAPI);
    }

    @Singleton
    @Provides
    CommentListRecyclerviewAdapter providesCommentListRecyclerviewAdapter(List<Comment> comments, ImageLoader loader, Context context) {
        return new CommentListRecyclerviewAdapter(comments, loader, context);
    }

    @Singleton
    @Provides
    List<Comment> providesComments() {
        return new ArrayList<Comment>();
    }
}
