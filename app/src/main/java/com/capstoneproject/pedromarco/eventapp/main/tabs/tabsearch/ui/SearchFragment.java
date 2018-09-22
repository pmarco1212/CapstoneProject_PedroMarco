package com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.capstoneproject.pedromarco.eventapp.EventApp;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.eventdetails.ui.EventDetailsActivity;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.SearchPresenter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.FragmentListRecyclerviewAdapter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.OnEventClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment handing all the UI of the search tab
 */
public class SearchFragment extends Fragment implements SearchView, OnEventClickListener, SeekBar.OnSeekBarChangeListener {
    @Bind(R.id.btnCloseSearch)
    Button btnCloseSearch;
    @Bind(R.id.searchResultsBar)
    LinearLayout searchResultsBar;
    @Bind(R.id.btnSearch)
    Button btnSearch;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.etFromDate)
    EditText etFromDate;
    @Bind(R.id.etToDate)
    EditText etToDate;
    @Bind(R.id.searchscreen)
    RelativeLayout searchscreen;
    @Bind(R.id.rvEventList)
    RecyclerView rvEventList;
    @Bind(R.id.tvDistance)
    TextView tvDistance;
    @Bind(R.id.etSearch)
    EditText etSearch;
    @Bind(R.id.spinnerCategory)
    Spinner spinnerCategory;
    @Bind(R.id.layoutNoEvents)
    LinearLayout layoutNoEvents;

    @Inject
    SearchPresenter presenter;
    @Inject
    FragmentListRecyclerviewAdapter adapter;

    EventApp app;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Bind the view, and sets up the rnecessary UI elements
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_layout, container, false);
        ButterKnife.bind(this, rootView);
        setupRecyclerView();
        setUpDatePicker();
        seekBar.setOnSeekBarChangeListener(this);
        return rootView;
    }

    /**
     * Create the present and sets up the injection
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
     * Set up the dagger injection getting the Component
     */
    private void setUpInjection() {
        app = (EventApp) getActivity().getApplication();
        app.getSearchFragmentComponent(this, this, this).inject(this);
    }

    /**
     * Sets up the recyclerview
     */
    private void setupRecyclerView() {
        rvEventList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvEventList.setAdapter(adapter);
    }

    /**
     * Hide the search layout
     */
    @Override
    public void hideSearchLayout() {
        searchscreen.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        etSearch.clearFocus();
    }

    /**
     * Shows the search layout
     */
    @Override
    public void showSearchLayout() {
        searchscreen.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the search results list
     */
    @Override
    public void hideEventList() {
        searchResultsBar.setVisibility(View.GONE);
        rvEventList.setVisibility(View.GONE);
        layoutNoEvents.setVisibility(View.GONE);
    }

    /**
     * Shows the search results list
     */
    @Override
    public void showEventList() {
        searchResultsBar.setVisibility(View.VISIBLE);
        rvEventList.setVisibility(View.VISIBLE);
    }

    /**
     * Show the no event found layout
     */
    @Override
    public void showNoEventsFound() {
        rvEventList.setVisibility(View.GONE);
        searchResultsBar.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        etSearch.clearFocus();
        layoutNoEvents.setVisibility(View.VISIBLE);
    }

    /**
     * Displays a search error
     */
    @Override
    public void showSearchError() {
        Toast.makeText(getActivity().getBaseContext(), getString(R.string.search_toast_error), Toast.LENGTH_LONG).show();
    }

    /**
     * Update the event list of the adapter
     *
     * @param eventList list with the new events
     */
    @Override
    public void updateEventList(List<Event> eventList) {
        adapter.addEvents(eventList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * Handles the clicks events on the layout
     *
     * @param view
     */
    @OnClick({R.id.btnCloseSearch, R.id.btnSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCloseSearch:
                presenter.closeSearch();
                break;
            case R.id.btnSearch:
                String category = null;
                if (spinnerCategory.getSelectedItemPosition() != 0) { //if the position is 0, is "Any", therefore doesnt seen the value
                    category = spinnerCategory.getSelectedItem().toString();
                }
                presenter.searchEvents(etSearch.getText().toString(), getNextMultipleOfTen(seekBar.getProgress()), etFromDate.getText().toString(), etToDate.getText().toString(), category);
                break;
        }
    }

    /**
     * If an event of the list is clicked, open the eventdetails activity with the event
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
     * Handles the seekbar changes
     *
     * @param seekBar
     * @param i
     * @param b
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar.getId() == R.id.seekBar) {
            int max = seekBar.getMax();
            if (i == 0) { //If its the min, sx the textview to minimun distance
                tvDistance.setText(R.string.search_minimum_distance);
            } else if (i == max) {//If its max value, set the textview to "any"
                tvDistance.setText(R.string.search_tv_any_distance);
            } else {
                int displayValue = getNextMultipleOfTen(i);
                tvDistance.setText("< " + displayValue + "km");
            }
        }
    }

    /**
     * Get the next multiple of 10 of a given value
     *
     * @param value
     * @return
     */
    public int getNextMultipleOfTen(int value) {
        return (int) (Math.ceil((double) value / 10)) * 10;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Sets up the datepickers
     */
    private void setUpDatePicker() {
        final Calendar myCalendar;
        final DatePickerDialog.OnDateSetListener date;
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format));
                etFromDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        final DatePickerDialog.OnDateSetListener date2;
        date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format));
                etToDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });
    }


}
