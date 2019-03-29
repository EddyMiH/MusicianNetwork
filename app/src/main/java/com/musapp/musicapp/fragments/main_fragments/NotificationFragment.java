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

import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.NotificationRecyclerViewAdapter;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;
import com.musapp.musicapp.model.Notification;

import java.util.Arrays;

public class NotificationFragment extends Fragment {
    private RecyclerView notificationRecyclerView;
    private NotificationRecyclerViewAdapter notificationRecyclerViewAdapter;
    private SetToolBarAndNavigationBarState mSetToolBarAndNavigationBarState;

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
        Notification[] notifications = new Notification[10];
        Arrays.fill(notifications, new Notification());
       notificationRecyclerViewAdapter.setData(Arrays.asList(notifications));
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationRecyclerView.setAdapter(notificationRecyclerViewAdapter);
        mSetToolBarAndNavigationBarState.setTitle(R.string.title_notifications);
    }

    public void setSetToolBarAndNavigationBarState(SetToolBarAndNavigationBarState toolBarTitle){
        mSetToolBarAndNavigationBarState = toolBarTitle;
    }

}
