package com.mpos.newthree.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mpos.newthree.R;
import com.mpos.newthree.dao.ISOBalanceInquiry;
import com.mpos.newthree.dao.ISOBillPayment;
import com.mpos.newthree.dao.ISOMessage;
import com.mpos.newthree.dao.ISOResponse;
import com.mpos.newthree.dao.ISOVTU;
import com.mpos.newthree.dao.Response;
import com.mpos.newthree.obj.Transaction;
import com.mpos.newthree.obj.TransactionUpdateDBHelper;
import com.mpos.newthree.utils.Utils;
import com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity;
import com.mpos.newthree.wizarpos.emvsample.constant.Constant;
import com.mpos.newthree.wizarpos.emvsample.parameter.ISOParams;
import com.mpos.newthree.wizarpos.emvsample.transaction.TransDefine;
import com.mpos.newthree.wizarpos.jni.CashDrawerInterface;
import com.mpos.newthree.wizarpos.jni.PinPadCallbackHandler;
import com.mpos.newthree.wizarpos.jni.PinPadInterface;
import com.mpos.newthree.wizarpos.jni.PrintSlip;
import com.mpos.newthree.wizarpos.util.AppUtil;
import com.mpos.newthree.wizarpos.util.ByteUtil;
import com.mpos.newthree.wizarpos.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.mpos.newthree.activity.ConstantFile.ref_code;
import static com.mpos.newthree.activity.MainActivity.card_check;
import static com.mpos.newthree.dao.ISOMessage.stan;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.appState;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_get_candidate_list;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_get_tag_data;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_get_tag_list_data;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_is_need_advice;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_is_need_signature;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_is_tag_present;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_process_next;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_kernel_type;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_online_pin_entered;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_online_result;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_other_amount;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_tag_data;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_trans_amount;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_trans_type;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_trans_initialize;

public class CardPayActivity extends AppCompatActivity implements Constant, PinPadCallbackHandler
{
    //wizar
    ConstantFile connect=new ConstantFile();
    public static String tId;
    private int defaultTagList[] = {
            0x57,
            0x5A,
            0x5F20,
            0x5F24,
            0x5F25,
            0x5F28,
            0x5F2A,
            0x5F34,
            0x82,
            0x84,
            0x8A,
            0x8E,
            0x95,
            0x9A,
            0x9B,
            0x9C,
            0x9F01,
            0x9F02,
            0x9F03,
            0x9F07,
            0x9F09,
            0x9F0D,
            0x9F0E,
            0x9F0F,
            0x9F10,
            0x9F15,
            0x9F16,
            0x9F1A,
            0x9F1C,
            0x9F1E,
            0x9F21,
            0x9F26,
            0x9F27,
            0x9F33,
            0x9F34,
            0x9F35,
            0x9F36,
            0x9F37,
            0x9F39,
            0x9F41,
            0x9F4C,
            0x9F5D,
            0x9F63,
            0x9F66,
            0x9F6C,
            0x9F74,
            0xDF31   };


    private int confirmTagList[] = {
            0x9F1C,
            0x9F27,  // Cryptogram Information Data
            0x95,    // Terminal Verification Results
            0x9B,    // TSI
            0x9F26,
            0x9F4C,
            0x9F74,
            0xDF31  // Issuer Script Results
    };

    ////////////////////////////
    private final int PINPAD_CANCEL  = -65792;
    private final int PINPAD_TIMEOUT = -65538;

    private Handler mHandler;
    private Thread mReadPINThread;
    private Handler mmHandler;


    private Thread mEMVThread = null;
    private Thread mEMVProcessNextThread = null;
    private int requestCode;
    private int resultCode;

    Double all_price=00.00;
    private LinearLayout llyt_Scroll_Demo;
    private ScrollView sclv_Demo;
    private FuncActivity func;

    private String zonePinKey;
    private String check;
    private final String zoneMasterKey =   "CF188277E109061121BBF6E766557830";
    public static String amt;
    public static boolean timeout;
    private String responseMsg;
    private ISOBalanceInquiry balanceEnquiry;
    public static String provider;
    public static String providerPhone;
    public static String cardNo;
    public static String accountType;
    public static String transType;
    public static String phone;
    private Transaction transaction;
    TransactionUpdateDBHelper dbHelper;
    //private int responseCode;
    private boolean etzCard;
    Context context;
    RequestQueue queue =null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_pay);
        setupActionBar();
       // transType ="BILL_PAY";
        queue = Volley.newRequestQueue(this);
        context=getApplicationContext();
        System.out.println("transType transType "+transType);
        getSupportActionBar().setTitle("Payment");
        dbHelper  = new TransactionUpdateDBHelper(this);
        transaction = new Transaction();
//Log.i("account type", PayOptActivity.accountType);
        llyt_Scroll_Demo = (LinearLayout) findViewById(R.id.llyt_scroll_);
       sclv_Demo = (ScrollView) findViewById(R.id.sclv_);

        //wizar
        resultCode = Activity.RESULT_OK;
        func = new FuncActivity(this);

        //get zone pin key
        SharedPreferences preferences= MainActivity.context.getSharedPreferences("temp", MainActivity.context.MODE_PRIVATE);
        zonePinKey=preferences.getString("zonePinKey",null);
        check=preferences.getString("checkDigit",null);
        System.out.println("zone card="+zonePinKey);




        func.mHandler = new Handler() {
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case CARD_INSERT_NOTIFIER:
                        PutMessage("<<< PROCESSING PLEASE WAIT");
                        Bundle bundle = msg.getData();
                        int nEventID = bundle.getInt("nEventID");
                        int nSlotIndex = bundle.getInt("nSlotIndex");
                        if (debug)
                            Log.d(APP_TAG, "get CONTACT_CARD_EVENT_NOTIFIER,event[" + nEventID + "]slot[" + nSlotIndex + "]");
                        if (nSlotIndex == 0
                                && nEventID == SMART_CARD_EVENT_INSERT_CARD
                                ) {
                            appState.resetCardError = false;
                            appState.trans.setCardEntryMode(INSERT_ENTRY);
                            appState.needCard = false;
                            main();
                        }
                        break;
                    case CARD_ERROR_NOTIFIER:
                        //cancelMSRThread();
                        appState.trans.setEmvCardError(true);
                        appState.resetCardError = true;
                        appState.needCard = true;
                        Toast.makeText(CardPayActivity.this, "CARD_ERROR_NOTIFIER...", Toast.LENGTH_SHORT).show();
                        //sale();
                        break;
                    case CARD_REMOVE_NOTIFIER:
                        break;
                }
            }
        };
Log.i("card=",String.valueOf(func.waitContactCard()));
        if (func.waitContactCard() == 0 ) {
            PutMessage("<<< PLEASE INSERT CARD");
        }else{

            PutMessage("<<< failed");
        }

        // Q1Test();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }



    private void openCashDrawer(){
        //open cash drawer
        if(openDrawer() >= 0){
            toastMessage("Drawer opened!");
            closeDrawer();
        }else{
            toastMessage("Drawer not opened");
        }
    }

    public  void PutMessage(String string)
    {

        TextView lb = new TextView(this);
        lb.setTextSize(16);
        lb.setText(string);

        if (llyt_Scroll_Demo.getChildCount() > 0)
            llyt_Scroll_Demo.addView(lb);
        else
            llyt_Scroll_Demo.addView(lb);
        if (llyt_Scroll_Demo.getChildCount() > 500)
        {
            llyt_Scroll_Demo.removeViewAt(llyt_Scroll_Demo.getChildCount() - 1);
        }
        llyt_Scroll_Demo.invalidate();

        sclv_Demo.post(new Runnable()
        {
            @Override
            public void run()
            {
                sclv_Demo.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    //////wizar//////////
    private void inputAmount(){
        requestCode = STATE_INPUT_AMOUNT;
        resultCode = Activity.RESULT_OK;
        System.out.println("inputAmount....");
        generic();
    }

    private void generic()
    {
        Log.i("resultCode generic",String.valueOf(resultCode));
        //if(debug)Log.d(APP_TAG, "ProcessEMVCard onActivityResult, requesCode=" + requestCode + ",resultCode=" + resultCode);
        if(appState.getProcessState() != PROCESS_NORMAL)
        {
            if(   appState.getProcessState() == PROCESS_REVERSAL
                    && appState.trans.getEMVRetCode() == APPROVE_OFFLINE
                    )
            {
                appState.setProcessState(PROCESS_CONFIMATION);
                getEMVCardInfo();
                requestCode = STATE_PROCESS_ONLINE;
                processResult();
                //generic();
            }
            else if(   emv_is_need_advice() == 1
                    && appState.getProcessState() != PROCESS_ADVICE_ONLINE
                    )
            {
                appState.setProcessState(PROCESS_ADVICE_ONLINE);
                getEMVCardInfo();
                requestCode = STATE_PROCESS_ONLINE;
                processResult();
                //generic();
            }
            else{
                appState.setProcessState(PROCESS_NORMAL);
                //setResult(Activity.RESULT_OK, getIntent());
                //exit();
            }
            return;
        }
        else{
            if(appState.getErrorCode() > 0)
            {
                if( requestCode == STATE_PROCESS_ONLINE )
                {
                    appState.trans.setEMVOnlineResult(ONLINE_FAIL);
                    emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), new byte[]{' '}, 0);
                    mEMVProcessNextThread = new CardPayActivity.EMVProcessNextThread();
                    mEMVProcessNextThread.start();
                    return;
                }
                //exit();
                return;
            }
            if(resultCode != Activity.RESULT_OK)
            {

                if( requestCode == STATE_PROCESS_ONLINE )
                {
                    appState.trans.setEMVOnlineResult(ONLINE_FAIL);
                    emv_set_online_result(ONLINE_FAIL, appState.trans.getResponseCode(), new byte[]{' '}, 0);
                    mEMVProcessNextThread = new CardPayActivity.EMVProcessNextThread();
                    mEMVProcessNextThread.start();
                    return;
                }
                //setResult(resultCode, getIntent());
                //exit();
                return;
            }
            if(requestCode == STATE_INPUT_AMOUNT)
            {
                func.setEMVTransAmount(String.valueOf(appState.trans.getTransAmount()));
                emv_set_other_amount(new byte[]{'0', 0x00});
            }
            else if(requestCode == STATE_INPUT_PIN)
            {
                if(appState.trans.getPinEntryMode() == CAN_PIN)
                {
                    emv_set_online_pin_entered(1);
                }
                else{
                    emv_set_online_pin_entered(0);
                }
            }
        }
        mEMVProcessNextThread = new CardPayActivity.EMVProcessNextThread();
        mEMVProcessNextThread.start();
    }

    private void getEMVCardInfo()
    {
        byte[] tagData = new byte[100];
        int tagDataLength = 0;

        byte[] iccData = new byte[1200];
        int offset = 0;
        if(appState.getProcessState() == PROCESS_CONFIMATION)
        {
            offset = emv_get_tag_list_data(confirmTagList, confirmTagList.length, iccData, iccData.length);
        }
        else{
            offset = emv_get_tag_list_data(defaultTagList, defaultTagList.length, iccData, iccData.length);
        }
        appState.trans.setICCData(iccData, 0, offset);

        int id5f34 = appState.trans.getICCData().indexOf("5F3401");
        ISOParams.setSeqNos(appState.trans.getICCData().substring(id5f34+6, id5f34+8));

        // Application Label 50
        if( emv_is_tag_present(0x50) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x50, tagData, tagData.length);
            byte[] appLabel = new byte[tagDataLength];
            System.arraycopy(tagData, 0, appLabel, 0, appLabel.length);
            appState.trans.setAppLabel(StringUtil.toString(appLabel));
        }

        // AIP
        if( emv_is_tag_present(0x82) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x82, tagData, tagData.length);
            appState.trans.setAIP(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        // TVR
        if( emv_is_tag_present(0x95) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x95, tagData, tagData.length);
            appState.trans.setTVR(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        // TSI
        if( emv_is_tag_present(0x9B) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9B, tagData, tagData.length);
            appState.trans.setTSI(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        // Application Identifier terminal
        if( emv_is_tag_present(0x9F06) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F06, tagData, tagData.length);
            appState.trans.setAID(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        // IAD
        if( emv_is_tag_present(0x9F10) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F10, tagData, tagData.length);
            appState.trans.setIAD(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        // ApplicationPreferredName  9F12
        if( emv_is_tag_present(0x9F12) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F12, tagData, tagData.length);
            byte[] appName = new byte[tagDataLength];
            System.arraycopy(tagData, 0, appName, 0, appName.length);
            appState.trans.setAppName(StringUtil.toString(appName));
        }

        if(emv_is_tag_present(0x9F26) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F26, tagData, tagData.length);
            appState.trans.setAC(StringUtil.toHexString(tagData, 0, tagDataLength, false));
            System.out.println("App Crypto..1." + appState.trans.getAC());
        }

        if(emv_is_tag_present(0x9F27) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F27, tagData, tagData.length);
            appState.trans.setCryptCode(StringUtil.toHexString(tagData, 0, tagDataLength, false));
            System.out.println("App Crypto..2." + StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9F36) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F36, tagData, tagData.length);
            appState.trans.settCounter(StringUtil.toHexString(tagData, 0, tagDataLength, false));
            System.out.println("App Crypto..3." + StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9F34) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F34, tagData, tagData.length);
            appState.trans.setCVM(StringUtil.toHexString(tagData, 0, tagDataLength, false));
            System.out.println("App Crypto..4." + StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9F37) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F37, tagData, tagData.length);
            appState.trans.setUnpredictableNumber(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(   emv_is_tag_present(0x9F79) >= 0
                && appState.trans.getECBalance() < 0
                )
        {
            tagDataLength = emv_get_tag_data(0x9F79, tagData, tagData.length);
            byte[] amt = new byte[tagDataLength];
            System.arraycopy(tagData, 0, amt, 0, amt.length);
            appState.trans.setECBalance(ByteUtil.bcdToInt(amt));
        }


        //////////////////////////////////////
        if(emv_is_tag_present(0x9A) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9A, tagData, tagData.length);
            appState.trans.setTransDate(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9C) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9C, tagData, tagData.length);
            appState.trans.setTransTypeStr(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9F02) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F02, tagData, tagData.length);
            appState.trans.setTransAmountStr(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9F03) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F03, tagData, tagData.length);
            appState.trans.setOtherAmountStr(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9F1A) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F1A, tagData, tagData.length);
            appState.trans.setCountryCode(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9F27) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F27, tagData, tagData.length);
            appState.trans.setCryptCode(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9F36) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F36, tagData, tagData.length);
            appState.trans.settCounter(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9F35) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F35, tagData, tagData.length);
            appState.trans.setTerminalType(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9F33) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F33, tagData, tagData.length);
            appState.trans.settCapability(StringUtil.toHexString(tagData, 0, tagDataLength, false));
        }

        if(emv_is_tag_present(0x9F1C) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x9F1C, tagData, tagData.length);
            appState.trans.setTid(StringUtil.toHexString(tagData, 0, tagDataLength, false));
            System.out.println("TID......."+appState.trans.getTid());
        }

        if(emv_is_tag_present(0x5F2A) >= 0)
        {
            tagDataLength = emv_get_tag_data(0x5F2A, tagData, tagData.length);
            appState.trans.setCurrCode(StringUtil.toHexString(tagData, 0, tagDataLength, false));
            System.out.println("Currency......."+appState.trans.getCurrCode());
        }

    }

    public static void setEMVData()
    {
        if(appState.getTranType() == QUERY_CARD_RECORD)
        {
            emv_set_trans_amount(new byte[]{'0', 0x00});
            emv_set_other_amount(new byte[]{'0', 0x00});
            if(appState.recordType == 0x00)
            {
                emv_set_trans_type(EMV_TRANS_CARD_RECORD);
            }
            else
            {
                emv_set_trans_type(EMV_TRANS_LOAD_RECORD);
            }
        }
        else{
            System.out.println("appState.trans.getTransDate()..."+appState.trans.getTransDate());
            emv_set_tag_data(0x9A,   StringUtil.hexString2bytes(appState.trans.getTransDate().substring(2)), 3);
            emv_set_tag_data(0x9F21, StringUtil.hexString2bytes(appState.trans.getTransTime()), 3);
            emv_set_tag_data(0x9F41, StringUtil.hexString2bytes(StringUtil.fillZero(Integer.toString(appState.trans.getTrace()), 8)), 4);

            emv_set_trans_type(EMV_TRANS_GOODS_SERVICE);
        }
    }

    private void timeInit(){

        //initialize trans data
        //appState.setTranType(TRAN_GOODS);
        //appState.trans.setTransType(TRAN_GOODS);
        appState.getCurrentDateTime();
        appState.trans.setTransDate(   appState.currentYear
                + StringUtil.fillZero(Integer.toString(appState.currentMonth), 2)
                + StringUtil.fillZero(Integer.toString(appState.currentDay), 2)
        );
        appState.trans.setTransTime(   StringUtil.fillZero(Integer.toString(appState.currentHour), 2)
                + StringUtil.fillZero(Integer.toString(appState.currentMinute), 2)
                + StringUtil.fillZero(Integer.toString(appState.currentSecond), 2)
        );
    }

    class EMVThread extends Thread
    {
        public void run()
        {
            super.run();
            emv_trans_initialize();
            emv_set_kernel_type(appState.trans.getEMVKernelType());
            func.setEMVTransAmount(String.valueOf(appState.trans.getTransAmount()));
            timeInit();
            setEMVData();

            //pre-process
            if(appState.trans.getEMVKernelType() == QPBOC_KERNAL && !func.preProcessQpboc())
            {
                Message msg = new Message();
                msg.what = PREPROCESS_ERROR_NOTIFIER;
                func.mHandler.sendMessage(msg);
                return;
            }
            emv_process_next();
        }
    }

    class EMVProcessNextThread extends Thread
    {
        public void run()
        {
            super.run();
            emv_process_next();
        }
    }

    public ISOResponse doVTU() {

        String decryptTrack2 = "";
        //gettheField();
        if(appState.trans.getTrack2Data().contains("D")){
            decryptTrack2 = appState.trans.getTrack2Data().replace('D', '=');
        }
        ISOParams.setPans(appState.trans.getPAN());
        ISOParams.setExpiryDate(appState.trans.getExpiry());
        //ISOParams.setSeqNos(appState.trans.getS);
      //  Log.i("airtimeValue=", airtimeValue+" phone"+phone+ "PayOptActivity.accountType="+ PayOptActivity.accountType);
        ISOVTU doVTU = new ISOVTU(this);
      //  String fld48 = airtimeValue + "~" + "PREPAID" +"~"+ phone;
        System.out.println("pin block"+ ISOParams.getPinBlock());
        Log.i(" after pin block","hello");
        String icc = appState.trans.getICCData();
Log.i("diff ", icc );
        int id5F2A = icc.indexOf("5F2A02");
        String TransCurrencyCode = icc.substring(id5F2A+6, id5F2A+9);
        icc = icc.replace(TransCurrencyCode, "5F2A020566");
        //System.out.println("ICC DATA..."+icc);
        int id9F1C = icc.indexOf("9F1C08");
        String terminalID = icc.substring(id9F1C+6, id9F1C+21);
        icc = icc.replace(terminalID, "9F1C04"+appState.trans.getTid());
        System.out.println("diff  icc "+icc +" getfield "+gettheField());
        String amount2 = amt.split("\\.")[0];
        ISOResponse response = null;
                //doVTU.doTransaction(decryptTrack2, ISOParams.getPinBlock(), fld48, gettheField() , String.valueOf(Integer.parseInt(amount2) *100), "0", accountType);
        return response;

    }

    public ISOResponse doBalance() {
       // Log.i("main getPinBlock",gettheField());
        System.out.println("pin block balance"+ ISOParams.getPinBlock());
        ISOResponse resp=null;
       // Log.w("DoBalance","Balance Enq Called "+PayOptActivity.accountType);
        ISOBalanceInquiry checkBalance = new ISOBalanceInquiry();
        String decryptTrack2 = "";
        if(appState.trans.getTrack2Data().contains("D")){
            decryptTrack2 = appState.trans.getTrack2Data().replace('D', '=');
        }

        System.out.println("decryptTrack2="+decryptTrack2);
        ISOParams.setPans(appState.trans.getPAN());
        ISOParams.setExpiryDate(appState.trans.getExpiry());
        //ISOParams.setSeqNos(appState.trans.getS);

        int id9C = appState.trans.getICCData().indexOf("9C01");
        String tRansType = "9C01"+appState.trans.getICCData().substring(id9C+4, id9C+6);
        String icc = appState.trans.getICCData().replace(tRansType, "9C0131");
        System.out.println("befroe resp------------- pin "+ ISOParams.getPinBlock());
        System.out.println("decryptTrack2 ="+decryptTrack2);
        resp=checkBalance.doTransaction(decryptTrack2, ISOParams.getPinBlock(), "",gettheField() , "", "",  accountType);
        System.out.println(" after resp---------------------"+resp);
        return resp;
    }

    public ISOResponse doBill() {
        ISOResponse result=null;
        String decryptTrack2 = "";
        if(appState.trans.getTrack2Data().contains("D")){
            decryptTrack2 = appState.trans.getTrack2Data().replace('D', '=');
        }
        ISOParams.setPans(appState.trans.getPAN());
        ISOParams.setExpiryDate(appState.trans.getExpiry());
        //ISOParams.setSeqNos(appState.trans.getS);

        ISOBillPayment bill = new ISOBillPayment();
        System.out.println("acct------- "+accountType);
       /// System.out.println("PayOptActivity.amount "+PayOptActivity.amount);
        int amountvalue=0;
        try {
            Double amountDouble=0.0;
                    //Double.parseDouble(PayOptActivity.amount);
            System.out.println("amountDouble "+amountDouble);
            amountvalue=  amountDouble.intValue();
        }catch(Exception ex){
            System.out.println("error in convertion "+ex.getMessage());
        }
        System.out.println("amountvalue "+amountvalue);
      try {
          beginPaymentViaCard( ConstantFile.driver_id, ConstantFile.userPhone, stan,appState.trans.getPAN());
      }catch(Exception ex){}
        //appState.trans.getICCData()
       // String amount2 = PayOptActivity.amount.split("\\.")[0];
        //System.out.println("after split "+amount2);
        System.out.println("phone+-+-+ "+phone);
       // gettheField();
        //System.out.println("new amount-- "+PayOptActivity.amount);
       // double conFee = (0.75 * Double.parseDouble(amount2)) / 100;
        double conFee = 0.0;
        if(conFee > 100 && !etzCard){
            return null;
        }

        //String fld48 = "ONEPAYMPOS"+"~"+ phone;
        String fld48 = "AUTOPOINTE"+"~"+"SPAREPARTS"+"700602X2KA";
        //amount2 = "10";
        if(ref_code!=null ||!ref_code.equalsIgnoreCase("")) {
            System.out.println("in process");
            result= bill.doTransaction(decryptTrack2, ISOParams.getPinBlock(), fld48, getICC(), String.valueOf(amountvalue * 100), "0", accountType);
        }else{
            System.out.println("else "+ref_code);
            result=null;
        }
        return result;
    }

    ////////////PROCESS ONLINE///////////////////////////////
    private void processResult()
    {
        if(debug)Log.d(APP_TAG, "processResult");
        appState.trans.setTrace(appState.terminalConfig.getTrace());
        appState.trans.setResponseCode(new byte[]{'0','0'});
        appState.terminalConfig.incTrace();

        switch(appState.getProcessState())
        {
            case PROCESS_NORMAL:
                if (    appState.trans.getResponseCode() != null
                        &&	appState.trans.getResponseCode()[0] == '0'
                        && appState.trans.getResponseCode()[1] == '0'
                        )
                {
                    if(appState.trans.getEMVOnlineFlag() == true)
                    {
                        appState.trans.setEMVOnlineResult(ONLINE_SUCCESS);
                        byte[] issuerData = appState.trans.getIssuerAuthData();
                        if(issuerData != null && issuerData.length > 0)
                        {
                            emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), issuerData, issuerData.length);
                        }
                        else
                        {
                            emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), new byte[]{' '}, 0);
                        }
                    }
                    break;
                }
                else if(   appState.trans.getResponseCode() != null
                        && appState.trans.getResponseCode()[0] == 'F'
                        && appState.trans.getResponseCode()[1] == 'F'
                        )
                {
                    appState.trans.setEMVOnlineResult(ONLINE_FAIL);
                    emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), new byte[]{' '}, 0);
                }
                else{
                    appState.trans.setEMVOnlineResult(ONLINE_DENIAL);
                    byte[] issuerData = appState.trans.getIssuerAuthData();
                    if(issuerData != null && issuerData.length > 0)
                    {
                        emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), issuerData, issuerData.length);
                    }
                    else
                    {
                        emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), new byte[]{' '}, 0);
                    }
                }
                break;
            case PROCESS_ADVICE_OFFLINE:
                break;
        }
        if(debug)Log.d(APP_TAG, "ProcessOnlineActivity finish success");
        //setResult(Activity.RESULT_OK, getIntent());
        //exit();
    }

    public String getICC(){

        SimpleDateFormat dtf = new SimpleDateFormat("yyyy/MM/dd");
        Date localDate = new Date();
        String newDate = (dtf.format(localDate)).replace("/", "");
        int  id = appState.trans.getICCData().indexOf("8407");
        String AID = appState.trans.getICCData().substring(id + 4, id+18);
        // System.out.print("\nAID is " + AID + "\n");
        int id82 = appState.trans.getICCData().indexOf("8202");
        String AIP82 = appState.trans.getICCData().substring(id82+4, id82+8);
        int id5f34 = appState.trans.getICCData().indexOf("5F3401");
        String PANSN = appState.trans.getICCData().substring(id5f34+6, id5f34+8);
        int idDate = appState.trans.getICCData().indexOf("9A03");
        String tRansDate = newDate.substring(2, newDate.length());
        int id9C = appState.trans.getICCData().indexOf("9C01");
        String tRansType = appState.trans.getICCData().substring(id9C+4, id9C+6);
        int id5F2A = appState.trans.getICCData().indexOf("5F2A02");
        String countryCode = "0566";
        int id9F02 = appState.trans.getICCData().indexOf("9F0206");
        String aMount = appState.trans.getICCData().substring(id9F02+6, id9F02+18);
        // String aMount = paddedAmount;
        int id9F03 = appState.trans.getICCData().indexOf("9F0306");
        String otheramount = appState.trans.getICCData().substring(id9F03+6, id9F03+18);

        int id9F1A = appState.trans.getICCData().indexOf("9F1A02");
        String terminalCountryCode = "0566";
        int id9F27 = appState.trans.getICCData().indexOf("9F2701");
        String cryptCode = appState.trans.getICCData().substring(id9F27+6, id9F27+8);
        int id9F36 = appState.trans.getICCData().indexOf("9F3602");
        String tCounter = appState.trans.getICCData().substring(id9F36+6, id9F36+10);
        int id9F26 = appState.trans.getICCData().indexOf("9F2608");
        String aCryptogram = appState.trans.getICCData().substring(id9F26+6, id9F26+22);
        int id9F10 = appState.trans.getICCData().indexOf("9F10");
        System.out.println("id9F10========"+id9F10);

        String issuerLength = appState.trans.getICCData().substring(id9F10+4, id9F10+6);
        int Length = Integer.parseInt(issuerLength);
        System.out.print("Issuer Length is-> "+Length);

        id9F10 = appState.trans.getICCData().indexOf("9F10"+Length);
        String issuerAD = appState.trans.getICCData().substring(id9F10+6, id9F10+42);

        if(Length==20)//Verve Card Issuer ID has a length of 20
        {
            id9F10 = appState.trans.getICCData().indexOf("9F10"+Length);
            issuerAD = appState.trans.getICCData().substring(id9F10+6, id9F10+70);
        }


        int id9F35  = appState.trans.getICCData().indexOf("9F3501");
        String tErminaltype = appState.trans.getICCData().substring(id9F35+6, id9F35+8);
        //String tErminaltype = "22";
        int id9F37 = appState.trans.getICCData().indexOf("9F3704");
        String NumberP = appState.trans.getICCData().substring(id9F37+6, id9F37+14);
        //String NumberP = rand;

        int id95 = appState.trans.getICCData().indexOf("9505");
        String tVR = appState.trans.getICCData().substring(id95+4, id95+14);
        //String tVR = "0880048000";
        int id9F34 = appState.trans.getICCData().indexOf("9F3403");
        String cVM = appState.trans.getICCData().substring(id9F34+6, id9F34+12);
        //String cVM = "020300";
        int id9F33 = appState.trans.getICCData().indexOf("9F3303");

        String tCapability = appState.trans.getICCData().substring(id9F33+6, id9F33+12);
        // String tCapability = "E0F8C8";


        //String output = "4F07"+AID+"8202"+AIP82+"5F3401"+PANSN+"9A03"+tRansDate+"9C01"+tRansType+"5F2A02"+"0566"+"9F0206"+aMount+"9F0306"+otheramount+"9F1A02"+terminalCountryCode+"9F2701"+cryptCode+"9F3602"+tCounter+"9F2608"+aCryptogram+"9F1012"+issuerAD+"9F3501"+tErminaltype+"9F3704"+NumberP+"9F3303"+tCapability+"9505"+tVR+"9F3403"+cVM;

        /*String track2 = DecryptedTrack2.getTrack2Decrypt();
        int dIndex = track2.indexOf('=');
        String pan = track2.substring(0, dIndex);*/
        //get the pan and use it to identify Eransact Card
        String word = (String) appState.trans.getPAN().subSequence(0, 6);
        String text = card_check;
        Boolean found;
        found = text.contains(word);

        //if the card is an etransact card, assign a new alias
        String output = "";
        if(found.equals(true))
        {
            //Etransact Cards
            output = "4F07"+AID+"8202"+AIP82+"5F3401"+PANSN+"9A03"+tRansDate+"9C01"+tRansType+"5F2A02"+"0566"+"9F0206"+aMount+"9F0306"+otheramount+"9F1A02"+terminalCountryCode+"5F2A02"+terminalCountryCode+ "9F3501"+tErminaltype+"9F3704"+NumberP+"9F3303"+tCapability+"9505"+tVR+"9F3403"+cVM;

        } else
        {
            //Other Cards
            output = "4F07"+AID+"8202"+AIP82+"5F3401"+PANSN+"9A03"+tRansDate+"9C01"+tRansType+"5F2A02"+"0566"+"9F0206"+aMount+"9F0306"+otheramount+"9F1A02"+terminalCountryCode+"5F2A02"+terminalCountryCode+  "9F2701"+cryptCode+"9F3602"+tCounter+"9F2608"+aCryptogram+"9F10"+Length+issuerAD+"9F3501"+tErminaltype+"9F3704"+NumberP+"9F3303"+tCapability+"9505"+tVR+"9F3403"+cVM;

        }
        System.out.println("output "+output);
        return output;
    }

    public String gettheField(){
        String amountField = "0";
        //String paddedAmount = Utils.leftPad(amountField, '0', (short) 12);

        //Setting Date

        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY/MM/dd");
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy/MM/dd");
        //LocalDate localDate = LocalDate.now();
        Date localDate = new Date();
        String newDate = (dtf.format(localDate)).replace("/", "");
        // System.out.println(dtf.format(localDate)); //2016/11/16


        int  id = appState.trans.getICCData().indexOf("8407");
        // System.out.print("\nID is " + id + "\n");
        String AID = appState.trans.getICCData().substring(id + 4, id+18);
        // System.out.print("\nAID is " + AID + "\n");
        int id82 = appState.trans.getICCData().indexOf("8202");
        String AIP82 = appState.trans.getICCData().substring(id82+4, id82+8);
        //String AIP82 = "3800";

        int id5f34 = appState.trans.getICCData().indexOf("5F3401");
        String PANSN = appState.trans.getICCData().substring(id5f34+6, id5f34+8);
        int idDate = appState.trans.getICCData().indexOf("9A03");
        //String tRansDate = appState.trans.getTrack2Data().substring(idDate+4, idDate+10);
        //String tRansDate = newDate;
        String tRansDate = newDate.substring(2, newDate.length());
        // System.out.print("New Date is =>"+ tRansDate);
        int id9C = appState.trans.getICCData().indexOf("9C01");

        String tRansType = appState.trans.getICCData().substring(id9C+4, id9C+6);
        //String tRansType = "00";

        int id5F2A = appState.trans.getICCData().indexOf("5F2A02");
        String countryCode = "0566";
        int id9F02 = appState.trans.getICCData().indexOf("9F0206");

        String aMount = appState.trans.getICCData().substring(id9F02+6, id9F02+18);
        // String aMount = paddedAmount;
        int id9F03 = appState.trans.getICCData().indexOf("9F0306");
        String otheramount = appState.trans.getICCData().substring(id9F03+6, id9F03+18);

        int id9F1A = appState.trans.getICCData().indexOf("9F1A02");
        String terminalCountryCode = "0566";
        int id9F27 = appState.trans.getICCData().indexOf("9F2701");
        String cryptCode = appState.trans.getICCData().substring(id9F27+6, id9F27+8);
        int id9F36 = appState.trans.getICCData().indexOf("9F3602");
        String tCounter = appState.trans.getICCData().substring(id9F36+6, id9F36+10);
        int id9F26 = appState.trans.getICCData().indexOf("9F2608");
        String aCryptogram = appState.trans.getICCData().substring(id9F26+6, id9F26+22);
        int id9F10 = appState.trans.getICCData().indexOf("9F10");

        String issuerLength = appState.trans.getICCData().substring(id9F10+4, id9F10+6);
        int Length = Integer.parseInt(issuerLength);
        System.out.print("Issuer Length is-> "+Length);

        id9F10 = appState.trans.getICCData().indexOf("9F10"+Length);
        String issuerAD = appState.trans.getICCData().substring(id9F10+6, id9F10+42);

        System.out.println("issuer ID..."+issuerAD+"...issuerLength..."+issuerLength+ "...length..."+appState.trans.getICCData());
        if(Length==20)//Verve Card Issuer ID has a length of 20
        {
            id9F10 = appState.trans.getICCData().indexOf("9F10"+Length);
            issuerAD = appState.trans.getICCData().substring(id9F10+6, id9F10+70);
            //appState.trans.getIAD()
        }


        int id9F35  = appState.trans.getICCData().indexOf("9F3501");
        String tErminaltype = appState.trans.getICCData().substring(id9F35+6, id9F35+8);
        //String tErminaltype = "22";
        int id9F37 = appState.trans.getICCData().indexOf("9F3704");
        String NumberP = appState.trans.getICCData().substring(id9F37+6, id9F37+14);
        //String NumberP = rand;

        int id95 = appState.trans.getICCData().indexOf("9505");
        String tVR = appState.trans.getICCData().substring(id95+4, id95+14);
        //String tVR = "0880048000";
        int id9F34 =appState.trans.getICCData().indexOf("9F3403");
        String cVM = appState.trans.getICCData().substring(id9F34+6, id9F34+12);
        //String cVM = "020300";
        int id9F33 = appState.trans.getICCData().indexOf("9F3303");

        String tCapability = appState.trans.getICCData().substring(id9F33+6, id9F33+12);
        // String tCapability = "E0F8C8";


        //String output = "4F07"+AID+"8202"+AIP82+"5F3401"+PANSN+"9A03"+tRansDate+"9C01"+tRansType+"5F2A02"+"0566"+"9F0206"+aMount+"9F0306"+otheramount+"9F1A02"+terminalCountryCode+"9F2701"+cryptCode+"9F3602"+tCounter+"9F2608"+aCryptogram+"9F1012"+issuerAD+"9F3501"+tErminaltype+"9F3704"+NumberP+"9F3303"+tCapability+"9505"+tVR+"9F3403"+cVM;

        //String track2 = DecryptedTrack2.getTrack2Decrypt();
        //int dIndex = track2.indexOf('=');
        //String pan = track2.substring(0, dIndex);
        //get the pan and use it to identify Eransact Card
        /*String word = (String) appState.trans.getTrack2Data().subSequence(0, 6);
        String text = card_check;
        Boolean found;
        found = text.contains(word);*/

        String decryptTrack2 = "";
        if(appState.trans.getTrack2Data().contains("D")){
            decryptTrack2 = appState.trans.getTrack2Data().replace('D', '=');
        }else{
            decryptTrack2 = appState.trans.getTrack2Data();
        }

        String track2 = decryptTrack2;
        int dIndex = track2.indexOf('=');
        String pan = appState.trans.getPAN();
        ISOMessage.pan=pan;
       // cardNo=pan;
        System.out.println("hhhhhhhh----"+pan);
        //get the pan and use it to identify Eransact Card
        String word = (String) pan.subSequence(0, 6);
        String text = card_check;
        Boolean found;
        found = text.contains(word);


        //if the card is an etransact card, assign a new alias
        String output = "";
        if(found.equals(true))
        {
            //Etransact Cards
            output = "4F07"+AID+"8202"+AIP82+"5F3401"+PANSN+"9A03"+tRansDate+"9C01"+tRansType+"5F2A02"+"0566"+"9F0206"+aMount+"9F0306"+otheramount+"9F1A02"+terminalCountryCode+"5F2A02"+terminalCountryCode+ "9F3501"+tErminaltype+"9F3704"+NumberP+"9F3303"+tCapability+"9505"+tVR+"9F3403"+cVM;
            etzCard = true;
        } else
        {
            //Other Cards
            output = "4F07"+AID+"8202"+AIP82+"5F3401"+PANSN+"9A03"+tRansDate+"9C01"+tRansType+"5F2A02"+"0566"+"9F0206"+aMount+"9F0306"+otheramount+"9F1A02"+terminalCountryCode+"5F2A02"+terminalCountryCode+  "9F2701"+cryptCode+"9F3602"+tCounter+"9F2608"+aCryptogram+"9F10"+Length+issuerAD+"9F3501"+tErminaltype+"9F3704"+NumberP+"9F3303"+tCapability+"9505"+tVR+"9F3403"+cVM;
            etzCard = false;
        }
        String icc125 = "";
        return output;
    }

    private void main(){

        //get zone pin key
        SharedPreferences preferences= MainActivity.context.getSharedPreferences("temp", MainActivity.context.MODE_PRIVATE);
        zonePinKey=preferences.getString("zonePinKey",null);
        check=preferences.getString("checkDigit",null);
        System.out.println("done=="+check);
        func.mHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case EMV_PROCESS_NEXT_COMPLETED_NOTIFIER:
                        if(debug)Log.d(APP_TAG, "EMV_PROCESS_NEXT_COMPLETED_NOTIFIER, emvStatus = " + appState.trans.getEMVStatus() + ", emvRetCode = " + appState.trans.getEMVRetCode());
                        byte[] tagData;
                        int tagDataLength = 0;
                        switch (appState.trans.getEMVStatus())
                        {
                            case STATUS_CONTINUE:
                                switch (appState.trans.getEMVRetCode())
                                {
                                    case EMV_CANDIDATE_LIST:
                                        appState.aidNumber = emv_get_candidate_list(appState.aidList, appState.aidList.length);
                                        //selectEMVAppList();
                                        break;
                                    case EMV_APP_SELECTED:
                                        if( appState.getTranType() == QUERY_CARD_RECORD || appState.trans.getTransAmount() > 0)
                                        {
                                            mEMVProcessNextThread = new CardPayActivity.EMVProcessNextThread();
                                            mEMVProcessNextThread.start();
                                        }
                                        else{
                                            inputAmount();
                                        }
                                        break;
                                    case EMV_READ_APP_DATA:
                                        if(emv_is_tag_present(0x9F79) >= 0 )
                                        {
                                            tagData = new byte[6];
                                            emv_get_tag_data(0x9F79, tagData, 6);
                                            appState.trans.setECBalance(ByteUtil.bcdToInt(tagData));
                                        }

                                        tagData = new byte[100];
                                        if(emv_is_tag_present(0x5A) >= 0)
                                        {
                                            tagDataLength = emv_get_tag_data(0x5A, tagData, tagData.length);
                                            appState.trans.setPAN(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
                                            ISOParams.setPans(appState.trans.getPAN());
                                        }
                                        // Track2
                                        if( emv_is_tag_present(0x57) >= 0)
                                        {
                                            tagDataLength = emv_get_tag_data(0x57, tagData, tagData.length);
                                            //appState.trans.setPAN(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
                                            appState.trans.setTrack2Data(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
                                        }
                                        // CSN
                                        if( emv_is_tag_present(0x5F34) >= 0)
                                        {
                                            tagDataLength = emv_get_tag_data(0x5F34, tagData, tagData.length);
                                            appState.trans.setCSN(tagData[0]);
                                        }
                                        // Expiry
                                        if( emv_is_tag_present(0x5F24) >= 0)
                                        {
                                            tagDataLength = emv_get_tag_data(0x5F24, tagData, tagData.length);
                                            appState.trans.setExpiry(StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
                                            ISOParams.setExpiryDate(appState.trans.getExpiry());
                                        }
                                        //confirmCard();
                                        mEMVProcessNextThread = new CardPayActivity.EMVProcessNextThread();
                                        mEMVProcessNextThread.start();

                                        break;
                                    case EMV_DATA_AUTH:
                                        byte[] TSI = new byte[2];
                                        byte[] TVR = new byte[5];
                                        emv_get_tag_data(0x9B, TSI, 2); // TSI
                                        emv_get_tag_data(0x95, TVR, 5); // TVR
                                        if(   (TSI[0] & (byte)0x80) == (byte)0x80
                                                && (TVR[0] & (byte)0x40) == (byte)0x00
                                                && (TVR[0] & (byte)0x08) == (byte)0x00
                                                && (TVR[0] & (byte)0x04) == (byte)0x00
                                                )
                                        {
                                            appState.promptOfflineDataAuthSucc = true;
                                        }
                                        mEMVProcessNextThread = new CardPayActivity.EMVProcessNextThread();
                                        mEMVProcessNextThread.start();
                                        break;
                                    case EMV_OFFLINE_PIN:
                                        System.out.println("mEmailSignInButton.setOnClickListener...1");
                                        mEMVProcessNextThread = new CardPayActivity.EMVProcessNextThread();
                                        mEMVProcessNextThread.start();
                                        break;
                                    case EMV_ONLINE_ENC_PIN:
                                        System.out.println("mEmailSignInButton.setOnClickListener...");
                                        inputPinActivity();
                                        break;
                                    case EMV_PIN_BYPASS_CONFIRM:
                                        //confirmBypassPin();
                                        break;
                                    case EMV_PROCESS_ONLINE:
                                        getEMVCardInfo();
                                        appState.trans.setEMVOnlineFlag(true);
                                        requestCode = STATE_PROCESS_ONLINE;
                                        processResult();
                                        generic();
                                        break;
                                    default:
                                        mEMVProcessNextThread = new CardPayActivity.EMVProcessNextThread();
                                        mEMVProcessNextThread.start();
                                        break;
                                }
                                break;
                            case STATUS_COMPLETION:
                                System.out.println("SYSTEM ONLINE 15...in");
                                appState.terminalConfig.incTrace();
                                appState.trans.setNeedSignature(emv_is_need_signature());

                                tagData = new byte[50];
                                if( emv_is_tag_present(0x95) >= 0)
                                {
                                    tagDataLength = emv_get_tag_data(0x95, tagData, tagData.length);
                                    appState.terminalConfig.setLastTVR(StringUtil.toHexString(tagData, 0, tagDataLength, false));
                                }
                                if( emv_is_tag_present(0x9B) >= 0)
                                {
                                    tagDataLength = emv_get_tag_data(0x9B, tagData, tagData.length);
                                    appState.terminalConfig.setLastTSI(StringUtil.toHexString(tagData, 0, tagDataLength, false));
                                }

                                getEMVCardInfo();
                                if ((TransDefine.transInfo[appState.getTranType()].flag & T_NOCAPTURE) == 0)
                                {
                                    if( appState.trans.getEMVRetCode() == APPROVE_OFFLINE )
                                    {
                                        if(appState.terminalConfig.getUploadType() == 0)
                                        {
                                            if(   appState.trans.getEMVOnlineFlag() == true
                                                    && appState.trans.getEMVOnlineResult() == ONLINE_FAIL
                                                    )
                                            {
                                                //saveAdvice();
                                            }
                                            //offlineSuccess();
                                        }
                                        else{
                                            // 需判断是否联机
                                            if(   appState.trans.getEMVOnlineFlag() == true
                                                    && appState.trans.getEMVOnlineResult() == ONLINE_FAIL
                                                    )
                                            {
                                                // Reversal
                                                appState.setProcessState(PROCESS_REVERSAL);
                                                requestCode = STATE_PROCESS_ONLINE;
                                                processResult();
                                                generic();
                                            }
                                            else{
                                                // Confirm
                                                appState.setProcessState(PROCESS_CONFIMATION);
                                                getEMVCardInfo();
                                                requestCode = STATE_PROCESS_ONLINE;
                                                processResult();
                                                generic();
                                            }
                                            return;
                                        }

                                    }
                                    else if(appState.trans.getEMVRetCode() == APPROVE_ONLINE)
                                    {
                                        if(appState.terminalConfig.getUploadType() == 0)
                                        {
                                            //transSuccess();
                                        }
                                        else{
                                            appState.setProcessState(PROCESS_CONFIMATION);
                                            getEMVCardInfo();
                                            requestCode = STATE_PROCESS_ONLINE;
                                            processResult();
                                            generic();
                                            return;
                                        }
                                    }
                                    else{
                                        if(   appState.trans.getEMVOnlineFlag() == true
                                                && appState.trans.getEMVOnlineResult() == ONLINE_FAIL
                                                )
                                        {
                                            // 通讯失败
                                            if(appState.terminalConfig.getUploadType() == 0)
                                            {
                                                //saveAdvice();
                                            }
                                            else{
                                                appState.setProcessState(PROCESS_REVERSAL);
                                                getEMVCardInfo();
                                                requestCode = STATE_PROCESS_ONLINE;
                                                processResult();
                                                generic();
                                                return;
                                            }
                                        }
                                        else if(   appState.trans.getEMVOnlineFlag() == true
                                                && appState.trans.getEMVOnlineResult() == ONLINE_SUCCESS
                                                )
                                        {
                                            if(emv_is_need_advice() == 1)
                                            {
                                                if(appState.terminalConfig.getUploadType() == 0)
                                                {
                                                    //saveAdvice();
                                                }
                                                else{
                                                    appState.setProcessState(PROCESS_ADVICE_ONLINE);
                                                    getEMVCardInfo();
                                                    requestCode = STATE_PROCESS_ONLINE;
                                                    processResult();
                                                    generic();
                                                    return;
                                                }
                                            }
                                            else{
                                                if(appState.terminalConfig.getUploadType() == 0)
                                                {
                                                    //saveAdvice();
                                                }
                                                else{
                                                    appState.setProcessState(PROCESS_REVERSAL);
                                                    getEMVCardInfo();
                                                    requestCode = STATE_PROCESS_ONLINE;
                                                    processResult();
                                                    generic();
                                                    return;
                                                }
                                            }
                                        }
                                        else{
                                            if(emv_is_need_advice() == 1)
                                            {
                                                if(appState.terminalConfig.getUploadType() == 0)
                                                {
                                                    //saveAdvice();
                                                }
                                                else{
                                                    appState.setProcessState(PROCESS_ADVICE_ONLINE);
                                                    getEMVCardInfo();
                                                    requestCode = STATE_PROCESS_ONLINE;
                                                    processResult();
                                                    generic();
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    appState.setProcessState(PROCESS_NORMAL);
                                }
                                //setResult(RESULT_OK, getIntent());
                                //finish();
                                //doVTU();
                               /* if(doBalance() != null){
                                    try {
                                        printReceipt();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }*/
                                //transType
                                System.out.println("transType....."+transType);
                                //transType ="BAL_INQ";
                                switch (transType){
                                    case"VTU":

                                        doVTU();

                                        if(!timeout){
                                            int responseCode = responseCodeVTU();
                                            if(0 == responseCode){
                                                ActivityTransactionCompleted.responseCode = "00";
                                            }else{
                                                ActivityTransactionCompleted.responseCode =String.valueOf(responseCode) ;
                                            }
                                           // responseCode();
                                            Intent ite = new Intent(CardPayActivity.this, ActivityTransactionCompleted.class);
                                            ite.putExtra("transType",transType);
                                            ite.putExtra("status",responseMsg);
                                            startActivity(ite);
                                            finish();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Server timeout", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    case"BILL_PAY":
                                        if(doBill() == null){
                                            PutMessage("Service not available please try again");
                                            return;
                                        }
                                        /*else {
                                            ActivityTransactionCompleted.responseCode = "00";
                                            Intent ite = new Intent(CardPayActivity.this, ActivityTransactionCompleted.class);
                                            ite.putExtra("transType",transType);
                                            ite.putExtra("status",responseMsg);
                                            startActivity(ite);
                                            finish();
                                        }*/

                                        if(!timeout){
                                            proceedResult();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Server timeout", Toast.LENGTH_LONG).show();
                                        }



                                        break;
                                    case"BAL_INQ":
                                        System.out.println("in card balanced before");
                                       doBalance();
                                        System.out.println("in card balanced after timeout"+timeout);
                                        if(!timeout){
                                            int responseCode=responseCodeBalance();
                                            System.out.println("responseCode+++++"+responseCode+"pp"+balanceEnquiry.getBal());
                                            Intent it = new Intent(CardPayActivity.this, ActivityTransactionCompleted.class);
                                            if(0 ==responseCode ){

                                                ActivityTransactionCompleted.responseCode = "00";
                                                it.putExtra("transType",transType);
                                                it.putExtra("status",responseMsg);
                                                it.putExtra("ledger",balanceEnquiry.getBal());
                                                it.putExtra("main",balanceEnquiry.getBal1());
                                                PrintSlip.ledgerBalance = balanceEnquiry.getBal();
                                                PrintSlip.availableBalance = balanceEnquiry.getBal1();
                                                startActivity(it);
                                                finish();
                                            }else{
                                                ActivityTransactionCompleted.responseCode =String.valueOf(responseCodeBalance());
                                               /** ActivityTransaction.responseCode =String.valueOf(responseCode) ;
                                                it.putExtra("transType",transType);
                                                it.putExtra("status",responseMsg);

                                                startActivity(it);
                                                finish();**/
                                                Intent it2 = new Intent(CardPayActivity.this, ActivityTransactionCompleted.class);
Response rsp=new Response();
String answer=rsp.getResponse(responseCode);
                                                it2.putExtra("transType",transType);
                                                it2.putExtra("status",answer);
                                                startActivity(it2);
                                                finish();

                                            }




                                        }else{
                                            Toast.makeText(getApplicationContext(), "Server timeout", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                }


                                break;
                            default:
                                switch (appState.trans.getEMVRetCode())
                                {
//    					case ERROR_NO_APP:
//    					case ERROR_INIT_APP:
//    						//appState.trans.setEmvCardError(true);
//    						//setResult(RESULT_OK, getIntent());
//    						appState.setErrorCode(R.string.error_no_app);
//    						finish();
//    						break;
                                    case ERROR_OTHER_CARD:
                                        appState.trans.setEmvCardError(true);
                                        //setResult(RESULT_OK, getIntent());
                                        appState.setErrorCode(R.string.error_other_card);
                                        //finish();
                                        break;
                                    case ERROR_EXPIRED_CARD:
                                        appState.setErrorCode(R.string.error_expiry_card);
                                        //finish();
                                        break;
                                    case ERROR_CARD_BLOCKED:
                                        appState.setErrorCode(R.string.error_card_blocked);
                                        //finish();
                                        break;
                                    case ERROR_APP_BLOCKED:
                                        appState.setErrorCode(R.string.error_app_blocked);
                                        //finish();
                                        break;
                                    case ERROR_SERVICE_NOT_ALLOWED:
                                        appState.setErrorCode(R.string.error_not_accepted);
                                        //finish();
                                        break;
                                    case ERROR_PINENTERY_TIMEOUT:
                                        appState.setErrorCode(R.string.error_pin_timeout);
                                        //finish();
                                        break;
                                    default:
                                        appState.setErrorCode(R.string.error_ic );
                                        //finish();
                                        break;
                                }
                                break;
                        }
                        break;
                    case PREPROCESS_ERROR_NOTIFIER:
                        if(appState.getErrorCode() == 0)
                            appState.setErrorCode(R.string.error_pre_process);
                        //finish();
                        break;
                }
            }
        };

        mEMVThread = new CardPayActivity.EMVThread();
        mEMVThread.start();
    }

    private void proceedResult(){
        int responseCode = responseCode();
        Response response = new Response();
        System.out.println("responseCode "+responseCode);
       int response1;

        if(0 == responseCode){
            ActivityTransactionCompleted.responseCode = "00";
           response1 = 00;
            responseMsg = response.getResponse(response1);
        }else{
            ActivityTransactionCompleted.responseCode =String.valueOf(responseCode) ;
            response1 =responseCode ;
            responseMsg = response.getResponse(response1);
        }


        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");//dd-MM-yyyy
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        final String str = formatter.format(curDate);
        System.out.println("current time======"+str);
        PrintSlip.tId= tId;
        PrintSlip.date =str;
        Intent it = new Intent(CardPayActivity.this, ActivityTransactionCompleted.class);
        it.putExtra("transType",transType);
        it.putExtra("status",responseMsg);
        finish();
        startActivity(it);
        finish();
    }

    private void toastMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private int openDrawer(){
        try {
            int result = CashDrawerInterface.open();
            System.out.println("open drawer value..."+result);
            if (result < 0) {
                toastMessage("Drawer could not be opened");
            } else {
                FuncActivity.isOpened = true;
                return CashDrawerInterface.kickOut();
                //toastMessage("Drawer could not be opened");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int closeDrawer() {
        FuncActivity.isOpened = false;
        int result = CashDrawerInterface.close();
        return result;
    }


    @Override
    public void onStart() {
        if(debug)Log.e(APP_TAG, "idleActivity onStart");
        super.onStart();
        appState.initData();

        appState.idleFlag = true;
        if(appState.icInitFlag != true)
        {
            appState.idleFlag = false;
            //go2Error(R.string.error_init_ic);
            Toast.makeText(this, R.string.error_init_ic, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public  void PutMessage(char[] text, int start, int len)
    {

        TextView lb = new TextView(this);
        lb.setTextSize(16);
        lb.setText(text, 0, len);

        if (llyt_Scroll_Demo.getChildCount() > 0)
            llyt_Scroll_Demo.addView(lb);
        else
            llyt_Scroll_Demo.addView(lb);
        if (llyt_Scroll_Demo.getChildCount() > 500)
        {
            llyt_Scroll_Demo.removeViewAt(llyt_Scroll_Demo.getChildCount() - 1);
        }
        llyt_Scroll_Demo.invalidate();

        sclv_Demo.post(new Runnable()
        {
            @Override
            public void run()
            {
                sclv_Demo.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void printReceipt() throws UnsupportedEncodingException {
      /**  PrintSlip print = new PrintSlip();
        if(print.open() > 0){
            if(print.queryStatus() == 0){
                //NO paper
                toastMessage("No paper in printer");
            }else if(print.queryStatus() > 0){
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher);
                print.writesBitmap(bitmap);
                //print.writeLineBreak(1);
            }
        }else{

        }**/
    }


    ////////////////////PIN///////////////////////

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

    private void notifyPinSuccess()
    {
        System.out.println("notifyPinSuccess");
        Message msg = new Message();
        msg.what = PIN_SUCCESS_NOTIFIER;
        mmHandler.sendMessage(msg);
        requestCode = STATE_INPUT_PIN;
        generic();
    }

    private void notifyPinError()
    {
        Message msg = new Message();
        msg.what = PIN_ERROR_NOTIFIER;
        mmHandler.sendMessage(msg);
    }

    private void notifyPinCancel()
    {
        Message msg = new Message();
        msg.what = PIN_CANCELLED_NOTIFIER;
        mmHandler.sendMessage(msg);
    }

    private void notifyPinTimeout()
    {
        Message msg = new Message();
        msg.what = PIN_TIMEOUT_NOTIFIER;
        mmHandler.sendMessage(msg);
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
                        //textPin.setText(msg.obj.toString());	// 这一行也不会执行, 因为 Pinpad.showText() 不会触发回调... DuanCS@[20150912]
                        System.out.println("PIN_AMOUNT_SHOW..."+msg.obj.toString());
                        break;
                    case PIN_KEY_CALLBACK:
                        //textPin.setText(stars, 0, msg.arg1 & 0x0F);
                        System.out.println("PIN_AMOUNT_SHOW..."+stars+"...textPin.getText()...");
                        PutMessage("*");
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
            byte[] MasterKey = new byte[]{(byte)0xCF,(byte)0x18,(byte)0x82,(byte)0x77,(byte)0xE1,(byte)0x09,(byte)0x06,(byte)0x11,(byte)0x21,
                    (byte)0xBB,(byte)0xF6,(byte)0xE7,(byte)0x66,(byte)0x55,(byte)0x78,(byte)0x32};
            //5242820805099262
            byte[] pan = new byte[]{(byte)0x52,(byte)0x42,(byte)0x82,(byte)0x08,(byte)0x05,(byte)0x09,(byte)0x92,(byte)0x62};

            byte[] NewMasterKey = new byte[]{(byte)0xCF,(byte)0x18,(byte)0x82,(byte)0x77,(byte)0xE1,(byte)0x09,(byte)0x06,(byte)0x11,(byte)0x21,
                    (byte)0xBB,(byte)0xF6,(byte)0xE7,(byte)0x66,(byte)0x55,(byte)0x78,(byte)0x30};

            byte[] ZONEPINKey = new byte[]{(byte)0xAD,(byte)0xBD,(byte)0xEF,(byte)0x38,(byte)0x2F,(byte)0x0C,(byte)0x15,(byte)0x0B,(byte)0x61,
                    (byte)0x40,(byte)0xC2,(byte)0x9C,(byte)0xBF,(byte)0x90,(byte)0x96,(byte)0xDF};


            byte[] ZONEPINKeys = new byte[]{(byte)0xAD,(byte)0xBD,(byte)0xEF,(byte)0x38,(byte)0x2F,(byte)0x0C,(byte)0x15,(byte)0x0B,(byte)0x61,
                    (byte)0x40,(byte)0xC2,(byte)0x9C,(byte)0xBF,(byte)0x90,(byte)0x96,(byte)0xDF};
            byte[] MasterKeys = new byte[]{(byte)0xCF,(byte)0x18,(byte)0x82,(byte)0x77,(byte)0xE1,(byte)0x09,(byte)0x06,(byte)0x11,(byte)0x21,
                    (byte)0xBB,(byte)0xF6,(byte)0xE7,(byte)0x66,(byte)0x55,(byte)0x78,(byte)0x30};
            Log.i("MasterKeys",MasterKeys.toString());
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

                //PinPadInterface.setupCallbackHandler(CardPayActivity.this);
            }
//appState.terminalConfig.getKeyIndex()
            int masterInt = PinPadInterface.updateMasterKey(1,
                    MasterKeys,
                    MasterKeys.length,
                    MasterKeys,
                    MasterKeys.length
            );
            System.out.println("master int ..." + masterInt + "..."+zonePinKey);

            //appState.terminalConfig.getKeyIndex()
            ret = PinPadInterface.updateUserKey(1,
                    0,
                    Utils.hexToBytes(zonePinKey),
                    Utils.hexToBytes(zonePinKey).length
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
                //byte[] text = (AppUtil.formatAmount(appState.trans.getTransAmount())).getBytes();
                byte[] text = (String.valueOf(appState.trans.getTransAmount())).getBytes();
                //PinPadInterface.setText(0, text, text.length, 0);

                String instr = "ENTER PIN";
                //PinPadInterface.setText(1, instr.getBytes(), instr.getBytes().length, 0);
                PinPadInterface.setText(0, null, 0, 0);
                PinPadInterface.setText(1, null, 0, 0);
                System.out.println("ENTER PIN..."+instr);
            }
            PinPadInterface.setKey(2, appState.terminalConfig.getKeyIndex(), 0, DOUBLE_KEY);

            System.out.println("....123PAN..."+appState.trans.getPAN());
            ret = PinPadInterface.inputPIN(appState.trans.getPAN().getBytes(), appState.trans.getPAN().getBytes().length, pinBlock, 60000, 0);
            System.out.println("ret..ret..."+ret+" pinBlock.."+pinBlock);
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
            System.out.println("ret..ret... second   "+ret);
            if(ret == 0)
            {
                appState.trans.setPinEntryMode(CANNOT_PIN);
            }
            else
            {
                ISOParams.setPinBlock(StringUtil.toHexString(pinBlock, 0, pinBlock.length, false));
                appState.trans.setPinBlock(pinBlock);
                System.out.println("pinBlock second "+pinBlock);
                appState.trans.setPinEntryMode(CAN_PIN);
            }
            notifyPinSuccess();
            PinPadInterface.close();
            appState.pinpadOpened = false;
        }
    }

    private void inputPinActivity(){
        requestCode = STATE_INPUT_PIN;
        mReadPINThread=new CardPayActivity.ReadPINThread();
        mReadPINThread.start();
        PutMessage("<<< PLEASE INPUT PIN ON PINPAD");
        appState.trans.setTransAmount(1);

        mmHandler = new Handler()
        {
            public void handleMessage(Message msg)
            { /*这里是处理信息的方法*/
                switch (msg.what)
                {
                    case PIN_SUCCESS_NOTIFIER:
                        PutMessage("Please Wait...");
                        setResult(Activity.RESULT_OK, getIntent());
                        return;
                    //setResult(Activity.RESULT_OK, getIntent());
                    //break;
                    case PIN_ERROR_NOTIFIER:
                        appState.setErrorCode(R.string.error_pinpad);
                        PutMessage(getString(R.string.error_pinpad));
                        break;
                    case PIN_CANCELLED_NOTIFIER:
                        appState.setErrorCode(R.string.error_user_cancelled);
                        PutMessage(getString(R.string.error_user_cancelled));
                        break;
                    case PIN_TIMEOUT_NOTIFIER:
                        appState.setErrorCode(R.string.error_input_timeout);
                        PutMessage(getString(R.string.error_input_timeout));
                        break;
                }
                //func.exit();
            }
        };
    }

    public int responseCode(){

        int res = -1;
        Response response = new Response();

        try {
            res = Integer.parseInt(ISOBillPayment.getInitialResponse());
            Log.w("res",""+res);
            responseMsg = response.getResponse(res);
            return res;
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        return -1;
    }

    public int responseCodeBalance(){

        int res = -1;
        Response response = new Response();
        System.out.println("iso response first="+ ISOBalanceInquiry.getInitialResponse());
        try {
            res = Integer.parseInt(ISOBalanceInquiry.getInitialResponse());
           //ISOBalanceInquiry.resetInitialResponse();
            Log.w("res",""+res);
            responseMsg = response.getResponse(res);
            return res;
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        return -1;
    }
    public int responseCodeVTU(){
        System.out.println("not in responseCodeVTU()");
        int res = -1;
        Response response = new Response();

        try {
            res = Integer.parseInt(ISOVTU.getInitialResponse());
           // ISOVTU.resetInitialResponse();
            Log.w("res vtu",""+res);
            responseMsg = response.getResponse(res);
            return res;
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        return -1;
    }

    public void beginPaymentViaCard( String driver_id, String phone,String stan,String pan){

        Map<String, String> param= new HashMap<>();

        System.out.println("phone "+phone+" pass "+pan);
        param.put("driver_id", driver_id);
        param.put("phone", phone);
        param.put("stan", stan);
        param.put("pan", pan);

        System.out.println("params "+new JSONObject(param));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                connect.getUrlOyo("beginPaymentViaCard"), new JSONObject(param),
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("response ll "+response);
                            System.out.println("response-- LOGIN "+response.getString("response_message"));
                            System.out.println("code  return"+response.getString("response_code"));

                            if(response.getString("response_code").equalsIgnoreCase("200")) {
                                ParseJSonResponse(response);



                            }else{
                                ref_code="";
                                // passResponseMessage("Failed",DESDESDES.decrypt(response.getString("response_message")));
                            }
                        }catch(Exception ex){}
                    }
                }, new com.android.volley.Response.ErrorListener() {

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
    public void ParseJSonResponse(JSONObject object) {
        JSONArray array = null;
        JSONArray arraycat = null;
        JSONObject obj = null;
        JSONObject ticket = null;


        try {
            obj = object.getJSONObject("data");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ticket = obj.getJSONObject("ticket");
            System.out.println("ticket " + ticket);
            ConstantFile.ref_code = ticket.getString("ref_code");
            System.out.println("ref "+ ConstantFile.ref_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
