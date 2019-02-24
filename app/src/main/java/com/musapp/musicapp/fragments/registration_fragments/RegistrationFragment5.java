package com.musapp.musicapp.fragments.registration_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.musapp.musicapp.R;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.utils.CheckUtils;
import com.musapp.musicapp.utils.ContextUtils;
import com.musapp.musicapp.utils.ErrorShowUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationFragment5 extends Fragment {
    @BindView(R.id.registration_fragment_4_email) EditText email;
    @BindView(R.id.registration_fragment_4_password) EditText password;
    @BindView(R.id.registration_fragment_4_confirm_password) EditText confirm_password;
    @BindView(R.id.registration_fragment_4_next_button)
    Button nextButton;
    private final String TAG = RegistrationFragment1.class.getSimpleName();
    private View.OnClickListener nextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(submitInformation())
                RegistrationTransactionWrapper.registerForNextFragment((int)nextButton.getTag());
            //TODO set animation to another fragment

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.registration_fragment_5, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton.setTag(R.integer.registration_fragment_5);
        nextButton.setOnClickListener(nextClickListener);
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
        if(!CheckUtils.checkEqual(password, confirm_password)){
            ErrorShowUtils.showEditTextError(confirm_password, ContextUtils.getResourceString(this, R.string.password_match));
            return false;
        }
        return true;
    }

    private boolean submitInformation(){

        if(checEditTextField()) {
            //TODO set information to user local object
            return true;
    }
        return false;
    }
}
