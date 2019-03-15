package com.musapp.musicapp.firebase_repository;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase.DBAccess;

public class FirebaseRepository {


    public static void setUserInnerPrimaryKey(ValueEventListener valueEventListener) {
        Query lastQuery = DBAccess.getDatabaseReference().child("users").orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    public static void addCurrentUserToFirebase(ValueEventListener valueEventListener) {
        DBAccess.createChild("users", CurrentUser.getCurrentUser());
        DBAccess.getDatabaseReference().addListenerForSingleValueEvent(valueEventListener);
    }

    public static void setUserInnerPrimaryKeyToFirebase() {
        DBAccess.createField(CurrentUser.getCurrentUser().getPrimaryKey(), "users/" + CurrentUser.getCurrentUser().getPrimaryKey() + "/primaryKey");
    }

    public static void setUserHashedPassword(String password) {
        DBAccess.createField(password, "users/" + CurrentUser.getCurrentUser().getPrimaryKey() + "/password");

    }


    public static void getUser(ValueEventListener valueEventListener) {
        DBAccess.getUserReference("users").addValueEventListener(valueEventListener);
    }

    public static void getPosts(String userPrimaryKey, int limit, int offset, ValueEventListener valueEventListener) {
       Query postQuery = DBAccess.getDatabaseReference().child("posts").orderByChild("userId").equalTo(userPrimaryKey);
       postQuery.addValueEventListener(valueEventListener);
    }

    public static void getGenres(String userPrimaryKey, ValueEventListener valueEventListener){
        Query genreQuery = DBAccess.getUserReference("users/" + userPrimaryKey).child("genreId");
        genreQuery.addListenerForSingleValueEvent(valueEventListener);
    }

}
