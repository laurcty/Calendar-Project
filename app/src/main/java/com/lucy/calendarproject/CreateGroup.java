package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
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
                DatabaseHelper db = new DatabaseHelper(CreateGroup.this);
                EditText groupName = (EditText)findViewById(R.id.edittext_groupname);
                String strGroupName = groupName.getText().toString().trim();
                Boolean free = db.checkGroupFree(strGroupName);

                if(!strGroupName.equals("") && addedUsers.size()>=2 && free) {
                    db.addGroup(strGroupName, addedUsers.size());

                    // Get ID of group that has been just created (since it is set as an autoincrement field in userGroups table)
                    int createdGroupID = db.getGroupID(strGroupName);

                    String addedUserName;
                    int addedUserID = 0;
                    for (int i = 0; i < addedUsers.size(); i++) {
                        addedUserName = addedUsers.get(i);
                        addedUserID = db.getUserID(addedUserName);
                        db.addUserGroupLink(addedUserID, createdGroupID);
                    }

                    // Switch back to main activity
                    Intent Intent = new Intent(CreateGroup.this, MainActivity.class);
                    Intent.putExtra("USERNAME", username);
                    startActivity(Intent);

                }else if(strGroupName.equals("")){
                    Toast.makeText(CreateGroup.this, "Please enter a group name", Toast.LENGTH_SHORT).show();
                }else if(!free){
                    Toast.makeText(CreateGroup.this, "Group name is taken", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CreateGroup.this, "Group cannot have only one user", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // This method gets called after background.java finishes
    @Override
    public void updateResult(String result){
        System.out.println("I'm in the updateResult method!!!!!");
        result = result.substring(0, result.length() - 1);      // Remove comma from end of string (perhaps redundant but oh well)
        users = new ArrayList<String>(Arrays.asList(result.split("\\s*,\\s*")));

        ListView lv = (ListView) findViewById(R.id.userListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        lv.setAdapter(arrayAdapter);
    }
}
