package com.mpos.newthree.dao;

/**
 * Created by ACCESS on 4/4/2017.
 */

public class ItemData {
    String text;
    Integer imageId;
    public ItemData(String text, Integer imageId){
        this.text=text;
        this.imageId=imageId;
    }
    public ItemData(String text){
        this.text=text;
        this.imageId=imageId;
    }

    public String getText(){
        return text;
    }

    public Integer getImageId(){
        return imageId;
    }

    @Override
    public String toString() {
        return getText();
    }
}
