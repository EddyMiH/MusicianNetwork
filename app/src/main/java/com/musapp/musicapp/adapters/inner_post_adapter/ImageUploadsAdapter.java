package com.musapp.musicapp.adapters.inner_post_adapter;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.ImagePostViewHolder;
import com.musapp.musicapp.uploads.ImageUpload;

public class ImageUploadsAdapter extends BaseUploadsAdapter<ImageUpload, ImagePostViewHolder> {


    @NonNull
    @Override
    public ImagePostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_inner_recycler_view_image_item, viewGroup, false);
        return new ImagePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePostViewHolder imagePostViewHolder, int i) {
        super.onBindViewHolder(imagePostViewHolder, i);
        imagePostViewHolder.setImage(uploads.get(i).getUrl());
    }
}
