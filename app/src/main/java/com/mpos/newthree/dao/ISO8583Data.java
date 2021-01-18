/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpos.newthree.dao;

/**
 *
 * @author SUNNYben
 */
public class ISO8583Data {    
    private String mti;
    private String pan_2;
    private String procCode_3;
    private String amount_4;
    private String transDateTime_7;
    private String stan_11;
    private String transTime_12;
    private String transDate_13;
    private String expiryDate_14;
    private String settlementPeriod_15;
    private String merchantType_18;
    private String posEntryMode_22;
    private String cardSequenceNumber_23;
    private String posConditionCode_25;
    private String transFee_28;
    private String interchangeFee_30;
    private String acquirerIntId_32;
    private String forwardIntId_33;
    private String track2Data_35;
    private String reference_37;
    private String responseCode_39;
    private String terminalId_41;
    private String cardAcceptorId_42;
    private String cardAcceptorName_43;
    private String additionalData_48;
    private String currencyCode_49;
    private String pinData_52;
    private String newPinData_53;
    private String additionalAmounts_54;
    private String iccData_55;
    private String messageReasonCode_56;
    private String extPaymentCode_67;
    private String originalDataElements_90;
    private String sourceAccountId_102;
    private String destAccountId_103;
    private String channelId_123;
    private String phcnPin_125;

    public ISO8583Data(String mti, String pan_2, String procCode_3, String amount_4, String transDateTime_7, String stan_11, String transTime_12, String transDate_13, String expiryDate_14, String settlementPeriod_15, String merchantType_18, String posEntryMode_22, String cardSequenceNumber_23, String posConditionCode_25, String transFee_28, String interchangeFee_30, String acquirerIntId_32, String forwardIntId_33, String track2Data_35, String reference_37, String terminalId_41, String cardAcceptorId_42, String cardAcceptorName_43, String additionalData_48, String currencyCode_49, String pinData_52, String newPinData_53, String iccData_55, String messageReasonCode_56, String extPaymentCode_67, String originalDataElements_90, String sourceAccountId_102, String destAccountId_103, String channelId_123) {
        this.mti = mti;
        this.pan_2 = pan_2;
        this.procCode_3 = procCode_3;
        this.amount_4 = amount_4;
        this.transDateTime_7 = transDateTime_7;
        this.stan_11 = stan_11;
        this.transTime_12 = transTime_12;
        this.transDate_13 = transDate_13;
        this.expiryDate_14 = expiryDate_14;
        this.settlementPeriod_15 = settlementPeriod_15;
        this.merchantType_18 = merchantType_18;
        this.posEntryMode_22 = posEntryMode_22;
        this.cardSequenceNumber_23 = cardSequenceNumber_23;
        this.posConditionCode_25 = posConditionCode_25;
        this.transFee_28 = transFee_28;
        this.interchangeFee_30 = interchangeFee_30;
        this.acquirerIntId_32 = acquirerIntId_32;
        this.forwardIntId_33 = forwardIntId_33;
        this.track2Data_35 = track2Data_35;
        this.reference_37 = reference_37;        
        this.terminalId_41 = terminalId_41;
        this.cardAcceptorId_42 = cardAcceptorId_42;
        this.cardAcceptorName_43 = cardAcceptorName_43;
        this.additionalData_48 = additionalData_48;
        this.currencyCode_49 = currencyCode_49;
        this.pinData_52 = pinData_52;
        this.newPinData_53 = newPinData_53;
        this.iccData_55 = iccData_55;
        this.messageReasonCode_56 = messageReasonCode_56;
        this.extPaymentCode_67 = extPaymentCode_67;
        this.originalDataElements_90 = originalDataElements_90;
        this.sourceAccountId_102 = sourceAccountId_102;
        this.destAccountId_103 = destAccountId_103;
        this.channelId_123 = channelId_123;
    }
       
    public String getAcquirerIntId_32() {
        return acquirerIntId_32;
    }

    public void setAcquirerIntId_32(String acquirerIntId_32) {
        this.acquirerIntId_32 = acquirerIntId_32;
    }

    public String getAdditionalAmounts_54() {
        return additionalAmounts_54;
    }

    public void setAdditionalAmounts_54(String additionalAmounts_54) {
        this.additionalAmounts_54 = additionalAmounts_54;
    }

    public String getAdditionalData_48() {
        return additionalData_48;
    }

    public void setAdditionalData_48(String additionalData_48) {
        this.additionalData_48 = additionalData_48;
    }

    public String getInterchangeFee_30() {
        return interchangeFee_30;
    }

    public void setInterchangeFee_30(String interchangeFee_30) {
        this.interchangeFee_30 = interchangeFee_30;
    }

    public String getTransFee_28() {
        return transFee_28;
    }

    public void setTransFee_28(String transFee_28) {
        this.transFee_28 = transFee_28;
    }

    public String getAmount_4() {
        return amount_4;
    }

    public void setAmount_4(String amount_4) {
        this.amount_4 = amount_4;
    }

    public String getCardAcceptorId_42() {
        return cardAcceptorId_42;
    }

    public void setCardAcceptorId_42(String cardAcceptorId_42) {
        this.cardAcceptorId_42 = cardAcceptorId_42;
    }

    public String getCardAcceptorName_43() {
        return cardAcceptorName_43;
    }

    public void setCardAcceptorName_43(String cardAcceptorName_43) {
        this.cardAcceptorName_43 = cardAcceptorName_43;
    }

    public String getCardSequenceNumber_23() {
        return cardSequenceNumber_23;
    }

    public void setCardSequenceNumber_23(String cardSequenceNumber_23) {
        this.cardSequenceNumber_23 = cardSequenceNumber_23;
    }

    public String getChannelId_123() {
        return channelId_123;
    }

    public void setChannelId_123(String channelId_123) {
        this.channelId_123 = channelId_123;
    }

    public String getCurrencyCode_49() {
        return currencyCode_49;
    }

    public void setCurrencyCode_49(String currencyCode_49) {
        this.currencyCode_49 = currencyCode_49;
    }

    public String getForwardIntId_33() {
        return forwardIntId_33;
    }

    public void setForwardIntId_33(String forwardIntId_33) {
        this.forwardIntId_33 = forwardIntId_33;
    }

    public String getMerchantType_18() {
        return merchantType_18;
    }

    public void setMerchantType_18(String merchantType_18) {
        this.merchantType_18 = merchantType_18;
    }

    public String getMessageReasonCode_56() {
        return messageReasonCode_56;
    }

    public void setMessageReasonCode_56(String messageReasonCode_56) {
        this.messageReasonCode_56 = messageReasonCode_56;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getOriginalDataElements_90() {
        return originalDataElements_90;
    }

    public void setOriginalDataElements_90(String originalDataElements_90) {
        this.originalDataElements_90 = originalDataElements_90;
    }

    public String getPan_2() {
        return pan_2;
    }

    public void setPan_2(String pan_2) {
        this.pan_2 = pan_2;
    }

    public String getPosConditionCode_25() {
        return posConditionCode_25;
    }

    public void setPosConditionCode_25(String posConditionCode_25) {
        this.posConditionCode_25 = posConditionCode_25;
    }

    public String getPosEntryMode_22() {
        return posEntryMode_22;
    }

    public void setPosEntryMode_22(String posEntryMode_22) {
        this.posEntryMode_22 = posEntryMode_22;
    }

    public String getProcCode_3() {
        return procCode_3;
    }

    public void setProcCode_3(String procCode_3) {
        this.procCode_3 = procCode_3;
    }

    public String getResponseCode_39() {
        return responseCode_39;
    }

    public void setResponseCode_39(String responseCode_39) {
        this.responseCode_39 = responseCode_39;
    }

    public String getSourceAccountId_102() {
        return sourceAccountId_102;
    }

    public void setSourceAccountId_102(String sourceAccountId_102) {
        this.sourceAccountId_102 = sourceAccountId_102;
    }

    public String getStan_11() {
        return stan_11;
    }

    public void setStan_11(String stan_11) {
        this.stan_11 = stan_11;
    }

    public String getTerminalId_41() {
        return terminalId_41;
    }

    public void setTerminalId_41(String terminalId_41) {
        this.terminalId_41 = terminalId_41;
    }

    public String getTransDateTime_7() {
        return transDateTime_7;
    }

    public void setTransDateTime_7(String transDateTime_7) {
        this.transDateTime_7 = transDateTime_7;
    }

    public String getPinData_52() {
        return pinData_52;
    }

    public void setPinData_52(String pinData_52) {
        this.pinData_52 = pinData_52;
    }

    public String getNewPinData_53() {
        return newPinData_53;
    }

    public void setNewPinData_53(String newPinData_53) {
        this.newPinData_53 = newPinData_53;
    }

    public String getDestAccountId_103() {
        return destAccountId_103;
    }

    public void setDestAccountId_103(String destAccountId_103) {
        this.destAccountId_103 = destAccountId_103;
    }

    public String getExpiryDate_14() {
        return expiryDate_14;
    }

    public void setExpiryDate_14(String expiryDate_14) {
        this.expiryDate_14 = expiryDate_14;
    }

    public String getIccData_55() {
        return iccData_55;
    }

    public void setIccData_55(String iccData_55) {
        this.iccData_55 = iccData_55;
    }

    public String getReference_37() {
        return reference_37;
    }

    public void setReference_37(String reference_37) {
        this.reference_37 = reference_37;
    }

    public String getSettlementPeriod_15() {
        return settlementPeriod_15;
    }

    public void setSettlementPeriod_15(String settlementPeriod_15) {
        this.settlementPeriod_15 = settlementPeriod_15;
    }

    public String getTrack2Data_35() {
        return track2Data_35;
    }

    public void setTrack2Data_35(String track2Data_35) {
        this.track2Data_35 = track2Data_35;
    }

    public String getTransDate_13() {
        return transDate_13;
    }

    public void setTransDate_13(String transDate_13) {
        this.transDate_13 = transDate_13;
    }

    public String getTransTime_12() {
        return transTime_12;
    }

    public void setTransTime_12(String transTime_12) {
        this.transTime_12 = transTime_12;
    }

    public String getPhcnPin_125() {
        return phcnPin_125;
    }

    public void setPhcnPin_125(String phcnPin_125) {
        this.phcnPin_125 = phcnPin_125;
    }

    public String getExtPaymentCode_67() {
        return extPaymentCode_67;
    }

    public void setExtPaymentCode_67(String extPaymentCode_67) {
        this.extPaymentCode_67 = extPaymentCode_67;
    }

    @Override
    public String toString() {
        return "ISO8583Data{" + "mti=" + mti + ", pan_2=" + pan_2 + ", procCode_3=" + procCode_3 + ", amount_4=" + amount_4 + ", transDateTime_7=" + transDateTime_7 + ", stan_11=" + stan_11 + ", transTime_12=" + transTime_12 + ", transDate_13=" + transDate_13 + ", expiryDate_14=" + expiryDate_14 + ", settlementPeriod_15=" + settlementPeriod_15 + ", merchantType_18=" + merchantType_18 + ", posEntryMode_22=" + posEntryMode_22 + ", cardSequenceNumber_23=" + cardSequenceNumber_23 + ", posConditionCode_25=" + posConditionCode_25 + ", transFee_28=" + transFee_28 + ", interchangeFee_30=" + interchangeFee_30 + ", acquirerIntId_32=" + acquirerIntId_32 + ", forwardIntId_33=" + forwardIntId_33 + ", track2Data_35=" + track2Data_35 + ", reference_37=" + reference_37 + ", responseCode_39=" + responseCode_39 + ", terminalId_41=" + terminalId_41 + ", cardAcceptorId_42=" + cardAcceptorId_42 + ", cardAcceptorName_43=" + cardAcceptorName_43 + ", additionalData_48=" + additionalData_48 + ", currencyCode_49=" + currencyCode_49 + ", pinData_52=" + pinData_52 + ", newPinData_53=" + newPinData_53 + ", additionalAmounts_54=" + additionalAmounts_54 + ", iccData_55=" + iccData_55 + ", messageReasonCode_56=" + messageReasonCode_56 + ", extPaymentCode_67=" + extPaymentCode_67 + ", originalDataElements_90=" + originalDataElements_90 + ", sourceAccountId_102=" + sourceAccountId_102 + ", destAccountId_103=" + destAccountId_103 + ", channelId_123=" + channelId_123 + ", phcnPin_125=" + phcnPin_125 + '}';
    }    
}
