package com.lucy.calendarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewCalendar extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_calendar);

        //set up array list to store days to put dots under
        final List<CalendarDay> calendarDays = new ArrayList<>();

        //get id of calendarView
        MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);


        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView calendar, @NonNull CalendarDay date, boolean selected) {
                // Store the value of date as a string
                // Month indexes start at 0 so add one to it
                final TextView date_view = (TextView) findViewById(R.id.date_view);
                int day = date.getDay();
                int month = date.getMonth();
                int year = date.getYear();

                String strDate = day + "-" + (month+1) + "-" + year ;
                date_view.setText(strDate);

                //add dot onto selected date
                calendarDays.add(date);
                calendar.addDecorator(new EventDecorator(Color.rgb(143, 209, 219), calendarDays));

                //todo remove dots from own calendar when selected
                /*remove dots if already selected
                if(dot already exists on selected day){
                       calendar.removeDecorators();
                }
                */
            }
        });


        //todo add button for saving changes to calendar i.e. update database
        //todo oh also make a new database table to store dates of when users are free idk how that's gonna happen but ok
    }

}



