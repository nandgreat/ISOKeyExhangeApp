/**
 * 
 */
package com.mpos.newthree.utils;

/**
 * @author SUNNYben
 *
 */

public class ResponseMessage {
	public static final String[] responseString = {
		"Transaction Completed Successfully",	//0
		"Refer to card issuer", 	//1
		"Refer to card issuer",		//2
		"Invalid merchant",			//3
		"Pick-up",					//4
		"Do not honour",			//5
                "Error",					//6
		"Pick-up, Special condition",//7
		"Honour with identification",//8
		"Request in progress",		//9
		"Approved for partial amount",//10
		"Approved(VIP)",			//11
		"Invalid transaction",		//12
		"Invalid amount",			//13
		"Invalid card number",		//14
		"",							//15	
		"Approved, update track 3",	//16
		"Customer cancellatoin",	//17
		"",							//18
		"Re-enter transaction",		//19
		"Invalid response",			//20
		"No action taken",			//21
		"Suspected malfunction",	//22
		"Unacceptable transaction fee",//23
		"File update not supported",//24
		"Unable to complete transaction",				//25
		"Duplicate file update record",//26
		"File update edit error",	//27
		"File update record locked out",//28
		"File update not successful",//29
		"Format error",				//30
		"",							//31
		"",							//32
		"Expired card",				//33
		"Suspected fraud",			//34
		"Card acceptor contact acquirer",//35
		"Restricted card",			//36
		"Card acceptor call acquirer security",	//37
		"Allowable PIN retries succeeded",//38
		"No credit account",		//39
		"Requested function not supported",	//40
		"Lost card",				//41
		"No universal account",		//42
		"Stolen card",				//43
		"No investment account",	//44
		"",							//45
		"",							//46
		"",							//47
		"",							//48
		"",							//49
		"",							//50
		"Not sufficient funds",		//51
		"No chequing account",		//52
		"No savings account",		//53
		"Expired card",				//54
		"Incorrect PIN",			//55
		"No card record",			//56
		"Transaction not permitted to cardholder",	//57
		"Transaction not permitted to terminal",	//58
		"Suspected fraud",			//59
		"Card acceptor contact acquirer",	//60
		"Exceeds withdrawal amount limit",	//61
		"Restricted card",			//62
		"Security violation",		//63
		"Original amount incorrect",//64
		"Exceeds withdrawal frequency limit",//65
		"Card acceptor call acquirer's security",//66
		"Hard capture(by ATM)",		//67
		"Response Received too late",//68
		"",							//69
		"",							//70
		"",							//71
		"",							//72
		"",							//73
		"",							//74
		"Allowable number of PINs exceeded",	//75
		"",							//76
		"",							//77
		"",							//78
		"",							//79
		"",							//80
		"",							//81
		"",							//82
		"",							//83
		"",							//84
		"",							//85
		"",							//86
		"",							//87
		"",							//88
		"",							//89
		"",							//90
		"Issuer or switch inoperative",		//91
		"",							//92
		"Transaction cannot be completed",	//93
		"Duplicate transaction",	//94
		"",							//95
		"System Unavailable",		//96
                "",		//97
                "Connection Timed Out",		//98
                "Your Financial Institution \nis not available",		//99
		
	};
	/*
	public static final ResponseArray[] response =  {
		new ResponseArray(0, "Completed Successfully"),
		new ResponseArray(1, "Refer to card issuer"),
		new ResponseArray(2, "Refer to card issuer"),
		new ResponseArray(3, "Invalid merchant"),
		new ResponseArray(4, "Pick-up"),
		new ResponseArray(5, "Do not honour"),
		new ResponseArray(6, "Error"),
		new ResponseArray(7, "Pick-up, Special condition"),
		new ResponseArray(8, "Honour with identification"),
		new ResponseArray(9, "Request in progress"),
		new ResponseArray(10, "Approved for partial amount"),
		new ResponseArray(11, "Approved(VIP)"),
		new ResponseArray(12, "Invalid transaction"),
		new ResponseArray(13, "Invalid amount"),
		new ResponseArray(14, "Invalid card number"),
		// No 15
		new ResponseArray(16, "Approved, update track 3"),
		new ResponseArray(17, "Customer cancellatoin"),
		// No 18
		new ResponseArray(19, "Re-enter transaction"),
		new ResponseArray(20, "Invalid response"),
		new ResponseArray(21, "No action taken"),
		new ResponseArray(22, "Suspected malfunction"),
		new ResponseArray(23, "Unacceptable transaction fee"),
		new ResponseArray(24, "File update not supported"),
		new ResponseArray(25, "Out of stock"),
		new ResponseArray(26, "Duplicate file update record"),
		new ResponseArray(27, "File update edit error"),
		new ResponseArray(28, "File update record locked out"),
		new ResponseArray(29, "File update not successful"),
		new ResponseArray(30, "Format error"),
		new ResponseArray(33, "Expired card"),
		new ResponseArray(34, "Suspected fraud"),
		new ResponseArray(35, "Card acceptor contact acquirer"),
		new ResponseArray(36, "Restricted card"),
		new ResponseArray(37, "Card acceptor call acquirer security"),
		new ResponseArray(38, "Allowable PIN retries succeeded"),
		new ResponseArray(39, "No credit account"),
		new ResponseArray(40, "Requested function not supported"),
		new ResponseArray(41, "Lost card"),
		new ResponseArray(42, "No universal account"),
		new ResponseArray(43, "Stolen card"),
		new ResponseArray(44, "No investment account"),
		// 45 - 50 ISO reserved 
		new ResponseArray(51, "Not sufficient funds"),
		new ResponseArray(52, "No chequing account"),
		new ResponseArray(53, "No savings account"),
		new ResponseArray(54, "Expired card"),
		new ResponseArray(55, "Incorrect PIN"),
		new ResponseArray(56, "No card record"),
		new ResponseArray(57, "Transaction not permitted to cardholder"),
		new ResponseArray(58, "Transaction not permitted to terminal"),
		new ResponseArray(59, "Suspected fraud"),
		new ResponseArray(60, "Card acceptor contact acquirer"),
		new ResponseArray(61, "Exceeds withdrawal amount limit"),
		new ResponseArray(62, "Restricted card"),
		new ResponseArray(63, "Security violation"),
		new ResponseArray(64, "Original amount incorrect"),
		new ResponseArray(65, "Exceeds withdrawal frequency limit"),
		new ResponseArray(66, "Card acceptor call acquirer's security"),
		new ResponseArray(67, "Hard capture(by ATM)"),
		new ResponseArray(68, "Response Received too late"),
		// 69 - 74 Reserved ISO
		new ResponseArray(75, "Allowable number of PINs exceeded"),
		// 76 - 89 Reserved private
		// No 90
		new ResponseArray(91, "Issuer or switch inoperative"),
		// No 92
		new ResponseArray(93, "Transaction cannot be completed"),
		new ResponseArray(94, "Duplicate transaction"),
		// No 95
		new ResponseArray(96, "System malfunction"),
		// 97 - 99 Reserved national
		// 0A -9Z Reserved ISO
	};
	*/
}
