/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpos.newthree.dao;

/**
 *
 * @author Access Solutions
 */
public class ISOResponse {
    /*
         * response[0] = fld 39 response
         * response[1] = ledger balance
         * response[2] = available balance
         * response[3] = pan
         * response[4] = transaction stan
         * response[5] = transaction amount
         * response[6] = additional response fld48
         * response[7] = ********____________ fld 125             
         * response[8] =                      fld 100             PHCN specific informations    
         * response[9] = ********------------ fld 62
            
         */
    private String serverPin;
    private String responseCode;
    private String ledgerBalance;
    private String availableBalance;
    private String pan;
    private String stan;
    private String amount;
    private String response48;
    private String response125Pin;
    private String response100Name;
    private String response62Address;
    private String response126CustomerInfo;
    private boolean reversed;
     
    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    } 
    
    
  public String getPin() {
        return serverPin;
    }
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    
     public void setPin(String enterPin) {
        this.serverPin = enterPin;
    }

    public String getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(String ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getResponse48() {
        return response48;
    }

    public void setResponse48(String response48) {
        this.response48 = response48;
    }

    public String getResponse125Pin() {
        return response125Pin;
    }

    public void setResponse125Pin(String response125Pin) {
        this.response125Pin = response125Pin;
    }

    public String getResponse100Name() {
        return response100Name;
    }

    public void setResponse100Name(String response100Name) {
        this.response100Name = response100Name;
    }

    public String getResponse62Address() {
        return response62Address;
    }

    public void setResponse62Address(String response62Address) {
        this.response62Address = response62Address;
    }

    public String getResponse126CustomerInfo() {
        return response126CustomerInfo;
    }

    public void setResponse126CustomerInfo(String response126CustomerInfo) {
        this.response126CustomerInfo = response126CustomerInfo;
    }    
}
