package com.musapp.musicapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.FeedViewHolder;
import com.musapp.musicapp.model.Post;

import java.util.ArrayList;
import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private List<Post> mData;
    private OnItemSelectedListener mOnItemSelectedListener;
    private Context context;

    public void setData(List<Post> mData) {
        if(this.mData == null){
            this.mData = new ArrayList<>();
        }else{
            this.mData.clear();
        }
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    public void addPostItem(Post post){
        if (mData != null) {
            mData.add(post);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_recycler_view_item, viewGroup, false);
        context = viewGroup.getContext();
        final FeedViewHolder viewHolder = new FeedViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(mData.get(viewHolder.getAdapterPosition()));
                    //TODO implement this interface in homePageFragment
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedViewHolder feedViewHolder, int i) {
        Post post = mData.get(i);

        //feedViewHolder.setOnSettingClickListener(mOnClickListener);
        feedViewHolder.setUserName(post.getUserName());
        //feedViewHolder.setFeedProfileImage(post.getProfileImage());
        feedViewHolder.setPostText(post.getPostText());
        feedViewHolder.setPostTime(post.getPublishedTime());

        switch (post.getType()){
            case NONE:

                break;
            case IMAGE:
                feedViewHolder.setPostImage(post.getPostImage());
                feedViewHolder.setPostImageVisible();
                break;
            case VIDEO:
                feedViewHolder.setPostVideo(post.getVideoUri());
                feedViewHolder.setPostVideoVisible();
                break;
            case MUSIC:
                //TODO
                break;
            case IMAGE_AND_MUSIC:
                feedViewHolder.setPostImage(post.getPostImage());
                feedViewHolder.setPostImageVisible();
                //TODO
        }
        //after creating Comment class
        //feedViewHolder.setCommentCount();
        //feedViewHolder.setCommentIcon();



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemSelectedListener{
        void onItemSelected(Post post);
    }
}