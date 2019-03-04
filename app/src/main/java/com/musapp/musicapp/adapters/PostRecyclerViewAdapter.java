package com.musapp.musicapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.PostViewHolder;
import com.musapp.musicapp.model.Post;
import java.util.ArrayList;
import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private List<Post> data = new ArrayList<>();

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_profile_post, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i) {
        postViewHolder.setPostText(data.get(i).getText());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<Post> list) {
        if (data == null) {
            data = new ArrayList<>(list);
        } else {
            data.clear();
            data.addAll(list);
        }
        notifyDataSetChanged();
    }

}
