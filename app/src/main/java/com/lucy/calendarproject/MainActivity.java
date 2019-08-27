package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

        // Get userID
        final String strUserID = getIntent().getStringExtra("USER_ID");
        final int userID = Integer.parseInt(strUserID);


        // Database lookup to create arrayList with groups the user belongs to
        ArrayList<String> usersGroups = new ArrayList<String>();
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        usersGroups= db.getGroupsOfCurrentUser(userID);


        // Get id of radio group
        final RadioGroup rg = (RadioGroup) findViewById(R.id.myRadioGroup);

        // Populate radio group with groups by looping over ArrayList usersGroups
        for(int i=0;i<usersGroups.size();i++){

            // Get name of group to add
            String groupName = usersGroups.get(i);

            // Initialize a new RadioButton
            RadioButton radioButton = new RadioButton(getApplicationContext());

            // Set text of new button
            radioButton.setText(groupName);

            // todo make set colour work otherwise it's ugly green idk why this line does nothing
            radioButton.setHighlightColor(Color.argb(90, 223, 112, 152));

            // Add radio button to group
            rg.addView(radioButton);

        }



        // Change to viewGroup page when button clicked
        Button viewGroup = (Button) findViewById(R.id.viewGroup);
        viewGroup.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int radioButtonId = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(radioButtonId);

                try {
                    String groupName = rb.getText().toString();
                    if(radioButtonId!=-1) {
                        Intent Intent = new Intent(MainActivity.this, ViewGroup.class);
                        Intent.putExtra("NAME_OF_GROUP", groupName);
                        startActivity(Intent);
                    }else{
                        // Error - no group selected
                        Toast.makeText(MainActivity.this, "No group selected", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    // Goes in here if rb content is null
                    Toast.makeText(MainActivity.this, "No group selected", Toast.LENGTH_SHORT).show();
                }


            }
        });

        // Change to createGroup page when button clicked
        Button createGroup = (Button) findViewById(R.id.createGroup);
        createGroup.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, CreateGroup.class);
                Intent.putExtra("USER_ID", strUserID);
                startActivity(Intent);
            }
        });

        // Change to ViewCalendar page when button clicked
        Button viewCalendar = (Button) findViewById(R.id.myCalendar);
        viewCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(MainActivity.this, ViewCalendar.class);
                Intent.putExtra("USER_ID", strUserID);
                startActivity(Intent);
            }
        });

    }
}

