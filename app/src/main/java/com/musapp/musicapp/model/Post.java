package com.musapp.musicapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.uploads.AttachedFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Post implements Parcelable{

    private String primaryKey;
    private String mUserId;
    private String mPublishedTime;
    private String mPostText;
    private String mProfileImage;
    private String mUserName;

    private PostUploadType type;
    private List<String> commentsId;

    private AttachedFile attachment;
 /*  private String attachmentId;
    @Exclude
    private static MusicPlayerService.LocalBinder mLocalBinder;
    public static void setLocalBinder(MusicPlayerService.LocalBinder binder){
        mLocalBinder = binder;
    }
*/
    public List<String> getCommentsId() {
        return commentsId;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setCommentsId(List<String> commentsId) {
        this.commentsId = commentsId;
    }

    @Exclude
    public int getCommentsQuantity(){
        return commentsId == null ? 0 : commentsId.size();
    }

    public void addCommentId(String id){
        /*if(commentsId == null){
            commentsId = new ArrayList<>();
        }*/
        commentsId.add(id);

    }


    public void setType(PostUploadType type) {
        this.type = type;
    }

    public PostUploadType getType() {
        return type;
    }

    public Post() {
        //temporary hardcode
        mPostText = "Post";
        commentsId = new ArrayList<>();
        attachment = new AttachedFile();
        type = PostUploadType.NONE;

    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Post(String mUserName, String mPublishedTime, String mPostText, String mProfileImageUri
            ,AttachedFile attachedFile, List<String> commentsId, PostUploadType type) {
        this.mUserId = mUserName;
        this.mPublishedTime = mPublishedTime;
        this.mPostText = mPostText;
        this.mProfileImage = mProfileImageUri;
        this.commentsId = commentsId;
        this.type = type;
        this.attachment = attachedFile;
    }

    public String getUserId() {
        return mUserId;
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

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
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

/*
    @Exclude
    private RecyclerView innerRecyclerView;
    @Exclude
    private BaseUploadsAdapter<BaseUpload, BasePostViewHolder> innerAdapter;
    @Exclude
    private AttachedFile attachedFile;
    @Exclude
    private List<BaseUpload> uploads = new ArrayList<>();
    @Exclude
    private BaseUploadsAdapter.OnItemSelectedListener mOnSongItemSelectedListener =
            new BaseUploadsAdapter.OnItemSelectedListener() {
                @Override
                public void onItemSelected(String uri) {
                    if(mLocalBinder != null){
                        if(mLocalBinder.isPlaying()){
                            mLocalBinder.pause();
                        }else{
                            mLocalBinder.play(uri);
                        }
                    }
                }
            };

    public void setInnerRecyclerView(View view){
        innerRecyclerView = view.findViewById(R.id.inner_recycler_view_post_item_container);
    }

    public void initializeInnerRecyclerAndAdapter(Context context){
        innerAdapter = UploadsAdapterFactory.setAdapterTypeByInputType(type);
        if(type == PostUploadType.MUSIC){
            innerAdapter.setOnItemSelectedListener(mOnSongItemSelectedListener);
        }
        //TODO select attached file from firebase by id and set here
        loadAttachedFiles();
        innerAdapter.setUploads(uploads);
        if(type ==PostUploadType.VIDEO || type == PostUploadType.MUSIC){
            innerRecyclerView.setLayoutManager(new GridLayoutManager(context, 1));

        }else{
            innerRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        }
        innerRecyclerView.setAdapter(innerAdapter);
<<<<<<< HEAD
        //innerAdapter.setUploads(uploads);
=======

>>>>>>> f227bbc78a9ac682e425daf2ff0fa8e144071708
    }

    //old version
    private void loadAttachedFiles(){
        uploads.clear();
        if(type != PostUploadType.NONE){
            if(attachmentId != null) {
                FirebaseRepository.getAttachment(attachmentId, new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        attachedFile = dataSnapshot.getValue(AttachedFile.class);
                        for (String url : attachedFile.getFilesUrls()) {
                            uploads.add(UploadTypeFactory.setUploadByType(attachedFile.getFileType(), url));
                        }
                        innerAdapter.setUploads(uploads);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
*/
    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(primaryKey);
            dest.writeString(mUserId);
            dest.writeString(mPublishedTime);
            dest.writeString(mPostText);
            dest.writeString(mProfileImage);
            dest.writeStringList(commentsId);
            dest.writeString(mUserName);
            dest.writeString(type.name());
            dest.writeSerializable(attachment);

    }

    private static Post createFromParcel(Parcel source){
        Post post = new Post();
        post.primaryKey = source.readString();
        post.mUserId = source.readString();
        post.mPublishedTime = source.readString();
        post.mPostText = source.readString();
        post.mProfileImage = source.readString();
        post.commentsId = new ArrayList<String>();
        source.writeStringList(post.commentsId);
        post.mUserName = source.readString();

        final String name = source.readString();
        switch (name){
            case "NONE" : {
                post.type = PostUploadType.NONE;
            }break;
            case "IMAGE" : {
                post.type = PostUploadType.IMAGE;
            }break;
            case "VIDEO" : {
                post.type = PostUploadType.VIDEO;
            }break;
            case "MUSIC" : {
                post.type = PostUploadType.MUSIC;
            }break;
        }

        post.attachment = (AttachedFile) source.readSerializable();
        return post;
    }


    public static final Parcelable.Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return Post.createFromParcel(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public AttachedFile getAttachment(){return attachment;}
    public void setAttachment(AttachedFile attachment){this.attachment = attachment;}

    @Override
    public boolean equals( Object obj) {
        if(obj instanceof Post){
            if(primaryKey == null){
                return ((Post) obj).getPublishedTime().equals(mPublishedTime)
                        && ((Post)obj).getUserId().equals(mUserId)
                        && ((Post)obj).getPostText().equals(mPostText)
                        && ((Post)obj).getType() == type;
            }
        else
            return primaryKey.equals(((Post) obj).getPrimaryKey());}
        return false;
    }
}
