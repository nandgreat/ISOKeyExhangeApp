package com.mpos.newthree.wizarpos.jni;


public class ContactlessICCardReaderInterface 
{
	/*native interface */
	static
	{
		System.loadLibrary("wizarpos_contactless_ic_card");
	}
	
	public static int CONTACTLESS_CARD_MODE_AUTO = 0;
	public static int CONTACTLESS_CARD_MODE_TYPE_A = 1;
	public static int CONTACTLESS_CARD_MODE_TYPE_B	= 2;
	public static int CONTACTLESS_CARD_MODE_TYPE_C	= 3;
	
	public static int RC500_COMMON_CMD_GET_READER_VERSION = 0x40;
	
	public native static int init();
	public native static int open();
	public native static int close();
	public native static int searchTargetBegin(int nCardMode, int nFlagSearchAll, int nTimeout_MS);
	public native static int searchTargetEnd();
	public native static int attachTarget(byte byteArrayATR[]);
	public native static int detachTarget();
	
	public native static int transmit(byte byteArrayAPDU[], int nAPDULength, byte byteArrayResponse[]);
	public native static int sendControlCommand(int nCmdID, byte byteArrayCmdData[], int nDataLength);
	
	public native static int pollEvent(int nTimeout_MS, ContactlessEvent event);

}
