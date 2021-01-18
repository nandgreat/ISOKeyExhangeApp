package com.mpos.newthree.obj;

import android.content.Context;
import android.content.SharedPreferences;

import com.mpos.newthree.activity.MainActivity;


/**
 * Created by TECH-PC on 4/12/2018.
 */
public class MyPreferences {

    private static final String BLUETOOTH_PREF = "BluetoothPref" ;
    private static final String BLUTOOTH_NAME = "BluetoothName";
    private static final String BLUTOOTH_ADDRESS = "BluetoothAddress";
    private static final String BLUTOOTH_PASS = "BluetoothPass";
    private static final String DEVICE_NAME = "DevicePass";
    private static final String DEVICE_ADDRESS = "DeviceAddress";


    SharedPreferences sharedpreferences;

    public MyPreferences(){
        sharedpreferences = MainActivity.context.getSharedPreferences(BLUETOOTH_PREF, Context.MODE_PRIVATE);
    }

    public boolean setBlutoothName(String blutoothName){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(BLUTOOTH_NAME, blutoothName);
        return editor.commit();
    }

    public boolean setBlutoothPass(String blutoothPass){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(BLUTOOTH_PASS, blutoothPass);
        return editor.commit();
    }

    public boolean setDeviceName(String deviceName){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DEVICE_NAME, deviceName);
        return editor.commit();
    }

    public boolean setBluetoothAddress(String bluetoothAddress){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(BLUTOOTH_ADDRESS, bluetoothAddress);
        return editor.commit();
    }

    public boolean setDeviceAddress(String deviceAddress){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DEVICE_ADDRESS, deviceAddress);
        return editor.commit();
    }

    public String getBlutoothName(){
        return sharedpreferences.getString(BLUTOOTH_NAME, null);
    }

    public String getBlutoothPass(){
        return sharedpreferences.getString(BLUTOOTH_PASS, null);
    }

    public String getDeviceName(){
        return sharedpreferences.getString(DEVICE_NAME, null);
    }

    public String getBlutoothAddress(){
        return sharedpreferences.getString(BLUTOOTH_ADDRESS, null);
    }

    public String getDeviceAddress(){
        return sharedpreferences.getString(DEVICE_ADDRESS, null);
    }

}
