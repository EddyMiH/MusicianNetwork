package com.musapp.musicapp.model;

public class Notification {
    private String description;
    private int imgId;
    public Notification(){
        description = "Description";
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
