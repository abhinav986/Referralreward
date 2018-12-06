package com.example.abhinav.referral_reward.models;

import java.io.Serializable;
import java.util.Date;

public class UserWorks implements Serializable {
    private int id;
    private String comments;
    private int isVerified;
    private Date created_at;
    private String workType;

    public UserWorks(int id, String comments, int isVerified, Date created_at, String workType){
        this.id = id;

        this.comments = comments;
        this.isVerified = isVerified;
        this.created_at = created_at;
        this.workType = workType;
    }
    public  UserWorks(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }
}
