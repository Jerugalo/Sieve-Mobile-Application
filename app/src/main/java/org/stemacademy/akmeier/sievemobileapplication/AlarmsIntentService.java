package org.stemacademy.akmeier.sievemobileapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.a53914.sievemobileapplication.R;

public class AlarmsIntentService extends JobIntentService {
    private static final int NOTIFICATION_ID=555;

    @Override
    protected void onHandleWork(Intent intent){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"Sieve App")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Sieve App")
                .setContentText("You have homework to do!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setChannelId("Sieve App Best App")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent notifyIntent = new Intent(this, HomePage.class);
        PendingIntent activity = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(activity);
        Notification notificationCompat=mBuilder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID,notificationCompat);
    }
}
