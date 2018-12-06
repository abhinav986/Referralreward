package com.example.abhinav.referral_reward.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhinav.referral_reward.R;
import com.example.abhinav.referral_reward.RestApiCall.LoginApi;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyRewards extends AppCompatActivity {
    TextView rewards_point,used_reward_points,total_reward_points;
    LoginApi loginApi;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rewards);

        rewards_point = (TextView)findViewById(R.id.rewards_point);
        used_reward_points = (TextView) findViewById(R.id.used_reward_points);
        total_reward_points = (TextView) findViewById(R.id.total_reward_points);

        //api call for setting reward value
        pref = getApplicationContext().getSharedPreferences("MyPref",0);
        int id = pref.getInt("user_id",0);
        loginApi = new LoginApi();
        loginApi.userRewardPoints(id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                int totalRewardPoints;
                int usedRewardPoints;
                try{
                    if(Integer.parseInt(response.getString("status")) == 1){
                        totalRewardPoints = response.getInt("totalRewardPoints");
                        usedRewardPoints = response.getInt("usedRewardPoints");

                        //setting reward value
                        used_reward_points.setText(Integer.toString(usedRewardPoints));
                        total_reward_points.setText(Integer.toString(totalRewardPoints));
                        rewards_point.setText(Integer.toString(totalRewardPoints - usedRewardPoints));
                    }else{
                        Toast.makeText(getApplicationContext(),response.getString("error"),
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(),"Error in api call",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
