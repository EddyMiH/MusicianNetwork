package com.musapp.musicapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class NavigatorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Class<?> activityClass;

        try {
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            activityClass = Class.forName(
                    prefs.getString("lastActivity", StartActivity.class.getName()));

        } catch(ClassNotFoundException ex) {
            activityClass = StartActivity.class;
        }

        startActivity(new Intent(this, activityClass));
        finish();
    }
}
