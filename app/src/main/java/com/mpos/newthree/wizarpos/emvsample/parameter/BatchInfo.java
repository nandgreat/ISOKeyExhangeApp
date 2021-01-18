package com.mpos.newthree.wizarpos.emvsample.parameter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class BatchInfo
{
	private final String settlePendingTag     = "settlePending";
	private final String batchNumberTag       = "batchNumber"; 
	private final String saleCountTag         = "saleCount";
	private final String saleAmountTag        = "saleAmount";
	
	private Integer batchNumber = 1;
	private Integer saleCount   = 0;    // 脱机消费 + 折扣消费
	private Integer saleAmount  = 0;
	private Integer settlePending = 0;
	
	private SharedPreferences batchPref;
	private Editor editor;
	
	public BatchInfo( SharedPreferences pref)
	{
		batchPref = pref;
    	editor = batchPref.edit();
	}
	
    public void initBatch(int batchNumber)
    {
    	this.batchNumber = batchNumber;
    	editor.putInt(batchNumberTag, batchNumber);
    	
    	saleCount = 0;
    	editor.putInt(saleCountTag, 0);
    	
    	saleAmount = 0;
    	editor.putInt(saleAmountTag, 0);
    	
    	settlePending = 0;
    	editor.putInt(settlePendingTag, 0);

    	editor.commit();
    }
    
    public void loadBatch()
    {
    	batchNumber   = batchPref.getInt(batchNumberTag, 1);
    	saleCount     = batchPref.getInt(saleCountTag, 0);
    	saleAmount    = batchPref.getInt(saleAmountTag, 0);
    	settlePending = batchPref.getInt(settlePendingTag, 0);
    }
    
	// batchNumber
	public int getBatchNumber()
	{
		return batchNumber;
	}
	
    public void incBatchNumber()
    {
    	this.batchNumber += 1; 
    	editor.putInt(batchNumberTag, batchNumber);
    	editor.commit();
    }
    
	public void setBatchNumber(Integer batchNumber)
	{
    	this.batchNumber = batchNumber;
    	editor.putInt(batchNumberTag, batchNumber);
    	editor.commit();
	}
	
	// sale
	public int getSaleAmount()
	{
		return saleAmount;
	}
	
	public int getSaleCount()
	{
		return saleCount;
	}
	
    public void incSale(Integer amount)
    {
    	saleCount += 1;
    	saleAmount += amount;
    	
    	editor.putInt(saleCountTag,  saleCount);
    	editor.putInt(saleAmountTag, saleAmount);
    	editor.commit();
    }

	public void incSale(float amount)
	{
		saleCount += 1;
		saleAmount = saleAmount + (int)amount;

		editor.putInt(saleCountTag,  saleCount);
		editor.putInt(saleAmountTag, saleAmount);
		editor.commit();
	}
    
    // settlePending
    public int getSettlePending()
    {
    	return settlePending;
    }
    
    public void setSettlePending(int settlePending)
    {
    	this.settlePending = settlePending;
    	editor.putInt(settlePendingTag, settlePending);
    	editor.commit();
    }
}
