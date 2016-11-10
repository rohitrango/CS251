package com.homebrew.feed_er;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CalendarTabFragment extends Fragment {

    public class Deadline {
        public String name;
        public Date deadline;
        public String type;
        public int pk;
    }

    public Deadline[] assigns;
    public Deadline[] comFBs;
    public Deadline[] incomFBs;
    private Map<Date, Deadline> impDates;
    private Map<Date, Drawable> backgroundForDateMap;
    private CaldroidFragment caldroidFragment;
    private String token;

    private OnFragmentInteractionListener mListener;

    public CalendarTabFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Boolean isNull = (getActivity() == null);
        Log.d("NULL", isNull.toString());

        View view = inflater.inflate(R.layout.fragment_calendar_tab, container, false);


        token = getActivity().getIntent().getExtras().getString("token");
        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, true);
        caldroidFragment.setArguments(args);
        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarView, caldroidFragment);
        t.commit();

        // set up filter

        Spinner spinner = (Spinner)view.findViewById(R.id.filterDropDown);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text =((Spinner)getView().findViewById(R.id.filterDropDown)).getSelectedItem().toString();
                DatesListGetter datesListGetter = new DatesListGetter(text);
                new Thread(datesListGetter, "DatesListGetter").start();
                // Toast.makeText(getActivity().getApplicationContext(),text,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity().getApplicationContext(),"Please select an option.",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DatesListGetter datesListGetter = new DatesListGetter();
        new Thread(datesListGetter, "DatesListGetter").start();
    }

    private class DatesListGetter implements Runnable {

        public String text;
        public DatesListGetter() {
            text = "All";
        }
        public DatesListGetter(String t) {
            text = t;
        }

        @Override
        public void run() {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            String url = getString(R.string.api_base_url) + "dates";
            impDates = new HashMap<>();
            Log.d("CLG", "sending request...");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String responseString) {
                            Log.d("CLG", "response obtained...");
                            Log.d("Response",responseString);
                            Log.d("TOKEN",token);

                            try{
                                SharedPreferences prefs = getActivity().getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                                prefs.edit().putString("dates", responseString).apply();
                                call(responseString);
                            } catch (Exception e) {
//                                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPref.edit();
//                                editor.remove("token");
//                                editor.remove("fullname");
//                                editor.commit();
//
//                                Intent intent = new Intent(getActivity().getApplicationContext(),LoginActivity.class);
//                                intent.putExtra("status","multiple");
//                                startActivity(intent);
                                Log.d("Logout", "Logged Out");
                            }
                            //System.out.println("Response recorded");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //textView.setText("Please check your internet connection.");
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                    String responseString;

                    if(prefs.contains("dates"))
                        call(prefs.getString("dates", " "));
                    else
                    {
                        Toast.makeText(getContext(), "You are not connected to the internet.", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("DATES", "response not received");
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

        public void call(String responseString) {
            try {
                final JSONObject response = new JSONObject(responseString);
                final JSONArray assignments = response.getJSONArray("assignment");
                JSONArray complete_feedbacks = response.getJSONArray("complete");
                JSONArray incomplete_feedbacks = response.getJSONArray("incomplete");
                assigns = new Deadline[0];
                comFBs = new Deadline[0];
                incomFBs = new Deadline[0];
                backgroundForDateMap = new HashMap<>();
                impDates.clear();
                final Calendar c = Calendar.getInstance();

                if (text.equals("All") || text.equals("Assignments")) {
                    // Show in case of assignments
                    assigns = new Deadline[assignments.length()];
                    for (int i = 0; i < assignments.length(); i++) {
                        Deadline mydeadline = new Deadline();
                        JSONObject rDetail = (JSONObject) assignments.get(i);
                        JSONObject rFields = (JSONObject) rDetail.get("fields");
                        mydeadline.pk = rDetail.getInt("pk");
                        mydeadline.name = rFields.getString("name");
                        String dds[] = rFields.getString("deadline").substring(0, 10).split("-");
                        int yyyy = Integer.parseInt(dds[0]);
                        int mm = Integer.parseInt(dds[1]);
                        int dd = Integer.parseInt(dds[2].substring(0, 2));
                        mydeadline.deadline = new Date(yyyy - 1900, mm - 1, dd);
                        c.setTimeInMillis(mydeadline.deadline.getTime());
                        mydeadline.type = rDetail.getString("model");
                        backgroundForDateMap.put(c.getTime(), new ColorDrawable(Color.RED));
                        impDates.put(c.getTime(), mydeadline);
                        Log.d("JSON", "Added");
                        Log.e("Assignment", c.toString());
                        assigns[i] = mydeadline;

                    }
                }

                if (text.equals("All") || text.equals("Feedbacks")) {
                    // get only the feedbacks

                    comFBs = new Deadline[complete_feedbacks.length()];
                    for (int i = 0; i < complete_feedbacks.length(); i++) {
                        Deadline mydeadline = new Deadline();
                        JSONObject rDetail = (JSONObject) complete_feedbacks.get(i);
                        JSONObject rFields = (JSONObject) rDetail.get("fields");
                        mydeadline.name = rFields.getString("name");
                        mydeadline.pk = rDetail.getInt("pk");
                        String dds[] = rFields.getString("deadline").substring(0, 10).split("-");
                        int yyyy = Integer.parseInt(dds[0]);
                        int mm = Integer.parseInt(dds[1]);
                        int dd = Integer.parseInt(dds[2].substring(0, 2));
                        mydeadline.deadline = new Date(yyyy - 1900, mm - 1, dd);
                        c.setTimeInMillis(mydeadline.deadline.getTime());
                        mydeadline.type = rDetail.getString("model");
                        if (impDates.containsKey(c.getTime())) {
                            backgroundForDateMap.put(c.getTime(), new ColorDrawable(Color.LTGRAY));
                        } else
                            backgroundForDateMap.put(c.getTime(), new ColorDrawable(Color.YELLOW));
                        impDates.put(c.getTime(), mydeadline);
                        Log.d("JSON", "Added");
                        comFBs[i] = mydeadline;
                    }

                    incomFBs = new Deadline[incomplete_feedbacks.length()];
                    for (int i = 0; i < incomplete_feedbacks.length(); i++) {
                        Deadline mydeadline = new Deadline();
                        JSONObject rDetail = (JSONObject) incomplete_feedbacks.get(i);
                        JSONObject rFields = (JSONObject) rDetail.get("fields");
                        mydeadline.name = rFields.getString("name");
                        mydeadline.pk = rDetail.getInt("pk");
                        String dds[] = rFields.getString("deadline").substring(0, 10).split("-");
                        int yyyy = Integer.parseInt(dds[0]);
                        int mm = Integer.parseInt(dds[1]);
                        int dd = Integer.parseInt(dds[2].substring(0, 2));
                        mydeadline.deadline = new Date(yyyy - 1900, mm - 1, dd);
                        c.setTimeInMillis(mydeadline.deadline.getTime());
                        mydeadline.type = rDetail.getString("model");
                        if (impDates.containsKey(c.getTime())) {
                            backgroundForDateMap.put(c.getTime(), new ColorDrawable(Color.DKGRAY));
                        } else backgroundForDateMap.put(c.getTime(), new ColorDrawable(Color.CYAN));
                        impDates.put(c.getTime(), mydeadline);
                        Log.d("JSON", "Added");
                        incomFBs[i] = mydeadline;
                    }
                }
                //listeners
                final CaldroidListener listener = new CaldroidListener() {
                    @Override
                    public void onSelectDate(final Date date, View view) {

                        // add the feedbacks
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.eventsList);
                        Button t;

                        //Log.d("SELECT DATE", date.toString());
                        if (impDates.containsKey(date)) {

                            if (layout.getChildCount() > 0)
                                layout.removeAllViews();

                            final List<Deadline> assignmentsList = new ArrayList<>();
                            final List<Deadline> comFBList = new ArrayList<>();
                            final List<Deadline> incomFBList = new ArrayList<>();

                            Log.e("popup", "1");
                            Log.e("popup", "1");

                            for (int i = 0; i < assigns.length; i++) {
                                if (assigns[i].deadline.equals(date)) {
                                    System.out.println(date);
                                    assignmentsList.add(assigns[i]);
                                    Log.e("popup", assigns[i].toString());
                                }
                            }

                            for (int i = 0; i < comFBs.length; i++) {
                                if (comFBs[i].deadline.equals(date)) {
                                    comFBList.add(comFBs[i]);
                                    Log.e("popup", comFBs[i].toString());
                                }
                            }
                            for (int i = 0; i < incomFBs.length; i++) {
                                if (incomFBs[i].deadline.equals(date)) {
                                    incomFBList.add(incomFBs[i]);
                                    Log.e("popup", incomFBs[i].toString());
                                }
                            }

                            Log.e("HERE", "HERE");
                            for (int i = 0; i < assignmentsList.size(); i++) {
                                Log.d("a", "s");
                                final int ii = i;
                                t = new Button(getActivity().getApplicationContext());
                                t.setText("ASSIGNMENT: " + assignmentsList.get(i).name);
                                t.setTextColor(Color.BLACK);
                                t.setTextSize(15);
                                t.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity().getApplicationContext(), AssignmentDetail.class);
                                        intent.putExtra("token", token);
                                        intent.putExtra("pk", assignmentsList.get(ii).pk);
                                        startActivity(intent);
                                    }
                                });
                                t.setPadding(0, 10, 0, 10);
                                t.setLayoutParams(layoutParams);
                                layout.addView(t);
                            }

                            for (int i = 0; i < comFBList.size(); i++) {
                                final int ii = i;
                                Log.d("a", "s");
                                t = new Button(getActivity().getApplicationContext());
                                t.setText("FEEDBACK: " + comFBList.get(i).name);
                                t.setTextColor(Color.BLACK);
                                t.setTextSize(15);
                                t.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(getActivity().getApplicationContext(), "You have already filled this form", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                t.setPadding(0, 10, 0, 10);
                                t.setLayoutParams(layoutParams);
                                layout.addView(t);
                            }
                            for (int i = 0; i < incomFBList.size(); i++) {
                                final int ii = i;
                                Log.d("a", "s");
                                t = new Button(getActivity().getApplicationContext());
                                t.setText("FEEDBACK: " + incomFBList.get(i).name);
                                t.setTextColor(Color.BLACK);
                                t.setTextSize(15);
                                t.setPadding(0, 10, 0, 10);
                                t.setLayoutParams(layoutParams);
                                t.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Date now = new Date();
                                        if (incomFBList.get(ii).deadline.compareTo(now) < 0) {
                                            Toast.makeText(getActivity().getApplicationContext(), "The deadline for the feedback has passed.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent intent = new Intent(getActivity().getApplicationContext(), FeedbackDetail.class);
                                            intent.putExtra("token", token);
                                            intent.putExtra("pk", incomFBList.get(ii).pk);
                                            startActivity(intent);
                                        }

                                    }
                                });
                                layout.addView(t);
                            }
                        }
                    }

                };

                caldroidFragment.setCaldroidListener(listener);

                caldroidFragment.setBackgroundDrawableForDates(backgroundForDateMap);
                caldroidFragment.refreshView();
            }
            catch (Exception e)
            {

            }
        }

    }


    // : Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
