package com.mpos.newthree.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TECH-PC on 3/26/2018.
 */
public class UserData extends SQLiteOpenHelper {
    private static final String DATABASE = "ONEPAY.db";
    private static final int DATABASE_VERSION = 1;

    public UserData(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String category = "CREATE TABLE " + MetaData.TABLE_JSON_DATA + " (" +
                "" + MetaData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "" + MetaData.JSON_FILE + " TEXT NOT NULL " +
                ")";

        String parameter = "CREATE TABLE " + MetaData.TABLE_PARAMETER + " (" +
                "" + MetaData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "" + MetaData.PARAMETER_KEY + " TEXT NOT NULL, " +
                "" + MetaData.PARAMETER_VALUE + " TEXT NOT NULL " +
                ")";

        String login = "CREATE TABLE " + MetaData.TABLE_LOGIN + " (" +
                "" + MetaData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "" + MetaData.USER + " TEXT NOT NULL, " +
                "" + MetaData.PASS + " BLOB NOT NULL, " +
                "" + MetaData.IS_LOGIN + " INTEGER DEFAULT 0 " +
                ")";
        //    "create table userdetails(usersno integer primary code autoincrement,userid text not null ,
        // //username text not null,password text not null,photo BLOB,visibility text not null);";

        db.execSQL(category);
        db.execSQL(parameter);
        db.execSQL(login);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MetaData.TABLE_JSON_DATA );
        onCreate(db);
    }
}
