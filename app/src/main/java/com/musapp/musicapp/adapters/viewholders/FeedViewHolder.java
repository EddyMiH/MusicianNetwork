package com.musapp.musicapp.adapters.viewholders;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.utils.GlideUtil;


public class FeedViewHolder extends RecyclerView.ViewHolder {

    private ImageView mFeedProfileImage;
    private TextView mUserName;
    private TextView mPostTime;
    private ImageView mPostSetting;
    private TextView mPostText;
    //image and video are placed in innner  recyclerview
 //   private ImageView mPostImage;
 //   private VideoView mPostVideo;
    private ImageView mCommentIcon;
    private TextView mCommentCount;

    private OnUserProfileImageListener userProfileImageListener;
    //add field for inflate music view

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(mPostSetting.getContext(), mPostSetting);
            popupMenu.inflate(R.menu.menu_pop_up_post_item);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.favorite_pop_up_menu_item:
                            //TODO add to favorites
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    };

    private View.OnClickListener onImageViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userProfileImageListener.onUserImageClickListener();
        }
    };

    public FeedViewHolder(@NonNull View itemView) {
        super(itemView);

        mFeedProfileImage = itemView.findViewById(R.id.image_profile_image_post);
        mFeedProfileImage.setOnClickListener(onImageViewClickListener);
        mUserName = itemView.findViewById(R.id.text_post_item_user_name);
        mPostTime = itemView.findViewById(R.id.text_post_item_published_time);
        mPostSetting = itemView.findViewById(R.id.image_post_item_setting);
        mPostText = itemView.findViewById(R.id.text_post_item_post_text);
        mCommentIcon = itemView.findViewById(R.id.image_comment_icon);
        mCommentCount = itemView.findViewById(R.id.text_post_item_comment_count);

        mPostSetting.setOnClickListener(onClickListener);
    }

    public void setOnSettingClickListener(View.OnClickListener listener){
        onClickListener = listener;
    }

    public void setPostImageVisible(){
   //     mPostImage.setVisibility(View.VISIBLE);
    }
    public void setPostVideoVisible(){
        //mPostVideo.setVisibility(View.VISIBLE);
    }

    public void setFeedProfileImage(String src) {
        GlideUtil.setImageGlide(src, mFeedProfileImage);
    }

    public void setUserName(String mUserName) {
        this.mUserName.setText(mUserName);
    }

    public void setPostTime(String mPostTime) {
        this.mPostTime.setText(mPostTime);
    }

    public void setPostSetting(ImageView mPostSetting) {
        this.mPostSetting = mPostSetting;
    }

    public void setPostText(String mPostText) {
        this.mPostText.setText(mPostText);
    }

    public void setPostImage(String src) {
        //GlideUtil.setImageGlide(src, this.mPostImage);
    }

    public void setPostVideo(Uri videoFilePath) {
      //  mPostVideo.setVideoURI(videoFilePath);
        //TODO set video into YouTubeVideoView
        //VideoViewInitUtil.setVideoView(mPostVideo);
    }

    public void setCommentIcon(ImageView mCommentIcon) {
        this.mCommentIcon = mCommentIcon;
    }

    public void setCommentCount(String mCommentCount) {
        this.mCommentCount.setText(mCommentCount);
    }

    public ImageView getPostSettingView(){
        return mPostSetting;
    }

    public void setUserProfileImageListener(OnUserProfileImageListener userProfileImageListener) {
        this.userProfileImageListener = userProfileImageListener;
    }

    public interface OnUserProfileImageListener {
        void onUserImageClickListener();
    }
}
