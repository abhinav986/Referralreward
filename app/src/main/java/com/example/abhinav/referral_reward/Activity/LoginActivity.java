package com.example.abhinav.referral_reward.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhinav.referral_reward.R;
import com.example.abhinav.referral_reward.RestApiCall.LoginApi;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity  {


    // UI references.
    private EditText username;
    private EditText password;
    private ProgressBar pb;
    private View mLoginFormView;
    LoginApi loginApi = null;
    SharedPreferences pref;
    boolean logedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //checking for loggedIn user
        pref = getApplicationContext().getSharedPreferences("MyPref",0);
        logedin = pref.getBoolean("isLoggedin",false);
        if(logedin != false){
            Intent intent = new Intent(getApplicationContext(),HomePage.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_login);

        // Set up the login form.
        pb = (ProgressBar) findViewById(R.id.login_progress);
        username = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        // clickListner on login button
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                pb.setVisibility(ProgressBar.VISIBLE);
                loginApi = new LoginApi();
                RequestParams params = new RequestParams();
                params.put("username",username.getText().toString());
                params.put("password",password.getText().toString());

                loginApi.frontend_login(params,new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if(response != null) {
                                pb.setVisibility(ProgressBar.INVISIBLE);
                                // toast message
                               // Toast.makeText(LoginActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();

                                if(Integer.parseInt(response.getString("status")) == 1){
                                    JSONObject obj = response.getJSONObject("user");
                                    //store user data in session
                                    pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putInt("user_id",Integer.parseInt(obj.getString("user_id")));
                                    editor.putString("first_name", obj.getString("first_name"));
                                    editor.putString("last_name", obj.getString("last_name"));
                                    editor.putString("email", obj.getString("email"));
                                    editor.putString("mobile",obj.getString("mobile"));
                                    editor.putString("address",obj.getString("address"));
                                    editor.putString("location",obj.getString("location"));
                                    editor.putBoolean("isLoggedin",true);
                                    editor.commit();

                                    // redirect to homePage
                                    Intent intent = new Intent(getApplicationContext(),HomePage.class);
                                    startActivity(intent);

                                }
                                else {
                                    Toast.makeText(LoginActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            // Invalid JSON format, show appropriate error.
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        pb.setVisibility(ProgressBar.INVISIBLE);
                        Toast.makeText(LoginActivity.this,"Some Error Occured",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

