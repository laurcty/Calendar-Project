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
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AsyncTaskListener{

    ArrayList<String> usersGroupsIDs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get username from intent of activity that called MainActivity
        final String username = getIntent().getStringExtra("USERNAME");

        background bg = new background (MainActivity.this);
        bg.execute("username","blank",username,"blank","findGroupsOfUser");        // Blank is used as a placeholder since this SQL only needs one variable

        // Change to createGroup page when button clicked
        Button createGroup = (Button) findViewById(R.id.createGroup);
        createGroup.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, CreateGroup.class);
                Intent.putExtra("USERNAME", username);
                startActivity(Intent);
            }
        });

        // Change to ViewCalendar page when button clicked
        Button viewCalendar = (Button) findViewById(R.id.myCalendar);
        viewCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(MainActivity.this, ViewCalendar.class);
                Intent.putExtra("USERNAME", username);
                startActivity(Intent);
            }
        });

    }

    // This method gets called after background.java finishes
    @Override
    public void updateResult(String result){
        //System.out.println("I'm in the updateResult method!!!!!");
        System.out.println("The result is: "+result);
        //displayGroups(usersGroupsIDs,result);
    }

    public void displayGroups(ArrayList<String> usersGroupsIDs, String result) {
        result = result.substring(0, result.length() - 1);      // Remove comma from end of string (perhaps redundant but oh well)
        usersGroupsIDs = new ArrayList<String>(Arrays.asList(result.split("\\s*,\\s*")));

        // Get id of radio group
        final RadioGroup rg = (RadioGroup) findViewById(R.id.myRadioGroup);

        // Populate radio group with groups by looping over ArrayList usersGroups
        for(int i=0;i<usersGroupsIDs.size();i++){

            // Get name of group to add
            String groupName = usersGroupsIDs.get(i);

            // Initialize a new RadioButton
            RadioButton radioButton = new RadioButton(getApplicationContext());

            // Set padding of radio button
            radioButton.setPadding(28,0,0,0);

            // Set text of radio button
            radioButton.setTextAppearance(MainActivity.this, android.R.style.TextAppearance_DeviceDefault_Medium);
            radioButton.setText(groupName);

            radioButton.setButtonDrawable(R.drawable.radio_button_selector);

            System.out.println("I think I'm adding a radio button that says "+groupName);
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
                    String groupID = rb.getText().toString();
                    if(radioButtonId!=-1) {
                        Intent Intent = new Intent(MainActivity.this, ViewGroup.class);
                        Intent.putExtra("GROUP_ID", groupID);
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
    }
}