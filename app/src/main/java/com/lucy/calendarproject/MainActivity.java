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
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncTaskListener{

    ArrayList<String> groupIDs = new ArrayList<String>();
    ArrayList<String> groupNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get username from intent of activity that called MainActivity
        final String username = getIntent().getStringExtra("USERNAME");

        // Create background instance to retrieve which groups the user belongs to
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

        // Display hint when button clicked
        Button helpButton = (Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Select a group and click VIEW GROUP to find a date to meet up. Click on MY CALENDAR to input when you are busy.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // This method gets called after background.java finishes
    @Override
    public void updateResult(String result){
        if(result.contains("fgou")) {      // "fgou" (FindGroupsOfUser) means this call is returning the groups which the user belongs to
            if(result.contains("CONNECTION ERROR")||result.contains("Failed to connect")){
                Toast.makeText(MainActivity.this, "Error connecting to server, please try again", Toast.LENGTH_SHORT).show();
            }else {
                // Get groups of user from string into array
                System.out.println("result of fgou is: " + result);
                String groupIDsString = result.substring(4, result.length() - 1);
                System.out.println("result after manipulating is: " + groupIDsString);
                groupIDs = new ArrayList<String>(Arrays.asList(groupIDsString.split("\\s*,\\s*")));

                background bg2 = new background(MainActivity.this);
                bg2.execute("groupIDs", "blank", groupIDsString, "blank", "getGroupNames");
            }

        }else{      // This call is returning the groupNames of the groups the user belongs to
            System.out.println("Result of getGroupNames is:"+result);
            String groupNamesStr = result.substring(0,result.length()-1);
            if(groupNamesStr.contains("error")){
                groupNamesStr = null;
            }
            System.out.println("groupNamesStr is: "+groupNamesStr);

            try {
                groupNames = new ArrayList<String>(Arrays.asList(groupNamesStr.split("\\s*,\\s*")));
                displayGroups(groupNames);
            } catch (Exception e) {
                // User doesn't have any groups yet
                Toast.makeText(MainActivity.this, "You don't have any groups yet- click on CREATE GROUP to get started.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void displayGroups(ArrayList<String> retrievedGroupNames) {

        // Get id of radio group
        final RadioGroup rg = (RadioGroup) findViewById(R.id.myRadioGroup);

        // Populate radio group with groups by looping over List retrievedGroupNames
        for(int i=0;i<retrievedGroupNames.size();i++){

            // Get name of group to add
            String groupName = retrievedGroupNames.get(i);

            // Initialize a new RadioButton, set padding, text, and image, and add the button to the radioGroup
            RadioButton radioButton = new RadioButton(getApplicationContext());
            radioButton.setPadding(28,0,0,0);
            radioButton.setTextAppearance(MainActivity.this, android.R.style.TextAppearance_DeviceDefault_Medium);
            radioButton.setText(groupName);
            radioButton.setButtonDrawable(R.drawable.radio_button_selector);
            rg.addView(radioButton);
        }

        // Change to viewGroup page when button clicked
        Button viewGroup = (Button) findViewById(R.id.viewGroup);
        viewGroup.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int radioButtonId = rg.getCheckedRadioButtonId();
                System.out.println("The radio button id is: "+radioButtonId);
                try {
                    // Get the ID of the group selected by the user and pass group info to ViewGroup
                    String groupName = groupNames.get(radioButtonId-1);
                    Intent Intent = new Intent(MainActivity.this, ViewGroup.class);
                    Intent.putExtra("GROUP_NAME", groupName);
                    String selectedGroupID = groupIDs.get(radioButtonId-1);
                    Intent.putExtra("GROUPID", selectedGroupID);
                    startActivity(Intent);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "No group selected", Toast.LENGTH_SHORT).show();
                    System.out.println("the error is " +e);
                }
            }
        });
    }
}