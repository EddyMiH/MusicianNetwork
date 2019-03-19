package com.musapp.musicapp.model;

import android.net.Uri;

public class Info {

    private String profession;
    private String additionalInfo;
    //maybe field for image
    private String imageUri;

    public Info() {
        //temporary hardcode, must generate gradient
        //imageUri = "https://firebasestorage.googleapis.com/v0/b/musiciannetwork.appspot.com/o/image%2FAvfrlT7f698.jpg?alt=media&token=f7f616ad-26f0-461f-b6ae-1eac8668c50d";
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getProfession() {
        return profession;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}

