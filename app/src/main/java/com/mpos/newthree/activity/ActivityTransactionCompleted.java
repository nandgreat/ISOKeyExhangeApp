package com.mpos.newthree.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mpos.newthree.R;
import com.mpos.newthree.com.cn.bluetooth.sdk.BluetoothService;
import com.mpos.newthree.com.cn.bluetooth.sdk.BluetoothService1;
import com.mpos.newthree.com.command.sdk.Command;
import com.mpos.newthree.com.command.sdk.PrintPicture;
import com.mpos.newthree.com.command.sdk.PrinterCommand;
import com.mpos.newthree.dao.ISOMessage;
import com.mpos.newthree.dao.Preferences;
import com.mpos.newthree.obj.Transaction;
import com.mpos.newthree.obj.TransactionUpdateDBHelper;
import com.mpos.newthree.security._c;
import com.mpos.newthree.utils.Utils;
import com.mpos.newthree.wizarpos.emvsample.printer.PrinterException;
import com.mpos.newthree.wizarpos.jni.PrintSlip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static com.mpos.newthree.BuildConfig.DEBUG;
import static com.mpos.newthree.activity.CardPayActivity.tId;
import static com.mpos.newthree.activity.MainActivity.Q2;
import static com.mpos.newthree.dao.ISOMessage.pan;
import static com.mpos.newthree.dao.ISOMessage.stan;
import static com.mpos.newthree.wizarpos.jni.PrintSlip.amount;
import static com.mpos.newthree.wizarpos.jni.PrintSlip.transactionid;

public class ActivityTransactionCompleted extends AppCompatActivity {
    ConstantFile connect=new ConstantFile();
    private static final String TAG = "ActivityTransactionCompleted";
    ConstantFile constanst=new ConstantFile();
    String TRANSACTION_URL =constanst.getUrl("savetrans");
    String HTTP_JSON_URL = constanst.getUrl("fetcheachtrans");
    public static String paymentMedium;
    RequestQueue queue;
    public static boolean reprint=false;
    public static String reprintid="";
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    public static String transState="";
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

    /******************************************************************************************************/
    // Name of the connected device
    private String mConnectedDeviceName ;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter ;
    // Member object for the services
    private BluetoothService1 mService;
    private Context ctx;
    private String address;

    TextView printStatus;
    private Bitmap logo;
    private final boolean is58mm = true;
    private static final String UTF8 = "UTF8";
    private Preferences pref;
    public static boolean active = false;

    public static String responseCode;
    private Transaction transaction;
    TransactionUpdateDBHelper dbHelper;
    private String transactionStatus;
    private String ledgerb;
    private String mainb;
    org.json.simple. JSONObject json =null;
    org.json.simple.JSONArray arrayElementOneArray =null;
    org.json.simple.JSONObject arrayElementOneArrayElementOne = null;
    AppCompatButton print =null;
   // public static List<CartHelper> cartList2 = new ArrayList<>();
    JSONArray categories =null;
    Toolbar tool;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_completed);
            context=getApplicationContext();
        //tool=(Toolbar)findViewById(R.id.my_toolbar) ;
        //setSupportActionBar(tool);
        getSupportActionBar().setTitle("Transaction Report");
        TextView ledger = (TextView) findViewById(R.id.ledger_bal);
        TextView payment=(TextView)findViewById(R.id.paymentfor);
        TextView card=(TextView)findViewById(R.id.cardname);
        json = new  org.json.simple.JSONObject();
        //dbManager = new DBManager(getApplication());
        pref = new Preferences(getApplicationContext());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
     queue = Volley.newRequestQueue(this);
        transactionStatus = getIntent().getExtras().getString("status");
        transState=transactionStatus;
        try {
            if (ISOMessage.pan != null && ActivityTransactionCompleted.responseCode.equalsIgnoreCase("00")) {
                System.out.println("success");
                completePaymentViaCard( ConstantFile.ref_code, ConstantFile.userPhone,ActivityTransactionCompleted.responseCode,transState,ConstantFile.amount);
            }else  if (ISOMessage.pan != null && !ActivityTransactionCompleted.responseCode.equalsIgnoreCase("00")) {
                System.out.println("failed");
                completePaymentViaCard( ConstantFile.ref_code, ConstantFile.userPhone,ActivityTransactionCompleted.responseCode,transState,ConstantFile.amount);

            }
        }catch(Exception ec){}
        try {
            if (ISOMessage.pan != null && !ActivityTransactionCompleted.responseCode.equalsIgnoreCase("00")) {
             //   addItem();

            }
        }catch(Exception ex){}
        try {
            if( ActivityTransactionCompleted.paymentMedium.equalsIgnoreCase("wallet") &&  PrintSlip.transactionStatus.equalsIgnoreCase("Transaction Completed Successfully")){
                card.setText("VUVAA WALLET: " +ConstantFile.userPhone);

            }else  if( ActivityTransactionCompleted.paymentMedium.equalsIgnoreCase("wallet") &&  !PrintSlip.transactionStatus.equalsIgnoreCase("Transaction Completed Successfully")){
               card.setText("VUVAA WALLET: " +ConstantFile.userPhone);


            }
        }catch(Exception ex){}


        TextView status =
                (TextView) findViewById(R.id.t_status);

        status.setText("Status: "+transactionStatus);

       printStatus = (TextView) findViewById(R.id.print_status);

        ledgerb = getIntent().getExtras().getString("ledger");


        TextView main =
                (TextView) findViewById(R.id.t_status);
        mainb = getIntent().getExtras().getString("ledger");
        if(mainb!=null) {
            main.setText("Main Balance: " + mainb);
            //amt = mainb;

        }

        dbHelper  = new TransactionUpdateDBHelper(this);
        transaction = new Transaction();
        if(reprint){
            viewTransactionById(reprintid, HTTP_JSON_URL);
        }
        //logTransactions();
        Button done =
                (Button) findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityTransactionCompleted.this, MainActivity.class);
                intent.putExtra("NewClicked", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

       print =
                (AppCompatButton) findViewById(R.id.print);
        System.out.println("Q2 in act "+Q2);

















        try{
        if( !ActivityTransactionCompleted.responseCode.equalsIgnoreCase("00")){
            print.setVisibility(View.INVISIBLE);

        }else{
            print.setVisibility(View.VISIBLE);
        }}catch(Exception nn){}
        try {

            if(Q2){
                System.out.println("in gone");
                // print.setVisibility(View.VISIBLE);
                print.setVisibility(View.VISIBLE);

            }else if(!Q2){
                //print.setVisibility(View.GONE);
                print.setVisibility(View.VISIBLE);
            }

        }catch(Exception ex){}


            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        System.out.println("reprintid "+reprintid+ "reprint "+reprint);
                        if(reprint){

                            if(Q2){
                                CardPayActivity.transType = "BILL_PAY";
                                printReceipt();
                            }else {


                               // printReceipt();

                                if(!Q2){
                                    CardPayActivity.transType = "non";
                                    if ((pref.getBlutoothAddress() != null && !"".equals(pref.getBlutoothAddress()))
                                            || (pref.getBlutoothAddress() != null && !"".equals(pref.getBlutoothAddress()))) {
                                        // Get local Bluetooth adapter
                                        mService = new BluetoothService1(ActivityTransactionCompleted.this, mHandler);
                                        // If the adapter is null, then Bluetooth is not supported
                                        if (mBluetoothAdapter == null) {
                                            Toast.makeText(getApplicationContext(), "Bluetooth is not available, cannot print",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                        if (!mBluetoothAdapter.isEnabled()) {
                                            Intent enableIntent = new Intent(
                                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                                            // Otherwise, setup the session
                                        } else {
                                            if (mService == null)
                                                mService = new BluetoothService1(ActivityTransactionCompleted.this, mHandler);
                                            //KeyListenerInit();//监听
                                        }
                                    }


                                    String resMsg = "";//responseCode();
                                    if (resMsg != null && !"".equals(resMsg)) {

                                        if (pref.getBlutoothAddress() != null && !"".equals(pref.getBlutoothAddress())) {
                                            if (mBluetoothAdapter.enable() && mService.getState() != BluetoothService.STATE_CONNECTED) {
                                                address = pref.getBlutoothName();
                                                if (BluetoothAdapter.checkBluetoothAddress(pref.getBlutoothAddress()) && mBluetoothAdapter != null) {
                                                    BluetoothDevice device = mBluetoothAdapter
                                                            .getRemoteDevice(pref.getBlutoothAddress());
                                                    mService.connect(device);
                                                }
                                            }
                                        }
                                    } else {
                                        if (pref.getBlutoothAddress() != null && !"".equals(pref.getBlutoothAddress())) {
                                            if (mBluetoothAdapter.enable() && mService.getState() != BluetoothService.STATE_CONNECTED) {
                                                address = pref.getBlutoothName();
                                                if (BluetoothAdapter.checkBluetoothAddress(pref.getBlutoothAddress()) && mBluetoothAdapter != null) {
                                                    BluetoothDevice device = mBluetoothAdapter
                                                            .getRemoteDevice(pref.getBlutoothAddress());
                                                    mService.connect(device);
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    printReceipt();
                                }







                            }
                        }else{
                            if(Q2) {
                                System.out.println("in Q2");
                                printReceipt();
                            }else{



                            }
                        }
/*if(Q2) {
    printReceipt();l
}else{
    CardPayActivity.transType="non";
    viewTransactionById(reprintid,HTTP_JSON_URL);
    printReceipt();
}*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });




      /*  try{
            switch (CardPayActivity.transType){
                case"VTU":
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");//dd-MM-yyyy
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    final String str = formatter.format(curDate);
                    PrintSlip.date = str;
                    PrintSlip.amount = amount;
                    PrintSlip.provider = CardPayActivity.provider;
                    PrintSlip.phone = CardPayActivity.providerPhone;
                    PrintSlip.transactionStatus =transactionStatus;
                    break;
                case"BILL_PAYMENT":
                    PrintSlip.amount = amount;
                    //PrintSlip.plateNo = plateNo;
                    PrintSlip.name = name;
                    PrintSlip.phone = phone;
                    PrintSlip.transactionStatus =transactionStatus;
                    break;
                case"BAL_ENQ":

                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
*/


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityTransactionCompleted.this, MainActivity.class);
        intent.putExtra("NewClicked", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("destroyed");
        PrintSlip.tId= null;
        amount = null;
        PrintSlip.plateNo = null;
        PrintSlip.name = null;
        PrintSlip.phone = null;
        PrintSlip.pin = null;
        PrintSlip.transactionStatus =null;
        PrintSlip.date =null;
        pan = null;
        stan = null;
      //  cartList = new ArrayList<>();
        //partList = new ArrayList<>();

        // Stop the Bluetooth services
        if (mService != null)
            mService.stop();

    }



    synchronized public void printReceipt() throws PrinterException
    {

        try {
            final PrintSlip print = new PrintSlip();
            int open = print.open();
            if(open >= 0){
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.oyoimage);
                print.writesBitmap(bitmap);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");//dd-MM-yyyy
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                final String str = formatter.format(curDate);
                PrintSlip.tId= tId;
                //print.writeLineBreak(10);
                switch (CardPayActivity.transType){
                    case "VTU":

                        PrintSlip.date = str;
                        amount = amount;
                        PrintSlip.provider = CardPayActivity.provider;
                        PrintSlip.phone = CardPayActivity.providerPhone;
                        PrintSlip.transactionStatus =transactionStatus;
                        print.writesVTU();
                        break;
                    case"BILL_PAY":

                        PrintSlip.provider = CardPayActivity.provider;
                        amount = amount;
                        PrintSlip.transactionStatus =transactionStatus;

                       print.writesBill();//BAL_INQ
                        break;
                    case"BAL_INQ":
                        PrintSlip.date = str;

                        System.out.println("in BAL_");
                        print.writesBalance();
                        break;
                    case"non":
                        //PrintSlip.date = str;
                        PrintSlip.transactionStatus ="Transaction Successful";
                        System.out.println("history" +"amount= "+ amount);
                        print.writesBill();
                        break;
                }
                bitmap = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.onepay);
                print.writesBitmap(bitmap);
                bitmap = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.printend);
                print.writesBitmap(bitmap);


            }else{
                Toast.makeText(ActivityTransactionCompleted.this,"Printer failed to open",Toast.LENGTH_LONG).show();
            }


        } catch (UnsupportedEncodingException e) {
            throw new PrinterException("PrinterHelper.printReceipt():" + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new PrinterException(e.getMessage(), e);
        }
    }

    //This prints the receipt
    @SuppressLint("SimpleDateFormat")
    public void PrintReceipt2() throws UnsupportedEncodingException {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String lang = getString(R.string.strLang);
        if((lang.compareTo("en")) == 0){

            if (is58mm) {
                int nPaperWidth = 400;
                int nMode = 0;
                logo = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.aedc_);
                if( logo != null) {
                    byte[] data = PrintPicture.POS_PrintBMP(logo, nPaperWidth, nMode);
                    Command.ESC_Align[2] = 0x01;
                    SendDataByte(Command.ESC_Align);
                    //print_image(mBitmap);
                    SendDataByte(data);
                }
                Command.ESC_Align[2] = 0x01;
                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                String msg = "102, Ogunnusi Road Ojodu Berger Lagos\n\n".toUpperCase();
                SendDataByte(com.mpos.newthree.com.command.sdk.PrinterCommand.POS_Print_Text(msg, UTF8, 0, 0, 0, 0));
                String form = "Reciept: \n"+ reprintid + "\n";
                form = "..................................\n";
                 form = "Transaction Status: \n"+ transactionStatus + "\n";

                if(PrintSlip.pin != null){
                    form = form +"PIN: \n"+ PrintSlip.pin + "\n\n";
                }
                if(PrintSlip.plateNo != null){
                    form = form +"Plate No: \n"+ PrintSlip.plateNo + "\n\n";
                }
                if(PrintSlip.name != null){
                    form = form +"Name: \n"+ PrintSlip.name + "\n\n";
                }
                if(PrintSlip.phone != null){
                    form = form +"Phone No: \n"+ PrintSlip.phone + "\n\n";
                }
                if(stan != null){
                    form = form +"STAN: \n"+stan+ "\n\n";
                }
                if(pan != null){
                    form = form +"CARD NO: \n"+ Utils.maskPAN(pan)+ "\n\n";
                }

                form = form +"Status: \n"+ PrintSlip.transactionStatus+ "\n\n";


              /*  if (name != null) {
                    form = form +"Name: "+ PrintSlip.name+ "\n\n";
                }

                if (phone != null) {
                    form = form +"Phone: "+ PrintSlip.phone+ "\n\n";
                }

                if (cartList.size()>0) {

                    form = form +"--------------------------------\n"
                            +"ITEM(S)\n" +
                            "--------------------------------\n";
                    String offences = "";
                    for (CartHelper i : cartList) {
                        offences=offences + i.getCartname()+"\n";
                        offences=offences + "N "+i.getAmount()+"\n\n";
                    }
                    form=form+offences;
                }
*/

                //   +"\nPAN: " + Utils.maskPAN(pan);
                String token =": ";
            /*    String to = "--------------------------------\n"+
                        "Amount: "+ MainActivity.formatCurrency(amount)
                        +"\n--------------------------------\n" +
                        "THANK YOU! ";*/
                Command.ESC_Align[2] = 0x00;
                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte("--------------------------------\n".getBytes("GBK"));
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte(form.getBytes("GBK"));
                SendDataByte(Command.GS_ExclamationMark);
               // SendDataByte(to.getBytes("GBK"));
                SendDataByte(Command.GS_ExclamationMark);
                //SendDataByte(to.getBytes("GBK"));
                logo = null;


                SendDataByte("\n".getBytes("GBK"));
                SendDataByte("\n".getBytes("GBK"));
                SendDataByte("\n".getBytes("GBK"));
                Command.ESC_Align[2] = 0x01;
                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x02;
                SendDataByte(Command.GS_ExclamationMark);
            }
        }
    }



    private void enableBluetooth(){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, 1000);
    }



    private void connectPrinter(){
        if (mService == null)
            mService = new BluetoothService1(this, mHandler);
        //KeyListenerInit();//监听

/*
        pref = new Preferences(ActivityTransactionCompleted.this);
        if( pref.getBlutoothAddress() != null && !"".equals(pref.getBlutoothAddress()) ){
            if(mBluetoothAdapter.enable() && mService.getState() != BluetoothService.STATE_CONNECTED ){
                System.out.println(pref.getBlutoothAddress()+"...pref.getBlutoothAddress()...");
                address = pref.getBlutoothName();
                System.out.println(BluetoothAdapter.checkBluetoothAddress(address)+"...pref.getBlutoothAddress()...BluetoothAdapter.checkBluetoothAddress(address)");
                if (BluetoothAdapter.checkBluetoothAddress(pref.getBlutoothAddress()) && mBluetoothAdapter!= null ) {
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(pref.getBlutoothAddress());
                    mService.connect(device);
                }
            }else {
                printStatus.setText("No printer saved in device");
            }
        }else {
            printStatus.setText("No printer saved on device use the setting to add a bluetooth printer");
        }
*/


    }

    /****************************************************************************************************/
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:

                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            printStatus.setText("Printer Connected");
                            try {
                                print.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                       try {
                                           PrintReceipt2();
                                       }catch(UnsupportedEncodingException ex){}
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case BluetoothService.STATE_CONNECTING:
                          //  printStatus.setText(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            printStatus.setText(R.string.title_not_connected);
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
                    printStatus.setText("Printer Connected");
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(ActivityTransactionCompleted.this,
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_CONNECTION_LOST:    //
                    printStatus.setText("Printer Disconnected");
                    break;
                case MESSAGE_UNABLE_CONNECT:     //
                    printStatus.setText("Unable to connect printer");
                    break;
            }
        }
    };
     /*
     *SendDataByte
     */

    private void SendDataByte(byte[] data) {
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            printStatus.setText("No printer saved on device use the setting to add a bluetooth printer");
            return;
        }
        mService.write(data);
    }

public void sendTransactionReport(String pan) {
    //System.out.println("size= "+cartList.size());
    int count=0;
    arrayElementOneArray=new org.json.simple.JSONArray();
    try {
       // arrayElementOneArray = new  org.json.simple.JSONArray[cartList.size()];
       /* if(cartList.size()>0 && !cartList.toString().equalsIgnoreCase("[]")&& cartList!=null){
            arrayElementOneArray=new org.json.simple.JSONArray();
            for(CartHelper li: cartList ){
                System.out.println("counting  cartList"+li.getProductid());
                arrayElementOneArrayElementOne = new  org.json.simple.JSONObject();
                arrayElementOneArrayElementOne.put("item_id", li.getProductid());
                arrayElementOneArrayElementOne.put("amount", li.getAmount());
                arrayElementOneArrayElementOne.put("item_name", li.getCartname());

                arrayElementOneArray.add(arrayElementOneArrayElementOne);

            }
            System.out.println("item is not empty "+cartList.toString());
            json.put("item", arrayElementOneArray);
        }else{
            System.out.println("item is empty");
            arrayElementOneArray=new org.json.simple.JSONArray();
            arrayElementOneArray.add(arrayElementOneArrayElementOne);
            json.put("item", arrayElementOneArray);
        }*/


       /* if(partList.size()>0){
            arrayElementOneArray=new org.json.simple.JSONArray();
            arrayElementOneArrayElementOne=null;
            for(CartHelper l2: partList){
                System.out.println("counting part "+l2.getProductid());
                arrayElementOneArrayElementOne = new  org.json.simple.JSONObject();
                arrayElementOneArrayElementOne.put("part_id", l2.getProductid());
                arrayElementOneArrayElementOne.put("amount", l2.getAmount());
                arrayElementOneArrayElementOne.put("part_name", l2.getCartname());
                arrayElementOneArrayElementOne.put("part_quantity", l2.getUserid());

                arrayElementOneArray.add(arrayElementOneArrayElementOne);

            }
            json.put("part", arrayElementOneArray);
        }*/

        json.put("trans", transactionid);
        json.put("phone", PrintSlip.phone);
        json.put("trans_method",paymentMedium);
        System.out.println("term "+ ISOMessage.terminalId);
        json.put("terminal_id", ISOMessage.terminalId);
        if(pan!=null){
            json.put("pan", Utils.maskPAN(pan));

        }
        if(stan!=null){
            json.put("stan",stan);
        }
        json.put("channel","pos");
        //channel
      //  json.put("sold_by",MainActivity.UserId);
        json.put("customer", PrintSlip.name);
        json.put("item",arrayElementOneArray);
        System.out.println("json "+json);
        validateAutoUser(new JSONObject(json),TRANSACTION_URL);

    }catch(Exception ex){}
}



    private void validateAutoUser(JSONObject data,String url) {
        System.out.println("data "+data);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, data,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

               // Toast.makeText(getApplicationContext(), "Failed: Please Check your Internet Connectivity", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");

                return headers;
            }



        };

        jsonObjReq.setTag(TAG);
        // Adding request to request queue
        queue.add(jsonObjReq);

        // Cancelling request
    /* if (queue!= null) {
    queue.cancelAll(TAG);
    } */

    }

    public void viewTransactionById(String transid,String url) {
        Map<String, String> postParam= new HashMap<String, String>();
        //U1MjU1M0FDOUZ.Qz
        postParam.put("transid", new _c()._e(transid));

        System.out.println("JSONBalance(postParam) "+new JSONObject(postParam));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println("response +++++ "+response);
                            JSON_PARSE_DATA_AFTER_WEBCALL(response);

                        }catch(Exception ex){}
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("failed "+error.getMessage());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");

                return headers;
            }



        };

        jsonObjReq.setTag(TAG);
        // Adding request to request queue
        queue.add(jsonObjReq);

        // Cancelling request
    /* if (queue!= null) {
    queue.cancelAll(TAG);
    } */

    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONObject array1){
        //cartList = new ArrayList<>();
        System.out.println("array1 main "+array1);
       try {
           categories=array1.getJSONArray("trans");
           System.out.println("cat "+categories);
       }catch(Exception ex){}
       try {
           System.out.println("meth "+array1.getString("transMethod"));
           ActivityTransactionCompleted.paymentMedium=array1.getString("transMethod");
           PrintSlip.transactionid=reprintid;
           PrintSlip.date=array1.getString("dateCreated");
           PrintSlip.transactionStatus ="Transaction Successful";
           PrintSlip.phone=array1.getString("phoneNo");
           PrintSlip.name = array1.getString("costomer");



       }catch(Exception ed){}
       try {
           if(!array1.getString("tarminalId").equalsIgnoreCase("")){
           ISOMessage.terminalId=array1.getString("tarminalId");}
           if(!array1.getString("pan").equalsIgnoreCase("")){
           ISOMessage.pan=array1.getString("pan");}
       }catch(Exception ex){}
       double totalAmount=0.0;
        for(int i = 0; i<categories.length(); i++) {

          //  CartHelper cart =null;

            JSONObject json = null;
            try {



                json = categories.getJSONObject(i);
                System.out.println("json "+json);
                System.out.println("id "+json.getString("item_id"));
               // cart=new CartHelper("", MainActivity.UserId, getDataAdapter2.getItem_id(), getDataAdapter2.getItem_amount(), getDataAdapter2.getItem_name()) ;
              /*  cart=new CartHelper("", MainActivity.UserId, json.getString("item_id"), json.getString("item_amount"), json.getString("itemName")) ;
                totalAmount=totalAmount+Double.valueOf(json.getString("item_amount"));
               cartList.add(cart);
*/

            } catch (JSONException e) {

                e.printStackTrace();
            }
            amount=String.valueOf(totalAmount);
            amount = amount;

           // menuItem.add(GetDataAdapter2);
        }

        // recyclerView.setAdapter(recyclerViewadapter);

    }
    @Override
    public void onStart() {
        super.onStart();

        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
       if(!Q2) {
            if ((pref.getBlutoothAddress() != null && !"".equals(pref.getBlutoothAddress()))
                    || (pref.getBlutoothAddress() != null && !"".equals(pref.getBlutoothAddress()))) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    // Otherwise, setup the session
                } else {
                    if (mService == null)
                        mService = new BluetoothService1(this, mHandler);
                    //KeyListenerInit();//监听
                }
            }
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(!Q2) {
            if (mService != null) {
                if (mService.getState() == BluetoothService.STATE_NONE) {
                    // Start the Bluetooth services
                    mService.start();
                }
            }
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (DEBUG){}

    }

    @Override
    public void onStop() {
        super.onStop();
        if (DEBUG){}

    }


    @SuppressLint("SimpleDateFormat")
    public void PrintBalance() throws UnsupportedEncodingException {
        //String lang = getString(solutions.access.com.thebluetoothapp.R.string.strLang);
        if(("en".compareTo("en")) == 0){

            if (is58mm) {
                int nPaperWidth = 400;
                int nMode = 0;
                Bitmap logo = getImageFromAssetsFile("auto.png");
                int width = 400;
                byte[] logoByte = PrintPicture.POS_PrintBMP(logo, width, nMode);
                SendDataByte(logoByte);

                String title = "Lacvis Mobile\n";
                String bodyTop = "\nDATE:        TIME:  "
                        + "\n" + new SimpleDateFormat("dd-MM-yyyyHHmmss").format(new Date()) + "   " + new SimpleDateFormat("HH-mm-ss").format(new Date()) ; //+ kioskNo
                String pinPrint = "\n" + transactionStatus+"\n";
                String bodyEnd = "\nStatus: " + " SUCCESSFUL TRANSACTION "+"";

                Command.ESC_Align[2] = 0x01;
                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                Command.ESC_M[2] = 0x02;
                SendDataByte(Command.ESC_M);
                SendDataByte(PrinterCommand.POS_Print_Text(title, "UTF8", 0, 0, 0, 0));
                Command.ESC_Align[2] = 0x00;
                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataString(bodyTop);
                Command.GS_ExclamationMark[2] = 0x01;
                SendDataByte(Command.GS_ExclamationMark);
                Command.ESC_G[2] = 0x01;
                SendDataByte(Command.ESC_G);
                SendDataString(pinPrint);
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                Command.ESC_G[2] = 0x00;
                SendDataByte(Command.ESC_G);
                SendDataString( bodyEnd);
                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                //SendDataByte(form.getBytes("GBK"));
                SendDataByte("\n================================\n".getBytes("GBK"));

                SendDataByte("THANK YOU, PLEASE CALL AGAIN.!\n".getBytes("GBK"));
                Command.ESC_Align[2] = 0x01;
                SendDataByte(Command.ESC_Align);
                Command.GS_ExclamationMark[2] = 0x00;
                SendDataByte(Command.GS_ExclamationMark);
                SendDataByte("      Powered By Access Solutions,LTD\n".getBytes("GBK"));


                SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(20));
                SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(20));
                SendDataString("\n");
            }
        }
    }
    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    private void SendDataString(String data) {
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, "no connection", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (data.length() > 0) {
            mService.write(data.getBytes());
        }
    }
    public void removeItem(){
        //System.out.println("cartList "+cartList.size()+" partList "+partList.size());
        Iterator it=null;
       // ArrayList<CartHelper>number=new ArrayList<>();
        try {
            int count=0;
            /*Iterator it2=partList.iterator();
            System.out.println("main number "+cartList.size());
            int size=cartList.size();
            System.out.println("size "+size);

         for(CartHelper help:cartList) {

                System.out.println("cartList +++++" + help.getProductid() + " count " + count);
                try {
                    for (CartHelper li : partList) {

                        System.out.println("search " + help.getProductid());
                        if (help.getProductid() == (li.getProductid())) {
                            System.out.println("equal - first " + help.getProductid() + "- second " + li.getProductid());
                            number.add(help);
                            // cartList.remove(help);
                            System.out.println("removing ");


                        } else {
                            System.out.println(" not equal - first " + help.getProductid() + "- second " + li.getProductid());

                            // continue;
                        }
                    }
                } catch (Exception nm) {
                    System.out.println("error in delet "+nm.getMessage());
                }
                count++;
                System.out.println("count main " + count);

            }*/
        /*    try {
                for(CartHelper help:number) {

                    boolean del= cartList.remove(help);
                    System.out.println("delete "+del);
                }
            }catch(Exception ex){
                System.out.println("failed report "+ex.getMessage());
            }*/

        }catch(Exception ed){}
    }

    public void addItem(){
       /* System.out.println("cartList "+cartList.size()+" partList "+partList.size());

        try {
            for(CartHelper help:partList) {

                boolean del= cartList.add(help);
                System.out.println("added "+del);
            }
        }catch(Exception ex){
            System.out.println("failed report "+ex.getMessage());
        }

*/
    }


    public void completePaymentViaCard( String ref_code, String phone,String response_code,String response_message,String paid_amount){

        Map<String, String> param= new HashMap<>();

        System.out.println("phone "+phone+" paid_amount "+paid_amount);
        param.put("ref_code", ref_code);
        param.put("phone", phone);
        param.put("response_code", response_code);
        param.put("response_message", response_message);
        param.put("paid_amount", paid_amount);

        System.out.println("params "+new JSONObject(param));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                connect.getUrlOyo("completePaymentViaCard"), new JSONObject(param),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("response ll "+response);
                            System.out.println("response-- LOGIN "+response.getString("response_message"));
                            System.out.println("code  return"+response.getString("response_code"));

                            if(response.getString("response_code").equalsIgnoreCase("200")) {

                                System.out.println("after");

                                //System.out.println("class got "+ClassNameReponse.getClassName(AppConstant.SELECTED_SERVICE));


                            }else{
                               // showMessage(response.getString("response_message"));
                                // passResponseMessage("Failed",DESDESDES.decrypt(response.getString("response_message")));
                            }
                        }catch(Exception ex){}
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "Failed: Please Check your Internet Connectivity", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");

                return headers;
            }



        };



        queue.add(jsonObjReq);


    }
}
