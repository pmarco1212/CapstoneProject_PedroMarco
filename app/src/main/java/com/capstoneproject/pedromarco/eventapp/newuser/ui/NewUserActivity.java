package com.capstoneproject.pedromarco.eventapp.newuser.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.capstoneproject.pedromarco.eventapp.EventApp;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.domain.Util;
import com.capstoneproject.pedromarco.eventapp.lib.GlideImageLoader;
import com.capstoneproject.pedromarco.eventapp.main.ui.MainActivity;
import com.capstoneproject.pedromarco.eventapp.newuser.NewUserPresenter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Activity handing all the UI of the new user screen
 */
public class NewUserActivity extends AppCompatActivity implements NewUserView {
    @Bind(R.id.etUsername)
    EditText etUsername;
    @Bind(R.id.etFirstname)
    EditText etFirstname;
    @Bind(R.id.etLastaname)
    EditText etLastaname;
    @Bind(R.id.etDescription)
    EditText etDescription;
    @Bind(R.id.btnSelectPicture)
    Button btnSelectPicture;
    @Bind(R.id.ivProfilePic)
    ImageView ivProfilePic;
    @Bind(R.id.login_progress)
    ProgressBar loginProgress;
    @Bind(R.id.layoutForm)
    LinearLayout layoutForm;

    @Inject
    NewUserPresenter presenter;

    private EventApp app;

    private String picturePath;

    /**
     * Start the presenter, and set up the dagger injection and the necessary UI elements
     *
     * @param savedInstanceState savedinstance
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);
        ButterKnife.bind(this);
        setUpInjection();
        presenter.onCreate();
    }

    /**
     * Set up the dagger injection getting the Component
     */
    private void setUpInjection() {
        app = (EventApp) getApplication();
        app.getNewUserComponent(this).inject(this);
    }

    /**
     * Inflate the options menu
     *
     * @param menu menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_userdetails, menu);
        return true;
    }

    /**
     * Call the addition of the user if the button is clicked
     *
     * @param item item selected
     * @return super
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_done) {
            handleAddUser();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Display success toas and navigate to the main screen
     */
    @Override
    public void newUserSuccess() {
        Toast.makeText(this, R.string.newuser_toast_usercreated, Toast.LENGTH_SHORT).show();
        navigateToMainScreen();
    }

    /**
     * Show the error pased as parameter in a toast
     *
     * @param errorMessage error to display
     */
    @Override
    public void newUserError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    /**
     * Show the username already in use error
     */
    @Override
    public void usernameAlreadyInUseError() {
        Toast.makeText(this, R.string.newuser_error_username_already_in_use, Toast.LENGTH_LONG).show();
    }

    /**
     * displays the empty field error
     */
    @Override
    public void emptyFieldError() {
        Toast.makeText(this, R.string.newuser_errormessage_emptyfield, Toast.LENGTH_LONG).show();
    }

    /**
     * Dsiplys the image uplad error
     */
    @Override
    public void onImageUploadError() {
        Toast.makeText(this, R.string.newuser_error_image_invalid, Toast.LENGTH_LONG).show();
    }

    /**
     * Navigate to the main activity
     */
    @Override
    public void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Execute the presenter method to add the user
     */
    @Override
    public void handleAddUser() {
        presenter.addNewUser(etUsername.getText().toString(), etFirstname.getText().toString(), etLastaname.getText().toString(), etDescription.getText().toString(), picturePath);
    }

    /**
     * Click event of the button, execute the method to get the profile pic
     */
    @OnClick(R.id.btnSelectPicture)
    public void onViewClicked() {
        getProfilePicture();
    }

    /**
     * Execute the cropimage activity to get the profile pic selected by the user
     */
    public void getProfilePicture() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setMinCropWindowSize(400, 400)
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(this);
    }

    /**
     * Handles the activity results of the image cropping
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageSelected(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, R.string.image_cropper_error, Toast.LENGTH_SHORT).show();
                Log.e("Image Cropper", error.getMessage());
            }
        }
    }

    /**
     * Update the picturepath with the picture selected and update the imageview
     *
     * @param imageUri uri of the image
     */
    private void imageSelected(Uri imageUri) {
        picturePath = Util.getRealPathFromURI(this, imageUri);
        btnSelectPicture.setText(R.string.newuser_btn_changepicture);
        ivProfilePic.setVisibility(View.VISIBLE);
        GlideImageLoader.loadWithContext(ivProfilePic, picturePath, this);
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
     * hide the layout and show the progress bar
     */
    @Override
    public void showProgress() {
        layoutForm.setVisibility(View.GONE);
        loginProgress.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the progressbar and show the layout
     */
    @Override
    public void hideProgress() {
        layoutForm.setVisibility(View.VISIBLE);
        loginProgress.setVisibility(View.GONE);
    }
}


