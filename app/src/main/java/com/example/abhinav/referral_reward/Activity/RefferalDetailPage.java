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
import com.example.abhinav.referral_reward.models.Refferal;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class RefferalDetailPage extends AppCompatActivity {
    TextView id,comment, created_at,status;
    Button btm;
    ImageButton ibtm;
    LoginApi loginApi;
    ImageButton edit,delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refferal_detail_page);
        Intent intent = getIntent();
        Refferal refferal = (Refferal)intent.getSerializableExtra("Refferal");

        id = (TextView) findViewById(R.id.refferal_id);
        comment = (TextView) findViewById(R.id.comments);
        created_at = (TextView) findViewById(R.id.created_at);
        status = (TextView) findViewById(R.id.status);
        edit = (ImageButton) findViewById(R.id.edit_refferal);
        delete = (ImageButton) findViewById(R.id.delete_refferal);

        // setting value in textView
        id.setText(Integer.toString(refferal.getId()));
        comment.setText(refferal.getComments());


        //change date format
        Calendar cal = Calendar.getInstance();
        cal.setTime(refferal.getCreated_at());
        String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
        created_at.setText(formatedDate);

        //setting status
        if(refferal.getIsActive() == 0){
            status.setText("Pending");

        }else if(refferal.getIsActive() == 1){
            status.setText("Rewarded");
            edit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);

        }else if(refferal.getIsActive() == -1){
            status.setText("Rejected");
            edit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }
        onDeleteRefferal(refferal);
        onEditRefferal(refferal);

    }

    public void onDeleteRefferal(final Refferal refferal){
        ibtm = (ImageButton) findViewById(R.id.delete_refferal);
        ibtm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(RefferalDetailPage.this);
                        a_builder.setMessage("Do You want to delete this Post?")
                                .setCancelable(false)
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int id = refferal.getId();
                                        loginApi = new LoginApi();
                                        loginApi.delete_refferal(id, new JsonHttpResponseHandler(){
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                super.onSuccess(statusCode, headers, response);
                                                try{
                                                    if(Integer.parseInt(response.getString("status")) == 1){
                                                        Toast.makeText(getApplicationContext(),response.getString("msg"),
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(),RefferalListView.class);
                                                        startActivity(intent);
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
                                                Toast.makeText(getApplicationContext(),"Error in api call",Toast.LENGTH_SHORT).show();
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
    public void onEditRefferal(final Refferal refferal){
        ibtm = (ImageButton) findViewById(R.id.edit_refferal);
        ibtm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),EditRefferal.class);
                        intent.putExtra("Refferal",refferal);
                        startActivity(intent);
                    }
                }
        );
    }
}
