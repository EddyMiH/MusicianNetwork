package com.musapp.musicapp.activities;

import android.app.Application;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.firebase.DBAsyncTask;
import com.musapp.musicapp.model.Genre;
import com.musapp.musicapp.model.Profession;
import com.musapp.musicapp.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference ref = database.getReference();
        StorageReference storageReference = storage.getReference();

        DBAccess.setDatabaseReference(ref);
        DBAccess.setStorageReference(storageReference);

        DBAsyncTask.setDatabaseReference(ref);

    }
}
