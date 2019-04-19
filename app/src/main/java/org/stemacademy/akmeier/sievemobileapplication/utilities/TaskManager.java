package org.stemacademy.akmeier.sievemobileapplication.utilities;


import org.stemacademy.akmeier.sievemobileapplication.db.Task;

import java.util.List;

public class TaskManager {

    private static TaskManager instance = new TaskManager();

    private TaskManager(){

    }

    public static List<Task> getSortedList(List<Task> tasks){
        return tasks;
    }

    public static List<Task> getProjectList(List<Task> tasks){
        tasks = getSortedList(tasks);
        return tasks;
    }

    public static List<Task> getProjectChildrenList(Task task){
        //tasks = getSortedList(tasks);
        return null;
    }
}
