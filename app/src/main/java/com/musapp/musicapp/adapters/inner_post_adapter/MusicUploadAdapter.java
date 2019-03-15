package com.musapp.musicapp.adapters.inner_post_adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.MusicPostViewHolder;
import com.musapp.musicapp.uploads.MusicUpload;

public class MusicUploadAdapter extends BaseUploadsAdapter<MusicUpload, MusicPostViewHolder> {
    @NonNull
    @Override
    public MusicPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_inner_recycler_view_music_item, viewGroup, false);
        return new MusicPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicPostViewHolder musicPostViewHolder, int i) {
        //super.onBindViewHolder(musicPostViewHolder, i);
        musicPostViewHolder.setMusic(uploads.get(i).getUrl());
    }
}
