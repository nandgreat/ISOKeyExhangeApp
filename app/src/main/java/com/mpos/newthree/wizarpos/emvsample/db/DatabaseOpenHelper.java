package com.mpos.newthree.wizarpos.emvsample.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper
{
	public final static String TABLE_CAPK             = "capk";
	public final static String TABLE_AID              = "aid";
	public final static String TABLE_REVOKED_CAPK     = "revoked_capk";
	public final static String TABLE_EXCEPTION_FILE   = "exception_file";
	public final static String TABLE_TRANS_DETAIL     = "trans_detail";
	public final static String TABLE_TRANS_ADVICE     = "trans_advice";
	
	private final static String DATABASE_NAME    = "emvsample.db";
    private final static int    DATABASE_VERSION = 1;
    		
    public final static String CREATE_CAPK_SQL = 
    		"Create table if not exists " + TABLE_CAPK + " ( _id"              + " integer primary key autoincrement," +
                                                           " rid"              + " char(10)," +
	                                                       " capki"            + " char(1)," +
	                                                       " hashIndex"		   + " char(1)," +
	                                                       " arithIndex"	   + " char(1)," +
	                                                       " modul"            + " varchar(496)," +
	                                                       " exponent"         + " varchar(6)," +
	                                                       " checkSum"	       + " char(40)," +
	                                                       " expiry"		   + " char(8));";	

    public final static String CREATE_AID_SQL = 
    		"Create table if not exists " + TABLE_AID  + " ( _id"                        + " integer primary key autoincrement," +
    				"                                        aid"                        + " varchar(16)," +
    		                                               " appLabel"                   + " varchar(16)," +
    		                                               " appPreferredName"           + " varchar(16)," +
    		                                               " appPriority"                + " char(1)," +
    		                                               " termFloorLimit"             + " integer," +
    		                                               " TACDefault"                 + " char(10)," +
    		                                               " TACDenial"                  + " char(10)," +
    		                                               " TACOnline"                  + " char(10)," +
    		                                               " targetPercentage"           + " integer," +
    		                                               " thresholdValue"             + " integer," +
    		                                               " maxTargetPercentage"        + " integer," +
    		                                               " acquirerId"                 + " varchar(6)," +
    		                                               " mcc"                        + " char(4)," +
    		                                               " mid"                        + " varchar(15)," +
    		                                               " appVersionNumber"           + " char(4)," +
    		                                               " posEntryMode"               + " char(1)," +
    		                                               " transReferCurrencyCode"     + " char(3)," +
    		                                               " transReferCurrencyExponent" + " char(1)," +
    		                                               " defaultDDOL"                + " varchar(256)," +
    		                                               " defaultTDOL"                + " varchar(256)," +
    		                                               " supportOnlinePin"           + " char(1)," +
    		                                               " needCompleteMatching"       + " char(1)," +
    		                                               " contactlessLimit"           + " integer," +
    		                                               " contactlessFloorLimit"      + " integer," +
    		                                               " cvmLimit"                   + " integer," +
    		                                               " ECTermTransLimit"           + " integer );";	

    public final static String CREATE_REVOKED_CAPK_SQL = 
    		"Create table if not exists " + TABLE_REVOKED_CAPK + " ( _id"              + " integer primary key autoincrement," +
                                                                   " rid"              + " char(10)," +
                                                                   " capki"            + " char(1),"  +
                                                                   " certSerial"       + " char(10) );";	
    
    public final static String CREATE_EXCEPTION_FILE_SQL = 
    		"Create table if not exists " + TABLE_EXCEPTION_FILE + " ( _id"            + " integer primary key autoincrement," +
                                                                   " pan"              + " varchar(19)," +
	                                                               " panSequence"      + " char(1));";	
    
    public final static String CREATE_TRANS_DETAIL_SQL = 
    		"Create table if not exists " + TABLE_TRANS_DETAIL + " ( _id"              + " integer primary key autoincrement," +
    				                                               " trace"            + " integer," +
    				                                               " pan"              + " varchar(19)," +
    				                                               " entryMode"        + " char(1)," +
    				                                               " expiry"           + " char(4)," +
    				                                               " transType"        + " char(1)," +

    				                                               " transDate"        + " char(8)," +
    				                                               " transTime"        + " char(6)," +
    				                                               " authCode"         + " char(6)," +
    				                                               " rrn"              + " char(12)," +
    				                                               " oper"             + " char(2)," +
    				                                               
    				                                               " transAmount"      + " integer," +
    				                                               " balance"          + " integer," +
    				                                               " csn"              + " char(1)," +
																   " unpredictableNumber" + " char(8)," +
																   " ac"               + " char(16)," +
																   
																   " tvr"              + " char(10)," +
																   " aid"              + " varchar(16)," +
																   " tsi"              + " char(4)," +
																   " appLabel"         + " varchar(16)," +
																   " appName"          + " varchar(16)," +
																   
																   " aip"              + " char(4)," +
																   " iad"              + " char(64)," +
																   " ecBalance"        + " integer," +
																   " iccData"          + " varchar(255)," +
                                                                   " scriptResult"     + " varchar(255));";
    
    
    public final static String CREATE_TRANS_ADVICE_SQL = 
    		"Create table if not exists " + TABLE_TRANS_ADVICE + " ( _id"              + " integer primary key autoincrement," +
    				                                               " trace"            + " integer," +
    				                                               " pan"              + " varchar(19)," +
    				                                               " entryMode"        + " char(1)," +
    				                                               " expiry"           + " char(4)," +
    				                                               " transType"        + " char(1)," +

    				                                               " transDate"        + " char(8)," +
    				                                               " transTime"        + " char(6)," +
    				                                               " authCode"         + " char(6)," +
    				                                               " rrn"              + " char(12)," +
    				                                               " oper"             + " char(2)," +
    				                                               
    				                                               " transAmount"      + " integer," +
    				                                               " balance"          + " integer," +
    				                                               " csn"              + " char(1)," +
																   " unpredictableNumber" + " char(8)," +
																   " ac"               + " char(16)," +
																   
																   " tvr"              + " char(10)," +
																   " aid"              + " varchar(16)," +
																   " tsi"              + " char(4)," +
																   " appLabel"         + " varchar(16)," +
																   " appName"          + " varchar(16)," +
																   
																   " aip"              + " char(4)," +
																   " iad"              + " char(64)," +
																   " ecBalance"        + " integer," +
																   " iccData"          + " varchar(255)," +
                                                                   " scriptResult"     + " varchar(255));";
    
    public DatabaseOpenHelper(Context context)
    {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }
    
    public void dropTable(SQLiteDatabase db, String tableName)
	{
		db.execSQL(	"DROP TABLE IF EXISTS " + tableName );
	}

	public void createTable(SQLiteDatabase db, String tableName)
	{
		if(tableName.equals(TABLE_AID))
		{
			db.execSQL(DatabaseOpenHelper.CREATE_AID_SQL);
		}
		else if(tableName.equals(TABLE_CAPK))
		{
			db.execSQL(DatabaseOpenHelper.CREATE_CAPK_SQL);
		}
		else if(tableName.equals(TABLE_REVOKED_CAPK))
		{
			db.execSQL(DatabaseOpenHelper.CREATE_REVOKED_CAPK_SQL);
		}
		else if(tableName.equals(TABLE_EXCEPTION_FILE))
		{
			db.execSQL(DatabaseOpenHelper.CREATE_EXCEPTION_FILE_SQL);
		}
		else if(tableName.equals(TABLE_TRANS_DETAIL))
		{
			db.execSQL(DatabaseOpenHelper.CREATE_TRANS_DETAIL_SQL);
		}
		else if(tableName.equals(TABLE_TRANS_ADVICE))
		{
			db.execSQL(DatabaseOpenHelper.CREATE_TRANS_ADVICE_SQL);
		}
	}
	
	public void clearTable(SQLiteDatabase db, String tableName)
	{
		db.delete(tableName, "1", null);
	}
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
    	db.execSQL(CREATE_TRANS_DETAIL_SQL);
    	db.execSQL(CREATE_AID_SQL);
    	db.execSQL(CREATE_CAPK_SQL);
    	db.execSQL(CREATE_EXCEPTION_FILE_SQL);
    	db.execSQL(CREATE_REVOKED_CAPK_SQL);
    	db.execSQL(CREATE_TRANS_ADVICE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    	db.execSQL("drop table if exists " + TABLE_TRANS_DETAIL);
    	db.execSQL("drop table if exists " + TABLE_AID);
    	db.execSQL("drop table if exists " + TABLE_CAPK);
    	db.execSQL("drop table if exists " + TABLE_REVOKED_CAPK);
    	db.execSQL("drop table if exists " + TABLE_EXCEPTION_FILE);
    	db.execSQL("drop table if exists " + TABLE_TRANS_ADVICE);
    	onCreate(db);
    }
}
