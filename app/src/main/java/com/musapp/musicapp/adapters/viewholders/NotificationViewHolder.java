package com.musapp.musicapp.adapters.viewholders;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.musapp.musicapp.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {

    private TextView descriptionText;
    private ImageView userImage;
    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        descriptionText = itemView.findViewById(R.id.text_item_user_notification_description);
        userImage = itemView.findViewById(R.id.image_item_user_notification_others_pic);
    }

    public void setDescriptionText(String text){
        descriptionText.setText(text);
    }

    public String getDescriptionText(){
        return descriptionText.getText().toString();
    }



    public void setUserImage(int  source) {
        userImage.setImageResource(source);
    }
}
