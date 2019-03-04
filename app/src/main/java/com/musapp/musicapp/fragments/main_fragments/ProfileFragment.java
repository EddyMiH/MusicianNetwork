package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.PostRecyclerViewAdapter;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarTitle;
import com.musapp.musicapp.model.Post;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    @BindView(R.id.circular_view_fragment_user_profile_image)
    CircleImageView userImage;
    @BindView(R.id.text_fragment_user_profile_fullname)
    TextView fullname;
    @BindView(R.id.text_fragment_user_profile_nickname)
    TextView nickname;
    @BindView(R.id.text_fragment_user_profile_more_info)
    TextView moreInfo;
    @BindView(R.id.text_fragment_user_profile_info_box)
    TextView infoBox;
    @BindView(R.id.recycler_fragment_profile_user_posts)
    RecyclerView postsRecyclerView;
    private SetToolBarTitle setToolBarTitle;

    private PostRecyclerViewAdapter postRecyclerViewAdapter = new PostRecyclerViewAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showDataFromFireBase();
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoBox.setVisibility(View.VISIBLE);
            }
        });
        infoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoBox.setVisibility(View.GONE);
            }
        });


        Post[] posts = new Post[10];
        Arrays.fill(posts, new Post());
        postRecyclerViewAdapter.setData(Arrays.asList(posts));
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postsRecyclerView.setAdapter(postRecyclerViewAdapter);
        setToolBarTitle.setTitle(R.string.toolbar_title_profile);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    private void showDataFromFireBase(){
        //TODO set fields info
    }

    public void setSetToolBarTitle(SetToolBarTitle toolBarTitle){
        setToolBarTitle = toolBarTitle;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_user_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
