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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        // Get username of person logged in
        final String username = getIntent().getStringExtra("USERNAME");

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

                //todo make it so that you can take a user out of the group when you click it in the scrollview at the top


                // Can't remove name1 from group as that is the user who is logged in
                name2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Remove name from addedUsers arrayList
                        //addedUsers.remove(addedUsers.indexOf(name2.getText()));   //todo this doesn't work

                        // Remove name from listView
                        name2.setText("");

                        // Subtract one from addedUsersCounter
                        userAddedCounter--;
                    }
                });
            }
        });

        Button createGroupBtn = (Button) findViewById(R.id.makeGroup);
        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create group in groups table
                EditText groupName = (EditText)findViewById(R.id.edittext_groupname);
                String strGroupName = groupName.getText().toString().trim();

                if(!strGroupName.equals("") && addedUsers.size()>=2) {
                    //db.addGroup(strGroupName, addedUsers.size());
                    String strNoUsers = Integer.toString(addedUsers.size());
                    background bg2 = new background(CreateGroup.this);
                    bg2.execute("groupName","noUsers",strGroupName,strNoUsers,"createGroup");


                    // Get ID of group that has been just created (since it is set as an autoincrement field in userGroups table)
                    background bg3 = new background(CreateGroup.this);
                    bg3.execute("groupName", "blank", strGroupName, "blank","getGroupID");


                }else if(strGroupName.equals("")){
                    Toast.makeText(CreateGroup.this, "Please enter a group name", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CreateGroup.this, "Group cannot have only one user", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void addUserGroupsLink(String groupID){
        System.out.println(" GROUP ID IS: "+groupID);

        // Would use String.join but requires min api26 which I can't use for my conn to work
        StringBuilder nameBuilder = new StringBuilder();
        for (String n : addedUsers) {
            nameBuilder.append("'").append(n.replace("'", "\\'")).append("',");
        }
        nameBuilder.deleteCharAt(nameBuilder.length() - 1);
        String strAddedUsers= nameBuilder.toString();

        System.out.println(" THE ADDEDUSERS STRING IS: "+strAddedUsers);
        background bg4 = new background(CreateGroup.this);
        bg4.execute("addedUsers", "groupID", strAddedUsers, groupID, "addUserGroupLinks");

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

        if(result.contains("CREATEGROUP")) {
            // This call was from bg2
        }else if(result.contains("GETGROUPID")){
            // This call was from bg3
            result = result.substring(10, result.length());
            addUserGroupsLink(result);
        }else{
            // Goes in here if db lookup returned a value i.e. this call was from bg not bg2
            result = result.substring(0, result.length() - 1);      // Remove comma from end of string (perhaps redundant but oh well)
            users = new ArrayList<String>(Arrays.asList(result.split("\\s*,\\s*")));

            ListView lv = (ListView) findViewById(R.id.userListView);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
            lv.setAdapter(arrayAdapter);
        }
    }
}