package com.musapp.musicapp.firebase_repository;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase.DBAccess;

public class FirebaseRepository {
    public static void getGenres(ValueEventListener valueEventListener) {
    }

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

}
