package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musapp.musicapp.R;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarTitle;

public class MessagesFragment extends Fragment {

    private SetToolBarTitle mSetToolBarTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //mSetToolBarTitle.setTitle(R.string.title_dashboard);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setSetToolBarTitle(SetToolBarTitle setToolBarTitle) {
        mSetToolBarTitle = setToolBarTitle;
    }

    public MessagesFragment() {
        //required
    }
}
