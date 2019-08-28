package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_MULTIPLE;

public class ViewGroup extends AppCompatActivity {

    // Set up global variables to store users and their calendars
    String dates1;
    String dates2;
    String dates3;
    String dates4;
    String dates5;
    String dates6;
    String dates7;
    String dates8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group);

        // Get IDs of names in scrollView
        final TextView name1 = (TextView) findViewById(R.id.viewGroupName1);
        final TextView name2 = (TextView) findViewById(R.id.viewGroupName2);
        final TextView name3 = (TextView) findViewById(R.id.viewGroupName3);
        final TextView name4 = (TextView) findViewById(R.id.viewGroupName4);
        final TextView name5 = (TextView) findViewById(R.id.viewGroupName5);
        final TextView name6 = (TextView) findViewById(R.id.viewGroupName6);
        final TextView name7 = (TextView) findViewById(R.id.viewGroupName7);
        final TextView name8 = (TextView) findViewById(R.id.viewGroupName8);


        // Get the ID (corresponding to position in radioGroup) of the chosen group
        String groupName = getIntent().getStringExtra("NAME_OF_GROUP");
        TextView groupIDText = (TextView) findViewById(R.id.GroupID);
        groupIDText.setText("Group: " + groupName);

        // Get ID of group and populate arrayList with users in the group
        DatabaseHelper db = new DatabaseHelper(ViewGroup.this);
        int groupID = db.getGroupID(groupName);
        ArrayList<String> usersInGroup = new ArrayList<String>();
        usersInGroup= db.getUsersInGroup(groupID);



        // Add names of users in group to scrollView at top and save their calendars in strings
        for(int i=0;i<usersInGroup.size();i++) {
            if (i == 0) {
                name1.setText(" " + usersInGroup.get(i) + " ");
                int userID1 = db.getUserID(usersInGroup.get(i));
                dates1 = db.getCalendarDates(userID1);
            } else if (i == 1) {
                name2.setText(" " + usersInGroup.get(i) + " ");
                int userID2 = db.getUserID(usersInGroup.get(i));
                dates2 = db.getCalendarDates(userID2);
            } else if (i == 2) {
                name3.setText(" " + usersInGroup.get(i) + " ");
                int userID3 = db.getUserID(usersInGroup.get(i));
                dates3 = db.getCalendarDates(userID3);
            } else if (i == 3) {
                name4.setText(" " + usersInGroup.get(i) + " ");
                int userID4 = db.getUserID(usersInGroup.get(i));
                dates4 = db.getCalendarDates(userID4);
            } else if (i == 4) {
                name5.setText(" " + usersInGroup.get(i) + " ");
                int userID5 = db.getUserID(usersInGroup.get(i));
                dates5 = db.getCalendarDates(userID5);
            } else if (i == 5) {
                name6.setText(" " + usersInGroup.get(i) + " ");
                int userID6 = db.getUserID(usersInGroup.get(i));
                dates6 = db.getCalendarDates(userID6);
            } else if (i == 6) {
                name7.setText(" " + usersInGroup.get(i) + " ");
                int userID7 = db.getUserID(usersInGroup.get(i));
                dates7 = db.getCalendarDates(userID7);
            } else if (i == 7) {
                name8.setText(" " + usersInGroup.get(i) + " ");
                int userID8 = db.getUserID(usersInGroup.get(i));
                dates8 = db.getCalendarDates(userID8);
            }
        }


        addCustomDecorators(usersInGroup.size());


        // Create popup window when findDate button pressed
        Button findDate = (Button) findViewById(R.id.findDate);
        findDate.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Inflate layout
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                // Create instance of popupWindow
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true;       // Means popup disappears when you tap outside it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                getBestDate();

                // Change textViews
                TextView popupTitle = (TextView) popupWindow.getContentView().findViewById(R.id.popupTitle);
                popupTitle.setText("Best dates:");

                // Show popupWindow
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, -400);

            }
        });

    }

    public String getBestDate(){
        String bestDate="";

        // todo need to make this function do smth and display best dates

        return bestDate;
    }


    public void addCustomDecorators(int noUsersInGroup){
        // Get id of calendarView
        final MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendar.setSelectionMode(SELECTION_MODE_MULTIPLE);


        // Set up hashMap to map all dates in calendars to how many decorators they should have
        HashMap<CalendarDay, Integer> hashMap = new HashMap<CalendarDay, Integer>();
        for(int i=0;i<noUsersInGroup;i++) {
            if(i==0) {
                setUpHashMap(hashMap, i , dates1);
            }else if(i==1){
                setUpHashMap(hashMap, i , dates2);
            }else if(i==2){
                setUpHashMap(hashMap, i , dates3);
            }else if(i==3){
                setUpHashMap(hashMap, i , dates4);
            } else if(i==4){
                setUpHashMap(hashMap, i , dates5);
            } else if(i==5){
                setUpHashMap(hashMap, i , dates6);
            }else if(i==6){
                setUpHashMap(hashMap, i , dates7);
            }else if(i==7){
                setUpHashMap(hashMap, i , dates8);
            }
        }


        // Loop over all entries in hashMap to add the corresponding number of decorators to them

        for(Map.Entry<CalendarDay,Integer> entry : hashMap.entrySet()){


            CalendarDay currDay = entry.getKey();
            Integer currDayCount = entry.getValue();

            // Convert to stored date format to check against users' calendars
            int day = currDay.getDay();
            int month = currDay.getMonth();
            int year = currDay.getYear();
            String formattedDate = day + "-" + (month + 1) + "-" + year;

            // Find which users have this date in their calendars and collect in arrayList
            ArrayList<Integer> usersWithThisDate =new ArrayList<Integer>();
            try {
                if (dates1.contains(formattedDate)) {
                    usersWithThisDate.add(1);
                }
            }catch(Exception e){
                // User has no dates in their calendar yet
            }
            try {
                if(dates2.contains(formattedDate)) {
                    usersWithThisDate.add(2);
                }
            }catch(Exception e){
                // User has no dates in their calendar yet
            }
            try {
                if(dates3.contains(formattedDate)) {
                    usersWithThisDate.add(3);
                }
            }catch(Exception e){
                // User has no dates in their calendar yet
            }
            try {
                if(dates4.contains(formattedDate)){
                    usersWithThisDate.add(4);
                }
            }catch(Exception e){
                // User has no dates in their calendar yet
            }
            try {
                if(dates5.contains(formattedDate)){
                    usersWithThisDate.add(5);
                }
            }catch(Exception e){
                // User has no dates in their calendar yet
            }
            try {
                if(dates6.contains(formattedDate)){
                    usersWithThisDate.add(6);
                }
            }catch(Exception e){
                // User has no dates in their calendar yet
            }
            try {
                if(dates7.contains(formattedDate)){
                    usersWithThisDate.add(7);
                }
            }catch(Exception e){
                // User has no dates in their calendar yet
            }
            try {
                if(dates8.contains(formattedDate)){
                    usersWithThisDate.add(8);
                }
            }catch(Exception e){
                // User has no dates in their calendar yet
            }

            CustomEventDecorator[] decoratorArray= setUpDecorators(usersWithThisDate, currDayCount);

            int counter=0;
            for(int j = 0; j<currDayCount; j++)
                try {
                    decoratorArray[counter].addDate(currDay);
                    counter++;
                }catch(Exception e){
                    // currDay is null
                }
            try {
                calendar.addDecorators(decoratorArray);
                calendar.invalidateDecorators();
            }catch(Exception e){
                // decoratorArray is null
            }
        }

    }



    public CustomEventDecorator[] setUpDecorators(ArrayList<Integer> usersWithThisDate, int currDayCount){
        CustomEventDecorator[] decoratorArray = new CustomEventDecorator[usersWithThisDate.size()];


        // Add decorators to decoratorArray if they should be added i.e. if that colour user has the date in their calendar

        // Use index as a counter to determine which spanType is used upon decorator creation (x position of the decorator)
        int index=0;
        int xSpanType=0;
        int ySpanType=0;
        if (usersWithThisDate.contains(1)) {
            decoratorArray[index] = new CustomEventDecorator(Color.rgb(130, 192 , 169), 12, xSpanType, ySpanType);
            index++;
            xSpanType++;
        }
        if (usersWithThisDate.contains(2)) {
            decoratorArray[index] = new CustomEventDecorator(Color.rgb(130, 155, 192), 12, xSpanType, ySpanType);
            index++;
            xSpanType++;
        }
        if (usersWithThisDate.contains(3)) {
            decoratorArray[index] = new CustomEventDecorator(Color.rgb(156, 130 , 192), 12, xSpanType, ySpanType);
            index++;
            xSpanType++;
        }
        if (usersWithThisDate.contains(4)) {
            decoratorArray[index] = new CustomEventDecorator(Color.rgb(192, 130 , 171), 12, xSpanType, ySpanType);
            index++;
            xSpanType++;
        }
        if(xSpanType==4){
            xSpanType=0;
            ySpanType=1;
        }
        if (usersWithThisDate.contains(5)) {
            decoratorArray[index] = new CustomEventDecorator(Color.rgb(192, 130 , 130), 12, xSpanType, ySpanType);
            index++;
            xSpanType++;
        }
        if(xSpanType==4){
            xSpanType=0;
            ySpanType=1;
        }
        if (usersWithThisDate.contains(6)) {
            decoratorArray[index] = new CustomEventDecorator(Color.rgb(192, 155 , 130), 12, xSpanType, ySpanType);
            index++;
            xSpanType++;
        }
        if(xSpanType==4){
            xSpanType=0;
            ySpanType=1;
        }
        if (usersWithThisDate.contains(7)) {
            decoratorArray[index] = new CustomEventDecorator(Color.rgb(192, 189 , 130), 12, xSpanType, ySpanType);
            index++;
            xSpanType++;
        }
        if(xSpanType==4){
            xSpanType=0;
            ySpanType=1;
        }
        if (usersWithThisDate.contains(8)) {
            decoratorArray[index] = new CustomEventDecorator(Color.rgb(156, 192 , 130), 12, xSpanType, ySpanType);
        }
        return decoratorArray;
    }



    public void setUpHashMap(HashMap<CalendarDay, Integer> hashMap, int counter, String dates){
        // Split user's calendar into CalendarDays
        try {
            String[] userDates = dates.split(",");

            for (int j = 0; j < userDates.length; j++) {
                // If date already in hashMap, increment number mapped to that date, if not add new entry of date and map to number one

                String[] DMY = userDates[j].split("-");
                int year = Integer.parseInt(DMY[2]);
                int month = Integer.parseInt(DMY[1]);
                int day = Integer.parseInt(DMY[0]);
                CalendarDay date = CalendarDay.from(year, month - 1, day);

                hashMap.putIfAbsent(date, 0);
                hashMap.put(date, hashMap.get(date) + 1);
            }
        }catch(Exception e){
            // User has no dates in calendar
        }
    }

}
