package com.mpos.newthree.wizarpos.emvsample.parameter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TerminalConfig
{
    // Default EMV Param
    private final byte DEFAULT_TTQ = 0x36;
    private final String DEFAULT_COUNTRY_CODE = "0566";
    private final String DEFAULT_TERMINAL_TYPE = "22";
    //private final String DEFAULT_TERMINAL_CAPABILITY = "E0F0C8"; //
    private final String DEFAULT_TERMINAL_CAPABILITY = "E060C8";//"E060C8";//60E8C8
    private final String DEFAULT_ADDITIONAL_TERMINAL_CAPABILITY = "F000F0A001";

    // Default other param
    private final int    DEFAULT_KEY_INDEX = 1;
    private final String DEFAULT_HOST_IP = "192.168.1.1";
    private final int    DEFAULT_HOST_PORT = 2025;
    private final int    DEFAULT_TIMEOUT = 10;
    private final int    DEFAULT_UPLOAD_TYPE = 0;
    private final int    DEFAULT_RECEIPT = 1;

    private final String traceTag             = "trace";
    private final String primaryHostIPTag     = "primaryHostIP";
    private final String primaryHostPortTag   = "primaryHostPort";
    private final String timeoutTag           = "timeout";
    private final String niiTag               = "nii";
    private final String tidTag               = "tid";
    private final String midTag               = "mid";
    private final String merchantName1Tag     = "merchantName1";
    private final String merchantName2Tag     = "merchantName2";
    private final String uploadTypeTag        = "uploadType";
    private final String keyIndexTag          = "keyIndex";
    private final String keyAlgorithmTag      = "keyAlgorithm";
    private final String terminalStatusTag    = "terminalStatus";
    private final String maxTransStoreTag     = "maxTransStore";
    private final String signOnTag            = "signOn";
    private final String receiptTag           = "receipt";
    private final String initFlagTag          = "initFlag";
    // EMV
    private final String forceOnlineTag       = "forceOnline";
    private final String countryCodeTag       = "countryCode";  // 9F1A: Terminal Country Code
    private final String IFDTag               = "IFD";          // 9F1E: IFD Serial Number
    private final String currencyCodeTag      = "currencyCode"; // 5F2A
    private final String currencyExponentTag  = "currencyExponent"; // 5F36
    private final String TCTag                = "TC";           // 9F33: Terminal Capabilities
    private final String terminalTypeTag      = "terminalType"; // 9F35
    private final String ATCTag               = "ATC";          // 9F40: additionalTerminalCapabilities
    // QPBOC
    private final String TTQTag               = "TTQ";
    private final String statusCheckSupportTag = "statusCheckSupport";
    // last EMV
    private final String lastTVRTag            = "lastTVR";
    private final String lastTSITag            = "lastTSI";
    // limit
    private final String ecLimitTag            = "ecLimit";
    private final String contactlessLimitTag   = "contactlessLimit";
    private final String contactlessFloorLimitTag            = "contactlessFloorLimit";
    private final String cvmLimitTag           = "cvmLimit";

    private Integer trace = 1;
    private String  primaryHostIP = DEFAULT_HOST_IP;
    private Integer primaryHostPort = DEFAULT_HOST_PORT;
    private Integer timeout = DEFAULT_TIMEOUT;
    private Integer nii = 3;
    private String  tid = "99990002";  // 消费终端
    private String  mid = "205910000010003";
    private String  merchantName1 = "Test Name 1";
    private String  merchantName2 = "Test Name 2";
    private Integer uploadType = DEFAULT_UPLOAD_TYPE;
    private Integer keyIndex = DEFAULT_KEY_INDEX;
    private byte    keyAlgorithm = 0;
    private Integer terminalStatus = 0;
    private Integer maxTransStore = 300;
    private boolean signOn = false;
    private byte    receipt = DEFAULT_RECEIPT;
    private boolean initFlag = false;
    // EMV
    private byte    forceOnlineSwitch = 0;
    private String  countryCode = DEFAULT_COUNTRY_CODE;
    private String  ifd = "123";
    private String  currencyCode = DEFAULT_COUNTRY_CODE;
    private byte    currencyExponent = 2;
    private String  tc = DEFAULT_TERMINAL_CAPABILITY;
    private String  atc = DEFAULT_ADDITIONAL_TERMINAL_CAPABILITY;
    private String  terminalType = DEFAULT_TERMINAL_TYPE;

    private String lastTVR = "";
    private String lastTSI = "";
    // QPBOC
    private byte    TTQ = DEFAULT_TTQ;     // 9F66第一个字节    0011 1010
    private byte    statusCheckSupport = 0;

    private int ecLimit = 0;
    private int contactlessLimit = 0;
    private int contactlessFloorLimit = 0;
    private int cvmLimit = 0;

    private SharedPreferences terminalPref;
    private Editor editor;

    public TerminalConfig( SharedPreferences pref)
    {
        terminalPref = pref;
        editor = terminalPref.edit();
    }

    // terminal config
    public void loadTerminalConfig()
    {
        trace = terminalPref.getInt(traceTag, 1);
        primaryHostIP = terminalPref.getString(primaryHostIPTag, DEFAULT_HOST_IP);
        primaryHostPort = terminalPref.getInt(primaryHostPortTag, DEFAULT_HOST_PORT);
        timeout =  terminalPref.getInt(timeoutTag, DEFAULT_TIMEOUT);
        nii = terminalPref.getInt(niiTag, 3);
        tid = terminalPref.getString(tidTag, "00000025");
        mid = terminalPref.getString(midTag, "205910000010003");
        merchantName1 = terminalPref.getString(merchantName1Tag, "test merchant");
        merchantName2 = terminalPref.getString(merchantName2Tag, "");
        uploadType = terminalPref.getInt(uploadTypeTag, DEFAULT_UPLOAD_TYPE);
        keyIndex = terminalPref.getInt(keyIndexTag, DEFAULT_KEY_INDEX);
        keyAlgorithm = (byte)terminalPref.getInt(keyAlgorithmTag, 0);
        terminalStatus = terminalPref.getInt(terminalStatusTag, 0);
        maxTransStore = terminalPref.getInt(maxTransStoreTag, 300);
        signOn = terminalPref.getBoolean(signOnTag, false);
        receipt = (byte)terminalPref.getInt(receiptTag, DEFAULT_RECEIPT);
        initFlag = terminalPref.getBoolean(initFlagTag, false);
        // EMV
        forceOnlineSwitch = (byte)terminalPref.getInt(forceOnlineTag, 0);
        countryCode = terminalPref.getString(countryCodeTag, DEFAULT_COUNTRY_CODE);
        ifd = terminalPref.getString(IFDTag, "123");
        currencyCode = terminalPref.getString(currencyCodeTag, DEFAULT_COUNTRY_CODE);
        tc = terminalPref.getString(TCTag, DEFAULT_TERMINAL_CAPABILITY);
        atc = terminalPref.getString(ATCTag,DEFAULT_ADDITIONAL_TERMINAL_CAPABILITY);
        terminalType = terminalPref.getString(terminalTypeTag,DEFAULT_TERMINAL_TYPE);
        currencyExponent = (byte)terminalPref.getInt(currencyExponentTag, 2);

        lastTVR = terminalPref.getString(lastTVRTag, "");
        lastTSI = terminalPref.getString(lastTSITag, "");
        //QPBOC
        TTQ = (byte)terminalPref.getInt(TTQTag, DEFAULT_TTQ);
        statusCheckSupport = (byte)terminalPref.getInt(statusCheckSupportTag, 0);
        ecLimit = terminalPref.getInt(ecLimitTag, 1000);
        contactlessLimit = terminalPref.getInt(contactlessLimitTag, 1000);
        contactlessFloorLimit = terminalPref.getInt(contactlessFloorLimitTag, 0);
        cvmLimit = terminalPref.getInt(cvmLimitTag, 0);
    }

    // trace
    public int getTrace()
    {
        return trace;
    }

    public void setTrace(int trace)
    {
        this.trace = trace;
        editor.putInt(traceTag, trace);
        editor.commit();
    }

    public void incTrace()
    {
        trace += 1;
        editor.putInt(traceTag, trace);
        editor.commit();
    }

    // primaryHostIP
    public String getPrimaryHostIP()
    {
        return primaryHostIP;
    }

    public void setPrimaryHostIP(String primaryHostIP)
    {
        this.primaryHostIP = primaryHostIP;
        editor.putString(primaryHostIPTag, primaryHostIP);
        editor.commit();
    }

    // primaryHostPort
    public int getPrimaryHostPort()
    {
        return primaryHostPort;
    }

    public void setPrimaryHostPort(Integer primaryHostPort)
    {
        this.primaryHostPort = primaryHostPort;
        editor.putInt(primaryHostPortTag, primaryHostPort);
        editor.commit();
    }

    // timeout
    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
        editor.putInt(timeoutTag, timeout);
        editor.commit();
    }

    // nii
    public int getNII()
    {
        return nii;
    }

    public void setNII(Integer nii)
    {
        this.nii = nii;
        editor.putInt(niiTag, nii);
        editor.commit();
    }

    // tid
    public String getTID()
    {
        return tid;
    }

    public void setTID(String tid)
    {
        this.tid = tid;
        editor.putString(tidTag, tid);
        editor.commit();
    }

    // mid
    public String getMID()
    {
        return mid;
    }

    public void setMID(String mid)
    {
        this.mid = mid;
        editor.putString(midTag, mid);
        editor.commit();
    }

    // merchantName1
    public String getMerchantName1()
    {
        return merchantName1;
    }

    public void setMerchantName1(String merchantName1)
    {
        this.merchantName1 = merchantName1;
        editor.putString(merchantName1Tag, merchantName1);
        editor.commit();
    }

    // merchantName2
    public String getMerchantName2()
    {
        return merchantName2;
    }

    public void setMerchantName2(String merchantName2)
    {
        this.merchantName2 = merchantName2;
        editor.putString(merchantName2Tag, merchantName2);
        editor.commit();
    }

    // uploadType
    public int getUploadType()
    {
        return uploadType;
    }

    public void setUploadType(Integer uploadType)
    {
        this.uploadType = uploadType;
        editor.putInt(uploadTypeTag, uploadType);
        editor.commit();
    }

    // keyIndex
    public int getKeyIndex()
    {
        return keyIndex;
    }

    public void setKeyIndex(Integer keyIndex)
    {
        this.keyIndex = keyIndex;
        editor.putInt(keyIndexTag, keyIndex);
        editor.commit();
    }

    // keyAlgorithm
    public int getKeyAlgorithm()
    {
        return keyAlgorithm;
    }

    public void setKeyAlgorithm(byte keyAlgorithm)
    {
        this.keyAlgorithm = keyAlgorithm;
        editor.putInt(keyAlgorithmTag, keyAlgorithm);
        editor.commit();
    }

    // terminalStatus
    public int getTerminalStatus()
    {
        return terminalStatus;
    }

    public void setTerminalStatus(Integer terminalStatus)
    {
        this.terminalStatus = terminalStatus;
        editor.putInt(terminalStatusTag, terminalStatus);
        editor.commit();
    }

    // maxTransStore
    public int getMaxTransStore()
    {
        return maxTransStore;
    }

    public void setMaxTransStore(Integer maxTrans)
    {
        this.maxTransStore = maxTrans;
        editor.putInt(maxTransStoreTag, maxTrans);
        editor.commit();
    }

    // signOn
    public boolean getSignOn()
    {
        return signOn;
    }

    public void setSignOn(boolean signOnFlag)
    {
        this.signOn = signOnFlag;
        editor.putBoolean(signOnTag, signOnFlag);
        editor.commit();
    }

    // receipt
    public byte getReceipt()
    {
        return receipt;
    }

    public void setReceipt(byte receipt)
    {
        this.receipt = receipt;
        editor.putInt(receiptTag, receipt);
        editor.commit();
    }

    // initFlag
    public boolean getInitFlag()
    {
        return initFlag;
    }

    public void setInitFlag(boolean initFlag)
    {
        this.initFlag = initFlag;
        editor.putBoolean(initFlagTag, initFlag);
        editor.commit();
    }

    // EMV
    // forceOnlineswitch
    public byte getforceOnline()
    {
        return forceOnlineSwitch;
    }

    public void setForceOnline(byte forceOnlineSwitch)
    {
        this.forceOnlineSwitch = forceOnlineSwitch;
        editor.putInt(forceOnlineTag, forceOnlineSwitch);
        editor.commit();
    }

    // countryCode
    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
        editor.putString(countryCodeTag, countryCode);
        editor.commit();
    }

    // ifd
    public String getIFD()
    {
        return ifd;
    }

    public void setIFD(String ifd)
    {
        this.ifd = ifd;
        editor.putString(IFDTag, ifd);
        editor.commit();
    }

    // currencyCode
    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
        editor.putString(currencyCodeTag, currencyCode);
        editor.commit();
    }

    // tc
    public String getTerminalCapabilities()
    {
        return tc;
    }

    public void setTerminalCapabilities(String tc)
    {
        this.tc = tc;
        editor.putString(TCTag, tc);
        editor.commit();
    }

    // atc
    public String getAdditionalTerminalCapabilities()
    {
        return atc;
    }

    public void setAdditionalTerminalCapabilities(String atc)
    {
        this.atc = atc;
        editor.putString(ATCTag, atc);
        editor.commit();
    }

    // terminalType
    public String getTerminalType()
    {
        return terminalType;
    }

    public void setTerminalType(String terminalType)
    {
        this.terminalType = terminalType;
        editor.putString(terminalTypeTag, terminalType);
        editor.commit();
    }

    // transCurrencyExponent
    public byte getCurrencyExponent()
    {
        return currencyExponent;
    }

    public void setCurrencyExponent(byte currencyExponent)
    {
        this.currencyExponent = currencyExponent;
        editor.putInt(currencyExponentTag, currencyExponent);
        editor.commit();
    }

    // lastTVR
    public String getLastTVR()
    {
        return lastTVR;
    }

    public void setLastTVR(String data)
    {
        this.lastTVR = data;
        editor.putString(lastTVRTag, data);
        editor.commit();
    }

    // lastTSI
    public String getLastTSI()
    {
        return lastTSI;
    }

    public void setLastTSI(String data)
    {
        this.lastTSI = data;
        editor.putString(lastTSITag, data);
        editor.commit();
    }

    // QPBOC
    // TTQ
    public byte getTTQ()
    {
        return TTQ;
    }

    public void setTTQ(byte TTQ)
    {
        this.TTQ = TTQ;
        editor.putInt(TTQTag, TTQ);
        editor.commit();
    }

    // statusCheckSupport
    public byte getStatusCheckSupport()
    {
        return statusCheckSupport;
    }

    public void setStatusCheckSupport(byte flag)
    {
        this.statusCheckSupport = flag;
        editor.putInt(statusCheckSupportTag, flag);
        editor.commit();
    }

    public int getEcLimit() {
        return ecLimit;
    }

    public void setEcLimit(int ecLimit) {
        this.ecLimit = ecLimit;
        editor.putInt(ecLimitTag, ecLimit);
        editor.commit();
    }

    public int getContactlessLimit() {
        return contactlessLimit;
    }

    public void setContactlessLimit(int contactlessLimit) {
        this.contactlessLimit = contactlessLimit;
        editor.putInt(contactlessLimitTag, contactlessLimit);
        editor.commit();
    }

    public int getContactlessFloorLimit() {
        return contactlessFloorLimit;
    }

    public void setContactlessFloorLimit(int contactlessFloorLimit) {
        this.contactlessFloorLimit = contactlessFloorLimit;
        editor.putInt(contactlessFloorLimitTag, contactlessFloorLimit);
        editor.commit();
    }

    public int getCvmLimit() {
        return cvmLimit;
    }

    public void setCvmLimit(int cvmLimit) {
        this.cvmLimit = cvmLimit;
        editor.putInt(cvmLimitTag, cvmLimit);
        editor.commit();
    }
}
