package com.musapp.musicapp.firebase_repository;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.uploads.AttachedFile;
import java.util.Date;


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

    private static void createObject(Object object, String childname, ValueEventListener valueEventListener) {
        DBAccess.createChild(childname, object);
        DBAccess.getDatabaseReference().child(childname).addListenerForSingleValueEvent(valueEventListener);
    }

    private static void setObjectInnerPrimaryKey(String childname, ValueEventListener valueEventListener) {
        Query lastQuery = DBAccess.getDatabaseReference().child(childname).limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    private static void setObjectInnerPrimaryKeyToFirebase(String childname, String primaryKeyToFirebase) {
        DBAccess.createField(primaryKeyToFirebase, childname + '/' + primaryKeyToFirebase + "/primaryKey");
    }


    public static void createComment(Object comment, ValueEventListener valueEventListener) {
        createObject(comment, "comments", valueEventListener);
    }

    public static void setCommentInnerPrimaryKey(ValueEventListener valueEventListener) {
        setObjectInnerPrimaryKey("comments", valueEventListener);
    }

    public static void setCommentInnerPrimaryKeyToFirebase(String primaryKeyToFirebase) {
        setObjectInnerPrimaryKeyToFirebase("comments", primaryKeyToFirebase);
    }

    public static void setCommentInnerPrimaryKeyToFirebasePost(Post post) {
        DBAccess.getDatabaseReference().child("posts").child(post.getPrimaryKey()).child("commentsId").setValue(post.getCommentsId());
    }

    public static void getNewComments(String postPrimaryKey, ChildEventListener childEventListener){
        DBAccess.getDatabaseReference().child("posts").child(postPrimaryKey).child("commentsId").addChildEventListener(childEventListener);
    }

    public static void getCommentById(String id, ValueEventListener valueEventListener){
        DBAccess.getDatabaseReference().child("comments").child(id).addListenerForSingleValueEvent(valueEventListener);
    }


    public static void createPost(Object post, ValueEventListener valueEventListener) {
        createObject(post, "posts", valueEventListener);
    }

    public static void setPostInnerPrimaryKey(ValueEventListener valueEventListener) {
        setObjectInnerPrimaryKey("posts", valueEventListener);
    }

    public static void setPostInnerPrimaryKeyToFirebase(String primaryKeyToFirebase) {
        setObjectInnerPrimaryKeyToFirebase("posts", primaryKeyToFirebase);
        DBAccess.createField(CurrentUser.getCurrentUser().getPrimaryKey(), "posts/" + primaryKeyToFirebase + "/userId");
    }


    public static StorageReference createImageStorageChild(String childname) {
        DBAccess.creatStorageChild("image/", childname);
        return DBAccess.getStorageReference().child("image/" + childname);
    }
    public static StorageReference createVideoStorageChild(String childname){
        DBAccess.creatStorageChild("video/", childname);
        return DBAccess.getStorageReference().child("video/" + childname);
    }
    public static StorageReference createMusicStorageChild(String childname) {
        DBAccess.creatStorageChild("music/", childname);
        return DBAccess.getStorageReference().child("music/" + childname);
    }

    public static StorageReference getMusicStorageReference(String childName){
        return DBAccess.creatStorageChild("music/", childName);
    }


    public static void putFileInStorage(StorageReference reference, Uri file, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener) {
        reference.putFile(file).addOnSuccessListener(onSuccessListener);
    }
    public static void putFileInStorageWithMetadata(StorageReference reference, Uri file, StorageMetadata metadata, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener) {
        reference.putFile(file, metadata).addOnSuccessListener(onSuccessListener);
    }

    public static void getDownloadUrl(StorageReference storageReference, OnSuccessListener<Uri> onSuccessListener) {
        storageReference.getDownloadUrl().addOnSuccessListener(onSuccessListener);
    }

    public static void getUser(ValueEventListener valueEventListener) {
        DBAccess.getUserReference("users").addValueEventListener(valueEventListener);
    }

    public static void getUserByPrimaryKey(String primaryKey, ValueEventListener valueEventListener) {
        DBAccess.getUserReference("users").child(primaryKey).addValueEventListener(valueEventListener);
    }

    public static void getCurrentUser(ValueEventListener valueEventListener) {
        DBAccess.getUserReference("users/" + CurrentUser.getCurrentUser().getPrimaryKey()).addListenerForSingleValueEvent(valueEventListener);
    }


    public static void updateCurrentUserPostsInFirebase(){
        DBAccess.getUserReference("users/" + CurrentUser.getCurrentUser().getPrimaryKey()).child("postId").setValue(CurrentUser.getCurrentUser().getPostId());
    }

    public static void updateCurrentUserFavouritePosts(){
        DBAccess.getUserReference("users/" + CurrentUser.getCurrentUser().getPrimaryKey()).child("favouriteId").setValue(CurrentUser.getCurrentUser().getFavouritePostId());
    }

    public static void getPosts(int limit, @NotNull String lastDateRetrivered, ValueEventListener valueEventListener) {
        Query postQuery = DBAccess.getDatabaseReference().child("posts").orderByChild("publishedTime").endAt(lastDateRetrivered).limitToFirst(limit);
        postQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    public static void getAllPosts(int limit, ValueEventListener valueEventListener) {
        DBAccess.getDatabaseReference().child("posts").orderByChild("publishedTime").limitToLast(limit).addListenerForSingleValueEvent(valueEventListener);
    }

    public static void getPostById(String id, ValueEventListener valueEventListener){
        DBAccess.getUserReference("posts").child(id).addValueEventListener(valueEventListener);
    }
    public static void getNewPost(@NotNull int limit, ValueEventListener valueEventListener) {
    //   DBAccess.getDatabaseReference().child("posts").orderByChild("publishedTime").limitToLast(limit).addChildEventListener(childEventListener);
        DBAccess.getDatabaseReference().child("posts").orderByChild("publishedTime").limitToLast(limit).addValueEventListener(valueEventListener);


    }

    public static void getNewPost(@NotNull int limit, ChildEventListener childEventListener) {
        DBAccess.getDatabaseReference().child("posts").orderByChild("publishedTime").limitToLast(limit).addChildEventListener(childEventListener);
     //   DBAccess.getDatabaseReference().child("posts").orderByChild("publishedTime").limitToLast(limit).addValueEventListener(valueEventListener);
    }

    public static void getGenres(String userPrimaryKey, ValueEventListener valueEventListener){

        Query genreQuery = DBAccess.getUserReference("users/" + userPrimaryKey).child("genreId");
        genreQuery.addListenerForSingleValueEvent(valueEventListener);
    }


    public static void getCurrentUserFavouritePosts(ValueEventListener valueEventListener){
        DBAccess.getUserReference("users/" + CurrentUser.getCurrentUser().getPrimaryKey()).child("favouriteId").addValueEventListener(valueEventListener);
    }

    public static void setCurrentUser(String primaryKey, ValueEventListener valueEventListener) {
        DBAccess.getUserReference("users/" + primaryKey).addListenerForSingleValueEvent(valueEventListener);
    }

//    public static void updateCurrentUser(String primaryKey, User user){
//        DBAccess.getDatabaseReference().child("users").child(primaryKey).setValue(user);
//    }

    public static void updatePassword(String primaryKey, String password){
        DBAccess.getDatabaseReference().child("users").child(primaryKey).child("password").setValue(password);

    }

    public static void updateCurrentUser(String primaryKey, User user){
        //DBAccess.getDatabaseReference().child("users").child(primaryKey).setValue(user);
        DBAccess.getDatabaseReference().child("users").child(primaryKey).child("fullName").setValue(user.getFullName());
        DBAccess.getDatabaseReference().child("users").child(primaryKey).child("birthDay").setValue(user.getBirthDay());
        DBAccess.getDatabaseReference().child("users").child(primaryKey).child("email").setValue(user.getEmail());
        DBAccess.getDatabaseReference().child("users").child(primaryKey).child("gender").setValue(user.getGender());
        DBAccess.getDatabaseReference().child("users").child(primaryKey).child("nickName").setValue(user.getNickName());
        DBAccess.getDatabaseReference().child("users").child(primaryKey).child("profession").setValue(user.getProfession());
        DBAccess.getDatabaseReference().child("users").child(primaryKey).child("userInfo").setValue(user.getUserInfo());


    }

    //query posts which starts with searchText
    public static void getSearchedPostByPostText(String searchText, ValueEventListener valueEventListener){
        Query query = DBAccess.getDatabaseReference().child("posts").orderByChild("postText").startAt(searchText).endAt(searchText + "\uf8ff");
        query.addValueEventListener(valueEventListener);
    }
    public static void getSearchedPostByPostCreator(String searchText, ValueEventListener valueEventListener){
        Query query = DBAccess.getDatabaseReference().child("posts").orderByChild("userName").startAt(searchText).endAt(searchText + "\uf8ff");
        query.addValueEventListener(valueEventListener);
    }

    //querying post which contains searchText
    public static void getSearchedPost( ValueEventListener valueEventListener){
        DBAccess.getDatabaseReference().child("posts").orderByChild("publishedTime").addValueEventListener(valueEventListener);
    }


}
