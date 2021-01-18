package com.mpos.newthree.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    private DatabaseOpenHelper databaseOpenHelper;


    private static Cursor rs = null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        System.out.println("write");
        this.database = openHelper.getWritableDatabase();

    }

    public boolean updateKey(String key, String checkDigit, String keyName) {
        try {
            System.out.println("in update");
            this.open();
            ContentValues values = new ContentValues();
            values.put("id", key);
            values.put("key", checkDigit);
            values.put("type", keyName);
            database.update("keyvalue", values, DatabaseOpenHelper.KEY_ID, new String[]{String.valueOf(keyName)});
        }catch(SQLiteException ex){
            System.out.println("failed="+ex.getMessage());
        }
        return true;
    }

    public void updateKey(){
        this.open();

    }

    public String getKey(String keyName){
        String retval = null;
        try{
            String sql = "SELECT iso_id FROM iso_Table WHERE iso_name = ?";
            SQLiteStatement statement =database.compileStatement(sql);
            statement.bindString(1, keyName);
            statement.execute();
        }catch (Exception e){e.printStackTrace();}
        return retval;
    }

    public void close(){
        if(database !=null){
          //  exportToSD();
            this.database.close();
        }
    }
    public void exportToSD() {
        try {
            Log.i("cl","saved");
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.mpos.sdk//databases//keyrecord.db";
                String backupDBPath = "record.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {}
    }

}