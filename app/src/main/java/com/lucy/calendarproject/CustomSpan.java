package com.lucy.calendarproject;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class CustomSpan extends DotSpan{
    private int color;
    private int xOffset;
    private int yOffset;
    private float radius=15;

    CustomSpan(int color, int xOffset, int yOffset){
        this.color = color;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline,
                               int bottom, CharSequence text, int start, int end, int lnum) {
        int oldColor = paint.getColor();
        if (color != 0) {
            paint.setColor(color);
        }
        int x = ((left + right) / 2);

        canvas.drawCircle(x + xOffset, bottom + 20 + yOffset, radius, paint);
        paint.setColor(oldColor);
    }
}