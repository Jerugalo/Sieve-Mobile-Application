package com.example.a53914.sievemobileapplication.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Defines various ways to interact with the database.
 */

@Dao
public interface ClassDao {
    @Query("SELECT * FROM Class")
    List<Class> getAll();

    @Insert
    void insertAll(Class cls);

    @Delete
    void delete(Class cls);

    @Update
    void update(Class cls);

    @Query("DELETE  FROM Class")
    void deleteAll();
}
