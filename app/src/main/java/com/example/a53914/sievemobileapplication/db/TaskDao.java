package com.example.a53914.sievemobileapplication.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM Task")
    List<Task> getAll();

    //@Query("SELECT * FROM activities WHERE uid IN (:userIds)")
    //List<Task> loadAllByIds(int[] userIds);

    //@Query("SELECT * FROM activities WHERE first_name LIKE :first AND "
    //        + "last_name LIKE :last LIMIT 1")
    //Task findByName(String first, String last);

    @Insert
    void insertAll(Task task);

// --Commented out by Inspection START (10/31/2018 2:03 PM):
//    @Delete
//    void delete(Task user);
// --Commented out by Inspection STOP (10/31/2018 2:03 PM)

}