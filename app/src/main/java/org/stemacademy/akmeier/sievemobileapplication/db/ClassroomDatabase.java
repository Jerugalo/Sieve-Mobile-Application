package org.stemacademy.akmeier.sievemobileapplication.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Fetches the database.
 */

@Database(entities = {Classroom.class}, version = 1)
public abstract class ClassroomDatabase extends RoomDatabase {

    public abstract ClassroomDao classroomDao();

    private static ClassroomDatabase classroomDB;

    /**
     * Returns the database by building it from ClassroomDatabase and running it through the database
     * migrations.
     */

    public static ClassroomDatabase getInstance(Context context){

        if (classroomDB ==null){
            classroomDB = Room.databaseBuilder(context, ClassroomDatabase.class, "ClassroomDatabase")
                    .addMigrations().allowMainThreadQueries().build();
        }
        return classroomDB;
    }

    /** Wipes the database */
    public void cleanUp() {
        classroomDB = null;
    }
}
