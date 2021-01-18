package com.mpos.newthree.dao;

import android.util.Log;

import com.mpos.newthree.iso.ISOUtil;
import com.mpos.newthree.security.CryptoUtils;
import com.mpos.newthree.security.DukptDecrypt;
import com.mpos.newthree.security.KeySerialNumber;
import com.mpos.newthree.security.SecureDESKey;
import com.mpos.newthree.security.jceadapter.JCESecurityModule;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("Duplicates")
public class Encryptor
{
    protected static CryptoUtils crypt=null;
    static String tracking;
    static String track2Decrypt;
    static InputStream theLMK;
    //public static Tab_Transaction thedata;
    private static KeySerialNumber getKSN(String s)
    {
        return new KeySerialNumber(
                s.substring(0, 6),
                s.substring(6, 10),
                s.substring(10)
        );
    }


    public static String getTrack2Decrypt(){

        String used = Data.getJustT();
        int  ksn = used.indexOf("9FAA0A");
        String theksn = used.substring(ksn+6,ksn+26);
        String tracker2 = used.substring(4,68);

        try {
            tracking = DukptDecrypt.decrypt(theksn, "0123456789ABCDEFFEDCBA9876543210", tracker2);
            if(tracking.contains("D")){
                tracking = tracking.replace('D', '=');
               // tracking.substring(0, 33);
            }
            System.out.print(tracking);
            System.out.print("Called"+ used);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.w("Track2",tracking.substring(0, 36));
                return tracking.substring(0, 36);
    }

    public static void main(String[] args) throws Exception {

        tracking = DukptDecrypt.decrypt("", "", "");

    }

    private static Key getDerivedKey(JCESecurityModule sm,
                                     SecureDESKey bdk,
                                     KeySerialNumber ksn,
                                     boolean dataEncryption) throws Exception
    {
        Method method=sm.getClass().getDeclaredMethod("calculateDerivedKey",
                KeySerialNumber.class,
                SecureDESKey.class,
                boolean.class,
                boolean.class);
        method.setAccessible(true);
        byte[] k  = (byte[])method.invoke(sm,ksn,bdk,true,true);
        return new SecretKeySpec(ISOUtil.concat(k, 0, 16, k, 0, 8), "DESede");
    }
}
//public class Encryptor