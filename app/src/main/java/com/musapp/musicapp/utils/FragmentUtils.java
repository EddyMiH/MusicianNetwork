package com.musapp.musicapp.utils;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public final class FragmentUtils {
    private FragmentUtils(){}

    public static boolean checkLastStackFragment( Fragment fragment){
        FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        return fragment == fragmentManager.findFragmentByTag(fragmentTag);
    }
}
