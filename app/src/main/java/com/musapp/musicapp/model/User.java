package com.musapp.musicapp.model;

import android.arch.persistence.room.Ignore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class User {
    private String fullName;
    private String nickName;
    private String password;
    private String email;
    private Calendar birthDay;
    private Gender gender;
    private List<String> postId;
    private String professionAndInfoId;
    private List<String> genresId;

    @Ignore
    private String primaryKey;

    public User(){}

    public List<String> getPostId() {
        return postId;
    }

    public List<String> getGenresId() {
        return genresId;
    }

    public void setGenresId(List<String> genresId) {
        this.genresId = genresId;
    }

    public void addGenreId(String genre){
        if(genresId == null){
            genresId = new ArrayList<>();
        }
        genresId.add(genre);
    }

    public void setPostId(List<String> postId) {
        this.postId = postId;
    }

    public void addPostId(String id){
        if (postId == null) {
            postId = new ArrayList<>();
        }
        postId.add(id);
    }

    public String getProfessionAndInfoId() {
        return professionAndInfoId;
    }

    public void setProfessionAndInfoId(String professionAndInfoId) {
        this.professionAndInfoId = professionAndInfoId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Calendar getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Calendar birthDay) {
        this.birthDay = birthDay;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
