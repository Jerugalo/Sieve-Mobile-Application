package org.stemacademy.akmeier.sievemobileapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.stemacademy.akmeier.sievemobileapplication.R;
import org.stemacademy.akmeier.sievemobileapplication.db.Class;
import org.stemacademy.akmeier.sievemobileapplication.db.ClassDatabase;
import org.stemacademy.akmeier.sievemobileapplication.fragments.ClassCreationDialog;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {
    RadioButton T1Rd ;
    RadioButton T2Rd;
    RadioButton T3Rd;
    RadioButton T4Rd;
    RadioButton T5Rd;
    RadioButton T6Rd;
    int themeId;

    private Spinner classChooser;
    private ArrayAdapter classAdapter;
    private final ArrayList<String> classList = new ArrayList<>();
    private ClassDatabase classDatabase;

    /*
     * This public class defines a SharedPreferencesManager.
     * The retrieveInt definition allows us to access the themeId rom SPM.
     * The storeInt method allows us to write to the SharedPreferencesManager.
     */
    public class SharedPreferencesManager {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        determineTheme();
        setContentView(R.layout.activity_settings);
        T1Rd = findViewById(R.id.themeDefault);
        T2Rd = findViewById(R.id.themeAlternative);
        T3Rd = findViewById(R.id.themeCombined);
        T4Rd = findViewById(R.id.themeDark);
        T5Rd = findViewById(R.id.themeSimple);
        T6Rd = findViewById(R.id.themeOlive);

        /* Fills the spinner and allows user to select a class from the class database */
        createClassList();
        classChooser = findViewById(R.id.deleteClassSpinner);
        classAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classChooser.setAdapter(classAdapter);
        classChooser.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if ((parent.getItemAtPosition(pos)).toString().equals("Delete All Classes")){
                    classDatabase.classDao().deleteAll();
                    refreshSpinner();
                } else if (!(parent.getItemAtPosition(pos)).toString().equals("Delete Class")) {
                    deleteClass((parent.getItemAtPosition(pos)).toString());
                    refreshSpinner();
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /** Creates a new class list and updates the spinner*/
    private void refreshSpinner(){
        createClassList();
        classAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classAdapter.notifyDataSetChanged();
    }

    /** Creates the array of classes that the spinner displays */
    private void createClassList() {
        classList.clear();
        classDatabase = ClassDatabase.getInstance(this);
        List<Class> clses = classDatabase.classDao().getAll();
        classList.add("Delete Class");
        for (Class cls : clses) {
            classList.add(cls.getName());
        }
        classList.add("Delete All Classes");
    }

    /** Creates the array of classes that the spinner displays */
    private void deleteClass(String deleteCls) {
        List<Class> clses = classDatabase.classDao().getAll();
        for (Class cls : clses) {
            if (deleteCls.equals(cls.getName())){
                classDatabase.classDao().delete(cls);
            }
        }
    }

    public void onThemeRadio(){
        new SharedPreferencesManager(getApplicationContext()).storeInt("themeId",themeId);
        recreate();
    }
    public void determineTheme(){
        int themeId = new SharedPreferencesManager(this).retrieveInt("themeId",1);
        if(themeId == 1){setTheme(R.style.SieveDefault);}
        else if(themeId == 2){setTheme(R.style.SieveAlternative);}
        else if(themeId == 3){setTheme(R.style.SieveCombined);}
        else if(themeId == 4){setTheme(R.style.SieveDark);}
        else if(themeId == 5){setTheme(R.style.SieveSimple);}
        else if(themeId == 6){setTheme(R.style.SieveOlive);}
        else{setTheme(R.style.SieveDefault);}
    }
    //Returning Home
    public void toHomePage(View view){
        Intent toHomePage = new Intent(this, HomePage.class);
        startActivity(toHomePage);
    }
    public void T1RDClicked(View view){
        themeId = 1;
        //Manual RadioButton setup, b/c android studio only works with radio groups when RadioButtons are under one Radiogroup and nothing else
        T1Rd.setChecked(true);
        T2Rd.setChecked(false);
        T3Rd.setChecked(false);
        T4Rd.setChecked(false);
        T5Rd.setChecked(false);
        T6Rd.setChecked(false);
        onThemeRadio();
    }
    public void T2RDClicked(View view){
        themeId = 2;
        T1Rd.setChecked(false);
        T2Rd.setChecked(true);
        T3Rd.setChecked(false);
        T4Rd.setChecked(false);
        T5Rd.setChecked(false);
        T6Rd.setChecked(false);
        onThemeRadio();
    }
    public void T3RDClicked(View view){
        themeId = 3;
        T1Rd.setChecked(false);
        T2Rd.setChecked(false);
        T3Rd.setChecked(true);
        T4Rd.setChecked(false);
        T5Rd.setChecked(false);
        T6Rd.setChecked(false);
        onThemeRadio();
    }
    public void T4RDClicked(View view){
        themeId = 4;
        T1Rd.setChecked(false);
        T2Rd.setChecked(false);
        T3Rd.setChecked(false);
        T4Rd.setChecked(true);
        T5Rd.setChecked(false);
        T6Rd.setChecked(false);
        onThemeRadio();
    }
    public void T5RDClicked(View view){
        themeId = 5;
        T1Rd.setChecked(false);
        T2Rd.setChecked(false);
        T3Rd.setChecked(false);
        T4Rd.setChecked(false);
        T5Rd.setChecked(true);
        T6Rd.setChecked(false);
        onThemeRadio();
    }
    public void T6RDClicked(View view){
        themeId = 6;
        T1Rd.setChecked(false);
        T2Rd.setChecked(false);
        T3Rd.setChecked(false);
        T4Rd.setChecked(false);
        T5Rd.setChecked(false);
        T6Rd.setChecked(true);
        onThemeRadio();
    }

}
