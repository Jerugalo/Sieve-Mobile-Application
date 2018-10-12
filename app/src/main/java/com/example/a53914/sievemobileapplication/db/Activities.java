package com.example.a53914.sievemobileapplication.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Activities {
    @PrimaryKey
    public float Priority;

    @ColumnInfo(name = "name")
    public String Name;

    @ColumnInfo(name = "class")
    public String Class;//Class as in school, not programming

    @ColumnInfo(name = "date")
    public String DueDate;

    @ColumnInfo(name="timeEst")
    public float TimeEst;

    @ColumnInfo(name="notes")
    public String Notes;

    // Getters and setters are ignored for brevity,
    // but they're required for Room to work.
}