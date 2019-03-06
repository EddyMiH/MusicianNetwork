package com.musapp.musicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.musapp.musicapp.R;
import com.musapp.musicapp.fragments.main_fragments.NotificationFragment;
import com.musapp.musicapp.fragments.main_fragments.ProfileFragment;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarTitle;
import com.musapp.musicapp.preferences.RememberPreferences;

public class AppMainActivity extends AppCompatActivity {


    private NotificationFragment mNotificationFragment;
    private ProfileFragment mProfileFragment;
    private SetToolBarTitle mToolBarTitle = new SetToolBarTitle() {
        @Override
        public void setTitle(int titleId) {
            getSupportActionBar().setTitle(getString(titleId));
        }
    };

    private ProfileFragment.ChangeActivity mChangeActivity = new ProfileFragment.ChangeActivity() {
        @Override
        public void changeActivity( Class nextActivity) {
           activityTransaction( nextActivity);
        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
              //      mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_mail:
                //    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                 beginTransaction(mNotificationFragment);
                    return true;
                case R.id.navigation_profile:
                    beginTransaction(mProfileFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_activity_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        init();
    }

    private void init(){
        mNotificationFragment = new NotificationFragment();
        mProfileFragment = new ProfileFragment();
        mProfileFragment.setSetToolBarTitle(mToolBarTitle);
        mNotificationFragment.setSetToolBarTitle(mToolBarTitle);
        mProfileFragment.setChangeActivity(mChangeActivity);
    }

    private void beginTransaction(Fragment fragment){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.layout_activity_app_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }





    private void activityTransaction(Class nextActivity){
        Intent intent = new Intent(this, nextActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(getString(R.string.quit), false );
        startActivity(intent);
        finish();
    }
}
