package com.mpos.newthree.obj;

import java.util.List;

/**
 * Created by HP on 8/22/2017.
 */

public class User {
    private String username, password, op ,trans_id,provider,phone,airtime_value,customer_name;
    private String transaction_desc,transaction_amount,trans_type,customer_id,token,customer_phone,plate_number;


    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    private List<String> itemtype_code,merchant_codes;

    public List<String> getItemtype_code() {
        return itemtype_code;
    }

    public void setItemtype_code(List<String> itemtype_code) {
        this.itemtype_code = itemtype_code;
    }

    public List<String> getMerchant_codes() {
        return merchant_codes;
    }

    public void setMerchant_codes(List<String> merchant_codes) {
        this.merchant_codes = merchant_codes;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String op) {
        this.username = username;
        this.password = password;
        this.op = op;
    }

    public User(String username, String password, String op, String trans_id, String transaction_desc,
                String transaction_amount, String trans_type ) {
        this.username = username;
        this.password = password;
        this.op = op;
        this.trans_id = trans_id;
        this.transaction_desc = transaction_desc;
        this.transaction_amount = transaction_amount;
        this.trans_type = trans_type;
        this.itemtype_code = itemtype_code;
        this.merchant_codes = merchant_codes;
    }

    public User(String username, String password, String op, String provider, String phone, String airtime_value) {
        this.username = username;
        this.password = password;
        this.op = op;
        this.provider = provider;
        this.phone = phone;
        this.airtime_value = airtime_value;
    }



    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getId() {
        return token;
    }

    public void setId(String id) {
        this.token = id;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getOp() {
        return op;
    }

}
