package com.musapp.musicapp.model;

import android.net.Uri;

import com.musapp.musicapp.enums.PostUploadType;

public class Post {
    //instead of userName & ProfileImage fields must be User class field
    //private User mUser;
    //after creating Comment class |
    //private List<Comment> comments;
    private String mUserName;
    private String mPublishedTime;
    private String mPostText;
    private String mProfileImage;
    private String mPostImage;
    private Uri mVideoUri;

    private PostUploadType type;


    public void setType(PostUploadType type) {
        this.type = type;
    }

    public PostUploadType getType() {
        return type;
    }

    public Post() {
        //temporary hardcode
        mPostText = "Post";
    }

    public Post(String mUserName, String mPublishedTime, String mPostText, String mProfileImageUri, String mPostImageUri, Uri mVideoUri, PostUploadType type) {
        this.mUserName = mUserName;
        this.mPublishedTime = mPublishedTime;
        this.mPostText = mPostText;
        this.mProfileImage = mProfileImageUri;
        this.mPostImage = mPostImageUri;
        this.mVideoUri = mVideoUri;
        this.type = type;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getPublishedTime() {
        return mPublishedTime;
    }

    public String getPostText() {
        return mPostText;
    }

    public String getProfileImage() {
        return mProfileImage;
    }

    public String getPostImage() {
        return mPostImage;
    }

    public Uri getVideoUri() {
        return mVideoUri;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public void setPublishedTime(String mPublishedTime) {
        this.mPublishedTime = mPublishedTime;
    }

    public void setPostText(String mPostText) {
        this.mPostText = mPostText;
    }

    public void setProfileImage(String mProfileImageUri) {
        this.mProfileImage = mProfileImageUri;
    }

    public void setPostImage(String mPostImageUri) {
        this.mPostImage = mPostImageUri;
    }

    public void setVideoUri(Uri mVideoUri) {
        this.mVideoUri = mVideoUri;
    }
}
