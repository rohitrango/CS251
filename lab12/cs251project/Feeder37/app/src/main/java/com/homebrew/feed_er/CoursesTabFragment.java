package com.homebrew.feed_er;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class CoursesTabFragment extends Fragment {

    TextView output;

    // Store the class objects here
    public class Course {
        public String name;
        public String course_code;
        public int pk;

        public Course() {
            name = course_code = "";
            pk = -1;
        }

        @Override
        public String toString() {
            if (course_code.equals("")) {
                return name;
            } else {
                return course_code + "\n" + name;
            }
        }
    }

    public String token;

    public Course[] CourseList;
    public String[] CourseNameList;

    // make the adapter here
    public class CourseAdapter extends ArrayAdapter<Course> {


        public CourseAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public CourseAdapter(Context context, int resource, Course[] items) {
            super(context, resource, items);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.textviewxml, null);
            }

            final Course p = getItem(position);

            if (p != null) {
                TextView tt = (TextView) v.findViewById(R.id.courseTextView);
                tt.setText(p.toString());

                if (!p.course_code.equals("")) {
                    tt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), CourseFeedbackList.class);
                            intent.putExtra("token", token);
                            intent.putExtra("pk", p.pk);
                            startActivity(intent);
                        }
                    });
                }

            }

            return v;
        }
    }

    public ArrayAdapter adapter;


    // Create the JSON API getter from here
    private class coursesListGetter implements Runnable {

        private Context context;
        private ListView mylistview;

        private coursesListGetter(Context applicationContext, ListView view) {
            Log.d("Start", "Start Courses");
            mylistview = view;
            context = applicationContext;
        }

        @Override
        public void run() {

            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            String url = getString(R.string.api_base_url) + "courses";
            Log.d("CLG", "sending request...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String responseString) {
                            try {
                                Log.d("course response", responseString);
                                SharedPreferences prefs = getActivity().getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                                prefs.edit().putString("course", responseString).apply();
                                JSONArray course_list = new JSONArray(responseString);
                                CourseList = new Course[course_list.length()];
                                CourseNameList = new String[course_list.length()];

                                for (int i = 0; i < course_list.length(); i++) {
                                    Course mc = new Course();
                                    JSONObject course = (JSONObject) course_list.get(i);
                                    mc.pk = (int) course.get("pk");
                                    JSONObject fields = (JSONObject) course.get("fields");
                                    mc.name = fields.getString("name");
                                    mc.course_code = fields.getString("course_code");
                                    CourseList[i] = mc;
                                    CourseNameList[i] = mc.name;
                                }

                                Log.d("Done", "Course list name");

                                setAdapterForFrame();
                                Log.d("done", "done");
                            } catch (Exception ee) {

                                Log.e("error", "Some error occured here.");

                            }
                            //System.out.println("Response recorded");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //textView.setText("Please check your internet connection.");
                    Log.d("WHAT", "WHAT");
                    try
                    {
                        SharedPreferences prefs = getActivity().getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                        String responseString;

                        if(prefs.contains("course")) {
                            Log.d("WHAT2", "WHAT2");

                            responseString = prefs.getString("course", " ");
                            Log.d("WHAT2", responseString);
                            JSONArray course_list = new JSONArray(responseString);
                            CourseList = new Course[course_list.length()];
                            CourseNameList = new String[course_list.length()];

                            for (int i = 0; i < course_list.length(); i++) {
                                Course mc = new Course();
                                JSONObject course = (JSONObject) course_list.get(i);
                                mc.pk = (int) course.get("pk");
                                JSONObject fields = (JSONObject) course.get("fields");
                                mc.name = fields.getString("name");
                                mc.course_code = fields.getString("course_code");
                                CourseList[i] = mc;
                                CourseNameList[i] = mc.name;

                            }

                            Log.d("Done", "Course list name");
                            setAdapterForFrame();
                            Log.d("done", "done");
                        }
                        else
                        {
                            Toast.makeText(getContext(), "You are not connected to the internet.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e)
                    {
                        Log.d("WHAT@", "WHATTttt");
                    }


                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    Log.d("TOKEN", token);
                    return params;
                }

            };
            queue.add(stringRequest);

        }
    }

    ;

    private View view;
    private ListView listView;
    private Context context;

    // on create
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_courses_tab, viewGroup, false);
        listView = (ListView) view.findViewById(R.id.courseOuterView);
        // Get the request here
        Boolean isNull = (getActivity() == null);
        Log.d("NULL", isNull.toString());
        token = getActivity().getIntent().getExtras().getString("token");
        Log.d("HERE", "HERE");
        Context c = getActivity().getApplicationContext();
        coursesListGetter g = new coursesListGetter(c, listView);
        new Thread(g, "DatesListGetter").start();
        return view;
    }


    public void display(String txt) {
        output.setText(txt);
    }


    public void setAdapterForFrame() {
        listView = (ListView) view.findViewById(R.id.courseOuterView);
        if (CourseList.length == 0) {
            CourseList = new Course[1];
            CourseList[0] = new Course();
            CourseList[0].name = "No course for you.";
            CourseList[0].course_code = "";
        }
        adapter = new CourseAdapter(context, R.layout.textviewxml, CourseList);
        listView.setAdapter(adapter);
    }

}
