package org.stemacademy.akmeier.sievemobileapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.stemacademy.akmeier.sievemobileapplication.db.Task;

public class AssignmentStart extends AppCompatActivity {
    public class SharedPreferencesManager{
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
    GlobalVars global = GlobalVars.getInstance();
    Task mTask = global.getCurrentTask();
    TextView taskTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        determineTheme();
        setContentView(R.layout.activity_assignment_start);
        taskTitle = (TextView)findViewById(R.id.TaskNameTitle);
        taskTitle.setText(mTask.getNameID());
    }
    protected void onStart(){
        super.onStart();
        determineTheme();
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
    public void determineTheme(){
        int themeId = new SharedPreferencesManager(this).retrieveInt("themeId",1);
        if(themeId == 1){setTheme(R.style.SieveDefault);}
        else if(themeId == 2){setTheme(R.style.SieveAlternative);}
        else if(themeId == 3){setTheme(R.style.SieveTwilight);}
        else if(themeId == 4){setTheme(R.style.SieveDark);}
        else if(themeId == 5){setTheme(R.style.SieveSimple);}
        else if(themeId == 6){setTheme(R.style.SieveOlive);}
        else{setTheme(R.style.SieveDefault);}
    }
}
