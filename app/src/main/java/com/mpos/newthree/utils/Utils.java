package com.mpos.newthree.utils;

//* import com.access.ini.InitializeConfig;
import java.util.Vector;


public class Utils {
    //private static Logger logger = Logger.getLogger(Utils.class);	                

        public static String[] parseBalance(String balanceString) {
               
            int len = balanceString.length()/20;            
            String[] balanceArray=new String[2];
            String bal = "";

            for (int i = 0; i < len; i++){                                
                bal = balanceString.substring(i*20, (i+1)*20);
                
                String balance = bal.substring(8);
                //System.out.println("Balance = "+balance);
                long balanceInt= Long.parseLong(balance);

                String testaccount = bal.substring(2, 4);
                //System.out.println("Test = "+testaccount);
                //String testCardCurrency=new String(arrayChar,4,3);
                char accountStatus=bal.charAt(7);
                //System.out.println("Status = "+accountStatus);
                if (accountStatus == 'D') {
                    balanceInt *= -1;
                }
                               
                 if(testaccount.equals("01")){     // Available Balance                       
                     balanceArray[0]= Long.toString(balanceInt);
                 }
                 else if(testaccount.equals("02")){ // ledger Balance
                      balanceArray[1]= Long.toString(balanceInt);
                  }                                           
             }
                   
            return balanceArray; 
        }
        
     public static String parseBalance2(String balanceString) {
        String balance=null;

        String sample=balanceString.toString();
        int length=sample.length();
        int len=sample.length()/20;
        String[] arrayTest=new String[len];
        
            int j=0;
            int l=0;
            for(int k=20;k<=length;k=k+20){

                arrayTest[j]=sample.substring(l,k);
               // logger.info("This is the content of the array :"+arrayTest[j]);

                char[] arrayChar=arrayTest[j].toCharArray();
                balance=new String(arrayChar,11,9);
                int balanceInt= Integer.parseInt(balance);
                // System.out.println("balance in double "+balanceInt);
                balance = String.valueOf(balanceInt);

                  j++;
                  l=k;                          
                 }

            return balance;                                       
        }
        
	/**
	 * get the new string from the first non-0 value
	 * 
	 * @param part
	 *            the input string to be changed
	 * @return
	 */
	static String getIPPart(String part) {
		for (int i = 0; i < 3; i++) {
			if (part.charAt(0) == '0')
				part = part.substring(1);
			else
				return part;
		}
		return "0";
	}
		
	/**
	 * Split the input String based on the regex parameter
	 * 
	 * @param input String
	 * @param regex
	 * @return return a vector containing the splitted Strings
	 */
	public static Vector split(String input, char regex) {
		Vector a = new Vector();
		int firstIndex = 0;
		for (int i = 0; i < input.length(); i++) {			
			if (input.charAt(i) == regex) {
				a.addElement(input.substring(firstIndex, i));
				firstIndex = i+1;
			}
		}
		if (!input.substring(firstIndex, input.length()).equals(""));
			a.addElement(input.substring(firstIndex, input.length()));
		
		return a;
	}
	
	/**
	 * Split the input String based on the splitLen parameter
	 * 
	 * @param input String
	 * @param splitLen
	 * @return return a vector containing the splitted Strings
	 */
	public static Vector split(String input, int splitLen) {
		Vector a = new Vector();
		
		int len = input.length();
		for (int i = 0; i < len/splitLen; i++) {
			a.addElement(input.substring(i*splitLen, (i+1)*splitLen));
		}
		
		return a;
	}
	
	/**
	 * Split the input String based on the regex parameter
	 * 
	 * @param input String
	 * @return return a vector containing the splitted Strings
	 */
	public static String[][] doubleSplit(String input, char regex1, char regex2) {
		if (input.endsWith("~"))
			input = input.substring(0, input.length()-1);
		
		Vector a = new Vector();
		int firstIndex = 0;
		String c;
		for (int i = 0; i < input.length(); i++) {			
			if (input.charAt(i) == regex1) {
				a.addElement(input.substring(firstIndex, i));
				firstIndex = i+1;
			}
		}
		//if (!input.substring(firstIndex, input.length()).equals(""));
			a.addElement(input.substring(firstIndex, input.length()));
		
		firstIndex = 0;
		
		String[][] b = new String[a.size()][2];
		for (int i = 0; i < a.size(); i++) {
			c = String.valueOf(a.elementAt(i));
			firstIndex = c.indexOf(regex2);
			b[i][0] = c.substring(0, firstIndex);
			b[i][1] = c.substring(firstIndex+1, c.length());	
		}
		
		return b;
	}
	
	/**
	 * compare the two byte array
	 * 
	 * @param src
	 * @param dst
	 * @return true: the two byte array are equal false: not equal
	 */
	public static boolean compareByteArray(byte[] src, byte[] dst) {
		if (src.length != dst.length)
			return false;
		for (int i = 0; i < src.length; i++) {
			if (src[i] != dst[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * compare the two byte array
	 * 
	 * @param src
	 * @param dst
	 * @return true: the two byte array are equal false: not equal
	 */
	public static boolean compareByteArray(byte[] src, byte[] dst, int srcoffset, int srclength) {
		if (srclength != dst.length)
			return false;
		for (int i = 0; i < srclength; i++) {
			if (src[i+srcoffset] != dst[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * get one part of the byte array
	 * 
	 * @param buf
	 *            the byte array
	 * @param part
	 *            the part index
	 * @return the corresponding part of the byte array
	 */
	public static byte[] getPart(byte[] buf, int part) {
		if (part >= 3)
			return null;
		int partlen = buf.length / 3;
		byte[] partbuf = new byte[partlen];
		System.arraycopy(buf, part * partlen, partbuf, 0, partlen);
		return partbuf;
	}
	
      /**
      *
      * Method Concatenates the specified byte[].
      *
       *
      */
      public static byte[] concatByteArray(byte[] a, byte[] b)
      {
        int aL = a.length;
        int bL = b.length;
        int len = aL + bL;
        byte[] c = new byte[len];

        System.arraycopy(a, 0, c, 0, aL);
        System.arraycopy(b, 0, c, aL, bL);

        return c;
      }

      public static void append2(StringBuffer sb, int n) {
            if (n < 10)
                sb.append('0');

            sb.append(n);
      }
	  
      public static void append2(StringBuilder sb, int n) {
            if (n < 10)
                sb.append('0');

            sb.append(n);
      }
      
       public static String leftPad(String data, char padd, int len){
            String padddata = data;
            int datalen = data.length();           
                                    
            while((len - datalen) != 0) {
                padddata = padd + padddata;
                datalen = padddata.length();
            }
            return padddata;   
        }

       public static String rightPad(String data, char padd, int len){
            StringBuilder bd = new StringBuilder(data);
            while (bd.length() < len) {
                bd.append(padd);
            }
            
            return bd.toString();   
        }
       
       public static String byteToHex(byte data) {
            String str = "";

                if ((data & 0xFF) < 16) {
                    str = str + "0" + Integer.toHexString(data & 0xFF);
                } else {
                    str = str + Integer.toHexString(data & 0xFF);
                }

            return str.toUpperCase();        
        }

        public static byte[] hexToBytes(String str) {
            if (str == null) {
                return null;
            } else if (str.length() < 2) {
                return null;
            } else {
                short len = (short)(str.length() / 2);
                byte[] buffer = new byte[len];
                for (short i = 0; i < len; i++) {
                    buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
                }
                return buffer;
            }
        }

        public static String encode(byte[] data, int off, int len)
        {
            char[]	ch;
            short	i;

            // Convert bytes to hex digits
            ch = new char[len*2];
            i = 0;

            while (len-- > 0)
            {
                short		b;
                short		d;

                // Convert next byte into a hex digit pair
                b = (short)(data[off++] & 0xFF);

                d = (short)(b >> 4);
                d = (short)(d < 0xA ? d+'0' : d-0xA+'A');
                ch[i++] = (char) d;

                d = (short)(b & 0xF);
                d = (short)(d < 0xA ? d+'0' : d-0xA+'A');
                ch[i++] = (char) d;
            }

            return (new String(ch));
        }

        public static String encode (byte[] data) {
            return encode(data, 0, data.length);
        }

        public static byte[][] splitAFL(byte[] afl) {
                    short len = (short)afl.length;
                    //Tracer.debug("Data length in splitAFL= "+len);
                    byte[][] temp = new byte[len/4][4];	

                    if (len % 4 != 0) {
                            //Tracer.debug("AFL Length is not a multiple of 4. Length = "+len);			
                    } else {

                            for (short i = 0; i < len/4; i++) {
                                    for (short j = 0; j < 4; j++) {
                                            temp[i][j] = afl[i*4+j];
                                    }				
                            }
                    }
                    return temp;
            } 
            
      public String getPinBlock(String pin, String pan) {//ISO-0
        String pinBlock = null;
        int len = pan.length();

        try {
            pin = "0" + pin.length() + pin + "ffffffffff";
            pan = "0000" + pan.substring(len - 13, len - 1);

            //logger.info(pin+"    "+pan);
            pinBlock = xorByte(pin, pan);
        } catch (Exception ex) {
           //Tracer.debug("Exception: "+ex.getMessage());
        }
        return pinBlock;
    }

    private static String xorByte(String pin, String pan) {
        if (pan == null) {
            return null;
        } else {
            short len = (short)pan.length();

            StringBuffer buffer = new StringBuffer();
            
            for (short i = 0; i < len; i++) {                
                buffer.append((char)(pan.charAt(i) ^ pin.charAt(i)));
            }
            
            return buffer.toString();
            
        }
    }
    
    public static String maskPAN(String pan) {
        StringBuilder sb = new StringBuilder(pan.substring(0, 6));
        int len = pan.length();
        
        for (int i = 0; i < len-10; i++) {
            sb.append('*');
        }
        sb.append(pan.substring(len-4, len));
        
        return sb.toString();
    }
    
    public static String reFormatPin(String formattedPin) {
    	int a = formattedPin.indexOf('A');
    	String part1 = formattedPin.substring(0, a-1);
    	String part2 = formattedPin.substring(a-2, formattedPin.length()-2);
    	String finalVal="";
    	for (int i = 0; i < a-2; i++) {
            if (part1.charAt(i) == '\n' && part1.charAt(i+1) == '\n') {
                finalVal+=' ';
            }
            else if (part1.charAt(i) == '\n' || part1.charAt(i) == ' ') {
                    continue;
            }
            else {
                finalVal+=part1.charAt(i);
            }
    	}
    	return finalVal.trim()+". "+part2;
    	
    }
  
}
