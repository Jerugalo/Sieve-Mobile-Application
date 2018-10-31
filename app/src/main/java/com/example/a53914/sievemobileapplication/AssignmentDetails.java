package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AssignmentDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_details);
    }
    public void toHomePage(View view){
        Intent toHomePage = new Intent(this, HomePage.class);
        startActivity(toHomePage);
    }
    public void editTask(View view){
        Intent editTask = new Intent (this, TaskCreate.class);
        startActivity(editTask);
    }
    public void beginTask(View view){
        Intent beginTask = new Intent(this, AssignmentStart.class);
        startActivity(beginTask);
    }
}
