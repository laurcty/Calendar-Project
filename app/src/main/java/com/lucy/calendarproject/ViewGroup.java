package com.lucy.calendarproject;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViewGroup extends AppCompatActivity implements AsyncTaskListener{

    // Set up hash map to link dates to how many users have that date
    HashMap<CalendarDay, Integer> hashMap = new HashMap<CalendarDay, Integer>();

    ArrayList<String> usersInGroup = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group);

        // Get the ID (corresponding to position in radioGroup) of the chosen group
        String groupName = getIntent().getStringExtra("GROUP_NAME");
        String groupID = getIntent().getStringExtra("GROUPID");

        TextView groupNameText = (TextView) findViewById(R.id.GroupID);
        groupNameText.setText("Group: " + groupName);

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
                    // Function call to get hash map with best dates in it
                    LinkedHashMap<CalendarDay, Integer> sortedHashMap = getBestDate();

                    TextView popupTitle = (TextView) popupWindow.getContentView().findViewById(R.id.Title);
                    popupTitle.setText("Best dates:");

                    TextView popupTitle2 = (TextView) popupWindow.getContentView().findViewById(R.id.Title2);
                    popupTitle2.setText("People busy:");


                    ArrayList<TextView> textViews = new ArrayList<>();
                    textViews.add((TextView) popupWindow.getContentView().findViewById(R.id.bestDate1));
                    textViews.add((TextView) popupWindow.getContentView().findViewById(R.id.bestDate2));
                    textViews.add((TextView) popupWindow.getContentView().findViewById(R.id.bestDate3));
                    textViews.add((TextView) popupWindow.getContentView().findViewById(R.id.bestDate4));
                    textViews.add((TextView) popupWindow.getContentView().findViewById(R.id.bestDate5));

                    int counter = 0;
                    for (Map.Entry<CalendarDay, Integer> entry : sortedHashMap.entrySet()) {
                        if(counter>=5){         // Break out of loop if all textViews have been filled
                            break;
                        }
                        CalendarDay bestDate = entry.getKey();
                        int noPeopleBusy = entry.getValue();
                        int day = bestDate.getDay();
                        int month = bestDate.getMonth();
                        int year = bestDate.getYear();
                        String formattedDate = day + "-" + (month + 1) + "-" + year;
                        String strToDisplay = formattedDate + "       " + noPeopleBusy;
                        textViews.get(counter).setText(strToDisplay);
                        counter++;
                    }

                    // Show popupWindow
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, -400);
            }
        });


        // Display hint when button clicked
        Button helpButton = (Button) findViewById(R.id.helpButtonVG);
        helpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(ViewGroup.this, "Tap the start and end dates on the calendar of the dates you want to find a date from before clicking FIND DATE.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addCustomDecorators(int noUsersInGroup, ArrayList<String> datesArray){
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

            CustomEventDecorator[] decoratorArray= setUpDecorators(usersWithThisDate);

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

    private LinkedHashMap<CalendarDay, Integer> getBestDate(){

        // Store selected dates in list
        final MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        List<CalendarDay> selectedDates = calendar.getSelectedDates();

        // Set up new hash map for dates which are both selected by user AND have decorators on them
        HashMap<CalendarDay, Integer> selectedDatesHashMap = new HashMap<CalendarDay, Integer>();

        // Set up LINKED hash map (retains order elements were added in) which will be the final one to store sorted dates
        LinkedHashMap<CalendarDay, Integer> sortedHashMap = new LinkedHashMap<CalendarDay, Integer>();

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
        List<Map.Entry<CalendarDay, Integer>> list = new LinkedList<Map.Entry<CalendarDay, Integer> >(selectedDatesHashMap.entrySet());

        linkedList li = new linkedList();

        for (int i=0;i<=list.size()-1;i++){
            List<CalendarDay> keys = new LinkedList<CalendarDay>(selectedDatesHashMap.keySet());
            List<Integer> values = new LinkedList<Integer>(selectedDatesHashMap.values());
            li.push(keys.get(i),values.get(i));
        }

        // Apply merge Sort
        li.head = li.mergeSort(li.head);
        while (li.head != null) {
            System.out.println(li.head.date + ": " + li.head.val + " ");
            sortedHashMap.put(li.head.date, li.head.val);
            li.head = li.head.next;
        }
        return sortedHashMap;
    }

    private CustomEventDecorator[] setUpDecorators(ArrayList<Integer> usersWithThisDate){
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
                decoratorArray[index++] = new CustomEventDecorator(Color.rgb(c.get(i-1).get(0), c.get(i-1).get(1), c.get(i-1).get(2)), xSpanType++, ySpanType);
            }
            if (xSpanType == 4) {
                xSpanType = 0;
                ySpanType = 1;
            }
        }
        return decoratorArray;
    }

    private void setUpHashMap(HashMap<CalendarDay, Integer> hashMap, String dates){
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

    private void setUpUsersInGroup(String result){
        usersInGroup = new ArrayList<String>(Arrays.asList(result.split("\\s*,\\s*")));
        System.out.println(usersInGroup);

        ArrayList<TextView> namesArray = new ArrayList<>();
        namesArray.add((TextView) findViewById(R.id.viewGroupName1));
        namesArray.add((TextView) findViewById(R.id.viewGroupName2));
        namesArray.add((TextView) findViewById(R.id.viewGroupName3));
        namesArray.add((TextView) findViewById(R.id.viewGroupName4));
        namesArray.add((TextView) findViewById(R.id.viewGroupName5));
        namesArray.add((TextView) findViewById(R.id.viewGroupName6));
        namesArray.add((TextView) findViewById(R.id.viewGroupName7));
        namesArray.add((TextView) findViewById(R.id.viewGroupName8));

        // Add names of users in group to scrollView at top
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
        System.out.println("The result is "+result);
        if(result.contains("GROUPCALENDARDATES")){
            // bg2 has called this and returned calendar dates of all people in group
            result = result.substring(19, result.length() - 1);
            System.out.println("The result of getting dates is: "+result);
            ArrayList<String> datesArray = new ArrayList<String>(Arrays.asList(result.split("/")));
            System.out.println("datesArray is "+datesArray);
            System.out.println("The size of datesArray is: "+datesArray.size());
            addCustomDecorators(usersInGroup.size(), datesArray);
        }else{
            result = result.substring(0, result.length() - 1);      // Remove comma from end of string
            setUpUsersInGroup(result);
        }
    }
}