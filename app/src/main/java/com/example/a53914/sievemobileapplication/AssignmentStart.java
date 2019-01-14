package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.a53914.sievemobileapplication.db.Task;

public class AssignmentStart extends AppCompatActivity {

    GlobalVars global = GlobalVars.getInstance();
    Task mTask = global.getCurrentTask();
    TextView taskTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_assignment_start);
        taskTitle = (TextView)findViewById(R.id.TaskNameTitle);
        taskTitle.setText(mTask.getNameID());
    }

    public void toDetails(View view){
        Intent toDetails = new Intent(this, AssignmentDetails.class);
        startActivity(toDetails);
    }

    public void toHomePage(View view){
        Intent toHomePage = new Intent(this, HomePage.class);
        startActivity(toHomePage);
    }

    public void completeTask(View view){
        Intent toHomePage = new Intent(this, HomePage.class);
        toHomePage.putExtra("delete", true);
        startActivity(toHomePage);
    }
}
