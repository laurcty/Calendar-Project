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

public class ViewCalendar extends AppCompatActivity implements AsyncTaskListener{

    // Set up decorator reference so that it can be removed later
    private EventDecorator decoratorReference;

    // Set up array list to store days to put dots under
    final List<CalendarDay> calendarDays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_calendar);

        // Get userID of user logged in
        final String username = getIntent().getStringExtra("USERNAME");

        // Get dates in user's calendar and add decorators to them
        background bg = new background(ViewCalendar.this);
        bg.execute("username","blank",username,"blank","getCalendarDates");

    }

    public void setUpCalendar(String result){
        // Get id of calendarView
        final MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);

        try {
            String[] datesInCalendar = result.split(",");

            // Clear calendarDays
            calendarDays.clear();

            for(int i=0; i<datesInCalendar.length; i++) {
                System.out.println("Date "+i+" is "+datesInCalendar[i]);
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

        changeCalendar();
    }

    public void changeCalendar(){
        // When add date is clicked
        Button btnAddDecorator = (Button) findViewById(R.id.addDateBtn);
        btnAddDecorator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
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
                        background bg2 = new background(ViewCalendar.this);
                        final String username = getIntent().getStringExtra("USERNAME");
                        bg2.execute("username","dateToAdd",username,dateToAdd,"addCalendarDate");
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
                final MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
                try{
                    CalendarDay date = calendar.getSelectedDate();
                    int day = date.getDay();
                    int month = date.getMonth();
                    int year = date.getYear();
                    String dateToRemove = day +"-"+ (month+1) +"-"+ year ;

                    calendarDays.remove(date);

                    background bg3 = new background(ViewCalendar.this);
                    final String username = getIntent().getStringExtra("USERNAME");
                    bg3.execute("username","dateToRemove",username,dateToRemove,"removeCalendarDate");

                    // Remove all decorators
                    calendar.removeDecorators();

                    // Get dates in user's calendar and add decorators to them
                    background bg4 = new background(ViewCalendar.this);
                    bg4.execute("username","blank",username,"blank","getCalendarDates");
                }catch(Exception e){
                    // If no date is selected
                }
            }
        });
    }

    // This method gets called after background.java finishes
    @Override
    public void updateResult(String result){
        System.out.println("I'm in the updateResult method in ViewCalendar!!!!!");
        System.out.println("User dates are: "+result);
        if(result.contains("REMOVECALENDARDATE")) {
            changeCalendar();
        }else if(result.contains("ADDCALENDARDATE")){
            changeCalendar();
        }else{
            setUpCalendar(result);
        }
    }
}