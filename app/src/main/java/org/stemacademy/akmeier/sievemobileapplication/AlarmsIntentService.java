package org.stemacademy.akmeier.sievemobileapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.a53914.sievemobileapplication.R;

public class AlarmsIntentService extends JobService {
    private static final int NOTIFICATION_ID=555;



    @Override
    public boolean onStartJob(JobParameters params) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"Sieve App")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Sieve App")
                .setContentText("You have tasks to do!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setChannelId("Sieve App Best App")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent notifyIntent = new Intent(this, HomePage.class);
        PendingIntent activity = PendingIntent.getActivity(this,0,notifyIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(activity);
        Notification notificationCompat=mBuilder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID,notificationCompat);
        Log.d("AlarmsIntentService","Success!");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
