package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_home_page);
        boolean useDark = getIntent().getBooleanExtra("usedark",true);
        if(useDark){
            setTheme(R.style.SieveAlternative);
        }else{
            setTheme(R.style.SieveDefault);
        }
        super.onCreate(savedInstanceState);
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
}