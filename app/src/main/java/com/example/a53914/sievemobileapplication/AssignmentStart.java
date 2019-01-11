package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.a53914.sievemobileapplication.db.Task;

public class AssignmentStart extends AppCompatActivity {
	
    GlobalVars global = GlobalVars.getInstance();
    Task mTask = global.getCurrentTask();
    TextView taskTitle;

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
                            setTheme(R.style.SieveSimple);
                        }else{
                            setTheme(R.style.SieveCandy);
                        }
                    }
                }
            }
        }
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
