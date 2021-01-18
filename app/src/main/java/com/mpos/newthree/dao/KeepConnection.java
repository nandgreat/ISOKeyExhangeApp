/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpos.newthree.dao;

import android.content.SharedPreferences;
import android.util.Log;

import com.mpos.newthree.activity.MainActivity;
import com.mpos.newthree.iso.ISOException;
import com.mpos.newthree.iso.ISOMsg;
import com.mpos.newthree.iso.ISOUtil;


import java.io.IOException;
import java.net.SocketException;

import static android.widget.Toast.makeText;


//* import com.access.ini.InitializeConfig;

/**
 * @author SUNNYben
 */
public class KeepConnection extends ISOMessage {
    private int pingCounter = 0;

    private static boolean reachable = false;
    public static boolean requestKey = false;

    private static boolean lockScreen = true;


    @Override
    public void run() {
        if (!isWaiting) {
            //ISOUtil.sleep(20 * 1000);
            while (true){
                try {
                    connectHost();

                    if (!reachable) {
                        try {
                            //logISOMsg(keyExchangeMessage(), "!reachable");
                            etzChannel.send(keyExchangeMessage());
                        } catch(SocketException | ISOException ex ) {
                            //*     logger2.info("SocketException: "+ex.getMessage() + " Reconnecting...");
                            etzChannel.connect();
                            etzChannel.send(keyExchangeMessage());
                        }
                        ISOMsg response = etzChannel.receive();
                        System.out.println("Checkpoint 2" +response);
                        //logISOMsg(response, "code Exchange Incoming");
                        Log.w("Checkpoint 2",response.toString());

                        String key = response.getString(125);
                        System.out.println("key "+key);
                   //     Log.w("CheckDigit: ",key+" length="+key.length());
                        String zonePinKey = "";
                        String checkDigit = "";
                        if (key.length() > 32) {
                            zonePinKey = key.substring(0, 32);
                            checkDigit = key.substring(32);
                            System.out.println(" substring zone="+zonePinKey+" checkDigit="+checkDigit);
                        } else {
                            zonePinKey = key;
                        }
                        //System.out.println("Checkpoint 3");
                        //sql.updateKey(zonePinKey, checkDigit, "etranzact");
                        SharedPreferences preferences = MainActivity.context.getSharedPreferences("temp", MainActivity.context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("zonePinKey", zonePinKey);
                        editor.putString("checkDigit", checkDigit);
                        editor.commit();

                        //DatabaseAccess.code=zonePinKey;
                        //sql.close();

                        //frame.showHomePage();
                        reachable = true;

                    } else if (requestKey) {
                        try {
                            //logISOMsg(keyExchangeMessage(), "requestKey");
                            etzChannel.send(keyExchangeMessage());
                        } catch(SocketException | ISOException ex ) {
                            //*    logger2.info("SocketException: "+ex.getMessage() + " Reconnecting...");
                            etzChannel.connect();
                            etzChannel.send(keyExchangeMessage());
                        }
                        ISOMsg response = etzChannel.receive();
                        String key = response.getString(125);
                        String zonePinKey = "";
                        String checkDigit = "";
                        if (key.length() > 32) {
                            zonePinKey = key.substring(0, 32);
                            checkDigit = key.substring(32);
                        } else {
                            zonePinKey = key;
                        }
                        requestKey = false;

                        SharedPreferences preferences = MainActivity.context.getSharedPreferences("temp", MainActivity.context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("zonePinKey", zonePinKey);
                        editor.putString("checkDigit", checkDigit);
                        editor.commit();
                        //DatabaseAccess.code=zonePinKey;
                        //sql.updateKey(zonePinKey, checkDigit, "etranzact");
                        //sql.close();

                    }
                    else {
                        try {
                            etzChannel.send(echoMessage());
                        } catch(SocketException | ISOException ex ) {
                            //*    logger2.info("SocketException: "+ex.getMessage() + " Reconnecting...");
                            etzChannel.connect();
                            etzChannel.send(keyExchangeMessage());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        ISOMsg msg = etzChannel.receive();
                        if (msg.getString(39).equals("06")) {
                            requestKey = true;
                        }
                    }
                } catch (NullPointerException e) {
                    //*    logger2.info("NullPointerException "+e.getMessage());
                    e.printStackTrace();
                } catch (java.net.SocketTimeoutException e) {
                    // System.out.println("Timed out");
                    Log.d("Socket Time :","Timed out");
                    //logger2.info("SocketTimeOutException: "+e.getMessage());
                    e.printStackTrace();
                } catch (IOException | ISOException ex) {
                    reachable = false;
                }

                SQLOperations net = new SQLOperations();

                if (reachable) {
                    // System.out.println("Host Server is Reachable");
                    Log.d("Host RESULT :","Host Server is Reachable");

                    lockScreen = false;
                    pingCounter = 0;
                    //*     net.insertNetworkStatus("Connected");
                } else {
                    //System.out.println("Host Server NOT Reachable");
                    Log.d("Host RESULT :"," Host Server NOT Reachable");
                    //*        net.insertNetworkStatus("Disconnected");
                    pingCounter++;
                    // if (pingCounter == (180 / InitializeConfig.getEchoInterval())) {      // 3 minutes
                    try {
                        //frame.showOutOfService();
                        lockScreen = true;
                    } catch (Exception e) {

                    }
                    //  }
                }
                ISOUtil.sleep(60 * 1000);
                //     ISOUtil.sleep(InitializeConfig.getEchoInterval() * 1000);
            }
        }
        else {
            // System.out.println("Is waiting is false");
            Log.d("Waiting :"," Is waiting is false");

        }
    }

    public static boolean isLockScreen() {
        return lockScreen;
    }

    @Override
    protected ISOMsg setTransactionParams(String track2, String PlainPin, String parameter, String fld55, String amount, String fees, String acctType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ISOResponse doTransaction(String track2, String PlainPin, String parameter, String fld55, String amount, String fees, String acctType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
