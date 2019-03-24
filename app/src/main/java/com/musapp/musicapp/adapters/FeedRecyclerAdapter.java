package com.musapp.musicapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.adapters.viewholders.FeedViewHolder;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.BasePostViewHolder;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.uploads.BaseUpload;

import java.util.ArrayList;
import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private List<Post> mData;
    private BaseUploadsAdapter<BaseUpload, BasePostViewHolder> innerAdapter;
    private OnItemSelectedListener mOnItemSelectedListener;
    private OnUserImageListener mOnUserImageListener;
    private FeedViewHolder.OnUserProfileImageListener mOnUserProfileImageListener = new FeedViewHolder.OnUserProfileImageListener() {
        @Override
        public void onUserImageClickListener() {
            mOnUserImageListener.onProfileImageClickListener();
        }
    };
    private AppMainActivity.MusicPlayerServiceConnection mPlayerServiceConnection;

    private BaseUploadsAdapter.OnItemSelectedListener mInnerMusicItemOnClickListener = new BaseUploadsAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(String uri) {
            mPlayerServiceConnection.play(uri);
        }
    };

    private BaseUploadsAdapter.OnMusicSeekBarListener mMusicSeekBarListener =
            new BaseUploadsAdapter.OnMusicSeekBarListener() {
                @Override
                public void onSeekBarChanged(int position) {
                    mPlayerServiceConnection.seekTo(position);
                }

                @Override
                public void onStartHandle(View view, View view2) {
                    mPlayerServiceConnection.handleSeekBar( (SeekBar) view, (Button) view2);
                }
            };

    private Context context;

    public void setPlayerServiceConnection(AppMainActivity.MusicPlayerServiceConnection connection){
        mPlayerServiceConnection = connection;
    }

    public FeedRecyclerAdapter() {
        mData = new ArrayList<>();
    }

    public void setData(List<Post> mData) {
        if (this.mData == null) {
            this.mData = new ArrayList<>();
        }
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    public void setData(List<Post> mData, int index) {
        if (this.mData == null) {
            this.mData = new ArrayList<>();
        }
        this.mData.addAll(index, mData);
        notifyDataSetChanged();
    }


    public void addPostItem(Post post, int index) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(index, post);
        notifyItemInserted(index);

    }

    public List<Post> getData() {
        if (mData == null)
            mData = new ArrayList<>();
        return mData;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

//    public void setInnerItemClickListener(BaseUploadsAdapter.OnItemSelectedListener listener){
//        mInnerItemClickListener = listener;
//    }


    private View view;

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_post_recycler_view_item, viewGroup, false);
        context = viewGroup.getContext();
        final FeedViewHolder viewHolder = new FeedViewHolder(view);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(mData.get(viewHolder.getAdapterPosition()));
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
        if(post.getType() == PostUploadType.MUSIC){
            feedViewHolder.setInnerItemClickListener(mInnerMusicItemOnClickListener);
            feedViewHolder.setOnSeekBarListener(mMusicSeekBarListener);
        }
        //in else if statements set inner listener for image(open tabbed fragment for fullscreen)
        Log.i("BINDING", i + "");
        feedViewHolder.initializeRecyclerView(post, context);

    }
    
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setOnUserImageListener(OnUserImageListener onUserImageListener) {
        mOnUserImageListener = onUserImageListener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(Post post) ;
    }

    public interface OnUserImageListener {
        void onProfileImageClickListener();
    }



}
