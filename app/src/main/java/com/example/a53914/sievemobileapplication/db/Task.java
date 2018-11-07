package com.example.a53914.sievemobileapplication.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int Priority;

    @ColumnInfo(name = "name")
    private String NameID;

    @ColumnInfo(name = "class")
    private String Classroom;//Class as in school, not programming

   // @ColumnInfo(name = "date")
   // private String DueDate;

    //@ColumnInfo(name="timeEst")
    //private float TimeEst;

    @ColumnInfo(name = "notes")
    private String Notes;

    private int TypeID;//0 is habit, 1 is assignment, 2 is project

    public Task(int Priority, String NameID, String Classroom, /*String DueDate,*/ String Notes, int TypeID) {
        this.Priority = Priority;
        this.NameID = NameID;
        this.Classroom = Classroom;
        //this.DueDate = DueDate;
        this.Notes = Notes;
        this.TypeID = TypeID;
    }

    //Below Are Getters and Setters
    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }

    public String getNameID() {
        return NameID;
    }

    public void setNameID(String name) {
        NameID = name;
    }

    public String getClassroom() {
        return Classroom;
    }

    public void setClassroom(String aClass) {
        Classroom = aClass;
    }

    //public String getDueDate() { return DueDate; }

    //public void setDueDate(String dueDate) { DueDate = dueDate; }

    //public float getTimeEst() { return TimeEst; }

    //public void setTimeEst(float timeEst) { TimeEst = timeEst; }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public int getTypeID() { return TypeID; }
    public void setTypeID(int type) { TypeID = type; }

    public int getId(){return id;}

    public void setId(int id) { this.id = id; }
    // Getters and setters are ignored for brevity,
    // but they're required for Room to work.
    @Override
    public String toString(){
        return "Note{"+"id="+id+"NameID="+NameID+"}";
    }
}