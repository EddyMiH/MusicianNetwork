package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.NotificationRecyclerViewAdapter;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarTitle;
import com.musapp.musicapp.model.Notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NotificationFragment extends Fragment {
    private RecyclerView notificationRecyclerView;
    private NotificationRecyclerViewAdapter notificationRecyclerViewAdapter;
    private SetToolBarTitle setToolBarTitle;
    private NotificationRecyclerViewAdapter.OnProfileImageClickListener mOnProfileImageClickListener = new NotificationRecyclerViewAdapter.OnProfileImageClickListener() {
        @Override
        public void onProfileImageClick(Notification notification) {
                OtherUserProfileFragment otherUserProfileFragment = new OtherUserProfileFragment();
            Bundle args = new Bundle();
            args.putString(String.class.getSimpleName(), notification.getCommentatorId());
            otherUserProfileFragment.setSetToolBarTitle(setToolBarTitle);
            otherUserProfileFragment.setArguments(args);
            beginTransaction(otherUserProfileFragment);
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_user_notifications, container, false);
        notificationRecyclerView = view.findViewById(R.id.recycler_fragment_notifications);
        notificationRecyclerViewAdapter = new NotificationRecyclerViewAdapter();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        notificationRecyclerViewAdapter.setOnProfileImageClickListener(mOnProfileImageClickListener);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationRecyclerView.setAdapter(notificationRecyclerViewAdapter);
        loadNotifications();
        setToolBarTitle.setTitle(R.string.title_notifications);
    }

    private void loadNotifications(){
        FirebaseRepository.loadNotifications(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                notificationRecyclerViewAdapter.addItem(dataSnapshot.getValue(Notification.class), 0);
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

       /* FirebaseRepository.getCurrentUser(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("notifications").getValue() == null)
                    return;
                List<Notification> notifications = new ArrayList<>();
                for(DataSnapshot notifSnapshot: dataSnapshot.child("notifications").getChildren())
                    notifications.add(notifSnapshot.getValue(Notification.class));
                Collections.reverse(notifications);
                notificationRecyclerViewAdapter.setData(notifications);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void beginTransaction(Fragment fragment){
        if(fragment.isAdded())
            return;
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().add(R.id.layout_activity_app_container, fragment).commit();
    }

    public void setSetToolBarTitle(SetToolBarTitle toolBarTitle){
        setToolBarTitle = toolBarTitle;
    }

}
