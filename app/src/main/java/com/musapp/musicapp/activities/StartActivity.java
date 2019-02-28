package com.musapp.musicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;


import com.musapp.musicapp.R;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment1;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment2;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment5;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegisterFragmentTransaction;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.fragments.sign_in_fragments.SignInFragment;
import com.musapp.musicapp.preferences.RegisterPreferences;
import com.musapp.musicapp.preferences.RememberPreferences;


public class StartActivity extends AppCompatActivity {

  private   RegistrationFragment1 register1;
  private   RegistrationFragment2 register2;
  private RegistrationFragment5 register4;
  private SignInFragment signInFragment;
  ConstraintLayout main;

  private RegisterFragmentTransaction registerFragmentTransaction = new RegisterFragmentTransaction() {
      @Override
      public void getNextFragment(int id) {
          if(id == R.integer.registration_fragment_1) {
              beginTransaction(register2);
          }
          if(id == R.integer.registration_fragment_2) {
              beginTransaction(register4);
          }
          if(id == R.integer.registration_fragment_5) {
             beginTransaction(signInFragment);
          }
          if(id == R.integer.sign_in_fragment_main_page){
              startMainPageActivity();
          }
          if(id == R.integer.sign_in_fragment_registration){
              beginTransaction(register1);
          }
          if(id == R.integer.sign_in_fragment_forgot_pass){
              //TODO beginTransaction(forgotPasswordFragment)
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

    private void init(){
        register1 = new RegistrationFragment1();
        register2 = new RegistrationFragment2();
        register4 = new RegistrationFragment5();
        signInFragment = new SignInFragment();
    }

    private void beginTransaction(Fragment fragment){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.layout_activity_start_content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void start(){
        if(!RegisterPreferences.getState(this))
            beginTransaction(register1);
        else if(!RememberPreferences.getState(this)){
           beginTransaction(signInFragment);
        }
        else
            startMainPageActivity();
    }
    private void startMainPageActivity(){
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
       //TODO call onDestroy() when necessary
    }
}
