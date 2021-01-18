package com.mpos.newthree.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TECH-PC on 3/26/2018.
 */

public class DBManager {
    public Context context;
    public UserData ud;
    Pass encrypt;
    private String TERMINAL_ID = "terminal_id";

    public DBManager(Context ctx) {
        this.context = ctx;
        this.ud = new UserData(ctx);
        encrypt  = new Pass();
    }

    public String fetchJSONString() {
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            db = this.ud.getReadableDatabase();
            cursor = db.rawQuery("SELECT "+MetaData.JSON_FILE+" FROM " + MetaData.TABLE_JSON_DATA + " ORDER BY " + MetaData._ID + " DESC LIMIT 1", null);
            if (cursor.isBeforeFirst()) {
                cursor.moveToNext();
                return cursor.getString(cursor.getColumnIndex(MetaData.JSON_FILE));
            }else{
                return  null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
            db.close();
        }
        return null;
    }

    public boolean savedJSONString(String json) {
        boolean saved = false;
        SQLiteDatabase db = this.ud.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MetaData.JSON_FILE, json);
        if (db.insertOrThrow(MetaData.TABLE_JSON_DATA, null, values) != -1) {
            saved = true;
        }
        db.close();
        return saved;
    }

    public String fetchTerminalID() {
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            db = this.ud.getReadableDatabase();
            String[] settingsProjection = {
                    MetaData.PARAMETER_VALUE
            };

            String whereClause = MetaData.PARAMETER_KEY+"=?";
            String[] whereArgs = {TERMINAL_ID};

            cursor = db.query(
                    MetaData.TABLE_PARAMETER,
                    settingsProjection,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null
            );

            //cursor = db.rawQuery("SELECT "+MetaData.PARAMETER_VALUE+" FROM " + MetaData.TABLE_PARAMETER + " WHERE "+ MetaData.PARAMETER_KEY +" == " + SettingsActivity.TERMINAL_ID + " LIMIT 1", null);
            if (cursor.isBeforeFirst()) {
                cursor.moveToNext();
                return cursor.getString(cursor.getColumnIndex(MetaData.PARAMETER_VALUE));
            }else{
                return  null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
            db.close();
        }
        return null;
    }

    public boolean savedTerminalID(String terminalID) {
        boolean saved = false;
        SQLiteDatabase db = this.ud.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MetaData.PARAMETER_KEY, TERMINAL_ID);
        values.put(MetaData.PARAMETER_VALUE, terminalID);

        int id = getID(TERMINAL_ID);
        if(id==-1){
            if(db.insert(MetaData.TABLE_PARAMETER, null, values) > 0){
                saved = true;
            }
        }else {
            if(db.update(MetaData.TABLE_PARAMETER, values, MetaData.PARAMETER_KEY + "=?", new String[]{TERMINAL_ID}) > 0){
                saved = true;
            }
        }
        db.close();
        return saved;
    }

    private int getID(String key){
        SQLiteDatabase db = this.ud.getWritableDatabase();
        Cursor c = db.query(MetaData.TABLE_PARAMETER,new String[]{MetaData._ID}, MetaData.PARAMETER_KEY+" = ?",new String[]{key},null,null,null,null);
        if (c.moveToFirst()) //if the row exist then return the id
            return c.getInt(c.getColumnIndex(MetaData._ID));
        return -1;
    }

    /////not done yet
    public List<String> fetchLogin() {
        Cursor cursor = null;
        SQLiteDatabase db = null;
        List<String> user = new ArrayList<>();
        try {
            db = this.ud.getReadableDatabase();
            String[] settingsProjection = {
                    MetaData.USER,
                    MetaData.PASS
            };
            cursor = db.query(
                    MetaData.TABLE_LOGIN,
                    settingsProjection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor.isBeforeFirst()) {
                cursor.moveToNext();
                String pass = encrypt.decryptPassword(cursor.getString(cursor.getColumnIndex(MetaData.PASS)));
                user.add(cursor.getString(cursor.getColumnIndex(MetaData.USER)));
                user.add(pass);
                return user;
            }else{
                return  null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
            db.close();
        }
        return null;
    }

    public boolean isUserExist(String user, String pass) {
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            db = this.ud.getReadableDatabase();

            String[] settingsProjection = {
                    MetaData.USER
            };

            String whereClause = MetaData.USER + " = ? AND " + MetaData.PASS + " = ?";
            String[] whereArgs = {user, encrypt.encryptPassword(pass).toString()};

            cursor = db.query(
                    MetaData.TABLE_LOGIN,
                    settingsProjection,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null
            );
            if (cursor.isBeforeFirst()) {
                return true;
            }else{
                return  false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
            db.close();
        }
        return false;
    }

    public int updateLogin(String user, String pass, int isLogin){
        SQLiteDatabase db = this.ud.getWritableDatabase();
        String encryptPassword = encrypt.encryptPassword(pass);
        ContentValues values = new ContentValues();
        values.put(MetaData.USER, user);
        values.put(MetaData.PASS, encryptPassword);
        values.put(MetaData.IS_LOGIN, isLogin);
        // updating row
        return db.update(MetaData.TABLE_LOGIN, values, MetaData.USER + " = ? AND " + MetaData.PASS + " = ?",
                new String[] { user, encryptPassword.toString() });
    }

    public boolean savedLogin(String user, String pass) {
        String encryptPassword = encrypt.encryptPassword(pass);
        boolean saved = false;
        SQLiteDatabase db = this.ud.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MetaData.USER, user);
        values.put(MetaData.PASS, encryptPassword);
        values.put(MetaData.IS_LOGIN, 1);
        int u = db.update(MetaData.TABLE_LOGIN, values, MetaData.USER+"=?", new String[]{user});
        if (u == 0) {
            long index = db.insertWithOnConflict(MetaData.TABLE_LOGIN, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            if(index > 0){
                return true;
            }
        }else{
            return true;
        }
        db.close();
        return saved;
    }
}
