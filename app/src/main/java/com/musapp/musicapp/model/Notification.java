package com.musapp.musicapp.model;

import com.musapp.musicapp.utils.StringUtils;

public class Notification {
    private String description;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private int imgId;
    public Notification(){
        description = "Description";
        date = StringUtils.CurrentDateAndTimeToString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
