package com.example.abhinav.referral_reward.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.abhinav.referral_reward.R;
import com.example.abhinav.referral_reward.models.Work;

import java.util.ArrayList;

public class AddWorkDropDown extends ArrayAdapter<Work> {
    private Context context;
    private ArrayList<Work> work;
    public Resources res;
    Work currRowVal = null;
    LayoutInflater inflater;

    public AddWorkDropDown(Context context,
                         int textViewResourceId, ArrayList<Work> work) {
        super(context, textViewResourceId, work);
        this.context = context;
        this.work = work;
    }

    @Override
    public Work getItem(int position){
        currRowVal = null;
        currRowVal = (Work) work.get(position);
        return currRowVal;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        currRowVal = null;
        currRowVal = (Work) work.get(position);
        label.setText(currRowVal.getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        currRowVal = null;
        currRowVal = (Work) work.get(position);
        label.setText(currRowVal.getName());

        return label;
    }
}
