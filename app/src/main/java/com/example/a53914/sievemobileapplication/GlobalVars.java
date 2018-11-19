package com.example.a53914.sievemobileapplication;

import android.app.Application;

import com.example.a53914.sievemobileapplication.db.Task;
import com.example.a53914.sievemobileapplication.db.TaskDatabase;

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
}
