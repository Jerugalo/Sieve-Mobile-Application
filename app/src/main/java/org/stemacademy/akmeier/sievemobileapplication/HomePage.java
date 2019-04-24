package org.stemacademy.akmeier.sievemobileapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import pl.droidsonroids.gif.GifImageView;

import org.stemacademy.akmeier.sievemobileapplication.db.TaskDatabase;
import org.stemacademy.akmeier.sievemobileapplication.db.Task;
import org.stemacademy.akmeier.sievemobileapplication.utilities.SwipeController;
import org.stemacademy.akmeier.sievemobileapplication.utilities.SwipeControllerActions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.toIntExact;

/**
 * Default activity for app. Displays a list of events created by user, links to settings menu
 * and task creation menu. Events display name of event, color coded priority, and provide link to
 * further information on the task.
 */

public class HomePage extends AppCompatActivity {
    private static Context context;

    public class SharedPreferencesManager {
        private SharedPreferences themeStorage;
        private SharedPreferences.Editor sEditor;
        private Context context;

        SharedPreferencesManager(Context context) {
            this.context = context;
            themeStorage = PreferenceManager.getDefaultSharedPreferences(context);
        }

        private SharedPreferences.Editor getEditor() {
            return themeStorage.edit();
        }

        int retrieveInt(String tag, int defValue) {
            return themeStorage.getInt(tag, defValue);
        }

        void storeInt(String tag, int defValue) {
            sEditor = getEditor();
            sEditor.putInt(tag, defValue);
            sEditor.commit();
        }
    }

    GlobalVars global = GlobalVars.getInstance();
    Task mTask = global.getCurrentTask();
    JobScheduler jobScheduler;
    TextView dateText;
    static TaskDatabase taskDatabase;
    static TaskListAdapter adapter;
    static List<Task> tasks;
    RecyclerView rvTasks;

    Integer currentID;
    Dictionary<Integer, List<Integer>> AlarmsDict;
    List<Integer> alarmsForTask;

    /**
     * Creates Activity and sets up the recycler view. Recycler view pulls a list of Task objects
     * from the TaskDao and displays them in a visual list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createNotificationChannel();
        jobScheduler = (JobScheduler) this.getSystemService(this.JOB_SCHEDULER_SERVICE);
        BroadcastReceiver notificationJava = new Notificationjava();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(notificationJava, filter);

        if (global.getgAlarmDict() == null) {
            global.setgAlarmDict(new Hashtable<Integer, List<Integer>>());
        }
        AlarmsDict = global.getgAlarmDict();
        currentID = -1;
        alarmsForTask = new ArrayList<Integer>();

        super.onCreate(savedInstanceState);
        context = this;
        Fabric.with(this, new Crashlytics());
        determineTheme();
        setContentView(R.layout.activity_home_page);
        dateText = (TextView) findViewById(R.id.dateViewHP);
        setDate(dateText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        determineTheme();

        createListofNotifications();
        setDate(dateText);
    }

    @Override
    protected void onResume() {
        super.onResume();

        taskDatabase = TaskDatabase.getInstance(this);
        tasks = taskDatabase.taskDao().getAll();
        sortTasks();
        rvTasks = findViewById(R.id.TaskList);
        adapter = new TaskListAdapter(this);
        rvTasks.setAdapter(adapter);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter.updateItems(tasks);

        SwipeController swipeController;
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
               deleteTask(adapter.items.get(position));//adapter.items.get(position)
            }

            public void onLeftClicked(int position) {
                ToDetails(adapter.items.get(position));
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rvTasks);

        Intent intent = getIntent();
        final Bundle bd = intent.getExtras();
        createListofNotifications();
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.HomeView);
        ViewTreeObserver vTO = layout.getViewTreeObserver();
        vTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (bd != null) {
                    Boolean delete = (Boolean) bd.get("complete");
                    if (delete != null && delete) {
                        layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        EnableDisableCheckmark(false);
                    }
                }
            }
        });
        if (bd != null) {
            Boolean clearAlarms = (Boolean) bd.get("CLEAR_ALARMS");
            if (clearAlarms != null && clearAlarms) {
                clearAlarms();
            }
        }
    }

    /**
     * Opens Details activity
     *
     * @param task the task to be opened
     */
    public void ToDetails(Task task) {
        if (task.getTypeID() != -1) {
            Intent toDetails = new Intent(this, AssignmentDetails.class);
            global.setCurrentTask(task);
            startActivity(toDetails);
        }
    }

    /**
     * Opens Settings activity
     */
    public void toSettings(android.view.View view) {
        Intent toSettings = new Intent(this, Settings.class);
        startActivity(toSettings);
    }

    /**
     * Opens TaskCreate activity
     */
    public void toTaskCreate(View view) {
        Intent toTaskCreate = new Intent(this, TaskCreate.class);
        startActivity(toTaskCreate);
    }

    /**
     *
     */
    private void sortTasks() {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                float compare1;
                float compare2;
                float days1 = 0;
                float days2 = 0;
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.AM_PM, Calendar.PM);
                Date date1 = calendar.getTime();
                Date date2 = calendar.getTime();
                String incorrectTaskDate1 = o1.getDueDate();
                String incorrectTaskDate2 = o2.getDueDate();
                List<String> divided1 = new ArrayList<>(Arrays.asList(incorrectTaskDate1.split("/")));
                String cTD1 = divided1.get(1) + "/" + divided1.get(0) + "/" + divided1.get(2);
                List<String> divided2 = new ArrayList<>(Arrays.asList(incorrectTaskDate2.split("/")));
                String cTD2 = divided2.get(1) + "/" + divided2.get(0) + "/" + divided2.get(2);
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date1 = sdf1.parse(cTD1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date2 = sdf2.parse(cTD2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date1 != null && date2 != null) {
                    long diff1 = date1.getTime() - calendar.getTime().getTime();
                    long diff2 = date2.getTime() - calendar.getTime().getTime();
                    days1 = abs(TimeUnit.DAYS.convert(diff1, TimeUnit.MILLISECONDS));
                    days2 = abs(TimeUnit.DAYS.convert(diff2, TimeUnit.MILLISECONDS));
                }
                if (days1 != 0 && days2 != 0) {
                    int sample = o1.getPriority();
                    int sample2 = o2.getPriority();
                    compare1 = ((o1.getPriority() + 1) * 10) / ((days1 + 1) * 50);
                    compare2 = ((o2.getPriority() + 1) * 10) / ((days2 + 1) * 50);
                } else {
                    compare1 = 0;
                    compare2 = 0;
                }
                if (days1 == 0) {
                    compare1 = (100 * (o1.getPriority() + 1)) + 1000;
                }
                if (days2 == 0) {
                    compare2 = (100 * (o2.getPriority() + 1)) + 1000;
                }
                if (o1.getTypeID() == 0) {
                    compare1 += 500;
                } else if (o1.getTypeID() == 2) {
                    compare1 += 250;
                }
                if (o2.getTypeID() == 0) {
                    compare2 += 500;
                } else if (o1.getTypeID() == 2) {
                    compare2 += 250;
                }
                return compare1 > compare2 ? -1 : (compare1 < compare2) ? 1 : 0;
            }
        });
    }



    /**
     * Deletes a task from the homepage.
     *
     * @param task The task to be deleted. Needs to be a copy of the one you want to delete in
     *             the database.
     */
    public void deleteTask(Task task){
        if (global.getgAlarmDict() == null) {
            global.setgAlarmDict(new Hashtable<Integer, List<Integer>>());
        }
        AlarmsDict = global.getgAlarmDict();
        if (task.getTypeID() != -1) {
            List<Integer> alarms = AlarmsDict.get(task.getId());
            if (alarms != null) {
                for (int i = 0; i < alarms.size(); i++) {
                    Integer alarm = alarms.get(i);
                    jobScheduler.cancel(alarm);
                }
            }
            tasks.remove(task);
            taskDatabase.taskDao().delete(task);
            adapter.updateItems(tasks);
        }
    }

    /**
     * Creates an alarm to be triggered to send a notification, also built here
     * @param context put context of calling class
     * @param day
     * @param month
     * @param year
     * @param hour
     * @param minute
     * @param taskID
     */
    public void setAlarm(Context context, int day,int month, int year, int hour, int minute,int taskID){
        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(year,month,day,hour,minute);
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(System.currentTimeMillis());

        ComponentName componentName = new ComponentName(context,AlarmsIntentService.class);
        long diff = alarmCalendar.getTimeInMillis() - otherCalendar.getTimeInMillis();
        int alarmNumber=day+month+year+hour+minute;

        if (diff >= 0) {
            JobInfo jobInfo = new JobInfo.Builder(alarmNumber, componentName)
                    .setMinimumLatency(diff)
                    .setOverrideDeadline(diff+180000)
                    .build();
            jobScheduler.schedule(jobInfo);
            alarmsForTask.add(alarmNumber);
            if(currentID!=taskID){
                if(currentID!=-1) {
                    AlarmsDict.put(currentID, alarmsForTask);
                    currentID=taskID;
                    alarmsForTask.clear();
                }else{
                    currentID=taskID;
                    Integer cIDInteger=new Integer(taskID);
                    if (alarmsForTask!=null){
                        Log.d("HomePage_CreateAlarms","alarmsForTask is not null!");
                        AlarmsDict.put(cIDInteger,alarmsForTask);
                    }else{
                        Log.d("HomePage_CreateAlarms","alarmsForTask is null, we've got a problem");
                    }
                }
            }else{
                AlarmsDict.remove(currentID);
                AlarmsDict.put(currentID,alarmsForTask);
            }
            Log.d("setAlarm", "Success!");
            global.setgAlarmDict(AlarmsDict);
        }

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

    /**
     * Cycles through all tasks and activates notifications for all tasks that have them
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
                        setAlarm(this,scheduleDay,scheduleMonth,scheduleYear,scheduleHour,scheduleMinute,task.getId());
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
            else{

            }
            tasks.get(i).setNotified(1);
            taskDatabase.taskDao().update(tasks.get(i));
        }
    }

    public void determineTheme(){
        int themeId = new SharedPreferencesManager(this).retrieveInt("themeId",1);
        if(themeId == 1){setTheme(R.style.SieveDefault);}
        else if(themeId == 2){setTheme(R.style.SieveAlternative);}
        else if(themeId == 3){setTheme(R.style.SieveTwilight);}
        else if(themeId == 4){setTheme(R.style.SieveDark);}
        else if(themeId == 5){setTheme(R.style.SieveSimple);}
        else if(themeId == 6){setTheme(R.style.SieveOlive);}
        else{setTheme(R.style.SieveDefault);}
    }

    public void clearAlarms(){
        Log.d("clearAlarms","At HomePage");
        jobScheduler.cancelAll();
        jobScheduler.getAllPendingJobs();
        Intent intent = new Intent(this,Settings.class);
        startActivity(intent);
    }

    /**
     * Sets the date on a text view
     * @param textView the textView you want to write the date to
     */
    public void setDate(TextView textView){
        Calendar calendar=Calendar.getInstance();
        int dayInt=(calendar.get(Calendar.DAY_OF_WEEK));
        String dayNameS;
        if(dayInt==1){
            dayNameS="Sunday";
        }else if(dayInt==2){
            dayNameS="Monday";
        }else if(dayInt==3){
            dayNameS="Tuesday";
        }else if(dayInt==4){
            dayNameS="Wednesday";
        }else if(dayInt==5){
            dayNameS="Thursday";
        }else if(dayInt==6){
            dayNameS="Friday";
        } else if(dayInt==7){
            dayNameS="Saturday";
        }else{
            dayNameS="";
        }
        int monthInt=(calendar.get(Calendar.MONTH))+1;
        String monthName;
        if(monthInt==1){
            monthName="January";
        }else if (monthInt==2){
            monthName="February";
        }else if(monthInt==3){
            monthName="March";
        }else if(monthInt==4){
            monthName="April";
        }else if(monthInt==5){
            monthName="May";
        }else if(monthInt==6){
            monthName="June";
        }else if(monthInt==7){
            monthName="July";
        }else if(monthInt==8){
            monthName="August";
        }else if(monthInt==9){
            monthName="September";
        }else if(monthInt==10){
            monthName="October";
        }else if(monthInt==11){
            monthName="November";
        }else if(monthInt==12){
            monthName="December";
        }else{
            monthName="";
        }
        int dayNum=calendar.get(Calendar.DAY_OF_MONTH);
        String dateFull=dayNameS+ ", " + monthName + " " + dayNum;
        textView.setText(dateFull);
    }

    /**
     * Makes the checkmark for completion of a task visible or invisible
     * Added 3/8/2019
     * @param setInvisible If true, this will make the checkmark gif on HomePage disappear
     */
    public void EnableDisableCheckmark(Boolean setInvisible) {
        GifImageView checkmark = (GifImageView) findViewById(R.id.CheckMarkView);
        if(setInvisible){
            checkmark.setVisibility(View.GONE);
            checkmark.setElevation(-1);
        }else{
            checkmark.setVisibility(View.VISIBLE);
            checkmark.setElevation(10);
            TimerForCheckmark();
        }
    }

    /**
     * Functions as a timer for how long the checkmark will be displayed
     * Added 3/8/2019
     */
    public void TimerForCheckmark(){
        new CountDownTimer(2000,100){
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                EnableDisableCheckmark(true);
            }
        }.start();
    }

    public static Context getContext(){
        return context;
    }
}
