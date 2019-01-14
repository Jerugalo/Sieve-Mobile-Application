package com.example.a53914.sievemobileapplication.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a53914.sievemobileapplication.R;
import com.example.a53914.sievemobileapplication.TaskCreate;
import com.example.a53914.sievemobileapplication.db.Class;

/**
 * Configures the class creation dialog. The ok button is set to take the input and insert it into
 * the class database. The cancel button closes the dialog.
 */

public class ClassCreationDialog extends DialogFragment implements DialogInterface.OnDismissListener {
    private static final String TAG = "ClassCreationDialog";
    private EditText mInput;
    private TaskCreate taskCreate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_class_creation, container, false);
        TextView mButtonOk = view.findViewById(R.id.action_ok);
        TextView mButtonCancel = view.findViewById(R.id.action_cancel);
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

        /* Adds the class when ok is selected */
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input;
                Class mClass = new Class();
                Log.d(TAG, "onClick: capturing input.");
                input = mInput.getText().toString();
                Log.d(TAG, input);
                mClass.setName(input);
                mClass.setType(0);
                mClass.setId(0);
                mClass.setDueDate("");
                taskCreate.callInsertClass(mClass);
                getDialog().dismiss();
            }
        });
        return view;
    }
}
