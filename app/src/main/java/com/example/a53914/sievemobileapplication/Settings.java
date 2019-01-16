package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class Settings extends AppCompatActivity {

    /** Creates instance of Settings activity. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    /** Opens HomePage activity */
    public void toHomePage(View view) {
        Intent toHomePage = new Intent(this, HomePage.class);
        startActivity(toHomePage);
    }

    public void onThemeRadio(View view){

    }
}
