package com.example.a53914.sievemobileapplication.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Fetches the database.
 */

@Database(entities = {Class.class}, version = 1)
public abstract class ClassDatabase extends RoomDatabase {

    public abstract ClassDao classDao();

    private static ClassDatabase classDB;

    /**
     * Returns the database by building it from ClassDatabase and running it through the database
     * migrations.
     */

    public static ClassDatabase getInstance(Context context){

        if (classDB==null){
            classDB = Room.databaseBuilder(context, ClassDatabase.class, "ClassDatabase")
                    .addMigrations().allowMainThreadQueries().build();
        }
        return classDB;
    }

    /** Wipes the database */
    public void cleanUp() {
        classDB = null;
    }
}
