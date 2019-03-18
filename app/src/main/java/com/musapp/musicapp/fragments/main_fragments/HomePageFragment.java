package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.FeedRecyclerAdapter;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.utils.ContextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomePageFragment extends Fragment {

    private FeedRecyclerAdapter feedRecyclerAdapter;
    private final int limit = 5;

    private ProgressBar mProgressBar;

    private List<Post> posts;
    public static final String ARG_POST = "current_post";

    private OpenUserFragment mOpenUserFragment;

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
                    OpenedPostFragment fragment = new OpenedPostFragment();
                    fragment.setArguments(bundle);
                    beginTransaction(fragment);
                }
            };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_home_page, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_fragment_home_page_posts);
        mProgressBar = rootView.findViewById(R.id.progressbar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.darkPurple), android.graphics.PorterDuff.Mode.MULTIPLY);
     //   loadPostsFromDatabase();
        posts = new ArrayList<>();
        if(savedInstanceState == null){
        initRecyclerView(recyclerView);
        loadFirstPosts(limit);}
        updateRecyclerView();
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
        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ArrayList.class.getSimpleName(), (Serializable) feedRecyclerAdapter.getData());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null)
        feedRecyclerAdapter.setData((List<Post>) savedInstanceState.getSerializable(ArrayList.class.getSimpleName()));
    }

    private void initRecyclerView(RecyclerView view){
        feedRecyclerAdapter = new FeedRecyclerAdapter();
        feedRecyclerAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        feedRecyclerAdapter.setOnUserImageListener(mOnUserImageListener);
        feedRecyclerAdapter.setData(posts);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        view.setAdapter(feedRecyclerAdapter);
    }

    private void updateRecyclerView(){
        FirebaseRepository.getNewPost(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                feedRecyclerAdapter.addPostItem(dataSnapshot.getValue(Post.class), 0);
                //posts.set(0, dataSnapshot.getValue(Post.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                posts.addAll(list);
                feedRecyclerAdapter.setData(posts);
                feedRecyclerAdapter.notifyDataSetChanged();
                posts.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPostsFromDatabase(String lastPostTime,final int limit){
     /*   FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> list = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Post post = postSnapshot.getValue(Post.class);
                    if(!posts.contains(post)){
                        list.add(post);
                    }
                }
                posts.addAll(list);
                feedRecyclerAdapter.setData(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("HOME_PAGE", "onCancelled: data loading is cancelled");
            }
        });*/
        //TODO
      /*  FirebaseRepository.getAllPosts(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> list = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        Post post = postSnapshot.getValue(Post.class);
                        if(!posts.contains(post)){
                            list.add(post);
                        }
                    }
                    posts.addAll(list);
                    feedRecyclerAdapter.setData(posts);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


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
                  feedRecyclerAdapter.setData(posts);
                  feedRecyclerAdapter.notifyItemRangeChanged(feedRecyclerAdapter.getItemCount() - limit + 1, limit);
              } setProgressBarVisibility(View.GONE);
              posts.clear();

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
                beginTransaction(new AddPostFragment());
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public void beginTransaction(Fragment fragment){
       /* getFragmentManager().beginTransaction().replace(R.id.layout_activity_app_container, fragment)
                .addToBackStack(null).commit();*/
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.layout_activity_app_container, fragment)
                .commit();

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
