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
import android.os.BaseBundle;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
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
import android.widget.LinearLayout;
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
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.toIntExact;

/**
 * Default activity for app. Displays a list of events created by user, links to settings menu
 * and task creation menu. Events display name of event, color coded priority, and provide link to
 * further information on the task.
 */

public class HomePage extends AppCompatActivity {
    public class SharedPreferencesManager{
        private SharedPreferences themeStorage;
        private SharedPreferences.Editor sEditor;
        private Context context;
        SharedPreferencesManager(Context context){
            this.context = context;
            themeStorage = PreferenceManager.getDefaultSharedPreferences(context);
        }
        private SharedPreferences.Editor getEditor(){
            return themeStorage.edit();
        }
        int retrieveInt(String tag, int defValue){
            return themeStorage.getInt(tag, defValue);
        }
        void storeInt(String tag, int defValue){
            sEditor = getEditor();
            sEditor.putInt(tag, defValue);
            sEditor.commit();
        }
    }
    GlobalVars global = GlobalVars.getInstance();
    Task mTask = global.getCurrentTask();
    List <Integer> alarmNames;
    JobScheduler jobScheduler;
    TextView dateText;
    TaskDatabase taskDatabase;

    TaskListAdapter adapter;
    List<Task> tasks;
    RecyclerView rvTasks;

    Context gContext;
    Dictionary<Integer,List<Integer>> AlarmDicts;

    /**
     * Creates Activity and sets up the recycler view. Recycler view pulls a list of Task objects
     * from the TaskDao and displays them in a visual list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        alarmNames=new ArrayList<>();
        createNotificationChannel();
        jobScheduler=(JobScheduler) this.getSystemService(this.JOB_SCHEDULER_SERVICE);
        BroadcastReceiver notificationJava = new Notificationjava();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(notificationJava,filter);
        AlarmDicts=new Dictionary<Integer, List<Integer>>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public Enumeration<Integer> keys() {
                return null;
            }

            @Override
            public Enumeration<List<Integer>> elements() {
                return null;
            }

            @Override
            public List<Integer> get(Object key) {
                return null;
            }

            @Override
            public List<Integer> put(Integer key, List<Integer> value) {
                return null;
            }

            @Override
            public List<Integer> remove(Object key) {
                return null;
            }
        };

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        determineTheme();
        setContentView(R.layout.activity_home_page);
        dateText= (TextView) findViewById(R.id.dateViewHP);
        setDate(dateText);

        taskDatabase = TaskDatabase.getInstance(this);
        tasks = taskDatabase.taskDao().getAll();
        sortTasks();
        rvTasks = findViewById(R.id.TaskList);
        adapter = new TaskListAdapter(tasks,this);
        rvTasks.setAdapter(adapter);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));


        gContext=this;

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            Boolean delete = (Boolean) bd.get("delete");
            if (delete != null && delete){
                if(mTask.getTypeID()==0){
                    List<Integer> ints= AlarmDicts.get(mTask.getId());
                    if(ints!=null) {
                        for (int i = 0; i < ints.size(); i++) {
                            jobScheduler.cancel(ints.get(i));
                        }
                    }else{}
                }
                deleteTask(mTask);
            }
            Boolean clearAlarms = (Boolean) bd.get("CLEAR_ALARMS");
            if (clearAlarms !=null && clearAlarms){
                clearAlarms();
            }

        }
        final Bundle bundle=bd;
        createListofNotifications();
        final ConstraintLayout layout =(ConstraintLayout) findViewById(R.id.HomeView);
        ViewTreeObserver vTO=layout.getViewTreeObserver();
        vTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(bundle != null) {
                    Boolean delete = (Boolean) bundle.get("delete");
                    if (delete != null && delete) {
                        layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        EnableDisableCheckmark(false);
                    }
                }
            }
        });
    }

    /** Instates the RecyclerView TODO: OUTDATED COMMENT PLEASE UPDATE */
    @Override
    protected void onStart(){
        super.onStart();
        determineTheme();
        SwipeController swipeController;
        final TaskDatabase taskDatabase = TaskDatabase.getInstance(HomePage.this);
        rvTasks = findViewById(R.id.TaskList);
        global.setgDivPos(0);
        for(int i=0;i<taskDatabase.taskDao().getAll().size();i++){
            if(taskDatabase.taskDao().getAll().get(i).getTypeID()!=0) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.AM_PM, Calendar.PM);
                Date date1 = calendar.getTime();
                String incorrectDate = taskDatabase.taskDao().getAll().get(i).getDueDate();
                List<String> divided1 = new ArrayList<>(Arrays.asList(incorrectDate.split("/")));
                String cTD1 = divided1.get(1) + "/" + divided1.get(0) + "/" + divided1.get(2);
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date1 = sdf1.parse(cTD1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long check = date1.getTime() - calendar.getTime().getTime();
                int days1 = 0;
                days1 = abs(toIntExact(TimeUnit.DAYS.convert(check, TimeUnit.MILLISECONDS)));
                if (days1 == 0) {
                    global.setgDivPos(global.getgDivPos() + 1);
                }
            }
        }

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                deleteTask(tasks.get((int)rvTasks.findViewHolderForAdapterPosition(position)
                        .itemView.getTag()));
            }
            public void onLeftClicked(int position) {
                ToDetails((int)rvTasks.findViewHolderForAdapterPosition(position)
                        .itemView.getTag());
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rvTasks);

        createListofNotifications();
        setDate(dateText);
        refreshRecycler();
    }

    /** Opens Details activity */
    public void ToDetails(int position) {
        Intent toDetails = new Intent(this, AssignmentDetails.class);
        global.setCurrentTask(tasks.get(position));
        startActivity(toDetails);
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
     * TODO: Come up with an adequate description of the function
     */
    private void sortTasks(){
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                float compare1;
                float compare2;
                float days1=0;
                float days2=0;
                Calendar calendar =Calendar.getInstance();
                calendar.set(Calendar.MILLISECOND,0);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.HOUR_OF_DAY,12);
                calendar.set(Calendar.HOUR,0);
                calendar.set(Calendar.AM_PM,Calendar.PM);
                Date date1=calendar.getTime();
                Date date2=calendar.getTime();
                String incorrectTaskDate1;
                String incorrectTaskDate2;
                String cTD1;
                String cTD2;
                if(o1.getTypeID()!=0) {
                    incorrectTaskDate1=o1.getDueDate();
                    List<String> divided1 = new ArrayList<>(Arrays.asList(incorrectTaskDate1.split("/")));
                    cTD1 = divided1.get(1) + "/" + divided1.get(0) + "/" + divided1.get(2);
                    SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        date1=sdf1.parse(cTD1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(o2.getTypeID()!=0) {
                    incorrectTaskDate2=o2.getDueDate();
                    List<String> divided2 = new ArrayList<>(Arrays.asList(incorrectTaskDate2.split("/")));
                    cTD2 = divided2.get(1) + "/" + divided2.get(0) + "/" + divided2.get(2);
                    SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        date2=sdf2.parse(cTD2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if(date1!=null&&date2!=null){
                    long diff1= date1.getTime()-calendar.getTime().getTime();
                    long diff2= date2.getTime()-calendar.getTime().getTime();
                    days1=abs(TimeUnit.DAYS.convert(diff1,TimeUnit.MILLISECONDS));
                    days2=abs(TimeUnit.DAYS.convert(diff2,TimeUnit.MILLISECONDS));
                }
                if(days1!=0&&days2!=0){
                    int sample=o1.getPriority();
                    int sample2= o2.getPriority();
                    compare1=((o1.getPriority()+1)*10)/((days1+1)*50);
                    compare2=((o2.getPriority()+1)*10)/((days2+1)*50);
                }else{
                    compare1=0;
                    compare2=0;
                }
                if(days1==0){
                    compare1=(100*(o1.getPriority()+1))+1000;
                }
                if(days2==0){
                    compare2=(100*(o2.getPriority()+1))+1000;
                }
                if(o1.getTypeID()==0){
                    compare1+=500;
                }
                else if(o1.getTypeID()==2){
                    compare1+=250;
                }
                if(o2.getTypeID()==0){
                    compare2+=500;
                }else if(o1.getTypeID()==2){
                    compare2+=250;
                }
                return compare1>compare2 ? -1:(compare1<compare2) ? 1: 0;
            }
        });
    }

    /**
     * Refreshes the recycler view with a new set of data from the database.
     */
    private void refreshRecycler(){
        taskDatabase = TaskDatabase.getInstance(this);
        tasks = taskDatabase.taskDao().getAll();
        sortTasks();
        adapter = new TaskListAdapter(tasks,this);
        adapter.notifyDataSetChanged();
    }

    /**
     * Deletes a task from the homepage.
     *
     * @param task The task to be deleted. Needs to be a copy of the one you want to delete in
     *             the database.
     */
    public void deleteTask(Task task){
        taskDatabase.taskDao().delete(task);
        refreshRecycler();
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
     */
    public void setAlarm(Context context, int day,int month, int year, int hour, int minute,int notificationID){
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
            alarmNames.add(alarmNumber);
            Log.d("setAlarm", "Success!");
        }

    }

    public class setRepeatingAlarm{
        public void setRepeatingAlarm(boolean[] dateArray,int hour,int minute,int taskID,String name){
            Calendar c=Calendar.getInstance();
            Calendar presDate=Calendar.getInstance();
            ComponentName cN=new ComponentName(gContext,AlarmsIntentService.class);
            List<Integer> ids=new ArrayList<>();

            if(dateArray[0]){
                Calendar randomizer=Calendar.getInstance();
                while(c.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
                    c.add(Calendar.DATE,1);
                }
                c.set(Calendar.HOUR,hour);
                c.set(Calendar.MINUTE,minute);
                long diff=c.getTimeInMillis()-presDate.getTimeInMillis();
                int id=randomizer.get(Calendar.MILLISECOND)+randomizer.get(Calendar.YEAR)+randomizer.get(Calendar.DAY_OF_MONTH);
                ids.add(id);
                PersistableBundle pB = createPersistableBundle(hour,minute,0,taskID,id,name);
                if(diff>=0){
                    JobInfo jobInfo=new JobInfo.Builder(id,cN).setMinimumLatency(diff)
                            .setExtras(pB)
                            .build();
                    jobScheduler.schedule(jobInfo);
                }
            }
            if(dateArray[1]){
                Calendar randomizer=Calendar.getInstance();
                while(c.get(Calendar.DAY_OF_WEEK)!=Calendar.MONDAY){
                    c.add(Calendar.DATE,1);
                }
                c.set(Calendar.HOUR,hour);
                c.set(Calendar.MINUTE,minute);
                long diff=c.getTimeInMillis()-presDate.getTimeInMillis();
                int id=randomizer.get(Calendar.MILLISECOND)+randomizer.get(Calendar.YEAR)+randomizer.get(Calendar.DAY_OF_MONTH);
                ids.add(id);
                PersistableBundle pB = createPersistableBundle(hour,minute,1,taskID,id,name);
                if(diff>=0){
                    JobInfo jobInfo=new JobInfo.Builder(id,cN).setMinimumLatency(diff)
                            .setExtras(pB)
                            .build();
                    jobScheduler.schedule(jobInfo);
                }
            }
            if(dateArray[2]){
                Calendar randomizer=Calendar.getInstance();
                while(c.get(Calendar.DAY_OF_WEEK)!=Calendar.TUESDAY){
                    c.add(Calendar.DATE,1);
                }
                c.set(Calendar.HOUR,hour);
                c.set(Calendar.MINUTE,minute);
                long diff=c.getTimeInMillis()-presDate.getTimeInMillis();
                int id=randomizer.get(Calendar.MILLISECOND)+randomizer.get(Calendar.YEAR)+randomizer.get(Calendar.DAY_OF_MONTH);
                ids.add(id);
                PersistableBundle pB = createPersistableBundle(hour,minute,2,taskID,id,name);
                if(diff>=0){
                    JobInfo jobInfo=new JobInfo.Builder(id,cN).setMinimumLatency(diff)
                            .setExtras(pB)
                            .build();
                    jobScheduler.schedule(jobInfo);
                }
            }
            if(dateArray[3]){
                Calendar randomizer=Calendar.getInstance();
                while(c.get(Calendar.DAY_OF_WEEK)!=Calendar.WEDNESDAY){
                    c.add(Calendar.DATE,1);
                }
                c.set(Calendar.HOUR,hour);
                c.set(Calendar.MINUTE,minute);
                long diff=c.getTimeInMillis()-presDate.getTimeInMillis();
                int id=randomizer.get(Calendar.MILLISECOND)+randomizer.get(Calendar.YEAR)+randomizer.get(Calendar.DAY_OF_MONTH);
                ids.add(id);
                PersistableBundle pB = createPersistableBundle(hour,minute,3,taskID,id,name);
                if(diff>=0){
                    JobInfo jobInfo=new JobInfo.Builder(id,cN).setMinimumLatency(diff)
                            .setExtras(pB)
                            .build();
                    jobScheduler.schedule(jobInfo);
                }
            }
            if(dateArray[4]){
                Calendar randomizer=Calendar.getInstance();
                while(c.get(Calendar.DAY_OF_WEEK)!=Calendar.THURSDAY){
                    c.add(Calendar.DATE,1);
                }
                c.set(Calendar.HOUR,hour);
                c.set(Calendar.MINUTE,minute);
                long diff=c.getTimeInMillis()-presDate.getTimeInMillis();
                int id=randomizer.get(Calendar.MILLISECOND)+randomizer.get(Calendar.YEAR)+randomizer.get(Calendar.DAY_OF_MONTH);
                ids.add(id);
                PersistableBundle pB = createPersistableBundle(hour,minute,4,taskID,id,name);
                if(diff>=0){
                    JobInfo jobInfo=new JobInfo.Builder(id,cN).setMinimumLatency(diff)
                            .setExtras(pB)
                            .build();
                    jobScheduler.schedule(jobInfo);
                }
            }
            if(dateArray[5]){
                Calendar randomizer=Calendar.getInstance();
                while(c.get(Calendar.DAY_OF_WEEK)!=Calendar.FRIDAY){
                    c.add(Calendar.DATE,1);
                }
                c.set(Calendar.HOUR,hour);
                c.set(Calendar.MINUTE,minute);
                long diff=c.getTimeInMillis()-presDate.getTimeInMillis();
                int id=randomizer.get(Calendar.MILLISECOND)+randomizer.get(Calendar.YEAR)+randomizer.get(Calendar.DAY_OF_MONTH);
                ids.add(id);
                PersistableBundle pB = createPersistableBundle(hour,minute,5,taskID,id,name);
                if(diff>=0){
                    JobInfo jobInfo=new JobInfo.Builder(id,cN).setMinimumLatency(diff)
                            .setExtras(pB)
                            .build();
                    jobScheduler.schedule(jobInfo);
                }
            }
            if(dateArray[6]){
                Calendar randomizer=Calendar.getInstance();
                while(c.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY){
                    c.add(Calendar.DATE,1);
                }
                c.set(Calendar.HOUR,hour);
                c.set(Calendar.MINUTE,minute);
                long diff=c.getTimeInMillis()-presDate.getTimeInMillis();
                int id=randomizer.get(Calendar.MILLISECOND)+randomizer.get(Calendar.YEAR)+randomizer.get(Calendar.DAY_OF_MONTH);
                ids.add(id);
                PersistableBundle pB = createPersistableBundle(hour,minute,6,taskID,id,name);
                if(diff>=0){
                    JobInfo jobInfo=new JobInfo.Builder(id,cN).setMinimumLatency(diff)
                            .setExtras(pB)
                            .build();
                    jobScheduler.schedule(jobInfo);
                }
            }
            AlarmDicts.put(taskID,ids);
        }
    }

    public PersistableBundle createPersistableBundle(Integer hour,Integer minute, Integer day,Integer taskID,Integer id,String name){
        PersistableBundle pB=new PersistableBundle();
        pB.putBoolean("ISREPEAT",true);
        pB.putInt("HOUR",hour);
        pB.putInt("MINUTE",minute);
        pB.putInt("DAY",day);
        pB.putInt("TASKID",taskID);
        pB.putInt("ID",id);
        pB.putString("NAME",name);
        return pB;
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
        TaskDatabase taskDatabase = TaskDatabase.getInstance(this);
        List<Task> tasks = taskDatabase.taskDao().getAll();
        int scheduleDay=0;
        int scheduleMonth=0;
        int scheduleYear=0;
        int scheduleHour=0;
        int scheduleMinute=0;
        for(int i = 0; i<taskDatabase.taskDao().getRowCount(); i++){
            Task task =tasks.get(i);
            if(task.getTypeID()==0){
                if(task.getNotified()==0){
                    String daysOfWeekString=task.getAlertList();
                    String alarmTime=task.getDueDate();
                    if(daysOfWeekString==null||daysOfWeekString.length()<1){

                    }else{
                        boolean[] bools=new boolean[7];
                        List<String> daysSeparate=new ArrayList<>(Arrays.asList(daysOfWeekString.split(";")));
                        int hour;
                        int minute;
                        for(int II=0;II<daysSeparate.size();II++){
                            String tester=daysSeparate.get(II);
                            int testnum=Integer.parseInt(tester);
                            if(testnum==1){
                                bools[II]=true;
                            }else{
                                bools[II]=false;
                            }
                        }
                        List<String> alarmTimes =new ArrayList<>(Arrays.asList(alarmTime.split(":")));
                        hour=Integer.parseInt(alarmTimes.get(0));
                        minute=Integer.parseInt(alarmTimes.get(1));
                        setRepeatingAlarm s = new setRepeatingAlarm();
                        s.setRepeatingAlarm(bools,hour,minute,task.getId(),task.getNameID());
                    }
                }else{
                    //Fill in checker for date here as opposed to day listed in getNotified
                }
            }
            else {
                if (task.getNotified() == 0) {
                    String AlertList = task.getAlertList();
                    if (AlertList == null || AlertList.length() < 1) {
                        //Oops
                    } else {
                        List<String> dates = new ArrayList<>(Arrays.asList(AlertList.split(":")));
                        for (int iI = 0; iI < dates.size(); iI++) {
                            List<String> alarmseparate = new ArrayList<>(Arrays.asList(dates.get(iI).split("/")));
                            for (int II = 0; II < alarmseparate.size(); II++) {
                                if (II == 0) {
                                    scheduleHour = Integer.parseInt(alarmseparate.get(II));
                                } else if (II == 1) {
                                    scheduleMinute = Integer.parseInt(alarmseparate.get(II));
                                } else if (II == 2) {
                                    scheduleYear = Integer.parseInt(alarmseparate.get(II));
                                } else if (II == 3) {
                                    scheduleMonth = Integer.parseInt(alarmseparate.get(II));
                                } else if (II == 4) {
                                    scheduleDay = Integer.parseInt(alarmseparate.get(II));
                                }
                            }
                            int LastNotificationID = toIntExact(SystemClock.uptimeMillis() / 1000);
                            setAlarm(this, scheduleDay, scheduleMonth, scheduleYear, scheduleHour, scheduleMinute, LastNotificationID);
                            scheduleDay = 0;
                            scheduleMonth = 0;
                            scheduleYear = 0;
                            scheduleHour = 0;
                            scheduleMinute = 0;
                            alarmseparate.clear();
                        }
                        dates.clear();
                    }
                } else {

                }
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
}
