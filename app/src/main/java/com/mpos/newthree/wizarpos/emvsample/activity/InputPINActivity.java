package com.mpos.newthree.wizarpos.emvsample.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mpos.newthree.R;
import com.mpos.newthree.activity.MainActivity;
import com.mpos.newthree.wizarpos.emvsample.parameter.ISOParams;
import com.mpos.newthree.wizarpos.jni.PinPadCallbackHandler;
import com.mpos.newthree.wizarpos.jni.PinPadInterface;
import com.mpos.newthree.wizarpos.util.AppUtil;
import com.mpos.newthree.wizarpos.util.StringUtil;

public class InputPINActivity extends FuncActivity1 implements PinPadCallbackHandler
{
	private final int PINPAD_CANCEL  = -65792;
	private final int PINPAD_TIMEOUT = -65538;
	
	private TextView textTitle  = null;
	private TextView textPin = null;
	private Button   buttonBack = null;
    private Button   buttonMore = null;
    
	private Handler mHandler;
	private Thread mReadPINThread;

	private String zonePinKey;
	private String check;
	private final String zoneMasterKey =   "CF188277E109061121BBF6E766557830";
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_input_pin);

		//get zone pin key
		SharedPreferences preferences= MainActivity.context.getSharedPreferences("temp", MainActivity.context.MODE_PRIVATE);
		zonePinKey=preferences.getString("zonePinKey",null);
		check=preferences.getString("checkDigit",null);

        
    	mHandler = new Handler() 
		{ 
			public void handleMessage(Message msg)
			{ /*这里是处理信息的方法*/ 
				switch (msg.what)
				{ 
				case PIN_SUCCESS_NOTIFIER:
					setResult(Activity.RESULT_OK, getIntent());
					break;
				case PIN_ERROR_NOTIFIER:
					appState.setErrorCode(R.string.error_pinpad);
					break;
				case PIN_CANCELLED_NOTIFIER:
					appState.setErrorCode(R.string.error_user_cancelled);
					break;
				case PIN_TIMEOUT_NOTIFIER:
					appState.setErrorCode(R.string.error_input_timeout);
					break;
				} 
				//exit();
			}
		};

    }

	@Override
	protected void onStart()
	{
		super.onStart();
		mReadPINThread=new ReadPINThread(); 
		mReadPINThread.start();
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}
	
    @Override
    protected void onPause()
    {
        super.onPause();
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
    }
    
    @Override
    public void onBackPressed(){

    }
    
    private void notifyPinSuccess()
    {
    	Message msg = new Message();
    	msg.what = PIN_SUCCESS_NOTIFIER;
    	mHandler.sendMessage(msg);
    }
    
    private void notifyPinError()
    {
    	Message msg = new Message();
    	msg.what = PIN_ERROR_NOTIFIER;
    	mHandler.sendMessage(msg);
    }
    
    private void notifyPinCancel()
    {
    	Message msg = new Message();
    	msg.what = PIN_CANCELLED_NOTIFIER;
    	mHandler.sendMessage(msg);
    }
    
    private void notifyPinTimeout()
    {
    	Message msg = new Message();
    	msg.what = PIN_TIMEOUT_NOTIFIER;
    	mHandler.sendMessage(msg);
    }

	protected char[] stars = "●●●●●●●●●●●●●●●●".toCharArray();
	public static final int PIN_AMOUNT_SHOW  = 0x10000;
	public static final int PIN_KEY_CALLBACK = 0x10001;
	private Handler commHandler = createCommHandler();

	public void processCallback(byte[] data) {
		Log.i("processCallback", "" + data);
		if(data != null)
			commHandler.obtainMessage(PIN_KEY_CALLBACK, data[0], data[1]).sendToTarget();
	}

	@SuppressLint("HandlerLeak")
	protected Handler createCommHandler()
	{	// 无 Pinpad时跳过. DuanCS@[20141001]
		return new Handler()
		{
			public void handleMessage(Message msg)
			{ /* 这里是处理信息的方法 */
				switch (msg.what)
				{
				case PIN_AMOUNT_SHOW:	// 其值已通过onFlush显示. DuanCS@[20150907]
//					setTextById(R.id.amount, msg.obj.toString());
					textPin.setText(msg.obj.toString());	// 这一行也不会执行, 因为 Pinpad.showText() 不会触发回调... DuanCS@[20150912]
					System.out.println("PIN_AMOUNT_SHOW..."+msg.obj.toString());
					break;
				case PIN_KEY_CALLBACK:
					textPin.setText(stars, 0, msg.arg1 & 0x0F);
					System.out.println("PIN_AMOUNT_SHOW..."+stars+"...textPin.getText()..."+textPin.getText());
					break;
				}
			}
		};
	}

	class ReadPINThread extends Thread
    {
    	public void run() 
    	{ 
    		byte[] pinBlock = new byte[8];
    		byte[] zeroPAN = new byte[]{'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'};
    		
    		// masterKey is new byte[]{'1','1','1','1','1','1','1','1' }
			//Q1上不支持单倍长PINKEY
    		byte[] defaultPINKey = new byte[]{'2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2' };

			byte[] defaultMasterKey = new byte[]{(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x38};
			/*byte[] MasterKey = new byte[]{(byte)0xCF,(byte)0x18,(byte)0x82,(byte)0x77,(byte)0xE1,(byte)0x09,(byte)0x06,(byte)0x11,(byte)0x21,
					(byte)0xBB,(byte)0xF6,(byte)0xE7,(byte)0x66,(byte)0x55,(byte)0x78,(byte)0x32};*/
			//5242820805099262
			byte[] pan = new byte[]{(byte)0x52,(byte)0x42,(byte)0x82,(byte)0x08,(byte)0x05,(byte)0x09,(byte)0x92,(byte)0x62};

			byte[] NewMasterKey = new byte[]{(byte)0xCF,(byte)0x18,(byte)0x82,(byte)0x77,(byte)0xE1,(byte)0x09,(byte)0x06,(byte)0x11,(byte)0x21,
					(byte)0xBB,(byte)0xF6,(byte)0xE7,(byte)0x66,(byte)0x55,(byte)0x78,(byte)0x30};

			/*byte[] ZONEPINKey = new byte[]{(byte)0xAD,(byte)0xBD,(byte)0xEF,(byte)0x38,(byte)0x2F,(byte)0x0C,(byte)0x15,(byte)0x0B,(byte)0x61,
					(byte)0x40,(byte)0xC2,(byte)0x9C,(byte)0xBF,(byte)0x90,(byte)0x96,(byte)0xDF};*/


			byte[] ZONEPINKey = new byte[]{(byte)0xAD,(byte)0xBD,(byte)0xEF,(byte)0x38,(byte)0x2F,(byte)0x0C,(byte)0x15,(byte)0x0B,(byte)0x61,
					(byte)0x40,(byte)0xC2,(byte)0x9C,(byte)0xBF,(byte)0x90,(byte)0x96,(byte)0xDF};
			byte[] MasterKey = new byte[]{(byte)0xCF,(byte)0x18,(byte)0x82,(byte)0x77,(byte)0xE1,(byte)0x09,(byte)0x06,(byte)0x11,(byte)0x21,
					(byte)0xBB,(byte)0xF6,(byte)0xE7,(byte)0x66,(byte)0x55,(byte)0x78,(byte)0x30};

			//"ADBDEF382F0C150B6140C29CBF9096DF" pin key
			//"CF188277E109061121BBF6E766557830" master key
    		int ret = -1;
    		if(appState.pinpadOpened == false)
    		{
    			if(PinPadInterface.open() < 0)
    		    {
    				notifyPinError();
    				return;
    		    }
			    appState.pinpadOpened = true;

				PinPadInterface.setupCallbackHandler(InputPINActivity.this);
    		}
//appState.terminalConfig.getKeyIndex()
			int masterInt = PinPadInterface.updateMasterKey(1,
					MasterKey,
					MasterKey.length,
					MasterKey,
					MasterKey.length
			);
			System.out.println("master int ..." + masterInt + "..."+zonePinKey);

			//appState.terminalConfig.getKeyIndex()
    	    ret = PinPadInterface.updateUserKey(1,
                                                0,
					ZONEPINKey,
					ZONEPINKey.length
												//hexStringToByteArray(zonePinKey),
												//hexStringToByteArray(zonePinKey).length
			);
			System.out.println("zone key ..." + ret);
			/*ret = PinPadInterface.updateUserKey(appState.terminalConfig.getKeyIndex(),
					0,
					zonePinKey.getBytes(),
					zonePinKey.getBytes().length);*/

			//updateMasterKey(int nMasterKeyID, byte arrayOldKey[], int nOldKeyLength, byte arrayNewKey[], int nNewKeyLength);



    		if(ret < 0)
    		{
    			if(debug)Log.d(APP_TAG, "pinpad open error");
    			notifyPinError();
    			PinPadInterface.close();
    			appState.pinpadOpened = false;
    			return;
    		}
			//Q1上不支持单倍长PINKEY
    		//PinPadInterface.setKey(2, appState.terminalConfig.getKeyIndex(), 0, DOUBLE_KEY);
//    		PinPadInterface.setKey(2, appState.terminalConfig.getKeyIndex(), 0, appState.terminalConfig.getKeyAlgorithm());
    		if(appState.trans.getTransAmount() > 0)
    		{
	    		byte[] text = (AppUtil.formatAmount(appState.trans.getTransAmount())).getBytes();
	    		PinPadInterface.setText(0, text, text.length, 0);

				String instr = "please input pin";
				PinPadInterface.setText(1, instr.getBytes(), instr.getBytes().length, 0);
    		}





			System.out.println("PAN PAN PAN..."+appState.trans.getPAN());
			/*PinPadInterface.selectKey(2,
					1,
					0,
					0);*/

			PinPadInterface.setKey(2, 1, 0, 0);
			//PinPadInterface.setKey(2, appState.terminalConfig.getKeyIndex(), 0, DOUBLE_KEY);
			System.out.println("appState.trans.getPAN().."+appState.trans.getPAN());
    		ret = PinPadInterface.inputPIN(appState.trans.getPAN().getBytes(), appState.trans.getPAN().getBytes().length, pinBlock, 60000, 0);
    		if(ret < 0)
    		{
    			if(ret == PINPAD_CANCEL)
    			{
    				notifyPinCancel();
    			}
    			else if(ret == PINPAD_TIMEOUT)
    			{
    				notifyPinTimeout();
    			}
    			else{
	    			notifyPinError();
    			}
    			PinPadInterface.close();
    			appState.pinpadOpened = false;
    			return;
    		}



			/*PinPadInterface.calculatePINBlock(appState.trans.getPAN().getBytes(), appState.trans.getPAN().getBytes().length(),
					arryPINblock, -1, 0);*/

    		if(ret == 0)
    		{
    			appState.trans.setPinEntryMode(CANNOT_PIN);
    		}
    		else
    		{
				System.out.println("");
				System.out.println("pinblock..."+StringUtil.toHexString(pinBlock, 0, pinBlock.length, false));
				ISOParams.setPinBlock(StringUtil.toHexString(pinBlock, 0, pinBlock.length, false));
	    		appState.trans.setPinBlock(pinBlock);
	    		appState.trans.setPinEntryMode(CAN_PIN);
    		}
    		notifyPinSuccess();
			PinPadInterface.close();
			appState.pinpadOpened = false;
		} 
    }
	
}