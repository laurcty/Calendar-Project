package com.lucy.calendarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.EventLog;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewCalendar extends AppCompatActivity {

    DatabaseHelper db = new DatabaseHelper(this);

    // Set up decorator reference so that it can be removed later
    private EventDecorator decoratorReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_calendar);

        // Get userID of person logged in
        String strUserID = getIntent().getStringExtra("USER_ID");
        final int userID = Integer.parseInt(strUserID);

        // Set up array list to store days to put dots under
        final List<CalendarDay> calendarDays = new ArrayList<>();

        // Get id of calendarView
        final MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);


        // Get dates in user's calendar and add decorators to them
        try {
            String calendarDates = db.getCalendarDates(userID);
            String[] datesInCalendar = calendarDates.split(",");


            for(int i=0; i<datesInCalendar.length; i++) {

                String[] DMY = datesInCalendar[i].split("-");
                int year = Integer.parseInt(DMY[2]);
                int month = Integer.parseInt(DMY[1]);
                int day = Integer.parseInt(DMY[0]);
                CalendarDay date = CalendarDay.from(year, month - 1, day);

                calendarDays.add(date);
                decoratorReference = new EventDecorator(Color.rgb(143, 209, 219), calendarDays);
                calendar.addDecorator(decoratorReference);
                calendar.invalidateDecorators();
            }

        }catch(Exception e){
            // User doesn't have any dates yet
        }



        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull final MaterialCalendarView calendar, @NonNull final CalendarDay date, boolean selected) {
                // Store the value of date as a string
                // Month indexes start at 0 so add one to it
                int day = date.getDay();
                int month = date.getMonth();
                int year = date.getYear();
                String strDate = day + "-" + (month+1) + "-" + year ;

            }
        });


        Button btnAddDecorator = (Button) findViewById(R.id.addDateBtn);
        btnAddDecorator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Add dot onto selected date
                try {
                    CalendarDay date = calendar.getSelectedDate();
                    if(!calendarDays.contains(date)) {
                        int day = date.getDay();
                        int month = date.getMonth();
                        int year = date.getYear();
                        String dateToAdd = day + "-" + (month + 1) + "-" + year;

                        calendarDays.add(date);
                        decoratorReference = new EventDecorator(Color.rgb(143, 209, 219), calendarDays);
                        calendar.addDecorator(decoratorReference);
                        calendar.invalidateDecorators();

                        // Add this date to database
                        db.addDate(userID, dateToAdd);
                    }

                }catch(Exception e){
                    // If no date is selected
                }

            }
        });

        Button btnDeleteDecorator = (Button) findViewById(R.id.removeDateBtn);
        btnDeleteDecorator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                CalendarDay date = calendar.getSelectedDate();
                int day = date.getDay();
                int month = date.getMonth();
                int year = date.getYear();
                String dateToRemove = day +"-"+ (month+1) +"-"+ year ;

                    calendarDays.remove(date);
                    // Remove this date from database
                    db.removeDate(userID, dateToRemove);

                    // Remove all decorators
                    calendar.removeDecorators();

                    // Add all decorators back except one which has been deleted
                    final List<CalendarDay> calendarDays = new ArrayList<>();
                    try {
                        String calendarDates = db.getCalendarDates(userID);
                        String[] datesInCalendar = calendarDates.split(",");
                        for(int i=0; i<datesInCalendar.length; i++) {
                            String[] DMY = datesInCalendar[i].split("-");
                            int year1 = Integer.parseInt(DMY[2]);
                            int month1 = Integer.parseInt(DMY[1]);
                            int day1 = Integer.parseInt(DMY[0]);
                            CalendarDay date1 = CalendarDay.from(year1, month1 - 1, day1);
                            calendarDays.add(date1);
                            decoratorReference = new EventDecorator(Color.rgb(143, 209, 219), calendarDays);
                            calendar.addDecorator(decoratorReference);
                            calendar.invalidateDecorators();
                        }
                    }catch(Exception e){
                        // User doesn't have any dates yet
                    }
                }catch(Exception e){
                    // If no date is selected
                }
            }
        });

    }


}



