package com.musapp.musicapp.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.URL;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public final class GlideUtil {

    private static Context context;

    public static void setContext(Context ctx){
        context = ctx;
    }

    public static void setImageGlide(String src, ImageView view){
        //.transform(new RoundedCornersTransformation(radius, margin))
        Glide.with(context).load(src)
                .apply(RequestOptions.circleCropTransform())
                .into(view);
    }

    public static  void setImageGlideSquare(String src, ImageView view){
        Glide.with(context).load(src)
                .into(view);
    }

}
