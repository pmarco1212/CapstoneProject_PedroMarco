package com.capstoneproject.pedromarco.eventapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.capstoneproject.pedromarco.eventapp.login.ui.LoginActivity;

/**
 * Activity that displays the splash screen when the app is loading, until the LoginActivity is ready
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}
