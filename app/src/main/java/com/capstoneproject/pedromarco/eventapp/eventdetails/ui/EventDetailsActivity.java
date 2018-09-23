package com.capstoneproject.pedromarco.eventapp.eventdetails.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.capstoneproject.pedromarco.eventapp.EventApp;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.entities.Comment;
import com.capstoneproject.pedromarco.eventapp.entities.CurrentLocation;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.capstoneproject.pedromarco.eventapp.eventdetails.EventDetailsPresenter;
import com.capstoneproject.pedromarco.eventapp.lib.GlideImageLoader;
import com.capstoneproject.pedromarco.eventapp.notifications.NotificationsScheduler;
import com.capstoneproject.pedromarco.eventapp.userdetails.ui.UserDetailsActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity handing all the UI of the event details
 */
public class EventDetailsActivity extends AppCompatActivity implements EventDetailsView, RateEventDialogFragment.RateEventDialogListener {
    @Bind(R.id.ivEventBackdrop)
    ImageView ivEventBackdrop;
    @Bind(R.id.tvDateTime)
    TextView tvDateTime;
    @Bind(R.id.tvLocation)
    TextView tvLocation;
    @Bind(R.id.tvDistance)
    TextView tvDistance;
    @Bind(R.id.tvNumberPeople)
    TextView tvNumberPeople;
    @Bind(R.id.btnAssist)
    Button btnAssist;
    @Bind(R.id.btnImHere)
    Button btnImHere;
    @Bind(R.id.tvDescriptionContent)
    TextView tvDescriptionContent;
    @Bind(R.id.tvNoComments)
    TextView tvNoComments;
    @Bind(R.id.etNewComments)
    EditText etNewComments;
    @Bind(R.id.floatingButtonFav)
    FloatingActionButton floatingButtonFav;
    @Bind(R.id.tvCreator)
    TextView tvCreator;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.app_bar)
    AppBarLayout toolbarLayout;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.layoutCreator)
    LinearLayout layoutCreator;
    @Bind(R.id.btnSendComment)
    Button btnSendComment;
    @Bind(R.id.location_llayout)
    LinearLayout locationLlayout;
    @Bind(R.id.rvComments)
    RecyclerView rvComments;
    @Bind(R.id.scrollView)
    NestedScrollView scrollView;

    @Inject
    EventDetailsPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    CommentListRecyclerviewAdapter adapter;

    EventApp app;
    Event event;

    public static final String EVENT_KEY = "selectedEvent";
    InterstitialAd mInterstitialAd;
    /**
     * Start the presenter, and set up the dagger injection and the necessary UI elements
     * Cehck if the evnet has currently started, and get all details fot he event
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);
        event = (Event) getIntent().getSerializableExtra(EVENT_KEY);
        setUpInjection();
        setupCommentsRecyclerView();
        checkIfEventHasStarted();
        presenter.onCreate();
        presenter.getEventDetails(event.getId());
        //Load the add to be sown when the first click event happened
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ads_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    /**
     * sets up the recyclerview for the comments
     */
    private void setupCommentsRecyclerView() {
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setAdapter(adapter);
    }

    /**
     * Set up the dagger injection getting the Component
     */
    private void setUpInjection() {
        app = (EventApp) getApplication();
        app.getSingleEventComponent(this).inject(this);
    }

    /**
     * Cehck if an event has already started, if so, execute the appropiate method
     */
    private void checkIfEventHasStarted() {
        SimpleDateFormat formmatter = new SimpleDateFormat(getString(R.string.date_format));
        String today = formmatter.format(new Date());
        if (today.equals(event.getDate())) {
            setEventStarted();
        }
    }


    /**
     * Fill all layout with the event details
     *
     * @param event the event to use
     */
    @Override
    public void fillLayout(Event event) {
        //Update the event with the event received from firebase
        this.event = event;
        //Set the event title in the action bar
        toolbar.setTitle(event.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Set the rest of fields
        tvDateTime.setText(event.getDate() + " - " + event.getTime());
        tvDescriptionContent.setText(event.getDescription());
        tvLocation.setText(event.getLocation());
        tvCreator.setText(event.getCreator());
        int maxpeople = event.getMaxPeople();
        tvNumberPeople.setText(event.getCurrentPeople() + ((maxpeople != 0) ? ("/" + maxpeople) : "").toString());
        GlideImageLoader.loadWithContext(ivEventBackdrop, event.getPhotoURL(), this);
        setComments();
        toolbarLayout.setExpanded(true);
        scrollView.fullScroll(View.FOCUS_UP);
    }

    /**
     * Show the comments recyclrerview and udate the comment list
     *
     * @param comments
     */
    @Override
    public void updateComments(List<Comment> comments) {
        showComments();
        adapter.setComments(comments);
    }

    /**
     * Handles the back arrow of the actionbar
     *
     * @return true
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * If there are comments, set them in the recyclerview
     */
    private void setComments() {
        if (event.getComments() != null) {
            ArrayList<Comment> comments = new ArrayList<Comment>(event.getComments().values());
            Collections.sort(comments);
            if (!comments.isEmpty() && comments != null) {
                showComments();
                adapter.setComments(comments);
            }
        }
    }

    /**
     * Show the comments recyclerview and hide the no comments textview
     */
    private void showComments() {
        tvNoComments.setVisibility(View.GONE);
        rvComments.setVisibility(View.VISIBLE);
    }

    /**
     * Execute the appropiate method deppending on the view clicked
     *
     * @param view view clicked
     */
    @OnClick({R.id.btnAssist, R.id.btnImHere, R.id.btnSendComment, R.id.floatingButtonFav, R.id.layoutCreator, R.id.location_llayout})
    public void onViewClicked(View view) {
        //Displays the add if its loaded after the first click of the activity.
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        switch (view.getId()) {
            case R.id.btnAssist:
                presenter.toogleAssiting(event.getId());
                break;
            case R.id.btnImHere:
                getCurrentLocation();
                break;
            case R.id.btnSendComment:
                presenter.writeComment(event.getId(), etNewComments.getText().toString());
                break;
            case R.id.floatingButtonFav:
                presenter.toogleFavourite(event.getId());
                break;
            case R.id.layoutCreator:
                goToUserDetailsActivity();
                break;
            case R.id.location_llayout:
                openLocation();
                break;
        }
    }

    /**
     * gets the current location from the currentlocaiton class
     */
    private void getCurrentLocation() {
        double currentLatitude = CurrentLocation.getLatitude();
        double currentLongitude = CurrentLocation.getLongitude();
        if (currentLatitude != 0.0 || currentLongitude != 0.0) {
            presenter.setIAmHere(event.getLongitude(), event.getLatitude(), currentLongitude, currentLatitude);
        } else {
            needToActivateLocation();
        }
    }

    /**
     * If there is no locaiton, show the toast to activate location
     */
    private void needToActivateLocation() {
        Toast.makeText(this, R.string.event_details_need_location, Toast.LENGTH_LONG).show();
    }

    /**
     * Open the location of the event in google maps
     */
    private void openLocation() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/?q=" + event.getLatitude() + "," + event.getLongitude()));
        startActivity(browserIntent);
    }

    /**
     * Set the floating button as its marked as favourited
     */
    @Override
    public void setIsFavourite() {
        floatingButtonFav.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(this, R.color.pink))));
    }

    /**
     * Sets the assisting button as the user is assisting
     */
    @Override
    public void setIsAssisting() {
        btnAssist.setBackground(getResources().getDrawable(R.drawable.round_going_button));
        btnAssist.setText(R.string.event_details_btn_assisting);
    }

    /**
     * Show the Im here button as the event has started
     */
    @Override
    public void setEventStarted() {
        btnAssist.setVisibility(View.GONE);
        btnImHere.setVisibility(View.VISIBLE);
    }

    /**
     * Show the dialogue fragment for rating
     */
    @Override
    public void iAmHereCheckedAndAllowRating() {
        RateEventDialogFragment dialogFragment = new RateEventDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "rateEvent");
    }

    /**
     * Toogle the favourite button deppending on the boolean given
     *
     * @param isFavourite boolean value to set
     */
    @Override
    public void toogleFavourite(Boolean isFavourite) {
        if (isFavourite == true) {
            floatingButtonFav.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(this, R.color.pink))));
            Toast.makeText(this, R.string.event_details_favourites_added, Toast.LENGTH_SHORT).show();
        } else {
            floatingButtonFav.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(this, R.color.grey))));
            Toast.makeText(this, R.string.event_details_favourites_removed, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show the error of marking favourite
     */
    @Override
    public void setFavouriteError() {
        Toast.makeText(this, R.string.event_details_favourites_error, Toast.LENGTH_SHORT).show();
    }

    /**
     * Change the ssisting button deppending if the user is assisting or not
     *
     * @param isAssisting   boolean indication assitance
     * @param event_updated event updated with the new assisting info
     */
    @Override
    public void toogleAssisting(Boolean isAssisting, Event event_updated) {
        if (isAssisting) {
            btnAssist.setBackground(getResources().getDrawable(R.drawable.round_going_button));
            btnAssist.setText(R.string.event_details_btn_assisting);
            if (getNotificationState()) { //if notification are actives and meet the minimun SDK
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //Set the notification for the event
                    NotificationsScheduler.scheduleNotifications(this, event_updated);
                }
            }
        } else {
            btnAssist.setBackground(getResources().getDrawable(R.drawable.round_assist_button));
            btnAssist.setText(R.string.event_details_btn_assist);
        }
        event = event_updated;
        fillLayout(event);
    }

    /**
     * Gets the notification state from the preferences
     *
     * @return
     */
    private boolean getNotificationState() {
        return sharedPreferences.getBoolean(app.getNotificationsKey(), false);
    }

    /**
     * Show the error of marking assisting
     */
    @Override
    public void setAssistingError() {
        Toast.makeText(this, R.string.event_details_assisting_error, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show the rror of the event full
     */
    @Override
    public void setEventFullError() {
        Toast.makeText(this, R.string.event_details_error_event_full, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show the error of trying to rate when not in the location
     */
    @Override
    public void setImHereError() {
        Toast.makeText(this, R.string.event_details_location_invalid, Toast.LENGTH_SHORT).show();
    }

    /**
     * Go to the User Details activity
     */
    private void goToUserDetailsActivity() {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra(UserDetailsActivity.USERNAME_KEY, event.getCreator());
        startActivity(intent);
    }

    /**
     * destroys the presenter
     */
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Send the rate to the presenter
     *
     * @param rating value of the rate
     */
    @Override
    public void onRateEventPositiveClick(float rating) {
        //send the rating
        presenter.rateEvent(rating, event.getCreator(), event.getId());
    }

    /**
     * Do nothing
     *
     * @param dialog
     */
    @Override
    public void onRateEventNegativeClick(DialogFragment dialog) {
        //do nothing
    }

    /**
     * Set the rated! button and disable it
     */
    @Override
    public void setAlreadyRated() {
        btnImHere.setText(getString(R.string.event_details_btn_rated));
        btnImHere.setEnabled(false);
    }

    /**
     * Show toas with rating success
     */
    @Override
    public void ratingSucess() {
        Toast.makeText(this, getString(R.string.event_details_rated_success), Toast.LENGTH_SHORT).show();
    }

    /**
     * Show toast with rating error
     */
    @Override
    public void ratingError() {
        Toast.makeText(this, getString(R.string.event_details_rating_error), Toast.LENGTH_SHORT).show();
    }

    /**
     * Clear the comment edittext and close the keyboard
     */
    @Override
    public void clearEditTextAndCloseKeyboard() {
        etNewComments.getText().clear();
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etNewComments.getWindowToken(), 0);
        etNewComments.clearFocus();
    }
}
