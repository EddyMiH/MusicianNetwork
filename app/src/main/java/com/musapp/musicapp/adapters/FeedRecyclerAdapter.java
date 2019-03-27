package com.musapp.musicapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SeekBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.adapters.viewholders.FeedViewHolder;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.BasePostViewHolder;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.enums.SearchMode;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.uploads.BaseUpload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Handler;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedViewHolder> implements Filterable {

    private List<Post> mData;
    private List<Post> mSearchData;
    private BaseUploadsAdapter<BaseUpload, BasePostViewHolder> innerAdapter;
    private SearchMode mSearchMode = SearchMode.POST_SEARCH;
    private OnItemSelectedListener mOnItemSelectedListener;
    private OnUserImageListener mOnUserImageListener;
    private FeedViewHolder.OnUserProfileImageListener mOnUserProfileImageListener = new FeedViewHolder.OnUserProfileImageListener() {
        @Override
        public void onUserImageClickListener(int position) {
            mOnUserImageListener.onProfileImageClickListener(mData.get(position));
        }
    };
    private FeedViewHolder.OnPostSettingsClickListener mOnPostSettingsClickListener = new FeedViewHolder.OnPostSettingsClickListener() {
        @Override
        public void onFavouriteClickListener(int position) {
            CurrentUser.getCurrentUser().addFavouritePostId(mData.get(position).getPrimaryKey());
            updateUsersFavouritePosts();
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
        mSearchData = new ArrayList<>();
    }

    public void setData(List<Post> mData) {
        if (this.mData == null) {
            this.mData = new ArrayList<>();
        }
        this.mData.removeAll(mData);
        this.mData.addAll(mData);
        this.mSearchData.addAll(mData);
        notifyDataSetChanged();
    }

    public void setData(List<Post> mData, int index) {
        if (this.mSearchData == null){
            this.mSearchData = new ArrayList<>();
        }
        this.mData.removeAll(mData);
        this.mData.addAll(index, mData);
        this.mSearchData.addAll(index, mData);
        notifyDataSetChanged();
    }


    public void addPostItem(Post post, int index) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (this.mSearchData == null){
            this.mSearchData = new ArrayList<>();
        }
        mData.add(index, post);
        mSearchData.add(index, post);
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
        feedViewHolder.setPostSettingsClickListener(mOnPostSettingsClickListener);
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

    @Override
    public Filter getFilter() {
        return postFilter;
    }

    private Filter postFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final List<Post> filteredPosts = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredPosts.addAll(mSearchData);
            }else{
                final String queryText = constraint.toString().toLowerCase().trim();

                //query all posts ant check contains queryText
//                FirebaseRepository.getSearchedPost( new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//                            Post post = snapshot.getValue(Post.class);
//                            String postText = post.getPostText().toLowerCase().trim();
//                            String name = post.getUserName().toLowerCase().trim();
//                            if(postText.contains(queryText) || name.contains(queryText)){
//                                filteredPosts.add(post);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
                if (mSearchMode == SearchMode.POST_SEARCH) {
                    FirebaseRepository.getSearchedPostByPostText(queryText, new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<HashMap<String, Post>> T = new GenericTypeIndicator<HashMap<String, Post>>() {
                            };
                            HashMap<String, Post> posts = dataSnapshot.getValue(T);
                            if (posts != null) {
                                filteredPosts.addAll(posts.values());
                            }
                            upDateAdapterDataAfterSearch(filteredPosts);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    FirebaseRepository.getSearchedPostByPostCreator(queryText, new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<HashMap<String,Post>> T = new GenericTypeIndicator<HashMap<String,Post>>() {};
                            HashMap<String,Post> posts = dataSnapshot.getValue(T);
                            if (posts != null){
                                filteredPosts.addAll(posts.values());
                            }
                            upDateAdapterDataAfterSearch(filteredPosts);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            FilterResults result = new FilterResults();
            result.values = filteredPosts;
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            upDateAdapterDataAfterSearch((List)results.values);
        }
    };

    private void upDateAdapterDataAfterSearch(List<Post> list){
        mData.clear();
        //Collections.reverse(list);
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public void setSearchMode(SearchMode searchMode) {
        mSearchMode = searchMode;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(Post post) ;
    }

    public interface OnUserImageListener {
        void onProfileImageClickListener(Post post);
    }


    private void updateUsersFavouritePosts(){
        FirebaseRepository.updateCurrentUserFavouritePosts();
    }

}
