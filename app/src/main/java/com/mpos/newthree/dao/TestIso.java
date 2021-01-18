package com.mpos.newthree.dao;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mpos.newthree.db.DatabaseOpenHelper;
import com.mpos.newthree.iso.BaseChannel;
import com.mpos.newthree.iso.ISOException;
import com.mpos.newthree.iso.ISOMsg;
import com.mpos.newthree.iso.channel.PostChannel;
import com.mpos.newthree.iso.packager.ISO87APackagerBBitmap;
import com.mpos.newthree.security.CryptoUtils;
import com.mpos.newthree.util.Logger;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestIso extends AppCompatActivity {
    public static int echoInterval = 60;
    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
    public String zoneMasterKey;//CF188277E109061121BBF6E766557830"; //"9CB8662B93A95059DEBB66A64D113677";
    public static String keyEncryptKey;
    public static String track2Decrypt;
    protected CryptoUtils crypt = null;
    public static DatabaseOpenHelper getKeys;
    protected SQLOperations sql = null;
    //public static String terminalId = "27003608";
    protected String acqId;
    protected String ip;
    protected int port;
    //* protected org.apache.log4j.Logger logger2 = org.apache.log4j.Logger.getLogger(ISOMessage.class);
    private String address;
    protected String merchantId;
    protected String itemTypeCode;
    protected static String myfullName;
    protected ISOMsg m = new ISOMsg();
    public static int hostPort = 10001; //etz
    //public static int hostPort = 1010;//access switch
    //public static String hostUrl = "174.142.90.139";//access switch
    public static String hostUrl = "98.129.32.74";//etz
    public static String pan;
    //private Tab_Transaction tab;

    public static boolean withdrawal;

    public static String zonePinKeys="";

    public TestIso testIso;

    protected static SimpleDateFormat curDateTime = new SimpleDateFormat("MMddHHmmss");
    protected static SimpleDateFormat curDate = new SimpleDateFormat("MMdd");
    protected static SimpleDateFormat curTime = new SimpleDateFormat("HHmmss");
    protected NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));
    public static BaseChannel etzChannel = null;
    Logger logger = null;
    protected StringBuilder responseString = null;
    android.os.Handler customHandler = new android.os.Handler();
    public static String theMasterKey;
    private static Context ctx;
    private static int stan;
    private DBManager dbManager;
    private  static String terminalID = null;//20390007, 27002378
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        themains();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public TestIso(Context ctx, String terminalID){
        this.ctx = ctx;
        this.stan = rand();
        dbManager = new DBManager(ctx);
       TestIso.terminalID = terminalID;
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            try {
                //isoo8583.main();
                //    t.start();
                Log.d("", "testering");
                //    t.start();
                //   isoo8583.main();
                themains();
                customHandler.postDelayed(this, 1000);
                // t.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }};

    static String getTerminal(){
        if(terminalID != null) {
            return terminalID;
        }
        return null;
    }

    public static String getStan(){
        return String.format("%06d", stan);
    }
    public static String getStan(int stan){
        return String.format("%06d", stan);
    }

    public static int rand(){
        Random rand = new Random();
        int no =  rand.nextInt(100000);
        if( no <= 999999 ){
            return  no;
        }else{
            return getNextTransId();
        }
    }


    protected static int getNextTransId() {
        int count = 1;
        count++;
        int retval = count;
        return retval;
    }

    public static void themains() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //   GenericPackager packager = null;
        Date date = new Date();
        try {
            // packager = new GenericPackager("config/hbngpostilion.xml");

            // Create ISO Message
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setMTI("0800");
            //   isoMsg.set(3, "9A0000");
            isoMsg.set(7, curDateTime.format(date));
            //  m.set(7, curDateTime.format(date));
            isoMsg.set(12, curTime.format(date));
            isoMsg.set(13, curDate.format(date));
            isoMsg.set(15, curDate.format(date));
            // m.set(13, curDate.format(date));

            isoMsg.set(41, getTerminal());         //terminalId
            isoMsg.set(70, "101");
            // print the DE list
            isoMsg.setPackager(new ISO87APackagerBBitmap());
            //isoMsg.setPackager(new GenericPackager("config/hbngpostilion.xml"));
            logISOMsg(isoMsg);
            // Get and print the output result
            byte[] data = isoMsg.pack();
            etzChannel = new PostChannel(ISOMessage.getHostUrl(), ISOMessage.getHostPort(), new ISO87APackagerBBitmap());
            //	 etzChannel = new PostChannel("192.168.3.230", 10001, new GenericPackager("C:\\Users\\HP\\Documents\\Adobe\\just_test\\EMVSample\\src\\com\\wizarpos\\emvsample\\activity\\config\\hbngpostilion.xml"));
            //	etzChannel = new PostChannel("127.0.0.1", 10001, new XMLPackager());
            // etzChannel = new PostChannel("192.168.3.230", 10001, new ISOBasePackager(){});
            etzChannel.setTimeout(300 * 1000);
            // ((LogSource)etzChannel).setLogger(logger, "etz-channel");
            etzChannel.connect();
            etzChannel.send(isoMsg);
            ISOMsg resp = etzChannel.receive();
            theMasterKey = resp.getString(125);
            System.out.print("resp.... "+resp);
            System.out.print("the master"+theMasterKey);

        } catch (ISOException e) {
            System.out.print("ERROR Found:");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTheMasterKey(){
        System.out.print("MyKEYZ"+theMasterKey+" return="+theMasterKey.substring(0, 32));
        return theMasterKey.substring(0, 32);
    }

    private static void logISOMsg(ISOMsg msg) {
        System.out.println("----ISO MESSAGE-----");
        try {
            System.out.println("  MTI : " + msg.getMTI());
            for (int i=1;i<msg.getMaxField();i++) {
                if (msg.hasField(i)) {
                    System.out.println("    Field-"+i+" : "+msg.getString(i));
                }
            }
            System.out.println("--------------------");
            //Timer timer = new Timer();
            //timer.schedule(, 5000, echoInterval*1000);
            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
            long period = 5000; // the period between successive executions
            exec.scheduleAtFixedRate(new KeepConnection(), 0, period, TimeUnit.MICROSECONDS);
            long delay = echoInterval*1000; //the delay between the termination of one execution and the commencement of the next
            exec.scheduleWithFixedDelay(new KeepConnection(), 0, delay, TimeUnit.MICROSECONDS);
        } catch (ISOException e) {
            e.printStackTrace();
        }

    }
}
