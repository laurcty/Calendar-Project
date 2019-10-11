package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateGroup extends AppCompatActivity implements AsyncTaskListener {
    ArrayList<String> users=new ArrayList<String>();
    int userAddedCounter =1;
    ArrayList<String> addedUsers=new ArrayList<String>();
    String username;
    String strGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        // Get username of person logged in
        username = getIntent().getStringExtra("USERNAME");

        background bg = new background(CreateGroup.this);
        bg.execute("blank","blank","blank","blank","getAllUsers");      // Blanks due to no vars needing to be passed

        final TextView name1 = (TextView) findViewById(R.id.name1);
        final TextView name2 = (TextView) findViewById(R.id.name2);
        final TextView name3 = (TextView) findViewById(R.id.name3);
        final TextView name4 = (TextView) findViewById(R.id.name4);
        final TextView name5 = (TextView) findViewById(R.id.name5);
        final TextView name6 = (TextView) findViewById(R.id.name6);
        final TextView name7 = (TextView) findViewById(R.id.name7);
        final TextView name8 = (TextView) findViewById(R.id.name8);

        // First add user who is logged in
        name1.setText(" " + username + " ");
        addedUsers.add(username);

        final ListView listView = (ListView) findViewById(R.id.userListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                String selectedUser = o.toString();

                // Validate to ensure same user only added to group once
                if(!addedUsers.contains(selectedUser)) {
                    userAddedCounter++;
                    if (userAddedCounter == 1) {
                        // Do nothing because first user has already been added
                    } else if (userAddedCounter == 2) {
                        name2.setText(" " + selectedUser + " ");
                        addedUsers.add(selectedUser);
                    } else if (userAddedCounter == 3) {
                        name3.setText(" " + selectedUser + " ");
                        addedUsers.add(selectedUser);
                    } else if (userAddedCounter == 4) {
                        name4.setText(" " + selectedUser + " ");
                        addedUsers.add(selectedUser);
                    } else if (userAddedCounter == 5) {
                        name5.setText(" " + selectedUser + " ");
                        addedUsers.add(selectedUser);
                    } else if (userAddedCounter == 6) {
                        name6.setText(" " + selectedUser + " ");
                        addedUsers.add(selectedUser);
                    } else if (userAddedCounter == 7) {
                        name7.setText(" " + selectedUser + " ");
                        addedUsers.add(selectedUser);
                    } else if (userAddedCounter == 8) {
                        name8.setText(" " + selectedUser + " ");
                        addedUsers.add(selectedUser);
                    } else {
                        Toast.makeText(CreateGroup.this, "Maximum of 8 users can be added", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CreateGroup.this, "User cannot be added twice", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button createGroupBtn = (Button) findViewById(R.id.makeGroup);
        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create group in groups table
                EditText groupName = (EditText)findViewById(R.id.edittext_groupname);
                strGroupName = groupName.getText().toString().trim();
                background bg3 = new background(CreateGroup.this);
                bg3.execute("groupName","groupOwner",strGroupName,username,"checkGroupName");

            }
        });
    }

    public void createGroup(boolean groupNameUnique){
        if(!strGroupName.equals("") && addedUsers.size()>=2 && strGroupName.length()<=15 && groupNameUnique) {
            background bg2 = new background(CreateGroup.this);
            bg2.execute("groupName","groupOwner",strGroupName,username,"createGroup");
        }else if(strGroupName.equals("")) {
            Toast.makeText(CreateGroup.this, "Please enter a group name", Toast.LENGTH_SHORT).show();
        }else if(strGroupName.length()>=16) {
            Toast.makeText(CreateGroup.this, "Group name must be less than 16 characters", Toast.LENGTH_SHORT).show();
        }else if(!groupNameUnique){
            Toast.makeText(CreateGroup.this, "You have already created a group with this name. Please choose another", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(CreateGroup.this, "Group cannot have only one user", Toast.LENGTH_SHORT).show();
        }
    }

    public void addUserGroupsLink(){

        // Would use String.join but requires min api26 which I can't use for my conn to work
        StringBuilder nameBuilder = new StringBuilder();
        for (String n : addedUsers) {
            nameBuilder.append("'").append(n.replace("'", "\\'")).append("',");
        }
        nameBuilder.deleteCharAt(nameBuilder.length() - 1);
        String strAddedUsers= nameBuilder.toString();

        System.out.println(" THE ADDEDUSERS STRING IS: "+strAddedUsers);
        background bg4 = new background(CreateGroup.this);
        String groupNameAndOwner = strGroupName + "-" + username;
        bg4.execute("addedUsers", "groupNameAndOwner", strAddedUsers, groupNameAndOwner, "addUserGroupLinks");

        // Switch back to main activity
        Intent Intent = new Intent(CreateGroup.this, MainActivity.class);
        final String username = getIntent().getStringExtra("USERNAME");
        Intent.putExtra("USERNAME", username);
        startActivity(Intent);
    }

    // This method gets called after background.java finishes
    @Override
    public void updateResult(String result){
        System.out.println("I'm in the updateResult method!!!!!");
        System.out.println("The result of CreateGroup thing is "+result);

        if(result.contains("CREATEGROUP")){
            // This call was from bg2
            addUserGroupsLink();
        }else if(result.contains("groupNameInvalid")){
            // This call was from bg3 and name of group is not valid
            boolean groupNameValid = false;
            createGroup(groupNameValid);
        }else if(result.contains("groupNameValid")){
            // This call was from bg3 and name of group is valid
            boolean groupNameValid = true;
            createGroup(groupNameValid);
        }else{
            // Goes in here if db lookup returned a value i.e. this call was from bg not bg2
            result = result.substring(0, result.length() - 1);      // Remove comma from end of string (perhaps redundant but oh well)
            users = new ArrayList<String>(Arrays.asList(result.split("\\s*,\\s*")));
            users.remove(username);

            ListView lv = (ListView) findViewById(R.id.userListView);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
            lv.setAdapter(arrayAdapter);
        }
    }
}