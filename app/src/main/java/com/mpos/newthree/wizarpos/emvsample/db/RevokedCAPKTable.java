package com.mpos.newthree.wizarpos.emvsample.db;

import com.mpos.newthree.wizarpos.util.StringUtil;

public class RevokedCAPKTable
{
	private Integer _id;
	private String rid;
	private String capki;
	private String certSerial;

	public RevokedCAPKTable()
	{
		init();
	}
	
	public void init()
	{
		_id = -1;
		rid = "";
		capki = "";
		certSerial = "";
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
	
	// rid
	public String getRID()
	{
		return rid;
	}
	
	public void setRID(String rid)
	{
		this.rid = rid;
	}
	
	// capki
	public String getCapki()
	{
		return capki;
	}
	
	public void setCapki(String capki)
	{
		this.capki = capki;
	}
	
	// certSerial
	public String getCertSerial()
	{
		return certSerial;
	}
	
	public void setCertSerial(String certSerial)
	{
		this.certSerial = certSerial;
	}
	
	public byte[] getDataBuffer()
	{
		if(rid != null && rid.length() == 10)
		{
			byte[] dataOut = new byte[9];
			System.arraycopy(StringUtil.hexString2bytes(rid), 0, dataOut, 0, 5);
			dataOut[5] = StringUtil.hexString2bytes(capki)[0];
			System.arraycopy(StringUtil.hexString2bytes(certSerial), 0, dataOut, 6, 3);
			return dataOut;
		}
		else{
			return null;
		}
	}
}
