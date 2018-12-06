package com.example.abhinav.referral_reward.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhinav.referral_reward.Adapter.RefferalAdapter;
import com.example.abhinav.referral_reward.R;
import com.example.abhinav.referral_reward.RestApiCall.LoginApi;
import com.example.abhinav.referral_reward.models.Refferal;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Date.*;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RefferalListView extends AppCompatActivity {
    ListView listView;
    RefferalAdapter refferalAdapter;
    ProgressBar progress;
    LoginApi loginApi;
    SharedPreferences pref;
    Refferal ref;
    ImageButton ibtm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refferal_list_view);
        listView = (ListView) findViewById(R.id.list_refferal);
        ArrayList<Refferal> refferals = new ArrayList<Refferal>();
        refferalAdapter = new RefferalAdapter(this,refferals);
        listView.setAdapter(refferalAdapter);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        fetchRefferals();
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Refferal referal = (Refferal) listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),RefferalDetailPage.class);
                intent.putExtra("Refferal",referal);
                startActivity(intent);
            }
        });
    }

    public void fetchRefferals(){
        progress.setVisibility(ProgressBar.VISIBLE);
        loginApi = new LoginApi();

        //getting id of user from sharedpreference
        pref = getApplicationContext().getSharedPreferences("MyPref",0);
        int id = pref.getInt("user_id",0);

        loginApi.get_refferal(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progress.setVisibility(ProgressBar.GONE);
                try {
                    JSONArray reff = null;
                    Date date = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    ArrayList<Refferal> reff_list = new ArrayList<Refferal>();
                    if(Integer.parseInt(response.getString("status")) == 1) {

                        reff = response.getJSONArray("refferals");
                        // Parse json array into array of model objects
                        for (int i=0;i<reff.length();i++){
                            ref = new Refferal();
                            ref.setComments(reff.getJSONObject(i).getString("comments"));
                            ref.setId(Integer.parseInt(reff.getJSONObject(i).getString("id")));

                            //changing string to date
                            try{
                                date = sdf.parse(reff.getJSONObject(i).getString("created_at"));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            ref.setCreated_at(date);
                            ref.setIsActive(reff.getJSONObject(i).getInt("isVerified"));
                            reff_list.add(ref);
                        }
                        // Remove all books from the adapter
                        refferalAdapter.clear();
                        // Load model objects into the adapter
                        for (Refferal refferal : reff_list) {
                            refferalAdapter.add(refferal); // add book through the adapter
                        }
                        refferalAdapter.notifyDataSetChanged();
                    }else{

                        Toast.makeText(getApplicationContext(),response.getString("error"),
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.setVisibility(ProgressBar.GONE);
                Toast.makeText(getApplicationContext(),"Error in api call",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


}
