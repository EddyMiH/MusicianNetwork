package com.musapp.musicapp.adapters.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.utils.GlideUtil;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    private ImageView mProfileImage;
    private TextView mUserName;
    private TextView mPublishedTime;
    private TextView mCommentText;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);

        mProfileImage = itemView.findViewById(R.id.image_profile_image_comments);
        mUserName  = itemView.findViewById(R.id.text_comment_item_user_name);
        mPublishedTime = itemView.findViewById(R.id.text_comment_published_time);
        mCommentText = itemView.findViewById(R.id.text_comment_item_comment_text);
    }

    public void setProfileImage(String url) {
        GlideUtil.setImageGlide(url, mProfileImage);
    }

    public void setUserName(String userName) {
        mUserName.setText(userName);
    }

    public void setPublishedTime(String publishedTime) {
        mPublishedTime.setText(publishedTime);
    }

    public void setCommentText(String commentText) {
        mCommentText.setText(commentText);
    }
}
