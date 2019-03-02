package com.musapp.musicapp.adapters.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.musapp.musicapp.R;


public class GenreViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;
    private TextView mName;

    public GenreViewHolder(@NonNull View itemView) {
        super(itemView);

        img = itemView.findViewById(R.id.genre_image_view);
        mName = itemView.findViewById(R.id.genre_label_text_view);
    }

    public void setImage(int source) {
        img.setImageResource(source);
    }

    public void setName(String Name) {
        this.mName.setText(Name);
    }

    public void setNameBackground(int color) {
        mName.setBackgroundColor(color);
    }
}

