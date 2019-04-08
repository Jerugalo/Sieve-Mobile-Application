package org.stemacademy.akmeier.sievemobileapplication.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.stemacademy.akmeier.sievemobileapplication.R;
import org.stemacademy.akmeier.sievemobileapplication.TaskCreate;

import java.lang.reflect.Array;

public class DaysOfWeekDialog extends DialogFragment implements DialogInterface.OnDismissListener{
    private TaskCreate taskCreate;
    public boolean[] array;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_days_of_week, container, false);
        TextView doneButton = view.findViewById(R.id.DoneButton);
        final ToggleButton sun=view.findViewById(R.id.toggleButtonSun);
        final ToggleButton mon=view.findViewById(R.id.toggleButtonMon);
        final ToggleButton tue=view.findViewById(R.id.toggleButtonTue);
        final ToggleButton wed=view.findViewById(R.id.toggleButtonWed);
        final ToggleButton thu=view.findViewById(R.id.toggleButtonThu);
        final ToggleButton fri=view.findViewById(R.id.toggleButtonFri);
        final ToggleButton sat=view.findViewById(R.id.toggleButtonSat);
        TextView cancelButton = view.findViewById(R.id.CancelButton);
        taskCreate=(TaskCreate)getActivity();

        array= new boolean[7];
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sun.isChecked()){
                    array[0]=true;
                } else{
                    array[0]=false;
                }
                if(mon.isChecked()){
                    array[1]=true;
                } else{
                    array[1]=false;
                }
                if(tue.isChecked()){
                    array[2]=true;
                } else{
                    array[2]=false;
                }
                if(wed.isChecked()){
                    array[3]=true;
                } else{
                    array[3]=false;
                }
                if(thu.isChecked()){
                    array[4]=true;
                } else{
                    array[4]=false;
                }
                if(fri.isChecked()){
                    array[5]=true;
                } else{
                    array[5]=false;
                }
                if(sat.isChecked()){
                    array[6]=true;
                } else{
                    array[6]=false;
                }
                taskCreate.dayOfWeekBools=array;
                getDialog().dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }
}
