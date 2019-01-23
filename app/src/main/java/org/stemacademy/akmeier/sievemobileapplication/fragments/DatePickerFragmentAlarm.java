package org.stemacademy.akmeier.sievemobileapplication.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.stemacademy.akmeier.sievemobileapplication.TaskCreate;

import java.util.Calendar;
import java.util.Objects;


public class DatePickerFragmentAlarm extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    TaskCreate taskCreate;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month =c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        taskCreate=(TaskCreate)getActivity();
        return new DatePickerDialog(Objects.requireNonNull(getActivity()),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String dateString = year + "/" + month + "/" +dayOfMonth;

        taskCreate.currentDate = dateString;
        taskCreate.alarmSet3(taskCreate.findViewById(android.R.id.content));
    }
}
