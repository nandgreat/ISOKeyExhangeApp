package com.mpos.newthree.wizarpos.emvsample.transaction;

import com.mpos.newthree.R;
import com.mpos.newthree.wizarpos.emvsample.constant.Constant;

public class TransDefine implements Constant
{
	public static TransInfo[] transInfo = { 
		new TransInfo( TRAN_GOODS,  		R.string.tran_sale_en, 			(byte)0  ), // 1
		new TransInfo( TRAN_SETTLE,  	    R.string.tran_settlement_en,	(byte)(T_NOCAPTURE               ) 	), // 6

		new TransInfo( QUERY_CARD_RECORD,	R.string.query_trans_record_en,	(byte)(T_NOCAPTURE	+ T_NORECEIPT)  ), // 8
		
		new TransInfo( QUERY_SPECIFIC,  	R.string.query_specific_en,		(byte)(T_NOCAPTURE	+ T_NORECEIPT) 	), //11
		new TransInfo( QUERY_TRANS_DETAIL,	R.string.query_trans_detail_en,	(byte)(T_NOCAPTURE	+ T_NORECEIPT)  ), //12
	};
}
