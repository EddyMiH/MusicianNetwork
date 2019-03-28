package com.musapp.musicapp.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.MusicPostViewHolder;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.BlankFragment;
import com.musapp.musicapp.fragments.main_fragments.HomePageFragment;
import com.musapp.musicapp.fragments.main_fragments.MessagesFragment;
import com.musapp.musicapp.fragments.main_fragments.NotificationFragment;
import com.musapp.musicapp.fragments.main_fragments.ProfileFragment;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarTitle;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.preferences.RememberPreferences;
import com.musapp.musicapp.service.MusicPlayerService;
import com.musapp.musicapp.utils.GlideUtil;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppMainActivity extends AppCompatActivity {


    private NotificationFragment mNotificationFragment;
    private ProfileFragment mProfileFragment;
    private HomePageFragment mHomePageFragment;
    private BlankFragment mMessagesFragment;
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
              //     mTextMessage.setText(R.string.title_home);
                    mHomePageFragment.setPlayerServiceConnection(mPlayerServiceConnection);
                    beginTransaction(mHomePageFragment);
                    return true;

                case R.id.navigation_mail:
                //    mTextMessage.setText(R.string.title_dashboard);
                    beginTransaction(mMessagesFragment);
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
        GlideUtil.setContext(this);
        setCurrentUser();
        init();
        navigation.setSelectedItemId(R.id.navigation_home);
new Notify().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.apply();
    }

    private void init(){
        mNotificationFragment = new NotificationFragment();
        mProfileFragment = new ProfileFragment();
        mHomePageFragment = new HomePageFragment();
        mHomePageFragment.setSetToolBarTitle(mToolBarTitle);
        mMessagesFragment = new BlankFragment();
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


    @Override
    public void onBackPressed() {


            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 0 || count == 1) {
                finish();
                //additional code
            } else {
                getSupportFragmentManager().popBackStack();
            }

        }

    private void setCurrentUser(){
        FirebaseRepository.setCurrentUser(RememberPreferences.getUser(this), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CurrentUser.setCurrentUser(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private MusicPlayerService.LocalBinder mLocalBinder;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLocalBinder = (MusicPlayerService.LocalBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLocalBinder = null;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicPlayerService.class);
        this.bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mLocalBinder != null){
            mLocalBinder.stop();

        }
        this.unbindService(mServiceConnection);
    }

    private MusicPlayerServiceConnection mPlayerServiceConnection =
            new MusicPlayerServiceConnection() {

                @Override
                public void play(String url) {
                    if(mLocalBinder != null){
                       mLocalBinder.play(url);
                    }
                }
                @Override
                public void pause() {
                    mLocalBinder.pause();
                }

                @Override
                public void seekTo(int progress) {
                    mLocalBinder.seekTo(progress);
                }


                @Override
                public void handleSeekBar( SeekBar seekBar, Button button) {
                    mLocalBinder.setSeekBar(seekBar);
                    mLocalBinder.setPlayPauseButton(button);
                }

                @Override
                public boolean isPlayerPlaying(String url) {
                    if (mLocalBinder.isPlaying() && mLocalBinder.getCurrentUrl().equals(url)){
                        //mLocalBinder.startSeekBarHandle();
                        return true;
                    }
                    return false;
                }
            };

    public interface MusicPlayerServiceConnection{
        void play(String url);
        void pause();
        void seekTo(int progress);
        void handleSeekBar(SeekBar seekBar, Button button);
        boolean isPlayerPlaying(String url);
    }


    public class Notify extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {


            try {

                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "key=AIzaSyCgGkIpG7sPYu99dcZEuhLWr3KacbjGzzU");
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();

                String tkn = FirebaseInstanceId.getInstance().getToken();


                        /*getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                       t[0] = getTokenResult.getToken();
                    }
                });*/
                json.put("to", tkn);


                JSONObject info = new JSONObject();
                info.put("title", "TechnoWeb");   // Notification title
                info.put("body", "Hello Test notification"); // Notification body

                json.put("notification", info);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                conn.getInputStream();

            }
            catch (Exception e)
            {
                Log.d("Error",""+e);
            }


            return null;
        }
    }
}
