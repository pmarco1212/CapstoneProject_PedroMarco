package com.capstoneproject.pedromarco.eventapp.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.SplashActivity;
import com.capstoneproject.pedromarco.eventapp.entities.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class used to schedule notifications
 */
public class NotificationsScheduler {
    static final String EVENT_NAME = "EVENT_NAME";

    /**
     * Method to Schedule a notification for a given event date
     *
     * @param context context
     * @param event   event to schedule
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleNotifications(Context context, Event event) {
        String eventDateString = event.getDate();
        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.date_format));
        Date eventDate = new Date();
        try {
            eventDate = format.parse(eventDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentDate = new Date();

        long milisecondsToDate = eventDate.getTime() - currentDate.getTime();
        scheduleJob(context, milisecondsToDate, event.getName());
    }

    //

    /**
     * Method to Cancel all scheduled jobs
     *
     * @param context context
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void cancelSchedulledJobs(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
    }

    /**
     * Mehtod that schedule a job at the given date
     *
     * @param context           context
     * @param milisecondsToDate miliseconds to the date
     * @param eventName         name of the event
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void scheduleJob(Context context, long milisecondsToDate, String eventName) {
        ComponentName serviceComponent = new ComponentName(context, EventAppJobService.class);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        //Assign the id depending on how many jobs are scehduled to avoid repetition
        int id = jobScheduler.getAllPendingJobs().size();
        JobInfo.Builder builder = new JobInfo.Builder(id, serviceComponent);
        //Set job to trigger on the desired date (at 00:00) with 1h margin (so between 0 and 1 am)
        long minimunLatency = milisecondsToDate;
        long maximumLatency = milisecondsToDate + 3600000;
        builder.setMinimumLatency(minimunLatency);
        builder.setOverrideDeadline(maximumLatency);
        builder.setRequiresDeviceIdle(false); // we don't care if the device is idle
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        builder.setPersisted(true); //persisted across device restarts
        PersistableBundle extras = new PersistableBundle();
        extras.putString(EVENT_NAME, eventName);
        builder.setExtras(extras);
        jobScheduler.schedule(builder.build());
    }

    /**
     * Static class defining the jobService. The job is to display a notification.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static public class EventAppJobService extends JobService {

        @Override
        public boolean onStartJob(JobParameters params) {
            //build the notification setting the layout elements
            String eventName = params.getExtras().getString(EVENT_NAME);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this, "EventApp")
                            .setContentTitle(eventName)
                            .setContentText("You have an event to assist today!!");

            //Create pending intent, mention the Activity which needs to be
            //triggered when user clicks on notification(MainActivity in this case)
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, SplashActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(contentIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1, mBuilder.build());
            //Return false as the job would be done by now
            return false;
        }

        @Override
        public boolean onStopJob(JobParameters params) {
            return false;
        }
    }
}

