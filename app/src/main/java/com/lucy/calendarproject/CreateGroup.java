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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateGroup extends AppCompatActivity {
    ArrayList<String> users=new ArrayList<String>();
    int userAddedCounter =0;
    ArrayList<String> addedUsers=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        openAndQueryDatabase();
        displayResultList();

        final TextView name1 = (TextView) findViewById(R.id.name1);
        final TextView name2 = (TextView) findViewById(R.id.name2);
        final TextView name3 = (TextView) findViewById(R.id.name3);
        final TextView name4 = (TextView) findViewById(R.id.name4);
        final TextView name5 = (TextView) findViewById(R.id.name5);
        final TextView name6 = (TextView) findViewById(R.id.name6);
        final TextView name7 = (TextView) findViewById(R.id.name7);
        final TextView name8 = (TextView) findViewById(R.id.name8);


        final ListView listView = (ListView) findViewById(R.id.userListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                String selectedUser = o.toString();


                //validate to ensure same user only added to group once
                if(!addedUsers.contains(selectedUser)) {
                    addedUsers.add(selectedUser);
                    //Toast.makeText(CreateGroup.this, selectedUser, Toast.LENGTH_SHORT).show();
                    userAddedCounter++;
                    if (userAddedCounter == 1) {
                        name1.setText(" " + selectedUser + " ");        //should make this one user who is logged in so that they will always be first in group
                    } else if (userAddedCounter == 2) {
                        name2.setText(" " + selectedUser + " ");
                    } else if (userAddedCounter == 3) {
                        name3.setText(" " + selectedUser + " ");
                    } else if (userAddedCounter == 4) {
                        name4.setText(" " + selectedUser + " ");
                    } else if (userAddedCounter == 5) {
                        name5.setText(" " + selectedUser + " ");
                    } else if (userAddedCounter == 6) {
                        name6.setText(" " + selectedUser + " ");
                    } else if (userAddedCounter == 7) {
                        name7.setText(" " + selectedUser + " ");
                    } else if (userAddedCounter == 8) {
                        name8.setText(" " + selectedUser + " ");
                    } else {
                        Toast.makeText(CreateGroup.this, "Maximim of 8 users can be added", Toast.LENGTH_SHORT).show();
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
                //use addedUsers arrayList to put all users into group


            }
        });


    }

    private void openAndQueryDatabase() {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
            SQLiteDatabase newDB = dbHelper.getWritableDatabase();
            Cursor c = newDB.rawQuery("SELECT username FROM " +
                    "users" +
                    " where ID>0", null);

            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                        String user = c.getString(c.getColumnIndex("username"));
                        users.add(user);
                        //don't add user if user = username of person logged in
                    }while (c.moveToNext());
                }
            }
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }

    }

    private void displayResultList() {
        ListView lv = (ListView) findViewById(R.id.userListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                users );

        lv.setAdapter(arrayAdapter);
    }
}
