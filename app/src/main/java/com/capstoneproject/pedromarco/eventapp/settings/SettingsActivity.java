package com.capstoneproject.pedromarco.eventapp.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.capstoneproject.pedromarco.eventapp.EventApp;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.notifications.NotificationsScheduler;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.ui.EditUserActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity handing all the UI of the settings
 */
public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.switchNotifications)
    Switch switchNotifications;
    @Bind(R.id.tvEditProfile)
    TextView tvEditProfile;
    @Bind(R.id.tvNotificationSettings)
    TextView tvNotificationSettings;

    EventApp app;

    @Inject
    SharedPreferences sharedPreferences;

    /**
     * Activity handing all the UI of the settings
     *
     * @param savedInstanceState saved
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setUpInjection();
        getNotificationStateAndSetSwitch();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpInjection() {
        app = (EventApp) getApplication();
        app.getSettingComponent(this).inject(this);
    }

    private void getNotificationStateAndSetSwitch() {
        boolean state = sharedPreferences.getBoolean(app.getNotificationsKey(), false);
        switchNotifications.setChecked(state);
    }

    private void setNotificationState(boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(app.getNotificationsKey(), state);
        editor.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick({R.id.switchNotifications, R.id.tvEditProfile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.switchNotifications:
                if (switchNotifications.isChecked()) { //Activate notifications
                    activateNotifications();
                } else { //Deactivate notifications
                    deactivateNotifications();
                }
                break;
            case R.id.tvEditProfile:
                goToEditUserProfile();
                break;
        }
    }

    private void goToEditUserProfile() {
        startActivity(new Intent(this, EditUserActivity.class));
    }

    //Activate notification if the version is at least Lollipop (for job scheduler)
    private void activateNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNotificationState(true);
            Toast.makeText(this, R.string.settings_toas_notifications_on, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.settings_notifications_require_version, Toast.LENGTH_SHORT).show();
        }
    }

    //Deactivate notifications and cancel all scheceduled jobs if the version is at least Lollipop (for job scheduler)
    private void deactivateNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NotificationsScheduler.cancelSchedulledJobs(this);
            setNotificationState(false);
            Toast.makeText(this, R.string.settings_toast_notifications_off, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.settings_notifications_require_version, Toast.LENGTH_SHORT).show();
        }
    }
}
