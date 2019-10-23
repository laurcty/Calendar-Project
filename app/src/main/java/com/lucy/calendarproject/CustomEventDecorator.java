package com.lucy.calendarproject;

import android.text.style.LineBackgroundSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import java.util.HashSet;

//          --- This class is used for decorating days with the possibility of more than one event ---

public class CustomEventDecorator implements DayViewDecorator {

    private static final int[] xOffsets = new int[]{-36,-12,12,36};          // The negative values shift the decorator to the left, positive to the right
    private static final int[] yOffsets = new int[]{0,30};                   // The +30 shifts the decorator down
    private int color;
    private HashSet<CalendarDay> dates;
    private int xSpanType;
    private int ySpanType;

    CustomEventDecorator(int color, int xSpanType, int ySpanType) {
        this.color = color;
        this.dates = new HashSet<>();
        this.xSpanType = xSpanType;
        this.ySpanType = ySpanType;
    }

    boolean addDate(CalendarDay day){
        return dates.add(day);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        LineBackgroundSpan span = new CustomSpan(color, xOffsets[xSpanType], yOffsets[ySpanType]);
        view.addSpan(span);
    }
}

