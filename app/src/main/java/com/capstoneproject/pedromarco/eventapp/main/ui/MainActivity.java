package com.capstoneproject.pedromarco.eventapp.main.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstoneproject.pedromarco.eventapp.EventApp;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.createevent.ui.CreateEventActivity;
import com.capstoneproject.pedromarco.eventapp.entities.CurrentLocation;
import com.capstoneproject.pedromarco.eventapp.login.ui.LoginActivity;
import com.capstoneproject.pedromarco.eventapp.main.MainPresenter;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.ui.FavouritesFragment;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.ui.NearByFragment;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.ui.SearchFragment;
import com.capstoneproject.pedromarco.eventapp.main.ui.adapters.ViewPagerAdapter;
import com.capstoneproject.pedromarco.eventapp.settings.SettingsActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Activity handing all the UI of the main screen
 */
public class MainActivity extends AppCompatActivity implements MainView {
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Inject
    MainPresenter mainPresenter;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ViewPagerAdapter adapter;

    FusedLocationProviderClient client;
    LocationCallback mLocationCallback;
    EventApp app;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 98;

    /**
     * Set up the dagger injection and the necessary UI elements
     *
     * @param savedInstanceState saved state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupInjection();
        // Set the adapter onto the view pager
        viewpager.setAdapter(adapter);
        // Give the TabLayout the ViewPager
        tablayout.setupWithViewPager(viewpager);
    }

    /**
     * Check if the location permission is provided every time the activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        //get the current location every tome the activity starts
        checkLocationPermission();
    }

    /**
     * Set up the dagger injection, defining the fragments and getting the Component
     */
    private void setupInjection() {
        String[] titles = new String[]{getString(R.string.main_tab_name_nearby),
                getString(R.string.main_tab_name_search), getString(R.string.main_tab_name_favourites)};

        Fragment[] fragments = new Fragment[]{new NearByFragment(),
                new SearchFragment(), new FavouritesFragment()};

        app = (EventApp) getApplication();
        app.getMainComponent(this, getSupportFragmentManager(), fragments, titles).inject(this);
    }

    /**
     * Inflates the options menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles the click in the options of the menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
        } else if (id == R.id.action_create_new_event) {
            goToCreateEvent();
        } else if (id == R.id.action_settings) {
            goToSettings();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Logout and go to the Login Activity
     */
    private void logout() {
        mainPresenter.logout();
        //Go to login activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Go to CreateEvent activity
     */
    private void goToCreateEvent() {
        startActivity(new Intent(this, CreateEventActivity.class));
    }

    /**
     * Go to the settings activity
     */
    private void goToSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    /**
     * If the back arrow is pressed, go back
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Handles the results of the permission request
     * If sucessfull, cehck the location settings, otherwise check the permission again
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, call the checklocation again
                    checkLocationSettings();
                } else {
                    Toast.makeText(this, getString(R.string.event_details_permission_denied), Toast.LENGTH_SHORT).show();
                    checkLocationPermission();
                }
                return;
            }
        }
    }

    /**
     * Check if the permissions of the location are accepted
     */
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        checkLocationSettings();
    }

    /**
     * Checks the if location settings are the necessary ones
     * If they are not, ask the user to change them
     */
    private void checkLocationSettings() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); // ten seconds interval
        mLocationRequest.setFastestInterval(2000); //2 seconds fast interval
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //High accuracy
        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        // Check whether location settings are satisfied
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequest);
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d("Locationnn", "Location settings satisfied");
                getActualLocation();
            }
        });
        Activity activity = this;
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        Log.w("Locationnn", "Location settings not satisfied, attempting resolution intent");
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(activity, REQUEST_CODE_LOCATION_SETTINGS);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            Log.e("Locationnn", "Unable to start resolution intent");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.w("Locationnn", "Location settings not satisfied and can't be changed");
                        break;
                }
            }
        });
    }

    /**
     * On pause, stops requesting location updates
     */
    @Override
    protected void onPause() { //Stop the location client
        if (client != null && mLocationCallback != null) {
            Log.w("Locationnn", "Stopping location client");
            client.removeLocationUpdates(mLocationCallback);
            client = null;
            mLocationCallback = null;
        }
        super.onPause();
    }

    /**
     * Cehck the activity result of the settings change request
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_SETTINGS:
                waitAndCheckSettingsAgain();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * Wait for 5 seconds (to avoid displying the dialogue to change events twice)
     * and check the location settings again to see if the user has accepted the change
     */
    private void waitAndCheckSettingsAgain() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLocationSettings(); //Wait for the location to be activated before checkign again (5 seconds)
            }
        }, 5000);
    }


    /**
     * Gets the actuall location of the user, subscribing for updates
     */
    private void getActualLocation() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //Try to get a location in less than 5 seconds
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locationList = locationResult.getLocations();
                Log.d("AAAAA", "Location obtained");
                if (locationList.size() > 0) {
                    Log.d("AAAAA", "Location GOOOOOD");
                    //The last location in the list is the newest
                    Location location = locationList.get(locationList.size() - 1);
                    if (location != null) {
                        Log.d("AAAAA", "Location " + location.getLongitude() + "," + location.getLatitude());
                        CurrentLocation.setLatitude(location.getLatitude());
                        CurrentLocation.setLongitude(location.getLongitude());
                        //If location obtained, set the new request/callback to receive update only every 3 mins
                        setNewLocationCallback();
                    }
                }
            }
        };
        client.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    /**
     * Change the callback to only get updates every 3 mins (periodic)
     */
    private void setNewLocationCallback() {
        //Cancel the previous request
        client.removeLocationUpdates(mLocationCallback);
        mLocationCallback = null;
        client = null;
        //Set the new request
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(180000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locationList = locationResult.getLocations();
                if (locationList.size() > 0) {
                    //The last location in the list is the newest
                    Location location = locationList.get(locationList.size() - 1);
                    if (location != null) {
                        CurrentLocation.setLatitude(location.getLatitude());
                        CurrentLocation.setLongitude(location.getLongitude());
                    }
                }
            }
        };
        client.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
}
