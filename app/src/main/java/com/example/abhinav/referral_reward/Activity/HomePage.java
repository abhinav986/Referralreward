package com.example.abhinav.referral_reward.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhinav.referral_reward.R;
import com.example.abhinav.referral_reward.RestApiCall.LoginApi;
import com.example.abhinav.referral_reward.models.Users;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences pref;
    TextView name, email;
    String first_name, email_id;
    Button btm;
    EditText comments;
    LoginApi apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check login user
        pref = getApplicationContext().getSharedPreferences("MyPref",0);
        if(pref.getBoolean("isLoggedin",false) == false){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        name = (TextView) header.findViewById(R.id.name);
        email = (TextView) header.findViewById(R.id.email);

        // retriving data from shredPreference
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        first_name = pref.getString("first_name", null);
        email_id = pref.getString("email",null);

        //setting data to navigation view
        name.setText(first_name);
        email.setText(email_id);

        //reffreal store api call
        refferalStore();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.bell) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_details) {
            Intent intent = new Intent(getApplicationContext(),MyDetails.class);
            startActivity(intent);

        } else if (id == R.id.my_works) {
            Intent intent = new Intent(getApplicationContext(),MyWorkList.class);
            startActivity(intent);

        } else if (id == R.id.my_referrals) {
            Intent intent = new Intent(getApplicationContext(),RefferalListView.class);
            startActivity(intent);

        } else if (id == R.id.my_rewards) {
            Intent intent = new Intent(getApplicationContext(), MyRewards.class);
            startActivity(intent);

        } else if (id == R.id.sign_out){
            pref = getApplicationContext().getSharedPreferences("MyPref",0);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void refferalStore(){
        btm = (Button) findViewById(R.id.refferal_submit);
        btm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        comments = (EditText) findViewById(R.id.comments);
                        pref = getApplicationContext().getSharedPreferences("MyPref",0);
                        int id = pref.getInt("user_id",0);

                        RequestParams params = new RequestParams();
                        params.put("comments",comments.getText().toString());
                        apiCall = new LoginApi();
                        apiCall.store_refferal(params,id,new JsonHttpResponseHandler(){

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try{
                                    if(Integer.parseInt(response.getString("status")) == 1){
                                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), response.getString("msg"),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), response.getString("error"),
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
        );
    }

    
}
