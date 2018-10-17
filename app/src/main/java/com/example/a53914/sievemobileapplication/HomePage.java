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
    //The function below is called when user clicks on "+" key on the homepage.
    public void toAssignmentCreation(View view){
        Intent toAssignmentCreation = new Intent(this, TaskCreate.class);
        startActivity(toAssignmentCreation);
    }
}
