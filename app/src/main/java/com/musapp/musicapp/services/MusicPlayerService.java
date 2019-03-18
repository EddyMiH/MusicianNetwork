package com.musapp.musicapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerService extends IntentService {

    private MediaPlayer mMediaPlayer;
    private LocalBunder mLocalBunder;
    //maybe don't need
    private List<String> mSongsDataUrls;
    private int mCurrentSongIndex;

    private MediaPlayer.OnPreparedListener mOnPreparedListener =
            new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            };

    private MediaPlayer.OnCompletionListener mOnCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //TODO play next song of post(or the same song)
                    //LocalBinder.playNext()
                }
            };

    @Override
    public void onCreate() {
        super.onCreate();
        mSongsDataUrls = new ArrayList<>();
        initMediaPlayer();
    }


    public MusicPlayerService(String name) {
        super(name);
    }

    public MusicPlayerService() {
        super("MusicPlayerService");
    }

    public void setSongsDataUrls(List<String> songsDataUrls) {
        mSongsDataUrls = songsDataUrls;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    public void initMediaPlayer(){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);

    }
    private void playSong(String url){
        try{
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private boolean isPlayerPlaying(){
        return mMediaPlayer.isPlaying();
    }
    private int getDuration(){
        return mMediaPlayer.isPlaying() ? mMediaPlayer.getCurrentPosition() : 0;
    }
    private void resume(){
        if(mMediaPlayer != null && !mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
        }
    }

    private void pause(){
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }
    }

    private void stop(){
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();

        }
    }
    private void seekTo(int position){
        if(mMediaPlayer != null ){
            mMediaPlayer.seekTo(position);
        }
    }
    private int getMediaPlayerDuration(){
        return  mMediaPlayer.getDuration();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer != null){
            mMediaPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(mLocalBunder == null){
            mLocalBunder = new LocalBunder();
        }
        return mLocalBunder;
    }

    public class LocalBunder extends Binder{
        public void play(String path){
            playSong(path);

        }
        public void pause(){
            MusicPlayerService.this.pause();


        }
        public void resume(){
            MusicPlayerService.this.resume();
        }
        public void stop(){
            MusicPlayerService.this.stop();
        }
        public void seekTo(int position){
            MusicPlayerService.this.seekTo(position);
        }
        public void setSongDataUrl(List<String> url){
            setSongsDataUrls(url);
        }


        public boolean isPlaying(){
            return isPlayerPlaying();
        }
        public int getCurrentDuration(){
            return getDuration();
        }


    }
    public interface UiChangedListener{
        void setUi();
    }
}
