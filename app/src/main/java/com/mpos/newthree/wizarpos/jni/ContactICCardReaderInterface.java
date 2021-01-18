package com.mpos.newthree.wizarpos.jni;

import com.mpos.newthree.wizarpos.emvsample.constant.Constant;

public class ContactICCardReaderInterface implements Constant
{
	/*native interface */
	static
	{
		//System.loadLibrary("wizarpos_contact_ic_card");
		String fileName = "wizarpos_contact_ic_card";
		JNILoad.jniLoad(fileName);
	}
	
	public native static int init();
	public native static int terminate();
	
	public native static int pollEvent(int nTimeout_MS, SmartCardEvent event);
	
	public native static int queryMaxNumber();
	public native static int queryPresence(int nSlotIndex);
	
	public native static int open(int nSlotIndex);
	public native static int close(int handle);

	public native static int powerOn(int handle, byte byteArrayATR[], ContactICCardSlotInfo info);
	public native static int powerOff(int handle);
	
	public native static int setSlotInfo(int Handle, ContactICCardSlotInfo info);

	public native static int transmit(int handle, byte byteArrayAPDU[], int nAPDULength, byte byteArrayResponse[]);
}
