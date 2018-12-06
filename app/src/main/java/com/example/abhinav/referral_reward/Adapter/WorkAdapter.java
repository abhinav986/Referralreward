package com.example.abhinav.referral_reward.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhinav.referral_reward.R;
import com.example.abhinav.referral_reward.models.UserWorks;
import com.example.abhinav.referral_reward.models.Work;

import java.util.ArrayList;
import java.util.Calendar;

public class WorkAdapter extends ArrayAdapter<UserWorks> {
    private static class ViewHolder {
        public TextView id;
        public TextView comment;
        public TextView created_at;
        public ImageView status;
    }

    public WorkAdapter(Context context, ArrayList<UserWorks> works){
        super(context, 0, works);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserWorks work = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.refferal_list, parent, false);
            viewHolder.id = (TextView) convertView.findViewById(R.id.refferal_id);
            viewHolder.comment = (TextView)convertView.findViewById(R.id.comments);
            viewHolder.created_at = (TextView)convertView.findViewById(R.id.created_at);
            viewHolder.status = (ImageView)convertView.findViewById(R.id.status);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.comment.setText(work.getComments());
        viewHolder.id.setText("ID:"+Integer.toString(work.getId()));

        //change date format
        Calendar cal = Calendar.getInstance();
        cal.setTime(work.getCreated_at());
        String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

        //set changed format date to viewHolder
        viewHolder.created_at.setText(formatedDate);
        // Return the completed view to render on screen

        //setting status image in viewHolder
        if(work.getIsVerified() == 0){
            viewHolder.status.setImageResource(R.drawable.process);
        }else if(work.getIsVerified() == 1){
            viewHolder.status.setImageResource(R.drawable.awarded);
        }else if(work.getIsVerified() == -1){
            viewHolder.status.setImageResource(R.drawable.rejected);
        }
        return convertView;
    }
}
