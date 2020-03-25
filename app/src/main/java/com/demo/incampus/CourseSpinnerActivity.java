package com.demo.incampus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CourseSpinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_spinner);

        Spinner spinner = findViewById(R.id.spinner);

        List<String> courses = new ArrayList<>();
        courses.add(0,"Choose Your Course");
        courses.add("(B.Tech/BE) Computers/IT/\nSoftware");
        courses.add("(B.Tech/BE) Electrical/\nElectronics");
        courses.add("(B.Tech/BE) Mechanical/\nAutomobile/Aeronautical");
        courses.add("(B.Tech/BE) Production/\nManufacturing");
        courses.add("(B.Tech/BE) Civil");
        courses.add("(B.Tech/BE) Chemical/\nPetroleum/Textile");
        courses.add("(B.Tech/BE) BioTech/\nEnvironmental");
        courses.add("(B.Tech/BE) Others");
        courses.add("(M.Tech/ME) Computers/IT/\nSoftware");
        courses.add("(M.Tech/ME) Electrical/\nElectronics");
        courses.add("(M.Tech/ME) Mechanical/\nAutomobile/Aeronautical");
        courses.add("(M.Tech/ME) Production/\nManufacturing");
        courses.add("(M.Tech/ME) Civil");
        courses.add("(M.Tech/ME) Chemical/\nPetroleum/Textile");
        courses.add("(M.Tech/ME) BioTech/\nEnvironmental");
        courses.add("(M.Tech/ME) Others");
        courses.add("(BCA)");
        courses.add("(MCA)");
        courses.add("(B.Sc/BS) Physics or related");
        courses.add("(B.Sc/BS) Chemistry or related");
        courses.add("(B.Sc/BS) Maths or related");
        courses.add("(M.Sc/MS) Physics or related");
        courses.add("(M.Sc/MS) Chemistry or related");
        courses.add("(M.Sc/MS) Maths or related");
        courses.add("(B.Com/B.Sc) Economics or\n Finance");
        courses.add("(M.Com/M.Sc) Economics or\n Finance");
        courses.add("(BA)");
        courses.add("(MA)");
        courses.add("(Ph.D/M.Phil/Research)");




        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,courses);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose Your Course")) {
                    //do nothing
                } else {
                    String course = parent.getItemAtPosition(position).toString();
                    Toast.makeText(CourseSpinnerActivity.this, course, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
