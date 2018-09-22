package com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.ui;

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
import android.widget.Toast;

import com.capstoneproject.pedromarco.eventapp.EventApp;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.eventdetails.ui.EventDetailsActivity;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.NearByPresenter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.EventListView;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.FragmentListRecyclerviewAdapter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.OnEventClickListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment class to handle all the UI of the Nearby tab
 */
public class NearByFragment extends Fragment implements EventListView, OnEventClickListener {

    @Bind(R.id.rvRecyclerview)
    RecyclerView rvRecyclerview;
    @Bind(R.id.layoutNoEvents)
    LinearLayout layoutNoEvents;

    EventApp app;

    @Inject
    NearByPresenter presenter;
    @Inject
    FragmentListRecyclerviewAdapter adapter;


    public NearByFragment() {
        // Required empty public constructor
    }

    /**
     * Start the presenter, and set up the dagger injection and the necessary UI elements
     *
     * @param savedInstanceState
     */
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
     * Inflate the view and set up the recyclerview
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
        app.getNearByFragmentComponent(this, this, this).inject(this);
    }

    /**
     * Set up the recyclerview
     */
    private void setupRecyclerView() {
        rvRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRecyclerview.setAdapter(adapter);
    }

    /**
     * Starts the presenter and get Nearby events
     */
    @Override
    public void onStart() {
        super.onStart();
        presenter.getNearByEvents();
    }

    /**
     * When an event is clicked, open the details activity with the event
     *
     * @param event event to open
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
     * Updates the event list of the adapter
     *
     * @param eventList list with the new events
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
        Toast.makeText(getContext(), getString(R.string.search_toast_error), Toast.LENGTH_LONG).show();
    }

    /**
     * Show no events dound and hide the event list
     */
    @Override
    public void showNoEventsFound() {
        rvRecyclerview.setVisibility(View.GONE);
        layoutNoEvents.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the event list
     */
    @Override
    public void showEventList() {
        layoutNoEvents.setVisibility(View.GONE);
        rvRecyclerview.setVisibility(View.VISIBLE);
    }

}
