package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }
    //Function called when user opens Settings//
    public void toSettingsMenu(android.view.View view){
        Intent toSettingsMenu = new Intent(this, Settings.class);
        startActivity(toSettingsMenu);
    }
    //The function below is called when user clicks on "+" key on the homepage.
    public void toAssignmentCreation(View view){
        Intent toAssignmentCreation = new Intent(this, TaskCreate.class);
        startActivity(toAssignmentCreation);
    }
    //Function to send user to Assignment Details
    public void toAssignmentDetails(View view){
        Intent toAssignmentDetails = new Intent(this, AssignmentDetails.class);
        startActivity(toAssignmentDetails);
    }

}
