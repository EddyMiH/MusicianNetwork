package com.musapp.musicapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class RememberPreferences {
    private static final String PREF_NAME = "RememberPreferences";
    private static final String IS_QUITED = "quited";


    private RememberPreferences() {

    }

    public static void saveState(Context context, boolean quit) {
        getSharedPreferences(context)
                .edit()
                .putBoolean(IS_QUITED, quit)
                .apply();
    }

    public static boolean getState(Context context){
        return getSharedPreferences(context)
                .getBoolean(IS_QUITED, true);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
