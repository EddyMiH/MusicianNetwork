package com.musapp.musicapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.FeedViewHolder;
import com.musapp.musicapp.model.Post;

import java.util.ArrayList;
import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private List<Post> mData;
    private OnItemSelectedListener mOnItemSelectedListener;
    private OnUserImageListener mOnUserImageListener;
    private FeedViewHolder.OnUserProfileImageListener mOnUserProfileImageListener = new FeedViewHolder.OnUserProfileImageListener() {
        @Override
        public void onUserImageClickListener() {
            mOnUserImageListener.onProfileImageClickListener();
        }
    };
    private Context context;

    public FeedRecyclerAdapter(){
        mData = new ArrayList<>();
    }

    public void setData(List<Post> mData) {
        if(this.mData == null){
            this.mData = new ArrayList<>();
        }else{
        //    this.mData.clear();
        }
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    public void addPostItem(Post post, int index){
        if(mData == null)
            mData = new ArrayList<>();
            mData.add(index, post);
           notifyItemInserted(index);

    }


    public List<Post> getData(){
        if(mData == null)
            mData = new ArrayList<>();
        return mData;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

    @NonNull    @Override

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

        viewHolder.setUserProfileImageListener(mOnUserProfileImageListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedViewHolder feedViewHolder, int i) {
        Post post = mData.get(i);

        //feedViewHolder.setOnSettingClickListener(mOnClickListener);
        feedViewHolder.setUserName(post.getUserName());
        feedViewHolder.setFeedProfileImage(post.getProfileImage());
        feedViewHolder.setPostText(post.getPostText());
        feedViewHolder.setPostTime(post.getPublishedTime());
        feedViewHolder.setCommentCount(String.valueOf(post.getCommentsQuantity()));

        /*switch (post.getType()){
            case NONE:

                break;
            case IMAGE:
<<<<<<< HEAD
          //      feedViewHolder.setPostImage(post.getPostImage());
            //    feedViewHolder.setPostImageVisible();
                break;
            case VIDEO:
              //  feedViewHolder.setPostVideo(post.getVideoUri());
                //feedViewHolder.setPostVideoVisible();
=======
                //feedViewHolder.setPostImage(post.getPostImage());
                feedViewHolder.setPostImageVisible();
                break;
            case VIDEO:
                //feedViewHolder.setPostVideo(post.getVideoUri());
                feedViewHolder.setPostVideoVisible();
>>>>>>> add posts firebase database
                break;
            case MUSIC:
                //TODO
                break;
            case IMAGE_AND_MUSIC:
                //feedViewHolder.setPostImage(post.getPostImage());
<<<<<<< HEAD
                //feedViewHolder.setPostImageVisible();
=======
                feedViewHolder.setPostImageVisible();
>>>>>>> add posts firebase database
                //TODO
        }*/


    }

    @Override
    public int getItemCount() {
        return mData ==  null ? 0 : mData.size();
    }

    public void setOnUserImageListener(OnUserImageListener onUserImageListener) {
        mOnUserImageListener = onUserImageListener;
    }

    public interface OnItemSelectedListener{
        void onItemSelected(Post post);
    }

    public interface OnUserImageListener{
        void onProfileImageClickListener();
    }
}
