package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.a53914.sievemobileapplication.db.AppDatabase;
import com.example.a53914.sievemobileapplication.db.Task;

import java.util.List;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Setup the database
        AppDatabase appDatabase = AppDatabase.getInstance(HomePage.this);

        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = findViewById(R.id.TaskList);

        // Initialize contacts
        List<Task> tasks = appDatabase.getTaskDao().getAll();
        // Create adapter passing in the sample user data
        TaskListAdapter adapter = new TaskListAdapter(tasks);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }

    //Function called when user opens Settings//
    public void toSettingsMenu(android.view.View view) {
        Intent toSettingsMenu = new Intent(this, Settings.class);
        startActivity(toSettingsMenu);
    }

    //The function below is called when user clicks on "+" key on the homepage.
    public void toAssignmentCreation(View view) {
        Intent toAssignmentCreation = new Intent(this, TaskCreate.class);
        startActivity(toAssignmentCreation);
    }

}
