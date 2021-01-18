package com.mpos.newthree.dao;

/**
 * Created by HP on 9/21/2016.
 */
//import com.accessng.onepaylite.fragment.Tab_Transaction;
import com.mpos.newthree.security.DukptDecrypt2;

import java.io.InputStream;

public class PinDecryptor
{
    static String plainPinBlock;
    static String track2Decrypt;
    static InputStream theLMK;
    //public static Tab_Transaction thedata;



    public static String getPinBlockDecrypt(){
        String used = Data.getJustT();
        int  ksn = used.indexOf("9FAA0A");
        System.out.println("ksn number="+ksn);
       // ksn=106;
        String theksn = used.substring(ksn+6,ksn+26);
        System.out.println(" the ksn="+theksn);

        int iPinBlock = used.indexOf("9FA710");
        System.out.println("iPinBlock main ="+iPinBlock);
//129E4390A0E1AA0439E9
        String ePinBlock = used.substring(iPinBlock+6, iPinBlock+38);
       // ePinBlock="129E4390A0E1AA0439E979BF2A317DD2";
        System.out.println("iPinBlock ="+iPinBlock);
        System.out.println("ePinBlock="+ePinBlock);
        try {
            plainPinBlock= DukptDecrypt2.decrypt(theksn, "0123456789ABCDEFFEDCBA9876543210", ePinBlock);
            //plainPinBlock= DukptDecrypt2.decrypt(theksn, "122334455667A1BC122334455667A1BC", ePinBlock);
plainPinBlock="04005AADFE8BDF8C0000000000000000";
            System.out.print("\nThe Decrypted PINBLOCK" +plainPinBlock);
            System.out.print("\nCalled"+ used);
        }catch (Exception e){
            System.out.print(e);}
        return plainPinBlock.substring(0, 16);
    }
}
