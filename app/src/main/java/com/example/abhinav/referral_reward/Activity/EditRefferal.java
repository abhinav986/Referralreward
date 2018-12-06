package com.example.abhinav.referral_reward.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhinav.referral_reward.R;
import com.example.abhinav.referral_reward.RestApiCall.LoginApi;
import com.example.abhinav.referral_reward.models.Refferal;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class EditRefferal extends AppCompatActivity {
    EditText comments;
    Button btm;
    LoginApi loginApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_refferal);

        Intent intent = getIntent();
        Refferal refferal = (Refferal)intent.getSerializableExtra("Refferal");

        //getting element
        comments = (EditText) findViewById(R.id.comments);

        // setting comment value
        comments.setText(refferal.getComments());
        saveEditRefferal(refferal);

    }

    public void saveEditRefferal(final Refferal refferal){
        btm = (Button) findViewById(R.id.edit_refferal);
        btm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        comments = (EditText) findViewById(R.id.comments);
                        RequestParams params = new RequestParams();
                        params.put("comments",comments.getText().toString());
                        int id = refferal.getId();

                        // calling api for edit
                        loginApi = new LoginApi();
                        loginApi.edit_refferal(params, id, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    if(Integer.parseInt(response.getString("status")) == 1){
                                        refferal.setComments(response.getString("comments"));
                                        Intent intent = new Intent(getApplicationContext(),RefferalDetailPage.class);
                                        intent.putExtra("Refferal",refferal);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(),response.getString("msg"),
                                                Toast.LENGTH_SHORT).show();
                                    }else {
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
                                Toast.makeText(getApplicationContext(),"Api call error",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );
    }
}
