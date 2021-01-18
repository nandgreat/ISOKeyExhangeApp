package com.mpos.newthree.wizarpos.util;

public class StringUtil
{
	private static final String HexChars = "1234567890abcdefABCDEF";
	public static final int LCD_WIDTH = 16;

	/** A table of hex digits */
	public static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

  /**
   * Convert a nibble to a hex character
   * 
   * @param nibble
   *          the nibble to convert.
   */
  public static char toHexChar(int nibble)
  {
    return hexDigit[(nibble & 0xF)];
  }

  /**
  * Method trim space
  *
  * @param The string to be format.
  *
  */
  public static String trimSpace(String oldString) 
  {
    if (null == oldString)
      return null;
    if (0 == oldString.length())
      return "";
      
    StringBuffer sbuf = new StringBuffer();
    int oldLen = oldString.length();
    for(int i = 0; i < oldLen; i++)
    {
      if (' ' != oldString.charAt(i))
        sbuf.append(oldString.charAt(i));
    }     
    String returnString = sbuf.toString();
    sbuf = null;
    return returnString;
  }
   
  /**
  * Method convert byte[] to String
  *
  * @param The string to be format.
  *
  */
  public static String toString(byte abyte0[])
  {
    if(null == abyte0)
      return null;
    else
      return new String(abyte0);
  }

  /**
  * Method fill string
  *
  * @param The string to be format.
  *
  */
  public static String fillString(String formatString, int length, char fillChar, boolean leftFillFlag)
  {
    if (null == formatString)
    {
      formatString = "";
    }
    int strLen = formatString.length();
    if (strLen >= length)
    {
      if (true == leftFillFlag)  // left fill 
        return formatString.substring(strLen - length, strLen);
      else
        return formatString.substring(0, length);
    } else
    {
      StringBuffer sbuf = new StringBuffer();
      int fillLen = length - formatString.length();
      for (int i = 0; i < fillLen; i++)
      { 
        sbuf.append(fillChar);
      }
      
      if (true == leftFillFlag)  // left fill 
      {
        sbuf.append(formatString);
      } else
      {
        sbuf.insert(0, formatString);
      }
      String returnString = sbuf.toString();
      sbuf = null;
      return returnString;
    }
  }

  /**
  * Method fill string
  *
  * @param The string to be format.
  *
  */
  public static String fillSpace(String formatString, int length)
  {
    return fillString(formatString, length, ' ', false);
  }


  /**
  * Method Format string
  *
  * @param The string to be format.
  *
  */
  public static String fillZero(String formatString, int length)
  {
    return fillString(formatString, length, '0', true);
  }

  /**
       * @param s source string (with Hex representation)
       * @return byte array
       */
  public static byte[] hexString2bytes (String s) 
  {
    if (null == s)
      return null;
  
    s = trimSpace(s);
    
    if (false == isHexChar(s, false))
      return null;
      
    return hex2byte (s, 0, s.length() >> 1);
  }

  /**
   * @param   s       source string
   * @param   offset  starting offset
   * @param   len     number of bytes in destination (processes len*2)
   * @return  byte[len]
   */
  public static byte[] hex2byte (String s, int offset, int len) {
      byte[] d = new byte[len];
      int byteLen = len * 2;
      for (int i=0; i < byteLen; i++) {
        int shift = (i%2 == 1) ? 0 : 4;
        d[i>>1] |= Character.digit(s.charAt(offset+i), 16) << shift;
      }
      return d;
  }
  
  private static void appendHex(StringBuffer stringbuffer, byte byte0)
  {
    stringbuffer.append(toHexChar(byte0 >> 4));
    stringbuffer.append(toHexChar(byte0));
  }

  public static String toHexString(byte abyte0[], int beginIndex, int endIndex, boolean spaceFlag)
  {
    if(null == abyte0)
      return null;
    if(0 == abyte0.length)
      return "";
    StringBuffer sbuf = new StringBuffer();
    appendHex(sbuf, abyte0[beginIndex]);
    for(int i = (beginIndex + 1); i < endIndex; i++)
    {
      if (spaceFlag)
        sbuf.append(" ");
      appendHex(sbuf, abyte0[i]);
    }
    String returnString = sbuf.toString();
    sbuf = null;
    return returnString;
  }

  public static String toHexString(byte abyte0[], boolean spaceFlag)
  {
    if(null == abyte0)
      return null;
    return toHexString(abyte0, 0, abyte0.length, spaceFlag);
  }

  /**
  * Method Check String 
  *
  * @param The string to be format.
  *
  */  
  public static boolean isHexChar(String hexString, boolean trimSpaceFlag) 
  {
    if (null == hexString || 0 == hexString.length())
      return false;
  
    if (trimSpaceFlag)
      hexString = trimSpace(hexString);
      
    if (hexString.length() % 2 != 0)
      return false;
    int hexLen = hexString.length();
    for(int i = 0; i < hexLen; i++)
    {
      if (HexChars.indexOf(hexString.charAt(i)) < 0)
        return false;
    }
    
    return true;
  }
  public static boolean isHexChar(String hexString) 
  {
    return isHexChar(hexString, true);
  }
}
