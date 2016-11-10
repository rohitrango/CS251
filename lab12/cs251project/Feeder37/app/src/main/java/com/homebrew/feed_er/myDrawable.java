package com.homebrew.feed_er;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

/**
 * Created by MEET on 04-Nov-16.
 */

class myDrawable extends Drawable {

    private static final String TAG = myDrawable.class.getSimpleName();
    private ColorFilter cf;
    @Override
    public void draw(Canvas canvas) {


        //First you define a colour for the outline of your rectangle

        Paint rectanglePaint = new Paint();
        rectanglePaint.setARGB(255, 255, 0, 0);
        rectanglePaint.setStrokeWidth(2);
        rectanglePaint.setStyle(Paint.Style.FILL);

        //Then create yourself a Rectangle
        RectF rectangle = new RectF(15.0f, 50.0f, 55.0f, 75.0f); //in pixels


        Log.d(TAG,"On Draw method");
        // TODO Auto-generated method stub
        Paint paintHandl = new Paint();
        //  paintHandl.setColor(0xaabbcc);
        paintHandl.setARGB(125, 234, 213, 34 );
        RectF rectObj = new RectF(5,5,25,25);
        canvas.drawRoundRect(rectangle, 0.5f, 0.5f, rectanglePaint);

    }


    @Override
    public int getOpacity()
    {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setAlpha(int alpha) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // TODO Auto-generated method stub
        this.cf = cf;
    }
}
