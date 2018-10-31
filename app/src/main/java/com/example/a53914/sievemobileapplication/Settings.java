package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_LIGHT_THEME = "dark_theme";

    private void toggleTheme(boolean SieveAlternative) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_LIGHT_THEME, SieveAlternative);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_LIGHT_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.SieveAlternative);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        Switch toggle = (Switch) findViewById(R.id.switch1);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });
    }
    //Returning Home
    public void toHomePage(View view){
        Intent toHomePage = new Intent(this, HomePage.class);
        startActivity(toHomePage);
    }
}
