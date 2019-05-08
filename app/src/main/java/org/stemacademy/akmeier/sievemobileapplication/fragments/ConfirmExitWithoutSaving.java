package org.stemacademy.akmeier.sievemobileapplication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
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

import org.stemacademy.akmeier.sievemobileapplication.AssignmentDetails;
import org.stemacademy.akmeier.sievemobileapplication.R;
import org.stemacademy.akmeier.sievemobileapplication.TaskCreate;

public class ConfirmExitWithoutSaving extends DialogFragment implements DialogInterface.OnDismissListener {
    private AssignmentDetails assignmentDetails;
    private TaskCreate taskCreate;
    public static String PARENT = "";
    private static final String TAG = "ClassroomCreationDialog";
    public static String NEXT="";
    private boolean isTaskCreate;

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //final View view = inflater.inflate(R.layout.dialog_confirm_save, container, false);
        //TextView mNo=view.findViewById(R.id.NoButton);
        //TextView mYes=view.findViewById(R.id.YesButton);
        if (PARENT.equals("TaskCreate")){
            taskCreate = (TaskCreate)getActivity();
            isTaskCreate=true;
        }
        if (PARENT.equals("AssignmentDetails")) {
            assignmentDetails = (AssignmentDetails)getActivity();
            isTaskCreate=false;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.ConfirmExitWOSave)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG,"onClick:moving to next");
                        if(isTaskCreate){
                            taskCreate.intentToHomePage();
                        }
                        if(!isTaskCreate){
                            if(NEXT.equals("AssignmentStart")){
                                assignmentDetails.beginTaskIntent();
                            }
                            if(NEXT.equals("HomePage")){
                                assignmentDetails.toHomePageIntent();
                            }
                        }
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "onClick: closing dialog");
                        getDialog().dismiss();
                    }
                });

        /*
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });

        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick:moving to next");
                if(isTaskCreate){
                    taskCreate.intentToHomePage();
                }
                if(!isTaskCreate){
                    if(NEXT.equals("AssignmentStart")){
                        assignmentDetails.beginTaskIntent();
                    }
                    if(NEXT.equals("HomePage")){
                        assignmentDetails.toHomePageIntent();
                    }
                }
                getDialog().dismiss();
            }
        });
        */

        return builder.create();
    }
}
