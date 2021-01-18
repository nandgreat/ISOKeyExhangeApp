package com.mpos.newthree.printer;

import com.android.common.utils.StringUtils;

import java.io.Serializable;

public class PurchaseBillForQ1Entity implements Serializable {
	private static final long serialVersionUID = 1L;

//	private String merchantCopy;			//Merchant stub

	private String merchantName;		//Merchant name		商户名称

	private String merchantNo;				//Merchant No	商户编号

	private String terminalNoAndOperator;				//Terminal No.  and Operator   终端编号, 操作员号

//	private String operator;				//Operator No  操作员号

	private String cardNo;						//Card No	卡号

	private String issuerAndAcquirer;							//Issuer, can be empty.Acquirer, can be empty. 发卡机构,可空, 收单机构,可空

//	private String acquirer;						//Acquirer, can be empty.	收单机构,可空

	private String transType;					//the type of transaction	交易类型
	
	public String getIssuerAndAcquirer() {
		return issuerAndAcquirer;
	}

	public void setIssuerAndAcquirer(String issuerAndAcquirer) {
		this.issuerAndAcquirer = issuerAndAcquirer;
	}

	public String getDataTimeAndExpDate() {
		return dataTimeAndExpDate;
	}

	public void setDataTimeAndExpDate(String dataTimeAndExpDate) {
		this.dataTimeAndExpDate = dataTimeAndExpDate;
	}

	public String getRefNoAndBatchNo() {
		return refNoAndBatchNo;
	}

	public void setRefNoAndBatchNo(String refNoAndBatchNo) {
		this.refNoAndBatchNo = refNoAndBatchNo;
	}

	public String getVoucherAndAuthNo() {
		return voucherAndAuthNo;
	}

	public void setVoucherAndAuthNo(String voucherAndAuthNo) {
		this.voucherAndAuthNo = voucherAndAuthNo;
	}

	private String dataTimeAndExpDate;					//Date Time  Expiry Date	日期和有效期

//	private String expDate;						//Expiry Date  can be empty
	
	private String refNoAndBatchNo;							//Transaction NO and Batch NO 交易参考号 批次号

//	private String batchNo;						//批次号
 
	private String voucherAndAuthNo;					//	Voucher NO and Authorization code  凭证号 授权码

//	private String authNo;						//授权码,可空

	private String amout;							//Money  金额
	
	private String reference;					//Note, can be empty.  备注,可空

	public boolean checkValidity() {

		if (StringUtils.isEmpty(merchantName)
				|| StringUtils.isEmpty(merchantNo)
				|| StringUtils.isEmpty(terminalNoAndOperator)
				|| StringUtils.isEmpty(cardNo)
				|| StringUtils.isEmpty(transType)
				|| StringUtils.isEmpty(dataTimeAndExpDate)
				|| StringUtils.isEmpty(refNoAndBatchNo) 
				|| StringUtils.isEmpty(voucherAndAuthNo)
				|| StringUtils.isEmpty(amout)) {
			return false;
		} else
			return true;
	}

//	public String getMerchantCopy() {
//		return merchantCopy;
//	}
//
//	public void setMerchantCopy(String merchantCopy) {
//		this.merchantCopy = merchantCopy;
//	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getTerminalNoAndOperator() {
		return terminalNoAndOperator;
	}

	public void setTerminalNoAndOperator(String terminalNoAndOperator) {
		this.terminalNoAndOperator = terminalNoAndOperator;
	}

	public String getAmout() {
		return amout;
	}

	public void setAmout(String amout) {
		this.amout = amout;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
}
