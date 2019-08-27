package com.lucy.calendarproject;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class CustomSpan extends DotSpan{
    private int color;
    private int xOffset;
    private float radius=15;


    CustomSpan(int color, int xOffset){
        this.color = color;
        this.xOffset = xOffset;
    }

    /*Note! The following code is more or less copy-pasted from the DotSpan class. I have commented the changes below.*/
    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline,
                               int bottom, CharSequence text, int start, int end, int lnum) {
        int oldColor = paint.getColor();
        if (color != 0) {
            paint.setColor(color);
        }
        int x = ((left + right) / 2); /*This is the x-coordinate right
    below the date. If we add to x, we will draw the
    circle to the right of the date and vice versa if we subtract from x.*/
        canvas.drawCircle(x + xOffset, bottom + radius, radius, paint);
        paint.setColor(oldColor);
    }
}