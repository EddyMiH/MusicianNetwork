package com.musapp.musicapp.adapters.viewholders.post_viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.VideoView;

import com.musapp.musicapp.R;

public class VideoPostViewHolder extends BasePostViewHolder {
    private VideoView videoView;
    public VideoPostViewHolder(@NonNull View itemView) {
        super(itemView);
        videoView = itemView.findViewById(R.id.video_post_item_upload_video);

    }

    public VideoView getVideoView() {
        return videoView;
    }

    public void setVideo(String url) {
       //TODO set video by url
    }
}
