package com.mpos.newthree.wizarpos.emvsample.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpos.newthree.R;
import com.mpos.newthree.wizarpos.emvsample.constant.Constant;
import com.mpos.newthree.wizarpos.emvsample.parameter.ISOParams;
import com.mpos.newthree.wizarpos.emvsample.transaction.TransDefine;
import com.mpos.newthree.wizarpos.jni.PinPadInterface;
import com.mpos.newthree.wizarpos.util.AppUtil;
import com.mpos.newthree.wizarpos.util.ByteUtil;
import com.mpos.newthree.wizarpos.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.mpos.newthree.activity.MainActivity.card_check;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.appState;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_get_candidate_list;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_get_tag_data;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_get_tag_list_data;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_is_need_advice;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_is_need_signature;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_is_tag_present;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_process_next;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_kernel_type;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_online_pin_entered;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_online_result;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_other_amount;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_tag_data;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_trans_amount;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_set_trans_type;
import static com.mpos.newthree.wizarpos.emvsample.activity.FuncActivity.emv_trans_initialize;

public class ProcessEMVCardActivity extends Fragment implements Constant
{
	private int defaultTagList[] = {    0x57,
										0x5A,
										0x5F20,
										0x5F24,
										0x5F25,
										0x5F28,
										0x5F2A,
										0x5F34,
										0x82,
										0x84,
										0x8A,
										0x8E,
										0x95,
										0x9A,
										0x9B,
										0x9C,
										0x9F01,
										0x9F02,
										0x9F03,
										0x9F07,
										0x9F09,
										0x9F0D,
										0x9F0E,
										0x9F0F,
										0x9F10,
										0x9F15,
										0x9F16,
										0x9F1A,
										0x9F1C,
										0x9F1E,
										0x9F21,
										0x9F26,
										0x9F27,
										0x9F33,
										0x9F34,
										0x9F35,
										0x9F36,
										0x9F37,
										0x9F39,
										0x9F41,
										0x9F4C,
										0x9F5D,
										0x9F63,
										0x9F66,
										0x9F6C,
										0x9F74,
										0xDF31   };
	
	
	private int confirmTagList[] = {	0x9F1C,
										0x9F27,  // Cryptogram Information Data
										0x95,    // Terminal Verification Results
										0x9B,    // TSI
										0x9F26,
										0x9F4C,
										0x9F74,
										0xDF31  // Issuer Script Results   
								   };
	
	private TextView textTitle  = null;
	private Button   buttonBack = null;
    private Button   buttonMore = null;
    
	private TextView textTransType = null;
	private TextView textLine1 = null;
	
    private Thread mEMVThread = null;
    private Thread mEMVProcessNextThread = null;
	private FuncActivity func;
	private LinearLayout llyt_Scroll_Demo;
	private int requestCode;
	private int resultCode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_emv, container, false);

		llyt_Scroll_Demo = (LinearLayout) view.findViewById(R.id.llyt_scroll_demo);

		/*final EditText amountView = view.findViewById(R.id.et_amount);
		final EditText phoneView = view.findViewById(R.id.et_phone);
		final Spinner airtimeView = view.findViewById(R.id.airtime_value);

		airtimeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				MainActivity.airtimeValue = airtimeView.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});*/




		/*Button ctd =  view.findViewById(R.id.btn_ctd);
		ctd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MainActivity.amount = amountView.getText().toString();
				MainActivity.phone = phoneView.getText().toString();
				MainActivity.transType = "VTU";
				MainActivity.transaction_type = MainActivity.transaction_Type_Goods;

				amt = amount;

				getFragmentManager()
						.beginTransaction()
						.replace(R.id.replace_frag, new ConfirmFragment(), "ConfirmFragment")
						.addToBackStack("Confirm")
						.commit();
			}
		});

		Button cancel =  view.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getFragmentManager().popBackStack("VTU",
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}
		});*/

		func = new FuncActivity(this.getActivity());

		func.mHandler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				/*这里是处理信息的方法*/
				System.out.println("SYSTEM ONLINE 1..." + msg.what);
				switch (msg.what)
				{
					case EMV_PROCESS_NEXT_COMPLETED_NOTIFIER:
						if(debug)Log.d(APP_TAG, "EMV_PROCESS_NEXT_COMPLETED_NOTIFIER, emvStatus = " + appState.trans.getEMVStatus() + ", emvRetCode = " + appState.trans.getEMVRetCode());
						byte[] tagData;
						int tagDataLength = 0;
						System.out.println("SYSTEM ONLINE 2..." + appState.trans.getEMVStatus());
						switch (appState.trans.getEMVStatus())
						{
							case STATUS_CONTINUE:
								System.out.println("SYSTEM ONLINE 3..." + appState.trans.getEMVRetCode());
								switch (appState.trans.getEMVRetCode())
								{
									case EMV_CANDIDATE_LIST:
										appState.aidNumber = emv_get_candidate_list(appState.aidList, appState.aidList.length);
										System.out.println("SYSTEM ONLINE 4..." + appState.trans.getEMVRetCode());
										//selectEMVAppList();
										break;
									case EMV_APP_SELECTED:
										System.out.println("SYSTEM ONLINE 5...out..."+appState.getTranType());
										if( appState.getTranType() == QUERY_CARD_RECORD || appState.trans.getTransAmount() > 0)
										{
											System.out.println("SYSTEM ONLINE 6...in");
											mEMVProcessNextThread = new EMVProcessNextThread();
											mEMVProcessNextThread.start();
										}
										else{
											inputAmount();
											System.out.println("SYSTEM ONLINE 7...in");
										}
										break;
									case EMV_READ_APP_DATA:
										System.out.println("SYSTEM ONLINE 8...in");
										if(emv_is_tag_present(0x9F79) >= 0 )
										{
											tagData = new byte[6];
											emv_get_tag_data(0x9F79, tagData, 6);
											appState.trans.setECBalance(ByteUtil.bcdToInt(tagData));
										}

										tagData = new byte[100];
										if(emv_is_tag_present(0x5A) >= 0)
										{
											tagDataLength = emv_get_tag_data(0x5A, tagData, tagData.length);
											appState.trans.setPAN(StringUtil.toString(AppUtil.removeTailF(ByteUtil.bcdToAscii(tagData, 0, tagDataLength))));
											ISOParams.setPans(appState.trans.getPAN());
										}
										// Track2
										if( emv_is_tag_present(0x57) >= 0)
										{
											System.out.println("TRACK 2 Track");
											tagDataLength = emv_get_tag_data(0x57, tagData, tagData.length);
											appState.trans.setTrack2Data(StringUtil.toString(ByteUtil.bcdToAscii(tagData, 0, tagDataLength)));
										}
										// CSN
										if( emv_is_tag_present(0x5F34) >= 0)
										{
											tagDataLength = emv_get_tag_data(0x5F34, tagData, tagData.length);
											appState.trans.setCSN(tagData[0]);
										}
										// Expiry
										if( emv_is_tag_present(0x5F24) >= 0)
										{
											tagDataLength = emv_get_tag_data(0x5F24, tagData, tagData.length);
											appState.trans.setExpiry(StringUtil.toHexString(tagData, 0, 3, false).substring(0, 4));
											ISOParams.setExpiryDate(appState.trans.getExpiry());
										}
										//confirmCard();
										mEMVProcessNextThread = new EMVProcessNextThread();
										mEMVProcessNextThread.start();

										break;
									case EMV_DATA_AUTH:
										System.out.println("SYSTEM ONLINE 9...in");
										byte[] TSI = new byte[2];
										byte[] TVR = new byte[5];
										emv_get_tag_data(0x9B, TSI, 2); // TSI
										emv_get_tag_data(0x95, TVR, 5); // TVR
										if(   (TSI[0] & (byte)0x80) == (byte)0x80
												&& (TVR[0] & (byte)0x40) == (byte)0x00
												&& (TVR[0] & (byte)0x08) == (byte)0x00
												&& (TVR[0] & (byte)0x04) == (byte)0x00
												)
										{
											appState.promptOfflineDataAuthSucc = true;
										}
										mEMVProcessNextThread = new EMVProcessNextThread();
										mEMVProcessNextThread.start();
										break;
									case EMV_OFFLINE_PIN:
										System.out.println("SYSTEM ONLINE 10...in");
										System.out.println("EMV_OFFLINE_PIN...");
										textLine1.setText("PLEASE INPUT PIN ON THE PINPAD");
										mEMVProcessNextThread = new EMVProcessNextThread();
										mEMVProcessNextThread.start();
										break;
									case EMV_ONLINE_ENC_PIN:
										System.out.println("SYSTEM ONLINE 11...in");
										System.out.println("EMV_ONLINE_ENC_PIN...");
										//inputPIN();

										inputPinActivity();
										//generic();
										break;
									case EMV_PIN_BYPASS_CONFIRM:
										System.out.println("SYSTEM ONLINE 12...in");
										//confirmBypassPin();
										break;
									case EMV_PROCESS_ONLINE:
										System.out.println("SYSTEM ONLINE 13...in");
										getEMVCardInfo();
										appState.trans.setEMVOnlineFlag(true);
										requestCode = STATE_PROCESS_ONLINE;
										processResult();
										generic();
										break;
									default:
										System.out.println("SYSTEM ONLINE 14...in");
										mEMVProcessNextThread = new EMVProcessNextThread();
										mEMVProcessNextThread.start();
										break;
								}
								break;
							case STATUS_COMPLETION:
								System.out.println("SYSTEM ONLINE 15...in");
								appState.terminalConfig.incTrace();
								appState.trans.setNeedSignature(emv_is_need_signature());

								tagData = new byte[50];
								if( emv_is_tag_present(0x95) >= 0)
								{
									tagDataLength = emv_get_tag_data(0x95, tagData, tagData.length);
									appState.terminalConfig.setLastTVR(StringUtil.toHexString(tagData, 0, tagDataLength, false));
								}
								if( emv_is_tag_present(0x9B) >= 0)
								{
									tagDataLength = emv_get_tag_data(0x9B, tagData, tagData.length);
									appState.terminalConfig.setLastTSI(StringUtil.toHexString(tagData, 0, tagDataLength, false));
								}

								getEMVCardInfo();
								if ((TransDefine.transInfo[appState.getTranType()].flag & T_NOCAPTURE) == 0)
								{
									if( appState.trans.getEMVRetCode() == APPROVE_OFFLINE )
									{
										if(appState.terminalConfig.getUploadType() == 0)
										{
											if(   appState.trans.getEMVOnlineFlag() == true
													&& appState.trans.getEMVOnlineResult() == ONLINE_FAIL
													)
											{
												//saveAdvice();
											}
											//offlineSuccess();
										}
										else{
											// 需判断是否联机
											if(   appState.trans.getEMVOnlineFlag() == true
													&& appState.trans.getEMVOnlineResult() == ONLINE_FAIL
													)
											{
												// Reversal
												appState.setProcessState(PROCESS_REVERSAL);
												requestCode = STATE_PROCESS_ONLINE;
												processResult();
												generic();
											}
											else{
												// Confirm
												appState.setProcessState(PROCESS_CONFIMATION);
												getEMVCardInfo();
												requestCode = STATE_PROCESS_ONLINE;
												processResult();
												generic();
											}
											return;
										}

									}
									else if(appState.trans.getEMVRetCode() == APPROVE_ONLINE)
									{
										if(appState.terminalConfig.getUploadType() == 0)
										{
											//transSuccess();
										}
										else{
											appState.setProcessState(PROCESS_CONFIMATION);
											getEMVCardInfo();
											requestCode = STATE_PROCESS_ONLINE;
											processResult();
											generic();
											return;
										}
									}
									else{
										if(   appState.trans.getEMVOnlineFlag() == true
												&& appState.trans.getEMVOnlineResult() == ONLINE_FAIL
												)
										{
											// 通讯失败
											if(appState.terminalConfig.getUploadType() == 0)
											{
												//saveAdvice();
											}
											else{
												appState.setProcessState(PROCESS_REVERSAL);
												getEMVCardInfo();
												requestCode = STATE_PROCESS_ONLINE;
												processResult();
												generic();
												return;
											}
										}
										else if(   appState.trans.getEMVOnlineFlag() == true
												&& appState.trans.getEMVOnlineResult() == ONLINE_SUCCESS
												)
										{
											if(emv_is_need_advice() == 1)
											{
												if(appState.terminalConfig.getUploadType() == 0)
												{
													//saveAdvice();
												}
												else{
													appState.setProcessState(PROCESS_ADVICE_ONLINE);
													getEMVCardInfo();
													requestCode = STATE_PROCESS_ONLINE;
													processResult();
													generic();
													return;
												}
											}
											else{
												if(appState.terminalConfig.getUploadType() == 0)
												{
													//saveAdvice();
												}
												else{
													appState.setProcessState(PROCESS_REVERSAL);
													getEMVCardInfo();
													requestCode = STATE_PROCESS_ONLINE;
													processResult();
													generic();
													return;
												}
											}
										}
										else{
											if(emv_is_need_advice() == 1)
											{
												if(appState.terminalConfig.getUploadType() == 0)
												{
													//saveAdvice();
												}
												else{
													appState.setProcessState(PROCESS_ADVICE_ONLINE);
													getEMVCardInfo();
													requestCode = STATE_PROCESS_ONLINE;
													processResult();
													generic();
													return;
												}
											}
										}
									}
									appState.setProcessState(PROCESS_NORMAL);
								}
								//setResult(RESULT_OK, getIntent());
								//finish();

								break;
							default:
								System.out.println("SYSTEM ONLINE 16...in");
								switch (appState.trans.getEMVRetCode())
								{
//    					case ERROR_NO_APP:
//    					case ERROR_INIT_APP:
//    						//appState.trans.setEmvCardError(true);
//    						//setResult(RESULT_OK, getIntent());
//    						appState.setErrorCode(R.string.error_no_app);
//    						finish();
//    						break;
									case ERROR_OTHER_CARD:
										appState.trans.setEmvCardError(true);
										//setResult(RESULT_OK, getIntent());
										appState.setErrorCode(R.string.error_other_card);
										//finish();
										break;
									case ERROR_EXPIRED_CARD:
										appState.setErrorCode(R.string.error_expiry_card);
										//finish();
										break;
									case ERROR_CARD_BLOCKED:
										appState.setErrorCode(R.string.error_card_blocked);
										//finish();
										break;
									case ERROR_APP_BLOCKED:
										appState.setErrorCode(R.string.error_app_blocked);
										//finish();
										break;
									case ERROR_SERVICE_NOT_ALLOWED:
										appState.setErrorCode(R.string.error_not_accepted);
										//finish();
										break;
									case ERROR_PINENTERY_TIMEOUT:
										appState.setErrorCode(R.string.error_pin_timeout);
										//finish();
										break;
									default:
										appState.setErrorCode(R.string.error_ic );
										//finish();
										break;
								}
								break;
						}
						break;
					case PREPROCESS_ERROR_NOTIFIER:
						System.out.println("SYSTEM ONLINE 17...in");
						if(appState.getErrorCode() == 0)
							appState.setErrorCode(R.string.error_pre_process);
						//finish();
						break;
				}
			}
		};

		mEMVThread = new EMVThread();
		mEMVThread.start();


		if (func.waitContactCard() == 0 ) {
			PutMessage("<<< PLEASE INSERT CARD");
		}

		return view;
	}


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		resultCode = Activity.RESULT_OK;


	}


	private void generic()
	{
		//if(debug)Log.d(APP_TAG, "ProcessEMVCard onActivityResult, requesCode=" + requestCode + ",resultCode=" + resultCode);
		if(appState.getProcessState() != PROCESS_NORMAL)
		{
			if(   appState.getProcessState() == PROCESS_REVERSAL
					&& appState.trans.getEMVRetCode() == APPROVE_OFFLINE
					)
			{
				appState.setProcessState(PROCESS_CONFIMATION);
				getEMVCardInfo();
				requestCode = STATE_PROCESS_ONLINE;
				processResult();
				//generic();
			}
			else if(   emv_is_need_advice() == 1
					&& appState.getProcessState() != PROCESS_ADVICE_ONLINE
					)
			{
				appState.setProcessState(PROCESS_ADVICE_ONLINE);
				getEMVCardInfo();
				requestCode = STATE_PROCESS_ONLINE;
				processResult();
				//generic();
			}
			else{
				appState.setProcessState(PROCESS_NORMAL);
				//setResult(Activity.RESULT_OK, getIntent());
				//exit();
			}
			return;
		}
		else{
			if(appState.getErrorCode() > 0)
			{
				if( requestCode == STATE_PROCESS_ONLINE )
				{
					appState.trans.setEMVOnlineResult(ONLINE_FAIL);
					emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), new byte[]{' '}, 0);
					mEMVProcessNextThread = new EMVProcessNextThread();
					mEMVProcessNextThread.start();
					return;
				}
				//exit();
				return;
			}
			if(resultCode != Activity.RESULT_OK)
			{

				if( requestCode == STATE_PROCESS_ONLINE )
				{
					appState.trans.setEMVOnlineResult(ONLINE_FAIL);
					emv_set_online_result(ONLINE_FAIL, appState.trans.getResponseCode(), new byte[]{' '}, 0);
					mEMVProcessNextThread = new EMVProcessNextThread();
					mEMVProcessNextThread.start();
					return;
				}
				//setResult(resultCode, getIntent());
				//exit();
				return;
			}
			if(requestCode == STATE_INPUT_AMOUNT)
			{
				func.setEMVTransAmount(String.valueOf(appState.trans.getTransAmount()));
				emv_set_other_amount(new byte[]{'0', 0x00});
			}
			else if(requestCode == STATE_INPUT_PIN)
			{
				if(appState.trans.getPinEntryMode() == CAN_PIN)
				{
					emv_set_online_pin_entered(1);
				}
				else{
					emv_set_online_pin_entered(0);
				}
				//doVTU();
			}
		}
		mEMVProcessNextThread = new EMVProcessNextThread();
		mEMVProcessNextThread.start();
	}

	@Override
	public void onStart() {
		super.onStart();
		//textLine1.setText("PROCESSING CARD，PLS WAITING...");
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}


	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(debug)Log.d(APP_TAG, "ProcessEMVCard onActivityResult, requesCode=" + requestCode + ",resultCode=" + resultCode);
		if(appState.getProcessState() != PROCESS_NORMAL)
		{
			if(   appState.getProcessState() == PROCESS_REVERSAL
					&& appState.trans.getEMVRetCode() == APPROVE_OFFLINE
					)
			{
				appState.setProcessState(PROCESS_CONFIMATION);
				getEMVCardInfo();
				processOnline();
			}
			else if(   emv_is_need_advice() == 1
					&& appState.getProcessState() != PROCESS_ADVICE_ONLINE
					)
			{
				appState.setProcessState(PROCESS_ADVICE_ONLINE);
				getEMVCardInfo();
				processOnline();
			}
			else{
				appState.setProcessState(PROCESS_NORMAL);
				setResult(Activity.RESULT_OK, getIntent());
				exit();
			}
			return;
		}
		else{
			if(appState.getErrorCode() > 0)
			{
				if( requestCode == STATE_PROCESS_ONLINE )
				{
					appState.trans.setEMVOnlineResult(ONLINE_FAIL);
					emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), new byte[]{' '}, 0);
					mEMVProcessNextThread = new EMVProcessNextThread();
					mEMVProcessNextThread.start();
					return;
				}
				exit();
				return;
			}
			if(resultCode != Activity.RESULT_OK)
			{

				if( requestCode == STATE_PROCESS_ONLINE )
				{
					appState.trans.setEMVOnlineResult(ONLINE_FAIL);
					emv_set_online_result(ONLINE_FAIL, appState.trans.getResponseCode(), new byte[]{' '}, 0);
					mEMVProcessNextThread = new EMVProcessNextThread();
					mEMVProcessNextThread.start();
					return;
				}
				setResult(resultCode, getIntent());
				exit();
				return;
			}
			if(requestCode == STATE_INPUT_AMOUNT)
			{
				setEMVTransAmount(Integer.toString(appState.trans.getTransAmount()));
				emv_set_other_amount(new byte[]{'0', 0x00});
			}
			else if(requestCode == STATE_INPUT_PIN)
			{
*//*
				getEMVCardInfo();

				//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY/MM/dd");
				SimpleDateFormat dtf = new SimpleDateFormat("yyyy/MM/dd");
				//LocalDate localDate = LocalDate.now();
				Date localDate = new Date();
				String newDate = (dtf.format(localDate)).replace("/", "");
				String tRansDate = newDate.substring(2, newDate.length());
				// System.out.println(dtf.format(localDate)); //2016/11/16

				String terminalCountryCode = "0566";

				String output = "4F07"+appState.trans.getAID()
						+"8202"+appState.trans.getAIP()
						+"5F3401"+appState.trans.getPAN().substring(6, 8)
						+"9A03"+tRansDate+"9C0131"//+appState.trans.getTransTypeStr()
						+"5F2A02"+"0566"+"9F0206000000000000"//+appState.trans.getTransAmountStr()
						+"9F0306000000000000"//+appState.trans.getOtherAmountStr()
						+"9F1A02"+terminalCountryCode+"5F2A02"+terminalCountryCode
						+"9F3501"+appState.trans.getTerminalType()+"9F3704"+appState.trans.getUnpredictableNumber()
						+"9F3303"+appState.trans.gettCapability()
						+"95054080041800"//+appState.trans.getTVR()
						+"9F3403"+appState.trans.getCVM();
				System.out.println("getthefield..."+output);*//*

				if(appState.trans.getPinEntryMode() == CAN_PIN)
				{
					emv_set_online_pin_entered(1);
				}
				else{
					emv_set_online_pin_entered(0);
				}
			}
		}
		mEMVProcessNextThread = new EMVProcessNextThread();
		mEMVProcessNextThread.start();
	}*/


	private void inputAmount(){
		requestCode = STATE_INPUT_AMOUNT;
		resultCode = Activity.RESULT_OK;
		System.out.println("inputAmount....");
		generic();
	}

	private void getEMVCardInfo()
	{
		byte[] tagData = new byte[100];
		int tagDataLength = 0;

		byte[] iccData = new byte[1200];
		int offset = 0;
		if(appState.getProcessState() == PROCESS_CONFIMATION)
		{
			offset = emv_get_tag_list_data(confirmTagList, confirmTagList.length, iccData, iccData.length);
		}
		else{
			offset = emv_get_tag_list_data(defaultTagList, defaultTagList.length, iccData, iccData.length);
		}
		appState.trans.setICCData(iccData, 0, offset);
		System.out.println("icc icc data..."+appState.trans.getICCData());
		int id5f34 = appState.trans.getICCData().indexOf("5F3401");
		ISOParams.setSeqNos(appState.trans.getICCData().substring(id5f34+6, id5f34+8));

		// Application Label 50
		if( emv_is_tag_present(0x50) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x50, tagData, tagData.length);
			byte[] appLabel = new byte[tagDataLength];
			System.arraycopy(tagData, 0, appLabel, 0, appLabel.length);
			appState.trans.setAppLabel(StringUtil.toString(appLabel));
		}

		// AIP
		if( emv_is_tag_present(0x82) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x82, tagData, tagData.length);
			appState.trans.setAIP(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		// TVR
		if( emv_is_tag_present(0x95) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x95, tagData, tagData.length);
			appState.trans.setTVR(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		// TSI
		if( emv_is_tag_present(0x9B) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9B, tagData, tagData.length);
			appState.trans.setTSI(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		// Application Identifier terminal
		if( emv_is_tag_present(0x9F06) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F06, tagData, tagData.length);
			appState.trans.setAID(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		// IAD
		if( emv_is_tag_present(0x9F10) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F10, tagData, tagData.length);
			appState.trans.setIAD(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		// ApplicationPreferredName  9F12
		if( emv_is_tag_present(0x9F12) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F12, tagData, tagData.length);
			byte[] appName = new byte[tagDataLength];
			System.arraycopy(tagData, 0, appName, 0, appName.length);
			appState.trans.setAppName(StringUtil.toString(appName));
		}

		if(emv_is_tag_present(0x9F26) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F26, tagData, tagData.length);
			appState.trans.setAC(StringUtil.toHexString(tagData, 0, tagDataLength, false));
			System.out.println("App Crypto..1." + appState.trans.getAC());
		}

		if(emv_is_tag_present(0x9F27) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F27, tagData, tagData.length);
			appState.trans.setCryptCode(StringUtil.toHexString(tagData, 0, tagDataLength, false));
			System.out.println("App Crypto..2." + StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9F36) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F36, tagData, tagData.length);
			appState.trans.settCounter(StringUtil.toHexString(tagData, 0, tagDataLength, false));
			System.out.println("App Crypto..3." + StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9F34) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F34, tagData, tagData.length);
			appState.trans.setCVM(StringUtil.toHexString(tagData, 0, tagDataLength, false));
			System.out.println("App Crypto..4." + StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9F37) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F37, tagData, tagData.length);
			appState.trans.setUnpredictableNumber(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(   emv_is_tag_present(0x9F79) >= 0
				&& appState.trans.getECBalance() < 0
				)
		{
			tagDataLength = emv_get_tag_data(0x9F79, tagData, tagData.length);
			byte[] amt = new byte[tagDataLength];
			System.arraycopy(tagData, 0, amt, 0, amt.length);
			appState.trans.setECBalance(ByteUtil.bcdToInt(amt));
		}


		//////////////////////////////////////
		if(emv_is_tag_present(0x9A) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9A, tagData, tagData.length);
			appState.trans.setTransDate(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9C) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9C, tagData, tagData.length);
			appState.trans.setTransTypeStr(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9F02) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F02, tagData, tagData.length);
			appState.trans.setTransAmountStr(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9F03) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F03, tagData, tagData.length);
			appState.trans.setOtherAmountStr(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9F1A) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F1A, tagData, tagData.length);
			appState.trans.setCountryCode(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9F27) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F27, tagData, tagData.length);
			appState.trans.setCryptCode(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9F36) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F36, tagData, tagData.length);
			appState.trans.settCounter(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9F35) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F35, tagData, tagData.length);
			appState.trans.setTerminalType(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9F33) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F33, tagData, tagData.length);
			appState.trans.settCapability(StringUtil.toHexString(tagData, 0, tagDataLength, false));
		}

		if(emv_is_tag_present(0x9F1C) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x9F1C, tagData, tagData.length);
			appState.trans.setTid(StringUtil.toHexString(tagData, 0, tagDataLength, false));
			System.out.println("TID......."+appState.trans.getTid());
		}

		if(emv_is_tag_present(0x5F2A) >= 0)
		{
			tagDataLength = emv_get_tag_data(0x5F2A, tagData, tagData.length);
			appState.trans.setCurrCode(StringUtil.toHexString(tagData, 0, tagDataLength, false));
			System.out.println("Currency......."+appState.trans.getCurrCode());
		}

	}

	public static void setEMVData()
	{
		if(appState.getTranType() == QUERY_CARD_RECORD)
		{
			emv_set_trans_amount(new byte[]{'0', 0x00});
			emv_set_other_amount(new byte[]{'0', 0x00});
			if(appState.recordType == 0x00)
			{
				emv_set_trans_type(EMV_TRANS_CARD_RECORD);
			}
			else
			{
				emv_set_trans_type(EMV_TRANS_LOAD_RECORD);
			}
		}
		else{
			emv_set_tag_data(0x9A,   StringUtil.hexString2bytes(appState.trans.getTransDate().substring(2)), 3);
			emv_set_tag_data(0x9F21, StringUtil.hexString2bytes(appState.trans.getTransTime()), 3);
			emv_set_tag_data(0x9F41, StringUtil.hexString2bytes(StringUtil.fillZero(Integer.toString(appState.trans.getTrace()), 8)), 4);

			emv_set_trans_type(EMV_TRANS_GOODS_SERVICE);
		}
	}

	private void timeInit(){

		//initialize trans data
		//appState.setTranType(TRAN_GOODS);
		//appState.trans.setTransType(TRAN_GOODS);
		appState.getCurrentDateTime();
		appState.trans.setTransDate(   appState.currentYear
				+ StringUtil.fillZero(Integer.toString(appState.currentMonth), 2)
				+ StringUtil.fillZero(Integer.toString(appState.currentDay), 2)
		);
		appState.trans.setTransTime(   StringUtil.fillZero(Integer.toString(appState.currentHour), 2)
				+ StringUtil.fillZero(Integer.toString(appState.currentMinute), 2)
				+ StringUtil.fillZero(Integer.toString(appState.currentSecond), 2)
		);
	}

	class EMVThread extends Thread
	{
		public void run()
		{
			super.run();
			emv_trans_initialize();
			emv_set_kernel_type(appState.trans.getEMVKernelType());
			func.setEMVTransAmount(String.valueOf(appState.trans.getTransAmount()));
			timeInit();
			setEMVData();

			//pre-process
			if(appState.trans.getEMVKernelType() == QPBOC_KERNAL && !func.preProcessQpboc())
			{
				Message msg = new Message();
				msg.what = PREPROCESS_ERROR_NOTIFIER;
				func.mHandler.sendMessage(msg);
				return;
			}
			emv_process_next();
		}
	}

	class EMVProcessNextThread extends Thread
	{
		public void run()
		{
			super.run();
			emv_process_next();
		}
	}


	public String newgettheField(){
		//Setting Amount

		System.out.println("appState.trans.getICCData()..."+appState.trans.getICCData());

		String amountField = "0";
		//String paddedAmount = Utils.leftPad(amountField, '0', (short) 12);

		//Setting Date

		//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY/MM/dd");
		SimpleDateFormat dtf = new SimpleDateFormat("yyyy/MM/dd");
		//LocalDate localDate = LocalDate.now();
		Date localDate = new Date();
		String newDate = (dtf.format(localDate)).replace("/", "");
		// System.out.println(dtf.format(localDate)); //2016/11/16

		int  id = appState.trans.getICCData().indexOf("8407");
		// System.out.print("\nID is " + id + "\n");
		String AID = appState.trans.getICCData().substring(id + 4, id+18);
		// System.out.print("\nAID is " + AID + "\n");
		int id82 = appState.trans.getICCData().indexOf("8202");
		String AIP82 = appState.trans.getICCData().substring(id82+4, id82+8);
		//String AIP82 = "3800";

		int id5f34 = appState.trans.getICCData().indexOf("5F3401");
		String PANSN = appState.trans.getICCData().substring(id5f34+6, id5f34+8);
		int idDate = appState.trans.getICCData().indexOf("9A03");
		//String tRansDate = appState.trans.getICCData().substring(idDate+4, idDate+10);
		//String tRansDate = newDate;
		String tRansDate = newDate.substring(2, newDate.length());
		// System.out.print("New Date is =>"+ tRansDate);
		int id9C = appState.trans.getICCData().indexOf("9C01");

		String tRansType = appState.trans.getICCData().substring(id9C+4, id9C+6);
		//String tRansType = "31";
		// String tRansType = "00";

		int id5F2A = appState.trans.getICCData().indexOf("5F2A02");
		String countryCode = "0566";
		int id9F02 = appState.trans.getICCData().indexOf("9F0206");

		String aMount =appState.trans.getICCData().substring(id9F02+6, id9F02+18);

		System.out.println("aMount aMount..."+aMount);
		// String aMount = paddedAmount;
		int id9F03 = appState.trans.getICCData().indexOf("9F0306");
		String otheramount = appState.trans.getICCData().substring(id9F03+6, id9F03+18);

		int id9F1A = appState.trans.getICCData().indexOf("9F1A02");
		String terminalCountryCode = "0566";
		int id9F27 = appState.trans.getICCData().indexOf("9F2701");
		String cryptCode = appState.trans.getICCData().substring(id9F27+6, id9F27+8);
		int id9F36 = appState.trans.getICCData().indexOf("9F3602");
		String tCounter = appState.trans.getICCData().substring(id9F36+6, id9F36+10);
		int id9F26 = appState.trans.getICCData().indexOf("9F2608");
		String aCryptogram = appState.trans.getICCData().substring(id9F26+6, id9F26+22);
		int id9F10 = appState.trans.getICCData().indexOf("9F10");

		String issuerLength = appState.trans.getICCData().substring(id9F10+4, id9F10+6);
		int Length = Integer.parseInt(issuerLength);
		System.out.print("Issuer Length is-> "+Length);

		id9F10 = appState.trans.getICCData().indexOf("9F10"+Length);
		String issuerAD = "0110A00003220000000000000000000000FF";//appState.trans.getICCData().substring(id9F10+6, id9F10+42);


		int id9F35  = appState.trans.getICCData().indexOf("9F3501");
		String tErminaltype = appState.trans.getICCData().substring(id9F35+6, id9F35+8);
		//String tErminaltype = "22";
		int id9F37 = appState.trans.getICCData().indexOf("9F3704");
		String NumberP = appState.trans.getICCData().substring(id9F37+6, id9F37+14);
		//String NumberP = rand;

		int id95 = appState.trans.getICCData().indexOf("9505");
		String tVR = appState.trans.getICCData().substring(id95+4, id95+14);
		//String tVR = "0880048000";
		int id9F34 =appState.trans.getICCData().indexOf("9F3403");
		String cVM = appState.trans.getICCData().substring(id9F34+6, id9F34+12);
		//String cVM = "020300";
		int id9F33 = appState.trans.getICCData().indexOf("9F3303");

		String tCapability = appState.trans.getICCData().substring(id9F33+6, id9F33+12);
		// String tCapability = "E0F8C8";

		ISOParams.setSeqNos(PANSN);


		//String output = "4F07"+AID+"8202"+AIP82+"5F3401"+PANSN+"9A03"+tRansDate+"9C01"+tRansType+"5F2A02"+"0566"+"9F0206"+aMount+"9F0306"+otheramount+"9F1A02"+terminalCountryCode+"9F2701"+cryptCode+"9F3602"+tCounter+"9F2608"+aCryptogram+"9F1012"+issuerAD+"9F3501"+tErminaltype+"9F3704"+NumberP+"9F3303"+tCapability+"9505"+tVR+"9F3403"+cVM;


		String pan = appState.trans.getPAN();
		//get the pan and use it to identify Eransact Card
		String word = (String) pan.subSequence(0, 6);
		String text = card_check;
		Boolean found;
		found = text.contains(word);

		//if the card is an etransact card, assign a new alias
		String output;
		if(found.equals(true))
		{
			Log.w("gettheField()","Etransact Cards Found");
			//Etransact Cards
			output = "4F07"+AID+"8202"+AIP82+"5F3401"+PANSN+"9A03"+tRansDate+"9C01"+tRansType+"9F0206"+aMount+"9F0306"+otheramount+"9F1A02"+terminalCountryCode+"5F2A02"+terminalCountryCode+ "9F3501"+tErminaltype+"9F3704"+NumberP+"9F3303"+tCapability+"9505"+tVR+"9F3403"+cVM;

		} else
		{
			Log.w("gettheField()","Other Cards Found");
			//Other Cards
			output = "4F07"+AID+"8202"+AIP82+"5F3401"+PANSN+"9A03"+tRansDate+"9C01"+tRansType+"5F2A02"+"0566"+"9F0206"+aMount+"9F0306"+otheramount+"9F1A02"+terminalCountryCode+"5F2A02"+terminalCountryCode+  "9F2701"+cryptCode+"9F3602"+tCounter+"9F2608"+aCryptogram+"9F1012"+issuerAD+"9F3501"+tErminaltype+"9F3704"+NumberP+"9F3303"+tCapability+"9505"+tVR+"9F3403"+cVM;
		}

		String icc125 = "";
		Log.w("getTheField()",output);
		System.out.println("DAYO 2..."
				+"\nAID..."+"4F07"+AID
				+"\nAIP82..."+"8202"+AIP82
				+"\nPANSN..."+"5F3401"+PANSN
				+"\ntRansDate..."+"9A03"+tRansDate
				+"\ntRansType..."+"9C01"+tRansType
				+"\naMount..."+"5F2A02"+"0566"+"9F0206"+aMount
				+"\notheramount..."+"9F0306"+otheramount
				+"\nterminalCountryCode..."+"9F1A02"+terminalCountryCode
				+"\nterminalCountryCode..."+"5F2A02"+terminalCountryCode
				+"\ncryptCode..."+"9F2701"+cryptCode
				+"\ntCounter..."+"9F3602"+tCounter
				+"\naCryptogram..."+"9F2608"+aCryptogram
				+"\nissuerAD..."+"9F1012"+issuerAD
				+"\ntErminaltype..."+tErminaltype
				+"\nNumberP..."+"9F3704"+NumberP
				+"\ntCapability..."+"9F3303"+tCapability
				+"\ntVR..."+"9505"+tVR
				+"\ncVM..."+"9F3403"+cVM
		);
		return output;
	}


















////////////PROCESS ONLINE///////////////////////////////
private void processResult()
{
	if(debug)Log.d(APP_TAG, "processResult");
	appState.trans.setTrace(appState.terminalConfig.getTrace());
	appState.trans.setResponseCode(new byte[]{'0','0'});
	appState.terminalConfig.incTrace();

	switch(appState.getProcessState())
	{
		case PROCESS_NORMAL:
			if (    appState.trans.getResponseCode() != null
					&&	appState.trans.getResponseCode()[0] == '0'
					&& appState.trans.getResponseCode()[1] == '0'
					)
			{
				if(appState.trans.getEMVOnlineFlag() == true)
				{
					appState.trans.setEMVOnlineResult(ONLINE_SUCCESS);
					byte[] issuerData = appState.trans.getIssuerAuthData();
					if(issuerData != null && issuerData.length > 0)
					{
						emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), issuerData, issuerData.length);
					}
					else
					{
						emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), new byte[]{' '}, 0);
					}
				}
				break;
			}
			else if(   appState.trans.getResponseCode() != null
					&& appState.trans.getResponseCode()[0] == 'F'
					&& appState.trans.getResponseCode()[1] == 'F'
					)
			{
				appState.trans.setEMVOnlineResult(ONLINE_FAIL);
				emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), new byte[]{' '}, 0);
			}
			else{
				appState.trans.setEMVOnlineResult(ONLINE_DENIAL);
				byte[] issuerData = appState.trans.getIssuerAuthData();
				if(issuerData != null && issuerData.length > 0)
				{
					emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), issuerData, issuerData.length);
				}
				else
				{
					emv_set_online_result(appState.trans.getEMVOnlineResult(), appState.trans.getResponseCode(), new byte[]{' '}, 0);
				}
			}
			break;
		case PROCESS_ADVICE_OFFLINE:
			break;
	}
	if(debug)Log.d(APP_TAG, "ProcessOnlineActivity finish success");
	//setResult(Activity.RESULT_OK, getIntent());
	//exit();
}











	////////////////input Activity/////////////////////////////////

	private void inputPinActivity(){
		requestCode = STATE_INPUT_PIN;
		mReadPINThread=new ReadPINThread();
		mReadPINThread.start();
		PutMessage("Please input pin");
		appState.trans.setTransAmount(1);

		mmHandler = new Handler()
		{
			public void handleMessage(Message msg)
			{ /*这里是处理信息的方法*/
				switch (msg.what)
				{
					case PIN_SUCCESS_NOTIFIER:
						getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent());
						break;
					case PIN_ERROR_NOTIFIER:
						appState.setErrorCode(R.string.error_pinpad);
						break;
					case PIN_CANCELLED_NOTIFIER:
						appState.setErrorCode(R.string.error_user_cancelled);
						break;
					case PIN_TIMEOUT_NOTIFIER:
						appState.setErrorCode(R.string.error_input_timeout);
						break;
				}
				//func.exit();
			}
		};



	}

	private final int PINPAD_CANCEL  = -65792;
	private final int PINPAD_TIMEOUT = -65538;

	private Handler mmHandler;
	private Thread mReadPINThread;


	private void notifyPinSuccess()
	{
		Message msg = new Message();
		msg.what = PIN_SUCCESS_NOTIFIER;
		mmHandler.sendMessage(msg);
		requestCode = STATE_INPUT_PIN;
		generic();
	}

	private void notifyPinError()
	{
		Message msg = new Message();
		msg.what = PIN_ERROR_NOTIFIER;
		mmHandler.sendMessage(msg);
	}

	private void notifyPinCancel()
	{
		Message msg = new Message();
		msg.what = PIN_CANCELLED_NOTIFIER;
		mmHandler.sendMessage(msg);
	}

	private void notifyPinTimeout()
	{
		Message msg = new Message();
		msg.what = PIN_TIMEOUT_NOTIFIER;
		mmHandler.sendMessage(msg);
	}

	protected char[] stars = "●●●●●●●●●●●●●●●●".toCharArray();
	public static final int PIN_AMOUNT_SHOW  = 0x10000;
	public static final int PIN_KEY_CALLBACK = 0x10001;
	private Handler commHandler = createCommHandler();

	public void processCallback(byte[] data) {
		Log.i("processCallback", "" + data);
		if(data != null)
			commHandler.obtainMessage(PIN_KEY_CALLBACK, data[0], data[1]).sendToTarget();
	}


	@SuppressLint("HandlerLeak")
	protected Handler createCommHandler()
	{	// 无 Pinpad时跳过. DuanCS@[20141001]
		return new Handler()
		{
			public void handleMessage(Message msg)
			{ /* 这里是处理信息的方法 */
				switch (msg.what)
				{
					case PIN_AMOUNT_SHOW:	// 其值已通过onFlush显示. DuanCS@[20150907]
//					setTextById(R.id.amount, msg.obj.toString());
						PutMessage(msg.obj.toString());
						//textPin.setText(msg.obj.toString());	// 这一行也不会执行, 因为 Pinpad.showText() 不会触发回调... DuanCS@[20150912]
						break;
					case PIN_KEY_CALLBACK:
						PutMessage(msg.obj.toString());
						//textPin.setText(stars, 0, msg.arg1 & 0x0F);
						break;
				}
			}
		};
	}
	class ReadPINThread extends Thread
	{
		public void run()
		{
			byte[] pinBlock = new byte[8];
			byte[] zeroPAN = new byte[]{'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'};

			// masterKey is new byte[]{'1','1','1','1','1','1','1','1' }
			//Q1上不支持单倍长PINKEY
			byte[] defaultPINKey = new byte[]{'2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2' };

			int ret = -1;
			if(appState.pinpadOpened == false)
			{
				if(PinPadInterface.open() < 0)
				{
					notifyPinError();
					return;
				}
				appState.pinpadOpened = true;

				//PinPadInterface.setupCallbackHandler(InputPINActivity.this);
			}

			ret = PinPadInterface.updateUserKey(appState.terminalConfig.getKeyIndex(),
					0,
					defaultPINKey,
					defaultPINKey.length);
			if(ret < 0)
			{
				if(debug)Log.d(APP_TAG, "pinpad open error");
				notifyPinError();
				PinPadInterface.close();
				appState.pinpadOpened = false;
				return;
			}
			//Q1上不支持单倍长PINKEY
			PinPadInterface.setKey(2, appState.terminalConfig.getKeyIndex(), 0, DOUBLE_KEY);
//    		PinPadInterface.setKey(2, appState.terminalConfig.getKeyIndex(), 0, appState.terminalConfig.getKeyAlgorithm());
			if(appState.trans.getTransAmount() > 0)
			{
				byte[] text = (AppUtil.formatAmount(appState.trans.getTransAmount())).getBytes();
				PinPadInterface.setText(0, text, text.length, 0);
			}
			ret = PinPadInterface.inputPIN(zeroPAN, zeroPAN.length, pinBlock, 60000, 0);
			if(ret < 0)
			{
				if(ret == PINPAD_CANCEL)
				{
					notifyPinCancel();
				}
				else if(ret == PINPAD_TIMEOUT)
				{
					notifyPinTimeout();
				}
				else{
					notifyPinError();
				}
				PinPadInterface.close();
				appState.pinpadOpened = false;
				return;
			}
			if(ret == 0)
			{
				appState.trans.setPinEntryMode(CANNOT_PIN);
			}
			else
			{
				appState.trans.setPinBlock(pinBlock);
				appState.trans.setPinEntryMode(CAN_PIN);
				ISOParams.setPinBlock(StringUtil.toHexString(pinBlock, 0, pinBlock.length, false));
			}
			notifyPinSuccess();
			PinPadInterface.close();
			appState.pinpadOpened = false;
		}
	}

	public  void PutMessage(String string)
	{

		TextView lb = new TextView(getActivity());
		lb.setTextSize(16);
		lb.setText(string);

		if (llyt_Scroll_Demo.getChildCount() > 0)
			llyt_Scroll_Demo.addView(lb);
		else
			llyt_Scroll_Demo.addView(lb);
		if (llyt_Scroll_Demo.getChildCount() > 500)
		{
			llyt_Scroll_Demo.removeViewAt(llyt_Scroll_Demo.getChildCount() - 1);
		}
		llyt_Scroll_Demo.invalidate();

	}

}
