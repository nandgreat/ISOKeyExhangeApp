/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpos.newthree.dao;

import java.text.DecimalFormat;

/**
 *
 * @author Ebenezer
 */
public class TransObj {
    private String itemName;
    private String itemCode;
    private int quantity;
    private double amount;
    private String formAmount;
    private double totalAmount;
    private String totalAmountString;
    private static final DecimalFormat printer = new DecimalFormat("#,###.00");

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        this.formAmount = printer.format(amount);
    }

    public String getFormAmount() {
        return formAmount;
    }   

    public double getTotalAmount() {
        return this.quantity * this.amount;
    }
    
    public String getTotalAmountString() {
        return printer.format(this.quantity * this.amount);
    }
    
    @Override
    public String toString() {
        return "TransObj{" + "itemName=" + itemName + ", quantity=" + quantity + ", amount=" + amount + ", formAmount=" + formAmount + '}';
    }        
}
