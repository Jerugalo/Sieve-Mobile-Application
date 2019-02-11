package org.stemacademy.akmeier.sievemobileapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

//import org.stemacademy.akmeier.sievemobileapplication.R;
import org.stemacademy.akmeier.sievemobileapplication.db.Classroom;
import org.stemacademy.akmeier.sievemobileapplication.db.ClassroomDatabase;
import org.stemacademy.akmeier.sievemobileapplication.db.Task;
import org.stemacademy.akmeier.sievemobileapplication.db.TaskDatabase;
import org.stemacademy.akmeier.sievemobileapplication.fragments.ClassroomCreationDialog;
import org.stemacademy.akmeier.sievemobileapplication.fragments.DatePickerFragmentD;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *  Java Classroom for Assignment Details activity
 */
public class AssignmentDetails extends AppCompatActivity {
    public class SharedPreferencesManager{
        private SharedPreferences themeStorage;
        private SharedPreferences.Editor sEditor;
        private Context context;
        SharedPreferencesManager(Context context){
            this.context = context;
            themeStorage = PreferenceManager.getDefaultSharedPreferences(context);
        }
        private SharedPreferences.Editor getEditor(){
            return themeStorage.edit();
        }
        int retrieveInt(String tag, int defValue){
            return themeStorage.getInt(tag, defValue);
        }
        void storeInt(String tag, int defValue){
            sEditor = getEditor();
            sEditor.putInt(tag, defValue);
            sEditor.commit();
        }
    }
    GlobalVars global = GlobalVars.getInstance();
    Task mTask = global.getCurrentTask();

    boolean isEditing = false;

    /** Below variables are all View objects stored in convenient location */
    RadioButton habitD;
    RadioButton assignD;
    RadioButton projectD;
    EditText titleText;
    Spinner classroomSpinner;
    ArrayAdapter classroomAdapter;
    TextView dateText;
    RadioButton lowPCb;
    RadioButton medPCb;
    RadioButton highPCb;
    EditText notesD;

    TaskDatabase taskDatabase;
    Task task;
    int taskID;

    int priorityID;
    String currentClassroom;
    int typeID;

    private final ArrayList<String> classroomList = new ArrayList<>();
    private ClassroomDatabase classroomDatabase;

    /**
     * Runs when activity started
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        determineTheme();
        setContentView(R.layout.activity_assignment_details);
        task=mTask;
        taskDatabase=TaskDatabase.getInstance(this);

        isEditing = false;

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month =c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        habitD = findViewById(R.id.HabitButton);
        assignD = findViewById(R.id.AssignmentButton);
        projectD = findViewById(R.id.ProjectButton);
        titleText =findViewById(R.id.TaskTitle);
        classroomSpinner =findViewById(R.id.DetailsClassSpinner);
        dateText = findViewById(R.id.DateEditTextD);
        lowPCb = findViewById(R.id.LPriorityButton);
        medPCb = findViewById(R.id.MPriorityButton);
        highPCb = findViewById(R.id.HPriorityButton);
        notesD = findViewById(R.id.NotesDetails);

        habitD.setClickable(false);
        assignD.setClickable(false);
        projectD.setClickable(false);
        titleText.setClickable(false);
        titleText.setInputType(0);
        //titleText.setFocusable(false);
        classroomSpinner.setClickable(false);
        dateText.setClickable(false);
        //dateText.setFocusable(false);
        lowPCb.setClickable(false);
        medPCb.setClickable(false);
        highPCb.setClickable(false);
        notesD.setClickable(false);
        notesD.setInputType(0);
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

        /* Fills the spinner and allows user to select a class from the class database */
        createClassroomList();
        classroomSpinner = findViewById(R.id.DetailsClassSpinner);
        classroomAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classroomList);
        classroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classroomSpinner.setAdapter(classroomAdapter);
        classroomSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if ((parent.getItemAtPosition(pos)).toString().equals("Create New Classroom")){
                    ClassroomCreationDialog dialog = new ClassroomCreationDialog();
                    dialog.PARENT = "AssignmentDetails";
                    dialog.show(getSupportFragmentManager(), "ClassroomCreationDialog");
                } else if (isEditing){

                    currentClassroom = (parent.getItemAtPosition(pos)).toString();

                    if(currentClassroom==null){
                        currentClassroom="";
                    }

                } else {
                    int spinnerPos = classroomAdapter.getPosition(task.getClassroom());
                    classroomSpinner.setSelection(spinnerPos);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    protected void onStart(){
        super.onStart();
        determineTheme();
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
        classroomSpinner =findViewById(R.id.DetailsClassSpinner);
        dateText = findViewById(R.id.DateEditTextD);
        lowPCb = findViewById(R.id.LPriorityButton);
        medPCb = findViewById(R.id.MPriorityButton);
        highPCb = findViewById(R.id.HPriorityButton);
        notesD = findViewById(R.id.NotesDetails);

        taskDatabase = TaskDatabase.getInstance(AssignmentDetails.this);
        if(isEditing==false){

            editButton.setText("Save");

            classroomSpinner.setClickable(true);
            habitD.setClickable(true);
            assignD.setClickable(true);
            projectD.setClickable(true);
            titleText.setClickable(true);
            titleText.setInputType(97);
            //titleText.setFocusable(true);
            classroomSpinner.setClickable(true);
            dateText.setClickable(true);
            lowPCb.setClickable(true);
            medPCb.setClickable(true);
            highPCb.setClickable(true);
            notesD.setClickable(true);
            notesD.setInputType(97);
            //notesD.setFocusable(true);
            isEditing=true;
        }
        else{


            editButton.setText("Edit Text");
            classroomSpinner.setClickable(false);
            habitD.setClickable(false);
            assignD.setClickable(false);
            projectD.setClickable(false);
            titleText.setClickable(false);
            //titleText.setFocusable(false);
            titleText.setInputType(0);
            classroomSpinner.setClickable(false);
            dateText.setClickable(false);
            //dateText.setFocusable(false);
            lowPCb.setClickable(false);
            medPCb.setClickable(false);
            highPCb.setClickable(false);
            notesD.setClickable(false);
            notesD.setInputType(0);
            //notesD.setFocusable(false);

            task.setId(taskID);
            task.setPriority(priorityID);
            task.setNameID(titleText.getText().toString());



            if(currentClassroom==null){
                currentClassroom=task.getClassroom();
                if(currentClassroom==null){
                    currentClassroom="";
                }
            }
            task.setClassroom(currentClassroom);
            task.setDueDate(dateText.getText().toString());
            task.setNotes(notesD.getText().toString());
            task.setTypeID(typeID);
            global.setCurrentTask(task);
            taskDatabase.taskDao().update(task);
            refreshSpinner();
            isEditing=false;
        }
    }

    /** Creates a new class list and updates the spinner*/
    private void refreshSpinner(){
        createClassroomList();
        classroomAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classroomList);
        classroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classroomAdapter.notifyDataSetChanged();
    }

    /** Creates the array of currentClassroom that the spinner displays */
    private void createClassroomList() {
        classroomList.clear();
        classroomDatabase = ClassroomDatabase.getInstance(this);
        List<Classroom> clses = classroomDatabase.classroomDao().getAll();
        classroomList.add(task.getClassroom());
        for (Classroom cls : clses) {
            if (!cls.getName().equals(task.getClassroom())){
                classroomList.add(cls.getName());
            }
        }
        classroomList.add("Create New Classroom");
    }

    /** Calls the AsyncTask InsertClass, that cannot be called from other currentClassroom */
    public void callInsertClassroom(Classroom mClassroom){
        new AssignmentDetails.InsertClass(AssignmentDetails.this, mClassroom).execute();
    }

    /** Puts the class into the class database */
    private static class InsertClass extends AsyncTask<Void, Void,Boolean> {
        private final WeakReference<AssignmentDetails> activityReference;
        private final Classroom cls;
        InsertClass(AssignmentDetails context, Classroom mClassroom){
            activityReference = new WeakReference<>(context);
            cls = mClassroom;
        }
        @Override
        protected Boolean doInBackground(Void...objs){
            activityReference.get().classroomDatabase.classroomDao().insertAll(cls);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean bool){
            if(bool){
                activityReference.get().refreshSpinner();
            }
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
    public void determineTheme(){
        int themeId = new SharedPreferencesManager(this).retrieveInt("themeId",1);
        if(themeId == 1){setTheme(R.style.SieveDefault);}
        else if(themeId == 2){setTheme(R.style.SieveAlternative);}
        else if(themeId == 3){setTheme(R.style.SieveTwilight);}
        else if(themeId == 4){setTheme(R.style.SieveDark);}
        else if(themeId == 5){setTheme(R.style.SieveSimple);}
        else if(themeId == 6){setTheme(R.style.SieveOlive);}
        else{setTheme(R.style.SieveDefault);}
    }



}
