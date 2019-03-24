package com.musapp.musicapp.fragments.main_fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.adapters.FeedRecyclerAdapter;
import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarTitle;
import com.musapp.musicapp.model.Post;

import com.musapp.musicapp.service.MusicPlayerService;

import com.musapp.musicapp.utils.FragmentShowUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePageFragment extends Fragment {

    private FeedRecyclerAdapter feedRecyclerAdapter;
    private final int limit = 5;

    private ProgressBar mProgressBar;

    private List<Post> posts;
    public static final String ARG_POST = "current_post";
    private OpenUserFragment mOpenUserFragment;
    private SetToolBarTitle mSetToolBarTitle;
    private RecyclerView recyclerView;
    private AppMainActivity.MusicPlayerServiceConnection mPlayerServiceConnection;

    private  SwipeRefreshLayout swipeRefreshLayout;
    public void setSetToolBarTitle(SetToolBarTitle setToolBarTitle) {
        mSetToolBarTitle = setToolBarTitle;
    }

    private FeedRecyclerAdapter.OnUserImageListener mOnUserImageListener = new FeedRecyclerAdapter.OnUserImageListener() {
        @Override
        public void onProfileImageClickListener() {
            mOpenUserFragment.openUserFragment();
        }
    };

    private FeedRecyclerAdapter.OnItemSelectedListener mOnItemSelectedListener =
            new FeedRecyclerAdapter.OnItemSelectedListener() {
                @Override
                public void onItemSelected(Post post) {
                    //TODO open extended post fragment
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(HomePageFragment.ARG_POST, post);
                    PostDetailsFragment fragment = new PostDetailsFragment();
                    fragment.setPlayerServiceConnection(mPlayerServiceConnection);
                    fragment.setArguments(bundle);
                    fragment.setSetToolBarTitle(mSetToolBarTitle);
                    beginTransaction(fragment);
                }
            };

    public void setPlayerServiceConnection(AppMainActivity.MusicPlayerServiceConnection connection){
        mPlayerServiceConnection = connection;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_home_page, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_fragment_home_page_posts);
        mProgressBar = rootView.findViewById(R.id.progressbar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.darkPurple), android.graphics.PorterDuff.Mode.MULTIPLY);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_feed);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        posts = new ArrayList<>();
        initRecyclerView(recyclerView);
        loadFirstPosts(limit);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getChildCount();
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int totalItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    if(feedRecyclerAdapter.getData().size() >= limit){
                        setProgressBarVisibility(View.VISIBLE);
                        loadPostsFromDatabase(feedRecyclerAdapter.getData().get(feedRecyclerAdapter.getData().size() - limit + 1).getPublishedTime(), limit);}
                }

            }});
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initRecyclerView(RecyclerView view){
        feedRecyclerAdapter = new FeedRecyclerAdapter();
        feedRecyclerAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        feedRecyclerAdapter.setOnUserImageListener(mOnUserImageListener);
        //feedRecyclerAdapter.setInnerItemClickListener(mInnerItemOnClickListener);
        feedRecyclerAdapter.setPlayerServiceConnection(mPlayerServiceConnection);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        view.setAdapter(feedRecyclerAdapter);
    }



    private void loadFirstPosts(int limit){
        FirebaseRepository.getAllPosts(limit, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> list = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Post post = postSnapshot.getValue(Post.class);
                    if(!feedRecyclerAdapter.getData().contains(post)){
                        list.add(post);
                    }
                }
               Collections.reverse(list);
              //  posts.addAll(list);
                feedRecyclerAdapter.setData(list);
                //feedRecyclerAdapter.setData(list, 0);
           //    feedRecyclerAdapter.notifyDataSetChanged();
           //     posts.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPostsFromDatabase(String lastPostTime,final int limit){

      FirebaseRepository.getPosts(limit, lastPostTime, new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              List<Post> list = new ArrayList<>();
              boolean showProgress = false;
              for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                  Post post = postSnapshot.getValue(Post.class);
                  if(!feedRecyclerAdapter.getData().contains(post)){
                      list.add(post);
                      showProgress = true;
                  }
                  if(!showProgress)
                      setProgressBarVisibility(View.GONE);
              }
              Collections.reverse(list);
              posts.addAll(list);
              if(list.size() > 0) {
                  feedRecyclerAdapter.setData(list);
                  feedRecyclerAdapter.notifyItemRangeChanged(feedRecyclerAdapter.getItemCount() - limit + 1, limit);
              } setProgressBarVisibility(View.GONE);
             // posts.clear();

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_fragment_search_add, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search_home_fragment_menu_item);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //TODO search
                //feedRecyclerAdapter.getFilter().filter(s);

                return false;
            }
        });
        menuItem = menu.findItem(R.id.action_add_home_fragment_menu_item);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_home_fragment_menu_item:
                //TODO open new fragment for add new post
                AddPostFragment fragment = new AddPostFragment();
                //fragment.setSetToolBarTitle(mSetToolBarTitle);
                beginTransaction(fragment);
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

//    private MusicPlayerService.LocalBinder mLocalBinder;
//
//    private ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mLocalBinder = (MusicPlayerService.LocalBinder) service;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mLocalBinder = null;
//        }
//    };

    public void beginTransaction(Fragment fragment){
        if(fragment.isAdded())
            return;
        FragmentShowUtils.setPreviousFragment(this);
        FragmentShowUtils.setCurrentFragment(fragment);
        getFragmentManager().beginTransaction()
                .addToBackStack(fragment.getClass().getCanonicalName())
                .replace(R.id.layout_activity_app_container, fragment)
                .commit();
        //.add(R.id.layout_activity_app_container, fragment)
    }

    public HomePageFragment() {
        //Required
    }

    public void setOpenUserFragment(OpenUserFragment openUserFragment) {
        mOpenUserFragment = openUserFragment;
    }

    public interface OpenUserFragment{
        void openUserFragment();
    }

    private void setProgressBarVisibility(int visibility){
        mProgressBar.setVisibility(visibility);
    }

}
