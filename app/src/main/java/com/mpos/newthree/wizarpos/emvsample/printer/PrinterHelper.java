package com.mpos.newthree.wizarpos.emvsample.printer;

import com.mpos.newthree.R;
import com.mpos.newthree.wizarpos.emvsample.MainApp;
import com.mpos.newthree.wizarpos.emvsample.constant.Constant;
import com.mpos.newthree.wizarpos.emvsample.transaction.TransDefine;
import com.mpos.newthree.wizarpos.jni.PrinterInterface;
import com.mpos.newthree.wizarpos.util.AppUtil;
import com.mpos.newthree.wizarpos.util.StringUtil;

import java.io.UnsupportedEncodingException;

/**
 * 打印操作
 * 
 * @author lianyi
 */
public class PrinterHelper 
{
    private static PrinterHelper _instance;

    private PrinterHelper() {
    }

    synchronized public static PrinterHelper getInstance()
    {
		if (null == _instance){
		    _instance = new PrinterHelper();
		}
		return _instance;
    }

    /**
     * 打印签购单
     * 
     * @throws PrinterException
     */
    synchronized public void printReceipt(MainApp appState, int receipt) throws PrinterException
    {
		try {
		    PrinterInterface.open();
		    PrinterInterface.begin();
	
		    printerWrite(PrinterCommand.init());
		    printerWrite(PrinterCommand.setHeatTime(180));
		    printerWrite(PrinterCommand.setAlignMode(1));
		    printerWrite(PrinterCommand.setFontBold(1));

		    printerWrite(("POS SLIP").getBytes("GB2312"));
		    printerWrite(PrinterCommand.feedLine(2));
		    printerWrite(PrinterCommand.setAlignMode(0));
		   
		    printerWrite("--------------------------------".getBytes("GB2312"));
		    printerWrite(PrinterCommand.linefeed());
		    
		    printerWrite(appState.terminalConfig.getMerchantName1().getBytes("GB2312"));
		    printerWrite(PrinterCommand.linefeed());
			    
	    	if(receipt == 0)
		    {
		    	printerWrite(("MERCHANT COPY").getBytes("GB2312"));
			    printerWrite(PrinterCommand.linefeed());
		    }
		    else if(receipt == 1)
		    {
		    	printerWrite(("CARDHOLDER COPY").getBytes("GB2312"));
		    	
			    printerWrite(PrinterCommand.linefeed());
		    }
		    else if(receipt == 2)
		    {
		    	printerWrite(("BANK COPY").getBytes("GB2312"));
			    printerWrite(PrinterCommand.linefeed());
		    }
		    printerWrite("--------------------------------".getBytes("GB2312"));
		    printerWrite(PrinterCommand.linefeed());
		    printerWrite((appState.getString(R.string.tid_tag) + " " + appState.terminalConfig.getTID()).getBytes("GB2312"));
		    printerWrite(PrinterCommand.linefeed());
		    
		    printerWrite((appState.getString(R.string.mid_tag) + " " + appState.terminalConfig.getMID()).getBytes("GB2312"));
		    printerWrite(PrinterCommand.linefeed());
 
		    String pan = appState.getString(R.string.pan_tag) + " " + appState.trans.getPAN();
		    switch(appState.trans.getCardEntryMode())
		    {
		    case 0:
		    	pan = pan + " N";
		    	break;
		    case Constant.SWIPE_ENTRY:
		    	pan = pan + " S";
		    	break;
		    case Constant.INSERT_ENTRY:
		    	pan = pan + " I";
		    	break;
		    case Constant.MANUAL_ENTRY:
		    	pan = pan + " M";
		    	break;
		    default:
		    	pan = pan + " C";
		    	break;
		    }
		    printerWrite(pan.getBytes("GB2312"));
		    printerWrite(PrinterCommand.linefeed());
		    
		    printerWrite((  appState.getString(R.string.date_tag)
		    		      + " " + appState.trans.getTransDate().substring(0, 4)
		    		      + "/" + appState.trans.getTransDate().substring(4, 6)
		    		      + "/" + appState.trans.getTransDate().substring(6, 8)
		    		      + " " + appState.trans.getTransTime().substring(0, 2)
		    		      + ":" + appState.trans.getTransTime().substring(2, 4)
		    		      + ":" + appState.trans.getTransTime().substring(4, 6) ).getBytes("GB2312"));
		    printerWrite(PrinterCommand.linefeed());
		    
		    printerWrite(( "TICKET:" + StringUtil.fillZero(Integer.toString(appState.trans.getTrace()), 6)).getBytes("GB2312"));
		    printerWrite(PrinterCommand.linefeed()); 
	    
		    printerWrite(appState.getString(TransDefine.transInfo[appState.trans.getTransType()].id_display_en).getBytes("GB2312"));
		    printerWrite(PrinterCommand.linefeed());
		    
		    printerWrite(("AMOUNT:" + StringUtil.fillString(AppUtil.formatAmount(appState.trans.getTransAmount()), 22, ' ', true)).getBytes("GB2312"));
		    printerWrite(PrinterCommand.linefeed());

		    if(appState.trans.getCardEntryMode() != Constant.SWIPE_ENTRY)
		    {
			    printerWrite(("CSN:" + StringUtil.fillZero(Byte.toString(appState.trans.getCSN()),2)).getBytes());
			    printerWrite(PrinterCommand.linefeed());
		    	
			    printerWrite(("UNPR NUM:" + appState.trans.getUnpredictableNumber()).getBytes());
			    printerWrite(PrinterCommand.linefeed());
			    
		    	printerWrite(("AC:" + appState.trans.getAC()).getBytes());
			    printerWrite(PrinterCommand.linefeed());
			    
			    printerWrite(("TVR:" + appState.trans.getTVR()).getBytes());
			    printerWrite(PrinterCommand.linefeed());
			    
			    printerWrite(("AID:" + appState.trans.getAID()).getBytes());
			    printerWrite(PrinterCommand.linefeed());
			    
			    printerWrite(("TSI:" + appState.trans.getTSI()).getBytes());
			    printerWrite(PrinterCommand.linefeed());
			    
			    printerWrite(("APPLAB:" + appState.trans.getAppLabel()).getBytes());
			    printerWrite(PrinterCommand.linefeed());
			    
			    printerWrite(("APPNAME:" + appState.trans.getAppName()).getBytes());
			    printerWrite(PrinterCommand.linefeed());
			    
			    printerWrite(("AIP:" + appState.trans.getAIP()).getBytes());
			    printerWrite(PrinterCommand.linefeed());
			    
			    printerWrite(("IAD:" + appState.trans.getIAD()).getBytes());
			    printerWrite(PrinterCommand.linefeed());
			    
			    printerWrite(("TermCap:" + appState.terminalConfig.getTerminalCapabilities()).getBytes());
			    printerWrite(PrinterCommand.linefeed());
		    }
		    if( appState.trans.getNeedSignature() == 1)
		    {
			    String sig = "CARDHOLDER SIGNATURE";
			    printerWrite(sig.getBytes("GB2312")); 
			    printerWrite(PrinterCommand.feedLine(3)); 
			    printerWrite("--------------------------------".getBytes("GB2312"));
			    printerWrite(PrinterCommand.linefeed());
		    }
		    printerWrite(PrinterCommand.feedLine(2));
		    
		} catch (UnsupportedEncodingException e) {
		    throw new PrinterException("PrinterHelper.printReceipt():" + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
		    throw new PrinterException(e.getMessage(), e);
		} finally {
		    PrinterInterface.end();
		    PrinterInterface.close();
		}
    }
    
    public void printerWrite(byte[] data)
    {
    	PrinterInterface.write(data, data.length);
    }
}
