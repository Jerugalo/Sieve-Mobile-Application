package com.example.a53914.sievemobileapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {

    public class SharedPreferencesManager {
        private SharedPreferences themeStorage;
        private Context context;
        SharedPreferencesManager(Context context){
            this.context = context;
            themeStorage = PreferenceManager.getDefaultSharedPreferences(context);
        }
        int retrieveInt(String tag, int defValue){
            return themeStorage.getInt(tag, defValue);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * The themeId int pulls a "themeId" int from the SPM.
         * The if/else block calls setTheme based on the value of "themeId".
         */
        int themeId = new SharedPreferencesManager(this).retrieveInt("themeId",1);
        if(themeId == 1){
            setTheme(R.style.SieveDefault);
        }else{
            if(themeId == 2){
                setTheme(R.style.SieveAlternative);
            }else{
                if(themeId == 3){
                    setTheme(R.style.SieveCombined);
                }else{
                    if(themeId == 4){
                        setTheme(R.style.SieveDark);
                    }else{
                        if(themeId == 5){
                            setTheme(R.style.SievePastel);
                        }else{
                            setTheme(R.style.SieveCandy);
                        }
                    }
                }
            }
        }
        setContentView(R.layout.activity_home_page);
    }

    //Function called when user opens Settings
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