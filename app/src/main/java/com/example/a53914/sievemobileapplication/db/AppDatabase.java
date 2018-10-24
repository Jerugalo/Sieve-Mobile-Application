package com.example.a53914.sievemobileapplication.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;



@Database(entities = {Task.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao getTaskDao();

    private static AppDatabase taskDB;

    public static AppDatabase getInstance(Context context){
        if (null == taskDB){
            taskDB = buildDatabaseInstance(context);
        }
        return taskDB;
    }

    private static AppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                "mainTasks.db")
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        taskDB = null;
    }
}