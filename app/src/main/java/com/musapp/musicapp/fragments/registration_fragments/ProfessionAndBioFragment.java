package com.musapp.musicapp.fragments.registration_fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.musapp.musicapp.R;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.model.ProfessionAndInfo;
import com.musapp.musicapp.utils.UIUtils;


public class ProfessionAndBioFragment extends Fragment  implements AdapterView.OnItemSelectedListener {

    private ImageView profileImage;
    private Spinner professionSpinner;
    private TextView userInfoTextView;
    private Button nextButton;
    private ProfessionAndInfo userInfo;

    private final int REQUEST_CAMERA = 1;
    private final int SELECT_FILE = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userInfo = new ProfessionAndInfo();
        View rootView = inflater.inflate(R.layout.profession_bio_fragment, container, false);
        profileImage = rootView.findViewById(R.id.profile_img_image_view);
        professionSpinner = rootView.findViewById(R.id.profession_spinner_view);
        userInfoTextView = rootView.findViewById( R.id.user_info_text_view_profession_fragment);
        nextButton = UIUtils.getButtonFromView(getActivity().findViewById(R.id.layout_activity_start_content_main), R.id.action_fragment_grid_and_profession_next);
        professionSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> spinnerDataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.professions, android.R.layout.simple_spinner_dropdown_item);
        spinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(spinnerDataAdapter);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo.setAdditionalInfo(userInfoTextView.getText().toString());
                RegistrationTransactionWrapper.registerForNextFragment((int)nextButton.getTag());

                //TODO save data and change fragment
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton.setTag(R.integer.registration_fragment_professions_4);
    }

    private void selectImage(){
        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                profileImage.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE){

                Uri selectedImageUri = data.getData();
                profileImage.setImageURI(selectedImageUri);
                userInfo.setImageUri(selectedImageUri);
            }

        }
    }

    public ProfessionAndBioFragment() {
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        userInfo.setProfession(item);
        Toast.makeText(getContext(), item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

