package com.mpos.newthree.wizarpos.jni;

public class CashDrawerInterface {
	static {
		//System.loadLibrary("jni_cloudpos_cashdrawer");
		String fileName = "wizarpos_cashdrawer";
		JNILoad.jniLoad(fileName);
	}
	/*
	 * open the money box device
	 * @return value : < 0 : error code
	 * 				   >= 0 : success;	
	 */
	public synchronized native static int open();
	/*
	 * close the money box device
	 * @return value : < 0 : error code
	 * 				   >= 0 : success;
	 */
	
	public synchronized native static int close();
	/*
	 * open money box
	 * @return value : < 0 : error code;
	 *                 >= 0 : success
	 */
	public synchronized native static int kickOut();
}
