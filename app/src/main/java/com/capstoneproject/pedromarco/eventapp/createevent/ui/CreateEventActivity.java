package com.capstoneproject.pedromarco.eventapp.createevent.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.capstoneproject.pedromarco.eventapp.EventApp;
import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.createevent.CreateEventPresenter;
import com.capstoneproject.pedromarco.eventapp.domain.Util;
import com.capstoneproject.pedromarco.eventapp.lib.GlideImageLoader;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity handing all the UI of the event creation
 */
public class CreateEventActivity extends AppCompatActivity implements CreateEventView {
    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etDescription)
    EditText etDescription;
    @Bind(R.id.etMaxPeople)
    EditText etMaxPeople;
    @Bind(R.id.etDate)
    EditText etDate;
    @Bind(R.id.etTime)
    EditText etTime;
    @Bind(R.id.etLocation)
    EditText etLocation;
    @Bind(R.id.btnSelectPicture)
    Button btnSelectPicture;
    @Bind(R.id.ivEventPicture)
    ImageView ivEventPicture;
    @Bind(R.id.btnChangePicture)
    Button btnChangePicture;
    @Bind(R.id.spinnerCategory)
    Spinner spinnerCategory;
    @Bind(R.id.layoutForm)
    ScrollView layoutForm;
    @Bind(R.id.login_progress)
    ProgressBar loginProgress;

    @Inject
    CreateEventPresenter presenter;
    EventApp app;
    private static final int PLACE_PICKER_REQUEST = 1;
    private double latitude;
    private double longitude;
    private String picturePath;

    /**
     * Start the presenter, and set up the dagger injection and the necessary UI elements
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);
        setUpInjection();
        setUpDatePicker();
        setUpLocationPicker();
        picturePath = "";
        presenter.onCreate();
    }

    /**
     * Set up the dagger injection getting the Component
     */
    void setUpInjection() {
        app = (EventApp) getApplication();
        app.getCreateEventComponent(this).inject(this);
    }

    /**
     * Sets up the location picker
     */
    private void setUpLocationPicker() {
        longitude = 0;
        latitude = 0;
        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(CreateEventActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sets up the date picker
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
                etDate.setText(sdf.format(myCalendar.getTime()));
            }

        };
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(CreateEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });
    }

    /**
     * Handles the results of the Place Picker and the ImageCropping
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String address = place.getAddress().toString();
                etLocation.setText(address);
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
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
     * Destroys the presenter
     */
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Infalte the options menu
     *
     * @param menu menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_event, menu);
        return true;
    }

    /**
     * HAndles the click of the menu item (create the event)
     *
     * @param item item clicked
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_event) {
            item.setEnabled(false);
            createEvent();
        }
        return true;
    }

    /**
     * Enables the create button again by calling invalidateOptionsMenu
     */
    @Override
    public void enableCreateButton() {
        invalidateOptionsMenu();
    }

    /**
     * Teel the presenter ot create the event with the layout values
     */
    private void createEvent() {
        int maxPeople;
        String maxPeopleString = etMaxPeople.getText().toString();
        if (maxPeopleString.isEmpty() || maxPeopleString.length() > 9) {
            maxPeople = 0;
        } else {
            maxPeople = Integer.parseInt(maxPeopleString);
        }
        presenter.createEvent(etName.getText().toString(), etDescription.getText().toString(), spinnerCategory.getSelectedItem().toString(), maxPeople, etDate.getText().toString(), etTime.getText().toString(), etLocation.getText().toString(), latitude, longitude, picturePath);
    }

    /**
     * Returns to the Main activity finishing this (event created suceessfully)
     */
    @Override
    public void returnToMainactivity() {
        Toast.makeText(this, R.string.create_event_event_created_success, Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * Show the name empty error
     */
    @Override
    public void setNameEmptyError() {
        etName.setError(getString(R.string.create_event_error_invalid_name));
    }

    /**
     * Show the description empty error
     */
    @Override
    public void setDescriptionEmptyError() {
        etDescription.setError(getString(R.string.create_event_error_invalid_description));
        etDescription.requestFocus();
    }

    /**
     * Show the invalid date error
     */
    @Override
    public void setDateError() {
        Toast.makeText(this, R.string.create_event_error_invalid_date, Toast.LENGTH_LONG).show();
    }

    /**
     * Show the time empty error
     */
    @Override
    public void setTimeEmptyError() {
        etTime.setError(getString(R.string.create_event_error_invalid_time));
        etTime.requestFocus();
    }

    /**
     * Show the location invlaid error
     */
    @Override
    public void setLocationError() {
        Toast.makeText(this, R.string.create_event_error_invalid_location, Toast.LENGTH_LONG).show();
    }

    /**
     * Show the invlaid picture error
     */
    @Override
    public void setPictureError() {
        Toast.makeText(this, R.string.create_event_error_invalid_picture, Toast.LENGTH_LONG).show();
    }

    /**
     * Show the generic create event error
     */
    @Override
    public void createEventError() {
        Toast.makeText(this, R.string.create_event_database_error, Toast.LENGTH_LONG).show();
    }

    /**
     * Handles the click of the button to set/change picture
     *
     * @param view view clicked
     */
    @OnClick({R.id.btnSelectPicture, R.id.btnChangePicture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSelectPicture:
                selectPicture();
                break;
            case R.id.btnChangePicture:
                selectPicture();
                break;
        }
    }

    /**
     * Start the CropImage activity so the user can choose a picture
     */
    private void selectPicture() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropWindowSize(400, 300)
                .setAspectRatio(4, 3)
                .start(this);
    }

    /**
     * Update the picturepath with the image obtained and update the imageview
     *
     * @param imageUri uri of the image
     */
    private void imageSelected(Uri imageUri) {
        picturePath = Util.getRealPathFromURI(this, imageUri);
        btnSelectPicture.setVisibility(View.GONE);
        btnChangePicture.setVisibility(View.VISIBLE);
        ivEventPicture.setVisibility(View.VISIBLE);
        GlideImageLoader.loadWithContext(ivEventPicture, picturePath, this);
    }

    /**
     * Show the pregress bar and hide the layout
     */
    @Override
    public void showProgress() {
        layoutForm.setVisibility(View.GONE);
        loginProgress.setVisibility(View.VISIBLE);
    }

    /**
     * Show the layout and hide the progressbar
     */
    @Override
    public void hideProgress() {
        layoutForm.setVisibility(View.VISIBLE);
        loginProgress.setVisibility(View.GONE);
    }
}
