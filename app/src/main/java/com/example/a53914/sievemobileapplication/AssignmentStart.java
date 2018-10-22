package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AssignmentStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_start);
    }

    public void taskCancel(View view){
        Intent taskCancel = new Intent(this, HomePage.class);
        startActivity(taskCancel);
    }
    public void taskComplete(View view){
        Intent taskComplete = new Intent(this, HomePage.class);
        startActivity(taskComplete);
    }
}
