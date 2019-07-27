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
        List<CalendarDay> calendarDays = new ArrayList<>();

        //remember that months index starts at 0 so minus one from each month
        CalendarDay newDay = new CalendarDay(2019, 7-1, 18);
        calendarDays.add(newDay);

        //get id of calendarView and create new instance of EventDecorator
        MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendar.addDecorator(new EventDecorator(Color.BLUE, calendarDays));

        /*
        CalendarView calendar = (CalendarView) findViewById(R.id.calender);
        final TextView date_view = (TextView) findViewById(R.id.date_view);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                // Store the value of date as a string
                // Month indexes start at 0 so add one to it

                int year=i;
                int month=i1;
                int day=i2;

                String Date
                        = day + "-"
                        + (month + 1) + "-" + year;

                    date_view.setText(Date);

            }
        });
        */
    }

}



