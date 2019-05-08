package org.stemacademy.akmeier.sievemobileapplication.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.stemacademy.akmeier.sievemobileapplication.AssignmentDetails;
import org.stemacademy.akmeier.sievemobileapplication.TaskCreate;

import java.util.Calendar;
import java.util.Objects;


public class DatePickerFragmentAlarm extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static String PARENT="";
    public boolean isTaskCreate;
    TaskCreate taskCreate;
    AssignmentDetails assignmentDetails;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month =c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if(PARENT=="TaskCreate") {
            taskCreate = (TaskCreate) getActivity();
            isTaskCreate=true;
        }else if (PARENT=="AssignmentDetails"){
            assignmentDetails=(AssignmentDetails)getActivity();
            isTaskCreate=false;
        }
        return new DatePickerDialog(Objects.requireNonNull(getActivity()),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateString = year + "/" + month + "/" +dayOfMonth;

        if(isTaskCreate) {
            taskCreate.currentDate = dateString;
            taskCreate.alarmSet3(taskCreate.findViewById(android.R.id.content));
        }else{
            assignmentDetails.currentDate=dateString;
            assignmentDetails.alarmSet3D(assignmentDetails.findViewById(android.R.id.content));
        }
    }
}
