package com.musapp.musicapp.fragments.registration_fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.musapp.musicapp.R;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationFragment2 extends Fragment {

     @BindView(R.id.date_fragment_registration_2_birth) DatePicker datePicker;
     @BindView(R.id.action_fragment_registration_2_male) RadioButton maleRadioButton;
     @BindView(R.id.action_fragment_registration_2_female) RadioButton femaleRadioButton;
     @BindView(R.id.action_fragment_registration_2_next) Button nextButton;

    private Calendar birthDate = Calendar.getInstance();

    private DatePicker.OnDateChangedListener dateChangedListener =
            new DatePicker.OnDateChangedListener(){
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                   birthDate.set(Calendar.YEAR, datePicker.getYear());
                   birthDate.set(Calendar.MONTH, datePicker.getMonth());
                   birthDate.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                }
            };
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            submitInformation();
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
       View view =  inflater.inflate(R.layout.fragment_registration_2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(dateChangedListener);
        }
        nextButton.setTag(R.integer.registration_fragment_2);
        nextButton.setOnClickListener(onClickListener);
    }

    private void submitInformation(){
        //TODO set information to local user
    }

}
