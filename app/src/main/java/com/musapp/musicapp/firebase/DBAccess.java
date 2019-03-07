package com.musapp.musicapp.firebase;

import com.google.firebase.database.DatabaseReference;

public final class DBAccess {
    private DBAccess(){}

    //CREATE
   public static  String createChild(DatabaseReference reference, String path,  Object obj, String childName){
        String key =  reference.child(path  +  childName).push().getKey();
        reference.child(path + childName).child(key).setValue(obj);
        return  key;
    }
}
