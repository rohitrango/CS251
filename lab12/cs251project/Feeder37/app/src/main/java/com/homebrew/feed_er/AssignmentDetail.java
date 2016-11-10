package com.homebrew.feed_er;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AssignmentDetail extends AppCompatActivity {

    String token;
    int pk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_detail);

        Intent intent = getIntent();
        token = intent.getExtras().getString("token");
        pk = intent.getExtras().getInt("pk");

        DeadlineThread d = new DeadlineThread();
        new Thread(d,"Deadline").start();

        // TextView textView = (TextView) findViewById(R.id.assignmentName);
        // textView.setText(token + "\n" + Integer.toString(pk));
    }

    public class DeadlineThread implements Runnable {

        @Override
        public void run() {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getString(R.string.api_base_url) + "assignment_detail";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // request here
                            try {
                                SharedPreferences prefs = getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                                prefs.edit().putString("assign" + pk, response).apply();
                                JSONArray assignmentDetail = new JSONArray(response);
                                JSONObject obj = assignmentDetail.getJSONObject(0);
                                JSONObject fields = obj.getJSONObject("fields");

                                // get the fields and set the Heading
                                TextView heading = (TextView)findViewById(R.id.assignmentName);
                                heading.setText(fields.getString("name"));

                                // Set description
                                TextView desc = (TextView)findViewById(R.id.assignmentDescription);
                                desc.setText("DESCRIPTION: " + fields.getString("description"));

                                // Set deadline
                                TextView deadline = (TextView)findViewById(R.id.assignmentDeadline);
                                String d[] = fields.getString("deadline").split("-");
                                int yyyy = Integer.parseInt(d[0]), mm = Integer.parseInt(d[1]), dd = Integer.parseInt(d[2].substring(0,2));
                                Date dDate = new Date(yyyy-1900,mm-1,dd);
                                DateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");

                                //to convert Date to String, use format method of SimpleDateFormat class.
                                String strDate = dateFormat.format(dDate);
                                deadline.setText("Deadline: " +  strDate);

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                Log.e("ERROR: ","Some problem.");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    SharedPreferences prefs = getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                    String response;
                    if(prefs.contains("assign" + pk))
                    {
                        response = prefs.getString("assign" + pk, " ");
                        try
                        {
                            JSONArray assignmentDetail = new JSONArray(response);
                            JSONObject obj = assignmentDetail.getJSONObject(0);
                            JSONObject fields = obj.getJSONObject("fields");

                            // get the fields and set the Heading
                            TextView heading = (TextView)findViewById(R.id.assignmentName);
                            heading.setText(fields.getString("name"));

                            // Set description
                            TextView desc = (TextView)findViewById(R.id.assignmentDescription);
                            desc.setText("DESCRIPTION: " + fields.getString("description"));

                            // Set deadline
                            TextView deadline = (TextView)findViewById(R.id.assignmentDeadline);
                            String d[] = fields.getString("deadline").split("-");
                            int yyyy = Integer.parseInt(d[0]), mm = Integer.parseInt(d[1]), dd = Integer.parseInt(d[2].substring(0,2));
                            Date dDate = new Date(yyyy-1900,mm-1,dd);
                            DateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");

                            //to convert Date to String, use format method of SimpleDateFormat class.
                            String strDate = dateFormat.format(dDate);
                            deadline.setText("Deadline: " +  strDate);
                        }
                        catch (Exception e)
                        {

                        }
                    }
                    else
                    {
                    }
                }
            }){
                @Override
               public Map<String, String> getParams() {
                    Map<String,String>params = new HashMap<String, String>();
                    params.put("token",token);
                    params.put("pk",Integer.toString(pk));
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }

}
