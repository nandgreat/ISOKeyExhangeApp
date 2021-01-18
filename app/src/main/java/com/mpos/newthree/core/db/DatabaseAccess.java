package com.mpos.newthree.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;


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
        this.database = openHelper.getWritableDatabase();

    }

    public boolean updateKey(String key, String checkDigit, String keyName) {
        this.open();
        ContentValues values = new ContentValues();
        values.put("iso_kwk", key);
        values.put("iso_check_digit", checkDigit);
        values.put("iso_name", keyName);
        database.update("iso_keydata", values, DatabaseOpenHelper.KEY_ID, new String[]{String.valueOf(keyName)});
        return true;
    }

    public void updateKey(){
        this.open();

    }

    public String getKey(String keyName){
        String retval = null;
        try{
            String sql = "SELECT iso_kwk FROM iso_keydata WHERE iso_name = ?";
            SQLiteStatement statement =database.compileStatement(sql);
            statement.bindString(1, keyName);
            statement.execute();
        }catch (Exception e){e.printStackTrace();}
        return retval;
    }

    public void close(){
        if(database !=null){
            exportToSD();
            this.database.close();
        }
    }
    public void exportToSD() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.mpos.sdk//databases//contacts.db";
                String backupDBPath = "contacts_backup.db";
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