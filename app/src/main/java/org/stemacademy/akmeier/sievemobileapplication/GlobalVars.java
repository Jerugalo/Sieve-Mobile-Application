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

    private static List<Task> taskData;
    private static Task currentTask;

    private static ArrayList<String> gAlarms;

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
}
