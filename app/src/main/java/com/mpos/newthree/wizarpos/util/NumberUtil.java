package com.mpos.newthree.wizarpos.util;

public class NumberUtil
{
  public NumberUtil()
  {
  }
  
  final static char[] digits = {
    '0' , '1' , '2' , '3' , '4' , '5' ,
    '6' , '7' , '8' , '9' , 'a' , 'b' ,
    'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
    'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
    'o' , 'p' , 'q' , 'r' , 's' , 't' ,
    'u' , 'v' , 'w' , 'x' , 'y' , 'z'
  };
  final static char [] DigitTens = {
    '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
    '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
    '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
    '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
    '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
    '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
    '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
    '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
    '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
    '9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
  }; 
  final static char [] DigitOnes = { 
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
  };

  public final static byte [] BCD_Digit = {
    (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07, (byte)0x08, (byte)0x09,
    (byte)0x10, (byte)0x11, (byte)0x12, (byte)0x13, (byte)0x14, (byte)0x15, (byte)0x16, (byte)0x17, (byte)0x18, (byte)0x19,
    (byte)0x20, (byte)0x21, (byte)0x22, (byte)0x23, (byte)0x24, (byte)0x25, (byte)0x26, (byte)0x27, (byte)0x28, (byte)0x29,
    (byte)0x30, (byte)0x31, (byte)0x32, (byte)0x33, (byte)0x34, (byte)0x35, (byte)0x36, (byte)0x37, (byte)0x38, (byte)0x39,
    (byte)0x40, (byte)0x41, (byte)0x42, (byte)0x43, (byte)0x44, (byte)0x45, (byte)0x46, (byte)0x47, (byte)0x48, (byte)0x49,
    (byte)0x50, (byte)0x51, (byte)0x52, (byte)0x53, (byte)0x54, (byte)0x55, (byte)0x56, (byte)0x57, (byte)0x58, (byte)0x59,
    (byte)0x60, (byte)0x61, (byte)0x62, (byte)0x63, (byte)0x64, (byte)0x65, (byte)0x66, (byte)0x67, (byte)0x68, (byte)0x69,
    (byte)0x70, (byte)0x71, (byte)0x72, (byte)0x73, (byte)0x74, (byte)0x75, (byte)0x76, (byte)0x77, (byte)0x78, (byte)0x79,
    (byte)0x80, (byte)0x81, (byte)0x82, (byte)0x83, (byte)0x84, (byte)0x85, (byte)0x86, (byte)0x87, (byte)0x88, (byte)0x89,
    (byte)0x90, (byte)0x91, (byte)0x92, (byte)0x93, (byte)0x94, (byte)0x95, (byte)0x96, (byte)0x97, (byte)0x98, (byte)0x99,
  }; 

  public static byte[] intToBcd(int number, int length )
  {
    byte[] buf =  new byte[length];
    buf = StringUtil.hexString2bytes(StringUtil.fillString("" + number, length*2, '0', true));
    
    return buf;
    
  }
  
    /**
     * Parses the string argument as a signed integer in the radix 
     * specified by the second argument. The characters in the string 
     * must all be digits of the specified radix (as determined by 
     * whether {@link Character#digit(char, int)} returns a
     * nonnegative value), except that the first character may be an
     * ASCII minus sign <code>'-'</code> (<code>'&#92;u002D'</code>) to
     * indicate a negative value. The resulting integer value is returned.
     * <p>
     * An exception of type <code>NumberFormatException</code> is
     * thrown if any of the following situations occurs:
     * <ul>
     * <li>The first argument is <code>null</code> or is a string of
     * length zero.
     * <li>The radix is either smaller than
     * {@link Character#MIN_RADIX} or
     * larger than {@link Character#MAX_RADIX}.
     * <li>Any character of the string is not a digit of the specified
     * radix, except that the first character may be a minus sign
     * <code>'-'</code> (<code>'&#92;u002D'</code>) provided that the
     * string is longer than length 1.
     * <li>The value represented by the string is not a value of type
     * <code>int</code>. 
     * </ul><p>
     * Examples:
     * <blockquote><pre>
     * parseInt("0", 10) returns 0
     * parseInt("473", 10) returns 473
     * parseInt("-0", 10) returns 0
     * parseInt("-FF", 16) returns -255
     * parseInt("1100110", 2) returns 102
     * parseInt("2147483647", 10) returns 2147483647
     * parseInt("-2147483648", 10) returns -2147483648
     * parseInt("2147483648", 10) throws a NumberFormatException
     * parseInt("99", 8) throws a NumberFormatException
     * parseInt("Kona", 10) throws a NumberFormatException
     * parseInt("Kona", 27) returns 411787
     * </pre></blockquote>
     *
     * @param      src   the <code>String</code> containing the integer 
     * 			representation to be parsed
     * @param      radix   the radix to be used while parsing <code>s</code>.
     * @return     the integer represented by the string argument in the
     *             specified radix.
     * @exception  NumberFormatException if the <code>String</code>
     * 		   does not contain a parsable <code>int</code>.
     */
  public static int parseInt(byte[] s, int offset, int radix, boolean lengthFlag)
		  throws NumberFormatException
  {
    if (s == null)
    {
      throw new NumberFormatException("null");
    }

    if (radix < Character.MIN_RADIX)
    {
      throw new NumberFormatException("radix " + radix
          + " less than Character.MIN_RADIX");
    }

    if (radix > Character.MAX_RADIX)
    {
      throw new NumberFormatException("radix " + radix
          + " greater than Character.MAX_RADIX");
    }

    int result = 0;
    boolean negative = false;
    int i = 0, max = (s.length - offset);
    int limit;
    int multmin;
    int digit;
    
    if (lengthFlag)
    {
      max = s[offset];
      offset++;
    }

    if (max > 0)
    {
      if (s[offset + 0] == (byte)'-')
      {
        negative = true;
        limit = Integer.MIN_VALUE;
        i++;
      } else
      {
        limit = -Integer.MAX_VALUE;
      }
      multmin = limit / radix;
      if (i < max)
      {
        digit = Character.digit((char)s[offset + (i++)], radix);
        if (digit < 0)
        {
          throw new NumberFormatException ("Byte array contains non-digit");
        } else
        {
          result = -digit;
        }
      }
      while (i < max)
      {
        // Accumulating negatively avoids surprises near MAX_VALUE
        digit = Character.digit((char)s[offset + (i++)], radix);
        if (digit < 0)
        {
          throw new NumberFormatException ("Byte array contains non-digit");
        }
        if (result < multmin)
        {
          throw new NumberFormatException ("Byte array contains non-digit");
        }
        result *= radix;
        if (result < limit + digit)
        {
          throw new NumberFormatException ("Byte array contains non-digit");
        }
        result -= digit;
      }
    } else
    {
      throw new NumberFormatException ("Byte array contains non-digit");
    }
    if (negative)
    {
      if (i > 1)
      {
        return result;
      } else
      { /* Only got "-" */
        throw new NumberFormatException ("Byte array contains non-digit");
      }
    } else
    {
      return -result;
    }
  }
  
  public static short byte2ToShort(byte[] src)
  {
    return byte2ToShort(src, 0);
  }
  
  public static short byte2ToShort(byte[] src, int offset)
  {
    if (null == src || offset < 0 || offset > src.length)
      throw new NullPointerException("invalid byte array ");
    if ((src.length - offset) < 2)
      throw new IndexOutOfBoundsException("invalid len: " + src.length); 	  
  	short number = (short)((src[offset + 0]&0xff)<<8 | (src[offset + 1]&0xff));
  	return number;
  }
  
  /**
  *
  * Method converting int into byte array.
  *
  * @param number The int value to be converted.
  *
  */
  public static byte[] intToByte4(int number)
  {
    int tmp_num = number;
    byte[] byte4 = new byte[4];
    
    for (int i = byte4.length - 1; i > -1; i--)
    {
      byte4[i] = (byte)(tmp_num & 0xff);
      tmp_num = tmp_num >> 8;
    }
    return byte4;
  }
  
}
