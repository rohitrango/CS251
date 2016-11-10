package com.homebrew.feed_er;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.homebrew.feed_er.R.id.textView;

public class CalendarActivity extends AppCompatActivity {
    private Map<Date,String> impDates;
    private Map<Date, Drawable> backgroundForDateMap;
    private CaldroidFragment caldroidFragment;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        caldroidFragment = new CaldroidFragment();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarView, caldroidFragment);
        t.commit();

        token = getIntent().getExtras().getString("token");


        DatesListGetter datesListGetter = new DatesListGetter();
        new Thread(datesListGetter, "DatesListGetter").start();

    }

    private class DatesListGetter implements Runnable {
        public DatesListGetter() {
            Log.d("DLG", "DLG constructed");
        }

        @Override
        public void run() {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getString(R.string.api_base_url)+"dates";
            impDates = new HashMap<>();
            Log.d("CLG", "sending request...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String responseString) {
                            Log.d("CLG", "response obtained...");
                            Log.d("Response",responseString);
                            Log.d("TOKEN",token);
                            // Display the first 500 characters of the response string.
                            try{
                                final JSONObject response = new JSONObject(responseString);
//                                Log.d("JSON","1 "+response.toString());
                                JSONArray assignDeadlines = response.getJSONArray("assignments");
                                JSONArray examDates = response.getJSONArray("feedback");
                                backgroundForDateMap = new HashMap<>();
                                impDates.clear();
                                final Calendar c = Calendar.getInstance();
                                for (int i = 0; i < assignDeadlines.length(); i++) {
                                    //Log.d("JSON","2");
                                    String assignDeadline = assignDeadlines.getString(i);
                                    //Log.d("JSON", assignDeadline);
                                    String components[] = assignDeadline.split("/");
                                    //Log.d("COMP",components[0]);
                                    int yyyy = Integer.parseInt(components[0]);
                                    int mm = Integer.parseInt(components[1]);
                                    int dd = Integer.parseInt(components[2]);
                                    Date dL = new Date(yyyy-1900,mm-1,dd);
                                    c.setTimeInMillis(dL.getTime());

                                    impDates.put(c.getTime(),"what is this?");
                                    backgroundForDateMap.put(c.getTime(), new ColorDrawable(Color.YELLOW));
                                    Log.d("JSON", dL.toString());
                                }
                                for (int i = 0; i < examDates.length(); i++) {
//                                    //Log.d("JSON","2");
                                    String examDate = examDates.getString(i);
                                    //Log.d("JSON", assignDeadline);
                                    String components[] = examDate.split("/");
                                    //Log.d("COMP",components[0]);
                                    int yyyy = Integer.parseInt(components[0]);
                                    int mm = Integer.parseInt(components[1]);
                                    int dd = Integer.parseInt(components[2]);
                                    Date dL = new Date(yyyy-1900,mm-1,dd);
                                    c.setTimeInMillis(dL.getTime());

                                    impDates.put(c.getTime(),"what is this?");
                                    if(backgroundForDateMap.get(c.getTime())==new ColorDrawable(Color.YELLOW))
                                        backgroundForDateMap.put(c.getTime(), new ColorDrawable(Color.GREEN));
                                    else
                                        backgroundForDateMap.put(c.getTime(), new ColorDrawable(Color.BLUE));
                                    Log.d("JSON", dL.toString());

                                }

                                //listeners
                                final CaldroidListener listener = new CaldroidListener() {
                                    @Override
                                    public void onSelectDate(Date date, View view) {
                                        //Log.d("SELECT DATE", date.toString());
                                        if(impDates.containsKey(date)){
                                            Log.d("IMPDATE", impDates.get(date));
                                            backgroundForDateMap.put(date, new ColorDrawable(Color.GREEN));
                                            caldroidFragment.setBackgroundDrawableForDates(backgroundForDateMap);
                                            caldroidFragment.refreshView();
                                        }
                                    }


                                };

                                caldroidFragment.setCaldroidListener(listener);

                                //




                                caldroidFragment.setBackgroundDrawableForDates(backgroundForDateMap);
                                caldroidFragment.refreshView();
                            }
                            catch (JSONException e){
                                Log.d("JSON","JSON error");
                            }
                            //System.out.println("Response recorded");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //textView.setText("Please check your internet connection.");
                    Log.d("DATES","response not received");
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("token",token);
                    Log.d("TOKEN",token);
                    return params;
                }

            };
            queue.add(stringRequest);
        }
    }
}
