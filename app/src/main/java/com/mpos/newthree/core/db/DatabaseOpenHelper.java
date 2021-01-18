package com.mpos.newthree.core.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "contacts.db";
    private static final String TABLE_ISO_KEY_DATA = "iso_keydata";
    private static final int DB_VERSION = 1;

    static final String KEY_ID = "id";
    private DatabaseAccess databaseAccess;
    static String thekey;

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_ISO_KEY_DATA);

    }

    public final static String CREATE_ISO_KEY_DATA =
            "Create table if not exists" + TABLE_ISO_KEY_DATA + "("+KEY_ID + " integer primary key autoincrement," +
                    "iso_name" + "char(40), DEFAULT etranzact"+
                    "iso_lmk" + "char(50),"+
                    "iso_kwk" + "char(50),"+
                    "iso_check_digit" + "char(20),"+
                    "iso_status" + "char(1) DEFAULT NULL,"+
                    "iso_created" + "datetime) DEFAULT NULL;";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists" + TABLE_ISO_KEY_DATA);


    }

    public String getKey (String keyName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select iso_kwk from contacts where id="+keyName+"", null );
        res.moveToFirst();
        if(res.moveToFirst()){
            thekey = res.getString(res.getColumnIndex("iso_kwk"));
        }
        return  thekey;
    }
}
