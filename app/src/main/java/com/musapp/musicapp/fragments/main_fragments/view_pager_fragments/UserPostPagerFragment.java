package com.musapp.musicapp.fragments.main_fragments.view_pager_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.FeedRecyclerAdapter;
import com.musapp.musicapp.model.Post;

import java.util.Arrays;

import butterknife.BindView;

public class UserPostPagerFragment extends Fragment {

    RecyclerView recyclerView;

    private FeedRecyclerAdapter postRecyclerAdapter = new FeedRecyclerAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_user_posts_pager, container, false);
        recyclerView = view.findViewById(R.id.recycler_fragment_profile_user_posts);

        Post[] posts = new Post[10];
        Arrays.fill(posts, new Post());
        loadUserPosts();
        postRecyclerAdapter.setData(Arrays.asList(posts));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(postRecyclerAdapter);
        return view;
    }

    private void loadUserPosts(){
        //TODO load usr post from firebase

    }

    public UserPostPagerFragment() {
    }
}
