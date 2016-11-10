package com.homebrew.feed_er;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private String fullname;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        try {
            String status = getIntent().getExtras().getString("status");
            if (status.equals("logout")) {
                TextView textView = (TextView) findViewById(R.id.invalid_login);
                textView.setText("Logged out successfully.");
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("token");
                editor.remove("fullname");
                editor.commit();
            } else if (status.equals("multiple")) {
                Log.e("MULTIPLE", "Why?");
                TextView textView = (TextView) findViewById(R.id.invalid_login);
                textView.setText("Logged out because you have logged in from some other device.");
            }
        } catch (Exception e) {
            Log.e("LO", "LOGOUT NS");
        }


        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "-1");
        if (sharedPref.contains("token")) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("fullname", sharedPref.getString("fullname", "Student"));
            startActivity(intent);
        }
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Log.d("LOGIN", "Username/Password format correct");
            Loginer loginer = new Loginer();
            new Thread(loginer, "Loginer").start();
        }
    }

    private class Loginer implements Runnable {
        public Loginer() {
            Log.d("DLG", "DLG constructed");
        }

        @Override
        public void run() {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getString(R.string.api_base_url) + "login";
            Log.d("URL", url);
            final String email = mEmailView.getText().toString();
            final String password = mPasswordView.getText().toString();
            Log.d("LOGIN", "sending request...");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String responseString) {
                            Log.d("LOGIN", responseString);
                            if (responseString.equals("-1")) {
                                Log.d("LOGIN", "Invalid up");
                                TextView textView = (TextView) findViewById(R.id.invalid_login);
                                textView.setText("Invalid username or password.");
                                textView.setVisibility(View.VISIBLE);
                            } else {
                                String token, fullname;
                                try {
                                    Log.e("JSON", responseString);
                                    final JSONArray response = new JSONArray(responseString);
                                    Log.e("JSON", "1");
                                    JSONObject user = response.getJSONObject(0).getJSONObject("fields");
                                    Log.e("JSON", "2");
                                    fullname = user.getString("fullname");
                                    Log.e("JSON", "3");
                                    token = user.getString("token");
                                    Log.e("JSON", "4");

                                } catch (Exception e) {
                                    Log.e("JSON", "Login");
                                    token = null;
                                    fullname = null;
                                }


                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                if (token != null) {
                                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("token", token);
                                    editor.putString("fullname", fullname);
                                    editor.commit();

                                    intent.putExtra("token", token);
                                    intent.putExtra("fullname", fullname);
                                }
                                startActivity(intent);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //textView.setText("Please check your internet connection.");
                    Log.d("LOGIN", "response not received");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", email);
                    params.put("password", password);
//                    params.put("token","f57609bb7440377f34628ba65047537ed316d8d665e4eed899629a9e8e9f");
                    return params;
                }

            };

            queue.add(stringRequest);
        }
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);
        return mat.matches();
    }

    private boolean isPasswordValid(String password) {

        Log.d("Pass", "Password being checked");
        return password.length() >= 8;

    }

}

