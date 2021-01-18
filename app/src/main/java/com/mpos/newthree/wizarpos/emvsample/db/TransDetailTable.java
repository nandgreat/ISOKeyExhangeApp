package com.mpos.newthree.wizarpos.emvsample.db;

import com.mpos.newthree.wizarpos.util.StringUtil;

public class TransDetailTable {
    private Integer _id;
    private Integer trace;
    private String pan;

    public byte[] getPanByte() {
        return panByte;
    }

    public void setPanByte(byte[] panByte) {
        this.panByte = panByte;
    }

    private byte[] panByte;
    private byte cardEntryMode;
    private byte pinEntryMode;
    private String expiry;
    private byte transType;

    public String getTransTypeStr() {
        return transTypeStr;
    }

    public void setTransTypeStr(String transTypeStr) {
        this.transTypeStr = transTypeStr;
    }

    private String transTypeStr;
    private String transDate; // YYYYMMDD
    private String transTime;
    private String authCode;
    private String rrn;
    private String oper;
    private float transAmount;

    public String getTransAmountStr() {
        return transAmountStr;
    }

    public void setTransAmountStr(String transAmountStr) {
        this.transAmountStr = transAmountStr;
    }

    private String transAmountStr;

    public String getOtherAmountStr() {
        return otherAmountStr;
    }

    public void setOtherAmountStr(String otherAmountStr) {
        this.otherAmountStr = otherAmountStr;
    }

    private String otherAmountStr;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    private String countryCode;

    public String getCryptCode() {
        return cryptCode;
    }

    public void setCryptCode(String cryptCode) {
        this.cryptCode = cryptCode;
    }

    private String cryptCode;

    public String gettCounter() {
        return tCounter;
    }

    public void settCounter(String tCounter) {
        this.tCounter = tCounter;
    }

    private String tCounter;

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    private String terminalType;

    public String getCVM() {
        return CVM;
    }

    public void setCVM(String CVM) {
        this.CVM = CVM;
    }

    private String CVM;

    public String gettCapability() {
        return tCapability;
    }

    public void settCapability(String tCapability) {
        this.tCapability = tCapability;
    }

    private String tCapability;
    private Integer othersAmount;
    private Integer balance;
    // EMV Data
    private byte csn;
    private String unpredictableNumber;
    private String ac;
    private String tvr;
    private String aid;
    private String tsi;
    private String appLabel;
    private String appName;
    private String aip;
    private String iad;
    private Integer ecBalance;
    private String iccData;
    private String scriptResult;
    private String tid;

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    private String currCode;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public TransDetailTable()
    {
        init();
    }

    public void init()
    {
        _id = -1;
        trace = 0;
        pan ="";
        cardEntryMode = 0;
        pinEntryMode = 0;
        expiry ="";
        transType = -1;
        transDate ="";
        transTime ="";
        authCode ="";
        rrn ="";
        oper ="";
        transAmount = -1;
        othersAmount = 0;
        balance = 0;
        // EMV Data
        csn = 0;
        unpredictableNumber = "";
        ac = "";
        tvr = "";
        aid = "";
        tsi = "";
        appLabel = "";
        appName = "";
        aip = "";
        iad = "";
        ecBalance = -1;
        scriptResult = "";
        iccData = "";
    }

    public Integer getId()
    {
        return _id;
    }

    public void setId(Integer id)
    {
        this._id = id;
    }

    public Integer getTrace()
    {
        return trace;
    }

    public void setTrace(Integer trace)
    {
        this.trace = trace;
    }
    // pan
    public String getPAN()
    {
        return pan;
    }

    public void setPAN(String pan)
    {
        this.pan = pan;
    }

    // cardEntryMode
    public byte getCardEntryMode()
    {
        return cardEntryMode;
    }

    public void setCardEntryMode(byte entryMode)
    {
        this.cardEntryMode = entryMode;
    }

    // pinEntryMode
    public byte getPinEntryMode()
    {
        return pinEntryMode;
    }

    public void setPinEntryMode(byte entryMode)
    {
        this.pinEntryMode = entryMode;
    }

    // expiry
    public String getExpiry()
    {
        return expiry;
    }

    public void setExpiry(String expiry)
    {
        this.expiry = expiry;
    }

    // transType
    public byte getTransType()
    {
        return transType;
    }

    public void setTransType(byte transType)
    {
        this.transType = transType;
    }

    // transDate
    public String getTransDate()
    {
        return transDate;
    }

    public void setTransDate(String transDate)
    {
        this.transDate = transDate;
    }

    // transTime
    public String getTransTime()
    {
        return transTime;
    }

    public void setTransTime(String transTime)
    {
        this.transTime = transTime;
    }

    // authCode
    public String getAuthCode()
    {
        return authCode;
    }

    public void setAuthCode(String authCode)
    {
        this.authCode = authCode;
    }

    // rrn
    public String getRRN()
    {
        return rrn;
    }

    public void setRRN(String rrn)
    {
        this.rrn = rrn;
    }
    // oper
    public String getOper()
    {
        return oper;
    }

    public void setOper(String oper)
    {
        this.oper = oper;
    }

    // transAmount
    public float getTransAmount()
    {
        return transAmount;
    }

    public void setTransAmount(float transAmount)
    {
        this.transAmount = transAmount;
    }

    // othersAmount
    public Integer getOthersAmount()
    {
        return othersAmount;
    }

    public void setOthersAmount(Integer amount)
    {
        this.othersAmount = amount;
    }

    // balance
    public Integer getBalance()
    {
        return balance;
    }

    public void setBalance(Integer balance)
    {
        this.balance = balance;
    }

    // EMV Data
    // csn
    public byte getCSN()
    {
        return csn;
    }

    public void setCSN(byte csn)
    {
        this.csn = csn;
    }

    // unpredictableNumber
    public String getUnpredictableNumber()
    {
        return unpredictableNumber;
    }

    public void setUnpredictableNumber(String unpredictableNumber)
    {
        this.unpredictableNumber = unpredictableNumber;
    }

    // ac
    public String getAC()
    {
        return ac;
    }

    public void setAC(String ac)
    {
        this.ac = ac;
    }

    // tvr
    public String getTVR()
    {
        return tvr;
    }

    public void setTVR(String tvr)
    {
        this.tvr = tvr;
    }

    // aid
    public String getAID()
    {
        return aid;
    }

    public void setAID(String aid)
    {
        this.aid = aid;
    }

    // tsi
    public String getTSI()
    {
        return tsi;
    }

    public void setTSI(String tsi)
    {
        this.tsi = tsi;
    }

    // appLabel
    public String getAppLabel()
    {
        return appLabel;
    }

    public void setAppLabel(String appLabel)
    {
        this.appLabel = appLabel;
    }

    // appName
    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    // aip
    public String getAIP()
    {
        return aip;
    }

    public void setAIP(String aip)
    {
        this.aip = aip;
    }

    // iad
    public String getIAD()
    {
        return iad;
    }

    public void setIAD(String iad)
    {
        this.iad = iad;
    }

    // availableOfflineAmount
    public Integer getECBalance()
    {
        return ecBalance;
    }

    public void setECBalance(Integer ecBalance)
    {
        this.ecBalance = ecBalance;
    }

    // scriptResult
    public String getScriptResult()
    {
        return scriptResult;
    }

    public void setScriptResult(String data)
    {
        this.scriptResult = data;
    }

    // iccData
    public String getICCData()
    {
        return iccData;
    }

    public void setICCData(String data)
    {
        this.iccData = data;
    }

    public void setICCData(byte[] data, int offset, int length)
    {
        if(   data != null
                && (offset + length) <= data.length
                )
        {
            iccData = StringUtil.toHexString(data, offset, length, false);
        }
    }

    @Override
    public String toString(){
        return DatabaseOpenHelper.TABLE_TRANS_DETAIL + " [trace=" + trace +
                ", pan="              + pan       +
                ", entryMode="        + cardEntryMode +
                ", expiry="           + expiry    +
                ", transType="        + transType +
                ", transDate="        + transDate +
                ", transTime="        + transTime +
                ", authCode="         + authCode  +
                ", rrn="              + rrn       +
                ", oper="             + oper      +
                ", transAmount="      + transAmount +
                ", balance="          + balance + "]";
    }
}