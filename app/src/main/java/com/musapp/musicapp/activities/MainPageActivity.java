package com.musapp.musicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.musapp.musicapp.R;
import com.musapp.musicapp.image_generator.RandomGradientGenerator;
import com.musapp.musicapp.preferences.RememberPreferences;

public class MainPageActivity extends AppCompatActivity {

    private Menu mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        findViewById(R.id.actity_main_page_content).setBackground(RandomGradientGenerator.getRandomGradient());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();
        getMenuInflater().inflate(R.menu.activity_main_page_menu, menu);
        mMenu = menu;
        return true;
    }

   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.quit_activity_main_page_menu:{
                RememberPreferences.saveState(getApplicationContext(), false);
                startSignIn();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSignIn(){
        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
    protected void setMenuBackground(){
        // Log.d(TAG, "Enterting setMenuBackGround");
        getLayoutInflater().setFactory( new LayoutInflater.Factory() {

            @Override
            public View onCreateView(String name, Context context,
                                     AttributeSet attrs) {
                if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {
                    try { // Ask our inflater to create the view
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView( name, null, attrs );
                        /* The background gets refreshed each time a new item is added the options menu.
                         * So each time Android applies the default background we need to set our own
                         * background. This is done using a thread giving the background change as runnable
                         * object */
                        new Handler().post(new Runnable() {
                            public void run () {
                                // sets the background color
                                view.setBackgroundColor(Color.TRANSPARENT);
                            }
                        } );
                        return view;
                    }
                    catch ( InflateException|ClassNotFoundException e ) {}
                }
                return null;
            }});
    }
}
