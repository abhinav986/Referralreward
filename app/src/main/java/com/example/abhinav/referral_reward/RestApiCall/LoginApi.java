package com.example.abhinav.referral_reward.RestApiCall;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;


import cz.msebera.android.httpclient.Header;

public class LoginApi {
    private static final String API_BASE_URL = "http://10.0.2.2:8000/";
    private AsyncHttpClient client;

    public LoginApi() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    public void frontend_login(RequestParams params,JsonHttpResponseHandler handler) {
        String url = getApiUrl("api/user/login");
        client.post(url , params, handler);

    }
    public void profile_edit(RequestParams params,int id, JsonHttpResponseHandler handler){
        String url = getApiUrl("api/user/profile-edit/"+id+"/");
        client.post(url,params, handler);
    }
    public void store_refferal(RequestParams params,int id, JsonHttpResponseHandler handler){
        String url = getApiUrl("api/refferal/add-refferal/"+id + "/");
        client.post(url,params,handler);
    }
    public void get_refferal(int id, JsonHttpResponseHandler handler){
        String url = getApiUrl("api/refferal/get-refferal/"+id+"/");
        client.get(url,handler);
    }
    public void delete_refferal(int id, JsonHttpResponseHandler handler){
        String url = getApiUrl("api/refferal/delete-refferal/"+id+"/");
        client.delete(url,handler);
    }
    public void edit_refferal(RequestParams params,int id, JsonHttpResponseHandler handler){
        String url = getApiUrl("api/refferal/edit-refferal/"+id+"/");
        client.post(url,params,handler);
    }
    public void get_work(JsonHttpResponseHandler handler){
        String url = getApiUrl("api/refferal/get-work/");
        client.get(url,handler);
    }
    public void get_UserWork(int id,JsonHttpResponseHandler handler){
        String url = getApiUrl("api/refferal/get-user-works/"+id+"/");
        client.get(url,handler);
    }

    public void add_UserWork(RequestParams params, int id, JsonHttpResponseHandler handler){
        String url = getApiUrl("api/refferal/add-work/"+id+"/");
        client.post(url,params,handler);
    }

    public void delete_UserWork(int id, JsonHttpResponseHandler handler){
        String url = getApiUrl("api/refferal/delete-user-work/"+id+"/");
        client.delete(url,handler);
    }
    public void edit_UserWork(RequestParams params,int id, JsonHttpResponseHandler handler){
        String url = getApiUrl("api/refferal/edit-user-work/"+id+"/");
        client.post(url,params,handler);
    }

    public void userRewardPoints(int id, JsonHttpResponseHandler handler){
        String url = getApiUrl("api/user/reward-points/"+id+"/");
        client.get(url,handler);
    }
}
