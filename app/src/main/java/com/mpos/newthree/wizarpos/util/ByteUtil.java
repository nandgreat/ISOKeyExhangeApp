package com.mpos.newthree.wizarpos.util;

public class ByteUtil
{
	public ByteUtil(){
	}

	public static byte[] bcdToAscii(byte[] bcdByte)
	{
	  	byte[] returnByte = new byte[bcdByte.length * 2];
		byte value;
		for(int i = 0; i < bcdByte.length; i++)
		{
    		value = (byte)(bcdByte[i] >> 4 & 0xF);
    		if( value > 9){
    			returnByte[i*2] = (byte)(value + (byte)0x37);
    		}
    		else{
    			returnByte[i*2] = (byte)(value + (byte)0x30);
    		}
    		value = (byte)(bcdByte[i] & 0xF);
    		if( value > 9){
    			returnByte[i*2+1] = (byte)(value + (byte)0x37);
    		}
    		else{
    			returnByte[i*2+1] = (byte)(value + (byte)0x30);
    		}   	
		}
		return returnByte;
	}

	public static byte[] bcdToAscii(byte[] bcdByte, int offset, int length)
	{
	  	byte[] returnByte = new byte[length * 2];
		byte value;
		for(int i = offset; i < length; i++)
		{
    		value = (byte)(bcdByte[i] >> 4 & 0xF);
    		if( value > 9){
    			returnByte[i*2] = (byte)(value + (byte)0x37);
    		}
    		else{
    			returnByte[i*2] = (byte)(value + (byte)0x30);
    		}
    		value = (byte)(bcdByte[i] & 0xF);
    		if( value > 9){
    			returnByte[i*2+1] = (byte)(value + (byte)0x37);
    		}
    		else{
    			returnByte[i*2+1] = (byte)(value + (byte)0x30);
    		}   	
		}
		return returnByte;
	}
	
	public static void bcdToAscii(byte[] bcd_buf, int offset, byte[] ascii_buf, int asc_offset,	int conv_len, int type)
	{
		int cnt;
		int bcdOffset = offset;
		int asciiOffset = asc_offset;
		if (conv_len > (bcd_buf.length * 2)){
			conv_len = (bcd_buf.length * 2);
		}
		if (((conv_len & 0x01) > 0) && (type > 0)) {
			cnt = 1;
			conv_len++;
		}else{
			cnt = 0;
		}
		for (; cnt < conv_len; cnt++, asciiOffset++) 
		{
			ascii_buf[asciiOffset] = (byte) (((cnt & 0x01) > 0) ? (bcd_buf[bcdOffset++] & 0x0f)	: ((bcd_buf[bcdOffset] & 0xFF) >>> 4));
			ascii_buf[asciiOffset] += (byte) ((ascii_buf[asciiOffset] > 9) ? (65 - 10) : 48); // 65 = 'A' 48 = '0'
		}
	}
  
	public static int bcdToInt(byte[] bcdByte)
	{
		return NumberUtil.parseInt(bcdToAscii(bcdByte),0,10,false);
	}
}
