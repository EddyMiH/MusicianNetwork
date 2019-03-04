package com.musapp.musicapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.NotificationViewHolder;
import com.musapp.musicapp.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private List<Notification> data;

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_notification_list, viewGroup, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        notificationViewHolder.setDescriptionText(data.get(i).getDescription());
        notificationViewHolder.setUserImage(data.get(i).getImgId());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<Notification> notificationArrayList){
        if(data == null){
            data = new ArrayList<>(notificationArrayList);
        }
        else{
            data.clear();
            data.addAll(notificationArrayList);
        }
        notifyDataSetChanged();
    }
}
