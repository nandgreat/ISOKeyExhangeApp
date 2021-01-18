package com.mpos.newthree.wizarpos.emvsample.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExceptionFileService {
	private SQLiteDatabase db = null;
	private Cursor queryCursor = null;

	public ExceptionFileService(SQLiteDatabase db)
	{
		this.db = db;
	}

	public void save(ExceptionFileTable exceptionFile)
	{
		db.execSQL("insert into " + DatabaseOpenHelper.TABLE_EXCEPTION_FILE + 
               "(pan,panSequence) " + "values(?,?)", 
               new Object[]{exceptionFile.getPAN(), exceptionFile.getPANSequence() });
	}
	
	public ExceptionFileTable[] query()
	{
		queryCursor = db.rawQuery("select _id,pan,panSequence from " + DatabaseOpenHelper.TABLE_EXCEPTION_FILE, null);
		
		int i = 0;
		ExceptionFileTable[] exceptionFiles = new ExceptionFileTable[queryCursor.getCount()];
		for(queryCursor.moveToFirst();!queryCursor.isAfterLast() && i < exceptionFiles.length; queryCursor.moveToNext(),i++)
		{
			exceptionFiles[i] = new ExceptionFileTable();
			getExceptionFileFromCursor(exceptionFiles[i], false);
		}
		if(queryCursor != null)
		{
			queryCursor.close();
		}
		return exceptionFiles;
	}
	
	public void delete(String pan, byte panSequence)
	{
		db.execSQL("delete from " + DatabaseOpenHelper.TABLE_EXCEPTION_FILE + " where pan=? and panSequence=?", new String[]{pan, Byte.toString(panSequence) });
	}
	
	public ExceptionFileTable find(String pan, int panSequence)
	{
		ExceptionFileTable exceptionFile = null;
		queryCursor = db.rawQuery("select _id,pan,panSequence from " + DatabaseOpenHelper.TABLE_EXCEPTION_FILE + " where pan=? and panSequence=?", 
				                  new String[]{String.valueOf(pan), String.valueOf(panSequence)});
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	if(queryCursor != null)
        		queryCursor.close();
        	db.close();
        	return null;
        }
		if(queryCursor.moveToNext())
        {
			exceptionFile = new ExceptionFileTable();
        	getExceptionFileFromCursor(exceptionFile, false);
        }
        queryCursor.close();
        return exceptionFile;
	}
	
	public long getExceptionFileCount()
	{
		queryCursor = db.rawQuery("select count(*) from " + DatabaseOpenHelper.TABLE_EXCEPTION_FILE, null);
		queryCursor.moveToFirst();
		long count = queryCursor.getLong(0);
		queryCursor.close();
		return count;
	}
	
	private void getExceptionFileFromCursor(ExceptionFileTable exceptionFile, boolean closeFlag )
	{
		exceptionFile.setId(queryCursor.getInt(0));
		exceptionFile.setPAN(queryCursor.getString(1));
		exceptionFile.setPANSequence((byte)queryCursor.getInt(2));
   	
        if(closeFlag)
        {
        	queryCursor.close();
        }
	}

	public boolean startQuery(ExceptionFileTable exceptionFile)
	{
		queryCursor = db.rawQuery("select _id,pan,panSequence from " + DatabaseOpenHelper.TABLE_EXCEPTION_FILE, null);
        return queryFirst(exceptionFile);
	}
	
	public Integer getCursorCount()
	{
		if(queryCursor == null)
			return 0;
		return queryCursor.getCount();
	}
	
	public boolean queryFirst(ExceptionFileTable exceptionFile)
	{
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	return false;
        }
		if(queryCursor.moveToFirst())
        {
			getExceptionFileFromCursor(exceptionFile, false);
			return true;
        }
        return false;
	}
	
	public boolean queryNext(ExceptionFileTable exceptionFile)
	{
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	return false;
        }
		if(queryCursor.moveToNext())
        {
			getExceptionFileFromCursor(exceptionFile, false);
			return true;
        }
        return false;
	}
	
	public boolean queryPrev(ExceptionFileTable exceptionFile)
	{
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	return false;
        }
		if(queryCursor.moveToPrevious())
        {
			getExceptionFileFromCursor(exceptionFile, false);
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
	}
}
