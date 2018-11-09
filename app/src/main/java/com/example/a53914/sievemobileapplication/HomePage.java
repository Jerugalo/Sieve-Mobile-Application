package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.a53914.sievemobileapplication.db.TaskDatabase;
import com.example.a53914.sievemobileapplication.db.Task;

import java.util.List;

/**
 * Default activity for app. Displays a list of events created by user, links to settings menu
 * and task creation menu. Events display name of event, color coded priority, and provide link to
 * further information on the task.
 */

public class HomePage extends AppCompatActivity {

    GlobalVars global = GlobalVars.getInstance();

    /**
     * Creates Activity and sets up the recycler view. Recycler view pulls a list of Task objects
     * from the TaskDao and displays them in a visual list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TaskDatabase taskDatabase = TaskDatabase.getInstance(HomePage.this);
        global.setTaskData(taskDatabase.taskDao().getAll());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        RecyclerView rvTasks = findViewById(R.id.TaskList);
        TaskListAdapter adapter = new TaskListAdapter(global.getTaskData());
        rvTasks.setAdapter(adapter);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
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

}
