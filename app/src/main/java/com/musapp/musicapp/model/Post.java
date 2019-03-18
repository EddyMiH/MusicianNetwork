package com.musapp.musicapp.model;

import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.BasePostViewHolder;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.pattern.UploadsAdapterFactory;
import com.musapp.musicapp.pattern.UploadTypeFactory;
import com.musapp.musicapp.uploads.AttachedFile;
import com.musapp.musicapp.uploads.BaseUpload;

import java.util.ArrayList;
import java.util.List;

public class Post implements Parcelable{
    //instead of userName & ProfileImage fields must be User class field
    //private User mUser;
    @Ignore
    private String primaryKey;
    private String mUserId;
    private String mPublishedTime;
    private String mPostText;
    private String mProfileImage;
    private String mUserName;
    private int mCommentCount;

    private PostUploadType type;
    private List<String> commentsId;

    private String attachmentId;

    public List<String> getCommentsId() {
        return commentsId;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setCommentsId(List<String> commentsId) {
        this.commentsId = commentsId;
    }

    public int getCommentsQuantity(){
        return commentsId == null ? 0 : commentsId.size();
    }

    public void addCommentId(String id){
        if(commentsId == null){
            commentsId = new ArrayList<>();
        }
        commentsId.add(id);
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
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
        mCommentCount = 0;
        commentsId = new ArrayList<>();
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Post(String mUserName, String mPublishedTime, String mPostText, String mProfileImageUri
            , String attachmentId, List<String> commentsId, PostUploadType type, int mCommentCount) {
        this.mUserId = mUserName;
        this.mPublishedTime = mPublishedTime;
        this.mPostText = mPostText;
        this.mProfileImage = mProfileImageUri;
        this.attachmentId = attachmentId;
        this.commentsId = commentsId;
        this.type = type;
        this.mCommentCount = mCommentCount;
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



    private RecyclerView innerRecyclerView;
    private BaseUploadsAdapter<BaseUpload, BasePostViewHolder> innerAdapter;

    public void setInnerRecyclerView(View view){
        innerRecyclerView = view.findViewById(R.id.inner_recycler_view_post_item_container);
    }

    public void initializeInnerRecyclerAndAapater(){
        innerAdapter = UploadsAdapterFactory.setAdapterTypeByInputType(type);
        List<BaseUpload> uploads = new ArrayList<>();
        //TODO select attached file from firebase by id and set here
        AttachedFile attachedFile = new AttachedFile();
        for(String url:  attachedFile.getFilesUrls()){
           uploads.add(UploadTypeFactory.setUploadByType(attachedFile.getFileType(), url));
        }
        innerAdapter.setUploads(uploads);
        innerRecyclerView.setAdapter(innerAdapter);
    }

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
            dest.writeString(attachmentId);
            dest.writeString(mUserName);
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
        post.attachmentId = source.readString();
        post.mUserName = source.readString();
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

    @Override
    public boolean equals( Object obj) {
        if(obj instanceof Post)
            return primaryKey.equals(((Post) obj).getPrimaryKey());
        return false;
    }
}
