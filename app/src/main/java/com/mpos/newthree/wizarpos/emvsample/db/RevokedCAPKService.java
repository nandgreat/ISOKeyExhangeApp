package com.mpos.newthree.wizarpos.emvsample.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RevokedCAPKService
{
	private SQLiteDatabase db = null;
	private Cursor queryCursor = null;

	public RevokedCAPKService(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public void save(RevokedCAPKTable revokedCAPK)
	{
		db.execSQL("insert into " + DatabaseOpenHelper.TABLE_REVOKED_CAPK + 
               "(rid,capki,certSerial) " + "values(?,?,?)", 
               new Object[]{revokedCAPK.getRID(),
				            revokedCAPK.getCapki(),
				            revokedCAPK.getCertSerial() });
	}
	
	public RevokedCAPKTable[] query()
	{
		queryCursor = db.rawQuery("select _id,rid,capki,certSerial from " + DatabaseOpenHelper.TABLE_REVOKED_CAPK, null);
		
		int i = 0;
		RevokedCAPKTable[] revokedCapks = new RevokedCAPKTable[queryCursor.getCount()];
		for(queryCursor.moveToFirst();!queryCursor.isAfterLast() && i < revokedCapks.length; queryCursor.moveToNext(),i++)
		{
			revokedCapks[i] = new RevokedCAPKTable();
			getRevokedCAPKFromCursor(revokedCapks[i], false);
		}
		if(queryCursor != null)
		{
			queryCursor.close();
		}
		return revokedCapks;
	}
	
	public void delete(String rid, String capki, String certSerial)
	{
		db.execSQL("delete from " + DatabaseOpenHelper.TABLE_REVOKED_CAPK + " where rid=? and capki=? and certSerial=?", new String[]{rid, capki,certSerial});
	}
	
	public long getRevokedCAPKCount()
	{
		queryCursor = db.rawQuery("select count(*) from " + DatabaseOpenHelper.TABLE_REVOKED_CAPK, null);
		queryCursor.moveToFirst();
		long count = queryCursor.getLong(0);
		queryCursor.close();
		return count;
	}
	
	private void getRevokedCAPKFromCursor(RevokedCAPKTable revokedCAPK, boolean closeFlag )
	{
		revokedCAPK.setId(queryCursor.getInt(0));
		revokedCAPK.setRID(queryCursor.getString(1));
		revokedCAPK.setCapki(queryCursor.getString(2));
		revokedCAPK.setCertSerial(queryCursor.getString(3));
        if(closeFlag)
        {
        	queryCursor.close();
        }
	}

	public boolean startQuery(RevokedCAPKTable revokedCAPK)
	{
		queryCursor = db.rawQuery("select _id,rid,capki,certSerial from " + DatabaseOpenHelper.TABLE_REVOKED_CAPK, null);
        return queryFirst(revokedCAPK);
	}
	
	public Integer getCursorCount()
	{
		if(queryCursor == null)
			return 0;
		return queryCursor.getCount();
	}
	
	public boolean queryFirst(RevokedCAPKTable revokedCAPK)
	{
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	return false;
        }
		if(queryCursor.moveToFirst())
        {
			getRevokedCAPKFromCursor(revokedCAPK, false);
			return true;
        }
        return false;
	}
	
	public boolean queryNext(RevokedCAPKTable revokedCAPK)
	{
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	return false;
        }
		if(queryCursor.moveToNext())
        {
			getRevokedCAPKFromCursor(revokedCAPK, false);
			return true;
        }
        return false;
	}
	
	public boolean queryPrev(RevokedCAPKTable revokedCAPK)
	{
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	return false;
        }
		if(queryCursor.moveToPrevious())
        {
			getRevokedCAPKFromCursor(revokedCAPK, false);
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
