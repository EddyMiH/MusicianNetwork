package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.StartActivity;
import com.musapp.musicapp.adapters.FeedRecyclerAdapter;
import com.musapp.musicapp.adapters.PostRecyclerViewAdapter;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarTitle;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.utils.GlideUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserProfileFragment extends Fragment {
    @BindView(R.id.circular_view_fragment_other_user_profile_image)
    CircleImageView userImage;
    @BindView(R.id.text_fragment_other_user_profile_fullname)
    TextView fullname;
    @BindView(R.id.text_fragment_other_user_profile_nickname)
    TextView nickname;
    @BindView(R.id.text_fragment_other_user_profile_more_info)
    TextView moreInfo;
    @BindView(R.id.text_fragment_other_user_profile_info_box)
    TextView infoBox;
    @BindView(R.id.recycler_fragment_profile_other_user_posts)
    RecyclerView postsRecyclerView;

    String userPrimaryKey;
    private SetToolBarTitle mSetToolBarTitle;

    public void setSetToolBarTitle(SetToolBarTitle setToolBarTitle) {
        mSetToolBarTitle = setToolBarTitle;
    }

    private FeedRecyclerAdapter postRecyclerViewAdapter = new FeedRecyclerAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        postRecyclerViewAdapter.setData(new ArrayList<Post>());
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postsRecyclerView.setAdapter(postRecyclerViewAdapter);

        showDataFromFireBase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_other_user_profile, container, false);
        ButterKnife.bind(this, view);
        if(getArguments() != null)
            userPrimaryKey = getArguments().getString(String.class.getSimpleName());
        return view;
    }




    private void showDataFromFireBase(){
        FirebaseRepository.getUserByPrimaryKey(userPrimaryKey, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              final User  user = dataSnapshot.getValue(User.class);
                GlideUtil.setImageGlide(user.getUserInfo().getImageUri(), userImage);
                fullname.setText(user.getFullName());
                mSetToolBarTitle.setTitle(user.getFullName());

                nickname.setText(user.getNickName());
                infoBox.setText(user.getUserInfo().getAdditionalInfo());
                final List<Post> posts = new ArrayList<>();
                for(final String postId: user.getPostId()){
                    FirebaseRepository.getPostById(postId, new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            posts.add(dataSnapshot.getValue(Post.class));
                            if(posts.size() == user.getPostId().size()){
                                postRecyclerViewAdapter.setData(posts);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
