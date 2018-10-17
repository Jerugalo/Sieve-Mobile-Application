package com.example.a53914.sievemobileapplication.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ActivitiesDao {
    /*@Query("SELECT * FROM activities")
    List<Activities> getAll();

    //@Query("SELECT * FROM activities WHERE uid IN (:userIds)")
    //List<Activities> loadAllByIds(int[] userIds);

    //@Query("SELECT * FROM activities WHERE first_name LIKE :first AND "
    //        + "last_name LIKE :last LIMIT 1")
    //Activities findByName(String first, String last);

    @Insert
    void insertAll(Activities activities);

    @Delete
    void delete(Activities user);
    */
}