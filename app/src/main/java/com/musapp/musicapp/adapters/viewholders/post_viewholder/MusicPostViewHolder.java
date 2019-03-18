package com.musapp.musicapp.adapters.viewholders.post_viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.musapp.musicapp.R;

public class MusicPostViewHolder extends BasePostViewHolder {

    private Button mPlayButton;
    private TextView mSongTitle;
    private TextView mArtistName;
    private TextView mSongDuration;


    public MusicPostViewHolder(@NonNull View itemView) {
        super(itemView);
        mPlayButton = itemView.findViewById(R.id.action_music_view_play_pause_button);
        mSongTitle = itemView.findViewById(R.id.text_music_title);
        mArtistName = itemView.findViewById(R.id.text_music_author);
        mSongDuration = itemView.findViewById(R.id.text_music_duration);
    }

    public void setPlayButton(Button playButton) {
        mPlayButton = playButton;
    }

    public void setSongTitle(String songTitle) {
        mSongTitle.setText(songTitle);
    }

    public void setArtistName(String artistName) {
        mArtistName.setText(artistName);
    }

    public void setSongDuration(String songDuration) {
        mSongDuration.setText(songDuration);
    }

    public void setMusic(String url){
        //TODO download music and set all textViews here ...

    }
}
