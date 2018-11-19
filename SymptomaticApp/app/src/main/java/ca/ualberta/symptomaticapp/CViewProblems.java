/*
An activity that only the caregiver will be able to see and is used for viewing the problems of a specific patient.
The caregiver will select a patient through the spinner, press the View button and be presented with a list of
problems associated with that patient. The caregiver can also view that patients' contact information.
 */

package ca.ualberta.symptomaticapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CViewProblems extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cview_problems);

        Toolbar toolbar = findViewById(R.id.cviewProblems_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Problems of Patient");
        TextView numproblems = (TextView) findViewById(R.id.tv_ProbNum); //need to change this text when view button pressed
        final Spinner patient = (Spinner) findViewById(R.id.sp_Patients); //need to get selections
        Button viewproblems = (Button) findViewById(R.id.btn_View);
        final Button viewcontactinfo = (Button) findViewById(R.id.btn_viewcontactinfo);
        ListView problemview = (ListView) findViewById(R.id.lv_problems);
        Caregiver caregiver = Login.thisCaregiver;
        ArrayList<String> patients = caregiver.getPatients();
        final List<String> usernames = new ArrayList<String>();;
        for(String user : patients){
            usernames.add(user);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, usernames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patient.setAdapter(adapter);

        viewproblems.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // take current selected patient and populate list view using their problems
                viewcontactinfo.setEnabled(true); //allow view contact info to be able to be pressed
                String selection = patient.getSelectedItem().toString(); //get current selection
                int patindex = usernames.indexOf(selection);
                // GET PROBLEMS OF USER HERE USING THE SELECTED INDEX
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cview_problems_menu, menu);
        return true;
    }
    public void viewHome(MenuItem menu) {
        Intent intent = new Intent(CViewProblems.this, CaregiverHome.class);
        startActivity(intent);
    }
    public void viewViewRecords(MenuItem menu) {
        Intent intent = new Intent(CViewProblems.this, CViewRecords.class);
        startActivity(intent);
    }public void viewViewPatients(MenuItem menu) {
        Intent intent = new Intent(CViewProblems.this, ViewPatients.class);
        startActivity(intent);
    }

}
