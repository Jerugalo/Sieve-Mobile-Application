package org.stemacademy.akmeier.sievemobileapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.PersistableBundle;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class AlarmsIntentService extends JobService {
    private static final int NOTIFICATION_ID=555;



    @Override
    public boolean onStartJob(JobParameters params) {
        JobScheduler jS=(JobScheduler)this.getSystemService(this.JOB_SCHEDULER_SERVICE);
        PersistableBundle pB=params.getExtras();
        ComponentName cN=new ComponentName(this,AlarmsIntentService.class);
        if(pB!=null){
            Boolean rCheck=pB.getBoolean("ISREPEAT");
            Integer Hour=pB.getInt("HOUR");
            Integer Minute=pB.getInt("MINUTE");
            Integer Day=pB.getInt("DAY");
            Integer TaskId=pB.getInt("TASKID");
            Integer Id=pB.getInt("ID");
            String Name=pB.getString("NAME");
            if(rCheck!=null){
                if(Hour!=null){
                    if(Minute!=null){
                        if (Day!=null){
                            if(TaskId!=null) {
                                if(Id!=null) {
                                        JobInfo jI = new JobInfo.Builder(Id, cN).setPeriodic(TimeUnit.DAYS.toMillis(1))
                                                .setExtras(pB)
                                                .build();
                                }
                            }
                        }
                    }
                }
            }
            if(Name!=null) {
                NotificationCompat.Builder MBuilder = new NotificationCompat.Builder(this, "Sieve App")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Sieve App")
                        .setContentText("Don't forget about: "+Name+"!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setChannelId("Sieve App Best App")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                Intent notifyIntent = new Intent(this, HomePage.class);
                PendingIntent activity = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                MBuilder.setContentIntent(activity);
                Notification notificationCompat = MBuilder.build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(NOTIFICATION_ID, notificationCompat);
                return false;
            }else{
                NotificationCompat.Builder MBuilder = new NotificationCompat.Builder(this, "Sieve App")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Sieve App")
                        .setContentText("Don't forget about your habit!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setChannelId("Sieve App Best App")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                Intent notifyIntent = new Intent(this, HomePage.class);
                PendingIntent activity = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                MBuilder.setContentIntent(activity);
                Notification notificationCompat = MBuilder.build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(NOTIFICATION_ID, notificationCompat);
                return false;
            }
        }

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
