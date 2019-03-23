package com.musapp.musicapp.adapters.viewholders.post_viewholder;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.musapp.musicapp.R;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.main_fragments.AddPostFragment;

public class MusicPostViewHolder extends BasePostViewHolder {

    private Button mPlayButton;
    private TextView mSongTitle;
    private TextView mArtistName;
    private TextView mSongDuration;
    private SeekBar mDurationSeekBar;
    private boolean flag = false;

    public MusicPostViewHolder(@NonNull View itemView) {
        super(itemView);
        mPlayButton = itemView.findViewById(R.id.action_music_view_play_pause_button);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    setButtonBackground(R.drawable.ic_play_arrow_white_24dp);
                    flag = false;
                }else{
                    setButtonBackground(R.drawable.ic_pause_black_24dp);
                    flag = true;
                }
            }
        });

        mSongTitle = itemView.findViewById(R.id.text_music_title);
        mArtistName = itemView.findViewById(R.id.text_music_author);
        mSongDuration = itemView.findViewById(R.id.text_music_duration);
        mDurationSeekBar = itemView.findViewById(R.id.seek_bar_music_view_post_inner_recycler_view);

        mDurationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mViewHolderClickListner.onItemClicked(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    public void setSongDuration(int songDuration) {
        mSongDuration.setText(songDuration);
    }

    public SeekBar getSeekBar(){
        return mDurationSeekBar;
    }

    public void setMusic(String url){
        //TODO download music and set all textViews here ...
        String songName = url.substring(82,99);
        Log.d("music viewholder", "setMusic: " + songName);
        FirebaseRepository.getMusicStorageReference(songName).getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata metadata) {
                mSongTitle.setText(metadata.getCustomMetadata(AddPostFragment.SONG_TITLE));
                Log.d("music viewholder", "title: " +metadata.getCustomMetadata(AddPostFragment.SONG_TITLE));

                mArtistName.setText(metadata.getCustomMetadata(AddPostFragment.SONG_ARTIST));
                Log.d("music viewholder", "artist: " + metadata.getCustomMetadata(AddPostFragment.SONG_ARTIST));

                mSongDuration.setText(castMillisecondToMinuteAndSecond(metadata.getCustomMetadata(AddPostFragment.SONG_DURATION)));
                if(metadata.getCustomMetadata(AddPostFragment.SONG_DURATION) != null)
                    mDurationSeekBar.setMax(Integer.parseInt(metadata.getCustomMetadata(AddPostFragment.SONG_DURATION)));
            }
        });
    }
    public String castMillisecondToMinuteAndSecond(String millisecondsString){
        if(millisecondsString != null){
            int milliseconds = Integer.parseInt(millisecondsString);
            long minutes = (milliseconds) / 1000 / 60;
            long sec = ( milliseconds / 1000 ) % 60;
            return (String.valueOf(minutes) + ":" + String.valueOf(sec) );
        }
        return "0:00";
    }

    public void setButtonBackground(int resource){
        mPlayButton.setBackgroundResource(resource);
    }

}
