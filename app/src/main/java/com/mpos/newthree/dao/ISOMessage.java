/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpos.newthree.dao;

//* import com.access.server.CoolServer;

import android.content.SharedPreferences;
import android.util.Log;

import com.mpos.newthree.activity.MainActivity;
import com.mpos.newthree.core.Configuration;
import com.mpos.newthree.core.ConfigurationException;
import com.mpos.newthree.core.SimpleConfiguration;
import com.mpos.newthree.db.DatabaseOpenHelper;
import com.mpos.newthree.iso.BaseChannel;
import com.mpos.newthree.iso.ISOException;
import com.mpos.newthree.iso.ISOMsg;
import com.mpos.newthree.iso.channel.PostChannel;
import com.mpos.newthree.iso.packager.ISO87APackagerBBitmap;
import com.mpos.newthree.obj.Transaction;
import com.mpos.newthree.obj.TransactionInsertDBHelper;
import com.mpos.newthree.security.CryptoUtils;
import com.mpos.newthree.util.BufferedLogListener;
import com.mpos.newthree.util.DailyLogListener;
import com.mpos.newthree.util.LogSource;
import com.mpos.newthree.util.Logger;
import com.mpos.newthree.util.ProtectedLogListener;
import com.mpos.newthree.util.SimpleLogListener;
import com.mpos.newthree.utils.Utils;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;


/**
 *
 * @author SUNNYben
 */
public abstract class ISOMessage extends TimerTask {
    public String zoneMasterKey;//CF188277E109061121BBF6E766557830"; //"9CB8662B93A95059DEBB66A64D113677";
    public static String keyEncryptKey;
    public static String track2Decrypt;
    protected CryptoUtils crypt = null;
    static SharedPreferences preferences1=  MainActivity.context.getSharedPreferences("temp", MODE_PRIVATE);

    public static BaseChannel etzChannel = null;
    public static DatabaseOpenHelper getKeys;
    protected SQLOperations sql = null;
  public static String terminalId = "20700003";//DEMO
  //public static String terminalId = preferences1.getString("terminal","").toString();

   //public static String terminalId = "27003631";
    //public static String terminalId = "27003608";
    protected String acqId;
    protected String ip;
    protected int port;
    //* protected org.apache.log4j.Logger logger2 = org.apache.log4j.Logger.getLogger(ISOMessage.class);
    private String address;
    protected String merchantId;
    protected StringBuilder responseString = null;
    protected String itemTypeCode;
    protected static String myfullName;
    protected ISOMsg m = new ISOMsg();
    //public static int hostPort = 10001; //etz
    public static int hostPort = 1010;//access switch
    //public static String hostUrl = "174.142.90.139";//access switch
    //public static String hostUrl = "98.129.32.74";//etz
    public static String hostUrl = "174.142.90.139";//demo
    public static String pan;
    //private Tab_Transaction tab;

    public static boolean withdrawal;

    public static String zonePinKeys="";

    public TestIso testIso;

    protected SimpleDateFormat curDateTime = new SimpleDateFormat("MMddHHmmss");
    protected SimpleDateFormat curDate = new SimpleDateFormat("MMdd");
    protected SimpleDateFormat curTime = new SimpleDateFormat("HHmmss");
    protected NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));
    protected ISOMsg request = null;
    Logger logger = null;
    protected static boolean isWaiting;
    private TransactionInsertDBHelper dbHelper;
    private Transaction transaction;
    //private NetApi api;
    public static String parameters;
    public static String stan;
    public static int connTimeout;
    public static String getHostUrl() {
     //return "98.129.32.79";
      return "174.142.90.139";//demo
        //return "174.142.90.139";
        //return "98.129.32.74"; //e-tranzact
        //return "192.168.3.144"; //accessswitch
       //return "196.6.103.10"; //NIBSS
       // return "192.168.3.10";
    }
    public static int getHostPort() {
    // return 10001;
   return 1010;//demo
        //return 10001;
      // return 1010;
       // return 55531;
       //return 1025;
        //return 1024;
    }


    public ISOMessage() {
        try {

            //tab = new Tab_Transaction();
            System.out.println("Initializing ISOMessage constructor");
            sql = new SQLOperations();
            //zoneMasterKey = "CF188277E109061121BBF6E766557830";//testIso.getTheMasterKey();CF188277E109061121BBF6E766557830
            zoneMasterKey =   "CF188277E109061121BBF6E766557830";
            crypt = new CryptoUtils();
            //  ip = InitializeConfig.getHostUrl();
            //port = InitializeConfig.getHostPort();
            //terminalId = InitializeConfig.getTerminalId();
            //acqId = InitializeConfig.getAcqId();
            //address = InitializeConfig.getAddress();
            //merchantId = InitializeConfig.getMerchantCode();
            //etzChannel = new PostChannel(getHostUrl(), getHostPort(), new ISO87APackagerBBitmap());



            logger = new Logger();
            logger.setName("Q2");

            ProtectedLogListener proLog = new ProtectedLogListener();
            //SimpleLogListener simLog = new SimpleLogListener();
            BufferedLogListener bufLog = new BufferedLogListener();

            DailyLogListener dayLog = new DailyLogListener();

            Configuration prologCfg = new SimpleConfiguration();
            prologCfg.put("protect", "2 14 35");
            prologCfg.put("wipe", "52 55");

            Configuration bufLogCfg = new SimpleConfiguration();
            //cfg2.put("window", "86400");
            bufLogCfg.put("max-size", "100");
            bufLogCfg.put("name", "logger.Q2.buffered");

            bufLog.setConfiguration(bufLogCfg);

            logger.addListener(bufLog);

            Configuration dayLogCfg = new SimpleConfiguration();
            dayLogCfg.put("window", "86400");
            dayLogCfg.put("prefix", "log/iso");
            dayLogCfg.put("suffix", ".log");
            dayLogCfg.put("date-format", "-yyyy-MM-dd");
            dayLogCfg.put("compression-format", "gzip");

            try {
                proLog.setConfiguration(prologCfg);
                //bufLog.setConfiguration(bufLogCfg);
                dayLog.setConfiguration(dayLogCfg);
            } catch (ConfigurationException ex) {
                //*   logger2.info("ConfigurationException: "+ex.getMessage());
            }

            try {
                logger.addListener(new SimpleLogListener(System.out));
                logger.addListener(proLog);
                //logger.addListener(bufLog);
                logger.addListener(dayLog);
            } catch (Exception ex) {
                //*    logger2.info("Unable to open log/iso.log: "+ex.getMessage());
            }

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    protected abstract ISOMsg setTransactionParams(String track2, String PlainPin, String parameter, String fld55, String amount, String fees, String acctType);
    
    public abstract ISOResponse doTransaction(String track2, String PlainPin, String parameter, String fld55, String amount, String fees, String acctType);

    public void connectHost() throws IOException, ISOException {
        if (etzChannel == null)
        {
            System.out.println("first connection");
            etzChannel = new PostChannel(getHostUrl(), getHostPort(), new ISO87APackagerBBitmap());
            System.out.println("connTimeout ="+connTimeout);
            etzChannel.setTimeout(connTimeout * 1000);
            ((LogSource)etzChannel).setLogger(logger, "etz-channel");
            etzChannel.connect();
            // etzChannel.getSocket().setTcpNoDelay(true);
            // etzChannel.getSocket().setKeepAlive(true);
        }
        else if (!etzChannel.isConnected()) {

            Log.d("Connection Status", "connectHostChannel is NOT connected. Reconnecting...");
            etzChannel.setTimeout(connTimeout * 1000);
            etzChannel.reconnect();
            KeepConnection.requestKey = true;
            // etzChannel.sendKeepAlive();
        }
    }

    public void disconnectHost() throws IOException { }

    public ISOMsg sendTransaction(ISOMsg m) throws ISOException, java.net.SocketTimeoutException, IOException {

        //String stan = String.format("%06d", TestIso.rand()) ;
        if (stan == null){
            System.out.println("stan is null");
            stan = String.format("%06d", TestIso.rand()) ;
        }
        System.out.println("After getNextTransId keepAlive");
        Date date = new Date();

        m.set(7, curDate.format(date)+curTime.format(date));
        m.set(11, Utils.leftPad(stan, '0', 6));
        m.set(12, curTime.format(date));
        m.set(13, curDate.format(date));
        m.set(15, curDate.format(date));
        m.set(18, "6012");
        m.set(22, "901");
        m.set(25, "00");
        m.set(26, "12");
        m.set(30, "C00000000");
        m.set(32, "627000");
        m.set(33, "111111");
        m.set(37, Utils.leftPad(stan, '0', 12));
        m.set(41, terminalId);
        m.set(43, "PLOT 1161,Mem Drive,CBDABUJA        ABNG");
        m.set(49, "566");
        m.set(56, "1510");
        m.set(42, "2329000024");
        m.set(67, "03");
        m.set(123, "211201513344002");
        System.out.println("isWaiting "+isWaiting);
        isWaiting = true;
        etzChannel.send(m);
        ISOMsg resp = etzChannel.receive();
//if(resp!=null)
        System.out.println("----------------resp------------"+resp);
        isWaiting = false;
        responseString = new StringBuilder("response_code_39 = '");
        responseString.append(resp.getString(39));
        responseString.append("'");
        if (resp.hasField(54)) {
            responseString.append(", additional_amounts_54 = '");
            responseString.append(resp.getString(54));
            responseString.append("'");
        }

        if (resp.hasField(125)) {
            responseString.append(", phcn_pin_125 = '");
            responseString.append(resp.getString(125));
            responseString.append("'");
        }



        //Log to server
        dbHelper  = new TransactionInsertDBHelper(MainActivity.context);
        transaction = new Transaction();
        System.out.println("from resp main isomessage "+resp);

        return resp;

    }

    protected void getKey() throws ISOException, IOException {
        SharedPreferences preferences= MainActivity.context.getSharedPreferences("temp", MainActivity.context.MODE_PRIVATE);
        String zonePinKey=preferences.getString("zonePinKey",null);
        //String zonePinKey = DatabaseAccess.code;
        Log.i("zonePinKey","zonePinKey=> "+zonePinKey);
        System.out.println("ZoneMaster=>"+ zoneMasterKey+" Zone pin=>"+ zonePinKey);


        keyEncryptKey = crypt.do3DESDecryption(zoneMasterKey, zonePinKey);
    }

 /*   protected String formReversalMessage(ISOMsg request) throws ISOException {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getMTI());            //MTI
        builder.append(request.getString(11));      // STAN
        builder.append(request.getString(13));     // Date
        builder.append(request.getString(12));    // Time
        builder.append("00000");
        builder.append(request.getString(acqId));
        builder.append("00000");
        builder.append(request.getString(33));        
        
        return builder.toString();
    }*/
    /*
     protected void sendReversal(ISOMsg origMsg) {
        origMsg.unset(22);                            
        origMsg.unset(123);
        ISOMsg resp = null;
        StringBuilder builder = new StringBuilder();
        try {
            builder.append(origMsg.getMTI());            //MTI
            builder.append(origMsg.getString(11));      // STAN
            builder.append(origMsg.getString(13));     // Date
            builder.append(origMsg.getString(12));    // Time        
            builder.append(Utils.leftPad(origMsg.getString(32), '0', 10));        
            builder.append(Utils.leftPad(origMsg.getString(33), '0', 10));
                        
            origMsg.set(90, builder.toString());
            origMsg.setMTI("0420");
            connectHost();
            resp = sendTransaction(origMsg);
        } catch (ISOException ex) {
            logger2.info("ISOException: "+ex.getMessage());
        } catch (IOException ex) {
            logger2.info("IOException: "+ex.getMessage());
        }
    }
    */
    
    protected void sendReversal(ISOMsg origMsg) {
        origMsg.unset(22);                            
        origMsg.unset(123);
        //ISOMsg resp = null;
        StringBuilder builder = new StringBuilder();
        try {
            builder.append(origMsg.getMTI());            //MTI
            builder.append(origMsg.getString(11));      // STAN
            builder.append(origMsg.getString(13));     // Date
            builder.append(origMsg.getString(12));    // Time        
            builder.append(Utils.leftPad(origMsg.getString(32), '0', 10));        
            builder.append(Utils.leftPad(origMsg.getString(33), '0', 10));
                        
            origMsg.set(90, builder.toString());
            origMsg.setMTI("0420");            
            
            ISO8583Data isodata = new ISO8583Data(m.getMTI(), Utils.maskPAN(m.getString(2)), m.getString(3), m.getString(4), m.getString(7), 
                m.getString(11), m.getString(12), m.getString(13), m.getString(14), m.getString(15), m.getString(18), 
                m.getString(22), m.getString(23), m.getString(25), m.getString(28), m.getString(30), m.getString(32), 
                m.getString(33), "", m.getString(37), m.getString(41), m.getString(42), m.getString(43), 
                m.getString(48), m.getString(49), "", m.getString(53), "", m.getString(56), 
                m.getString(67), m.getString(90), m.getString(102), m.getString(103), m.getString(123));

            //*   sql.createRequest(isodata);     // Saves trans data to db
        
        connectHost();
        isWaiting = true;
        etzChannel.send(m);                   
        ISOMsg resp = etzChannel.receive();
        isWaiting = false;
        responseString = new StringBuilder("response_code_39 = '");
        responseString.append(resp.getString(39));
        responseString.append("'");

            //*  sql.updateResponse(isodata, responseString);
        
        } catch (ISOException ex) {
            //*       logger2.info("ISOException: "+ex.getMessage());
        } catch (IOException ex) {
            //*       logger2.info("IOException: "+ex.getMessage());
        }
    }
    
    public ISOMsg keyExchangeMessage() {
         String stan = getNextTransId();
        Date date = new Date();
        Log.i("method=","keyExchangeMessage()");
        try {

            m.setMTI("0800");

            m.set(7, curDateTime.format(date));
            m.set(11, Utils.leftPad(stan, '0', 6));
            m.set(12, curTime.format(date));
            m.set(13, curDate.format(date));
           m.set(41, terminalId);
            m.set(70, "101");

        } catch(Exception ee){ Log.i("error=",ee.getMessage());
            //*     logger2.info("Key Exchange Message exception: "+ee.getMessage());
        }

        return m;
    }

    public ISOMsg echoMessage() {
Log.i("method","echoMessage()");
          String stan = getNextTransId();
        Date date = new Date();
        try {
            // Set Field Elements
            m.setMTI("0800");            
           m.set(7, curDateTime.format(date));
           // m.set(7, "");
            m.set(11, Utils.leftPad(stan, '0', 6));
            m.set(12, curTime.format(date));
           // m.set(13, curDate.format(date));
            //*        m.set(13,DBdate);
            m.set(41, terminalId);
            m.set(70, "301");
                        
        } catch(Exception ee){
            //*       logger2.info("ECHO Message exception: "+ee.getMessage());
        }

        return m;        
    }

     protected String getNextTransId() {
         int count = 1;
         count++;
         String retval = String.valueOf(count);
       return retval;
    }
}
