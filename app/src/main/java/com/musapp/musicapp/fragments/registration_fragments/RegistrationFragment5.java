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
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.firebase.DBAsyncTask;
import com.musapp.musicapp.firebase.DBAsyncTaskResponse;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.preferences.RegisterPreferences;
import com.musapp.musicapp.utils.CheckUtils;
import com.musapp.musicapp.utils.ContextUtils;
import com.musapp.musicapp.utils.ErrorShowUtils;
import com.musapp.musicapp.utils.HashUtils;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationFragment5 extends Fragment implements DBAsyncTaskResponse {
    @BindView(R.id.text_fragment_registration_5_email) EditText email;
    @BindView(R.id.text_fragment_registration_5_password) EditText password;
    @BindView(R.id.text_fragment_registration_5_confirm_pass) EditText confirm_password;
    @BindView(R.id.action_fragment_registration_5_next) Button nextButton;

    private User user = CurrentUser.getCurrentUser();
    private View.OnClickListener nextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(submitInformation()) {
                RegistrationTransactionWrapper.registerForNextFragment((int) nextButton.getTag());
                RegisterPreferences.saveState(getActivity().getBaseContext(), true);
                CurrentUser.setCurrentUser(user);
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_registration_5, container, false);
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
             user.setEmail(email.getText().toString());
            DBAsyncTask.waitResponse("user", this,  user);
            return true;
    }
        return false;
    }

    @Override
    public void doOnResponse(String key) {
        user.setPrimaryKey(key);
        user.setPassword(password.getText().toString());
     //   DBAccess.createChild("user/" + user.getPrimaryKey() + '/', "primaryKey", user.getPrimaryKey());
        HashMap<String, Object> userHashMap = new HashMap<>();
        //TODO if nested primaryKey is needed
        userHashMap.put("primaryKey", user.getPrimaryKey());
        userHashMap.put("password", HashUtils.hash(password.getText().toString()));
        DBAccess.createFields(userHashMap, "user/"+user.getPrimaryKey()+'/');
    }

    @Override
    public void doForResponse(String str, Object obj) {
        DBAccess.createChild("user", user);
    }
}
