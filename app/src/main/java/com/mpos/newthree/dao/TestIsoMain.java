package com.mpos.newthree.dao;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mpos.newthree.iso.BaseChannel;
import com.mpos.newthree.iso.ISOException;
import com.mpos.newthree.iso.ISOMsg;
import com.mpos.newthree.util.Logger;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by TECH-PC on 3/28/2018.
 */
public class TestIsoMain extends AppCompatActivity {
    public static int echoInterval = 60;
    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
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
    private  static String terminalID = "27003661";//20390007, 27002378
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        showResult();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public TestIsoMain(Context ctx, String terminalID){
        this.ctx = ctx;
        this.stan = rand();
        dbManager = new DBManager(ctx);
        TestIsoMain.terminalID = terminalID;
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            try {
                //isoo8583.main();
                //    t.start();
                Log.d("jjkkkkkkkkkkkkkkkk", "testering");
                //    t.start();
                //   isoo8583.main();
                showResult();
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
        logISOMsg(null);
    }

    public static String getTheMasterKey(){
        System.out.print("MyKEYZ"+theMasterKey);
        return theMasterKey.substring(0, 32);
    }

    public static void logISOMsg(ISOMsg msg) {
        System.out.println("----ISO MESSAGE-----");

        try {
            if(msg!=null){
            System.out.println("  MTI : " + msg.getMTI());
            for (int i=1;i<msg.getMaxField();i++) {
                if (msg.hasField(i)) {
                    System.out.println("    Field-"+i+" : "+msg.getString(i));
                }
            }}
            System.out.println("--------------------");
            //Timer timer = new Timer();
            //timer.schedule(, 5000, echoInterval*1000);
            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
            long period = 5000; // the period between successive executions
            //exec.scheduleAtFixedRate(new KeepConnection(), 0, period, TimeUnit.MICROSECONDS);
            long delay = echoInterval*1000; //the delay between the termination of one execution and the commencement of the next
            exec.scheduleWithFixedDelay(new KeepConnection(), 0, delay, TimeUnit.MICROSECONDS);
        } catch (ISOException e) {
            e.printStackTrace();
        }

    }
    public static void showResult(){
        Timer timer = new Timer();

        Context con;

        // button.setText(value);
        timer.schedule(new KeepConnection(), 0, 30*1000);
    }
}
