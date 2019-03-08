package org.stemacademy.akmeier.sievemobileapplication;

import android.app.Application;

import org.stemacademy.akmeier.sievemobileapplication.db.Task;

import java.util.ArrayList;
import java.util.List;

public class GlobalVars extends Application {

    private static GlobalVars instance = new GlobalVars();

    // Getter-Setters
    public static GlobalVars getInstance() {
        return instance;
    }
    public static void setInstance(GlobalVars instance) {
        GlobalVars.instance = instance;
    }

    private static Task currentTask;

    private static ArrayList<String> gAlarms;

    private static int gDivPos;

    public GlobalVars() {

    }

    public static Task getCurrentTask() {
        return currentTask;
    }
    public static void setCurrentTask(Task currentTask) {
        GlobalVars.currentTask = currentTask;
    }

    public static ArrayList<String> getgAlarms(){
        return gAlarms;
    }
    public static void setgAlarms(ArrayList<String> gAlarms1){
        GlobalVars.gAlarms=gAlarms1;
    }

    public static int getgDivPos(){return gDivPos;}
    public static void setgDivPos(int gDivPos1){GlobalVars.gDivPos=gDivPos1;}
}
