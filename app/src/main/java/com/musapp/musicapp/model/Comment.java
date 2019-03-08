package com.musapp.musicapp.model;

public class Comment {
    private String primaryKey;
    private String userCreatorNickName;
    private String userProfileImageUrl;
    private String creationTime;

    public Comment() {
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getUserCreatorNickName() {
        return userCreatorNickName;
    }

    public void setUserCreatorNickName(String userCreatorNickName) {
        this.userCreatorNickName = userCreatorNickName;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
}
