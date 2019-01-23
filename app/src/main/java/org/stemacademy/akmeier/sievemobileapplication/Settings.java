package org.stemacademy.akmeier.sievemobileapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.a53914.sievemobileapplication.R;

public class Settings extends AppCompatActivity {
    RadioButton T1Rd ;
    RadioButton T2Rd;
    RadioButton T3Rd;
    RadioButton T4Rd;
    RadioButton T5Rd;
    RadioButton T6Rd;
    int themeId;
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
        T1Rd = findViewById(R.id.themeDefault);
        T2Rd = findViewById(R.id.themeAlternative);
        T3Rd = findViewById(R.id.themeCombined);
        T4Rd = findViewById(R.id.themeDark);
        T5Rd = findViewById(R.id.themeSimple);
        T6Rd = findViewById(R.id.themeCandy);
    }

    public void onThemeRadio(){
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
    public void T1RDClicked(View view){
        themeId = 1;
        //Manual RadioButton setup, b/c android studio only works with radio groups when RadioButtons are under one Radiogroup and nothing else
        T1Rd.setChecked(true);
        T2Rd.setChecked(false);
        T3Rd.setChecked(false);
        T4Rd.setChecked(false);
        T5Rd.setChecked(false);
        T6Rd.setChecked(false);
        onThemeRadio();
    }
    public void T2RDClicked(View view){
        themeId = 2;
        T1Rd.setChecked(false);
        T2Rd.setChecked(true);
        T3Rd.setChecked(false);
        T4Rd.setChecked(false);
        T5Rd.setChecked(false);
        T6Rd.setChecked(false);
        onThemeRadio();
    }
    public void T3RDClicked(View view){
        themeId = 3;
        T1Rd.setChecked(false);
        T2Rd.setChecked(false);
        T3Rd.setChecked(true);
        T4Rd.setChecked(false);
        T5Rd.setChecked(false);
        T6Rd.setChecked(false);
        onThemeRadio();
    }
    public void T4RDClicked(View view){
        themeId = 4;
        T1Rd.setChecked(false);
        T2Rd.setChecked(false);
        T3Rd.setChecked(false);
        T4Rd.setChecked(true);
        T5Rd.setChecked(false);
        T6Rd.setChecked(false);
        onThemeRadio();
    }
    public void T5RDClicked(View view){
        themeId = 5;
        T1Rd.setChecked(false);
        T2Rd.setChecked(false);
        T3Rd.setChecked(false);
        T4Rd.setChecked(false);
        T5Rd.setChecked(true);
        T6Rd.setChecked(false);
        onThemeRadio();
    }
    public void T6RDClicked(View view){
        themeId = 6;
        T1Rd.setChecked(false);
        T2Rd.setChecked(false);
        T3Rd.setChecked(false);
        T4Rd.setChecked(false);
        T5Rd.setChecked(false);
        T6Rd.setChecked(true);
        onThemeRadio();
    }

}
