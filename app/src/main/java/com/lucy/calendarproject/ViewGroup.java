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



import java.lang.reflect.Array;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.Collections;

import java.util.Comparator;

import java.util.HashMap;

import java.util.LinkedHashMap;

import java.util.LinkedList;

import java.util.List;

import java.util.Map;



import static com.lucy.calendarproject.R.id.bestDateNoUsers1;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_MULTIPLE;

public class ViewGroup extends AppCompatActivity implements AsyncTaskListener{

    // Set up hash map to link dates to how many users have that date
    HashMap<CalendarDay, Integer> hashMap = new HashMap<CalendarDay, Integer>();

    ArrayList<String> usersInGroup = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group);


        // Get the ID (corresponding to position in radioGroup) of the chosen group
        String groupID = getIntent().getStringExtra("GROUP_ID");
        TextView groupIDText = (TextView) findViewById(R.id.GroupID);
        groupIDText.setText("Group: " + groupID);


        background bg = new background(ViewGroup.this);
        bg.execute("groupID", "blank", groupID, "blank", "getUsersInGroup");


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
                try {
                    HashMap<CalendarDay, Integer> sortedHashMap = getBestDate();
                    Map.Entry<CalendarDay, Integer> hashMapData = sortedHashMap.entrySet().iterator().next();
                    CalendarDay bestDate = hashMapData.getKey();
                    int noPeopleBusy = hashMapData.getValue();
                    int day = bestDate.getDay();
                    int month = bestDate.getMonth();
                    int year = bestDate.getYear();
                    String formattedDate = day + "-" + (month + 1) + "-" + year;

                    // Change textViews
                    TextView popupTitle = (TextView) popupWindow.getContentView().findViewById(R.id.popupTitle);
                    popupTitle.setText("Best dates:");
                    TextView tvBestDate = (TextView) popupWindow.getContentView().findViewById(R.id.BestDate1);
                    tvBestDate.setText(formattedDate);
                    TextView nextBestDate = (TextView) popupWindow.getContentView().findViewById(R.id.BestDate2);
                    nextBestDate.setText(noPeopleBusy);

                    // Show popupWindow
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, -400);
                }catch (Exception e){
                    // No dates selected
                    Toast.makeText(ViewGroup.this, "Please select a range of dates.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void addCustomDecorators(int noUsersInGroup, ArrayList<String> datesArray){
        // Get id of calendarView
        final MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);

        // Set up hashMap to map all dates in calendars to how many decorators they should have
        for(int i=0;i<noUsersInGroup;i++) {
            setUpHashMap(hashMap, datesArray.get(i));
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
            for (int i = 0; i < noUsersInGroup; i++) {
                try {
                    if (datesArray.get(i).contains(formattedDate)) usersWithThisDate.add(i + 1);
                } catch (Exception e) {
                    // User has no dates in their calendar yet
                }
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

    public HashMap<CalendarDay, Integer> getBestDate(){
        // todo print more than one good date, along with how many people are free on each date that is chosen by algorithm

        // Store selected dates in list
        final MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        List<CalendarDay> selectedDates = calendar.getSelectedDates();

        // Set up new hash map for dates which are both selected by user AND have decorators on them
        HashMap<CalendarDay, Integer> selectedDatesHashMap = new HashMap<CalendarDay, Integer>();

        // Set up LINKED hash map (retains order elements were added in) which will be the final one to store sorted dates
        HashMap<CalendarDay, Integer> sortedHashMap = new LinkedHashMap<CalendarDay, Integer>();

        // Loop around array of selected dates to find those which are not in the hashMap (everyone is free)
        for (CalendarDay date : selectedDates) {
            if(hashMap.containsKey(date)){
                // Add date to new hashMap to be sorted
                selectedDatesHashMap.put(date, hashMap.get(date));
            }else{
                // Add date to "front" of sortedHashMap as these dates are the best so should be suggested first
                sortedHashMap.put(date, 0);
            }
        }

        // Store all elements of hashMap in a LinkedList
        List<Map.Entry<CalendarDay, Integer> > list = new LinkedList<Map.Entry<CalendarDay, Integer> >(selectedDatesHashMap.entrySet());

        // Sort the LinkedList based on the integers mapped to each date
        Collections.sort(list, new Comparator<Map.Entry<CalendarDay, Integer> >() {
            public int compare(Map.Entry<CalendarDay, Integer> o1,
                               Map.Entry<CalendarDay, Integer> o2)

            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // Enter sorted dates into linked hash map
        for (Map.Entry<CalendarDay, Integer> listData : list) {
            sortedHashMap.put(listData.getKey(), listData.getValue());
        }
        return sortedHashMap;
    }

    public CustomEventDecorator[] setUpDecorators(ArrayList<Integer> usersWithThisDate, int currDayCount){
        CustomEventDecorator[] decoratorArray = new CustomEventDecorator[usersWithThisDate.size()];
        // Add decorators to decoratorArray if they should be added i.e. if that colour user has the date in their calendar
        // Use index as a counter to determine which spanType is used upon decorator creation (x position of the decorator)
        int index=0;
        int xSpanType=0;
        int ySpanType=0;
        List<List<Integer>> c = new ArrayList<List<Integer>>(); // colour array
        c.add(Arrays.asList(130, 192, 169));
        c.add(Arrays.asList(130, 155, 192));
        c.add(Arrays.asList(156, 130, 192));
        c.add(Arrays.asList(192, 130, 171));
        c.add(Arrays.asList(192, 130, 130));
        c.add(Arrays.asList(192, 155, 130));
        c.add(Arrays.asList(192, 189, 130));
        c.add(Arrays.asList(156, 192, 130));

        for (int i = 1; i <= 8; i++) {
            if (usersWithThisDate.contains(i)) {
                decoratorArray[index++] = new CustomEventDecorator(Color.rgb(c.get(i-1).get(0), c.get(i-1).get(1), c.get(i-1).get(2)), 12, xSpanType++, ySpanType);
            }
            if (xSpanType == 4) {
                xSpanType = 0;
                ySpanType = 1;
            }
        }
        return decoratorArray;
    }

    public void setUpHashMap(HashMap<CalendarDay, Integer> hashMap, String dates){
        // Split user's calendar into CalendarDays
        try {

            String[] userDates = dates.split("\\s*,\\s*");
            System.out.println("I'm setting up the hashMap");
            System.out.println("The string of dates before splitting is "+dates);
            for (int j = 0; j < userDates.length; j++) {
                // If date already in hashMap, increment number mapped to that date, if not add new entry of date and map to number one
                System.out.println("Date "+j+" is "+userDates[j]);
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

    public void setUpUsersInGroup(String result){
        usersInGroup = new ArrayList<String>(Arrays.asList(result.split("\\s*,\\s*")));

        ArrayList<TextView> namesArray = new ArrayList<>();
        namesArray.add((TextView) findViewById(R.id.viewGroupName1));
        namesArray.add((TextView) findViewById(R.id.viewGroupName2));
        namesArray.add((TextView) findViewById(R.id.viewGroupName3));
        namesArray.add((TextView) findViewById(R.id.viewGroupName4));
        namesArray.add((TextView) findViewById(R.id.viewGroupName5));
        namesArray.add((TextView) findViewById(R.id.viewGroupName6));
        namesArray.add((TextView) findViewById(R.id.viewGroupName7));
        namesArray.add((TextView) findViewById(R.id.viewGroupName8));


        // Add names of users in group to scrollView at top and save their calendars in strings
        for(int i=0;i<usersInGroup.size();i++) {
            System.out.println("I'm adding users to usersInGroup!!");
            namesArray.get(i).setText((" " + usersInGroup.get(i) + " "));
        }

        background bg2 = new background(ViewGroup.this);
        bg2.execute("usernames", "blank", result, "blank", "getGroupCalendarDates");

    }


    // This method gets called after background.java finishes
    @Override
    public void updateResult(String result){
        System.out.println("I'm in the updateResult method!!!!!");
        //System.out.println("The result is "+result);
        if(result.contains("GROUPCALENDARDATES")){
            // bg2 has called this and returned calendar dates of all people in group
            result = result.substring(19, result.length() - 1);
            //ArrayList<String> datesArray = new ArrayList<>();
            ArrayList<String> datesArray = new ArrayList<String>(Arrays.asList(result.split("/")));
            addCustomDecorators(usersInGroup.size(), datesArray);
        }else{
            result = result.substring(0, result.length() - 1);      // Remove comma from end of string (perhaps redundant but oh well)
            setUpUsersInGroup(result);
        }

    }
}