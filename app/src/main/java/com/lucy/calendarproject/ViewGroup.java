package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group);

        //get IDs of names in scrollView
        final TextView name1 = (TextView) findViewById(R.id.viewGroupName1);
        final TextView name2 = (TextView) findViewById(R.id.viewGroupName2);
        final TextView name3 = (TextView) findViewById(R.id.viewGroupName3);
        final TextView name4 = (TextView) findViewById(R.id.viewGroupName4);
        final TextView name5 = (TextView) findViewById(R.id.viewGroupName5);
        final TextView name6 = (TextView) findViewById(R.id.viewGroupName6);
        final TextView name7 = (TextView) findViewById(R.id.viewGroupName7);
        final TextView name8 = (TextView) findViewById(R.id.viewGroupName8);


        //get the ID (corresponding to position in radioGroup) of the chosen group
        String groupName = getIntent().getStringExtra("NAME_OF_GROUP");
        TextView groupIDText = (TextView) findViewById(R.id.GroupID);
        groupIDText.setText("Group: " + groupName);

        //get ID of group and populate arrayList with users in the group
        DatabaseHelper db = new DatabaseHelper(ViewGroup.this);
        int groupID = db.getGroupID(groupName);
        ArrayList<String> usersInGroup = new ArrayList<String>();
        usersInGroup= db.getUsersInGroup(groupID);

        //add names of users in group to scrollView at top
        for(int i=0;i<usersInGroup.size();i++) {
            if (i == 0) {
                name1.setText(" " + usersInGroup.get(i) + " ");
            } else if (i == 1) {
                name2.setText(" " + usersInGroup.get(i) + " ");
            } else if (i == 2) {
                name3.setText(" " + usersInGroup.get(i) + " ");
            } else if (i == 3) {
                name4.setText(" " + usersInGroup.get(i) + " ");
            } else if (i == 4) {
                name5.setText(" " + usersInGroup.get(i) + " ");
            } else if (i == 5) {
                name6.setText(" " + usersInGroup.get(i) + " ");
            } else if (i == 6) {
                name7.setText(" " + usersInGroup.get(i) + " ");
            } else if (i == 7) {
                name8.setText(" " + usersInGroup.get(i) + " ");
            }
        }

        //todo get calendars of people in group and decorate calendar

        //todo make findDate button do smth idk go nuts

    }
}
