package com.mpos.newthree.wizarpos.jni;

import android.graphics.Bitmap;
import android.util.Log;

import com.mpos.newthree.activity.ActivityTransactionCompleted;
import com.mpos.newthree.activity.CardPayActivity;
import com.mpos.newthree.activity.ConstantFile;
import com.mpos.newthree.activity.MainActivity;
import com.mpos.newthree.dao.ISOMessage;
import com.mpos.newthree.utils.Utils;
import com.mpos.newthree.printer.PrinterBitmapUtil;
import com.mpos.newthree.wizarpos.emvsample.printer.PrinterCommand;
import com.mpos.newthree.wizarpos.util.StringUtil;

import java.io.UnsupportedEncodingException;

import static com.mpos.newthree.dao.ISOMessage.pan;
import static com.mpos.newthree.dao.ISOMessage.stan;


/**
 * Created by AccessTech on 11/24/2017.
 */

public class PrintSlip {


    public static String date;
    public static String tId;
    public static String amount;
    public static String transactionStatus;
    public static String address;
    public static String plateNo;
    public static String name;
    public static String phone;
    public static String payMethod;
    public static String pin;
    public static String ledgerBalance;
    public static String availableBalance;
    public static String provider;
    public static String transactionid;
    public static String ticketid="";

    public PrintSlip() {
    }

    public int open() {
        try {
            int result = PrinterInterface.open();
            if (result < 0) {
                /*callback.sendFailedMsg(mContext.getResources().getString(
                        R.string.operation_with_error)
                        + result);*/
                return result;
            } else {
                return result;
                /*isOpened = true;
                callback.sendSuccessMsg(mContext.getResources().getString(
                        R.string.operation_successful));*/
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int close() {
        //isOpened = false;
        int result = PrinterInterface.close();
        return result;
    }

    public int queryStatus() {
        int result = PrinterInterface.queryStatus();
        if (result > 0) {
            // has paper
            return result;
            //mCallback.sendSuccessMsg("PAPER_ON");
        } else if (result == 0) {
            // no paper
            return result;
            //mCallback.sendFailedMsg("PAPER_OUT");
        }
        return result;
    }

    private int begin() {
        int result = PrinterInterface.begin();
        return result;
    }

    private int end() {
        int result = PrinterInterface.end();
        return result;
    }

   /* // print line break
    public void writeLineBreak(int lineNumber) {
        // write(new byte[]{'\n'});
        write(PrinterCommand.getCmdEscDN(lineNumber));
    }*/

    private int write(final byte[] arryData) {

        int result = 0;
        if (arryData == null) {
            result = PrinterInterface.write(null, 0);
        } else {
            Log.e("DEBUG", StringUtil.toHexString(arryData, false));
            // byte [] testData = new byte[arryData.length + 10];
            // System.arraycopy(arryData, 0, testData, 10,
            // arryData.length);
            // result = PrinterInterface.write(testData, 10,
            // arryData.length);
            result = PrinterInterface.write(arryData, arryData.length);
        }
        return result;

    }

    public void printerWrite(byte[] data) {
        PrinterInterface.write(data, data.length);
    }

    public void writesVTU() throws UnsupportedEncodingException {

        begin();
        printerWrite(PrinterCommand.init());
        printerWrite(PrinterCommand.setHeatTime(180));
        printerWrite(PrinterCommand.setAlignMode(1));
        printerWrite(PrinterCommand.setFontBold(1));

        printerWrite(("102, Ogunnusi road Ojodu Berger Lagos").getBytes("GB2312"));
        printerWrite(PrinterCommand.feedLine(2));
        printerWrite(PrinterCommand.setAlignMode(0));

        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());


        printerWrite(("Transaction ID: ").getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
        printerWrite(PrinterCommand.setFont(40));
       // printerWrite((randomNum(4)+"-"+randomNum(4)+"-"+randomNum(4)).getBytes("GB2312"));
        printerWrite(PrinterCommand.feedLine(1));



        printerWrite(PrinterCommand.setFont(10));
        printerWrite(("DATE/TIME: ").getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());

        printerWrite((date).getBytes("GB2312"));
        printerWrite(PrinterCommand.feedLine(1));
        payMethod="POS";


        if (stan != null) {
            printerWrite(("STAN: ").getBytes("GB2312"));
            printerWrite((stan).getBytes("GB2312"));
            printerWrite(PrinterCommand.feedLine(1));
        }

        if (pan != null) {
            printerWrite(("CARD NO: ").getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
            printerWrite(PrinterCommand.setFont(40));
            printerWrite((Utils.maskPAN(pan)).getBytes("GB2312"));
            printerWrite(PrinterCommand.feedLine(1));
        }

        printerWrite(PrinterCommand.setFont(10));
        printerWrite(("Status: ").getBytes("GB2312"));

        printerWrite((transactionStatus).getBytes("GB2312"));
        printerWrite(PrinterCommand.feedLine(1));

        printerWrite(("VTU").getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());

        if (CardPayActivity.phone != null) {
            printerWrite(("Phone No: " + phone).getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
        }

        if ( CardPayActivity.provider != null) {
            printerWrite(("Provider: " + provider).getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
        }


        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());

      //  printerWrite(("Amount: " + MainActivity.formatCurrency(  CardPayActivity.amt)).getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());

        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());

        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(("THANK YOU! ").getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());


        end();
    }

    public void writesBill() throws UnsupportedEncodingException {

     begin();
     printerWrite(PrinterCommand.init());
     printerWrite(PrinterCommand.setHeatTime(180));
     printerWrite(PrinterCommand.setAlignMode(1));
     printerWrite(PrinterCommand.setFontBold(1));
        printerWrite(PrinterCommand.setFont(8));
//Ogun State Commercial Motor-Vehicle Administration Services
     printerWrite(("Ogun State Commercial Motor-Vehicle Administration  Services").getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());

     printerWrite(PrinterCommand.setAlignMode(0));

        payMethod="Pos";
        if(ActivityTransactionCompleted.reprint){
            transactionStatus="Transaction Successful";
        }

        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
        printerWrite(("Transaction Status: "+transactionStatus.substring(transactionStatus.indexOf(" ")+1)).getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
        try {
            if (!transactionid.equalsIgnoreCase("") || transactionid != null) {

                printerWrite(("Transaction Id: " + transactionid).getBytes("GB2312"));
                printerWrite(PrinterCommand.linefeed());
            }
        }catch(Exception ex){}
         try {
             if (transactionid.equalsIgnoreCase("") || transactionid == null) {

                 printerWrite(("Transaction Id: " + "No ID").getBytes("GB2312"));
                 printerWrite(PrinterCommand.linefeed());

             }
         }catch(Exception ec){}
       /* printerWrite(PrinterCommand.setFont(40));
     printerWrite((transactionid).getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());*/
      try {
          if(!ticketid.equalsIgnoreCase("")) {
              printerWrite(PrinterCommand.setFont(10));
              printerWrite(("Ticket: " + ticketid).getBytes("GB2312"));
              printerWrite(PrinterCommand.linefeed());
              printerWrite("--------------------------------".getBytes("GB2312"));
              printerWrite(PrinterCommand.linefeed());
          }
      }catch(Exception ex){}
        printerWrite(PrinterCommand.setFont(10));
     printerWrite(("DATE/TIME: "+date).getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());




     if (pan != null) {
     printerWrite(("CARD NO: ").getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());
     printerWrite(PrinterCommand.setFont(40));
     printerWrite((Utils.maskPAN(pan)).getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());

     }
     if(ISOMessage.terminalId!=null && ActivityTransactionCompleted.paymentMedium.equalsIgnoreCase("Card")){
        printerWrite(PrinterCommand.setFont(10));
        printerWrite(("TERMINAL ID: "+ ISOMessage.terminalId).getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());}

        printerWrite(PrinterCommand.setFont(10));
    /* Iterator it=cartList.iterator();
     while(it.hasNext()) {
         CartHelper help = (CartHelper) it.next();

         printerWrite(("Item: ").getBytes("GB2312"));
         printerWrite((help.getCartname()).getBytes("GB2312"));
         printerWrite(PrinterCommand.feedLine(1));
         printerWrite(PrinterCommand.setFont(10));
         printerWrite(("Amount: ").getBytes("GB2312"));
         printerWrite((help.getAmount()).getBytes("GB2312"));
         printerWrite(PrinterCommand.feedLine(1));

     }
*/
   /*  printerWrite(PrinterCommand.setFont(10));
     printerWrite(("Address: ").getBytes("GB2312"));
     printerWrite(("AutoPointe Nigeria").getBytes("GB2312"));
     printerWrite(PrinterCommand.feedLine(1));
*/

     if (address != null) {
     printerWrite(("Address: " + address).getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());
     }
        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
        if (ConstantFile.driver_id != null) {
            printerWrite(("Driver ID: " + ConstantFile.driver_id).getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
        }
     if (name != null) {
     printerWrite(("Name: " + name).getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());
     }


     if (plateNo != null) {
     printerWrite(("Plate No: " + plateNo).getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());
     }else{
         System.out.println("plateNo "+plateNo);
     }

        if (ConstantFile.chasis != null) {
            printerWrite(("Chasis No: " + ConstantFile.chasis).getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
        }
        if (ConstantFile.category != null) {
            printerWrite(("Category: " + ConstantFile.category).getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
        }
   //  if (offences) {
     //printerWrite(("Plate No: " + plateNo).getBytes("GB2312"));
     //printerWrite(PrinterCommand.linefeed());

    // printerWrite("--------------------------------".getBytes("GB2312"));
     //printerWrite(PrinterCommand.linefeed());
        double itemAmount=0.0;
        //itemAmount=itemAmount+Double.valueOf(i.getAmount().toString());


      /*  if(MainActivity.cartList.size()>0) {

            printerWrite(("ITEM(S)").getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
            printerWrite("--------------------------------".getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
            int count = 0;

            for (CartHelper i : MainActivity.cartList) {
                count++;
                itemAmount=itemAmount+Double.valueOf(i.getAmount().toString());
                System.out.println("cart " + i.getCartname());
                printerWrite((count + " :" + i.getCartname() + " " + MainActivity.formatCurrency(i.getAmount())).getBytes("GB2312"));
                printerWrite(PrinterCommand.linefeed());

            }
            printerWrite("--------------------------------".getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());

            printerWrite((" Item Amount: " + MainActivity.formatCurrency(String.valueOf(itemAmount))).getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
        }*/
        double partAmount=0.0;
     /*   if(MainActivity.partList.size()>0) {
            printerWrite("--------------------------------".getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
            printerWrite(("PART(S)").getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
            printerWrite("--------------------------------".getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
            int count = 0;

            for (CartHelper i : MainActivity.partList) {
                count++;
                System.out.println("part " + i.getCartname());
                partAmount=partAmount+Double.valueOf(i.getAmount().toString());
                printerWrite((count + " :" + i.getCartname() + " X"+i.getUserid()+"   " + MainActivity.formatCurrency(i.getAmount())).getBytes("GB2312"));
                printerWrite(PrinterCommand.linefeed());

            }
            printerWrite("--------------------------------".getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());

            printerWrite((" Part Amount: " + MainActivity.formatCurrency(String.valueOf(partAmount))).getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
        }
*/

     printerWrite("--------------------------------".getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());
        printerWrite(( "Amount " + MainActivity.formatCurrency(ConstantFile.amount)).getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());

    // printerWrite(("Total Amount: " + MainActivity.formatCurrency(String.valueOf(partAmount+itemAmount))).getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());
        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
        printerWrite(("Payment Mode"+"( "+payMethod+"/"+ ActivityTransactionCompleted.paymentMedium+" )").getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
     printerWrite(("Thank you! ").getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());


     end();
     }

    public void writesBalance() throws UnsupportedEncodingException {
        System.out.println("N MAIN PRINT---------------------------------------- stan"+stan+" ledgerBalance "+ledgerBalance);
        begin();
        printerWrite(PrinterCommand.init());
        printerWrite(PrinterCommand.setHeatTime(180));
        printerWrite(PrinterCommand.setAlignMode(1));
        printerWrite(PrinterCommand.setFontBold(1));

        printerWrite(("102, Ogunnusi road Ojodu Berger Lagos").getBytes("GB2312"));
        printerWrite(PrinterCommand.feedLine(2));
        printerWrite(PrinterCommand.setAlignMode(0));

        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
        printerWrite(PrinterCommand.setFont(30));
        printerWrite(("Account Inquiry").getBytes("GB2312"));
        printerWrite(PrinterCommand.feedLine(2));

        printerWrite(PrinterCommand.setAlignMode(1));
        printerWrite(PrinterCommand.setFont(10));
        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());

        printerWrite(("DATE/TIME: ").getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());

        printerWrite((date).getBytes("GB2312"));
        printerWrite(PrinterCommand.feedLine(1));



        if (stan != null) {
            printerWrite(("TRANSACTION ID: ").getBytes("GB2312"));
           // printerWrite((randomNum(12)).getBytes("GB2312"));
            printerWrite(PrinterCommand.feedLine(1));
        }

        if (pan != null) {
            printerWrite(("CARD NO: ").getBytes("GB2312"));
            printerWrite(PrinterCommand.linefeed());
            printerWrite(PrinterCommand.setFont(40));
            printerWrite((Utils.maskPAN(pan)).getBytes("GB2312"));
            printerWrite(PrinterCommand.feedLine(1));
        }

        printerWrite(PrinterCommand.setFont(10));
        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
        ledgerBalance="NGN "+ledgerBalance.substring(1);
        availableBalance="NGN "+availableBalance.substring(1);

        printerWrite(("Ledger  Balance: "+ ledgerBalance).getBytes("GB2312"));
        printerWrite(PrinterCommand.feedLine(1));
        printerWrite(("Avail  Balance: "+ availableBalance).getBytes("GB2312"));
        printerWrite(PrinterCommand.feedLine(1));

        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());

        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(("THANK YOU! ").getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
        printerWrite("         Powered By Access Solutions".getBytes("GB2312"));

        printerWrite(PrinterCommand.feedLine(2));
        printerWrite(PrinterCommand.feedLine(1));
        printerWrite("--------------------------------".getBytes("GB2312"));
        printerWrite(PrinterCommand.linefeed());
        end();
    }

    /** public void writesBank() throws UnsupportedEncodingException {
     begin();
     printerWrite(PrinterCommand.init());
     printerWrite(PrinterCommand.setHeatTime(180));
     printerWrite(PrinterCommand.setAlignMode(1));
     printerWrite(PrinterCommand.setFontBold(1));

     printerWrite(("FCT Transport Secretariat").getBytes("GB2312"));
     printerWrite(PrinterCommand.feedLine(2));
     printerWrite(PrinterCommand.setAlignMode(0));

     printerWrite("--------------------------------".getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());

     printerWrite(("Transaction ID: ").getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());

     printerWrite((BankPayActivity.TID).getBytes("GB2312"));
     printerWrite(PrinterCommand.feedLine(1));


     printerWrite(("DATE/TIME: ").getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());

     printerWrite((BankPayActivity.date).getBytes("GB2312"));
     printerWrite(PrinterCommand.feedLine(1));

     printerWrite(("PAYMENT METHOD:").getBytes("GB2312"));
     printerWrite(("BANK DEPOSIT").getBytes("GB2312"));
     printerWrite(PrinterCommand.feedLine(1));


     printerWrite(("Plate No: ").getBytes("GB2312"));
     printerWrite((BankPayActivity.plateNo).getBytes("GB2312"));
     printerWrite(PrinterCommand.feedLine(1));

     printerWrite(("Name: ").getBytes("GB2312"));
     printerWrite((BankPayActivity.name).getBytes("GB2312"));
     printerWrite(PrinterCommand.feedLine(1));

     printerWrite(("Phone No: ").getBytes("GB2312"));
     printerWrite((BankPayActivity.phone).getBytes("GB2312"));
     printerWrite(PrinterCommand.feedLine(1));

     printerWrite("--------------------------------".getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());
     printerWrite(("OFFENCE(S)").getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());
     printerWrite("--------------------------------".getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());

     for (GetJSONResponse.Content.SubMenu.Item i : MainActivity.cartList) {
     printerWrite((i.getItemtype_name()).getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());
     printerWrite((MainActivity.formatCurrency(i.getPayitem_amt())).getBytes("GB2312"));
     printerWrite(PrinterCommand.feedLine(1));
     }

     printerWrite("--------------------------------".getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());

     printerWrite(("Amount: " + MainActivity.formatCurrency(BankPayActivity.amount)).getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());

     printerWrite("--------------------------------".getBytes("GB2312"));

     printerWrite(("NOTE: This is not a payment receipt kindly take this to any of these nearest bank: EcoBank," +
     " Heritage Bank,,, or go to DRTS and make us eof any of the payment platforms").getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());

     printerWrite(("THANK YOU! ").getBytes("GB2312"));
     printerWrite(PrinterCommand.linefeed());


     printerWrite(PrinterCommand.feedLine(1));
     end();
     }
     **/
    public void writesBitmap(Bitmap bitmap) throws UnsupportedEncodingException {
        begin();
        PrinterBitmapUtil.printBitmap(bitmap, 0, 0, true);
        end();
    }

}
