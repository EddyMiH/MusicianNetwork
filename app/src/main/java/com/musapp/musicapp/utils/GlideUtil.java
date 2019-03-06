package com.musapp.musicapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public final class GlideUtil {

    private static Context context;

    public static void setContext(Context ctx){
        context = ctx;
    }

    public static void setImageGlide(String src, ImageView view){
        int radius = 30;
        int margin = 10;
        //.transform(new RoundedCornersTransformation(radius, margin))
        Glide.with(context).load(src)
                .apply(RequestOptions.circleCropTransform())
                .into(view);
    }
}
