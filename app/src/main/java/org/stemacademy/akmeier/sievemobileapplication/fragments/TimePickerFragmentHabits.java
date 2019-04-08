package org.stemacademy.akmeier.sievemobileapplication.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.TimePicker;

import org.stemacademy.akmeier.sievemobileapplication.R;
import org.stemacademy.akmeier.sievemobileapplication.TaskCreate;

import java.util.Calendar;
import java.util.Objects;

public class TimePickerFragmentHabits extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
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
        String timeString = hourOfDay + ":" +minute;
        TextView datetext=(TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.DateViewer);
        datetext.setText(timeString);

    }
}
