package com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.ui;

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
import android.widget.Toast;

import com.capstoneproject.pedromarco.eventapp.EventApp;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.domain.Util;
import com.capstoneproject.pedromarco.eventapp.entities.User;
import com.capstoneproject.pedromarco.eventapp.lib.GlideImageLoader;
import com.capstoneproject.pedromarco.eventapp.settings.edituserprofile.EditUserPresenter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserActivity extends AppCompatActivity implements EditUserView {

    @Bind(R.id.etFirstname)
    EditText etFirstname;
    @Bind(R.id.etLastaname)
    EditText etLastaname;
    @Bind(R.id.etDescription)
    EditText etDescription;
    @Bind(R.id.ivProfilePic)
    CircleImageView ivProfilePic;
    @Bind(R.id.btnSelectPicture)
    Button btnSelectPicture;

    private EventApp app;
    private String picturePath;

    @Inject
    EditUserPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        ButterKnife.bind(this);
        setUpInjection();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter.onCreate();
    }

    private void setUpInjection() {
        app = (EventApp) getApplication();
        app.getEditUserComponent(this).inject(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.btnSelectPicture)
    public void onViewClicked() {
        getProfilePicture();
    }

    public void getProfilePicture() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setMinCropWindowSize(400, 400)
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(this);
    }

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

    private void imageSelected(Uri imageUri) {
        picturePath = Util.getRealPathFromURI(this, imageUri);
        btnSelectPicture.setText(R.string.newuser_btn_changepicture);
        ivProfilePic.setVisibility(View.VISIBLE);
        GlideImageLoader.loadWithContext(ivProfilePic, picturePath, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_userdetails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_done) {
            presenter.editUser(etFirstname.getText().toString(), etLastaname.getText().toString(), etDescription.getText().toString(), picturePath);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void editUserSuccess() {
        Toast.makeText(this, R.string.edit_user_toast_update_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void editUserError(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = getResources().getString(R.string.edit_user_error);
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }


    @Override
    public void emptyFieldError() {
        Toast.makeText(this, R.string.newuser_errormessage_emptyfield, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImageUploadError() {
        Toast.makeText(this, R.string.newuser_error_image_invalid, Toast.LENGTH_LONG).show();
    }


    @Override
    public void fillUserDetails(User user) {
        etFirstname.setText(user.getFirstname());
        etLastaname.setText(user.getSurname());
        etDescription.setText(user.getDescription());
        GlideImageLoader.loadWithContext(ivProfilePic, user.getProfilePhotoURL(), this);
    }
}
