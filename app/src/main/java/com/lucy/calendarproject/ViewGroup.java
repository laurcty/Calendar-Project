package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.List;

public class ViewGroup extends AppCompatActivity {


    // Set up decorator reference so that it can be removed later
    private EventDecorator decoratorReference;

    // Set up array of event decorator references so that they can be used to remove correct decorator
    private EventDecorator[] decoratorArray = new EventDecorator[32];

    // Set up array list to store days to put dots under
    final List<CalendarDay> calendarDays = new ArrayList<>();


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

        // Add names of users in group to scrollView at top and save their calendars in strings
        for(int i=0;i<usersInGroup.size();i++) {
            if (i == 0) {
                name1.setText(" " + usersInGroup.get(i) + " ");
                int userID1 = db.getUserID(usersInGroup.get(i));
                String dates1 = db.getCalendarDates(userID1);
                addDecorators(dates1);
            } else if (i == 1) {
                name2.setText(" " + usersInGroup.get(i) + " ");
                int userID2 = db.getUserID(usersInGroup.get(i));
                String dates2 = db.getCalendarDates(userID2);
                addDecorators(dates2);
            } else if (i == 2) {
                name3.setText(" " + usersInGroup.get(i) + " ");
                int userID3 = db.getUserID(usersInGroup.get(i));
                String dates3 = db.getCalendarDates(userID3);
                addDecorators(dates3);
            } else if (i == 3) {
                name4.setText(" " + usersInGroup.get(i) + " ");
                int userID4 = db.getUserID(usersInGroup.get(i));
                String dates4 = db.getCalendarDates(userID4);
                addDecorators(dates4);
            } else if (i == 4) {
                name5.setText(" " + usersInGroup.get(i) + " ");
                int userID5 = db.getUserID(usersInGroup.get(i));
                String dates5 = db.getCalendarDates(userID5);
                addDecorators(dates5);
            } else if (i == 5) {
                name6.setText(" " + usersInGroup.get(i) + " ");
                int userID6 = db.getUserID(usersInGroup.get(i));
                String dates6 = db.getCalendarDates(userID6);
                addDecorators(dates6);
            } else if (i == 6) {
                name7.setText(" " + usersInGroup.get(i) + " ");
                int userID7 = db.getUserID(usersInGroup.get(i));
                String dates7 = db.getCalendarDates(userID7);
                addDecorators(dates7);
            } else if (i == 7) {
                name8.setText(" " + usersInGroup.get(i) + " ");
                int userID8 = db.getUserID(usersInGroup.get(i));
                String dates8 = db.getCalendarDates(userID8);
                addDecorators(dates8);
            }
        }


        //todo make findDate button do smth idk go nuts

    }

    public void addDecorators(String calendarDates){

        // Get id of calendarView
        final MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);

        try {
            String[] datesInCalendar = calendarDates.split(",");


            for(int i=0; i<datesInCalendar.length; i++) {

                String[] DMY = datesInCalendar[i].split("-");
                int year = Integer.parseInt(DMY[2]);
                int month = Integer.parseInt(DMY[1]);
                int day = Integer.parseInt(DMY[0]);
                CalendarDay date = CalendarDay.from(year, month - 1, day);

                calendarDays.add(date);
                decoratorReference = new EventDecorator(Color.rgb(143, 209, 219), calendarDays);
                decoratorArray[day] = decoratorReference;
                calendar.addDecorator(decoratorReference);      // todo make decorators different colours to match users
                calendar.invalidateDecorators();
            }

        }catch(Exception e){
            // User doesn't have any dates yet
        }
    }

}
