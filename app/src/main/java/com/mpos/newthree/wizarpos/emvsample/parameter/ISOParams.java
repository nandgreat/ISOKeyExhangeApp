package com.mpos.newthree.wizarpos.emvsample.parameter;

/**
 * Created by AccessTech on 10/20/2017.
 */

public class ISOParams {
    public static String getPans() {
        return pans;
    }

    public static void setPans(String pans) {
        ISOParams.pans = pans;
    }

    public static String getExpiryDate() {
        return expiryDate;
    }

    public static void setExpiryDate(String expiryDate) {
        ISOParams.expiryDate = expiryDate;
    }

    public static String getSeqNos() {
        return seqNos;
    }

    public static void setSeqNos(String seqNos) {
        ISOParams.seqNos = seqNos;
    }

    public static String pans;
    public static String expiryDate;
    public static String seqNos;
    public static String pinBlock;

    public static String getPinBlock() {
        return pinBlock;
    }

    public static void setPinBlock(String pinBlock) {

        System.out.println("main pin-------------------------- "+pinBlock);
        ISOParams.pinBlock = pinBlock;
    }

}
