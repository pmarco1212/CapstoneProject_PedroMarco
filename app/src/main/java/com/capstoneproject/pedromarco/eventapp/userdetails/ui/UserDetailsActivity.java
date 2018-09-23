package com.capstoneproject.pedromarco.eventapp.userdetails.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.capstoneproject.pedromarco.eventapp.EventApp;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.entities.User;
import com.capstoneproject.pedromarco.eventapp.lib.GlideImageLoader;
import com.capstoneproject.pedromarco.eventapp.userdetails.UserDetailsPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Activity handing all the UI of the user details
 */
public class UserDetailsActivity extends AppCompatActivity implements UserDetailsView {
    @Bind(R.id.ivUserImage)
    ImageView ivUserImage;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvAboutMeContent)
    TextView tvAboutMeContent;
    @Bind(R.id.tvNumberAssistedEvents)
    TextView tvNumberAssistedEvents;
    @Bind(R.id.tvNumberCreatedEvents)
    TextView tvNumberCreatedEvents;
    @Bind(R.id.rbRating)
    RatingBar rbRating;
    @Bind(R.id.layoutUser)
    RelativeLayout layoutUser;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    UserDetailsPresenter presenter;

    EventApp app;
    public static final String USERNAME_KEY = "username";
    String username;

    /**
     * Start the presenter, and set up the dagger injection and the necessary UI elements
     * Gets the username form the intent and ask the presenter to get the data from the model
     *
     * @param savedInstanceState state saved
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        setUpInjection();
        getUsernameFromIntent();
        setUpActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter.onCreate();
        presenter.getUserData(username);
    }

    /**
     * Sets up the action bar
     */
    private void setUpActionBar() {
        setSupportActionBar(toolbar);
        setTitle(username);
    }

    /**
     * Set up the dagger injection getting the Component
     */
    private void setUpInjection() {
        app = (EventApp) getApplication();
        app.getUserDetailsComponent(this).inject(this);
    }

    /**
     * Handle the action bar back arrow click
     *
     * @return true
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Destroy the presenter
     */
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Hide the layout and show the progress bar
     */
    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        layoutUser.setVisibility(View.GONE);
    }

    /**
     * Show the layout and hide the progress bar
     */
    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        layoutUser.setVisibility(View.VISIBLE);
    }

    /**
     * Show a given error in a toast
     *
     * @param error error to show
     */
    @Override
    public void userDetailsError(String error) {
        Toast.makeText(this, R.string.user_details_error + error, Toast.LENGTH_LONG).show();
    }

    /**
     * Sets up all the layout fileds with the information of the user
     *
     * @param user user obtained form the model
     */
    @Override
    public void setLayoutFields(User user) {
        tvAboutMeContent.setText(user.getDescription());
        tvName.setText(user.getFirstname() + " " + user.getSurname());
        tvNumberAssistedEvents.setText(user.getNumEventsAssisted().toString());
        tvNumberCreatedEvents.setText(user.getNumEventCreated().toString());
        rbRating.setRating(user.getAvgRating());
        GlideImageLoader.loadWithContext(ivUserImage, user.getProfilePhotoURL(), this);
    }

    /**
     * Gets the username form the calling intent
     */
    public void getUsernameFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra(USERNAME_KEY);
        }
    }
}
