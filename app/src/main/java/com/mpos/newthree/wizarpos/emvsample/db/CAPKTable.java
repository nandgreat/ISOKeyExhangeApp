package com.mpos.newthree.wizarpos.emvsample.db;

import com.mpos.newthree.wizarpos.util.StringUtil;

public class CAPKTable
{
	private int _id;
	private String  rid;		    // Registered Application Provider Identifier
	private String  capki;		    // Certificate Authority Public Key Index
	private byte    hashIndex;		// Hash Algorithm Indicator
	private byte    arithIndex;		// Certificate Authority Public Key Algorithm Indicator
	private String  modul;	        // Certificate Authority Public Key Modulus
	private String  exponent;	    // Certificate Authority Public Key Exponent
	private String  checkSum;       // Certificate Authority Public Key Check Sum
	private String  expiry;	        // Certificate Expiration Date

	public CAPKTable()
	{
		init();
	}
	
	public void init()
	{
		_id = -1;
		rid = "";
		capki = "";
		hashIndex = 0;
		arithIndex = 0;
		modul = "";
		exponent = "";
		checkSum = "";
		expiry = "";
	}
	
	// _id
	public int getId()
	{
		return _id;
	}
	
	public void setId(int id)
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
	
	// hashIndex
	public byte getHashIndex()
	{
		return hashIndex;
	}
	
	public void setHashIndex(byte hashIndex)
	{
		this.hashIndex = hashIndex;
	}
	
	// arithIndex
	public byte getArithIndex()
	{
		return arithIndex;
	}
	
	public void setArithIndex(byte arithIndex)
	{
		this.arithIndex = arithIndex;
	}
	
	// modul
	public String getModul()
	{
		return modul;
	}
	
	public void setModul(String modul)
	{
		this.modul = modul;
	}

	public int getModulLength()
	{
		return modul.length();
	}

	
	// exponent
	public String getExponent()
	{
		return exponent;
	}

	public void setExponent(String exponent)
	{
		this.exponent = exponent;
	}
	
	public int getExponentLength()
	{
		return exponent.length();
	}

	// checksum
	public String getChecksum()
	{
		return checkSum;
	}
	public void setChecksum(String checksum)
	{
		this.checkSum = checksum;
	}
	
	// expiry
	public String getExpiry()
	{
		return expiry;
	}
	
	public void setExpiry(String expiry)
	{
		this.expiry = expiry;
	}
	
	public byte[] getDataBuffer()
	{
		byte[] data = new byte[512];
		int offset = 0;
		data[offset]   = (byte)0x9F;
		data[offset+1] = 0x06;
		data[offset+2] = (byte)(rid.length()/2);
		System.arraycopy(StringUtil.hexString2bytes(rid), 0, data, offset+3, rid.length()/2);
		offset += (3+rid.length()/2);
		
		data[offset]   = (byte)0x9F;
		data[offset+1] = 0x22;
		data[offset+2] = 0x01;
		data[offset+3] = StringUtil.hexString2bytes(capki)[0];
		offset += 4;
		
		data[offset]   = (byte)0xDF;
		data[offset+1] = 0x05;
		data[offset+2] = (byte)expiry.length();
		System.arraycopy(expiry.getBytes(), 0, data, offset+3, expiry.length());
		offset += (3+expiry.length());
		
		data[offset]   = (byte)0xDF;
		data[offset+1] = 0x06;
		data[offset+2] = 0x01;
		data[offset+3] = hashIndex;
		offset += 4;
		
		data[offset]   = (byte)0xDF;
		data[offset+1] = 0x07;
		data[offset+2] = 0x01;
		data[offset+3] = arithIndex;
		offset += 4;
		
		data[offset]   = (byte)0xDF;
		data[offset+1] = 0x02;
		data[offset+2] = (byte)0x81;
		data[offset+3] = (byte)(modul.length()/2 & 0xFF);
		System.arraycopy(StringUtil.hexString2bytes(modul), 0, data, offset+4, modul.length()/2);
		offset += (4+modul.length()/2);
		
		data[offset]   = (byte)0xDF;
		data[offset+1] = 0x04;
		data[offset+2] = (byte)(exponent.length()/2);
		System.arraycopy(StringUtil.hexString2bytes(exponent), 0, data, offset+3, exponent.length()/2);
		offset += (3+exponent.length()/2);
		
		if(checkSum != null && checkSum.length() > 0)
		{
			data[offset]   = (byte)0xDF;
			data[offset+1] = 0x03;
			data[offset+2] = (byte)(checkSum.length()/2);
			System.arraycopy(StringUtil.hexString2bytes(checkSum), 0, data, offset+3, checkSum.length()/2);
			offset += (3+checkSum.length()/2);
		}
		
		byte[] dataOut = new byte[offset];
		System.arraycopy(data, 0, dataOut, 0, dataOut.length);
		
		return dataOut;
	}
}
