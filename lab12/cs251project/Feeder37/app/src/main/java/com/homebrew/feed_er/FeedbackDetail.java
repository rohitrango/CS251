package com.homebrew.feed_er;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
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

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.key;

public class FeedbackDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        course_id =  getIntent().getIntExtra("course_id", -1);
        pk =  getIntent().getIntExtra("pk", -1);
        token =  getIntent().getStringExtra("token");
        context = getApplicationContext();
        Log.d("Feed", ((Integer)course_id).toString());

        FeedbackGetter feedbackgetter = new FeedbackGetter();
        new Thread(feedbackgetter, "FeedbackGetter").start();

        setContentView(R.layout.activity_feedback_detail);

    }

    public class RateQuestion
    {
        public String name;
        public int pk;
        public int val;
    }

    public class McqQuestion
    {
        public String name;
        public  int pk;
        public McqOptions[] options;
        public int answer_pk;
    }

    public class ShortQuestion
    {
        public  String name;
        public String answer;
        int pk;
    }

    public class McqOptions
    {
        String name;
        int pk;
    }

    public McqQuestion[] mcq_ques_set;
    public RateQuestion[] rate_ques_set;
    public ShortQuestion[] short_ques_set;

    public int course_id;
    public int pk;
    public String token;
    public Context context;

    public class FeedbackGetter implements Runnable {

        @Override
        public void run() {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getString(R.string.api_base_url) + "course_feedback_detail";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try
                            {
                                SharedPreferences prefs = getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                                prefs.edit().putString("course"+ course_id + "feedback" + pk, response).apply();

                                call(response);

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                Log.e("JSON", "Bad parsing");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            SharedPreferences prefs = getSharedPreferences("com.homebrew.feed_er", Context.MODE_PRIVATE);
                            String response;
                            if(prefs.contains("course" + course_id + "feedback" + pk))
                            {
                                response = prefs.getString("course" + course_id + "feedback" + pk, " ");
                                call(response);
                            }
                            else
                            {
                                Toast.makeText(context, "You are not connected to the internet.", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("Feeder", "No Response");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("course_id", Integer.toString(course_id));
                    params.put("token", token);
                    params.put("feedback_id", ((Integer)pk).toString());
//                    Log.d("TOKEN",token);
//                    Log.d("ID:",Integer.toString(course_id));
                    return params;
                }

            };

            queue.add(stringRequest);
        }

        public void call(String response)
        {
            try {

                JSONObject ques_array = new JSONObject(response);
                final JSONArray mcq_ques = ques_array.getJSONArray("mcq_ques");
                final JSONArray rate_ques = ques_array.getJSONArray("rate_ques");
                final JSONArray short_ques = ques_array.getJSONArray("short_ques");
                short_ques_set = new ShortQuestion[short_ques.length()];
                mcq_ques_set = new McqQuestion[mcq_ques.length()];
                rate_ques_set = new RateQuestion[rate_ques.length()];
                Log.e("JSON", "5");
                Log.d("JSON", mcq_ques.toString());
                ViewGroup layout = (ViewGroup) findViewById(R.id.feedbackquestions);

                int textsize = 16;

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ViewGroup.LayoutParams editTextParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                final EditText[] editTexts = new EditText[short_ques.length()];
                for (int i = 0; i < short_ques.length(); i++) {
                    JSONObject ques = short_ques.getJSONObject(i);
                    ShortQuestion single = new ShortQuestion();
                    single.name = ques.getJSONObject("fields").getString("question");
                    single.pk = ques.getInt("pk");
                    short_ques_set[i] = single;
                    if (i == 0) {
                        TextView t = new TextView(getApplicationContext());
                        t.setLayoutParams(layoutParams);
                        t.setTextSize(textsize);
                        t.setText("Short questions");
                        layout.addView(t);
                        addDummy(layout);
                    }

                    TextView t = new TextView(getApplicationContext());
                    t.setLayoutParams(layoutParams);
                    t.setText(Integer.toString(i + 1) + ". " + single.name);
                    layout.addView(t);

                    editTexts[i] = new EditText(getApplicationContext());
                    editTexts[i].setBackgroundColor(Color.WHITE);
                    editTexts[i].setTextColor(Color.BLACK);
                    editTexts[i].setLayoutParams(editTextParams);
                    layout.addView(editTexts[i]);
                }

                Log.e("JSON", "6");


                final RadioGroup mcqAnswers[] = new RadioGroup[mcq_ques.length()];
                for (int i = 0; i < mcq_ques.length(); i++) {
                    JSONObject ques = mcq_ques.getJSONObject(i);
                    McqQuestion single = new McqQuestion();
                    single.name = ques.getJSONObject("fields").getString("question");
                    single.pk = ques.getInt("pk");
                    Log.e("Options", ques.toString());
                    JSONArray opt_array = ques.getJSONArray("options");
                    Log.e("Options", "here");


                    single.options = new McqOptions[opt_array.length()];
                    Log.e("JSON", "7");
                    for (int j = 0; j < opt_array.length(); j++) {
                        JSONObject opt = opt_array.getJSONObject(j);

                        McqOptions single_opt = new McqOptions();
                        single_opt.pk = opt.getInt("pk");
                        single_opt.name = opt.getJSONObject("fields").getString("text");

                        single.options[j] = single_opt;
                    }
                    Log.e("JSON", "8");
                    Log.e("JSON", ((Integer) opt_array.length()).toString());
                    mcq_ques_set[i] = single;


                    if (i == 0) {
                        TextView t = new TextView(getApplicationContext());
                        t.setLayoutParams(layoutParams);
                        t.setTextSize(textsize);
                        t.setText("Multiple Choice questions");
                        layout.addView(t);
                        addDummy(layout);
                    }


                    TextView t = new TextView(getApplicationContext());
                    t.setLayoutParams(new ViewGroup.LayoutParams(layoutParams));
                    t.setTextSize(textsize);
                    t.setText(Integer.toString(i + 1) + ". " + single.name);
                    layout.addView(t);


                    Log.e("MCQ", single.name + single.options[1].name + opt_array.length());
                    // Add the options
                    RadioGroup radioGroup = new RadioGroup(getApplicationContext());
                    for (int j = 0; j < opt_array.length(); j++) {
                        RadioButton r = new RadioButton(getApplicationContext());
                        r.setText(single.options[j].name);
                        Log.e("WHY", "WHY");
                        r.setLayoutParams(layoutParams);
                        r.setId(single.options[j].pk);
                        Log.e("MCQPK2", ((Integer) single.options[j].pk).toString());
                        Log.e("MCQPK3", ((Integer) r.getId()).toString());
                        Log.e("WHY", "WHY");

                        if (j == 0) {
                            r.setChecked(true);
                        }
                        Log.e("WHY2", "WHY2");
                        radioGroup.addView(r);
                        Log.e("WHY3", "WHY3");
                    }

                    Log.e("WHY", "WHY");
                    mcqAnswers[i] = radioGroup;
                    layout.addView(radioGroup);


                }
                Log.e("JSON", "9");

                ////////////////////////////////////

                final RatingBar[] ratingAnswers = new RatingBar[rate_ques.length()];
                for (int i = 0; i < rate_ques.length(); i++) {
                    JSONObject ques = rate_ques.getJSONObject(i);
                    RateQuestion single = new RateQuestion();
                    single.name = ques.getJSONObject("fields").getString("question");
                    single.pk = ques.getInt("pk");
                    rate_ques_set[i] = single;
                    if (i == 0) {
                        TextView t = new TextView(getApplicationContext());
                        t.setLayoutParams(layoutParams);
                        t.setTextSize(textsize);
                        t.setText("Rating based questions");
                        layout.addView(t);
                        addDummy(layout);
                    }

                    TextView t = new TextView(getApplicationContext());
                    t.setLayoutParams(layoutParams);
                    t.setTextSize(textsize);
                    t.setText(Integer.toString(i + 1) + ". " + single.name);
                    layout.addView(t);


                    // Add the rating bar here
                    RatingBar ratingBar = new RatingBar(getApplicationContext(), null);
                    ratingBar.setStepSize(1.0f);
                    ratingBar.setLayoutParams(layoutParams);
                    ratingBar.setNumStars(5);
                    ratingBar.setRating(3);

                    // give it an ID equal to the question ID
                    ratingAnswers[i] = ratingBar;
                    layout.addView(ratingBar);
                }

                Button submit = new Button(getApplicationContext(), null);
//                                submit.setLayoutParams(layoutParams);
                submit.setText("Submit");
                submit.setBackgroundColor(Color.rgb(66, 134, 244));
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < short_ques.length(); i++) {
                            short_ques_set[i].answer = editTexts[i].getText().toString();
                        }

                        for (int i = 0; i < rate_ques.length(); i++) {
                            rate_ques_set[i].val = (int) ratingAnswers[i].getRating();
                        }

                        for (int i = 0; i < mcq_ques.length(); i++) {
                            mcq_ques_set[i].answer_pk = mcqAnswers[i].getCheckedRadioButtonId();
                            Log.e("MCQPK", ((Integer) mcqAnswers[i].getCheckedRadioButtonId()).toString());
                        }

                        Log.d("MCQ", mcq_ques_set.toString());
                        Log.d("Rate", rate_ques_set.toString());
                        Log.d("Single", short_ques_set.toString());

                        FeedbackSender sender = new FeedbackSender();
                        new Thread(sender, "Feedback Sender").start();
                    }
                });
                layout.addView(submit);

                Log.d("JSON", "Hurray works");
            }
            catch(Exception e)
            {

            }
        }
    }

        public void addDummy(ViewGroup layout) {
            View dummy;
            dummy = new View(getApplicationContext());
            dummy.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            ));
            dummy.setPadding(0,10,0,10);
            dummy.setBackgroundColor(Color.parseColor("#999999"));
            layout.addView(dummy);
        }


    public class FeedbackSender implements Runnable
    {
        @Override
        public void run() {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getString(R.string.api_base_url) + "submit_feedback_form";

            StringRequest stringrequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast toast = Toast.makeText(context, "Response Recorded Successfully", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            i.putExtra("token", token);
                            i.putExtra("fullname", "back");
                            startActivity(i);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast = Toast.makeText(context, "Some error occured. Please check your internet connection.", Toast.LENGTH_SHORT);
                            toast.show();
//                            error.printStackTrace();
                            Log.e("POST", "post request did not succeed");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    // Parameters will be feedback_id, pk with answers, token.
                    params.put("token", token);
                    params.put("feedback_id", ((Integer)pk).toString());

                    for(RateQuestion single: rate_ques_set)
                    {
                        params.put(((Integer)single.pk).toString(), ((Integer)single.val).toString());
                    }

                    for(ShortQuestion single: short_ques_set)
                    {
                        params.put(((Integer)single.pk).toString(), single.answer);
                    }

                    for(McqQuestion single: mcq_ques_set)
                    {
                        params.put(((Integer)single.pk).toString(), ((Integer)single.answer_pk).toString());
                    }

                    return params;
                }
            };

            queue.add(stringrequest);
        }
    }

}
