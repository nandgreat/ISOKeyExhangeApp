/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpos.newthree.security;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 *
 * @author samycorps
 */


public class DataEncryption {
    public String encryptText(String key, String text)
    {
      long finalKey = 0;
      for (int i=0; i<key.length(); i++)
      {
      long tempKey = key.charAt(i);
      tempKey *= 128;
      finalKey += tempKey;
      }

  Random generator = new Random(finalKey);
  String returnString = "";
      for (int i=0; i<text.length(); i++)
      {
      int temp = (int)text.charAt(i);
      temp += generator.nextInt(95);
          if (temp > 127)
          {
          temp -= 95;
          }
      returnString += (char)temp;
      }
  return returnString;
  }

    public String decryptText(String key, String text)
    {
      long finalKey = 0;
      for (int i=0; i<key.length(); i++)
      {
      long tempKey = key.charAt(i);
      tempKey *= 128;
      finalKey += tempKey;
      }
      Random generator = new Random(finalKey);
      String returnString = "";
      for (int i=0; i<text.length(); i++)
      {
      int temp = (int)text.charAt(i);
      temp -= generator.nextInt(95);
      if (temp < 32)
      {
      temp+= 95;
      }
      else if (temp > 127)
      {
      temp -= 95;
      }
      returnString += (char)temp;
      }
      return returnString;
     }

    public static void main(String[] args) {
      DataEncryption cypher = new DataEncryption();
      //String encdata = cypher.encryptText("mantraadmin", "abbey123456");
      String encdata = cypher.encryptText("kioskplatform", "access4heritae");
    //  System.out.println("Encrypted Data : "+encdata);
      String encdata2 = cypher.encryptText("kioskplatform", "kioskterminal");
      System.out.println("Encrypted Data : "+encdata2);
      
      String decryptdata = cypher.decryptText("kioskplatform", ",&");
      System.out.println("Decrypted Data : "+decryptdata);

      String decryptdata2 = cypher.decryptText("kioskplatform", ",2[{!GKV7N");
      System.out.println("Decrypted Data : "+decryptdata2);
      GregorianCalendar newCal = new GregorianCalendar();
      int day = newCal.get( Calendar.DAY_OF_WEEK );

      System.out.println("Date : "+ day);
  }
}