package com.example.a53914.sievemobileapplication.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Activities.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ActivitiesDao activitiesDao();
}