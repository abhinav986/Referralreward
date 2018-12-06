package com.example.abhinav.referral_reward.models;

import java.io.Serializable;
import java.util.Date;

public class Refferal implements Serializable{
    private String comments;
    private Date created_at;
    private int id;
    private int isActive;

    public Refferal(String comments,Date created_at,int id, int isActive){
        this.comments = comments;
        this.created_at = created_at;
        this.id = id;
        this.isActive = isActive;
    }

    public  Refferal(){

    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {this.isActive = isActive;}
}
