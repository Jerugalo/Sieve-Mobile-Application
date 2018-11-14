package com.example.a53914.sievemobileapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.a53914.sievemobileapplication.db.Task;

public class AssignmentStart extends AppCompatActivity {

    GlobalVars global = GlobalVars.getInstance();
    Task mTask = global.getCurrentTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_start);
    }
}
