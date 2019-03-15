package com.musapp.musicapp.firebase_repository;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.model.Comment;
import com.musapp.musicapp.model.Post;

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
    //TODO do in service maybe
    public static void createComment(Object comment, ValueEventListener valueEventListener){
        DBAccess.createChild( "comments" , comment);
        DBAccess.getDatabaseReference().child("comments").addListenerForSingleValueEvent(valueEventListener);
    }

    public static void setCommentInnerPrimaryKey(ValueEventListener valueEventListener){
        Query lastQuery = DBAccess.getDatabaseReference().child("comments").limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    public static void setCommentInnerPrimaryKeyToFirebase(Comment comment){
        DBAccess.createField(comment.getPrimaryKey(), "comments/" + comment.getPrimaryKey() + "/primaryKey");

    }

    public static void setCommentInnerPrimaryKeyToFirebasePost(Post post){
       DBAccess.getDatabaseReference().child("posts").child(post.getPrimaryKey()).child("commentsId").setValue(post.getCommentsId());

    }


    public static void getUser(ValueEventListener valueEventListener) {
        DBAccess.getUserReference("users").addValueEventListener(valueEventListener);
    }

    public static void getPosts(String userPrimaryKey, int limit, int offset, ValueEventListener valueEventListener) {
       Query postQuery = DBAccess.getDatabaseReference().child("posts").orderByChild("userId").equalTo(userPrimaryKey);
       postQuery.addValueEventListener(valueEventListener);
    }

    public static void getAllPosts(ValueEventListener valueEventListener){
        DBAccess.getDatabaseReference().child("posts").addValueEventListener(valueEventListener);
    }

    public static void getGenres(String userPrimaryKey, ValueEventListener valueEventListener){
        Query genreQuery = DBAccess.getUserReference("users/" + userPrimaryKey).child("genreId");
        genreQuery.addListenerForSingleValueEvent(valueEventListener);
    }

}
