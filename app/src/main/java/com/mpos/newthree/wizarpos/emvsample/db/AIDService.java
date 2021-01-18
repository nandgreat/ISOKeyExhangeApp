package com.mpos.newthree.wizarpos.emvsample.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AIDService {
	private SQLiteDatabase db = null;
	private Cursor queryCursor = null;

	public AIDService(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public void save(AIDTable aid)
	{
		db.execSQL("insert into " + DatabaseOpenHelper.TABLE_AID + 
                   "(aid,appLabel,appPreferredName,appPriority,termFloorLimit," +
                   "TACDefault,TACDenial,TACOnline,targetPercentage,thresholdValue," +
                   "maxTargetPercentage,acquirerId,mcc,mid,appVersionNumber," +
                   "posEntryMode,transReferCurrencyCode,transReferCurrencyExponent,defaultDDOL,defaultTDOL," +
                   "supportOnlinePin,needCompleteMatching) " +
                   "values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?)", 
                   new Object[]{aid.getAid(),
                                aid.getAppLabel(),
                                aid.getAppPreferredName(),
                                aid.getAppPriority(),
                                aid.getTermFloorLimit(),
                                
                                aid.getTACDefault(),
                                aid.getTACDenial(),
                                aid.getTACOnline(),
                                aid.getTargetPercentage(),
                                aid.getThresholdValue(),
                                
                                aid.getMaxTargetPercentage(),
                                aid.getAcquirerId(),
                                aid.getMCC(),
                                aid.getMID(),
                                aid.getAppVersionNumber(),
                                
                                aid.getPOSEntryMode(),
                                aid.getTransReferCurrencyCode(),
                                aid.getTransReferCurrencyExponent(),
                                aid.getDefaultDDOL(),
                                aid.getDefaultTDOL(),
                                
                                aid.getSupportOnlinePin(),
                                aid.getNeedCompleteMatching() });
	}
	
	public void update(AIDTable aid)
	{
		db.execSQL("update " + DatabaseOpenHelper.TABLE_AID + 
                   " set appLabel=?,appPreferredName=?,appPriority=?,termFloorLimit=?," +
                   "TACDefault=?,TACDenial=?,TACOnline=?,targetPercentage=?,thresholdValue=?," +
                   "maxTargetPercentage=?,acquirerId=?,mcc=?,mid=?,appVersionNumber=?," +
                   "posEntryMode=?,transReferCurrencyCode=?,transReferCurrencyExponent=?,defaultDDOL=?,defaultTDOL=?," +
                   "supportOnlinePin=?,needCompleteMatching=? where aid=?", 
                   new Object[]{aid.getAppLabel(),
                                aid.getAppPreferredName(),
                                aid.getAppPriority(),
                                aid.getTermFloorLimit(),
                                
                                aid.getTACDefault(),
                                aid.getTACDenial(),
                                aid.getTACOnline(),
                                aid.getTargetPercentage(),
                                aid.getThresholdValue(),
                                
                                aid.getMaxTargetPercentage(),
                                aid.getAcquirerId(),
                                aid.getMCC(),
                                aid.getMID(),
                                aid.getAppVersionNumber(),
                                
                                aid.getPOSEntryMode(),
                                aid.getTransReferCurrencyCode(),
                                aid.getTransReferCurrencyExponent(),
                                aid.getDefaultDDOL(),
                                aid.getDefaultTDOL(),
                                
                                aid.getSupportOnlinePin(),
                                aid.getNeedCompleteMatching(),

								aid.getAid()                                });
	}
	
	public void delete(String aid)
	{
		db.execSQL("delete from " + DatabaseOpenHelper.TABLE_AID + " where aid=?", new String[]{aid });
	}
	
	public AIDTable find(String aidName)
	{
		AIDTable aid = null;
		queryCursor = db.rawQuery("select _id,aid,appLabel,appPreferredName,appPriority,termFloorLimit," +
		                     "TACDefault,TACDenial,TACOnline,targetPercentage,thresholdValue," +
		                     "maxTargetPercentage,acquirerId,mcc,mid,appVersionNumber," +
		                     "posEntryMode,transReferCurrencyCode,transReferCurrencyExponent,defaultDDOL,defaultTDOL," +
		                     "supportOnlinePin,needCompleteMatching from "
		                     + DatabaseOpenHelper.TABLE_AID + " where aid=?", new String[]{String.valueOf(aidName)});
        if(queryCursor == null || queryCursor.getCount() == 0)
        {
        	if(queryCursor != null)
        		queryCursor.close();
        	return null;
        }
		if(queryCursor.moveToNext())
        {
        	aid = new AIDTable();
			getAIDFromCursor(aid, false);
        }
		queryCursor.close();
        return aid;
	}
	
	public long getAIDCount()
	{
		queryCursor = db.rawQuery("select count(*) from " + DatabaseOpenHelper.TABLE_AID, null);
		queryCursor.moveToFirst();
		long count = queryCursor.getLong(0);
		queryCursor.close();
		return count;
	}
	
	private void getAIDFromCursor(AIDTable aid, boolean closeFlag )
	{
		aid.setId(queryCursor.getInt(0));
		aid.setAid(queryCursor.getString(1));
    	aid.setAppLabel(queryCursor.getString(2));
    	aid.setAppPreferredName(queryCursor.getString(3));
    	aid.setAppPriority((byte)queryCursor.getShort(4));
    	aid.setTermFloorLimit(queryCursor.getInt(5));
    	
    	aid.setTACDefault(queryCursor.getString(6));
    	aid.setTACDenial(queryCursor.getString(7));
    	aid.setTACOnline(queryCursor.getString(8));
    	aid.setTargetPercentage((byte)queryCursor.getShort(9));
    	aid.setThresholdValue(queryCursor.getInt(10));
    	
    	aid.setMaxTargetPercentage((byte)queryCursor.getShort(11));
    	aid.setAcquirerId(queryCursor.getString(12));
    	aid.setMCC(queryCursor.getString(13));
    	aid.setMID(queryCursor.getString(14));
    	aid.setAppVersionNumber(queryCursor.getString(15));
    	
    	aid.setPOSEntryMode((byte)queryCursor.getShort(16));
    	aid.setTransReferCurrencyCode(queryCursor.getString(17));
    	aid.setTransReferCurrencyExponent((byte)queryCursor.getShort(18));
    	aid.setDefaultDDOL(queryCursor.getString(19));
    	aid.setDefaultTDOL(queryCursor.getString(20));
    	
    	aid.setSupportOnlinePin((byte)queryCursor.getShort(21));
    	aid.setNeedCompleteMatching((byte)queryCursor.getShort(22));
    	
        if(closeFlag)
        {
        	queryCursor.close();
        }
	}

	public AIDTable[] query()
	{
		queryCursor = db.rawQuery("select _id,aid,appLabel,appPreferredName,appPriority,termFloorLimit," +
		                     "TACDefault,TACDenial,TACOnline,targetPercentage,thresholdValue," +
		                     "maxTargetPercentage,acquirerId,mcc,mid,appVersionNumber," +
		                     "posEntryMode,transReferCurrencyCode,transReferCurrencyExponent,defaultDDOL,defaultTDOL," +
		                     "supportOnlinePin,needCompleteMatching from " + DatabaseOpenHelper.TABLE_AID, null);
		
		int i = 0;
		AIDTable[] aids = new AIDTable[queryCursor.getCount()];
		for(queryCursor.moveToFirst();!queryCursor.isAfterLast() && i < aids.length; queryCursor.moveToNext(),i++)
		{
			aids[i] = new AIDTable();
			getAIDFromCursor(aids[i], false);
		}
		if(queryCursor != null)
		{
			queryCursor.close();
		}
		return aids;
	}

	public void createDefaultAID()
	{
		AIDTable aidTable = new AIDTable();
		//01 A0000000031010
		aidTable.setAid("A0000000031010");
		aidTable.setAppLabel("AID2");
		aidTable.setAppPreferredName("AEEFFF");
		aidTable.setAppPriority((byte)0);
		aidTable.setTermFloorLimit(10000);
		aidTable.setTACDefault("0000000000");
		aidTable.setTACDenial("0000000000");
		aidTable.setTACOnline("0000000000");
		aidTable.setThresholdValue(0);
		aidTable.setMaxTargetPercentage((byte)0);
		aidTable.setTargetPercentage((byte)0);
		aidTable.setAcquirerId("000000123456");
		aidTable.setPOSEntryMode((byte)0x80);
		aidTable.setMCC("3333");
		aidTable.setMID("12345678");
		aidTable.setAppVersionNumber("0096");
		aidTable.setTransReferCurrencyCode("0840");
		aidTable.setTransReferCurrencyExponent((byte)2);
		aidTable.setDefaultTDOL("9F0804");
		aidTable.setDefaultDDOL("9F37049F47018F019F3201");
		aidTable.setNeedCompleteMatching((byte)0);
		aidTable.setSupportOnlinePin((byte)1);
		save(aidTable);

		//02 A000000003101003
		aidTable.setAid("A000000003101003");
		save(aidTable);

		//03 A000000003101004
		aidTable.setAid("A000000003101004");
		save(aidTable);

		//04 A000000003101005
		aidTable.setAid("A000000003101005");
		save(aidTable);

		//05 A000000003101006
		aidTable.setAid("A000000003101006");
		save(aidTable);

		//06 A000000003101007
		aidTable.setAid("A000000003101007");
		save(aidTable);

		//07 A0000000031010010203040506070809
		aidTable.setAid("A0000000031010010203040506070809");
		save(aidTable);

		//08 A0000000041010
		aidTable.setAppVersionNumber("0002");
		aidTable.setAid("A0000000041010");
		save(aidTable);

		//09 A0000000651010
		aidTable.setAppVersionNumber("0200");
		aidTable.setAid("A0000000651010");
		save(aidTable);

		//10 A0000000999090
		aidTable.setAppVersionNumber("0009");
		aidTable.setAid("A0000000999090");
		save(aidTable);

		//11 A00000999901
		aidTable.setAppVersionNumber("9999");
		aidTable.setAid("A00000999901");
		save(aidTable);

		//12 A000000025010501
		aidTable.setAppVersionNumber("0001");
		aidTable.setAid("A000000025010501");
		save(aidTable);

		//13 A122334455
		aidTable.setAppVersionNumber("1234");
		aidTable.setAid("A122334455");
		save(aidTable);

		//14 A0000003330101
		aidTable.setAppVersionNumber("0030");
		aidTable.setAid("A0000003330101");
		aidTable.setTransReferCurrencyCode("0156");
		save(aidTable);

		//14 A000000333010102
		aidTable.setAppVersionNumber("0030");
		aidTable.setAid("A000000333010102");
		aidTable.setTransReferCurrencyCode("0156");
		save(aidTable);

		//15 A0000001523010
		aidTable.setAppVersionNumber("0001");
		aidTable.setAid("A0000001523010");
		save(aidTable);

		//16 A000000003101003
		aidTable.setAid("A0000003710001");
		save(aidTable);

		//17 A000000003101003
		aidTable.setAid("A0000004540010");
		save(aidTable);

		//18 A000000003101003
		aidTable.setAid("A0000004540011");
		save(aidTable);
	}
	
}
