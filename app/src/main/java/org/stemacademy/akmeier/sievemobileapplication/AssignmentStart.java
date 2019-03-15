package org.stemacademy.akmeier.sievemobileapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.os.Vibrator;

import org.stemacademy.akmeier.sievemobileapplication.db.Task;

import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

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
    TextView timerText;
    Button contWork;
    Vibrator v;
    GifImageView gIF;
    GifDrawable gD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        determineTheme();
        setContentView(R.layout.activity_assignment_start);
        taskTitle = (TextView)findViewById(R.id.TaskNameTitle);
        taskTitle.setText(mTask.getNameID());
        timerText = findViewById(R.id.TimerCountdownText);
        timeUntilBreak.start();
        contWork = findViewById(R.id.contWorkButton);
        contWork.setVisibility(View.INVISIBLE);
        v = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        timeUntilBreak.cancel();
        Intent toHomePage = new Intent(this, HomePage.class);
        startActivity(toHomePage);
    }

    public void completeTask(View view){
        timeUntilBreak.cancel();
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
    CountDownTimer timeUntilBreak = new CountDownTimer(900000, 1000){
        public void onTick(long millisUntilFinished) {
            long minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
            String minuteS =minute+"";
            long second =TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
            String secondS=second+"";
            if(second<10){
                secondS="0"+second;
            }
            timerText.setText(""+String.format(minuteS+":"+secondS));
        }
        public void onFinish() {
            timerText.setText("Break Time!");
            contWork.setVisibility(View.VISIBLE);
            v.vibrate(500);
            pausePlayAnimation(true);
        }
    }.start();

    public void continueWork(View view){
        timeUntilBreak.start();
        contWork.setVisibility(View.INVISIBLE);
        pausePlayAnimation(false);
    }

    public void pausePlayAnimation(boolean pause){
        gIF=(GifImageView)findViewById(R.id.gifImageView);
        gD=(GifDrawable)gIF.getDrawable();
        if(pause){
            gD.stop();
        }else{
            gD.start();
        }
    }
}