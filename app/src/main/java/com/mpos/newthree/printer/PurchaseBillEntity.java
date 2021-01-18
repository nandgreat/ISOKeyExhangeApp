package com.mpos.newthree.printer;

import com.android.common.utils.StringUtils;

import java.io.Serializable;

public class PurchaseBillEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String merchantName;

	private String merchantNo;

	private String terminalNo;

	private String operator;

	private String cardNumber;

	private String issNo;

	private String acqNo;

	private String txnType;

	private String expDate;// selectable 可选

	private String batchNo;

	private String voucherNo;

	private String authNo;// selectable   可选

	private String dataTime;

	private String refNo;

	private String amout;

	private String tips;// selectable	可选

	private String total;// selectable	可选

	private String reference;// selectable	可选

	public PurchaseBillEntity() {
	}

	public boolean checkValidity() {

		if (StringUtils.isEmpty(merchantName)
				|| StringUtils.isEmpty(merchantNo)
				|| StringUtils.isEmpty(terminalNo)
				|| StringUtils.isEmpty(operator)
				|| StringUtils.isEmpty(cardNumber)
				|| StringUtils.isEmpty(issNo) || StringUtils.isEmpty(acqNo)
				|| StringUtils.isEmpty(txnType)
				|| StringUtils.isEmpty(batchNo)
				|| StringUtils.isEmpty(voucherNo)
				|| StringUtils.isEmpty(dataTime)
				|| StringUtils.isEmpty(refNo) || StringUtils.isEmpty(amout)) {
			return false;
		} else
			return true;
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

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getIssNo() {
		return issNo;
	}

	public void setIssNo(String issNo) {
		this.issNo = issNo;
	}

	public String getAcqNo() {
		return acqNo;
	}

	public void setAcqNo(String acqNo) {
		this.acqNo = acqNo;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getAuthNo() {
		return authNo;
	}

	public void setAuthNo(String authNo) {
		this.authNo = authNo;
	}

	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getAmout() {
		return amout;
	}

	public void setAmout(String amout) {
		this.amout = amout;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
}
