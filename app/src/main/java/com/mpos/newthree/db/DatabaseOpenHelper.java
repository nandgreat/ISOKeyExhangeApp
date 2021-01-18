package com.mpos.newthree.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "keyExchange.db";
    private static final String TABLE_ISO_KEY_DATA = "keyvalue";
    private static final int DB_VERSION = 1;

    static final String KEY_ID = "id";
    private DatabaseAccess databaseAccess;
    static String thekey;

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("hhah");
        db.execSQL(CREATE_ISO_KEY_DATA);

    }

    public final static String CREATE_ISO_KEY_DATA =
            "Create table if not exists" + TABLE_ISO_KEY_DATA + "("+KEY_ID + " integer primary key autoincrement," +

                    "key" + "char(50),"+
                    "type" + "char(20),"
                    ;

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("drop table if exists " + TABLE_ISO_KEY_DATA);


    }

    public String getKey (String keyName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select key from keyvalue where id="+keyName+"", null );
        res.moveToFirst();
        if(res.moveToFirst()){
            thekey = res.getString(res.getColumnIndex("iso_kwk"));
        }
        return  thekey;
    }
}
