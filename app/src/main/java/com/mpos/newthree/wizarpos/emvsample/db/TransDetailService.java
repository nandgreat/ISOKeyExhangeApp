package com.mpos.newthree.wizarpos.emvsample.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TransDetailService {
	private DatabaseOpenHelper dbOpenHelper;
	private SQLiteDatabase db = null;
	private Cursor queryCursor = null;

	public TransDetailService(Context context) {
		dbOpenHelper = new DatabaseOpenHelper(context);
	}

	public void dropTable( )
	{
		db = dbOpenHelper.getWritableDatabase();
		db.execSQL(	"DROP TABLE IF EXISTS " + DatabaseOpenHelper.TABLE_TRANS_DETAIL );
		db.close();
	}

	public void createTable()
	{
		db = dbOpenHelper.getWritableDatabase();
		db.execSQL(DatabaseOpenHelper.CREATE_TRANS_DETAIL_SQL);
		db.close();
	}
	
	public void clearTable()
	{
		db = dbOpenHelper.getWritableDatabase();
		db.delete(DatabaseOpenHelper.TABLE_TRANS_DETAIL, "1", null);
		db.close();
	}
	
	public void save(TransDetailTable transDetail)
	{
		db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into " + DatabaseOpenHelper.TABLE_TRANS_DETAIL + 
				                                   "(trace,pan,entryMode,expiry,transType," +
				                                   "transDate,transTime,authCode,rrn,oper," +
				                                   "transAmount,balance,csn,unpredictableNumber,ac," +
                                                   "tvr,aid,tsi,appLabel,appName," +
                                                   "aip,iad,ecBalance,iccData,scriptResult) " +
	                                               "values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?)", 
	                                               new Object[]{transDetail.getTrace(),
				                                                transDetail.getPAN(),
				                                                transDetail.getCardEntryMode(),
				                                                transDetail.getExpiry(),
				                                                transDetail.getTransType(),
				                                                
				                                                transDetail.getTransDate(),
				                                                transDetail.getTransTime(),
				                                                transDetail.getAuthCode(),
																transDetail.getRRN(),
				                                                transDetail.getOper(),
				                                                
				                                                transDetail.getTransAmount(),
				                                                transDetail.getBalance(),
				                                                transDetail.getCSN(),
				                                                transDetail.getUnpredictableNumber(),
				                    		                    transDetail.getAC(),
				                    		                    
				                    		                    transDetail.getTVR(),
				                    		                    transDetail.getAID(),
				                    		                    transDetail.getTSI(),
				                    		                    transDetail.getAppLabel(),
				                    		                    transDetail.getAppName(),
				                    		                    
				                    		                    transDetail.getAIP(),
				                    		                    transDetail.getIAD(),
				                    		                    transDetail.getECBalance(),
				                    		                    transDetail.getICCData(),
				                    		                    transDetail.getScriptResult() });
		db.close();
	}
	
	public void update(TransDetailTable transDetail)
	{
		db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update " + DatabaseOpenHelper.TABLE_TRANS_DETAIL + 
				                                   " set trace=?,pan=?,entryMode=?,expiry=?,transType=?," +
				                                   "transDate=?,transTime=?,authCode=?,rrn=?,oper=?," +
			                                       "transAmount=?,balance=?,csn=?,unpredictableNumber=?,ac=?," +
                                                   "tvr=?,aid=?,tsi=?,appLabel=?,appName=?," +
                                                   "aip=?,iad=?,ecBalance=?,iccData=?,scriptResult=? where _id=?" +
	                                               new Object[]{transDetail.getTrace(),
                                                                transDetail.getPAN(),
                                                                transDetail.getCardEntryMode(),
                                                                transDetail.getExpiry(),
                                                                transDetail.getTransType(),
                                                                
                                                                transDetail.getTransDate(),
                                                                transDetail.getTransTime(),
                                                                transDetail.getAuthCode(),
				                                                transDetail.getRRN(),
                                                                transDetail.getOper(),

                                                                transDetail.getTransAmount(),
                                                                transDetail.getBalance(),
                                                                transDetail.getCSN(),
				                                                transDetail.getUnpredictableNumber(),
				                    		                    transDetail.getAC(),
				                    		                    
				                    		                    transDetail.getTVR(),
				                    		                    transDetail.getAID(),
				                    		                    transDetail.getTSI(),
				                    		                    transDetail.getAppLabel(),
				                    		                    transDetail.getAppName(),
				                    		                    
				                    		                    transDetail.getAIP(),
				                    		                    transDetail.getIAD(),
				                    		                    transDetail.getECBalance(),
				                    		                    transDetail.getICCData(),     
				                    		                    transDetail.getScriptResult(),
				                    		                    
                                                                transDetail.getId() });
		db.close();
	}
	
	public boolean findByTrace(Integer trace, TransDetailTable trans)
	{
		if(trace <= 0)
		{
			return findLast(trans);
		}
		db = dbOpenHelper.getReadableDatabase();
		queryCursor = db.rawQuery("select _id,trace,pan,entryMode,expiry,transType," + 
				                             "transDate,transTime,authCode,rrn,oper," +
				                             "transAmount,balance,csn,unpredictableNumber,ac," +
                                             "tvr,aid,tsi,appLabel,appName," +
                                             "aip,iad,ecBalance,iccData,scriptResult from " +
	                                 DatabaseOpenHelper.TABLE_TRANS_DETAIL + " where trace=?", new String[]{String.valueOf(trace)});
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	if(queryCursor != null)
        		queryCursor.close();
        	db.close();
        	return false;
        }
		if(queryCursor.moveToNext())
        {
        	getTransDetailFromCursor(trans, true);
        	return true;
        }
		queryCursor.close();
        db.close();
        return false;
	}
	
	public boolean findLast(TransDetailTable trans)
	{
		db = dbOpenHelper.getReadableDatabase();
		queryCursor = db.rawQuery("select _id,trace,pan,entryMode,expiry,transType," + 
				                             "transDate,transTime,authCode,rrn,oper," +
				                             "transAmount,balance,csn,unpredictableNumber,ac," +
                                             "tvr,aid,tsi,appLabel,appName," +
                                             "aip,iad,ecBalance,iccData,scriptResult from " +
	                                 DatabaseOpenHelper.TABLE_TRANS_DETAIL + " order by _id desc", null);     //  where rownum<=1
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	if(queryCursor != null)
        		queryCursor.close();
        	db.close();
        	return false;
        }
		if(queryCursor.moveToFirst())
        {
			getTransDetailFromCursor(trans, true);
			return true;
        }
		queryCursor.close();
        db.close();
        return false;
	}
	
	public long getTransCount()
	{
		db = dbOpenHelper.getReadableDatabase();
		queryCursor = db.rawQuery("select count(*) from " + DatabaseOpenHelper.TABLE_TRANS_DETAIL, null);
		queryCursor.moveToFirst();
		long count = queryCursor.getLong(0);
		queryCursor.close();
		db.close();
		return count;
	}
	
	private void getTransDetailFromCursor(TransDetailTable transDetail, boolean closeFlag )
	{
    	transDetail.setId(queryCursor.getInt(queryCursor.getColumnIndex("_id")));
    	transDetail.setTrace(queryCursor.getInt(1));
    	transDetail.setPAN(queryCursor.getString(2));
        transDetail.setCardEntryMode((byte)queryCursor.getShort(3));
        transDetail.setExpiry(queryCursor.getString(4));
        transDetail.setTransType((byte)queryCursor.getShort(5));
        
        transDetail.setTransDate(queryCursor.getString(6));
        transDetail.setTransTime(queryCursor.getString(7));
        transDetail.setAuthCode(queryCursor.getString(8));
        transDetail.setRRN(queryCursor.getString(9));
        transDetail.setOper(queryCursor.getString(10));
        
        transDetail.setTransAmount(queryCursor.getInt(11));
        transDetail.setBalance(queryCursor.getInt(12));
        transDetail.setCSN((byte)queryCursor.getShort(13));
        transDetail.setUnpredictableNumber(queryCursor.getString(14));
        transDetail.setAC(queryCursor.getString(15));
        
        transDetail.setTVR(queryCursor.getString(16));
        transDetail.setAID(queryCursor.getString(17));
        transDetail.setTSI(queryCursor.getString(18));
        transDetail.setAppLabel(queryCursor.getString(19));
        transDetail.setAppName(queryCursor.getString(20));
        
        transDetail.setAIP(queryCursor.getString(21));
        transDetail.setIAD(queryCursor.getString(22));
        transDetail.setECBalance(queryCursor.getInt(23));
        transDetail.setICCData(queryCursor.getString(24));
        transDetail.setScriptResult(queryCursor.getString(25));
        
        if(closeFlag)
        {
        	queryCursor.close();
        	db.close();
        }
	}

	public boolean startQuery(TransDetailTable trans)
	{
		db = dbOpenHelper.getReadableDatabase();
		queryCursor = db.rawQuery("select _id,trace,pan,entryMode,expiry,transType," + 
				                             "transDate,transTime,authCode,rrn,oper," +
				                             "transAmount,balance,csn,unpredictableNumber,ac," +
                                             "tvr,aid,tsi,appLabel,appName," +
                                             "aip,iad,ecBalance,iccData,scriptResult from " +
	                              DatabaseOpenHelper.TABLE_TRANS_DETAIL, null);
        return queryFirst(trans);
	}
	
	public Integer getCursorCount()
	{
		if(queryCursor == null)
			return 0;
		return queryCursor.getCount();
	}
	
	public boolean queryFirst(TransDetailTable trans)
	{
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	return false;
        }
		if(queryCursor.moveToFirst())
        {
			getTransDetailFromCursor(trans, false);
			return true;
        }
        return false;
	}
	
	public boolean queryNext(TransDetailTable trans)
	{
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	return false;
        }
		if(queryCursor.moveToNext())
        {
			getTransDetailFromCursor(trans, false);
			return true;
        }
        return false;
	}
	
	public boolean queryPrev(TransDetailTable trans)
	{
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	return false;
        }
		if(queryCursor.moveToPrevious())
        {
			getTransDetailFromCursor(trans, false);
			return true;
        }
        return false;
	}
	
	public void endQuery()
	{
		if(queryCursor != null)
		{
			queryCursor.close();
		}
		db.close();
	}
}
