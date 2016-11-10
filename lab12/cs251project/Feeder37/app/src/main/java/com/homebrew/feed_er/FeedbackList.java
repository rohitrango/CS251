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
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class FeedbackList extends Fragment {

    // Feedback class
    public class Feedback {
        public String name;
        public int pk;
        public Date deadline;
        public String com;

        public Feedback() {
            name = "";
            pk = -1;
            com = "Incomplete";
        }

        @Override
        public String toString() {
            if(pk != -1) {
                return name + '\n' + com;
            }
            else {
                return name ;
            }
        }
    }

    String token;
    int course_id;
    Feedback[] feedbacks;
    ArrayAdapter<Feedback> adapter;


    public class FeedbackThread implements Runnable {

        @Override
        public void run() {
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            String url = getString(R.string.api_base_url) + "course_deadlines";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Make the json object
                            try {
                                Log.e("Resp",response);
                                SharedPreferences prefs = getActivity().getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                                prefs.edit().putString("course"+ course_id + "feedback", response).apply();

                                JSONObject json_obj = new JSONObject(response);
                                Log.e("JSON","1");
                                JSONArray complete_feedbacks = (JSONArray) json_obj.getJSONArray("complete");
                                JSONArray incomplete_feedbacks = (JSONArray) json_obj.getJSONArray("incomplete");
                                Log.e("JSON","2");
                                Log.e("JSON","3");
                                feedbacks = new Feedback[complete_feedbacks.length() + incomplete_feedbacks.length()];Log.e("JSON","4");
                                for (int i = incomplete_feedbacks.length(); i < feedbacks.length; i++) {
                                    JSONObject blob = (JSONObject) complete_feedbacks.get(i - incomplete_feedbacks.length());
                                    JSONObject fields = (JSONObject) blob.get("fields");
                                    feedbacks[i] = new Feedback();
                                    feedbacks[i].name = fields.getString("name");
                                    feedbacks[i].pk = blob.getInt("pk");
                                    feedbacks[i].com = "Complete";
                                    String unParsedDate[] = fields.getString("deadline").split("-");
                                    int yyyy = Integer.parseInt(unParsedDate[0]), mm = Integer.parseInt(unParsedDate[1]), dd = Integer.parseInt(unParsedDate[2].substring(0, 2));
                                    feedbacks[i].deadline = new Date(yyyy - 1900, mm - 1, dd);
                                }
                                Log.e("JSON","5");
                                for (int i = 0; i < incomplete_feedbacks.length(); i++) {
                                    JSONObject blob = (JSONObject) incomplete_feedbacks.get(i);
                                    JSONObject fields = (JSONObject) blob.get("fields");
                                    feedbacks[i] = new Feedback();
                                    feedbacks[i].name = fields.getString("name");
                                    feedbacks[i].pk = blob.getInt("pk");
                                    feedbacks[i].com = "Incomplete";
                                    String unParsedDate[] = fields.getString("deadline").split("-");
                                    int yyyy = Integer.parseInt(unParsedDate[0]), mm = Integer.parseInt(unParsedDate[1]), dd = Integer.parseInt(unParsedDate[2].substring(0, 2));
                                    feedbacks[i].deadline = new Date(yyyy - 1900, mm - 1, dd);

                                }
                                Log.e("JSON","6");

                                createFeedbackView();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("ERROR:", "ERROR in json response feedback");
                            }
                            // Display the first 500 characters of the response string.
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                    String response;

                    if(prefs.contains("course" + course_id + "feedback"))
                    {
                        try
                        {
                            response = prefs.getString("course" + course_id + "feedback", " ");
                            JSONObject json_obj = new JSONObject(response);
                            Log.e("JSON","1");
                            JSONArray complete_feedbacks = (JSONArray) json_obj.getJSONArray("complete");
                            JSONArray incomplete_feedbacks = (JSONArray) json_obj.getJSONArray("incomplete");
                            Log.e("JSON","2");
                            Log.e("JSON","3");
                            feedbacks = new Feedback[complete_feedbacks.length() + incomplete_feedbacks.length()];Log.e("JSON","4");
                            for (int i = incomplete_feedbacks.length(); i < feedbacks.length; i++) {
                                JSONObject blob = (JSONObject) complete_feedbacks.get(i - incomplete_feedbacks.length());
                                JSONObject fields = (JSONObject) blob.get("fields");
                                feedbacks[i] = new Feedback();
                                feedbacks[i].name = fields.getString("name");
                                feedbacks[i].pk = blob.getInt("pk");
                                feedbacks[i].com = "Complete";
                                String unParsedDate[] = fields.getString("deadline").split("-");
                                int yyyy = Integer.parseInt(unParsedDate[0]), mm = Integer.parseInt(unParsedDate[1]), dd = Integer.parseInt(unParsedDate[2].substring(0, 2));
                                feedbacks[i].deadline = new Date(yyyy - 1900, mm - 1, dd);
                            }
                            Log.e("JSON","5");
                            for (int i = 0; i < incomplete_feedbacks.length(); i++) {
                                JSONObject blob = (JSONObject) incomplete_feedbacks.get(i);
                                JSONObject fields = (JSONObject) blob.get("fields");
                                feedbacks[i] = new Feedback();
                                feedbacks[i].name = fields.getString("name");
                                feedbacks[i].pk = blob.getInt("pk");
                                feedbacks[i].com = "Incomplete";
                                String unParsedDate[] = fields.getString("deadline").split("-");
                                int yyyy = Integer.parseInt(unParsedDate[0]), mm = Integer.parseInt(unParsedDate[1]), dd = Integer.parseInt(unParsedDate[2].substring(0, 2));
                                feedbacks[i].deadline = new Date(yyyy - 1900, mm - 1, dd);

                            }
                            Log.e("JSON","6");

                            createFeedbackView();
                        }
                        catch (Exception e)
                        {

                        }
                    }
                    Log.e("ERROR:", "Error connecting feedback.");

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("course_id", Integer.toString(course_id));
//                    Log.d("TOKEN",token);
//                    Log.d("ID:",Integer.toString(course_id));
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

    // make the adapter here
    public class FeedbackAdapter extends ArrayAdapter<Feedback> {


        public FeedbackAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public FeedbackAdapter(Context context, int resource, Feedback[] items) {
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

            final Feedback p = getItem(position);

            if (p != null) {
                TextView tt = (TextView) v.findViewById(R.id.courseTextView);
                tt.setText(p.toString());

                if (!p.name.equals("") && p.pk != -1) {
                    tt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(p.com.equals("Complete"))
                            {
                                Toast toast = Toast.makeText(context, "You have already submitted this Feedback.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                Intent intent = new Intent(getContext(), FeedbackDetail.class);
                                intent.putExtra("token", token);
                                intent.putExtra("pk", p.pk);
                                intent.putExtra("course_id", course_id);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }

            return v;
        }
    }

    // Create view here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_feedback_list, container, false);
        listView = (ListView) view.findViewById(R.id.FeedbackListView);

        token = getActivity().getIntent().getExtras().getString("token");
        course_id = getActivity().getIntent().getExtras().getInt("pk");

        FeedbackThread f = new FeedbackThread();
        new Thread(f, "FeedbackThread").start();
        return view;
    }


    public void createFeedbackView() {
        if(feedbacks.length == 0) {
            feedbacks = new Feedback[1];
            feedbacks[0].name = "Hurray! No feedbacks!";
            feedbacks[0].pk = -1;
        }
        adapter = new FeedbackAdapter(getActivity().getApplicationContext(), R.layout.textviewxml, feedbacks);
        listView.setAdapter(adapter);
    }

}
