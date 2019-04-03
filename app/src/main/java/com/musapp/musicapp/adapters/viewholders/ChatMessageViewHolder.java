package com.musapp.musicapp.adapters.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.musapp.musicapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image_message_chat_image_conversation_profile)
    ImageView userProfile;
    @BindView(R.id.text_message_chat_item_last_message)
    TextView messageText;
    @BindView(R.id.text_message_chat_item_last_date)
    TextView messageDate;
    
    public ChatMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public ImageView getUserProfile(){
        return userProfile;
    }
    public void setMessageText(String message){
        messageText.setText(message);
    }
    public void setMessageDate(String date){
        messageDate.setText(date);
    }
}
