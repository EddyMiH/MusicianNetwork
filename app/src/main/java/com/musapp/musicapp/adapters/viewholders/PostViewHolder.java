package com.musapp.musicapp.adapters.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.musapp.musicapp.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    private TextView postText;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        postText = itemView.findViewById(R.id.text_item_user_profile_post_text);
    }

    public void setPostText(String text) {
        postText.setText(text);
    }

    public String getPostText(){
        return postText.getText().toString();
    }
}
