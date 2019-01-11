package com.example.a53914.sievemobileapplication.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a53914.sievemobileapplication.GlobalVars;
import com.example.a53914.sievemobileapplication.R;
import com.example.a53914.sievemobileapplication.TaskCreate;
import com.example.a53914.sievemobileapplication.db.Class;
import com.example.a53914.sievemobileapplication.db.ClassDatabase;
import com.example.a53914.sievemobileapplication.db.Task;
import com.example.a53914.sievemobileapplication.db.TaskDatabase;

import java.lang.ref.WeakReference;

public class ClassCreationDialog extends DialogFragment implements DialogInterface.OnDismissListener {

    private static final String TAG = "ClassCreationDialog";

    private EditText mInput;
    private TextView mButtonOk, mButtonCancel;
    TaskCreate taskCreate;

    GlobalVars global = GlobalVars.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_class_creation, container, false);
        mButtonOk = view.findViewById(R.id.action_ok);
        mButtonCancel = view.findViewById(R.id.action_cancel);
        mInput = view.findViewById(R.id.input);
        taskCreate = (TaskCreate)getActivity();

        /* Closes the dialog when cancel is selected */
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });

        /* Returns the class name when ok is selected */
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input.");

                if (!mInput.equals("")) {
                    String input = mInput.getText().toString();
                    Log.d(TAG, input);
                    global.setClassName(input);
                }
                Class mClass = new Class();
                if (global.getClassName() != null){
                    mClass.setName(global.getClassName());
                    global.setClassName(null);
                    mClass.setType(0);
                    mClass.setId(0);
                    mClass.setDueDate("");
                    taskCreate.callInsertClass(mClass);
                }
                getDialog().dismiss();
            }
        });

        return view;
    }
}
