package com.musapp.musicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.musapp.musicapp.R;
import com.musapp.musicapp.fragments.registration_fragments.GenreGridFragment;
import com.musapp.musicapp.fragments.registration_fragments.ProfessionAndBioFragment;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment1;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment2;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment5;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegisterFragmentTransaction;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.fragments.sign_in_fragments.SignInFragment;
import com.musapp.musicapp.preferences.RegisterPreferences;
import com.musapp.musicapp.preferences.RememberPreferences;

import butterknife.BindView;


public class StartActivity extends AppCompatActivity {

    private RegistrationFragment1 register1;
    private RegistrationFragment2 register2;
    private RegistrationFragment5 register4;
    private GenreGridFragment register3;
    private ProfessionAndBioFragment register5;
    private SignInFragment signInFragment;


    private RegisterFragmentTransaction registerFragmentTransaction = new RegisterFragmentTransaction() {
        @Override
        public void getNextFragment(int id) {
            if (id == R.integer.registration_fragment_1) {
                beginTransaction(register2);
            }
            if (id == R.integer.registration_fragment_2) {
                beginTransaction(register4);
            }
            if (id == R.integer.registration_fragment_5) {
                //beginTransaction(signInFragment);
                beginTransaction(register3);
            }
            if (id == R.integer.sign_in_fragment_main_page) {
                startMainPageActivity();
            }
            if (id == R.integer.sign_in_fragment_registration) {
                beginTransaction(register1);
            }
            if (id == R.integer.sign_in_fragment_forgot_pass) {
                //TODO beginTransaction(forgotPasswordFragment)
            }
            //@Gohar i pass wrong fragments in beginTransaction method and don't change above fragments sequence
            //please fix it, I could not understand which fragment is going after other fragments
            if(id == R.integer.registration_fragment_grid_view_3){
                beginTransaction(register5);
            }
            if(id == R.integer.registration_fragment_professions_4){
                beginTransaction(signInFragment);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);
        init();
        RegistrationTransactionWrapper.setRegisterFragmentTransaction(registerFragmentTransaction);
        start();


    }

    private void init() {
        register1 = new RegistrationFragment1();
        register2 = new RegistrationFragment2();
        register4 = new RegistrationFragment5();
        register3 = new GenreGridFragment();
        register5 = new ProfessionAndBioFragment();
        signInFragment = new SignInFragment();
    }

    private void beginTransaction(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //I change this set with my version of animation, if you don't like it, change =)
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        if(fragment instanceof GenreGridFragment || fragment instanceof ProfessionAndBioFragment){
            //TODO call slideContainerToLeft method, see below  |
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left
                    , R.anim.slide_in_left, R.anim.slide_out_right);
            findViewById(R.id.container_grid_and_profession_fragment).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_activity_start_content_main).setVisibility(View.GONE);
            transaction.replace(R.id.container_grid_and_profession_fragment, fragment);
        }else{
            findViewById(R.id.container_grid_and_profession_fragment).setVisibility(View.GONE);
            findViewById(R.id.layout_activity_start_content_main).setVisibility(View.VISIBLE);

            transaction.replace(R.id.layout_activity_start_content_main, fragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //for animation fragment visibility
    private void slideContainerToLeft(boolean hide){
        FrameLayout view = findViewById(R.id.container_grid_and_profession_fragment);
        TranslateAnimation animate = new TranslateAnimation(0, -view.getWidth(), 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    private void start() {
        if (!RegisterPreferences.getState(this))
            beginTransaction(register1);
        else if (!RememberPreferences.getState(this)) {
            beginTransaction(signInFragment);
        } else
            startMainPageActivity();
    }

    private void startMainPageActivity() {
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.layout_activity_start_content_main);
        if (f instanceof RegistrationFragment1 || f instanceof SignInFragment) {
            finish();
        }
        super.onBackPressed();
    }
}
