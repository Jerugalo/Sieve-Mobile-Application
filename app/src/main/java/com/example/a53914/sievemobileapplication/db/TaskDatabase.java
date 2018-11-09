package com.example.a53914.sievemobileapplication.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

/**
 * Fetches the database.
 */

@Database(entities = {Task.class}, version = 2)
public abstract class TaskDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    private static TaskDatabase taskDB;

    /**
     * Returns the database by building it from TaskDatabase and running it through the database
     * migrations.
     */
    public static TaskDatabase getInstance(Context context){

        if (taskDB==null){
           taskDB = Room.databaseBuilder(context, TaskDatabase.class, "TaskDatabase")
                   .addMigrations(MIGRATION_1_2).allowMainThreadQueries().build();
       }
       return taskDB;
    }

    //** Migration for the first version of the code */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("");
        }
    };

    /** Wipes the database */
    public void cleanUp() {
        taskDB = null;
    }
}