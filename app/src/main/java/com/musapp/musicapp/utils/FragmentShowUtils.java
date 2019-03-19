package com.musapp.musicapp.utils;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public final class FragmentShowUtils {
    private FragmentShowUtils(){}

    private static Fragment previousFragment;
    private static Fragment currentFragment;

    public static void hideAndGoToNext(Fragment firstFragment, Fragment secondFragment, FragmentManager fragmentManager, @IdRes int frame){
        previousFragment = firstFragment;
        currentFragment = secondFragment;
        fragmentManager.beginTransaction().replace(frame, secondFragment).commit();
    }

    public static void goBack(FragmentManager fragmentManager, @IdRes int frame){
        fragmentManager.beginTransaction().replace(frame, previousFragment).commit();
    }

    public static void goBack(FragmentManager fragmentManager, @IdRes int frame, Bundle bundle){
        previousFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(frame, previousFragment).commit();
    }


    public static Fragment getPreviousFragment() {
        return previousFragment;
    }
}
