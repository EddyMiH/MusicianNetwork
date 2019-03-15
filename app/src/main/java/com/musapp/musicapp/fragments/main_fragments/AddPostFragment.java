package com.musapp.musicapp.fragments.main_fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.model.Info;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.uploads.AttachedFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPostFragment extends Fragment {

    private Post mNewPost;
    private EditText mPostText;
    private RecyclerView mPostAttachment;
    private ImageView mAddImage;
    private ImageView mAddVideo;
    private ImageView mAddMusic;
    private TextView mSelectedFilesName;
    DatabaseReference mDatabaseReference;
    private PostUploadType mType;
    private String selectedFiles = "selected";

    private boolean isStoragePermissionAccepted = false;
    private final int SELECT_FILE_1 = 12;

    private AttachedFile mAttachedFile;
    private BaseUploadsAdapter mUploadsAdapter;


    public AddPostFragment() {

        mNewPost = new Post();
        mNewPost.setUserName(CurrentUser.getCurrentUser().getNickName());
        //get image url from profession and bio database
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        Query query = mDatabaseReference.child("profession_and_bio").equalTo(CurrentUser.getCurrentUser().getPrimaryKey());
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ProfessionAndInfo info = dataSnapshot.getValue(ProfessionAndInfo.class);
//                mNewPost.setProfileImage(info.getImageUri());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("EVENT_LISTENER ", "onCancelled: something wrong !!");
//            }
//        });
        mDatabaseReference.child("profession_and_bio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    if (data.getKey().equals(CurrentUser.getCurrentUser().getUserInfo())){
                        mNewPost.setProfileImage(data.getValue(Info.class).getImageUri());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        mPostText = view.findViewById(R.id.text_fragment_add_post_text);
        mPostAttachment = view.findViewById(R.id.recycler_view_fragment_add_post_file);
        mAddImage = view.findViewById(R.id.image_fragment_add_post_attach_image);
        mAddMusic = view.findViewById(R.id.image_fragment_add_post_attach_music);
        mAddVideo = view.findViewById(R.id.image_fragment_add_post_attach_video);
        mSelectedFilesName = view.findViewById(R.id.text_add_post_fragment_selected_file_names);

        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED){
                            isStoragePermissionAccepted = true;

                }
                mType = PostUploadType.IMAGE;
                mAttachedFile = new AttachedFile();
                mAttachedFile.setFileType(PostUploadType.IMAGE);

                if (isStoragePermissionAccepted){
                    selectImage();
                }
            }
        });
        return view;
    }

    private void selectImage(){

      if (isStoragePermissionAccepted){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE_1);
          Log.d("DODO", "onActivityResult: zero here");

      }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DODO", "onActivityResult: firs here");

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == SELECT_FILE_1){
                Log.d("DODO", "onActivityResult: I'm here, in if!!");
                final Uri selectedImageUri = data.getData();
                final StorageReference fileReference = DBAccess.creatStorageChild("image/", System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));
                fileReference.putFile(selectedImageUri).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mAttachedFile.addFile(uri.toString());
                                        selectedFiles += "image\n";

                                    }
                                });
                            }
                        });
                mSelectedFilesName.setText(selectedFiles);
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_post_fragment_publish_post, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_and_publish_post:
                //TODO save into Firebase and close fragment
                savePost();
                String postId = "";
                //TODO
                DBAccess.createChild("posts", mNewPost);
                FirebaseDatabase.getInstance().getReference().child("posts").child(postId)
                        .child("primaryKey").setValue(postId);
                User user = CurrentUser.getCurrentUser();
                user.addPostId(postId);
                CurrentUser.setCurrentUser(user);
                getFragmentManager().beginTransaction().replace(R.id.layout_activity_app_container, new HomePageFragment()).addToBackStack(null).commit();

        }
        return true;
    }
    private void savePost(){
        mNewPost.setPostText(mPostText.getText().toString());
        mNewPost.setType(mType);
        DateFormat simple = new SimpleDateFormat("dd MMM HH:mm");
        Date date = new Date(System.currentTimeMillis());
        mNewPost.setPublishedTime( simple.format(date));
        //TODO
      //  mNewPost.setAttachmentId(DBAccess.createChild("attachments", mAttachedFile));
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}