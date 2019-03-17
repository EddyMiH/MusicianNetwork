package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.StartActivity;
import com.musapp.musicapp.adapters.FeedRecyclerAdapter;
import com.musapp.musicapp.adapters.PostRecyclerViewAdapter;
import com.musapp.musicapp.adapters.UserPostViewPagerAdapter;
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
    @BindView(R.id.tab_layout_user_profile_fragment)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager_user_profile_fragment)
    ViewPager mViewPager;

    private SetToolBarTitle setToolBarTitle;
    private ChangeActivity changeActivity;

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

        mTabLayout.addTab(mTabLayout.newTab().setText("My Posts"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Favorites"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        UserPostViewPagerAdapter pagerAdapter = new UserPostViewPagerAdapter(getActivity(), getChildFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        Post[] posts = new Post[10];
//        Arrays.fill(posts, new Post());
//        postRecyclerAdapter.setData(Arrays.asList(posts));
//        postRecyclerViewAdapter.setData(Arrays.asList(posts));
//        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        postsRecyclerView.setAdapter(postRecyclerAdapter);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(R.id.action_log_out):{
              changeActivity.changeActivity(StartActivity.class);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setChangeActivity(ChangeActivity changeActivity){
        this.changeActivity = changeActivity;
    }

    public interface ChangeActivity{
        void changeActivity( Class nextActivity);
    }
}
