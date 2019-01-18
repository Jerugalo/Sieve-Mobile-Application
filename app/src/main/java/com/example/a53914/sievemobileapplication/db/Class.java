package com.example.a53914.sievemobileapplication.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Class {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    private int type;

    @ColumnInfo(name = "due date")
    private String dueDate; //Format is standard Month/Day/Year

    /* Getters and Setters */
    public int getId(){ return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String mName) { name = mName; }
    public int getType() { return type; }
    public void setType(int mType) { type = mType; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String mDueDate) { dueDate = mDueDate; }
}
