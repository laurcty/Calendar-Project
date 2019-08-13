package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get userID
        final String strUserID = getIntent().getStringExtra("USER_ID");
        final int userID = Integer.parseInt(strUserID);

        //todo make actual groups user belongs to appear here

        //database lookup to create arrayList with groups the user belongs to
        ArrayList<String> usersGroups = new ArrayList<String>();
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        usersGroups= db.getGroupsOfCurrentUser(userID);


        //get id of radio group
        final RadioGroup rg = (RadioGroup) findViewById(R.id.myRadioGroup);

        //populate radio group with groups by looping over ArrayList usersGroups
        for(int i=0;i<usersGroups.size();i++){

            // Get name of group to add
            String groupName = usersGroups.get(i);

            // Initialize a new RadioButton
            RadioButton radioButton = new RadioButton(getApplicationContext());

            // Set text of new button
            radioButton.setText(groupName);

            // Add radio button to group
            rg.addView(radioButton);

        }




        //change to viewGroup page when button clicked
        Button viewGroup = (Button) findViewById(R.id.viewGroup);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
        viewGroup.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int groupID = radioGroup.getCheckedRadioButtonId();
                if(groupID!=-1) {

                    String strGroupID = Integer.toString(groupID);
                    //Toast.makeText(MainActivity.this, Integer.toString(groupID), Toast.LENGTH_SHORT).show();
                    Intent Intent = new Intent(MainActivity.this, ViewGroup.class);
                    Intent.putExtra("ID_OF_GROUP", strGroupID);
                    startActivity(Intent);
                }else{
                    //error - no group selected
                    Toast.makeText(MainActivity.this, "No group selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //change to createGroup page when button clicked
        Button createGroup = (Button) findViewById(R.id.createGroup);
        createGroup.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, CreateGroup.class);
                Intent.putExtra("USER_ID", strUserID);
                startActivity(Intent);
            }
        });

        //change to ViewCalendar page when button clicked
        Button viewCalendar = (Button) findViewById(R.id.myCalendar);
        viewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewCalendar.class));
            }
        });


    }
}

