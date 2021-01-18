/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpos.newthree.security;

/**
 *
 * @author samycorps
 */

import java.util.Arrays;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 *
 * @author Crownus
 */
public class CryptoUtils {

    public CryptoUtils() {
    }
/*
    public String get3DESEncryptedPin(String key, String pinBlock) {
        String encryptedPin = null;
        try {
            encryptedPin = do3DESEncryption(key, pinBlock);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return encryptedPin;
    }
*/
    /**
	 * The key data for DESede
	 */
	public static byte[] DES3_KEY = new byte[] { (byte)0xF6, (byte)0xDB, 
		     (byte)0x31, (byte)0x01, (byte)0x7B, (byte)0x39, (byte)0x0C, 
		     (byte)0x26, (byte)0xF6, (byte)0xDB, (byte)0x31, (byte)0x01, 
		     (byte)0x7B, (byte)0x39, (byte)0x0C, (byte)0x26 };
	/**
	 * The input data for DESede encrypt
	 */
	static final byte[] DES3_INPUT_DATA = new byte[] { 
		   (byte)0x0f, (byte)0x0d, (byte)0xd1, (byte)0x02, (byte)0x13, 
		   (byte)0xc1, (byte)0x3b, (byte)0x8a, (byte)0xd0, (byte)0x4a, 
		   (byte)0x67, (byte)0xff, (byte)0x00, (byte)0xca, (byte)0xe5, 
		   (byte)0xb6, (byte)0x26, (byte)0x68, (byte)0x02, (byte)0xca, 
		   (byte)0xff, (byte)0xb9, (byte)0x55, (byte)0x8f, (byte)0x7d, 
		   (byte)0x41, (byte)0xbd, (byte)0x06, (byte)0x20, (byte)0xe2, 
		   (byte)0x53, (byte)0xb2, (byte)0x7c, (byte)0x95, (byte)0x8a, 
		   (byte)0xf8, (byte)0xfa, (byte)0xb5, (byte)0xa6, (byte)0x31, 
		   (byte)0xb3, (byte)0xc3, (byte)0x84, (byte)0x22, (byte)0x24, 
		   (byte)0x71, (byte)0x07, (byte)0xc8, (byte)0x52, (byte)0xf9, 
		   (byte)0x29, (byte)0x72, (byte)0x0c, (byte)0xe7, (byte)0xba, 
		   (byte)0x8d, (byte)0x95, (byte)0x3c, (byte)0xda, (byte)0xd8, 
		   (byte)0x4e, (byte)0x12, (byte)0xdf, (byte)0xdd, (byte)0x9d, 
		   (byte)0xb4, (byte)0x15, (byte)0xdb, (byte)0x93, (byte)0xc2, 
		   (byte)0x82, (byte)0x47, (byte)0x32, (byte)0x53, (byte)0xb1, 
		   (byte)0xb6, (byte)0x8b, (byte)0xce, (byte)0x66, (byte)0x7d, 
		   (byte)0x72, (byte)0x38, (byte)0x42, (byte)0x1c, (byte)0x8f, 
		   (byte)0x4a, (byte)0x4a, (byte)0xf2, (byte)0xe2, (byte)0x50, 
		   (byte)0xa7, (byte)0xf6, (byte)0x26, (byte)0xdd, (byte)0x18, 
		   (byte)0x67, (byte)0x5a, (byte)0x2b, (byte)0xfe, (byte)0x15, 
		   (byte)0x93, (byte)0x47, (byte)0xc0, (byte)0xe3, (byte)0x88, 
		   (byte)0x08, (byte)0xf5, (byte)0xf3, (byte)0x4b, (byte)0xbb, 
		   (byte)0xa6, (byte)0xbc, (byte)0xd4, (byte)0x65, (byte)0x8c, 
		   (byte)0x8a, (byte)0x86, (byte)0x53, (byte)0x17, (byte)0x69, 
		   (byte)0xa6, (byte)0x13, (byte)0x68, (byte)0x2e, (byte)0xf2, 
		   (byte)0xfe, (byte)0x6b, (byte)0x5e };
        
        
    public String get3DESEncryptedPinBlock(byte[] theKey, char[] pin, String pan) {
        
        char[] pinBlock = getPinBlock(pin, pan);
        
        return do3DESEncryption(theKey, new String(pinBlock));
    }
    
    public String get3DESEncryptedPinBlock(String key, String PlainPin) {
        
       // char[] pinBlock = getPinBlock(pin, pan);
        System.out.print("\nEncrypted PINBLOCK"+ PlainPin);
        
        return do3DESEncryption(key, PlainPin);
    }
    
    public String get3DESEncryptedPin(byte[] theKey, String pinBlock) {
        String encryptedPin = null;
        try {
            encryptedPin = do3DESEncryption(theKey, pinBlock);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return encryptedPin;
    }
    
    public String getClearPin(String key, String encryptedPinBlock, String pan){
        String pinBlock = null;
        int len = pan.length();
        String pin = null;
        try {
            pan = "0000" + pan.substring(len - 13, len - 1);
            pinBlock = do3DESDecryption(key, encryptedPinBlock);
            pin = xor(pinBlock, pan).substring(2, 6);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return pin;
    }

    public String getDESEncryptedPin(String key, String pinBlock) {
        String encryptedPin = null;
        try {
            encryptedPin = doDESEncryption(key, pinBlock);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        return encryptedPin;
    }

    public char[] getPinBlock(char[] pin, String pan) {//ISO-0
        char[] pinBlock = new char[16];
        int len = pan.length();
        char[] paddedPin = new char[16];
        try {
            paddedPin[0] = '0';
            paddedPin[1] = String.valueOf(pin.length).charAt(0);
            System.arraycopy(pin, 0, paddedPin, 2, pin.length);
            //pin = "0" + pin.length + rightPad(pin, 'F', 14);
            Arrays.fill(paddedPin, pin.length+2, 16, 'F');
            pan = "0000" + pan.substring(len - 13, len - 1);
            
            pinBlock = xor(paddedPin, pan);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return pinBlock;
    }

    public String do3DESEncryption(String key, String text) {
        String encryptedInfo = null;
        try {
            String key1 = key.substring(0, 16);
            String key2 = key.substring(16);
            encryptedInfo = doDESEncryption(key1, text);
            encryptedInfo = doDESDecryption(key2, encryptedInfo);
            encryptedInfo = doDESEncryption(key1, encryptedInfo);
        } catch (Exception ex) {
            System.out.println("do3DESEncryption error message"+ex.getMessage());
            ex.printStackTrace();
        }

        return encryptedInfo;
    }

    public String do3DESEncryption(byte[] theKey, String text) {
        String encryptedInfo = null;
        byte[] theKey1 = new byte[8];
        byte[] theKey2 = new byte[8];
        
        try {
            System.arraycopy(theKey, 0, theKey1, 0, 8);
            System.arraycopy(theKey, 8, theKey2, 0, 8);
            //String key1 = key.substring(0, 16);
            //String key2 = key.substring(16);
            encryptedInfo = doDESEncryption(theKey1, text);
            encryptedInfo = doDESDecryption(theKey2, encryptedInfo);
            encryptedInfo = doDESEncryption(theKey1, encryptedInfo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return encryptedInfo;
    }
    
    public String do3DESEncryption(byte[] theKey, byte[] theText) {
        String encryptedInfo = null;
        byte[] theKey1 = new byte[8];
        byte[] theKey2 = new byte[8];
        
        try {
            System.arraycopy(theKey, 0, theKey1, 0, 8);
            System.arraycopy(theKey, 8, theKey2, 0, 8);
            //String key1 = key.substring(0, 16);
            //String key2 = key.substring(16);
            encryptedInfo = doDESEncryption(theKey1, theText);
            encryptedInfo = doDESDecryption(theKey2, encryptedInfo);
            encryptedInfo = doDESEncryption(theKey1, encryptedInfo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return encryptedInfo;
    }
    
    public String do3DESDecryption(String key, String text) {
        String decryptedInfo = null;
        try {
            String key1 = key.substring(0, 16);
            String key2 = key.substring(16);
            decryptedInfo = doDESDecryption(key1, text);
            decryptedInfo = doDESEncryption(key2, decryptedInfo);
            decryptedInfo = doDESDecryption(key1, decryptedInfo);
            System.out.print("The Key do3DESDecryption   "+ decryptedInfo +" key1 "+key1+" text "+text +" key2 "+key2);
        } catch (Exception ex) {
            System.out.println("What's happening"+ex.getMessage());
            ex.printStackTrace();
        }

        return decryptedInfo;
    }

    public String do3DESDecryption(byte[] theKey, String text) {
        String decryptedInfo = null;
        byte[] theKey1 = new byte[8];
        byte[] theKey2 = new byte[8];
        
        try {
            System.arraycopy(theKey, 0, theKey1, 0, 8);
            System.arraycopy(theKey, 8, theKey2, 0, 8);
            //String key1 = key.substring(0, 16);
            //String key2 = key.substring(16);
            decryptedInfo = doDESDecryption(theKey1, text);
            decryptedInfo = doDESEncryption(theKey2, decryptedInfo);
            decryptedInfo = doDESDecryption(theKey1, decryptedInfo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return decryptedInfo;
    }
    
    public String do3DESDecryption(byte[] theKey, byte[] text) {
        String decryptedInfo = null;
        byte[] theKey1 = new byte[8];
        byte[] theKey2 = new byte[8];
        
        try {
            System.arraycopy(theKey, 0, theKey1, 0, 8);
            System.arraycopy(theKey, 8, theKey2, 0, 8);
            //String key1 = key.substring(0, 16);
            //String key2 = key.substring(16);
            decryptedInfo = doDESDecryption(theKey1, text);
            decryptedInfo = doDESEncryption(theKey2, decryptedInfo);
            decryptedInfo = doDESDecryption(theKey1, decryptedInfo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return decryptedInfo;
    }
    
    public String doDESEncryption(String key, String text) {
        String encryptedInfo = "";
        try {
            byte[] theCph = null;
            byte[] theKey = null;
            byte[] theMsg = null;
            theKey = hexToBytes(key);
            theMsg = hexToBytes(text);
            DESKeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
            cf.init(Cipher.ENCRYPT_MODE, ky);
            theCph = cf.doFinal(theMsg);
            encryptedInfo = bytesToHex(theCph);
        } catch (Exception e) {
            System.out.println("Encryption DES"+e.getMessage());
            e.printStackTrace();
        }
        return encryptedInfo;
    }

    public String doDESEncryption(byte[] theKey, String text) {
        String encryptedInfo = "";
        try {
            byte[] theCph = null;
            //byte[] theKey = null;
            byte[] theMsg = null;
            //theKey = hexToBytes(key);
            theMsg = hexToBytes(text);
            DESKeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
            cf.init(Cipher.ENCRYPT_MODE, ky);
            theCph = cf.doFinal(theMsg);
            encryptedInfo = bytesToHex(theCph);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return encryptedInfo;
    }
    
    public String doDESEncryption(byte[] theKey, byte[] theMsg) {
        String encryptedInfo = "";
        try {
            byte[] theCph = null;
            //byte[] theKey = null;
            //byte[] theMsg = null;
            //theKey = hexToBytes(key);
            //theMsg = hexToBytes(text);
            DESKeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
            cf.init(Cipher.ENCRYPT_MODE, ky);
            theCph = cf.doFinal(theMsg);
            encryptedInfo = bytesToHex(theCph);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return encryptedInfo;
    }
    
    public String doDESDecryption(String key, String text) {
        String decryptedInfo = "";
        try {
            byte[] theKey = null;
            byte[] theMsg = null;
            byte[] theCph = null;
            theKey = hexToBytes(key);
            theMsg = hexToBytes(text);
            DESKeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
            cf.init(Cipher.DECRYPT_MODE, ky);
            theCph = cf.doFinal(theMsg);
            decryptedInfo = bytesToHex(theCph);
        } catch (Exception e) {
            System.out.println("Now confused by this =>"+e.getMessage());
            e.printStackTrace();
        }
        return decryptedInfo;
    }
    
    public String doDESDecryption(byte[] theKey, String text) {
        String decryptedInfo = "";
        try {
           // byte[] theKey = null;
            byte[] theMsg = null;
            byte[] theCph = null;
            //theKey = hexToBytes(key);
            theMsg = hexToBytes(text);
            DESKeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
            cf.init(Cipher.DECRYPT_MODE, ky);
            theCph = cf.doFinal(theMsg);
            decryptedInfo = bytesToHex(theCph);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return decryptedInfo;
    }
    
    public String doDESDecryption(byte[] theKey, byte[] theMsg) {
        String decryptedInfo = "";
        try {
           // byte[] theKey = null;
            //byte[] theMsg = null;
            byte[] theCph = null;
            //theKey = hexToBytes(key);
            //theMsg = hexToBytes(text);
            DESKeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
            cf.init(Cipher.DECRYPT_MODE, ky);
            theCph = cf.doFinal(theMsg);
            decryptedInfo = bytesToHex(theCph);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return decryptedInfo;
    }
    
    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        } else {
            int len = data.length;
            String str = "";
            for (int i = 0; i < len; i++) {
                if ((data[i] & 0xFF) < 16) {
                    str = str + "0" + Integer.toHexString(data[i] & 0xFF);
                } else {
                    str = str + Integer.toHexString(data[i] & 0xFF);
                }
            }
            return str.toUpperCase();
        }
    }

     public static String leftPad(String data, String padd, int len){
        String padddata = data;
        int datalen = data.length();

        while((len - datalen) != 0){

            padddata = padd + padddata;
            datalen = padddata.length();
        }
        return padddata;
    }
     
    public static char[] xor(char[] cpin, String pan) {
        if (pan == null) {
            return null;
        } else {
            int len = pan.length();
            char[] cpan = pan.toCharArray();
            //char[] cpin = pin.toCharArray();
           // String buffer = "";    
            char[] cbuf = new char[16];
            for (int i = 0; i < len; i++) {
                //int val_xor = (int) Integer.parseInt(cpan[i] + "", 16) ^ Integer.parseInt(cpin[i] + "", 16);
                //buffer += (Integer.toHexString(val_xor)).replace("0x", ""); 
                int xorVal = Character.digit(cpan[i], 16) ^ Character.digit(cpin[i], 16);
                cbuf[i] = Character.forDigit(xorVal, 16);
            }
            return cbuf;
        }
    }

    public static String xor(String pin, String pan) {
        if (pan == null) {
            return null;
        } else {
            int len = pan.length();
            char[] cpan = pan.toCharArray();
            char[] cpin = pin.toCharArray();
            String buffer = "";
            for (int i = 0; i < len; i++) {
                int val_xor = (int) Integer.parseInt(cpan[i] + "", 16) ^ Integer.parseInt(cpin[i] + "", 16);
                buffer += (Integer.toHexString(val_xor)).replace("0x", "");
            }
            return buffer;
        }
    }
    
    public static String unsalt(String pin, String pan) {
        if (pan == null) {
            return null;
        } else {
            int len = pan.length();
            char[] cpan = pan.toCharArray();
            char[] cpin = pin.toCharArray();
            String buffer = "";
            for (int i = 0; i < len; i++) {
                int val_xor = (int) Integer.parseInt(cpan[i] + "", 16) ^ Integer.parseInt(cpin[i] + "", 16);
                buffer += (Integer.toHexString(val_xor)).replace("0x", "");
            }
            return buffer;
        }
    }

    public String generateKey(){
        String keydata = "";
        try {
	// Generate a DES key
	KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        SecretKey key = keyGen.generateKey();
        System.out.println("DES Key: "+bytesToHex(key.getEncoded()));
	// Generate a Blowfish key
	keyGen = KeyGenerator.getInstance("Blowfish");
        key = keyGen.generateKey();
        System.out.println("Blowfish Key: "+bytesToHex(key.getEncoded()));
	// Generate a triple DES key
	keyGen = KeyGenerator.getInstance("DESede");
	key = keyGen.generateKey();
        
        System.out.println("DESede Key: "+bytesToHex(key.getEncoded()));
        keydata = bytesToHex(key.getEncoded());
        
    } catch (java.security.NoSuchAlgorithmException e) {
    	e.printStackTrace();
    }

        return keydata;
    }

    public String generateDESKey() {
        String keydata = "";
        try {
	// Generate a DES key
	KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        SecretKey key = keyGen.generateKey();
        keydata = bytesToHex(key.getEncoded());
    } catch (java.security.NoSuchAlgorithmException e) {
    	e.printStackTrace();
    }
        return keydata;
    }

    public String generate3DESKey(){
        String keydata = "";
        try {
	// Generate a triple DES key
	KeyGenerator keyGen = KeyGenerator.getInstance("DESede");
	SecretKey key = keyGen.generateKey();
        keydata = bytesToHex(key.getEncoded());
        System.out.println("DESede Key: "+keydata);
    } catch (java.security.NoSuchAlgorithmException e) {
    	e.printStackTrace();
    }
        return keydata;
    }

    public String generate2DESKey(){
        String keydata = "";
        try {
	// Generate a triple DES key
	KeyGenerator keyGen = KeyGenerator.getInstance("DESede");
	SecretKey key = keyGen.generateKey();
        keydata = bytesToHex(key.getEncoded());
        keydata = keydata.substring(0, 32);
        System.out.println("Double Length Key: "+keydata);
    } catch (java.security.NoSuchAlgorithmException e) {
    	e.printStackTrace();
    }
        return keydata;
    }

    public String getCheckValue(String dkey) {
        String checkdata = "";
        try {
	checkdata = new CryptoUtils().do3DESEncryption(dkey, "0000000000000000");
        System.out.println("Check Digit: "+checkdata.substring(0,6));
    } catch (Exception e) {
    	e.printStackTrace();
    }
        return checkdata;
    }
    
    public static String rightPad(String data, char padd, int len){
            StringBuilder padddata = new StringBuilder(data);
            
            int datalen = data.length();

            while((len - datalen) != 0) {
                padddata.append(padd);
                datalen = padddata.length();
            }
            
            return padddata.toString();
        }
    //******************************************************************
    
    public static String asciiToString(String ascii) {
        int len = ascii.length();        
        
        if (len % 2 != 0)
            return "Invalid length";
        
        byte[] bArray = new byte[len/2];
        for (int i = 0, j=0; i < len/2; i+=2,j++) {
            bArray[j] = Byte.parseByte(ascii.substring(i,i+2), 16);
        }
        
        return new String(bArray);
    }
    public static Vector<String> split(String input, char regex) {
        Vector<String> a = new Vector<String>();
        int firstIndex = 0;
        for (int i = 0; i < input.length(); i++) 
            if (input.charAt(i) == regex) {
                a.addElement(input.substring(firstIndex, i));
                firstIndex = i+1;
            }
        if (!input.substring(firstIndex, input.length()).equals(""))
        a.addElement(input.substring(firstIndex, input.length()));
        
        return a;
    }
    
    public static String[][] doubleSplit(String input, char regex1, char regex2) {
        //Vector<String> a = new Vector<String>();
        int firstIndex = 0;
        int secondIndex = 0;
        String c;
        int pinCount = 0;
        int pinLength = 0;
        
        // Calculate no of pins contained here
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '~')
                    pinLength++;
        }
        
        String[][] b = new String[pinLength][2];
        
        
        for (int i = 0; i < pinLength; i++) {
                                    
                int regex2In = input.indexOf(regex2);
                
                b[i][0] = input.substring(firstIndex, regex2In);
                b[i][1] = input.substring(regex2In+1, regex2In+17);                                               
                             
        
        
        }
        return b;
    }
}

