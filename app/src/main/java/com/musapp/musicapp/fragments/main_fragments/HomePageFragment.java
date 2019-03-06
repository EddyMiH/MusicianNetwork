package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.FeedRecyclerAdapter;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.model.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePageFragment extends Fragment {

    private FeedRecyclerAdapter feedRecyclerAdapter;

    private List<Post> posts = new ArrayList<Post>(
            Arrays.asList(new Post("John Johnson", "21:11", "troi reijdsg lisgf oijgsf gijrsg "
                    , null, null, null, PostUploadType.NONE)
            ,new Post("John Johnson", "21:11", "troi reijdsg lisgf oijgsf gijrsg "
                            , null, null, null, PostUploadType.NONE)
            ,new Post("John Johnson", "21:11", "troi reijdsg lisgf oijgsf gijrsg "
                            , null, null, null, PostUploadType.NONE))
    );

    private FeedRecyclerAdapter.OnItemSelectedListener mOnItemSelectedListener =
            new FeedRecyclerAdapter.OnItemSelectedListener() {
                @Override
                public void onItemSelected(Post post) {
                    //TODO open extended post fragment
                }
            };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_home_page, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_fragment_home_page_posts);
        initRecyclerView(recyclerView);
        return rootView;
    }

    private void initRecyclerView(RecyclerView view){
        feedRecyclerAdapter = new FeedRecyclerAdapter();
        feedRecyclerAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        feedRecyclerAdapter.setData(posts);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        view.setAdapter(feedRecyclerAdapter);
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
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public HomePageFragment() {
        //Required
    }
}
