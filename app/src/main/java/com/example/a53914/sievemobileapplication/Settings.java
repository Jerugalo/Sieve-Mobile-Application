package com.example.a53914.sievemobileapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {
    /*
     * This public class defines a SharedPreferencesManager.
     * The retrieveInt definition allows us to access the themeId rom SPM.
     * The storeInt method allows us to write to the SharedPreferencesManager.
     */
    public class SharedPreferencesManager {
        private SharedPreferences themeStorage;
        private SharedPreferences.Editor sEditor;
        private Context context;
        SharedPreferencesManager(Context context){
            this.context = context;
            themeStorage = PreferenceManager.getDefaultSharedPreferences(context);
        }
        private SharedPreferences.Editor getEditor(){
            return themeStorage.edit();
        }
        int retrieveInt(String tag, int defValue){
            return themeStorage.getInt(tag, defValue);
        }
        void storeInt(String tag, int defValue){
            sEditor = getEditor();
            sEditor.putInt(tag, defValue);
            sEditor.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        determineTheme();
        setContentView(R.layout.activity_settings);
    }

    public void onThemeRadio(View view){
        int themeId;
        RadioButton T1Rd = findViewById(R.id.themeDefault);
        RadioButton T2Rd = findViewById(R.id.themeAlternative);
        RadioButton T3Rd = findViewById(R.id.themeCombined);
        RadioButton T4Rd = findViewById(R.id.themeDark);
        RadioButton T5Rd = findViewById(R.id.themeSimple);
        RadioButton T6Rd = findViewById(R.id.themeCandy);
        if(T1Rd.isChecked()){themeId = 1;}
        else if(T2Rd.isChecked()){themeId = 2;}
        else if(T3Rd.isChecked()){themeId = 3;}
        else if(T4Rd.isChecked()){themeId = 4;}
        else if(T5Rd.isChecked()){themeId = 5;}
        else if(T6Rd.isChecked()){themeId = 6;}
        else{themeId = 1;}
        new SharedPreferencesManager(getApplicationContext()).storeInt("themeId",themeId);
        recreate();
    }
    public void determineTheme(){
        int themeId = new SharedPreferencesManager(this).retrieveInt("themeId",1);
        if(themeId == 1){setTheme(R.style.SieveDefault);}
        else if(themeId == 2){setTheme(R.style.SieveAlternative);}
        else if(themeId == 3){setTheme(R.style.SieveCombined);}
        else if(themeId == 4){setTheme(R.style.SieveDark);}
        else if(themeId == 5){setTheme(R.style.SieveSimple);}
        else if(themeId == 6){setTheme(R.style.SieveCandy);}
        else{setTheme(R.style.SieveDefault);}
    }
    //Returning Home
    public void toHomePage(View view){
        Intent toHomePage = new Intent(this, HomePage.class);
        startActivity(toHomePage);
    }
}
