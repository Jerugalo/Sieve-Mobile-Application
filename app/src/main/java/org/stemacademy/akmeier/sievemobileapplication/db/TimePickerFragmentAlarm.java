package org.stemacademy.akmeier.sievemobileapplication.db;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import org.stemacademy.akmeier.sievemobileapplication.TaskCreate;

import java.util.Calendar;

public class TimePickerFragmentAlarm extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    TaskCreate taskCreate;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int hour =12;
        int minute=00;
        taskCreate = (TaskCreate) getActivity();
        return new TimePickerDialog(getActivity(),this,hour,minute,true);
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String timeString = hourOfDay + "/" +minute + "/";

        taskCreate.currentTime=timeString;
        taskCreate.alarmSet2(taskCreate.findViewById(android.R.id.content));
    }
}