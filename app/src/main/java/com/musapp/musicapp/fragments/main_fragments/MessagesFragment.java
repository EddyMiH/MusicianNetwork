package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;

public class MessagesFragment extends Fragment {

    private SetToolBarAndNavigationBarState mSetToolBarAndNavigationBarState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //mSetToolBarAndNavigationBarState.setTitle(R.string.title_dashboard);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setSetToolBarAndNavigationBarState(SetToolBarAndNavigationBarState setToolBarAndNavigationBarState) {
        mSetToolBarAndNavigationBarState = setToolBarAndNavigationBarState;
    }

    public MessagesFragment() {
        //required
    }
}
