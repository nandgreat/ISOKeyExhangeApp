package com.mpos.newthree.sdk;

public class Command {

	private static final byte ESC = 0x1B;
	private static final byte FS = 0x1C;
	private static final byte GS = 0x1D;
	private static final byte US = 0x1F;
	private static final byte DLE = 0x10;
	private static final byte DC4 = 0x14;
	private static final byte DC1 = 0x11;
	private static final byte SP = 0x20;
	private static final byte NL = 0x0A;
	private static final byte FF = 0x0C;
	public static final byte PIECE = (byte) 0xFF;
	public static final byte NUL = (byte) 0x00;
	
	//The printer is initialized
	public static byte[] ESC_Init = new byte[] {ESC, '@' };
	
	/**
	 * Print command
	 */
	//Print and wrap
	public static byte[] LF = new byte[] {NL};
	
	//Print and take paper
	public static byte[] ESC_J = new byte[] {ESC, 'J', 0x00 };
	public static byte[] ESC_d = new byte[] {ESC, 'd', 0x00 };
	
	//Print the self page
	public static byte[] US_vt_eot = new byte[] {US, DC1, 0x04 };
	
	 //Beep command
	public static byte[] ESC_B_m_n = new byte[] {ESC, 'B', 0x00, 0x00 };
	
    //Cutter instructions
	public static byte[] GS_V_n = new byte[] {GS, 'V', 0x00 };
    public static byte[] GS_V_m_n = new byte[] {GS, 'V', 'B', 0x00 };
    public static byte[] GS_i = new byte[] {ESC, 'i' };
    public static byte[] GS_m = new byte[] {ESC, 'm' };
	
	/**
	 * Character set command
	 */
	//Set the character right spacing
	public static byte[] ESC_SP = new byte[] {ESC, SP, 0x00 };
	
	//Set the character to print the font format
	public static byte[] ESC_ExclamationMark = new byte[] {ESC, '!', 0x00 };
	
	//Set the font times high width
	public static byte[] GS_ExclamationMark = new byte[] {GS, '!', 0x00 };
	
	//Set up anti-printing
	public static byte[] GS_B = new byte[] {GS, 'B', 0x00 };
	
	//Cancel / select 90 degrees to rotate the print
	public static byte[] ESC_V = new byte[] {ESC, 'V', 0x00 };
	
	//Select font font (mainly ASCII)
	public static byte[] ESC_M = new byte[] {ESC, 'M', 0x00 };
	
	//Select / cancel bold command
	public static byte[] ESC_G = new byte[] {ESC, 'G', 0x00 };
	public static byte[] ESC_E = new byte[] {ESC, 'E', 0x00 };
	
	//Select / cancel the inverted print mode
	public static byte[] ESC_LeftBrace = new byte[] {ESC, '{', 0x00 };
	
	//Set the underscore height (character)
	public static byte[] ESC_Minus = new byte[] {ESC, 45, 0x00 };
	
	//Character mode
	public static byte[] FS_dot = new byte[] {FS, 46 };
	
	//Chinese character mode
	public static byte[] FS_and = new byte[] {FS, '&' };
	
	//Set the Chinese character printing mode
	public static byte[] FS_ExclamationMark = new byte[] {FS, '!', 0x00 };
	
	//Set the underline height (Chinese character)
	public static byte[] FS_Minus = new byte[] {FS, 45, 0x00 };
	
	//Set the left and right spacing of Chinese characters
	public static byte[] FS_S = new byte[] {FS, 'S', 0x00, 0x00 };
	
	//Select the character code page
	public static byte[] ESC_t = new byte[] {ESC, 't', 0x00 };
	
	/**
	 * Formatting instructions
	 */
	//Set the default line spacing
	public static byte[] ESC_Two = new byte[] {ESC, 50}; 
	
	//Set line spacing
	public static byte[] ESC_Three = new byte[] {ESC, 51, 0x00 };
	
	//Set the alignment mode
	public static byte[] ESC_Align = new byte[] {ESC, 'a', 0x00 };
	
	//Set the left margin
	public static byte[] GS_LeftSp = new byte[] {GS, 'L', 0x00 , 0x00 };
	
	//Set the absolute print position
	//Set the current position to the beginning of the line (nL + nH x 256).
	//If the setting position is outside the specified print area, the command is ignored
	public static byte[] ESC_Relative = new byte[] {ESC, '$', 0x00, 0x00 };
	
	//Set the relative print position
	public static byte[] ESC_Absolute = new byte[] {ESC, 92, 0x00, 0x00 };
	
	//Set the print area width
	public static byte[] GS_W = new byte[] {GS, 'W', 0x00, 0x00 };

	/**
	 * Status command
	 */
	//Real-time status transfer instruction
	public static byte[] DLE_eot = new byte[] {DLE, 0x04, 0x00 };
	
	//Real-time cash box instructions
	public static byte[] DLE_DC4 = new byte[] {DLE, DC4, 0x00, 0x00, 0x00 };
	
	//Standard Cash Box Instruction
	public static byte[] ESC_p = new byte[] {ESC, 'F', 0x00, 0x00, 0x00 };
	
	/**
	 * Bar code setting instructions
	 */
	//Select HRI to print
	public static byte[] GS_H = new byte[] {GS, 'H', 0x00 };
	
	//Set the barcode height
	public static byte[] GS_h = new byte[] {GS, 'h', (byte) 0xa2 };
	
	//Set the bar code width
	public static byte[] GS_w = new byte[] {GS, 'w', 0x00 };
	
	//Set the HRI character font font
	public static byte[] GS_f = new byte[] {GS, 'f', 0x00 };
	
	//Bar code left offset instruction
	public static byte[] GS_x = new byte[] {GS, 'x', 0x00 };
	
	//Print the bar code instructions
	public static byte[] GS_k = new byte[] {GS, 'k', 'A', FF };

	//Two - dimensional code related instructions
    public static byte[] GS_k_m_v_r_nL_nH = new byte[] { ESC, 'Z', 0x03, 0x03, 0x08, 0x00, 0x00 };
	
}
