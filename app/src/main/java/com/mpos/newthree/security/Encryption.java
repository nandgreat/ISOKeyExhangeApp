package com.mpos.newthree.security;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by TECH-PC on 5/6/2019.
 */

public class Encryption {

    public  static String _e(String unencryptedString) {
        String key="GD4A.A5723A056BC7!-F=15B46BBABD3A1";
        String base64EncryptedString = "";
        try{
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] digestOfPassword = md.digest(key.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }

            SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] plainTextBytes = unencryptedString.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            base64EncryptedString = new String(base64Bytes);


        }catch(Exception e){
            e.printStackTrace();
        }
        return base64EncryptedString;
    }

    public static String  _d(String encryptedString) {
        String key="aCCeSSCE51E06875F7AAdeEsEiNo";
        String ret="";
        try{
            if(encryptedString == null)
            {
                return "";
            }
            byte[] message = Base64.decodeBase64(encryptedString.getBytes("utf-8"));

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(key.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }

            SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");

            Cipher decipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            decipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] plainText = decipher.doFinal(message);

            ret = new String(plainText, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;

    }
}
