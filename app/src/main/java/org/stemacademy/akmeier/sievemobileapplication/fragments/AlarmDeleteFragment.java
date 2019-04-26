package org.stemacademy.akmeier.sievemobileapplication.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.stemacademy.akmeier.sievemobileapplication.R;
import org.stemacademy.akmeier.sievemobileapplication.Settings;

public class AlarmDeleteFragment extends DialogFragment implements DialogInterface.OnDismissListener {
    Settings settings;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.dialog_alarm_delete, container, false);

        TextView mNo=view.findViewById(R.id.ADNo);
        TextView mYes=view.findViewById(R.id.ADYes);
        settings=(Settings)getActivity();
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.clearAlarms();
                getDialog().dismiss();
            }
        });

        return view;
    }
}
