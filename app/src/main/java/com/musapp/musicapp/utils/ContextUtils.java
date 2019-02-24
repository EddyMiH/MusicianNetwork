package com.musapp.musicapp.utils;

import android.support.v4.app.Fragment;

public final class ContextUtils {
    private ContextUtils(){}

    public static String getResourceString(Fragment fragment, int id){
        return fragment.getActivity().getBaseContext().getString(id);
    }
}
