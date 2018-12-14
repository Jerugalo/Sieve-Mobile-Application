package com.example.a53914.sievemobileapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DatabaseUtils;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TimePicker;

import com.example.a53914.sievemobileapplication.db.TaskDatabase;
import com.example.a53914.sievemobileapplication.db.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static java.lang.StrictMath.toIntExact;

/**
 * Default activity for app. Displays a list of events created by user, links to settings menu
 * and task creation menu. Events display name of event, color coded priority, and provide link to
 * further information on the task.
 */

public class HomePage extends AppCompatActivity {

    GlobalVars global = GlobalVars.getInstance();
    Task mTask =global.getCurrentTask();



    /**
     * Creates Activity and sets up the recycler view. Recycler view pulls a list of Task objects
     * from the TaskDao and displays them in a visual list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createNotificationChannel();

        BroadcastReceiver notificationJava = new Notificationjava();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(notificationJava,filter);

        TaskDatabase taskDatabase = TaskDatabase.getInstance(HomePage.this);
        global.setTaskData(taskDatabase.taskDao().getAll());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        RecyclerView rvTasks = findViewById(R.id.TaskList);
        TaskListAdapter adapter = new TaskListAdapter(global.getTaskData());
        rvTasks.setAdapter(adapter);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            Boolean delete = (Boolean) bd.get("delete");
            if (delete){
                taskDatabase.taskDao().delete(mTask);
            }
        }

        createListofNotifications();
    }

    /** Instates the RecyclerView */
    @Override
    protected void onStart(){
        super.onStart();

        TaskDatabase taskDatabase = TaskDatabase.getInstance(HomePage.this);
        RecyclerView rvTasks = findViewById(R.id.TaskList);
        List<Task> tasks = taskDatabase.taskDao().getAll();
        TaskListAdapter adapter = new TaskListAdapter(tasks);
        rvTasks.setAdapter(adapter);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        createListofNotifications();
    }

    /** Opens Settings activity */
    public void toSettings(android.view.View view) {
        Intent toSettings = new Intent(this, Settings.class);
        startActivity(toSettings);
    }

    /** Opens TaskCreate activity */
    public void toTaskCreate(View view) {
        Intent toTaskCreate = new Intent(this, TaskCreate.class);
        startActivity(toTaskCreate);
    }

    /**
     * Creates an alarm to be triggered to send a notification, also built here
     * @param context
     * @param day
     * @param month
     * @param year
     * @param hour
     * @param minute
     * @param notificationID
     * @param taskID
     */
    public void setAlarm(Context context, int day,int month, int year, int hour, int minute,int notificationID, int taskID){
        TaskDatabase taskDatabase = TaskDatabase.getInstance(HomePage.this);
        List<Task> tasks = taskDatabase.taskDao().getAll();
        String homework = tasks.get(taskID).getNameID();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"Sieve App")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Sieve App")
                .setContentText("You have homework to do!")
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Your Homework is " + homework+"!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setChannelId("Sieve App Best App")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                //Also has the necessary intent to send the user to the app upon tapping the notification, it is just added later.

        Intent intent = new Intent(context, HomePage.class);
        PendingIntent activity = PendingIntent.getActivity(context,notificationID,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(activity);

        Notification notification = mBuilder.build();

        Intent notificationIntent = new Intent(context,Notificationjava.class);
        notificationIntent.putExtra(Notificationjava.NOTIFICATION_ID,notificationID);
        notificationIntent.putExtra(Notificationjava.NOTIFICATION,notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationID,notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(year,month,day,hour,minute);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alarmCalendar.getTimeInMillis(),pendingIntent);

    }

    /**
     * Creates a notification channel for the app, as required by android post SDK 26(?)
     */
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= 26){
            CharSequence name = "SieveAppChannel";
            String description = "This is the notification channel for the Sieve App";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Sieve App Best App",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //Currently Activated by pressing a button, I'll change that now to whenever user goes to homepage

    /**
     * Cycles through all tasks and activates notifications for all tasks that have them
     * @param view
     */
    public void createListofNotifications(){
        int today =Calendar.DAY_OF_YEAR;
        TaskDatabase taskDatabase = TaskDatabase.getInstance(this);
        List<Task> tasks = taskDatabase.taskDao().getAll();
        int scheduleDay=0;
        int scheduleMonth=0;
        int scheduleYear=0;
        int scheduleHour=0;
        int scheduleMinute=0;
        for(int i = 0; i<taskDatabase.taskDao().getRowCount(); i++){
            Task task =tasks.get(i);
            if(task.getNotified() == 0){
                String AlertList =task.getAlertList();
                if(AlertList==null || AlertList.length()<1) {
                    //Oops
                }else {
                    List<String> dates=new ArrayList<>(Arrays.asList(AlertList.split(":")));
                    for(int iI=0;iI<dates.size();iI++){
                        List<String> alarmseparate= new ArrayList<>(Arrays.asList(dates.get(iI).split("/")));
                        for(int II = 0; II<alarmseparate.size(); II++){
                            if(II==0){
                                scheduleHour = Integer.parseInt(alarmseparate.get(II));
                            }else if(II==1){
                                scheduleMinute = Integer.parseInt(alarmseparate.get(II));
                            }else if (II ==2){
                                scheduleYear = Integer.parseInt(alarmseparate.get(II));
                            }else if (II == 3){
                                scheduleMonth = Integer.parseInt(alarmseparate.get(II));
                            }else if(II==4){
                                scheduleDay = Integer.parseInt(alarmseparate.get(II));
                            }
                        }
                        int LastNotificationID=toIntExact(SystemClock.uptimeMillis()/1000);
                        setAlarm(this,scheduleDay,scheduleMonth,scheduleYear,scheduleHour,scheduleMinute,LastNotificationID,i);
                        scheduleDay=0;
                        scheduleMonth=0;
                        scheduleYear=0;
                        scheduleHour=0;
                        scheduleMinute=0;
                        alarmseparate.clear();
                    }
                    dates.clear();
                }
            }
            tasks.get(i).setNotified(1);
        }
    }

}
