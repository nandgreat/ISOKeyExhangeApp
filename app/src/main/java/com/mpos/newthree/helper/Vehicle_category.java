package com.mpos.newthree.helper;

public class Vehicle_category {
    String catid;
    String name;
    String fee;

    public Vehicle_category() {
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
    @Override
    public String toString() {
        return getName();
    }
}
