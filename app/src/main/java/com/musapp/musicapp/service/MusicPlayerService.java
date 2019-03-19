package com.musapp.musicapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;



public class MusicPlayerService extends IntentService {

    private MediaPlayer mMediaPlayer;
    private LocalBinder mLocalBinder;
    private String currentSongUrl;
    Handler mHandler;

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
                    mLocalBinder.repeatSong();
                }
            };
    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();

    }


    public MusicPlayerService(String name) {
        super(name);
    }

    public MusicPlayerService() {
        super("MusicPlayerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    public void initMediaPlayer(){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mMediaPlayer.setOnErrorListener(mOnErrorListener);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);

    }
    private void release(){
        if(mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void playSong(String url){
        try{
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
            currentSongUrl = url;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 1000);
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
        release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(mLocalBinder == null){
            mLocalBinder = new LocalBinder();
        }
        return mLocalBinder;
    }

    public class LocalBinder extends Binder{
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
        public void repeatSong(){
            playSong(currentSongUrl);
        }

        public boolean isPlaying(){
            return isPlayerPlaying();
        }
        public int getCurrentDuration(){
            return getDuration();
        }
        public int getPlayerDuration(){
            return getMediaPlayerDuration();
        }


    }
    public interface UiChangedListener{
        void setUi();
    }
}
