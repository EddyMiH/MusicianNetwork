package com.musapp.musicapp.activities;

import android.app.Application;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.model.Genre;
import com.musapp.musicapp.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainApplication extends Application {

    private List<Genre> listOfGenres = new ArrayList<Genre>(
            Arrays.asList(new Genre("Rock", null)
                    ,new Genre("Blues", null)
                    ,new Genre("Classic", null)
                    ,new Genre("Classic Metal", null)
                    ,new Genre("Power Metal", null)
                    ,new Genre("Symphonic Metal", null)
                    ,new Genre("Electronic dance", null)
                    ,new Genre("Jazz", null)
                    ,new Genre("Pop", null)
                    ,new Genre("Folk", null)
                    ,new Genre("Hip Hop", null)
                    ,new Genre("Country", null)
                    ,new Genre("Rhythm & Blues", null)
                    ,new Genre("Heavy metal", null)
                    ,new Genre("Reggae", null)
                    ,new Genre("Punk Rock", null)
                    ,new Genre("Funk", null)
                    ,new Genre("Soul", null)
                    ,new Genre("Alternative Rock", null)
                    ,new Genre("Dance music", null)
                    ,new Genre("Techno", null)
                    ,new Genre("Rap", null)
                    ,new Genre("House", null)
                    ,new Genre("Singing Vocal", null)
                    ,new Genre("Ambient", null)
                    ,new Genre("Instrumental", null)
                    ,new Genre("World music", null)
                    ,new Genre("Trance music", null)
                    ,new Genre("Progressive Rock", null)
                    ,new Genre("Latin Music", null)
                    ,new Genre("Indie Rock", null)
                    ,new Genre("Pop Rock", null)
                    ,new Genre("Hard Rock", null)
                    ,new Genre("Psychedelic music", null)
                    ,new Genre("Grunge", null)
                    ,new Genre("Electro", null)
                    ,new Genre("New Wave", null)
                    ,new Genre("Death metal", null)
                    ,new Genre("Noise", null)
                    ,new Genre("Industrial Rock", null)
                    ,new Genre("Acoustic", null)
                    ,new Genre("Jazz Fusion", null)
                    ,new Genre("African music", null))
    );
    @Override
    public void onCreate() {
        super.onCreate();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        DBAccess.setDatabaseReference(ref);

        DatabaseReference childRef = ref.child("genres");

        Map<String, Genre> genreHashMap = new HashMap<>();
       for(Genre genre: listOfGenres){
           genreHashMap.put(genre.getName(), genre);
       }

       childRef.setValue(genreHashMap);

       childRef = ref.child("user");

       childRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               ArrayList<User> userList = new ArrayList();
               for (DataSnapshot child :
                       dataSnapshot.getChildren()) {
                   User user = child.getValue(User.class);
                   userList.add(user );
               }
               //Here you have all user you can pass ```userList``` to your method furthur
           }
           @Override
           public void onCancelled(DatabaseError databaseError) {
           }
       });






    }

}
