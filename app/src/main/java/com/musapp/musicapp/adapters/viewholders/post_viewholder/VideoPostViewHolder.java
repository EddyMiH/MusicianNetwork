package com.musapp.musicapp.adapters.viewholders.post_viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.musapp.musicapp.R;

public class VideoPostViewHolder extends BasePostViewHolder {
    private VideoView videoView;
    private MediaController mMediaController;
    private Context mContext;
    public VideoPostViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        mContext = context;
        videoView = itemView.findViewById(R.id.video_post_item_upload_video);
        mMediaController = new MediaController(context);
        videoView.setMediaController(mMediaController);
        mMediaController.setAnchorView(videoView);
    }

    public VideoView getVideoView() {
        return videoView;
    }

    public void setVideo(String url) {
       //TODO set video by url
        videoView.setVideoURI(Uri.parse(url));
    }
}
