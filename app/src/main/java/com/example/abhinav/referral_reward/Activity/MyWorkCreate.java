package com.example.abhinav.referral_reward.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.abhinav.referral_reward.Adapter.AddWorkDropDown;
import com.example.abhinav.referral_reward.R;
import com.example.abhinav.referral_reward.RestApiCall.LoginApi;
import com.example.abhinav.referral_reward.models.Refferal;
import com.example.abhinav.referral_reward.models.Work;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyWorkCreate extends AppCompatActivity {
    Spinner spinner;
    LoginApi loginApi;
    Work work = null;
    ArrayList<Work> works_list = new ArrayList<>();
    SharedPreferences pref;
    EditText comments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_work_create);

        //api call for work list
        loginApi = new LoginApi();
        loginApi.get_work(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray wor;

                try{
                    if(Integer.parseInt(response.getString("status")) == 1){
                        wor = response.getJSONArray("works");
                        // Parse json array into array of model objects
                        for (int i=0;i<wor.length();i++){
                            work = new Work(wor.getJSONObject(i).getString("name"));

                            works_list.add(work);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Error in api call",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(),"Error in api call",Toast.LENGTH_SHORT).show();
            }
        });
        //workAdapter = new AddWorkDropDown(this,android.R.layout.simple_spinner_item, works_list);
        Work w1 = new Work("Select Work");
        works_list.add(w1);
        spinner = (Spinner) findViewById(R.id.dropdown_work);
        ArrayAdapter<Work> adapter = new ArrayAdapter<Work>(this,
                R.layout.support_simple_spinner_dropdown_item,works_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Work work = (Work) spinner.getSelectedItem();
        System.out.println(work);
        Button btm = ( Button) findViewById(R.id.submit_work);
        btm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Work work = (Work) spinner.getSelectedItem();
                        comments = (EditText)findViewById(R.id.comments);
                        if(work.getName() == "Select Work"){
                            Toast.makeText(getApplicationContext(),"Please select Work",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            //getting user_id from shared preference
                            pref = getApplicationContext().getSharedPreferences("MyPref",0);
                            int id = pref.getInt("user_id",0);

                            loginApi = new LoginApi();
                            RequestParams params = new RequestParams();
                            params.put("workType",work.getName());
                            params.put("comments",comments.getText().toString());

                            loginApi.add_UserWork(params,id,new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    try{
                                        if(Integer.parseInt(response.getString("status")) == 1){
                                            Toast.makeText(getApplicationContext(),response.getString("msg"),
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),MyWorkList.class);
                                            startActivity(intent);
                                        }
                                        else{
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
                }
        );

    }
    public void getSelectedWork(){


    }
}
