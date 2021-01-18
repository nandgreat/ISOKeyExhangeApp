package com.mpos.newthree.activity;


/**
 * Created by TECH-PC on 6/12/2018.
 */

public class ConstantFile {
public static int IS_OPERATOR;
public static String FULL_NAME;
public static String token;
public static String userPhone="";
public static String driverpasspost="";
    public static String driver_fname=null;
    public static String driver_lastname=null;
    public static String email=null;
    public static String zone=null;
    public static String category=null;
    public static String amount=null;
    public static String chasis=null;
    public static String plate=null;
    public static String make=null;
    public static String driver_id=null;
    public static String ref_code="";
    public static String wallet="";

    public static int getIsOperator() {
        return IS_OPERATOR;
    }

    public static void setIsOperator(int isOperator) {
        IS_OPERATOR = isOperator;
    }

    public static String getFullName() {
        return FULL_NAME;
    }

    public static void setFullName(String fullName) {
        FULL_NAME = fullName;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        ConstantFile.token = token;
    }

    public static String getUserPhone() {
        return userPhone;
    }

    public static void setUserPhone(String userPhone) {
        ConstantFile.userPhone = userPhone;
    }

    public static String getDriverpasspost() {
        return driverpasspost;
    }

    public static void setDriverpasspost(String driverpasspost) {
        ConstantFile.driverpasspost = driverpasspost;
    }

    public static String getDriver_fname() {
        return driver_fname;
    }

    public static void setDriver_fname(String driver_fname) {
        ConstantFile.driver_fname = driver_fname;
    }

    public static String getDriver_lastname() {
        return driver_lastname;
    }

    public static void setDriver_lastname(String driver_lastname) {
        ConstantFile.driver_lastname = driver_lastname;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        ConstantFile.email = email;
    }

    public static String getZone() {
        return zone;
    }

    public static void setZone(String zone) {
        ConstantFile.zone = zone;
    }

    public static String getCategory() {
        return category;
    }

    public static void setCategory(String category) {
        ConstantFile.category = category;
    }

    public static String getAmount() {
        return amount;
    }

    public static void setAmount(String amount) {
        ConstantFile.amount = amount;
    }

    public static String getChasis() {
        return chasis;
    }

    public static void setChasis(String chasis) {
        ConstantFile.chasis = chasis;
    }

    public static String getPlate() {
        return plate;
    }

    public static void setPlate(String plate) {
        ConstantFile.plate = plate;
    }

    public static String getMake() {
        return make;
    }

    public static void setMake(String make) {
        ConstantFile.make = make;
    }

    public static String getDriver_id() {
        return driver_id;
    }

    public static void setDriver_id(String driver_id) {
        ConstantFile.driver_id = driver_id;
    }

    public static String getRef_code() {
        return ref_code;
    }

    public static void setRef_code(String ref_code) {
        ConstantFile.ref_code = ref_code;
    }

    public static String getWallet() {
        return wallet;
    }

    public static void setWallet(String wallet) {
        ConstantFile.wallet = wallet;
    }

    public String getUrl(String url){
        String address="http://192.168.137.1/auto/"+url;
        //169.254.69.255
        //192.168.43.219
        //192.168.43.219
        // ipconfig 169.254.69.255
        return  address;
    }

    public String getUrlOyo(String url){
        //http://localhost/oysams/demo/api/live/login
        //http://accessng.com/oysams/demo/api/live/login
     // String add="http://192.168.137.239/oysams/demo/api/live/"+url;//mr Ade
        //http://techhost.accessng.com/AJ/vehicle_admin/ODCMVAS/demo/api
        //http://techhost.accessng.com/AJ/vehicle_admin/OGCMVAS/demo/login.php
        String add="http://techhost.accessng.com/AJ/vehicle_admin/OSCMVAS/demo/api/live/"+url;//mr Ade
        System.out.println("add 1 "+add);
        return  add;
    }


}
