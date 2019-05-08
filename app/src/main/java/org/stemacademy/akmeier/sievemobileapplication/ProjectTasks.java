package org.stemacademy.akmeier.sievemobileapplication;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.stemacademy.akmeier.sievemobileapplication.db.Task;
import org.stemacademy.akmeier.sievemobileapplication.db.TaskDatabase;
import org.stemacademy.akmeier.sievemobileapplication.utilities.SwipeController;
import org.stemacademy.akmeier.sievemobileapplication.utilities.SwipeControllerActions;
import org.stemacademy.akmeier.sievemobileapplication.utilities.TaskListManager;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Default activity for app. Displays a list of events created by user, links to settings menu
 * and task creation menu. Events display name of event, color coded priority, and provide link to
 * further information on the task.
 */

public class ProjectTasks extends AppCompatActivity {
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
    TextView dateText;
    static TaskDatabase taskDatabase;
    static TaskListAdapter adapter;
    static List<Task> tasks;
    RecyclerView rvTasks;


    /**
     * Creates Activity and sets up the recycler view. Recycler view pulls a list of Task objects
     * from the TaskDao and displays them in a visual list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        super.onCreate(savedInstanceState);
        context = this;
        Fabric.with(this, new Crashlytics());
        determineTheme();
        setContentView(R.layout.activity_project_tasks);
        TextView textView = findViewById(R.id.projectTitle);
        textView.setText(GlobalVars.getCurrentTask().getNameID());
    }

    @Override
    protected void onStart() {
        super.onStart();
        determineTheme();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.PARENT="ProjectTasks";
        adapter = new TaskListAdapter(this);
        taskDatabase = TaskDatabase.getInstance(this);
        tasks = taskDatabase.taskDao().getAll();
        adapter.updateItems(tasks);
        tasks=TaskListManager.getProjectChildrenList(mTask,tasks);
        rvTasks = findViewById(R.id.TaskListPT);
        rvTasks.setAdapter(adapter);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter.updateItems(tasks);
        adapter.notifyDataSetChanged();

        SwipeController swipeController;
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                deleteTask(adapter.items.get(position));
            }

            public void onLeftClicked(int position) {
                ToDetails(adapter.items.get(position));
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rvTasks);

    }
    public void deleteTask(Task task){
        if (task.getTypeID() != -1) {
            tasks.remove(task);
            taskDatabase.taskDao().delete(task);
            TaskListManager.setTasks(tasks);
            adapter.updateItems(tasks);
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
            AssignmentDetails.PREVIOUSACTIVITY="ProjectTasks";
            global.setCurrentTask(task);
            startActivity(toDetails);
        }
    }

    /**
     * Opens Settings activity
     */
    public void toHomePage(View view) {
        Intent toHomePage = new Intent(this, HomePage.class);
        startActivity(toHomePage);
    }

    /**
     * Opens TaskCreate activity
     */
    public void toTaskCreate(View view) {
        Intent toTaskCreate = new Intent(this, AssignmentDetails.class);
        startActivity(toTaskCreate);
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

    public static Context getContext(){
        return context;
    }
}
