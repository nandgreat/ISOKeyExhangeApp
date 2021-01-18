package com.mpos.newthree.obj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 9/11/2017.
 */

public class TransactionUpdateDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UpdTrans.db";
    private static final int DATABASE_VERSION = 1;
    public static final String ID = "_id";
    public static final String TABLE_NAME = "trans_update";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String RESPONSE_CODE = "response_code";
    public static final String TRANS_PIN = "pin";
    public static final String UPLOADED = "uploaded";
    public static final String STAN = "stan";

    public TransactionUpdateDBHelper(Context context) {
        super(context,DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                USERNAME + " TEXT, " +
                PASSWORD + " TEXT, " +
                STAN + " TEXT, " +
                RESPONSE_CODE + " TEXT, " +
                TRANS_PIN + " TEXT, " +
                UPLOADED + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertTransaction(Transaction trans, String uploaded) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, trans.getUsername());
        contentValues.put(PASSWORD, trans.getPassword());
        contentValues.put(RESPONSE_CODE, trans.getResponse_code());
        contentValues.put(TRANS_PIN, trans.getPin());
        contentValues.put(STAN, trans.getStan());
        contentValues.put(UPLOADED, uploaded);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateTransaction(Transaction trans, String uploaded) {
        SQLiteDatabase db = getWritableDatabase();
        String qq="UPDATE "+TABLE_NAME+" SET "+UPLOADED+" ="+uploaded+" WHERE "+STAN+" = "+"'"+trans.getStan()+"'";
        db.execSQL(qq);
        Log.w("trans.getDbId()",trans.getStan());
        return true;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction = new Transaction();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NAME+ " WHERE " +UPLOADED +" = ?" , new String[]{"0"} );
        if(res.moveToFirst()){
            do{
                //transaction.setDbId(res.getString(res.getColumnIndex(ID)));
                transaction.setUsername(res.getString(res.getColumnIndex(USERNAME)));
                transaction.setPassword(res.getString(res.getColumnIndex(PASSWORD)));
                transaction.setResponse_code(res.getString(res.getColumnIndex(RESPONSE_CODE)));
                transaction.setPin(res.getString(res.getColumnIndex(TRANS_PIN)));
                transaction.setStan(res.getString(res.getColumnIndex(STAN)));
                transactionList.add(transaction);
            }while(res.moveToNext());
        }
        res.close();
        return transactionList;
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

}
