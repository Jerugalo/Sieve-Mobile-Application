package com.example.a53914.sievemobileapplication;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.a53914.sievemobileapplication.db.AppDatabase;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
        AppDatabase.class, "Activities").build();

        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.TaskList);

        // Initialize contacts
        tasks = Task.createTaskList(20);
        // Create adapter passing in the sample user data
        TaskListAdapter adapter = new TaskListAdapter(tasks);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }

}
