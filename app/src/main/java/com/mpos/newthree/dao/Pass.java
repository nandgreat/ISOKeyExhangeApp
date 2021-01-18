package com.mpos.newthree.dao;

import android.util.Base64;

import java.io.ByteArrayOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by TECH-PC on 3/26/2018.
 */


public class Pass {
    //private static SecretKey code;
    private static byte[] key = "12345678".getBytes();// 64 bit
    private static byte[] iv = "12345678".getBytes();
    public Pass(){
        /*
        try {
            byte[] secretBytes = "ONEPAY".getBytes("UTF8");
            DESKeySpec keySpec = new DESKeySpec(secretBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            code = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    public static String encryptPassword(String in) {
        String cypert = in;
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec k = new SecretKeySpec(key, "DES");
            Cipher c = Cipher.getInstance("DES/CBC/PKCS7Padding");
            c.init(Cipher.ENCRYPT_MODE, k, ivSpec);
            byte[] encryptedData = c.doFinal(in.getBytes());
            cypert = Base64.encodeToString(encryptedData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cypert;
    }


    public static String decryptPassword(String in) throws Exception {
        String plain=in;
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec keys = new SecretKeySpec(key, "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, keys, ivSpec);
            // decryption pass
            byte[] cipherText = Base64.decode(in, Base64.DEFAULT);
            int ctLength = cipherText.length;
            byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(cipher.doFinal(cipherText));
            plainText = bos.toByteArray();
            bos.close();
            plain = new String(plainText, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plain;
    }

    /*
    public byte[] encryptPassword(String userpw) {
        try {
            byte[] cleartext = userpw.getBytes("UTF8");

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, code);
            byte[] clearBytes = cipher.doFinal(cleartext);
            byte[] encryptedPwd = Base64.encodeBase64(clearBytes);
            return encryptedPwd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptPassword(byte[] userpw) {
        String pw = "";
        try {
            byte[] encrypedPwdBytes = Base64.decodeBase64(userpw);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, code);
            byte[] plainTextPwdBytes = cipher.doFinal(encrypedPwdBytes);
            pw = new String(plainTextPwdBytes, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pw;
    }
    */
}
