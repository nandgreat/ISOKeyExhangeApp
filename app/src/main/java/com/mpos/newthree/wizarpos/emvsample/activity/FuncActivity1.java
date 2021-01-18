package com.mpos.newthree.wizarpos.emvsample.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.KeyEvent;

import com.mpos.newthree.R;
import com.mpos.newthree.wizarpos.emvsample.MainApp;
import com.mpos.newthree.wizarpos.emvsample.constant.Constant;
import com.mpos.newthree.wizarpos.emvsample.transaction.TransDefine;
import com.mpos.newthree.wizarpos.jni.PinPadInterface;
import com.mpos.newthree.wizarpos.util.NumberUtil;
import com.mpos.newthree.wizarpos.util.StringUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class FuncActivity1 extends AppCompatActivity implements Constant
{
	protected static Handler mHandler = null;
    protected static Socket socket = null;
    protected static FuncActivity funcRef;
    protected static MainApp appState = null;
    
    protected static Thread msrThread = null;
    protected static boolean msrThreadActived = false;
    protected static boolean readMSRCard = false;
    protected static boolean msrClosed = true;
    
    protected static boolean contactOpened = false;
    protected static boolean contactlessOpened = false;
    
    protected static Thread mOpenPinpadThread = null;
    
    
    
	private Timer mTimerSeconds;
    private int mIntIdleSeconds;
    private boolean mBoolInitialized=false;
	private byte mTimerMode = 0;
    private int idleTimer = DEFAULT_IDLE_TIME_SECONDS;
    
	// EMV Function
	static
	{
		System.loadLibrary("wizarpos_emv_kernel");
	}
	
	
	public native static byte loadEMVKernel();
	public native static byte exitEMVKernel();

 	// Card Functions
 	public native static int open_reader(int reader);
 	public native static void close_reader(int reader);
 	public native static int poweron_card();
 	public native static int get_card_type();
 	public native static int get_card_atr(byte[] atr);
 	public native static int transmit_card(byte[] cmd, int cmdLength, byte[] respData, int respDataLength);
 	// EMV Functions
 	public native static void emv_kernel_initialize();                                                          // 0
 	public native static int emv_is_tag_present(int tag);                              	                        // 1
 	public native static int emv_get_tag_data(int tag, byte[] data, int dataLength);	                        // 2
 	public native static int emv_get_tag_list_data(int[] tagList, int tagCount, byte[] data, int dataLength);	// 3
 	public native static int emv_set_tag_data(int tag, byte[] data, int dataLength);                     		// 4
 	public native static int emv_preprocess_qpboc();                                                            // 5
 	public native static void emv_trans_initialize();                                                           // 6
 	public native static int emv_get_version_string(byte[] data, int dataLength);					            // 7
 	public native static int emv_set_trans_amount(byte[] amount); 							                    // 8  ASC 以分为单位
 	public native static int emv_set_other_amount(byte[] amount);						                        // 9
 	public native static int emv_set_trans_type(byte transType);							                    //10
 	public native static int emv_set_kernel_type(byte kernelType);							                    //11
 	public native static int emv_process_next();												                //12
 	public native static int emv_is_need_advice();							                                    //13
 	public native static int emv_is_need_signature();							                                //14
 	public native static int emv_set_force_online(int flag);							                        //15
 	public native static int emv_get_card_record(byte[] data, int dataLength);							        //16
 	public native static int emv_get_candidate_list(byte[] data, int dataLength);							    //17
 	public native static int emv_set_candidate_list_result(int index);							                //18
 	public native static int emv_set_id_check_result(int result);							                    //19
 	public native static int emv_set_online_pin_entered(int result);							                //20
 	public native static int emv_set_pin_bypass_confirmed(int result);							                //21
 	public native static int emv_set_online_result(int result, byte[] respCode, byte[] issuerRespData, int issuerRespDataLength); // 22

 	public native static int emv_aidparam_clear();                             							        //23
 	public native static int emv_aidparam_add(byte[] data, int dataLength);							            //24
 	public native static int emv_capkparam_clear();							                                    //25
 	public native static int emv_capkparam_add(byte[] data, int dataLength);    			                    //26
 	public native static int emv_terminal_param_set(byte[] TerminalParam);							            //27
 	public native static int emv_terminal_param_set2(byte[] TerminalParam, int paramLength);
 	public native static int emv_exception_file_clear();							                            //28
 	public native static int emv_exception_file_add(byte[] exceptFile);							                //29
 	public native static int emv_revoked_cert_clear();							                                //30
 	public native static int emv_revoked_cert_add(byte[] revokedCert);							                //31
 	
 	public native static int emv_log_file_clear();                                                              //32
	public native static int emv_set_kernel_attr(byte[] data, int dataLength);
 	
	public void capkChecksumErrorDialog(Context context) 
	{
		Builder builder = new Builder(context);
		builder.setTitle("提示");
		builder.setMessage("CAPK:" + appState.failedCAPKInfo + "\nChecksum Error");

		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
 	public static void emvProcessCallback(byte[] data)
	{
		if(debug)Log.d(APP_TAG, "emvProcessNextCompleted");
		appState.trans.setEMVStatus(data[0]);
		appState.trans.setEMVRetCode(data[1]);
		
     	Message msg = new Message();
     	msg.what = EMV_PROCESS_NEXT_COMPLETED_NOTIFIER;
     	mHandler.sendMessage(msg);
	}
    
	public static void cardEventOccured(int eventType)
	{
 		if(debug)Log.d(APP_TAG, "get cardEventOccured");
 		Message msg = new Message();
 		if(eventType == SMART_CARD_EVENT_INSERT_CARD)
 		{
 			appState.cardType = get_card_type();
 			if(debug)Log.d(APP_TAG, "cardType = " + appState.cardType);
 			
 			if(appState.cardType == CARD_CONTACT)
 			{
 				msg.what = CARD_INSERT_NOTIFIER;
 				mHandler.sendMessage(msg);
 			}
 			else if(appState.cardType == CARD_CONTACTLESS)
 			{
 				msg.what = CARD_TAPED_NOTIFIER;
 				mHandler.sendMessage(msg);
 			}
 			else{
 				appState.cardType = -1;
 			}
 		}
 		else if(eventType == SMART_CARD_EVENT_POWERON_ERROR)
 		{
 			appState.cardType = -1;
 			msg.what = CARD_ERROR_NOTIFIER;
			mHandler.sendMessage(msg);
 		}
 		else if(eventType == SMART_CARD_EVENT_REMOVE_CARD)
 		{
 			appState.cardType = -1;
 		}
 		else if(eventType == SMART_CARD_EVENT_CONTALESS_HAVE_MORE_CARD)
 		{
 			appState.cardType = -1;
 			msg.what = CONTACTLESS_HAVE_MORE_CARD_NOTIFIER;
			mHandler.sendMessage(msg);
 		}

	}
	
    public void setEMVTermInfo()
    {
        //terminal_country_code[2];             // 9F1A: Terminal Country Code  0  + 2
        //TID[8];								// 9F1C                         2  + 8
        //IFD[8];                               // 9F1E: IFD Serial Number      10 + 8
        //transaction_currency_code[2];			// 5F2A                         18 + 2
        //terminal_capabilities[3];             // 9F33                         20 + 3
        //terminal_type[1];						// 9F35                         23 + 1
        //transaction_currency_exponent[1];		// 5F36                         24 + 1
        //additional_terminal_capabilities[5];  // 9F40                         25 + 5
        //merchantNameLength;                                                   30 + 1
        //merchantName[20];                     // 9F4E                         31 +20
        //ttq                                   // 9F66首字节                                                                         51 + 1
        //statusCheckSupport                                                    52 + 1
        //ECTermTransLimit[6];	                // 9F7B                         53 + 6
        //contactlessLimit[6];                                                  59 + 6
        //contactlessFloorLimit[6];                                             65 + 6
        //cvmLimit[6];                                                          71 + 6
        //zeroCheck                                                             77
        //contactLessLimitEnable                                                78
        //contactLessFloorLimitEnable                                           79
        //cvmLimitEnable                                                        80
        //TerminalFloorLimit                                                    81 + 4
        //TerminalFloorLimitEnable                                              85
        //ctlOnDeviceCVMLimit                                                   86 + 6
        //ctlNoOnDeviceCVMLimit                                                 92 + 6

    	byte[] termInfo = new byte[98];
    	System.arraycopy(StringUtil.hexString2bytes(appState.terminalConfig.getCountryCode()), 0, termInfo, 0, 2);
    	System.arraycopy(appState.terminalConfig.getTID().getBytes(), 0, termInfo, 2, 8);
    	System.arraycopy(appState.terminalConfig.getIFD().getBytes(), 0, termInfo, 10, appState.terminalConfig.getIFD().length());  // 8
    	System.arraycopy(StringUtil.hexString2bytes(appState.terminalConfig.getCurrencyCode()), 0, termInfo, 18, 2);
    	System.arraycopy(StringUtil.hexString2bytes(appState.terminalConfig.getTerminalCapabilities()), 0, termInfo, 20, 3);
    	termInfo[23] = StringUtil.hexString2bytes(appState.terminalConfig.getTerminalType())[0];
    	termInfo[24] = appState.terminalConfig.getCurrencyExponent();
    	System.arraycopy(StringUtil.hexString2bytes(appState.terminalConfig.getAdditionalTerminalCapabilities()), 0, termInfo, 25, 5);
    	termInfo[30] = (byte)appState.terminalConfig.getMerchantName1().length();
    	System.arraycopy(appState.terminalConfig.getMerchantName1().getBytes(), 0, termInfo, 31, termInfo[30]); // 20
    	termInfo[51] = appState.terminalConfig.getTTQ();
   		termInfo[52] = appState.terminalConfig.getStatusCheckSupport();
   		System.arraycopy(NumberUtil.intToBcd(appState.terminalConfig.getEcLimit(), 6), 0, termInfo, 53, 6);
   		System.arraycopy(NumberUtil.intToBcd(appState.terminalConfig.getContactlessLimit(), 6), 0, termInfo, 59, 6);
   		System.arraycopy(NumberUtil.intToBcd(appState.terminalConfig.getContactlessFloorLimit(), 6), 0, termInfo, 65, 6);
   		System.arraycopy(NumberUtil.intToBcd(appState.terminalConfig.getCvmLimit(), 6), 0, termInfo, 71, 6);

		termInfo[77] = 1;
		termInfo[78] = 1;
		termInfo[79] = 1;
		termInfo[80] = 1;
		System.arraycopy(NumberUtil.intToBcd(999999, 4), 0, termInfo, 81, 4);
		termInfo[85] = 1;

		System.arraycopy(NumberUtil.intToBcd(999999, 6), 0, termInfo, 86, 6);
		System.arraycopy(NumberUtil.intToBcd(999999, 6), 0, termInfo, 92, 6);

		emv_terminal_param_set2(termInfo, 98);
    }
    
	void setEMVTransAmount(String strAmt)
	{
		byte[] amt = new byte[strAmt.length() + 1];
		System.arraycopy(strAmt.getBytes(), 0, amt, 0, strAmt.length());
		emv_set_trans_amount(amt);
	}
	

    public static boolean loadAID()
    {
    	appState.aids = appState.aidService.query();
    	emv_aidparam_clear();
    	byte[] aidInfo = null;
    	for(int i=0; i< appState.aids.length; i++)
    	{
			if(appState.aids[i] != null)
			{
	    		aidInfo = appState.aids[i].getDataBuffer();
				if(emv_aidparam_add(aidInfo, aidInfo.length) < 0)
					return false;
			}
			else
			{
				break;
			}
    	}
		return true;
    }
    
    public static int loadCAPK()
    {
    	appState.capks = appState.capkService.query();
    	emv_capkparam_clear();
    	byte[] capkInfo = null;
    	for(int i=0; i< appState.capks.length; i++)
    	{
			if(appState.capks[i] != null)
			{
	    		capkInfo = appState.capks[i].getDataBuffer();
				int ret = emv_capkparam_add(capkInfo, capkInfo.length);
	    		if( ret < 0)
				{
					appState.failedCAPKInfo = appState.capks[i].getRID() + "_" + appState.capks[i].getCapki();
	    			return ret;
				}
			}
			else
			{
				break;
			}
    	}
		return 0;
    }
    
    public static boolean loadExceptionFile()
    {
    	appState.exceptionFiles = appState.exceptionFileService.query();
    	emv_exception_file_clear();
    	byte[] exceptionFileInfo = null;
    	for(int i=0; i< appState.exceptionFiles.length; i++)
    	{
			if(appState.exceptionFiles[i] != null)
			{
				exceptionFileInfo = appState.exceptionFiles[i].getDataBuffer();
				if(emv_exception_file_add(exceptionFileInfo) < 0)
					return false;
			}
			else
			{
				break;
			}
    	}
		return true;
    }
    
    public static boolean loadRevokedCAPK()
    {
    	appState.revokedCapks = appState.revokedCAPKService.query();
    	emv_revoked_cert_clear();
    	byte[] revokedCAPKInfo = null;
    	for(int i=0; i< appState.revokedCapks.length; i++)
    	{
			if(appState.revokedCapks[i] != null)
			{
				revokedCAPKInfo = appState.revokedCapks[i].getDataBuffer();
				if(revokedCAPKInfo != null)
				{
					if(emv_revoked_cert_add(revokedCAPKInfo) < 0)
						return false;
				}
			}
			else
			{
				break;
			}
    	}
		return true;
    }
    
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        appState = ((MainApp)getApplicationContext());
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }
    
    public void onTouch()
    {
        //if(debug)Log.d(APP_TAG, "mIntIdleSeconds = 0");
    	mIntIdleSeconds=0;
    }

    public void cancelIdleTimer()
    {
    	mIntIdleSeconds=0;
    	if (mTimerSeconds != null)
        {
            if(debug)Log.d(APP_TAG, "timer cancelled");
        	mTimerSeconds.cancel();
        }
    }
    
    public void startIdleTimer(byte timerMode, int timerSecond)
    {
    	idleTimer = timerSecond;
    	mTimerMode = timerMode;
        //initialize idle counter
        mIntIdleSeconds=0;
    	if (mBoolInitialized == false)
        {
        	if(debug)Log.d(APP_TAG, "timer start");
    		mBoolInitialized = true;
            //create timer to tick every second
            mTimerSeconds = new Timer();
            mTimerSeconds.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    mIntIdleSeconds++;
                    if (mIntIdleSeconds == idleTimer)
                    {
                    	if(mTimerMode == TIMER_IDLE)
                    	{
                    		//go2Idle();
                    	}
                    	else
                    	{
                    		if(appState.needClearPinpad == true)
             		        {
             		        	// clear pinpad
             		        	appState.needClearPinpad = false;
                 	    		PinPadInterface.setText(0, null, 0, 0);
                 	    		PinPadInterface.setText(1, null, 0, 0);
             		        }
            			    
                    		setResult(Activity.RESULT_CANCELED, getIntent());
	                    	exit();
                    	}
                    }
                }
            }, 0, 1000);
        }
    }

    protected boolean connectSocket(String ip, int port, int timeout)
    {
	    try {
	    	socket = new Socket(); 
	    	socket.setSoTimeout(timeout); // 设置读超时
	    	SocketAddress remoteAddr = new InetSocketAddress(ip, port); 
	    	if(debug)
	    	{
	    		Log.d(APP_TAG, "Connect IP[" + ip + "]port[" + port + "]");
	    	}
	    	socket.connect(remoteAddr, timeout);
		} catch (UnknownHostException e) {

		} catch (IOException e) {

		}
	    if(socket!= null && socket.isConnected())
	    {
	    	return true;
	    }
	    return false;
    }
    
    protected void disconnectSocket()
    {
		if(debug)Log.d(APP_TAG, "disconnectSocket");
    	try {
            if(socket != null)
            {
            	socket.close();
            }
        } catch (IOException e) {

        }
    }
    
    protected void readAllCard()
    {
    	if(appState.acceptContactCard)
    	{
    		contactOpened = true;
    		open_reader(1);
    	}
    	if(appState.acceptContactlessCard)
    	{
    		contactlessOpened = true;
    		open_reader(2);
    	}
    }
    
    protected int waitContactCard()
    {
    	contactOpened = true;
    	return open_reader(1);
    }
    
    protected void cancelAllCard()
    {
   		cancelMSRThread();
    	cancelContactCard();
    	cancelContactlessCard();
    }
    
    protected void cancelContactCard()
    {
    	if(debug)Log.d(APP_TAG, "Close contact card");
    	if(contactOpened)
    	{
    		contactOpened = false;
    		close_reader(1);
    	}
    }
    
    
    protected void cancelContactlessCard()
    {
    	if(contactlessOpened)
    	{
    		contactlessOpened = false;
    		close_reader(2);
    	}
    }
    
    private void notifyContactlessCardOpenError()
    {
    	Message msg = new Message();
    	msg.what = CARD_OPEN_ERROR_NOTIFIER;
    	mHandler.sendMessage(msg);
    }
    
    protected void cancelMSRThread()
    {
    	if(readMSRCard == true)
    	{
        	readMSRCard = false;
    	}
    }
     
    protected void offlineSuccess()
    {
    		transSuccess();
    }

	public void saveTrans()
	{
		if(debug)Log.d(APP_TAG, "save trans");
		appState.transDetailService.save(appState.trans);
	}
	
	public void saveAdvice()
	{
		if(debug)Log.d(APP_TAG, "save advice");
		appState.adviceService.save(appState.trans);
	}
	
	public void clearTrans()
	{
		appState.transDetailService.clearTable();
	}
	
	public void clearAdvice()
	{
		appState.adviceService.clearTable();		
	}
	
	public void transSuccess()
	{
		if(appState.getTranType() != TRAN_SETTLE)
		{
		  	if ((TransDefine.transInfo[appState.getTranType()].flag & T_NOCAPTURE) == 0)
		  	{
	  			saveTrans();
	  			appState.batchInfo.incSale(appState.trans.getTransAmount());
		  	}
		}
	}
	
    public void exit()
    {
    	cancelIdleTimer();
    	finish();
    }
    
	public void exitTrans()
	{
		cancelContactlessCard();
		if(appState.cardType == CARD_CONTACT)
		{
			//removeCard();
		}
		else
		{
			appState.initData();
			finish();
		}
	}
	
	// ilde
	/*public void go2Idle()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, IdleActivity.class);
		startActivity(intent);
	}
	
	public void go2Error(int errorCode)
	{
		cancelIdleTimer();
		appState.setErrorCode(errorCode);
		Intent intent = new Intent(this, ErrorActivity.class);
		startActivity(intent);
	}
	
	// menu
	public void requestFuncMenu()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, FuncMenuActivity.class);
		startActivity(intent);
	}*/
	
	/*public void requestDataClear()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, DataClearActivity.class);
		startActivity(intent);
	}

	public void requestEnquireTrans()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, EnquireTransActivity.class);
		startActivity(intent);
	}

	public void showLastPBOC()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, ShowLastPBOCActivity.class);
		startActivity(intent);
	}*/
	
	// trans flow For Result
	/*public void requestCard(boolean acceptContact, boolean acceptContactless)
	{
		cancelIdleTimer();
		appState.setState(STATE_REQUEST_CARD);

		appState.acceptContactCard = acceptContact;
		appState.acceptContactlessCard = acceptContactless;
		Intent intent = new Intent(this, RequestCardActivity.class);
		startActivityForResult(intent, appState.getState());
	}
	
	public void removeCard()
	{
		cancelIdleTimer();
		appState.setState(STATE_REMOVE_CARD);
		Intent intent = new Intent(this, RemoveCardActivity.class);
		startActivityForResult(intent, appState.getState());
	}*/
	
//	public void requestManualCard(boolean acceptMSR, boolean acceptContact, boolean acceptContactless)
//	{
//		cancelIdleTimer();
//		appState.setState(STATE_REQUEST_CARD);
//		appState.acceptMSR = acceptMSR;
//		appState.acceptContactCard = acceptContact;
//		appState.acceptContactlessCard = acceptContactless;
//		Intent intent = new Intent(this, RequestManualCardActivity.class);
//		startActivityForResult(intent, appState.getState());
//	}
	
	/*public void confirmBypassPin()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, ConfirmBypassPinActivity.class);
		startActivityForResult(intent, STATE_CONFIRM_BYPASS_PIN);
	}*/
	
	/*public void confirmCard()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, ConfirmCardActivity.class);
		startActivityForResult(intent, STATE_CONFIRM_CARD);
	}
	
	public void inputAmount()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, InputAmountActivity.class);
		startActivityForResult(intent, STATE_INPUT_AMOUNT);
	}
	
	public void inputPIN()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, InputPINActivity.class);
		startActivityForResult(intent, STATE_INPUT_PIN);
	}
	public void processOnline()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, ProcessOnlineActivity.class);
		startActivityForResult(intent, STATE_PROCESS_ONLINE);
	}
	
	public void selectEMVAppList()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, SelectEMVAppListActivity.class);
		startActivityForResult(intent, STATE_SELECT_EMV_APP);
	}
	
	public void showPBOCCardRecord()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, ShowPBOCCardRecordActivity.class);
		startActivityForResult(intent, STATE_SHOW_EMV_CARD_TRANS);
	}
	
	public void showTransInfo()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, ShowTransInfoActivity.class);
		startActivityForResult(intent, STATE_SHOW_TRANS_INFO);
	}*/

	public void processEMVCard(byte kernelType)
	{
		appState.trans.setEMVKernelType(kernelType);
		Intent intent = new Intent(this, ProcessEMVCardActivity.class);
		startActivityForResult(intent, STATE_PROCESS_EMV_CARD);
	}
	
	/*public void showTransResult()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, TransResultActivity.class);
		startActivityForResult(intent, STATE_TRANS_END);
	}*/
	
	// Trans Object
	/*public void sale()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, Sale.class);
		startActivity(intent);
	}
	
	public void settle()
	{
		cancelIdleTimer();
		Intent intent = new Intent(this, Settle.class);
		startActivity(intent);
	}
	
	public void queryCardRecord(byte recordType)
	{
		appState.recordType = recordType;
		cancelIdleTimer();
		Intent intent = new Intent(this, QueryCardRecord.class);
		startActivity(intent);
	}*/
	//=============== Q1 keyboard =============
	protected void onEnter()
	{
	}

	protected void onCancel()
	{
	}

	protected void onBack()
	{
	}

	protected void onDel()
	{
	}

	protected void onKeyCode(char key)
	{}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(debug) Log.i("FuncActivity", "onKeyDown:"+keyCode);
		onTouch();
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_BACK:
			onBack();
			break;
		case KeyEvent.KEYCODE_ESCAPE:
			onCancel();
			break;
		case KeyEvent.KEYCODE_DEL:
			onDel();
			break;
		case KeyEvent.KEYCODE_ENTER:
			onEnter();
			break;
		case 232://'.'
			onKeyCode('.');
			break;
		default:
			onKeyCode((char) ('0'+(keyCode-KeyEvent.KEYCODE_0)));
			break;
		}
		return true;
	}
	//=============== Q1 keyboard =============

	protected boolean preProcessQpboc()
	{
		//pre-process
		int res = emv_preprocess_qpboc();
		if(res < 0)
		{
			if(res == -2)
			{
				appState.setErrorCode(R.string.error_amount_zero);
			}
			else
			{
				appState.setErrorCode(R.string.error_amount_over_limit);
			}
			emv_set_kernel_type(PBOC_KERNAL);
			return false;
		}
		return true;
	}
}
