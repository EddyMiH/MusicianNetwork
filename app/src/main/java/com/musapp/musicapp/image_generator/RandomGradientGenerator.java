package com.musapp.musicapp.image_generator;

import android.graphics.drawable.GradientDrawable;

import java.util.Random;

public class RandomGradientGenerator {
    private RandomGradientGenerator(){}
    private static Random random = new Random();

    public static GradientDrawable getRandomGradient(){
        return  new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {random.nextInt(),random.nextInt(),random.nextInt() });
    }

}
