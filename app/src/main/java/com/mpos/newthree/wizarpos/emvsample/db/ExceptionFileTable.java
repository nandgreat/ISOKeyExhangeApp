package com.mpos.newthree.wizarpos.emvsample.db;

import com.mpos.newthree.wizarpos.util.StringUtil;

public class ExceptionFileTable
{
	private Integer _id;
	private String pan;
	private byte panSequence; //PAN Sequence Number
	
	public ExceptionFileTable()
	{
		init();
	}
	
	private void init()
	{
		_id = -1;
		pan = "";
		panSequence = 0;
	}
	
	// _id
	public Integer getId()
	{
		return _id;
	}
	
	public void setId(Integer id)
	{
		this._id = id;
	}
	
	// pan
	public String getPAN()
	{
		return pan;
	}
	
	public void setPAN(String pan)
	{
		this.pan = pan;
	}
	
	// panSequence
	public byte getPANSequence()
	{
		return panSequence;
	}
	
	public void setPANSequence(byte panSequence)
	{
		this.panSequence = panSequence;
	}
	
	public byte[] getDataBuffer()
	{
		byte[] dataOut = new byte[20];
		System.arraycopy(StringUtil.fillString(pan, 19, 'F', false).getBytes(), 0, dataOut, 0, 19);
		dataOut[19] = panSequence;
		return dataOut;
	}
}
