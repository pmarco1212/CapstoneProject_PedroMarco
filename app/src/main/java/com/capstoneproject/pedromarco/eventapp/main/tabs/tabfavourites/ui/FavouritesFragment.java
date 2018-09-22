package com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.capstoneproject.pedromarco.eventapp.EventApp;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.eventdetails.ui.EventDetailsActivity;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.FavouritesPresenter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.EventListView;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.FragmentListRecyclerviewAdapter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.OnEventClickListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Fragment handing all the UI of the favourites tab, implementing EventListView
 */
public class FavouritesFragment extends Fragment implements EventListView, OnEventClickListener {
    @Bind(R.id.rvRecyclerview)
    RecyclerView rvRecyclerview;
    @Bind(R.id.tvNoResults)
    TextView tvNoResults;
    @Bind(R.id.layoutNoEvents)
    LinearLayout layoutNoEvents;

    @Inject
    FavouritesPresenter presenter;
    @Inject
    FragmentListRecyclerviewAdapter adapter;

    EventApp app;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpInjection();
        presenter.onCreate();
    }

    /**
     * Destroys the presenter
     */
    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Bind teh view and sets up the recyclerview
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.events_list_layout, container, false);
        ButterKnife.bind(this, rootView);
        setupRecyclerView();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * Set up the dagger injection getting the Component
     */
    private void setUpInjection() {
        app = (EventApp) getActivity().getApplication();
        app.getFavouritesFragmentComponent(this, this, this).inject(this);
    }

    /**
     * Sets up the recyclerview
     */
    private void setupRecyclerView() {
        rvRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRecyclerview.setAdapter(adapter);
    }

    /**
     * On start, gets the favourite events
     */
    @Override
    public void onStart() {
        super.onStart();
        presenter.getFavouriteEvents();
    }

    /**
     * On event click, open the EVentDetails activity sending that event in the intent
     *
     * @param event
     */
    @Override
    public void onEventClick(Event event) {
        if (event != null) {
            Intent intent = new Intent(getContext(), EventDetailsActivity.class);
            intent.putExtra(EventDetailsActivity.EVENT_KEY, event);
            startActivity(intent);
        }
    }

    /**
     * Update the event list of the adapter with the new events
     *
     * @param eventList
     */
    @Override
    public void updateEventList(List<Event> eventList) {
        adapter.addEvents(eventList);
    }

    /**
     * Show a search error
     */
    @Override
    public void showSearchError() {
        Toast.makeText(getActivity().getBaseContext(), getString(R.string.search_toast_error), Toast.LENGTH_LONG).show();
    }

    /**
     * Show the Textview with no event found and hide the event list
     */
    @Override
    public void showNoEventsFound() {
        tvNoResults.setText(R.string.favourites_no_events);
        rvRecyclerview.setVisibility(View.GONE);
        layoutNoEvents.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the Textview with no event found and shows the event list
     */
    @Override
    public void showEventList() {
        layoutNoEvents.setVisibility(View.GONE);
        rvRecyclerview.setVisibility(View.VISIBLE);
    }
}