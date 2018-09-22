package com.capstoneproject.pedromarco.eventapp.eventdetails.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.capstoneproject.pedromarco.eventapp.R;

import butterknife.Bind;

/**
 * Dialog fragment to rate an event
 */
public class RateEventDialogFragment extends DialogFragment {

    // Use this instance of the interface to deliver action events
    RateEventDialogListener mListener;
    @Bind(R.id.rbRatingBar)
    RatingBar rbRatingBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the RateEventDialogListener so we can send events to the host
            mListener = (RateEventDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement RateEventDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_rate_event, null, false);
        builder.setView(view);
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rbRatingBar);
        builder.setTitle(R.string.event_details_rate_event)
                .setPositiveButton(R.string.event_details_rate_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onRateEventPositiveClick(ratingBar.getRating());
                    }
                })
                .setNegativeButton(R.string.event_details_rate_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface RateEventDialogListener {
        public void onRateEventPositiveClick(float rating);

        public void onRateEventNegativeClick(DialogFragment dialog);
    }

}