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
import com.mpos.newthree.utils.Utils;
import com.mpos.newthree.wizarpos.emvsample.parameter.ISOParams;

import java.io.IOException;
import java.util.Date;

import static com.mpos.newthree.activity.MainActivity.Q2;


//import com.accessng.onepaylite.fragment.Tab_Transaction;


public class ISOBalanceInquiry extends ISOMessage {
    static String bal, bal1;
    static String initialResponse;
    private static String stan;
    private static String panSend;
    //private MainActivity mainAct;
    private String TAG = "ISO_BAL";


    @Override
    public ISOResponse doTransaction(String track2, String PlainPin, String parameter, String fld55, String amount, String fees, String acctType) {
        bal = "0.00";
        bal1 = "0.00";

        int dIndex = track2.indexOf('=');
     String   pan = track2.substring(0, dIndex);

        System.out.println("FIELD 55..." + fld55);
        System.out.println("track2..." + track2);
        System.out.println("PlainPin "+PlainPin);
        System.out.println("parameter "+parameter);
        ISOResponse isoResp = new ISOResponse();
        String[] balanceArray = {"0", "0"};
        
        try {           
            connectHost();
            getKey();

            request = setTransactionParams(track2, PlainPin, parameter, fld55, amount, fees, acctType);
            Log.i(TAG, "After setting trans params request "+request);
            //logISOMsg( request);
            ISOMsg resp = sendTransaction(request);
            Log.i(TAG,"After sending transaction ");
            initialResponse = resp.getString(39);
            System.out.println("-----------------------------------------"+initialResponse);
            Log.i(TAG,"Response 1==>\n" +resp.getString(39));
            System.out.println("resp.getString(39).."+resp.getString(39));
            if(resp.getString(39).equals("00") && resp.getString(54) != null) {
                try {

                    Log.i(TAG,"The response 1==>" +resp.getString(39));
                    balanceArray = Utils.parseBalance(resp.getString(54));
                    bal = nf.format(Double.parseDouble(balanceArray[0])/100);
                    bal1 = nf.format(Double.parseDouble(balanceArray[1])/100);
                    balanceArray[0]=bal;
                    balanceArray[1]=bal1;


                    Log.i(TAG,"balance 1==>\n" +bal1);
                    Log.i(TAG,"balance 2==>\n" +bal);
                    Log.i(TAG,"Response 1==>\n" +resp.getString(39));
                } catch (Exception e) {
                    balanceArray[0] = "";
                    balanceArray[1] = "";
                    e.printStackTrace();
                }
            }
            Log.i("whole respond=",resp.toString());
            if (resp.getString(39).equals("63")) {
               // Log.i("The response 2==>" +resp.getString(39));
                KeepConnection.requestKey = true;
            }
            isoResp.setResponseCode(resp.getString(39));            
            isoResp.setLedgerBalance(balanceArray[0]);
            isoResp.setAvailableBalance(balanceArray[1]);
            isoResp.setPan(request.getString(2));
            isoResp.setStan(request.getString(11));

            //MainActivity.timeout = false;
            disconnectHost();
        } catch (NullPointerException e) {
            e.printStackTrace();
            isoResp.setResponseCode("99");
            e.printStackTrace();
           // logger2.info("NullPointerException: "+e.getMessage());
        } catch (ISOException ex) {
            ex.printStackTrace();

           isoResp.setResponseCode("99");
            ex.printStackTrace();
       } catch(java.net.SocketTimeoutException e) {
            Log.i(TAG,"Timed out");
            //MainActivity.timeout = true;
            isoResp.setResponseCode("99");
            //thedata.timeOutCancel();
            e.printStackTrace();
        } catch (IOException ex) {
           //logger2.info("No connection");
            ex.printStackTrace();
           isoResp.setResponseCode("99");


           //MainFrame.showOutOfService();
        } catch (Exception ex) {
            isoResp.setResponseCode("99");

            ex.printStackTrace();
            //logger2.info("Exception: "+ex.getMessage());
        }
        
        return isoResp;
    }
    //public static Tab_Transaction thedata;

    public static String getPan(){return panSend;}
    public static String getStan(){return stan;}

    @Override
    protected ISOMsg setTransactionParams(String track2, String PlainPin, String fld48, String fld55, String amount, String fees, String acctType) {
        if(Q2){
            try {
                System.out.println("fees "+fees);
                System.out.println(" in q2---- PlainPin "+PlainPin);
                System.out.println("Utils.hexToBytes(PlainPin) "+Utils.hexToBytes(PlainPin));
                System.out.println("ISOParams.getExpiryDate() "+ISOParams.getExpiryDate()+" ISOParams.getPans() "+ISOParams.getPans()+" ISOParams.getSeqNos() "+ISOParams.getSeqNos());
                m.setMTI("0200");
                System.out.println("ISOParams.getSeqNos() "+ISOParams.getSeqNos()+" acctType "+acctType);
                m.set(2, ISOParams.getPans()); //Primary account Number (PAN)
                m.set(3, "31" + acctType + "00"); //Processing code
                m.set(4, "0000000000");// Amount, transaction
                m.set(14, ISOParams.getExpiryDate());
                m.set(23, Utils.leftPad(ISOParams.getSeqNos(), '0', 3)); // Application PAN sequence number
                m.set(28, "D"+ Utils.leftPad(fees, '0', 8)); //Amount, transaction fee
                m.set(35, track2); // Track 2 data
                m.set(52, Utils.hexToBytes(PlainPin)); // Personal identification number data
                m.set(55, Utils.hexToBytes(fld55)); //Reserved ISO
            } catch(Exception ee){
                //logger2.info("ECHO Message exception: "+ee.getMessage());
            }
            return m;
        }else{
            Log.w("track2 q2 else----", track2);
            String used ="";
            int dIndex = track2.indexOf('=');
            pan = track2.substring(0, dIndex);

            Log.i("track2 pan", pan);
            int theDate= used.indexOf("9A03");
            System.out.println("theDate "+theDate);
            String expiryDate = track2.substring(dIndex+1, dIndex+5);
            int seqNo = used.indexOf("5F34");
            System.out.println("seqNo ---"+seqNo);
            String sequenceNo = used.substring(seqNo+6, seqNo+8);
            System.out.println("sequenceNo="+sequenceNo);
            System.out.println("PlainPin="+PlainPin);
            String pinBlock = crypt.get3DESEncryptedPinBlock(keyEncryptKey, PlainPin);
            System.out.println("pinBlock="+pinBlock);
            Log.i(TAG,"\n Balance Inquiry");

            String myDate = used.substring(theDate +4, theDate+8);
            Log.i(TAG,"The date "+myDate);
            int theStan = used.indexOf("9F410400");
            String myStan = used.substring(theStan + 9, theStan+14);
            Date date = new Date();

            try {
                m.setMTI("0200");
                m.set(2, pan); //Primary account Number (PAN)
                m.set(3, "31" + acctType + "00"); //Processing code
                m.set(4, "0000000000");// Amount, transaction
                m.set(14, expiryDate); // Date expiration

                Log.i(TAG,"isobalance...stan.." +  TestIso.getStan());
                m.set(23, Utils.leftPad(sequenceNo, '0', 3)); // Application PAN sequence number
                m.set(28, "D"+ Utils.leftPad(fees, '0', 8)); //Amount, transaction fee
                m.set(35, track2); // Track 2 data
                m.set(52, Utils.hexToBytes(pinBlock)); // Personal identification number data
                Log.i(TAG,"...55...Utils.hexToBytes(fld55)... "+fld55);
                m.set(55, Utils.hexToBytes(fld55)); //Reserved ISO
                Log.i(TAG,"ok shaaaa in balance"+TestIso.getTerminal());
            } catch(Exception ee){
                //logger2.info("ECHO Message exception: "+ee.getMessage());
            }
            stan = Utils.leftPad(myStan, '0', 6);
            panSend = pan;
            return m;
        }

    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public static String getBal(){
        return bal;
    }

    public static String getBal1(){
        return bal1;
    }
    public static String getInitialResponse(){
        return initialResponse;
    }
    public static void resetInitialResponse(){
        SharedPreferences preferences = MainActivity.context.getSharedPreferences("temp", MainActivity.context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Response", "");
        editor.commit();
    }

}
