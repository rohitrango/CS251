package com.homebrew.feed_er;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class PopupActivity extends Activity {

    Point p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
    }
    @Override
    public void onWindowFocusChanged(boolean focusOn){
        int [] p = new int[2];

    }
}
