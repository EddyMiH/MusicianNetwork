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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.preferences.RegisterPreferences;
import com.musapp.musicapp.utils.CheckUtils;
import com.musapp.musicapp.utils.ContextUtils;
import com.musapp.musicapp.utils.ErrorShowUtils;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationFragment5 extends Fragment {
    @BindView(R.id.text_fragment_registration_5_email)
    EditText email;
    @BindView(R.id.text_fragment_registration_5_password)
    EditText password;
    @BindView(R.id.text_fragment_registration_5_confirm_pass)
    EditText confirm_password;
    @BindView(R.id.action_fragment_registration_5_next)
    Button nextButton;

    private User user = CurrentUser.getCurrentUser();
    private View.OnClickListener nextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (submitInformation()) {
                RegistrationTransactionWrapper.registerForNextFragment((int) nextButton.getTag());
                RegisterPreferences.saveState(getActivity().getBaseContext(), true);
                CurrentUser.setCurrentUser(user);
                setUserPrimaryKey();
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
        View view = inflater.inflate(R.layout.fragment_registration_5, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton.setTag(R.integer.registration_fragment_5);
        nextButton.setOnClickListener(nextClickListener);
    }

    private boolean checkEditTextField() {
        if (CheckUtils.checkEditTextEmpty(email)) {
            ErrorShowUtils.showEditTextError(email, ContextUtils.getResourceString(this, R.string.empty_error_message));
            return false;
        }
        if (!CheckUtils.checkIsMail(email)) {
            ErrorShowUtils.showEditTextError(email, ContextUtils.getResourceString(this, R.string.wrong_email_message));
            return false;
        }
        if (CheckUtils.checkEditTextEmpty(password)) {
            ErrorShowUtils.showEditTextError(password, ContextUtils.getResourceString(this, R.string.empty_error_message));
            return false;
        }
        if (!CheckUtils.checkEqual(password, confirm_password)) {
            ErrorShowUtils.showEditTextError(confirm_password, ContextUtils.getResourceString(this, R.string.password_match));
            return false;
        }
        return true;
    }

    private boolean submitInformation() {

        if (checkEditTextField()) {
            user.setEmail(email.getText().toString());
            user.setPassword(password.getText().toString());
            return true;
        }
        return false;
    }

    private void setUserPrimaryKey() {
        FirebaseRepository.addCurrentUserToFirebase(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseRepository.setUserInnerPrimaryKey(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> lastChild = dataSnapshot.getChildren().iterator();
                        CurrentUser.getCurrentUser().setPrimaryKey(lastChild.next().getKey());
                        FirebaseRepository.setUserInnerPrimaryKeyToFirebase();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
