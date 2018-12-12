package com.example.a53914.sievemobileapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import static java.lang.Math.toIntExact;

public class Notificationjava extends BroadcastReceiver {


    public static String NOTIFICATION_ID ="notification_id";
    public static String NOTIFICATION = "notification";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int notificationId=intent.getIntExtra(NOTIFICATION_ID,0);
        if (notification != null) {
            notificationManager.notify(notificationId, notification);
            notificationManagerCompat.notify(notificationId, notification);
        }
    }
}
