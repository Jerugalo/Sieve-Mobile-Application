package org.stemacademy.akmeier.sievemobileapplication.db;

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
public interface ClassroomDao {
    @Query("SELECT * FROM Classroom")
    List<Classroom> getAll();

    @Insert
    void insertAll(Classroom classroom);

    @Delete
    void delete(Classroom classroom);

    @Update
    void update(Classroom classroom);

    @Query("DELETE  FROM Classroom")
    void deleteAll();
}
