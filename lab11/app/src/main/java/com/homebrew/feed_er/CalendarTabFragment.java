package com.homebrew.feed_er;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarTabFragment extends Fragment {
    private Map<Date,String> impDates;
    private Map<Date, Drawable> backgroundForDateMap;
    private CaldroidFragment caldroidFragment;
    private String token;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CalendarTabFragment() {

    }

    private class DatesListGetter implements Runnable {
        public DatesListGetter() {
            Log.d("DLG", "DLG constructed");
        }

        @Override
        public void run() {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
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


    public static CalendarTabFragment newInstance(String param1, String param2) {
        CalendarTabFragment fragment = new CalendarTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Boolean isNull = (getActivity()==null);
        Log.d("NULL",isNull.toString());


        token = getActivity().getIntent().getExtras().getString("token");
        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, true);
        caldroidFragment.setArguments(args);
        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarView, caldroidFragment);
        t.commit();

        DatesListGetter datesListGetter = new DatesListGetter();
        new Thread(datesListGetter, "DatesListGetter").start();
        return inflater.inflate(R.layout.fragment_calendar_tab, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
