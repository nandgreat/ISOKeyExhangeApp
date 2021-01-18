package com.mpos.newthree.security;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by HP on 7/21/2017.
 */

public class _cc {
    private  Cipher cipher=null;
    private  SecretKeySpec key=null;
    private AlgorithmParameterSpec spec;
    String encryptedText ="";
    String decryptedText="";
    private static final String SEED_16_CHARACTER = "U1MjU1M0FDOUZ.Qz";
    //private static final String SEED_16_CHARACTER = "U1MjU1M0FDOUZ.Qz";
    public static final String _0 = "accessis4life";

    public _cc() {

       try {
           // hash password with SHA-256 and crop the output to 128-bit for key
           MessageDigest digest = MessageDigest.getInstance("SHA-256");
           digest.update(SEED_16_CHARACTER.getBytes("UTF-8"));
           byte[] keyBytes = new byte[32];
           System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

           cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
           key = new SecretKeySpec(keyBytes, "AES");
           spec = getIV();
       }catch(Exception ex){}
    }

    public AlgorithmParameterSpec getIV() {
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);

        return ivParameterSpec;
    }

    public String _ee(String plainText)  {
        byte[] encrypted =null;
      try {
          cipher.init(Cipher.ENCRYPT_MODE, key, spec);
          encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
         encryptedText = new String(Base64.encode(encrypted,
                  Base64.DEFAULT), "UTF-8");

      }catch(Exception ex){}
        return encryptedText;
    }

    public String _dd(String cryptedText){
        try{

            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
            byte[] decrypted = cipher.doFinal(bytes);
            decryptedText = new String(decrypted, "UTF-8");
        }catch(Exception ex){}

        return decryptedText;
    }
}