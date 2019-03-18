package com.musapp.musicapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;

public class MusicPlayerService extends IntentService {

    private MediaPlayer mMediaPlayer;



    public MusicPlayerService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
