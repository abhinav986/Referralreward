package com.example.abhinav.referral_reward.models;

import java.io.Serializable;

public class Work implements Serializable {
    private String name;

    public Work(String name) {
        this.name = name;
    }

    public  Work(){

    }
    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


