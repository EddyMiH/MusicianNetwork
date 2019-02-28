package com.musapp.musicapp.fragments.sign_in_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.musapp.musicapp.R;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.preferences.RegisterPreferences;
import com.musapp.musicapp.preferences.RememberPreferences;
import com.musapp.musicapp.utils.CheckUtils;
import com.musapp.musicapp.utils.ContextUtils;
import com.musapp.musicapp.utils.ErrorShowUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInFragment extends Fragment {

    @BindView(R.id.text_fragment_sign_in_email)
    EditText email;
    @BindView(R.id.text_fragment_sign_in_password)
    EditText password;
    @BindView(R.id.action_fragment_sign_in_sign_in)
    Button signIn;
    @BindView(R.id.action_fragment_sign_in_sign_up)
    Button signUp;
    @BindView(R.id.action_fragment_sign_in_forgot_pass)
    Button forgotPass;
    @BindView(R.id.check_fragment_sign_in_remember)
    CheckBox remembered;


    private View.OnClickListener onSignInClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(checkEnteredInformation()){
                RegistrationTransactionWrapper.registerForNextFragment((int)signIn.getTag());
                RememberPreferences.saveState(getActivity().getBaseContext(),remembered.isChecked());

            }

        }
    };

   private  View.OnClickListener onSignUpClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegistrationTransactionWrapper.registerForNextFragment((int)signUp.getTag());
            RegisterPreferences.saveState(getActivity().getBaseContext(), false);
        }
    };

   private View.OnClickListener onForgotPassClickListener = new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           //TODO
       }
   };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signIn.setTag(R.integer.sign_in_fragment_main_page);
        signIn.setOnClickListener(onSignInClickListener);
        signUp.setTag(R.integer.sign_in_fragment_registration);
        signUp.setOnClickListener(onSignUpClickListener);
        forgotPass.setTag(R.integer.sign_in_fragment_forgot_pass);
        forgotPass.setOnClickListener(onForgotPassClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private boolean checEditTextField(){
        if(CheckUtils.checkEditTextEmpty(email)){
            ErrorShowUtils.showEditTextError(email, ContextUtils.getResourceString(this, R.string.empty_error_message));
            return false;
        }
        if(!CheckUtils.checkIsMail(email)){
            ErrorShowUtils.showEditTextError(email, ContextUtils.getResourceString(this, R.string.wrong_email_message));
            return false;
        }
        if(CheckUtils.checkEditTextEmpty(password)){
            ErrorShowUtils.showEditTextError(password, ContextUtils.getResourceString(this, R.string.empty_error_message));
            return false;
        }

        return true;
    }




    private boolean checkEnteredInformation() {
        if(checEditTextField()) {
            //TODO check entered email and password
            return true;
        }
        return false;
    }
}
