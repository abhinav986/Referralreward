package com.example.abhinav.referral_reward.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhinav.referral_reward.R;
import com.example.abhinav.referral_reward.RestApiCall.LoginApi;
import com.example.abhinav.referral_reward.models.UserWorks;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class WorkDetailPage extends AppCompatActivity {
    TextView comments,created_at,status,id;
    Button btm;
    ImageButton ibtm;
    LoginApi loginApi;
    ImageButton edit,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_detail_page);
        Intent intent = getIntent();
        UserWorks work = (UserWorks) intent.getSerializableExtra("work");

        id = (TextView)findViewById(R.id.work_id);
        comments = (TextView)findViewById(R.id.comments);
        created_at = (TextView) findViewById(R.id.created_at);
        status = (TextView) findViewById(R.id.status);
        edit = (ImageButton) findViewById(R.id.edit_work);
        delete = (ImageButton) findViewById(R.id.delete_work);

        //setting value of textView
        id.setText(Integer.toString(work.getId()));
        comments.setText(work.getComments());

        //setting status
        if(work.getIsVerified() == 0){
            status.setText("Pending");

        }else if(work.getIsVerified() == 1){
            status.setText("Rewarded");
            edit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);

        }else if(work.getIsVerified() == -1){
            status.setText("Rejected");
            edit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }

        //change date format
        Calendar cal = Calendar.getInstance();
        cal.setTime(work.getCreated_at());
        String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
        created_at.setText(formatedDate);

        onDeleteWork(work);
        onEditWork(work);
    }
    public void onDeleteWork(final UserWorks work){
        ibtm = (ImageButton) findViewById(R.id.delete_work);
        ibtm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(WorkDetailPage.this);
                        a_builder.setMessage("Do You want to delete this Work?")
                                .setCancelable(false)
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int id = work.getId();
                                        loginApi = new LoginApi();
                                        loginApi.delete_UserWork(id, new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                super.onSuccess(statusCode, headers, response);
                                                try {
                                                    if (Integer.parseInt(response.getString("status")) == 1) {
                                                        Toast.makeText(getApplicationContext(), response.getString("msg"),
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), WorkDetailPage.class);
                                                        startActivity(intent);
                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(),response.getString("error"),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                super.onFailure(statusCode, headers, responseString, throwable);
                                                Toast.makeText(getApplicationContext(), "Error in api call", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Alert!!");
                        alert.show();
                    }
                }
        );
    }
    public void onEditWork(final UserWorks work){
        ibtm = (ImageButton) findViewById(R.id.edit_work);
        ibtm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),EditWork.class);
                        intent.putExtra("work",work);
                        startActivity(intent);
                    }
                }
        );
    }

}
