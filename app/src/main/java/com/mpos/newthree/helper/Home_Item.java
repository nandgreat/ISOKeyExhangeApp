package com.mpos.newthree.helper;

/**
 * Created by TECH-PC on 10/5/2018.
 */

public class Home_Item {
    String id;
    String name;

    public Home_Item() {
    }

    public Home_Item(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return getName();
    }
}
