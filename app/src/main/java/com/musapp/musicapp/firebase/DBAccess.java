package com.musapp.musicapp.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.model.User;

import java.util.HashMap;
import java.util.Map;

public final class DBAccess {
    private DBAccess(){}

    private static DatabaseReference databaseReference;
    private static String DEFAULT_PATH = "";
    //CREATE
   public static  void createChild(String childName,  Object obj){
        String key =  databaseReference.child(DEFAULT_PATH  +  childName).push().getKey();
       databaseReference.child(DEFAULT_PATH + childName).child(key).setValue(obj);

    }

    public static  void createChild(String path, String childName,  Object obj){
        String key =  databaseReference.child(path +  childName).push().getKey();
        databaseReference.child(path + childName).child(key).setValue(obj);

    }

    public static void createField( Object obj, String path){
       DatabaseReference  childRef =   databaseReference.child(path);
       childRef.setValue(obj);
    }

    public static void createFields(HashMap<String, Object> fields, String path){
        for(Map.Entry<String, Object> entry: fields.entrySet()){
            //TODO find better solution
           databaseReference.child(path + entry.getKey()).setValue(entry.getValue());
        }
    }


    public static void setDatabaseReference(DatabaseReference databaseReference) {
        DBAccess.databaseReference = databaseReference;
    }

    public static boolean selectEmail(final String childName, final String email){
        final boolean[] res = new boolean[1];
       databaseReference.child(childName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                      out:  for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for(DataSnapshot childSnapshoot: snapshot.getChildren()){
                            Log.i("SNPSHOT", childSnapshoot.toString());
                            if(!(childSnapshoot.getValue() instanceof String))
                                continue ;
                            String field = (String) childSnapshoot.getValue();
                            if(childSnapshoot.getKey() == "email" && field.equals(email)){
                                res[0] = true;
                                break out;}
                        }}
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

       return res[0];
    }
}
