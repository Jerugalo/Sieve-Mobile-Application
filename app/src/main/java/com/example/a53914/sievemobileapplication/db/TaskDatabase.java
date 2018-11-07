package com.example.a53914.sievemobileapplication.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = {Task.class}, version = 2)
public abstract class TaskDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    private static TaskDatabase taskDB;

    public static TaskDatabase getInstance(Context context){

        if (taskDB==null){
           taskDB = Room.databaseBuilder(context, TaskDatabase.class, "TaskDatabase")
                   .addMigrations(MIGRATION_1_2).allowMainThreadQueries().build();
       }
       return taskDB;
    }

    private static TaskDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(
                context,
                TaskDatabase.class,
                "mainTasks.db")
                .allowMainThreadQueries().build();
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("");
        }
    };

// --Commented out by Inspection START (10/31/2018 2:03 PM):
//    public void cleanUp() {
//        taskDB = null;
//    }
// --Commented out by Inspection STOP (10/31/2018 2:03 PM)
}