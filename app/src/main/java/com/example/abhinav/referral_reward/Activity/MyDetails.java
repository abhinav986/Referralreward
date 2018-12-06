package com.example.abhinav.referral_reward.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.abhinav.referral_reward.R;

public class MyDetails extends AppCompatActivity {
    SharedPreferences pref;
    TextView first_name, last_name, email_id, address, location, mobile;
    String first_nameP,last_nameP,email_idP,addressP,locationP,mobileP;
    Button btm;
    ImageButton ibtm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check login user
        pref = getApplicationContext().getSharedPreferences("MyPref",0);
        if(pref.getBoolean("isLoggedin",false) == false){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_my_details);

        first_name = (TextView) findViewById(R.id.first_name);
        last_name = (TextView) findViewById(R.id.last_name);
        email_id = (TextView) findViewById(R.id.email_id);
        address = (TextView) findViewById(R.id.address);
        location = (TextView) findViewById(R.id.location);
        mobile = (TextView) findViewById(R.id.mobile);

        //getting user data from sharedpreference
        pref = getApplicationContext().getSharedPreferences("MyPref",0);
        first_nameP = pref.getString("first_name",null);
        last_nameP = pref.getString("last_name",null);
        email_idP = pref.getString("email",null);
        mobileP = pref.getString("mobile",null);
        addressP = pref.getString("address",null);
        locationP = pref.getString("location",null);

        //setting profile data in activity
        first_name.setText(first_nameP);
        last_name.setText(last_nameP);
        email_id.setText(email_idP);
        mobile.setText(mobileP);
        address.setText(addressP);
        location.setText(locationP);

    }

    public void profileEditOnClick(View view){
        ibtm = (ImageButton) findViewById(R.id.profile_edit);
        ibtm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),ProfileEdit.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
