package com.mpos.newthree.helper;

/**
 * Created by TECH-PC on 10/5/2018.
 */

public class History {
    String ticketid;
    String payment_mode;
    String status;
    String payment_feedback;
    String member_id;
    String category;
    String amountpaid;
    String chasis;

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getPlateno() {
        return plateno;
    }

    public void setPlateno(String plateno) {
        this.plateno = plateno;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    String make;
    String plateno;
    String driver;

    public History() {
    }

    public String getTicketid() {
        return ticketid;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_feedback() {
        return payment_feedback;
    }

    public void setPayment_feedback(String payment_feedback) {
        this.payment_feedback = payment_feedback;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmountpaid() {
        return amountpaid;
    }

    public void setAmountpaid(String amountpaid) {
        this.amountpaid = amountpaid;
    }

    public String getPaid_on() {
        return paid_on;
    }

    public void setPaid_on(String paid_on) {
        this.paid_on = paid_on;
    }

    String paid_on;

}
