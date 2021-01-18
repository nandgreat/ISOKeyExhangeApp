package com.mpos.newthree.dao;

import android.content.SharedPreferences;

import com.mpos.newthree.activity.MainActivity;


/**
 * Created by AccessTech on 7/25/2017.
 */

public class Data {

    public static String getJustT() {
        SharedPreferences preferences= MainActivity.context.getSharedPreferences("temp", MainActivity.context.MODE_PRIVATE);

        return preferences.getString("justT",null);
    }

    public static void setJustT(String justT) {
        SharedPreferences preferences = MainActivity.context.getSharedPreferences("temp", MainActivity.context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("justT", justT);
        editor.commit();
    }
}
