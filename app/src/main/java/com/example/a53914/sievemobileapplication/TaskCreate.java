package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TaskCreate extends AppCompatActivity {
    //the toHomePage intent is used in two functions, hence it is here.
    Intent toHomePage = new Intent(this, HomePage.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

    }
    //Returning Home
    public void toHomePage(View view){
        startActivity(toHomePage);
    }

    //For when the user wants to create that task
    public void taskCreated(View view){
        //Keenan and Ben, put whatever code you need in here.
        startActivity(toHomePage);
    }
}
