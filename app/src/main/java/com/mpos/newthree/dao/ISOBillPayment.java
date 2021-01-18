/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpos.newthree.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mpos.newthree.activity.MainActivity;
import com.mpos.newthree.iso.ISOException;
import com.mpos.newthree.iso.ISOMsg;
import com.mpos.newthree.utils.Utils;
import com.mpos.newthree.wizarpos.emvsample.parameter.ISOParams;

import java.io.IOException;

import static com.mpos.newthree.activity.MainActivity.Q2;


public class ISOBillPayment extends ISOMessage {
    private Context ctx;
    private static String contact;
    private static String PIN;
    static String bal, bal1;
    public static int transID;
    static String initialResponse;
   /* public ISOBillPayment(Context ctx, String contact){
        this.contact = contact;
        this.ctx = ctx;
    }*/
    @Override
    public ISOResponse doTransaction(String track2, String PlainPin, String parameter, String fld55, String amount, String fees, String acctType) {
        //String bal = "Connection Timed Out";
        stan = TestIso.getStan(transID);
        System.out.println("PlainPin"+PlainPin);
        System.out.println("track2 2  "+track2+" - \n");
        System.out.println("acctType "+acctType);
        String bal = "0.00";
        String bal1 = "0.00";
        String[] balanceArray = {"0", "0"};
        //String responseCode = "98";
        //String[] response = new String[10];
        ISOResponse isoResp = new ISOResponse();
        try {
            connectHost();
            getKey();
            Log.d("I am here4","");
            request = setTransactionParams(track2, PlainPin, parameter, fld55, amount, fees, acctType);
            ISOMsg resp = sendTransaction(request);
            //initialResponse = resp.getString(39);

            SharedPreferences preferences = MainActivity.context.getSharedPreferences("temp", MainActivity.context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Response", resp.getString(39));
            editor.commit();

            //System.out.println("The response 1==>" +initialResponse);

            /*
            if (initialResponse.equals("25")) {             // reverse if resp == 25
                //reversed = true;
                request.unset(22);
                //request.unset(48);
                request.unset(123);
                //
                request.set(90, formReversalMessage(request));
                request.setMTI("0420");
                isoResp = keepAlive(request);

            }
            */
            PIN = null;
            if(resp.hasField(125) &&  resp.getString(125) != null){
                PIN = resp.getString(125);
            }
/*
                if (initialResponse.equals("00") && resp.getString(54) != null) {
                    try {
                        balanceArray = Utils.parseBalance(resp.getString(54));
                        bal = nf.format(Double.parseDouble(balanceArray[0]) / 100);
                        bal1 = nf.format(Double.parseDouble(balanceArray[1]) / 100);
                        balanceArray[0] = bal;
                        balanceArray[1] = bal1;
                        System.out.println("The response 2==>" +initialResponse);
                    } catch (Exception e) {
                        balanceArray[0] = "";
                        balanceArray[1] = "";
                    }
                }*/

            isoResp.setResponseCode(resp.getString(39));
            isoResp.setResponse48((resp.hasField(48)) ? resp.getString(48) : "");

            if (request.getString(48).startsWith("PHCN")) {
                isoResp.setResponse125Pin((resp.getString(125) != null) ? resp.getString(125) : "");
                isoResp.setResponse100Name((resp.getString(100) != null) ? resp.getString(100) : "");
                isoResp.setResponse62Address((resp.getString(62) != null) ? resp.getString(62) : "");
                isoResp.setResponse126CustomerInfo((resp.getString(126) != null) ? resp.getString(126) : "");
            }

            if (resp.getString(39).equals("63")) {
                KeepConnection.requestKey = true;
            }
            MainActivity.timeout = false;

        } catch (ISOException ex) {
            isoResp.setResponseCode("99");
            System.out.println("Another Challenge"+ex);
            ex.printStackTrace();
            // logger2.info("ISOException:  "+ex.getMessage());
        } catch (java.net.SocketTimeoutException e) {
            MainActivity.timeout = true;
            System.out.println("Timed out");
            isoResp.setResponseCode("99");
            isoResp.setReversed(true);

            //initialResponse = "99";
            sendReversal(request);
        } catch (IOException ex) {
            //  logger2.info("IOExecption:  "+ex.getMessage());
            //MainFrame.showOutOfService();
            isoResp.setResponseCode("99");
            System.out.println("Another Challenge 2"+ex);
            ex.printStackTrace();
        }

        isoResp.setPan(track2.substring(0, track2.indexOf('=')));
        isoResp.setStan(request.getString(11) == null ? "" : request.getString(11));
        isoResp.setAmount(nf.format(Double.parseDouble(amount) / 100));

        return isoResp;

    }
        @Override
        public ISOMsg setTransactionParams(String track2, String PlainPin, String parameter, String fld55, String amount, String fees, String acctType) {
            if(Q2){
                System.out.println("in Q2");
                System.out.println("PlainPin 22222 "+PlainPin);
                System.out.println("acctType "+acctType);
                int dIndex = track2.indexOf('=');
                pan = track2.substring(0, dIndex);
                String expiryDate = track2.substring(dIndex + 1, dIndex + 5);
                try {
                    m.setMTI("0200");
                    m.set(2,pan);
                    m.set(3, "54"+acctType+"00");
                    m.set(4, Utils.leftPad(amount, '0', 10));
                    m.set(14, expiryDate);
                    m.set(23, Utils.leftPad(ISOParams.getSeqNos(), '0', 3));
                    m.set(28, "D"+ Utils.leftPad(fees, '0', 8));
                    m.set(35, track2);
                    m.set(48, parameter);
                    m.set(52, Utils.hexToBytes(PlainPin));
                    m.set(55, Utils.hexToBytes(fld55));
                    //m.set(103, name);
                } catch (Exception ee) {
                    Log.d("ECHO Message: ",ee.toString());
                    ee.printStackTrace();
                }

                //Log to sever here
                return m;
            }else{
                int dIndex = track2.indexOf('=');
                pan = track2.substring(0, dIndex);
                String expiryDate = track2.substring(dIndex + 1, dIndex + 5);
                int seqNo = fld55.indexOf("5F34");
                String sequenceNo = fld55.substring(seqNo + 6, seqNo + 8);
                int theDate= fld55.indexOf("9A03");
                String myDate = fld55.substring(theDate +4, theDate+8);
                System.out.println("The date "+myDate);
                int theStan = fld55.indexOf("9F410400");
                String myStan = fld55.substring(theStan + 9, theStan+14);
                System.out.println("My Stan..." + myStan +"..."+ theStan);

                System.out.println("Track2 ="+track2+ "pan ="+pan +"i do not know"+ seqNo + "\n  field 55==>"+ Utils.hexToBytes(fld55) + "\n account type"+acctType + "expiry date" +expiryDate);
                String pinBlock = crypt.get3DESEncryptedPinBlock(keyEncryptKey, PlainPin);

                System.out.println("Fees is " + fees);
                System.out.println("Bill Payment");
                try {

                    m.setMTI("0200");
                    m.set(2,pan);
                    m.set(3, "54"+acctType+"00");
                    m.set(4, Utils.leftPad(amount, '0', 10));
                    m.set(14, expiryDate);
                    m.set(23, Utils.leftPad(sequenceNo, '0', 3));
                    m.set(28, "D"+ Utils.leftPad(fees, '0', 8));
                    m.set(35, track2);
                    m.set(48, parameter);
                    m.set(52, Utils.hexToBytes(pinBlock));
                    m.set(55, Utils.hexToBytes(fld55));
                } catch (Exception ee) {
                    Log.d("ECHO Message: ",ee.toString());
                    ee.printStackTrace();
                }
                return m;
            }

        }

        @Override
        public void run() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    public static String getBal1(){
        return bal1;
    }

    public static String getInitialResponse(){
        SharedPreferences preferences=MainActivity.context.getSharedPreferences("temp", MainActivity.context.MODE_PRIVATE);

        return preferences.getString("Response",null);
    }
}
