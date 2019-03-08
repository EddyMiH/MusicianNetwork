package com.musapp.musicapp.model;

import android.arch.persistence.room.Ignore;
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

public class Post {
    //instead of userName & ProfileImage fields must be User class field
    //private User mUser;
    @Ignore
    private String primaryKey;
    private String mUserName;
    private String mPublishedTime;
    private String mPostText;
    private String mProfileImage;

    private PostUploadType type;
    private List<String> commentsId;
    private String attachmentId;

    public List<String> getCommentsId() {
        return commentsId;
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
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Post(String mUserName, String mPublishedTime, String mPostText, String mProfileImageUri
            , String attachmentId, List<String> commentsId, PostUploadType type) {
        this.mUserName = mUserName;
        this.mPublishedTime = mPublishedTime;
        this.mPostText = mPostText;
        this.mProfileImage = mProfileImageUri;
        this.attachmentId = attachmentId;
        this.commentsId = commentsId;
        this.type = type;
    }

    public String getUserName() {
        return mUserName;
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

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
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
        innerRecyclerView = view.findViewById(R.id.innner_recycler_view_post_item_container);
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


}
