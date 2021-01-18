package com.mpos.newthree.com.cn.bluetooth.sdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.newthree.R;
import com.mpos.newthree.com.command.sdk.Command;
import com.mpos.newthree.com.command.sdk.PrintPicture;
import com.mpos.newthree.com.command.sdk.PrinterCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Main{
/******************************************************************************************************/
	// Debugging
	private static final String TAG = "Main_Activity";
	private static final boolean DEBUG = true;
/******************************************************************************************************/
	// Message types sent from the BluetoothService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_CONNECTION_LOST = 6;
	public static final int MESSAGE_UNABLE_CONNECT = 7;
/*******************************************************************************************************/
	// Key names received from the BluetoothService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int REQUEST_CHOSE_BMP = 3;
	private static final int REQUEST_CAMER = 4;
	
	//QRcode
	private static final int QR_WIDTH = 350;
	private static final int QR_HEIGHT = 350;
/*******************************************************************************************************/
	private static final String CHINESE = "GBK";
	private static final String THAI = "CP874";
	private static final String KOREAN = "EUC-KR";
	private static final String BIG5 = "BIG5";
/*********************************************************************************/
	private TextView mTitle;
	EditText editText;
	ImageView imageViewPicture;
	private static boolean is58mm = true;
	private RadioButton width_58mm, width_80;
	private RadioButton thai, big5, Simplified, Korean;
	private CheckBox hexBox;
	private Button sendButton = null;
	private Button testButton = null;
	private Button printbmpButton = null;
	private Button btnScanButton = null;
	private Button btnClose = null;
	private Button btn_BMP = null;
	private Button btn_ChoseCommand = null;
	private Button btn_prtsma = null;
	private Button btn_prttableButton = null;
	private Button btn_prtcodeButton = null;
	private Button btn_scqrcode = null;
	private Button btn_camer = null;

/******************************************************************************************************/
	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the services
	private BluetoothService mService = null;
	private Context ctx;
	private String address;
/***************************   The instruction***************************************************************/

    final String[] itemsen = { "Print Init", "Print and Paper", "Standard ASCII font", "Compressed ASCII font", "Normal size",			
    "Double high power wide", "Twice as high power wide", "Three times the high-powered wide", "Off emphasized mode", "Choose bold mode", "Cancel inverted Print", "Invert selection Print", "Cancel black and white reverse display", "Choose black and white reverse display",
	"Cancel rotated clockwise 90 °", "Select the clockwise rotation of 90 °", "Feed paper Cut", "Beep", "Standard CashBox", 
	"Open CashBox", "Char Mode", "Chinese Mode", "Print SelfTest", "DisEnable Button", "Enable Button" ,
	"Set Underline", "Cancel Underline", "Hex Mode" };
	final byte[][] byteCommands = { 
			{ 0x1b, 0x40, 0x0a },// Reset the printer
			{ 0x0a }, //Print and take paper
			{ 0x1b, 0x4d, 0x00 },// Standard ASCII font
			{ 0x1b, 0x4d, 0x01 },// Compress ASCII fonts
			{ 0x1d, 0x21, 0x00 },// The font does not zoom in
			{ 0x1d, 0x21, 0x11 },// Width and height doubled
			{ 0x1d, 0x21, 0x22 },// Width and height doubled
			{ 0x1d, 0x21, 0x33 },// Width and height doubled
			{ 0x1b, 0x45, 0x00 },// Cancel bold mode
			{ 0x1b, 0x45, 0x01 },//
			{ 0x1b, 0x7b, 0x00 },//
			{ 0x1b, 0x7b, 0x01 },//
			{ 0x1d, 0x42, 0x00 },//
			{ 0x1d, 0x42, 0x01 },//
			{ 0x1b, 0x56, 0x00 },//
			{ 0x1b, 0x56, 0x01 },//
			{ 0x0a, 0x1d, 0x56, 0x42, 0x01, 0x0a },//
			{ 0x1b, 0x42, 0x03, 0x03 },//
			{ 0x1b, 0x70, 0x00, 0x50, 0x50 },//
			{ 0x10, 0x14, 0x00, 0x05, 0x05 },//
			{ 0x1c, 0x2e },//
			{ 0x1c, 0x26 }, //
			{ 0x1f, 0x11, 0x04 }, //
			{ 0x1b, 0x63, 0x35, 0x01 }, //
			{ 0x1b, 0x63, 0x35, 0x00 }, //
			{ 0x1b, 0x2d, 0x02, 0x1c, 0x2d, 0x02 }, //
			{ 0x1b, 0x2d, 0x00, 0x1c, 0x2d, 0x00 }, //
			{ 0x1f, 0x11, 0x03 }, //
	};
/***************************条                          码***************************************************************/
	final String[] codebar = { "UPC_A", "UPC_E", "JAN13(EAN13)", "JAN8(EAN8)", 
							   "CODE39", "ITF", "CODABAR", "CODE93", "CODE128", "QR Code" };
	final byte[][] byteCodebar = { 
			{ 0x1b, 0x40 },//
			{ 0x1b, 0x40 },//
			{ 0x1b, 0x40 },//
			{ 0x1b, 0x40 },//
			{ 0x1b, 0x40 },//
			{ 0x1b, 0x40 },//
			{ 0x1b, 0x40 },//
			{ 0x1b, 0x40 },//
			{ 0x1b, 0x40 },//
			{ 0x1b, 0x40 },//
	};
/******************************************************************************************************/
	public Main(Context ctx) {
		this.ctx = ctx;
		/*
		if (DEBUG)
			Log.e(TAG, "+++ ON CREATE +++");

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(ctx, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
		}
		*/
		mService = new BluetoothService(ctx, mHandler);
	}

	public void connect(String address){
		this.address = address;
		mService.pairPrinter(this.address);
	}

	public BluetoothService getBluetoothService(){
		return mService;
	}

	public void onStart() {
		// If Bluetooth is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (mService == null)
			System.out.println(mService + "..mService..");
			mService = new BluetoothService(ctx, mHandler);
	}

	public void onResume() {
		if (mService != null) {
			if (mService.getState() == BluetoothService.STATE_NONE) {
				// Start the Bluetooth services
				mService.start();
			}
		}
	}

	public void onPause() {
		if (DEBUG)
			Log.e(TAG, "- ON PAUSE -");
	}

	public void onStop() {
		if (DEBUG)
			Log.e(TAG, "-- ON STOP --");
	}

	public void onDestroy() {
		// Stop the Bluetooth services
		if (mService != null)
			mService.stop();
		if (DEBUG)
			Log.e(TAG, "--- ON DESTROY ---");
	}

	/*
	 * SendDataString
	 */
	private void SendDataString(String data) {
		
		if (mService.getState() != BluetoothService.STATE_CONNECTED) {

			return;
		}
		if (data.length() > 0) {				
				System.out.println(this.address + " " + data.getBytes() + "....");
				//mService.pairPrinter(this.address);
				mService.writes(data.getBytes());
				//mService.write(data.getBytes("GBK"));
		}
	}
	
	/*
	 *SendDataByte 
	 */
	private void SendDataByte(byte[] data) {
		/*if (mService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(ctx, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}*/
		System.out.println("..testing 1..");
		System.out.println(this.address + " " + data + "....");
		if(data != null) {
			System.out.println("..testing 2..");
			mService.writes(data);
		}else{
			System.out.println("..testing 3..");
			System.out.println("..testing 3..");
			return;
		}
		//mService.write(data);
	}

	/****************************************************************************************************/
	@SuppressLint("HandlerLeak") 
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (DEBUG)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					Print_Test();//
					break;
				case BluetoothService.STATE_CONNECTING:

					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					Toast.makeText(ctx, R.string.title_not_connected + "..1..", Toast.LENGTH_SHORT).show();
					break;
				}
				break;
			case MESSAGE_WRITE:
				
				break;
			case MESSAGE_READ:
				
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				System.out.println(msg.getData().getString(DEVICE_NAME) + "..msg.getData().getString(DEVICE_NAME)..");
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(ctx,
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(ctx,
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			case MESSAGE_CONNECTION_LOST:    //
                Toast.makeText(ctx, "Device connection was lost",
                               Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_UNABLE_CONNECT:     //
            	Toast.makeText(ctx, "Unable to connect device",
                        Toast.LENGTH_SHORT).show();
            	break;
			}
		}
	};

	public void connectPrinter(BluetoothDevice device, String name){
		// Get the BLuetoothDevice object
		//System.out.println("connectPrinter..." + address);
		//System.out.println("BluetoothAdapter.checkBluetoothAddress(address)..." + BluetoothAdapter.checkBluetoothAddress(address));
		System.out.println("mBluetoothAdapter!= null ..." + mBluetoothAdapter);
		if ( device!= null ) {
			// Attempt to connect to the device
			mService.connect(device);
		}else{
			return;
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (DEBUG)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:{
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK) {
					// Get the device MAC address
					String address = data.getExtras().getString(
							DeviceListActivity.EXTRA_DEVICE_ADDRESS);
					// Get the BLuetoothDevice object
					if (BluetoothAdapter.checkBluetoothAddress(address)) {
						BluetoothDevice device = mBluetoothAdapter
								.getRemoteDevice(address);
						// Attempt to connect to the device
						mService.connect(device);
					}
				}
				break;
			}
			case REQUEST_ENABLE_BT:{
				// When the request to enable Bluetooth returns
				if (resultCode == Activity.RESULT_OK) {
					// Bluetooth is now enabled, so set up a session
					//KeyListenerInit();
				} else {
					// User did not enable Bluetooth or an error occured
					Log.d(TAG, "BT not enabled");

					//finish();
				}
				break;
			}
		}
	}

/****************************************************************************************************/
	/**
	 * Print the test page after the connection is successful
	 */
	private void Print_Test(){
		String lang = ctx.getString(R.string.strLang);
  		if((lang.compareTo("en")) == 0){
			String msg = "Congratulations!\n\n";
			String data = "You have sucessfully created communications between your device and our bluetooth printer.\n"
	                +"  the company is a high-tech enterprise which specializes" +
	                " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n";
			SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 1, 1, 0));
			SendDataByte(PrinterCommand.POS_Print_Text(data, CHINESE, 0, 0, 0, 0));
			SendDataByte(PrinterCommand.POS_Set_Cut(1));
			SendDataByte(PrinterCommand.POS_Set_PrtInit());
		}
  	}

	private void BluetoothPrintTest() {
		String msg = "";
		String lang = ctx.getString(R.string.strLang);
		if((lang.compareTo("en")) == 0 ){
			msg = "Division I is a research and development, production and services in one high-tech research and development, production-oriented enterprises, specializing in POS terminals finance, retail, restaurants, bars, songs and other areas, computer terminals, self-service terminal peripheral equipment R & D, manufacturing and sales! \n company's organizational structure concise and practical, pragmatic style of rigorous, efficient operation. Integrity, dedication, unity, and efficient is the company's corporate philosophy, and constantly strive for today, vibrant, the company will be strong scientific and technological strength, eternal spirit of entrepreneurship, the pioneering and innovative attitude, confidence towards the international information industry, with friends to create brilliant information industry !!! \n\n\n";
			SendDataString(msg);
		}
	}

	//This prints the receipt
	@SuppressLint("SimpleDateFormat")
	public void PrintBalance(){
		String lang = ctx.getString(R.string.strLang);
		if((lang.compareTo("en")) == 0){
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd/ HH:mm:ss ");
			Date curDate = new Date(System.currentTimeMillis());//获取当前时间
			String str = formatter.format(curDate);
			String date = str + "\n\n\n\n\n\n";
			if (is58mm) {

				try {
					int nPaperWidth = 100;
					int nMode = 0;
					Bitmap mBitmap = getImageFromAssetsFile("onepay_small.png");
					byte[] data = PrintPicture.POS_PrintBMP(mBitmap, nPaperWidth, nMode);
					byte[] qrcode = PrinterCommand.getBarCommand("Zijiang Electronic Thermal Receipt Printer!", 0, 3, 6);//
					Command.ESC_Align[2] = 0x01;
					SendDataByte(Command.ESC_Align);
					//SendDataByte(qrcode);
					SendDataByte(data);

					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x11;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("NIKE Shop\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x00;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x00;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("Number:  888888\nReceipt  S00003333\nCashier：1001\nDate：xxxx-xx-xx\nPrint Time：xxxx-xx-xx  xx:xx:xx\n".getBytes("GBK"));
					SendDataByte("Name    Quantity    price  Money\nShoes   10.00       899     8990\nBall    10.00       1599    15990\n".getBytes("GBK"));
					SendDataByte("Quantity：             20.00\ntotal：                16889.00\npayment：              17000.00\nKeep the change：      111.00\n".getBytes("GBK"));
					SendDataByte("company name：NIKE\nSite：www.xxx.xxx\naddress：ShenzhenxxAreaxxnumber\nphone number：0755-11111111\nHelpline：400-xxx-xxxx\n================================\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x01;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x11;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("Welcome again!\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x00;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x00;
					SendDataByte(Command.GS_ExclamationMark);

					SendDataByte("(The above information is for testing template, if agree, is purely coincidental!)\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x02;
					SendDataByte(Command.ESC_Align);
					SendDataString(date);
					SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(48));
					SendDataByte(Command.GS_V_m_n);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					byte[] qrcode = PrinterCommand.getBarCommand("Zijiang Electronic Thermal Receipt Printer!", 0, 3, 8);
					Command.ESC_Align[2] = 0x01;
					SendDataByte(Command.ESC_Align);
					SendDataByte(qrcode);

					Command.ESC_Align[2] = 0x01;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x11;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("NIKE Shop\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x00;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x00;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("Number: 888888\nReceipt  S00003333\nCashier：1001\nDate：xxxx-xx-xx\nPrint Time：xxxx-xx-xx  xx:xx:xx\n".getBytes("GBK"));
					SendDataByte("Name                   c Quantity price  Money\nNIKErunning shoes        10.00   899     8990\nNIKEBasketball Shoes     10.00   1599    15990\n".getBytes("GBK"));
					SendDataByte("Quantity：               20.00\ntotal：                  16889.00\npayment：                17000.00\nKeep the change：                111.00\n".getBytes("GBK"));
					SendDataByte("company name：NIKE\nSite：www.xxx.xxx\naddress：shenzhenxxAreaxxnumber\nphone number：0755-11111111\nHelpline：400-xxx-xxxx\n================================================\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x01;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x11;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("Welcome again!\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x00;
					SendDataByte(Command.ESC_Align);
					Command.GS_ExclamationMark[2] = 0x00;
					SendDataByte(Command.GS_ExclamationMark);
					SendDataByte("(The above information is for testing template, if agree, is purely coincidental!)\n".getBytes("GBK"));
					Command.ESC_Align[2] = 0x02;
					SendDataByte(Command.ESC_Align);
					SendDataString(date);
					SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(48));
					SendDataByte(Command.GS_V_m_n);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	 private Bitmap getImageFromAssetsFile(String fileName) {
		 Bitmap image = null;
		 AssetManager am = ctx.getResources().getAssets();
		 try {
			 InputStream is = am.open(fileName);
			 image = BitmapFactory.decodeStream(is);
			 is.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }

		 return image;
	 }

}