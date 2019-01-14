package com.example.a53914.sievemobileapplication;


import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a53914.sievemobileapplication.db.Task;
import com.example.a53914.sievemobileapplication.db.TaskDatabase;
import com.example.a53914.sievemobileapplication.fragments.DatePickerFragmentD;

import java.util.Calendar;

/**
 *  Java Class for Assignment Details activity
 */
public class AssignmentDetails extends AppCompatActivity {

    GlobalVars global = GlobalVars.getInstance();
    Task mTask = global.getCurrentTask();

    boolean isEditing = false;

    /** Below variables are all View objects stored in convenient location */
    RadioButton habitD;
    RadioButton assignD;
    RadioButton projectD;
    EditText titleText;
    Spinner classSpinner;
    TextView dateText;
    RadioButton lowPCb;
    RadioButton medPCb;
    RadioButton highPCb;
    EditText notesD;

    TaskDatabase taskDatabase;
    Task task;
    int taskID;

    int priorityID;
    String classes;
    int typeID;

    /**
     * Runs when activity started
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_assignment_details);
        task=mTask;

        isEditing = false;

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month =c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        habitD = findViewById(R.id.HabitButton);
        assignD = findViewById(R.id.AssignmentButton);
        projectD = findViewById(R.id.ProjectButton);
        titleText =findViewById(R.id.TaskTitle);
        classSpinner=findViewById(R.id.DetailsClassSpinner);
        dateText = findViewById(R.id.DateEditTextD);
        lowPCb = findViewById(R.id.LPriorityButton);
        medPCb = findViewById(R.id.MPriorityButton);
        highPCb = findViewById(R.id.HPriorityButton);
        notesD = findViewById(R.id.NotesDetails);

        habitD.setClickable(false);
        assignD.setClickable(false);
        projectD.setClickable(false);
        titleText.setClickable(false);
        //titleText.setFocusable(false);
        classSpinner.setClickable(false);
        dateText.setClickable(false);
        //dateText.setFocusable(false);
        lowPCb.setClickable(false);
        medPCb.setClickable(false);
        highPCb.setClickable(false);
        notesD.setClickable(false);
        //notesD.setFocusable(false);

        taskID=mTask.getId();
        int initPrior = mTask.getPriority();
        String initName = mTask.getNameID();
        String initClass = mTask.getClassroom();
        String initDDate = mTask.getDueDate();
        String initNotes = mTask.getNotes();
        int initType = mTask.getTypeID();

        titleText.setText(initName);
        dateText.setText(initDDate);
        notesD.setText(initNotes);
        if(initPrior==0){
            lowPCb.setChecked(true);
            medPCb.setChecked(false);
            highPCb.setChecked(false);
        }else if(initPrior==1){
            lowPCb.setChecked(false);
            medPCb.setChecked(true);
            highPCb.setChecked(false);
        }else if(initPrior==2){
            lowPCb.setChecked(false);
            medPCb.setChecked(false);
            highPCb.setChecked(true);
        }else{
            lowPCb.setChecked(false);
            medPCb.setChecked(true);
            highPCb.setChecked(false);
        }

        if (initType == 0) {
            habitD.setChecked(true);
            assignD.setChecked(false);
            projectD.setChecked(false);
        }else if (initType == 1) {
            habitD.setChecked(false);
            assignD.setChecked(true);
            projectD.setChecked(false);
        }else if (initType == 2) {
            habitD.setChecked(false);
            assignD.setChecked(false);
            projectD.setChecked(true);
        }else{
            habitD.setChecked(false);
            assignD.setChecked(true);
            projectD.setChecked(false);
        }


    }

    /**
     * Takes user back to home page when button clicked
     * @param view
     */
    public void toHomePage(View view){
        Intent toHomePage = new Intent(this, HomePage.class);
        startActivity(toHomePage);
    }

    /**
     * Enables editing of task shown
     * @param view
     */
    public void editTask(View view){
        task=mTask;
        //Intent editTask = new Intent (this, TaskCreate.class);
        //startActivity(editTask);
        Button editButton = (Button) findViewById(R.id.editButton);

        habitD = findViewById(R.id.HabitButton);
        assignD = findViewById(R.id.AssignmentButton);
        projectD = findViewById(R.id.ProjectButton);
        titleText =findViewById(R.id.TaskTitle);
        classSpinner=findViewById(R.id.DetailsClassSpinner);
        dateText = findViewById(R.id.DateEditTextD);
        lowPCb = findViewById(R.id.LPriorityButton);
        medPCb = findViewById(R.id.MPriorityButton);
        highPCb = findViewById(R.id.HPriorityButton);
        notesD = findViewById(R.id.NotesDetails);

        taskDatabase = TaskDatabase.getInstance(AssignmentDetails.this);
        if(isEditing==false){

            editButton.setText("Save");

            habitD.setClickable(true);
            assignD.setClickable(true);
            projectD.setClickable(true);
            titleText.setClickable(true);
            //titleText.setFocusable(true);
            classSpinner.setClickable(true);
            dateText.setClickable(true);
            lowPCb.setClickable(true);
            medPCb.setClickable(true);
            highPCb.setClickable(true);
            notesD.setClickable(true);
            //notesD.setFocusable(true);
            isEditing=true;
        }
        else{


            editButton.setText("Edit Text");
            habitD.setClickable(false);
            assignD.setClickable(false);
            projectD.setClickable(false);
            titleText.setClickable(false);
            //titleText.setFocusable(false);
            classSpinner.setClickable(false);
            dateText.setClickable(false);
            //dateText.setFocusable(false);
            lowPCb.setClickable(false);
            medPCb.setClickable(false);
            highPCb.setClickable(false);
            notesD.setClickable(false);
            //notesD.setFocusable(false);

            task.setId(taskID);

            task.setPriority(priorityID);

            task.setNameID(titleText.getText().toString());

            classSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    classes = (parent.getItemAtPosition(pos)).toString();
                }

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            task.setClassroom(classes);

            task.setDueDate(dateText.getText().toString());

            task.setNotes(notesD.getText().toString());

            task.setTypeID(typeID);
            taskDatabase.taskDao().update(task);
            global.setCurrentTask(task);

            isEditing=false;
        }
    }

    public void onTypeButtonClicked(View view){
        boolean checked = ((RadioButton) view ).isChecked();

        switch(view.getId()){
            case R.id.HabitButton:
                if(checked)
                    typeID=0;
                break;
            case R.id.AssignmentButton:
                if(checked)
                    typeID=1;
                break;
            case R.id.ProjectButton:
                if(checked)
                    typeID=2;
                break;
        }
    }
    public void onPriorityButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()){
            case R.id.LPriorityButton:
                if (checked)
                    priorityID=0;
                break;
            case R.id.MPriorityButton:
                if(checked)
                    priorityID=1;
                break;
            case R.id.HPriorityButton:
                if(checked)
                    priorityID=2;
                break;
        }
    }

    /**
     * Takes user to Assignment Start activity
     * @param view
     */
    public void beginTask(View view){
        Intent beginTask = new Intent(this, AssignmentStart.class);
        startActivity(beginTask);
    }
    public void showDatePickerDialog(View view){
        DialogFragment newFragment = new DatePickerFragmentD();
        newFragment.show(getSupportFragmentManager(),"datePicker");

    }
}
