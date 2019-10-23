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

public class MainActivity extends AppCompatActivity implements AsyncTaskListener{

    //todo
    //      add text to create group "click on usernames to add them to your group".
    //      make the popup window make sense cause it doesn't
    //      text in view group to say select a range of dates before clicking view dates

    ArrayList<String> usersGroupsIDs = new ArrayList<String>();
    ArrayList<String> groupNames = new ArrayList<String>();
    ArrayList<String> groupNamesAndOwners = new ArrayList<String>();

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
    }

    // This method gets called after background.java finishes
    @Override
    public void updateResult(String result){
        if(result.contains("fgou")) {      // "fgou" (FindGroupsOfUser) means this call is returning the groups which the user belongs to
            // Get groups of user from string into array
            result = result.substring(4, result.length() - 1);
            usersGroupsIDs = new ArrayList<String>(Arrays.asList(result.split("\\s*,\\s*")));   //todo change name of this cause I think it's group name/groupCreator not ID
            String groupIDsString = usersGroupsIDs.toString();
            groupIDsString = groupIDsString.substring(1, groupIDsString.length() - 1);
            groupIDsString = groupIDsString.replaceAll("\\s+", "");

            groupNamesAndOwners = new ArrayList<String>(Arrays.asList(groupIDsString.split("\\s*,\\s*")));
            try {
                for (int i = 0; i <= groupNamesAndOwners.size() - 1; i++) {
                    // Loop over all groupNamesAndOwners and add names of groups to groupNames arrayList
                    String groupNameAndOwner = groupNamesAndOwners.get(i);
                    String groupName = groupNameAndOwner.substring(0, groupNameAndOwner.indexOf("-"));
                    groupNames.add(groupName);
                }
                displayGroups(groupNames, groupIDsString);
            } catch (Exception e) {
                // User doesn't have any groups yet
                Toast.makeText(MainActivity.this, "You don't have any groups yet- click on CREATE GROUP to get started.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void displayGroups(ArrayList<String> retrievedGroupNames, String result) {      //todo take out result it's redundant

        // Get id of radio group
        final RadioGroup rg = (RadioGroup) findViewById(R.id.myRadioGroup);

        // Populate radio group with groups by looping over ArrayList retrievedGroupNames
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
                try {
                    // Get the ID of the group selected by the user and pass group info to ViewGroup
                    String groupName = groupNames.get(radioButtonId-1);
                    Intent Intent = new Intent(MainActivity.this, ViewGroup.class);
                    Intent.putExtra("GROUP_NAME", groupName);
                    String selectedGroupNameAndOwner = groupNamesAndOwners.get(radioButtonId-1);
                    Intent.putExtra("GROUP_NAME_AND_OWNER", selectedGroupNameAndOwner);
                    startActivity(Intent);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "No group selected", Toast.LENGTH_SHORT).show();
                    System.out.println("the error is " +e);
                }
            }
        });
    }
}