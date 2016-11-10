package com.homebrew.feed_er;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AssignmentList extends Fragment {

    String token;
    int course_id;

    // Assignment class
    public class Assignment {
        public String name;
        public int pk;
        public Date deadline;
        public String description;

        @Override
        public String toString() {
            return name;
        }
    }

    Assignment[] assignments;

    // class for custom adapter
    public class AssignmentAdapter extends ArrayAdapter<Assignment> {

        public AssignmentAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public AssignmentAdapter(Context context, int resource, Assignment[] items) {
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

            final Assignment p = getItem(position);

            if (p != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.courseTextView);
                if (tt1 != null) {
                    tt1.setText(p.toString());
                }
                if (p.pk != -1) {
                    tt1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), AssignmentDetail.class);
                            intent.putExtra("pk", p.pk);
                            intent.putExtra("token", token);
                            startActivity(intent);
                        }
                    });
                }
            }

            return v;
        }

    }

    AssignmentAdapter assignmentadapter;

    // Feedback list thread
    public class AssignmentThread implements Runnable {

        @Override
        public void run() {
            // Request a string response from the provided URL.
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            String url = getString(R.string.api_base_url) + "course_deadlines";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Log.d("RESPONSE",response);
                            // Make the json object
                            try {
                                SharedPreferences prefs = getActivity().getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                                prefs.edit().putString("course"+ course_id + "assign", response).apply();
                                JSONObject json_obj = new JSONObject(response);
                                JSONArray assignmentJSON = json_obj.getJSONArray("assignment");

                                assignments = new Assignment[assignmentJSON.length()];
                                for (int i = 0; i < assignmentJSON.length(); i++) {
                                    JSONObject blob = (JSONObject) assignmentJSON.get(i);
                                    JSONObject fields = (JSONObject) blob.get("fields");
                                    assignments[i] = new Assignment();
                                    assignments[i].name = fields.getString("name");
                                    assignments[i].description = fields.getString("description");
                                    assignments[i].pk = blob.getInt("pk");
                                    String unParsedDate[] = fields.getString("deadline").split("-");
                                    int yyyy = Integer.parseInt(unParsedDate[0]), mm = Integer.parseInt(unParsedDate[1]), dd = Integer.parseInt(unParsedDate[2].substring(0, 2));
                                    assignments[i].deadline = new Date(yyyy - 1900, mm - 1, dd);
                                }
                                createAssignmentView();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("ERROR:", "ERROR in json response assignment");
                            }
                            // Display the first 500 characters of the response string.
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                    String response;

                    if(prefs.contains("course" + course_id + "assign"))
                    {
                        try {
                            response = prefs.getString("course" + course_id + "assign", " ");
                            JSONObject json_obj = new JSONObject(response);
                            JSONArray assignmentJSON = json_obj.getJSONArray("assignment");

                            assignments = new Assignment[assignmentJSON.length()];
                            for (int i = 0; i < assignmentJSON.length(); i++) {
                                JSONObject blob = (JSONObject) assignmentJSON.get(i);
                                JSONObject fields = (JSONObject) blob.get("fields");
                                assignments[i] = new Assignment();
                                assignments[i].name = fields.getString("name");
                                assignments[i].description = fields.getString("description");
                                assignments[i].pk = blob.getInt("pk");
                                String unParsedDate[] = fields.getString("deadline").split("-");
                                int yyyy = Integer.parseInt(unParsedDate[0]), mm = Integer.parseInt(unParsedDate[1]), dd = Integer.parseInt(unParsedDate[2].substring(0, 2));
                                assignments[i].deadline = new Date(yyyy - 1900, mm - 1, dd);
                            }
                            createAssignmentView();
                        }
                        catch (JSONException e)
                        {

                        }

                    }
                    else
                    {
                        Toast.makeText(getContext(), "You are not connected to the internet.", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("ERROR:", "Error connecting assignment.");

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("course_id", Integer.toString(course_id));
                    Log.d("TOKEN", token);
                    Log.d("ID:", Integer.toString(course_id));
                    return params;
                }

            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }

    Context context;
    View view;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        token = getActivity().getIntent().getExtras().getString("token");
        course_id = getActivity().getIntent().getExtras().getInt("pk");

        Log.d("TOKEN", token);
        Log.d("COURSE_ID", Integer.toString(course_id));

        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_assignment_list, container, false);
        listView = (ListView) view.findViewById(R.id.AssignmentListView);

        AssignmentThread a = new AssignmentThread();
        new Thread(a, "AssignmentThread").start();

//        Log.d("Assignment thread","YES");

        // Inflate the layout for this fragment
        return view;
    }

    public void createAssignmentView() {

        if (assignments.length == 0) {
            // create a dummy assignment
            assignments = new Assignment[1];
            assignments[0] = new Assignment();
            assignments[0].name = "Hurray! No assignments!";
            assignments[0].pk = -1;
        }
        assignmentadapter = new AssignmentAdapter(getActivity().getApplicationContext(), R.layout.textviewxml, assignments);
        listView.setAdapter(assignmentadapter);
    }
}
