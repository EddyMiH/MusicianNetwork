package com.musapp.musicapp.model;

import android.net.Uri;

public class ProfessionAndInfo {

    private String profession;
    private String additionalInfo;
    //maybe field for image
    private Uri imageUri;

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
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

