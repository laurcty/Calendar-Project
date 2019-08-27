package com.lucy.calendarproject;

import android.text.style.LineBackgroundSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;


//          --- This class is used for decorating days with the possibility of more than one event ---

public class CustomEventDecorator implements DayViewDecorator {

    private static final int[] xOffsets = new int[]{-30,-15,0,15};          // The negative values shift the decorator to the left, positive to the right
    private int color;
    private HashSet<CalendarDay> dates;
    private float dotRadius;
    private int spanType;

    public CustomEventDecorator(int color, float dotRadius, int spanType) {
        this.color = color;
        this.dotRadius = dotRadius;
        this.dates = new HashSet<>();
        this.spanType = spanType;
    }

    public boolean addDate(CalendarDay day){
        return dates.add(day);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        LineBackgroundSpan span = new CustomSpan(color, xOffsets[spanType]);
        view.addSpan(span);
    }
}

