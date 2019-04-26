package org.stemacademy.akmeier.sievemobileapplication;

import android.app.Application;

import org.stemacademy.akmeier.sievemobileapplication.db.Task;

import java.util.ArrayList;
import java.util.Dictionary;
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


    private static List<Task> taskData;
    private static Task currentTask;
    private static ArrayList<String> gAlarms;

    private static int gDivPos;
    private static Dictionary<Integer, List<Integer>> gAlarmDict;

    public GlobalVars() {

    }

    public static List<Task> getTaskData() {
        return taskData;
    }
    public static void setTaskData(List<Task> taskData) {
        GlobalVars.taskData = taskData;
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

    public static Dictionary<Integer,List<Integer>> getgAlarmDict(){return gAlarmDict;}
    public static void setgAlarmDict(Dictionary<Integer,List<Integer>> gAlarmDict1){GlobalVars.gAlarmDict=gAlarmDict1;}
}
