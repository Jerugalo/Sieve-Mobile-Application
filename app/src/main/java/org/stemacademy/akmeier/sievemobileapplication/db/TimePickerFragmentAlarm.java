package org.stemacademy.akmeier.sievemobileapplication.db;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import org.stemacademy.akmeier.sievemobileapplication.AssignmentDetails;
import org.stemacademy.akmeier.sievemobileapplication.TaskCreate;

import java.util.Calendar;

public class TimePickerFragmentAlarm extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    TaskCreate taskCreate;
    AssignmentDetails assignmentDetails;
    public static String PARENT="";
    boolean isTaskCreate;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int hour =12;
        int minute=00;
        if(PARENT=="TaskCreate"){
            taskCreate = (TaskCreate) getActivity();
            isTaskCreate=true;
        }else if(PARENT=="AssignmentDetails"){
            assignmentDetails=(AssignmentDetails) getActivity();
            isTaskCreate=false;
        }

        return new TimePickerDialog(getActivity(),this,hour,minute,true);
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String timeString = hourOfDay + "/" +minute + "/";
        if(isTaskCreate) {
            taskCreate.currentTime = timeString;
            taskCreate.alarmSet2(taskCreate.findViewById(android.R.id.content));
        }else if(!isTaskCreate){
            assignmentDetails.currentTime=timeString;
            assignmentDetails.alarmSet2D(assignmentDetails.findViewById(android.R.id.content));
        }

    }
}
