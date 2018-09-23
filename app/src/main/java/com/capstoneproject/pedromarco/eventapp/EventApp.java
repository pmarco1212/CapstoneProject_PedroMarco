package com.capstoneproject.pedromarco.eventapp;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.capstoneproject.pedromarco.eventapp.createevent.di.CreateEventComponent;
import com.capstoneproject.pedromarco.eventapp.createevent.di.CreateEventModule;
import com.capstoneproject.pedromarco.eventapp.createevent.di.DaggerCreateEventComponent;
import com.capstoneproject.pedromarco.eventapp.createevent.ui.CreateEventView;
import com.capstoneproject.pedromarco.eventapp.domain.di.DomainModule;
import com.capstoneproject.pedromarco.eventapp.eventdetails.di.DaggerEventDetailsComponent;
import com.capstoneproject.pedromarco.eventapp.eventdetails.di.EventDetailsComponent;
import com.capstoneproject.pedromarco.eventapp.eventdetails.di.EventDetailsModule;
import com.capstoneproject.pedromarco.eventapp.eventdetails.ui.EventDetailsView;
import com.capstoneproject.pedromarco.eventapp.lib.di.LibsModule;
import com.capstoneproject.pedromarco.eventapp.login.di.DaggerLoginComponent;
import com.capstoneproject.pedromarco.eventapp.login.di.LoginComponent;
import com.capstoneproject.pedromarco.eventapp.login.di.LoginModule;
import com.capstoneproject.pedromarco.eventapp.login.ui.LoginView;
import com.capstoneproject.pedromarco.eventapp.main.di.DaggerMainComponent;
import com.capstoneproject.pedromarco.eventapp.main.di.MainComponent;
import com.capstoneproject.pedromarco.eventapp.main.di.MainModule;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.di.DaggerFavouritesFragmentComponent;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.di.FavouritesFragmentComponent;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabfavourites.di.FavouritesFragmentModule;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.di.DaggerNearByFragmentComponent;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.di.NearByFragmentComponent;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabnearby.di.NearByFragmentModule;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.di.DaggerSearchFragmentComponent;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.di.SearchFragmentComponent;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.di.SearchFragmentModule;
import com.capstoneproject.pedromarco.eventapp.main.tabs.tabsearch.ui.SearchView;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.EventListView;
import com.capstoneproject.pedromarco.eventapp.main.tabs.uicommon.OnEventClickListener;
import com.capstoneproject.pedromarco.eventapp.main.ui.MainView;
import com.capstoneproject.pedromarco.eventapp.newuser.di.DaggerNewUserComponent;
import com.capstoneproject.pedromarco.eventapp.newuser.di.NewUserComponent;
import com.capstoneproject.pedromarco.eventapp.newuser.di.NewUserModule;
import com.capstoneproject.pedromarco.eventapp.newuser.ui.NewUserView;
import com.capstoneproject.pedromarco.eventapp.settings.SettingsActivity;
import com.capstoneproject.pedromarco.eventapp.settings.di.DaggerSettingsComponent;
import com.capstoneproject.pedromarco.eventapp.settings.di.SettingsComponent;
import com.capstoneproject.pedromarco.eventapp.settings.di.SettingsModule;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.di.DaggerEditUserComponent;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.di.EditUserComponent;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.di.EditUserModule;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.ui.EditUserView;
import com.capstoneproject.pedromarco.eventapp.userdetails.di.DaggerUserDetailsComponent;
import com.capstoneproject.pedromarco.eventapp.userdetails.di.UserDetailsComponent;
import com.capstoneproject.pedromarco.eventapp.userdetails.di.UserDetailsModule;
import com.capstoneproject.pedromarco.eventapp.userdetails.ui.UserDetailsView;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.Firebase;
import com.google.android.gms.ads.MobileAds;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

/**
 * Application class initialized all Dagger Modules as well as Firebase, Twitter, Facebook
 */

public class EventApp extends Application {
    private final static String EMAIL_KEY = "email";
    private final static String NOTIFICATIONS_KEY = "notifications";
    private LibsModule libsModule;
    private EventAppModule eventAppModule;
    private DomainModule domainModule;

    @Override
    public void onCreate() {
        super.onCreate();
        initFirebase();
        initAdmob();
        initModules();
        initFacebook();
        initTwitter();
    }

    /**
     * Initialize Firebase
     */
    private void initFirebase() {
        Firebase.setAndroidContext(this);
    }

    /**
     * Initialize Admob
     */
    private void initAdmob() {
        MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID));
    }

    /**
     * * Initialize Facebook
     */
    private void initFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    /**
     * Initialize Twitter
     */
    private void initTwitter() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    /**
     * Init the Application and Domain modules
     */
    private void initModules() {
        libsModule = new LibsModule();
        eventAppModule = new EventAppModule(this);
        domainModule = new DomainModule();
    }

    /**
     * Get the email key to store the email in preferences
     *
     * @return the email key to stored in preferences
     */
    public static String getEmailKey() {
        return EMAIL_KEY;
    }

    /**
     * Get the notificaton key to store the notification status in preferences
     *
     * @return the notifications key to stored in preferences
     */
    public static String getNotificationsKey() {
        return NOTIFICATIONS_KEY;
    }

    /**
     * build and return the dagger LoginComponent for the dependency injection of all the login package
     *
     * @param view loginview
     * @return the dagger Logincomponent
     */
    public LoginComponent getLoginComponent(LoginView view) {
        return DaggerLoginComponent
                .builder()
                .eventAppModule(eventAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .loginModule(new LoginModule(view))
                .build();
    }

    /**
     * build and return the dagger NewUserComponent for the dependency injection of all the newuser package
     *
     * @param view NewUserview
     * @return the dagger NewUsercomponent
     */
    public NewUserComponent getNewUserComponent(NewUserView view) {
        return DaggerNewUserComponent
                .builder()
                .eventAppModule(eventAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .newUserModule(new NewUserModule(view))
                .build();
    }

    /**
     * build and return the dagger MainComponent for the dependency injection of the main package (except the content of each tab, which are sepparate components bellow)
     *
     * @param mainView  view of the mainactivity
     * @param manager   fragmen maneger to manage the fragment of each viewpager tab
     * @param fragments array with the fragments included in the viewpager
     * @param titles    array with the tittles of each tab of the tablayout
     * @return the dagger MainComponent
     */
    public MainComponent getMainComponent(MainView mainView, FragmentManager manager, Fragment[] fragments, String[] titles) {
        return DaggerMainComponent
                .builder()
                .eventAppModule(eventAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .mainModule(new MainModule(mainView, titles, fragments, manager))
                .build();
    }

    /**
     * build and return the dagger NearbyFragmentComponent for the dependency injection of all the NearBy tab package
     *
     * @param fragment      fragment containing the Nearby tab
     * @param eventListView view of the event list
     * @param clickListener listener for clicks on events of the list
     * @return the dagger NearbyFragmentComponent
     */
    public NearByFragmentComponent getNearByFragmentComponent(Fragment fragment, EventListView eventListView, OnEventClickListener clickListener) {
        libsModule.setFragment(fragment);
        return DaggerNearByFragmentComponent
                .builder()
                .eventAppModule(eventAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .nearByFragmentModule(new NearByFragmentModule(eventListView, clickListener))
                .build();
    }

    /**
     * build and return the dagger FavouritesFragmentComponent for the dependency injection of all the Favourite tab package
     *
     * @param fragment      fragment containing the Favourites tab
     * @param eventListView view of the event list
     * @param clickListener listener for clicks on events of the list
     * @return the dagger FavouritesFragmentComponent
     */
    public FavouritesFragmentComponent getFavouritesFragmentComponent(Fragment fragment, EventListView eventListView, OnEventClickListener clickListener) {
        libsModule.setFragment(fragment);
        return DaggerFavouritesFragmentComponent
                .builder()
                .eventAppModule(eventAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .favouritesFragmentModule(new FavouritesFragmentModule(eventListView, clickListener))
                .build();
    }

    /**
     * build and return the dagger SearchFragmentComponent for the dependency injection of all the Search tab package
     *
     * @param fragment      fragment containing the Search tab
     * @param searchView    view of the search fragment
     * @param clickListener listener for clicks on events of the list
     * @return the dagger SearchFragmentComponent
     */
    public SearchFragmentComponent getSearchFragmentComponent(Fragment fragment, SearchView searchView, OnEventClickListener clickListener) {
        libsModule.setFragment(fragment);
        return DaggerSearchFragmentComponent
                .builder()
                .eventAppModule(eventAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .searchFragmentModule(new SearchFragmentModule(searchView, clickListener))
                .build();
    }

    /**
     * build and return the dagger CreateEventComponent for the dependency injection of all the creteevent package
     *
     * @param view view of the CreateEvent Activity
     * @return the dagger CreateEventComponent
     */
    public CreateEventComponent getCreateEventComponent(CreateEventView view) {
        return DaggerCreateEventComponent
                .builder()
                .eventAppModule(eventAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .createEventModule(new CreateEventModule(view))
                .build();
    }

    /**
     * build and return the dagger EventDetailsComponent for the dependency injection of all the eventdetails package
     *
     * @param view view of the EventDetails Activity
     * @return the dagger EventDetailsComponent
     */
    public EventDetailsComponent getSingleEventComponent(EventDetailsView view) {
        return DaggerEventDetailsComponent
                .builder()
                .eventAppModule(eventAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .eventDetailsModule(new EventDetailsModule(view))
                .build();
    }

    /**
     * build and return the dagger UserDetailsComponent for the dependency injection of all the userdetails package
     *
     * @param view view of the UserDetails Activity
     * @return the dagger UserDetailsComponent
     */
    public UserDetailsComponent getUserDetailsComponent(UserDetailsView view) {
        return DaggerUserDetailsComponent
                .builder()
                .eventAppModule(eventAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .userDetailsModule(new UserDetailsModule(view))
                .build();
    }

    /**
     * build and return the dagger EditUserComponent for the dependency injection of all the edituser package inside settings
     *
     * @param view view of the EditUser Activity
     * @return the dagger EditUserComponent
     */
    public EditUserComponent getEditUserComponent(EditUserView view) {
        return DaggerEditUserComponent
                .builder()
                .eventAppModule(eventAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .editUserModule(new EditUserModule(view))
                .build();
    }

    /**
     * build and return the dagger SettingsComponent for the dependency injection of all the settings package
     *
     * @param activity activity of the settings
     * @return the dagger SettingsComponent
     */
    public SettingsComponent getSettingComponent(SettingsActivity activity) {
        return DaggerSettingsComponent
                .builder()
                .eventAppModule(eventAppModule)
                .settingsModule(new SettingsModule())
                .build();
    }
}
