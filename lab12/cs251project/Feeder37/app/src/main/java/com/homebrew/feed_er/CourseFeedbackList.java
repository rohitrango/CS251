package com.homebrew.feed_er;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CourseFeedbackList extends AppCompatActivity {

    String token;
    int course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_feedback_list);

        Intent intent = getIntent();
        course_id = getIntent().getExtras().getInt("pk");
        token = getIntent().getExtras().getString("token");
        Log.d("TOKEN", token);
        Log.d("COURSE_ID", Integer.toString(course_id));


        // Arrange stuff here
        Toolbar toolbar = (Toolbar) findViewById(R.id.navigator);
        setSupportActionBar(toolbar);

        // Add tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.navlayout);
        tabLayout.addTab(tabLayout.newTab().setText("Feedbacks"));
        tabLayout.addTab(tabLayout.newTab().setText("Assignments"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.navigatorpager);
        final DeadlineAdapter adapter = new DeadlineAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


}
