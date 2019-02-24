package com.musapp.musicapp.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.musapp.musicapp.R;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment1;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment2;
import com.musapp.musicapp.fragments.registration_fragments.RegistrationFragment5;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegisterFragmentTransaction;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;


public class MainActivity extends AppCompatActivity {

  private   RegistrationFragment1 register1;
  private   RegistrationFragment2 register2;
  private RegistrationFragment5 register4;

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
              //TODO go to user main page
          }
      }
  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        RegistrationTransactionWrapper.setRegisterFragmentTransaction(registerFragmentTransaction);
        beginTransaction(register1);
    }

    private void init(){
        register1 = new RegistrationFragment1();
        register2 = new RegistrationFragment2();
        register4 = new RegistrationFragment5();
    }

    private void beginTransaction(Fragment fragment){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
