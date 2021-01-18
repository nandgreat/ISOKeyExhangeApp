package com.mpos.newthree.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpos.newthree.R;
import com.mpos.newthree.helper.Vehicle_category;

import java.util.List;

/**
 * Created by TECH-PC on 3/22/2018.
 */

public class SpinnerAdapter extends ArrayAdapter<Vehicle_category> {
    int groupid;
    Activity context;
    List<Vehicle_category> list;
    LayoutInflater inflater;
    public SpinnerAdapter(Activity context, int groupid, int id, List<Vehicle_category>
            list){
        super(context,id,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img);

        TextView textView=(TextView)itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getName());
        return itemView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img);

        TextView textView=(TextView)itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getName());
        return itemView;
    }
/*
    public View getView(int position, View convertView, ViewGroup parent ){
        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImageId());
        TextView textView=(TextView)itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getText());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent){
        return getView(position,convertView,parent);

    }*/
}
