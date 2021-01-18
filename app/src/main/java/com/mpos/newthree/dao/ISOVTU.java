/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpos.newthree.dao;

import android.content.Context;

import com.mpos.newthree.iso.ISOException;
import com.mpos.newthree.iso.ISOMsg;
import com.mpos.newthree.utils.Utils;
import com.mpos.newthree.wizarpos.emvsample.parameter.ISOParams;

import java.io.IOException;
import java.util.Date;

import static com.mpos.newthree.dao.TestIso.getStan;

public class ISOVTU extends ISOMessage {
    static String initialResponse;
    static String bal, bal1;
    private Context ctx;
    public ISOVTU(Context ctx){
        this.ctx = ctx;
    }
    @Override
    public ISOResponse doTransaction(String track2, String PlainPin, String parameter, String fld55, String amount, String fees, String acctType) {
        //String responseMsg = "Connection Timed Out";
        //String[] response = new String[7];
        System.out.println("doTransaction one");
        String bal = "0.00";
        String bal1 = "0.00";

        String[] balanceArray = {"0", "0"};
        //String initialResponse = "98";
        ISOResponse isoResp = new ISOResponse();
        try {            
            connectHost();
            getKey();
            System.out.println("amount============"+amount+"  fees="+fees+" acctType"+acctType+"  track2 "+track2);
            request = setTransactionParams(track2, PlainPin, parameter, fld55, amount, fees, acctType);
            System.out.println("After setting trans params" +track2);
            System.out.println("request="+request);
            ISOMsg resp = sendTransaction(request);
            System.out.println("After sending trans");
            initialResponse = resp.getString(39);
            System.out.print("Field 39"+initialResponse);
            System.out.println("Response 1==>\n" +resp.getString(39));
            if(initialResponse.equals("00") && resp.getString(54) != null){          
                try {
                    balanceArray = Utils.parseBalance(resp.getString(54));
                    bal = nf.format(Double.parseDouble(balanceArray[0])/100);
                    bal1=nf.format(Double.parseDouble(balanceArray[1])/100);
                    balanceArray[0]=bal;
                    balanceArray[1]=bal1;
                    System.out.println("The response 22222==>" +initialResponse);
                } catch (Exception e) {
                    balanceArray[0]="";
                    balanceArray[1]=""; 
                }
            }
            if (initialResponse.equals("63")) {
                KeepConnection.requestKey = true;
            }
           /* if (initialResponse.equals("25")) {             // reverse if resp == 25
                //reversed = true;
                request.unset(22);
                //request.unset(48);                
                request.unset(123);
                
                request.set(90, formReversalMessage(request));
                request.setMTI("0420");
                resp = sendTransaction(request);
               
            }  
            */
                        
            isoResp.setResponseCode(initialResponse);      
            isoResp.setResponse48((resp.hasField(48)) ? resp.getString(48) : "");
            isoResp.setLedgerBalance(balanceArray[0]);
            isoResp.setAvailableBalance(balanceArray[1]);                       
                        
        } catch (NullPointerException ex) {
            isoResp.setResponseCode("99");
          //  logger2.info("Null Exception: "+ex.getMessage());
            
        } catch (ISOException ex) {
            isoResp.setResponseCode("99");
          //  logger2.info("ISOException: "+ex.getMessage());
        } catch(java.net.SocketTimeoutException e) {
            isoResp.setResponseCode("99");
            System.out.println("Timed out");
            //initialResponse = "99";
            sendReversal(request);
            isoResp.setReversed(true);
        } catch (IOException ex) {
           // logger2.info("IOException: "+ex.getMessage());
            //MainFrame.showOutOfService();
            isoResp.setResponseCode("99");
        } catch (Exception ex) {
          //  logger2.info("Exception: "+ex.getMessage());
            //ex.printStackTrace();
            isoResp.setResponseCode("99");
        }
        
        isoResp.setPan(track2.substring(0, track2.indexOf('=')));
        isoResp.setStan(request.getString(11) == null ? "" : request.getString(11));
        isoResp.setAmount(nf.format(Double.parseDouble(amount)/100));               
        
        return isoResp;
    }
    
    @Override
    public ISOMsg setTransactionParams(String track2, String PlainPin, String parameter, String fld55, String amount, String fees, String acctType){

        int dIndex = track2.indexOf('=');
        String pan = track2.substring(0, dIndex);
        String expiryDate = track2.substring(dIndex + 1, dIndex + 5);
        int seqNo = fld55.indexOf("5F34");
        String sequenceNo = fld55.substring(seqNo + 6, seqNo + 8);
        int theDate= fld55.indexOf("9A03");
        String myDate = fld55.substring(theDate +4, theDate+8);
        System.out.println("The date "+myDate);
        int theStan = fld55.indexOf("9F410400");
        String myStan = fld55.substring(theStan + 9, theStan+14);
        System.out.println("My Stan" + myStan+ "acctType "+acctType);
        String pinBlock = crypt.get3DESEncryptedPinBlock(keyEncryptKey, PlainPin);
        System.out.println("just VTU");
        Date date = new Date();
        //String stan = getNextTransId();
        try{

            System.out.println("PlainPin ="+PlainPin);



            m.setMTI("0200");
            m.set(2, ISOParams.getPans()); //Primary account Number (PAN)
            m.set(3, "53" + acctType + "00"); //Processing code
            m.set(4, Utils.leftPad(amount, '0', 10));// Amount, transaction
            m.set(7, curDateTime.format(date)); //Transmission date and time
            m.set(11, getStan());  // System trace audit number (STAN)
            m.set(12, curTime.format(date)); // Time, local transaction (hhmmss)
            m.set(13, curDate.format(date)); // Date, local transaction (MMDD)
            m.set(14, ISOParams.getExpiryDate()); // Date expiration
            m.set(15, curDate.format(date)); // Date, settlement
            m.set(18, "6012"); // Merchant type
            m.set(22, "901"); // Point of service entry mode
            m.set(23, Utils.leftPad(ISOParams.getSeqNos(), '0', 3)); // Application PAN sequence number
            m.set(25, "00"); //Point of service condition code
            m.set(26, "12"); //Point of service condition code
            m.set(28, "D"+ Utils.leftPad(fees, '0', 8)); //Amount, transaction fee
            m.set(30, "C00000000"); //Amount, transaction processing fee
            //m.set(32, "62774970"); //Acquiring institution identification code
            m.set(32, "627000"); //Acquiring institution identification code
            m.set(33, "111111"); // Forwarding institution identification code
            m.set(35, track2); // Track 2 data
            //m.set(37, Utils.leftPad(getStan(), '0', 6)); // Retrieval reference number
            m.set(37, Utils.leftPad(getStan(), '0', 12)); // Retrieval reference number
            m.set(41, TestIso.getTerminal()); // Card acceptor terminal identification
            //m.set(42, "2329000024     ");// card acceptor terminal identification
            m.set(42, "2329000024");// card acceptor terminal identification
            m.set(43, "PLOT 1161,Mem Drive,CBDABUJA        ABNG");// Card acceptor name/location (1-23 address 24-36 city 37-38 state 39-40 country
            m.set(48, parameter);//Additional data
            m.set(49, "566");// Currency code

            m.set(52, Utils.hexToBytes(PlainPin)); // Personal identification number data
            // m.set(52, Utils.hexToBytes(pinBlock));
            //String _55 ="4F07A0000000041010820238005F3401009A031710319C01315F2A0205669F02060000000000009F03060000000000009F1A0205665F2A0205669F2701809F360201D59F2608DE43D9F5B129F8069F10120110A00003220000000000000000000000FF9F3501229F37047C2F64B89F330360E8C8950508800400009F3403410300";

            m.set(55, Utils.hexToBytes(fld55)); //Reserved ISO
            m.set(56, "1510");//Reserved ISO
            m.set(67, "03");//Extended payment code
            m.set(123, "211201513344002");//Reserved for private use









        } catch(Exception ee) {
         // logger2.info("ECHO Message exception: "+ee.getMessage());
        }

        return m;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static String getInitialResponse(){
        return initialResponse;
    }
    public static String getBal(){
        return bal;
    }
}
