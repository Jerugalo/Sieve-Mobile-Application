package com.example.a53914.sievemobileapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.example.a53914.sievemobileapplication.db.Task;
import com.example.a53914.sievemobileapplication.db.TaskDao;

public class TaskCreate extends AppCompatActivity {
    int priorityID;
    String classes;
    int typeID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);
        SeekBar slidey = findViewById(R.id.TaskCreateSeekbar);
        slidey.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priorityID = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Spinner classChooser = findViewById(R.id.planets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classChooser.setAdapter(adapter);
        classChooser.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                classes = (parent.getItemAtPosition(pos)).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void AddToDB(View view) {
        DBAddition a = new DBAddition();
        new Thread(a).start();
    }

    public void HabitClick(View view) {
        typeID = 0;
    }

    public void AssignClick(View view) {
        typeID = 1;
    }

    public void ProjectClick(View view) {
        typeID = 2;
    }

    //Returning Home
    public void toHomePage(View view) {
        Intent toHomePage = new Intent(this, HomePage.class);
        startActivity(toHomePage);
    }

    class DBAddition implements Runnable {
        public void run() {
            final TaskDao taskDao = null;
            Task task = new Task();
            task.setPriority(priorityID);

            EditText nameText = findViewById(R.id.NameAddText);
            task.setNameID(nameText.getText().toString());

            task.setClassroom(classes);

            //task.setDueDate();

            EditText notesText = findViewById(R.id.NotesText);
            task.setNotes(notesText.getText().toString());

            task.setTypeID(typeID);
            taskDao.insertAll(task);
        }
    }
}
