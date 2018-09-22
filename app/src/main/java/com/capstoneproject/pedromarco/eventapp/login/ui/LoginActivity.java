package com.capstoneproject.pedromarco.eventapp.login.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.capstoneproject.pedromarco.eventapp.EventApp;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.login.LoginPresenter;
import com.capstoneproject.pedromarco.eventapp.main.ui.MainActivity;
import com.capstoneproject.pedromarco.eventapp.newuser.ui.NewUserActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity handing all the UI of the login
 */
public class LoginActivity extends AppCompatActivity implements LoginView {

    @Bind(R.id.etEmail)
    AutoCompleteTextView etEmail;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.email_login_in_button)
    Button emailLoginInButton;
    @Bind(R.id.login_button_facebook)
    LoginButton loginButtonFacebook;
    @Bind(R.id.login_button_twitter)
    TwitterLoginButton loginButtonTwitter;
    @Bind(R.id.login_progress)
    ProgressBar loginProgress;
    @Bind(R.id.rlContainer)
    RelativeLayout rlContainer;
    @Bind(R.id.login_form)
    ScrollView loginForm;

    @Inject
    LoginPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;
    private EventApp app;
    CallbackManager mCallbackManager;

    /**
     * Start the presenter, and set up the dagger injection and the necessary UI elements
     *
     * @param savedInstanceState state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setUpInjection();
        presenter.onCreate();
        checkForActiveSession();
        setUpFaceboookButton();
        setUpTwitterButton();
    }

    /**
     * Set up the dagger injection getting the Component
     */
    private void setUpInjection() {
        app = (EventApp) getApplication();
        app.getLoginComponent(this).inject(this);
    }

    /**
     * Enable the input fields of the login layout
     */
    @Override
    public void enableInput() {
        setInputs(true);
    }

    /**
     * Disable the input fields of the login layout
     */
    @Override
    public void disableInput() {
        setInputs(false);
    }

    /**
     * Show the progress bar
     */
    @Override
    public void showProgress() {
        loginProgress.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the progress bar
     */
    @Override
    public void hideProgress() {
        loginProgress.setVisibility(View.INVISIBLE);
    }

    /**
     * Ask the presenter to check for an active Firebase sesion
     */
    private void checkForActiveSession() {
        presenter.loginFirebase(null, null);
    }

    /**
     * Show an snackbar displaying the sucessfull register of the new user
     */
    @Override
    public void newUserSuccess() {
        Snackbar.make(rlContainer, R.string.login_notice_message_useradded, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Tell the presenter the login have been sucessfull so it can send it to the repository
     * to check if the user is already registered in the database
     */
    @Override
    public void loginSucessfull() {
        presenter.loginSucessfull();
    }

    /**
     * Navigate to the new user screen to create a new EventApp user
     */
    @Override
    public void navigateToNewUserScreen() {
        startActivity(new Intent(this, NewUserActivity.class));
    }

    /**
     * Navigate to the Main screen stating MainActivity
     */
    @Override
    public void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Set the user email in the preferences
     *
     * @param email user email
     */
    @Override
    public void setUserEmail(String email) {
        if (email != null) {
            String key = app.getEmailKey();
            sharedPreferences.edit().putString(key, email).commit();
        }
    }

    /**
     * Show the login error message passed as parameter in the password et
     *
     * @param error error message
     */
    @Override
    public void loginErrorFirebase(String error) {
        etPassword.setText("");
        String msgError = String.format(getString(R.string.login_error_message_signin), error);
        etPassword.setError(msgError);
    }

    /**
     * Show the signup error message passed as parameter in the password et
     *
     * @param error error message
     */
    @Override
    public void newUserErrorFirebase(String error) {
        etPassword.setText("");
        String msgError = String.format(getString(R.string.login_error_message_signup), error);
        etPassword.setError(msgError);
    }

    /**
     * Listener method that execute the corresponding presenter method when one of the buttons is clicked
     *
     * @param view view clicked
     */
    @OnClick({R.id.email_login_in_button, R.id.email_sign_in_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.email_login_in_button:
                presenter.loginFirebase(etEmail.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.email_sign_in_button:
                presenter.signUpFirebase(etEmail.getText().toString(), etPassword.getText().toString());
                break;
        }
    }

    /**
     * Enable or disable the inputs depending on the value of the parameter
     *
     * @param enabled boolean value to enable or disable the inputs
     */
    public void setInputs(boolean enabled) {
        emailLoginInButton.setEnabled(enabled);
        loginButtonFacebook.setEnabled(enabled);
        loginButtonTwitter.setEnabled(enabled);
        etEmail.setEnabled(enabled);
        etPassword.setEnabled(enabled);
    }

    /**
     * Set up the Facebook Login button, as well as define and register the callbacks
     */
    private void setUpFaceboookButton() {
        mCallbackManager = CallbackManager.Factory.create();
        loginButtonFacebook.setReadPermissions("email", "public_profile");
        loginButtonFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showProgress();
                loginForm.setVisibility(View.GONE);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.login_error_facebook, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    /**
     * Handles the facebook login with a given access token
     *
     * @param token access token
     */
    private void handleFacebookAccessToken(AccessToken token) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginSucessfull();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.facebook_login_error, Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    /**
     * Set up the Facebook Login button, as well as define and register the callbacks
     */
    private void setUpTwitterButton() {
        loginButtonTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                showProgress();
                loginForm.setVisibility(View.GONE);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Handles the twitter login of a given session
     *
     * @param session twitter session
     */
    private void handleTwitterSession(TwitterSession session) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginSucessfull();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.twitter_login_error, Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    /**
     * Get the results of the Facebook/Twitter login
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (FacebookSdk.isFacebookRequestCode(requestCode)) { //Is facebebook login result
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        } else { //Is twitter login result
            loginButtonTwitter.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Destroys the presenter when the activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
