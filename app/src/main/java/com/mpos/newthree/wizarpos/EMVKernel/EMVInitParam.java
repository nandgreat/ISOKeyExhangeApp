package com.mpos.newthree.wizarpos.EMVKernel;

public class EMVInitParam
{
	/**
	 * PBOC处理：1 ; QPBOC处理：2 ; 电子现金处理：3
	 */
	public int TransType = 0;
	
	/**
	 * 读卡器的句柄
	 */
	public int ReaderHandle = 0;
	
	/**
	 * CARD_CONTACT:1 ; CARD_CONTACTLESS:2
	 */
	public byte CardType;
	
	public short ATRLength;
	
	public byte[] ATR = new byte[30];
}
