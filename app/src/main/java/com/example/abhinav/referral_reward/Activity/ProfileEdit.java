package com.example.abhinav.referral_reward.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhinav.referral_reward.R;
import com.example.abhinav.referral_reward.RestApiCall.LoginApi;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileEdit extends AppCompatActivity {
    SharedPreferences pref;
    String first_nameP,last_nameP, mobileP, addressP, locationP;
    EditText first_name, last_name, address, mobile, location;
    Button btm;
    LoginApi apicall;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check login user
        pref = getApplicationContext().getSharedPreferences("MyPref",0);
        if(pref.getBoolean("isLoggedin",false) == false){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_profile_edit);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        address = (EditText) findViewById(R.id.address);
        mobile = (EditText) findViewById(R.id.mobile);
        location = (EditText) findViewById(R.id.location);

        //getting data from Sharedpreference
        pref = getApplicationContext().getSharedPreferences("MyPref",0);
        first_nameP = pref.getString("first_name",null);
        last_nameP = pref.getString("last_name",null);
        mobileP = pref.getString("mobile",null);
        addressP = pref.getString("address",null);
        locationP = pref.getString("location",null);

        //setting data in edit profile
        first_name.setText(first_nameP);
        last_name.setText(last_nameP);
        mobile.setText(mobileP);
        address.setText(addressP);
        location.setText(locationP);
    }

    public void editProfile(View view){
        pref = getApplicationContext().getSharedPreferences("MyPref",0);
        pb = (ProgressBar) findViewById(R.id.edit_profile_Bar);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        address = (EditText) findViewById(R.id.address);
        mobile = (EditText) findViewById(R.id.mobile);
        location = (EditText) findViewById(R.id.location);

        btm = (Button) findViewById(R.id.edit_profile);

        btm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pb.setVisibility(ProgressBar.VISIBLE);
                        RequestParams params = new RequestParams();
                        params.put("first_name",first_name.getText().toString());
                        params.put("last_name",last_name.getText().toString());
                        params.put("mobile",mobile.getText().toString());
                        params.put("address",address.getText().toString());
                        params.put("location",location.getText().toString());

                        //getting id of user from sharedPreference
                        int id = pref.getInt("user_id",0);
                        // api call for update profile
                        apicall = new LoginApi();
                        apicall.profile_edit(params,id, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try{
                                    if(response != null){
                                        if(Integer.parseInt(response.getString("status")) == 1){
                                            JSONObject obj = response.getJSONObject("user");
                                            pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("first_name", obj.getString("first_name"));
                                            editor.putString("last_name", obj.getString("last_name"));
                                            editor.putString("mobile",obj.getString("mobile"));
                                            editor.putString("address",obj.getString("address"));
                                            editor.putString("location",obj.getString("location"));
                                            editor.commit();
                                            pb.setVisibility(ProgressBar.INVISIBLE);

                                            // Toast message
                                            Toast.makeText(getApplicationContext(),response.getString("msg"),
                                                    Toast.LENGTH_SHORT).show();
                                            //open previous detail page
                                            Intent intent = new Intent(getApplicationContext(),MyDetails.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            pb.setVisibility(ProgressBar.INVISIBLE);
                                            Toast.makeText(getApplicationContext(),response.getString("error"),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                pb.setVisibility(ProgressBar.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"Some error in api call",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );
    }
}
