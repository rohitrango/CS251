package com.homebrew.feed_er;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by MEET on 04-Nov-16.
 */

public class CustomCaldroidDrawable extends Drawable {

    private final String text;
    private final Paint paint;

    public CustomCaldroidDrawable(String text) {

        this.text = text;

        this.paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(1000f);
//        paint.setAntiAlias(true);
//        paint.setFakeBoldText(true);
        paint.setShadowLayer(6f, 0, 0, Color.BLACK);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void draw(Canvas canvas) {
        for(int i=0; i<100; i+=10)
            for(int j=0; j<100; j+=10)
                canvas.drawText(text, i, j, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}