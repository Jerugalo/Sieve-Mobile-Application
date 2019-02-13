package org.stemacademy.akmeier.sievemobileapplication.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import org.stemacademy.akmeier.sievemobileapplication.R;

import java.util.Calendar;
import java.util.Objects;

public class DatePickerFragmentD extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month =c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(Objects.requireNonNull(getActivity()),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView datetext=(TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.DateEditTextD);
        month = month +1;
        String dateString = month + "/" + dayOfMonth + "/" +year;
        datetext.setText(dateString);
    }
}
