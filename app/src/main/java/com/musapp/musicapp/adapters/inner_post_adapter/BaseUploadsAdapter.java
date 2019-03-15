package com.musapp.musicapp.adapters.inner_post_adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.musapp.musicapp.adapters.viewholders.post_viewholder.BasePostViewHolder;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.ImagePostViewHolder;
import com.musapp.musicapp.uploads.BaseUpload;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseUploadsAdapter<T, Y extends RecyclerView.ViewHolder > extends RecyclerView.Adapter<Y> {

    protected List<T> uploads;

    @Override
    public int getItemCount() {
        return uploads == null ? 0 : uploads.size();
    }

    public void setUploads(@NonNull List<T> data) {
        if(uploads != null)
            uploads.clear();
        uploads = new ArrayList<>(data);
      notifyDataSetChanged();
    }

    public void addItem(T newUpload){
        if(uploads == null)
            uploads = new ArrayList<>();
        uploads.add(newUpload);
        notifyItemChanged(uploads.size() - 1);
    }

}
