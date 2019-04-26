package org.stemacademy.akmeier.sievemobileapplication.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

/**
 * Fetches the database.
 */

@Database(entities = {Task.class}, version = 6)
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
                   .addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4,MIGRATION_4_5,MIGRATION_5_6).allowMainThreadQueries().build();
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
    static final Migration MIGRATION_2_3 = new Migration(2,3){
        @Override
        public void migrate(SupportSQLiteDatabase database){
            database.execSQL("ALTER TABLE Task "+" ADD COLUMN date TEXT");
        }
    };
    static final Migration MIGRATION_3_4 =new Migration(3,4){
        @Override
        public void migrate(SupportSQLiteDatabase database){database.execSQL("ALTER TABLE Task ADD" + " COLUMN IsNotified INTEGER NOT NULL default 0");}
    };
    static Migration MIGRATION_4_5=new Migration(4,5){
        @Override
        public void migrate(SupportSQLiteDatabase database){database.execSQL("ALTER TABLE Task ADD COLUMN alertList TEXT");}
    };
    static Migration MIGRATION_5_6=new Migration(5,6){
        @Override
        public void migrate(SupportSQLiteDatabase database){
            database.execSQL("ALTER TABLE Task ADD COLUMN parentProject TEXT")
            ;}
    };

    /** Wipes the database */
    public void cleanUp() {
        taskDB = null;
    }
}