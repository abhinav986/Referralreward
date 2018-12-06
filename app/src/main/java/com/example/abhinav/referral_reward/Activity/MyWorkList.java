package com.example.abhinav.referral_reward.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhinav.referral_reward.Adapter.WorkAdapter;
import com.example.abhinav.referral_reward.R;
import com.example.abhinav.referral_reward.RestApiCall.LoginApi;
import com.example.abhinav.referral_reward.models.UserWorks;
import com.example.abhinav.referral_reward.models.Work;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class MyWorkList extends AppCompatActivity {
    ListView listView;
    WorkAdapter workAdapter;
    ProgressBar progress;
    LoginApi loginApi;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_work_list);
        listView = (ListView) findViewById(R.id.my_work_list);
        final ArrayList<UserWorks> works = new ArrayList<UserWorks>();
        workAdapter = new WorkAdapter(this,works);
        listView.setAdapter(workAdapter);
        progress = (ProgressBar) findViewById(R.id.progressBarWork);
        fetchUserWorks();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserWorks work = (UserWorks) listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),WorkDetailPage.class);
                intent.putExtra("work",work);
                startActivity(intent);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyWorkCreate.class);
                startActivity(intent);
            }
        });

    }

    public void fetchUserWorks(){
        progress.setVisibility(ProgressBar.VISIBLE);
        loginApi = new LoginApi();

        //getting id of user from sharedpreference
        pref = getApplicationContext().getSharedPreferences("MyPref",0);
        int id = pref.getInt("user_id",0);

        loginApi.get_UserWork(id,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progress.setVisibility(ProgressBar.GONE);
                try{
                    JSONArray userWorks = null;
                    UserWorks work;
                    Date date = null;
                    ArrayList<UserWorks> work_list = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    if(Integer.parseInt(response.getString("status")) ==1 ){
                        userWorks = response.getJSONArray("works");
                        // Parse json array into array of model objects
                        for (int i=0;i<userWorks.length();i++){
                            work = new UserWorks();
                            work.setComments(userWorks.getJSONObject(i).getString("comments"));
                            work.setId(Integer.parseInt(userWorks.getJSONObject(i).getString("id")));

                            //changing string to date
                            try{
                                date = sdf.parse(userWorks.getJSONObject(i).getString("created_at"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            work.setCreated_at(date);
                            work.setIsVerified(userWorks.getJSONObject(i).getInt("isVerified"));
                            work_list.add(work);
                        }
                        // Remove all books from the adapter
                        workAdapter.clear();
                        // Load model objects into the adapter
                        for (UserWorks works : work_list) {
                            workAdapter.add(works); // add book through the adapter
                        }
                        workAdapter.notifyDataSetChanged();
                    }else{
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
                Toast.makeText(getApplicationContext(),"Error in api call", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
