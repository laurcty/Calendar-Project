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

    // Set up global variables to store users' calendars
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


        String allDates="";

        // Add names of users in group to scrollView at top and save their calendars in strings
        for(int i=0;i<usersInGroup.size();i++) {
            if (i == 0) {
                name1.setText(" " + usersInGroup.get(i) + " ");
                int userID1 = db.getUserID(usersInGroup.get(i));
                dates1 = db.getCalendarDates(userID1);
                allDates = allDates + "," + dates1;
                //addDecorators(dates1, 1);
            } else if (i == 1) {
                name2.setText(" " + usersInGroup.get(i) + " ");
                int userID2 = db.getUserID(usersInGroup.get(i));
                dates2 = db.getCalendarDates(userID2);
                allDates = allDates + "," + dates2;
                //addDecorators(dates2, 2);
            } else if (i == 2) {
                name3.setText(" " + usersInGroup.get(i) + " ");
                int userID3 = db.getUserID(usersInGroup.get(i));
                dates3 = db.getCalendarDates(userID3);
                allDates = allDates + "," + dates3;
                //addDecorators(dates3, 3);
            } else if (i == 3) {
                name4.setText(" " + usersInGroup.get(i) + " ");
                int userID4 = db.getUserID(usersInGroup.get(i));
                dates4 = db.getCalendarDates(userID4);
                allDates = allDates + "," + dates4;
                //addDecorators(dates4, 4);
            } else if (i == 4) {
                name5.setText(" " + usersInGroup.get(i) + " ");
                int userID5 = db.getUserID(usersInGroup.get(i));
                dates5 = db.getCalendarDates(userID5);
                allDates = allDates + "," + dates5;
                //addDecorators(dates5, 5);
            } else if (i == 5) {
                name6.setText(" " + usersInGroup.get(i) + " ");
                int userID6 = db.getUserID(usersInGroup.get(i));
                dates6 = db.getCalendarDates(userID6);
                allDates = allDates + "," + dates6;
                //addDecorators(dates6, 6);
            } else if (i == 6) {
                name7.setText(" " + usersInGroup.get(i) + " ");
                int userID7 = db.getUserID(usersInGroup.get(i));
                dates7 = db.getCalendarDates(userID7);
                allDates = allDates + "," + dates7;
                //addDecorators(dates7, 7);
            } else if (i == 7) {
                name8.setText(" " + usersInGroup.get(i) + " ");
                int userID8 = db.getUserID(usersInGroup.get(i));
                dates8 = db.getCalendarDates(userID8);
                allDates = allDates + "," + dates8;
                //addDecorators(dates8, 8);
            }
        }


        addCustomDecorators(usersInGroup.size());

        // Set up new string that's the same as allDates so that it can be declared final (to be used in onClick method)
        final String AllDates = allDates;

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
 
                String bestDate = getBestDate(AllDates);

                // Change textViews
                TextView popupTitle = (TextView) popupWindow.getContentView().findViewById(R.id.popupTitle);
                popupTitle.setText("Best dates:");

                // Show popupWindow
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, -400);

            }
        });

    }

    public String getBestDate(String allDates){
        String bestDate="";

        // Split allDates into an array with all of the dates in it
        String[] datesInCalendar = allDates.split(",");



        return bestDate;
    }


    public void addCustomDecorators(int noUsersInGroup){
        // Get id of calendarView
        final MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendar.setSelectionMode(SELECTION_MODE_MULTIPLE);


        /*        ----  Decorates day with up to 4 decorators, thinking of making new customSpan class for second row of decorators  ----        */

        CustomEventDecorator[] decoratorArray = new CustomEventDecorator[4]; //Max 4 dots
        for(int i = 0; i<decoratorArray.length; i++)
            if(i==0) {
                decoratorArray[i] = new CustomEventDecorator(Color.rgb(130, 192, 169), 12, i);
            }else if(i==1){
                decoratorArray[i] = new CustomEventDecorator(Color.rgb(130, 155, 192), 12, i);
            }else if(i==2){
                decoratorArray[i] = new CustomEventDecorator(Color.rgb(156, 130 , 192), 12, i);
            }else{
                decoratorArray[i] = new CustomEventDecorator(Color.rgb(192, 130 , 171), 12, i);
            }


        // Set up hashMap to map all dates in calendars to how many decorators they should have             // Todo figure out how to store which user has which date to sort out colours
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
            }

            /*else if(i==4){
                setUpHashMap(hashMap, i , dates5);
            }
            else if(i==5){
                setUpHashMap(hashMap, i , dates6);
            }else if(i==6){
                setUpHashMap(hashMap, i , dates7);
            }else if(i==7){
                setUpHashMap(hashMap, i , dates8);
            }
*/

        }



        // Loop over all entries in hashMap to add the corresponding number of decorators to them
        for(Map.Entry<CalendarDay,Integer> entry : hashMap.entrySet()){
            CalendarDay currDay = entry.getKey();
            Integer currDayCount = entry.getValue();
            for(int j = 0; j<currDayCount; j++)
                decoratorArray[j].addDate(currDay);
        }

        calendar.addDecorators(decoratorArray);
        calendar.invalidateDecorators();

    }



    public void setUpHashMap(HashMap<CalendarDay, Integer> hashMap, int counter, String dates){
        // Split user's calendar into CalendarDays
        String[] userDates = dates1.split(",");

        for(int j=0;j<userDates.length;j++){
            // If date already in hashMap, increment number mapped to that date, if not add new entry of date and map to number one

            String[] DMY = userDates[j].split("-");
            int year = Integer.parseInt(DMY[2]);
            int month = Integer.parseInt(DMY[1]);
            int day = Integer.parseInt(DMY[0]);
            CalendarDay date = CalendarDay.from(year, month - 1, day);

            hashMap.putIfAbsent(date,0);
            hashMap.put(date, hashMap.get(date)+1);
        }
    }


    //      This function will be deleted once addCustomDecorators is ready for business
/*
    public void addDecorators(String calendarDates, int user){



        // Get id of calendarView
        final MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendar.setSelectionMode(SELECTION_MODE_MULTIPLE);





        try {
            String[] datesInCalendar = calendarDates.split(",");

            for(int i=0; i<datesInCalendar.length; i++) {

                String[] DMY = datesInCalendar[i].split("-");
                int year = Integer.parseInt(DMY[2]);
                int month = Integer.parseInt(DMY[1]);
                int day = Integer.parseInt(DMY[0]);
                CalendarDay date = CalendarDay.from(year, month - 1, day);


                EventDecorator decoratorReference;
                if(user==1){
                    List<CalendarDay> calendarDays = new ArrayList<>();
                    calendarDays.add(date);
                    decoratorReference = new EventDecorator(Color.rgb(130, 192 , 169), calendarDays);
                    calendar.addDecorator(decoratorReference);
                    calendar.invalidateDecorators();
                }else if(user==2) {
                    List<CalendarDay> calendarDays2 = new ArrayList<>();
                    calendarDays2.add(date);
                    decoratorReference = new EventDecorator(Color.rgb(130, 155, 192), calendarDays2);
                    calendar.addDecorator(decoratorReference);
                    calendar.invalidateDecorators();
                }else if(user==3){
                    List<CalendarDay> calendarDays = new ArrayList<>();
                    calendarDays.add(date);
                    decoratorReference = new EventDecorator(Color.rgb(156, 130 , 192), calendarDays);
                    calendar.addDecorator(decoratorReference);
                    calendar.invalidateDecorators();
                }else if(user==4){
                    List<CalendarDay> calendarDays = new ArrayList<>();
                    calendarDays.add(date);
                    decoratorReference = new EventDecorator(Color.rgb(192, 130 , 171), calendarDays);
                    calendar.addDecorator(decoratorReference);
                    calendar.invalidateDecorators();
                }else if(user==5){
                    List<CalendarDay> calendarDays = new ArrayList<>();
                    calendarDays.add(date);
                    decoratorReference = new EventDecorator(Color.rgb(192, 130 , 130), calendarDays);
                    calendar.addDecorator(decoratorReference);
                    calendar.invalidateDecorators();
                }else if(user==6){
                    List<CalendarDay> calendarDays = new ArrayList<>();
                    calendarDays.add(date);
                    decoratorReference = new EventDecorator(Color.rgb(192, 155 , 130), calendarDays);
                    calendar.addDecorator(decoratorReference);
                    calendar.invalidateDecorators();
                }else if(user==7){
                    List<CalendarDay> calendarDays = new ArrayList<>();
                    calendarDays.add(date);
                    decoratorReference = new EventDecorator(Color.rgb(192, 189 , 130), calendarDays);
                    calendar.addDecorator(decoratorReference);
                    calendar.invalidateDecorators();
                }else{
                    List<CalendarDay> calendarDays = new ArrayList<>();
                    calendarDays.add(date);
                    decoratorReference = new EventDecorator(Color.rgb(156, 192 , 130), calendarDays);
                    calendar.addDecorator(decoratorReference);
                    calendar.invalidateDecorators();
                }


            }

        }catch(Exception e){
            // User doesn't have any dates yet
        }






    }
*/
}
