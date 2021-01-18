/*
 * Copyright (c) 2003 GlobalPlatform and STIP Consortium.
 * All Rights Reserved.
 *
 * This software may only be used subject to the terms of a valid license
 * agreement from one of the co-owners of the copyright to this software.
 */
package com.mpos.newthree.wizarpos.emvsample.card;

import android.util.Log;

import com.mpos.newthree.wizarpos.emvsample.constant.Constant;
import com.mpos.newthree.wizarpos.jni.ContactICCardReaderInterface;
import com.mpos.newthree.wizarpos.jni.ContactICCardSlotInfo;
import com.mpos.newthree.wizarpos.jni.ContactlessICCardReaderInterface;

/*
 * This smartcard control
 * 
 * @version 1.0 Dec 15, 2006
 */
public class SmartCardControl implements Constant
{
	// slot State
	public static final byte SLOT_CLOSE     = 0;
	public static final byte SLOT_OPEN      = 1;
	
	// card State
	public static final byte CARD_ABSENT    = 0;
	public static final byte CARD_POWER_ON  = 1;    // the card have powered on
	public static final byte CARD_POWER_OFF = 2;   // the card have powered off
	
	public static final byte CARD_CONTACT     = 1;
	public static final byte CARD_CONTACTLESS = 2;
	
	private int  readerSlot = -1;
	private int  readerHandle = -1;
	private byte cardType = -1;
	private byte cardState = CARD_ABSENT;
	private byte slotState = SLOT_CLOSE;
	
	private byte[] ATR = new byte[64];
	private short atrLength = 0;
	private byte[] respApduBuf = new byte[512];
	private int  respApduLen = 0;
	
	private ContactICCardSlotInfo mSlotInfo = null;

	public SmartCardControl(byte cardType)
	{
    	this.cardType = cardType;
		mSlotInfo = new ContactICCardSlotInfo();
	}
	
	public SmartCardControl(byte cardType, int contactSlot)
	{
		this.cardType = cardType;
		readerSlot = contactSlot;
    	mSlotInfo = new ContactICCardSlotInfo();
	}
	
	// readerSlot
	public int getReaderSlot()
	{
		return readerSlot;
	}
	
	public void setReaderSlot(byte slot)
	{
		this.readerSlot = slot;
	}
	
	// readerHandle
	public int getReaderHandle()
	{
		return readerHandle;
	}
	
	public void setReaerHandle(int readerHandle)
	{
		this.readerHandle = readerHandle;
	}
	
	// cardType
	public byte getCardType()
	{
		return cardType;
	}
	
	public void setCardType(byte cardType)
	{
		this.cardType = cardType;
	}
	
	// cardState
	public byte getCardState()
	{
		return cardState;
	}
	
	public void setCardState(byte cardState)
	{
		this.cardState = cardState;
	}

	// slotState
	public byte getSlotState()
	{
		return slotState;
	}
	
	public void setSlotState(byte slotState)
	{
		this.slotState = slotState;
	}
	
	// ATR
	public byte[] getATR()
	{
		if(atrLength <= 0) return null;
		byte[] dataOut = new byte[atrLength];
		System.arraycopy(ATR, 0, dataOut, 0, atrLength);
		return dataOut;
	}
	
	public short getATRLength()
	{
		return atrLength;
	}
	
	// respApduBuff
	public byte[] getRespData()
	{
		int length = respApduLen - 2;
		if(length <= 0)return null;
		
		byte[] dataOut = new byte[length];
		System.arraycopy(respApduBuf, 0, dataOut, 0, length);
		return dataOut;
	}
	
	// 
	public int getRespDataLength()
	{
		return (respApduLen - 2);
	}
	
	public boolean open()
	{
		if(debug)Log.d(APP_TAG, "Smartcard open");
		if(cardType == CARD_CONTACT)
		{
			if(readerSlot >= 0)
			{
				readerHandle = ContactICCardReaderInterface.open(readerSlot);	
				if(readerHandle >= 0)
				{
					slotState = SLOT_OPEN;
					if(debug)Log.d(APP_TAG, "ContactICCardReaderInterface.open(" + readerSlot + ") OK");
					return true;
				}
			}
		}
		else if(cardType == CARD_CONTACTLESS)
		{ 
			int nResult = ContactlessICCardReaderInterface.open();
			if(debug)Log.d(APP_TAG, "ContactlessICCardReaderInterface.open() result = " + nResult);
			if(nResult >= 0)
			{
				slotState = SLOT_OPEN;
				return true;
			}
		}
		return false;
	}
	
	public boolean searchTargetBegin(int timeout)
	{
		int nResult = ContactlessICCardReaderInterface.searchTargetBegin(0, 0, timeout);
		if(nResult >= 0)
		{
			return true;
		}
		return false;
	}
	
	public void searchTargetEnd()
	{
		if(cardType == CARD_CONTACTLESS)
		{
			ContactlessICCardReaderInterface.searchTargetEnd();
		}
	}
	
	public boolean powerOn()
	{
		if(cardType == CARD_CONTACT)
		{
			if(slotState == SLOT_OPEN)
			{
				int nResult = ContactICCardReaderInterface.powerOn(readerHandle, ATR, mSlotInfo);
				if(nResult >= 0)
				{
					atrLength = (short)nResult;
					if(debug)
					{
						Log.d(APP_TAG, "ContactICCardReaderInterface.powerOn(" + readerSlot + ") OK");
	        			String strDebug = new String();
	        			for(int i = 0; i <nResult; i++)
	        				strDebug += String.format("%02X ", ATR[i]);
	        			Log.i(APP_TAG, "ATR: " + strDebug);
					}
					cardState = CARD_POWER_ON;
					return true;
				}
			}
		}
		else if(cardType == CARD_CONTACTLESS)
		{
			if(slotState == SLOT_OPEN)
			{
				int nResult = ContactlessICCardReaderInterface.attachTarget(ATR);
				if(nResult > 0)
				{
					atrLength = (short)nResult;
					cardState = CARD_POWER_ON;
					return true;
				}
			}
		}
		return false;
	}
	
	public void powerOff()
	{
		if(cardType == CARD_CONTACT)
		{
			if(cardState == CARD_POWER_ON)
			{
				cardState = CARD_POWER_OFF;
			}
			ContactICCardReaderInterface.powerOff(readerHandle);
		}
		else if(cardType == CARD_CONTACTLESS)
		{
			if(cardState == CARD_POWER_ON)
			{
				cardState = CARD_POWER_OFF;
			}
			ContactlessICCardReaderInterface.detachTarget();
		}
	}
	
	public void cardRemoved()
	{
		if(debug)Log.d(APP_TAG, "cardRemoved");
		cardState = CARD_ABSENT;
		powerOff();
	}
	
	public void close()
	{
		if(debug)Log.d(APP_TAG, "SmartCard Close");
		if(cardState == CARD_POWER_ON)
		{
			powerOff();
		}
		cardState = CARD_ABSENT;
		if(slotState != SLOT_CLOSE)
		{
			slotState = SLOT_CLOSE;
			if(cardType == CARD_CONTACT)
			{
				ContactICCardReaderInterface.close(readerHandle);
				if(debug)Log.d(APP_TAG, "ContactICCardReaderInterface.close(" + readerSlot + ")");
				readerHandle = -1;
			}
			else if(cardType == CARD_CONTACTLESS)
			{ 
				ContactlessICCardReaderInterface.close();
				if(debug)Log.d(APP_TAG, "ContactlessICCardReaderInterface.close(" + readerSlot + ")");
			}
		}
	}
	
}