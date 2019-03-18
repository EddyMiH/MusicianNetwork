package com.musapp.musicapp.fragments.main_fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarTitle;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.service.UploadForegroundService;
import com.musapp.musicapp.uploads.AttachedFile;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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
    private int selectedFiles;
    private SetToolBarTitle mSetToolBarTitle;

    private boolean isStoragePermissionAccepted = false;
    private final int SELECT_IMAGE = 12;
    private final int SELECT_VIDEO = 13;
    private final int SELECT_AUDIO = 14;

    private AttachedFile mAttachedFile;
    private BaseUploadsAdapter mUploadsAdapter;
    private User user = CurrentUser.getCurrentUser();

    private Map<String, Uri> fileUri  = new HashMap<>();



    public AddPostFragment() {

        mNewPost = new Post();
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
     /*   mDatabaseReference.child("profession_and_bio").addValueEventListener(new ValueEventListener() {
=======

        mDatabaseReference.child("profession_and_bio").addValueEventListener(new ValueEventListener() {
>>>>>>> 6eb200476d57252be643a5103f06269f8a7470f2
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
        });*/
     mNewPost.setProfileImage(CurrentUser.getCurrentUser().getUserInfo().getImageUri());


     /* FirebaseRepository.getCurrentUser(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              mNewPost.setProfileImage(dataSnapshot.getValue(Info.class).getImageUri());
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });*/
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
                isStoragePermissionAccepted();
                mType = PostUploadType.IMAGE;

                mAttachedFile = new AttachedFile();
                mAttachedFile.setFileType(PostUploadType.IMAGE);

                if (isStoragePermissionAccepted){
                    selectImage();
                }
            }
        });
        mAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStoragePermissionAccepted();
                mType = PostUploadType.VIDEO;
                mAttachedFile = new AttachedFile();
                mAttachedFile.setFileType(PostUploadType.VIDEO);
                if (isStoragePermissionAccepted){
                    selectVideo();
                }
            }
        });
        mAddMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStoragePermissionAccepted();
                mType = PostUploadType.MUSIC;
                mAttachedFile = new AttachedFile();
                mAttachedFile.setFileType(PostUploadType.MUSIC);
                if(isStoragePermissionAccepted){
                    selectAudio();
                }
            }
        });
        mSetToolBarTitle.setTitle(R.string.add_post_action_bar_title_menu);
        return view;
    }

    private void selectImage(){

      if (isStoragePermissionAccepted){
        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, SELECT_IMAGE);


      }

    }

    private void selectVideo(){
        if(isStoragePermissionAccepted){
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            intent.setType("video/*");
            startActivityForResult(intent, SELECT_VIDEO);
        }
    }

    private void selectAudio(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent,"Gallery"), SELECT_AUDIO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DODO", "onActivityResult: firs here");

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == SELECT_IMAGE){
                mAddVideo.setEnabled(false);
                mAddMusic.setEnabled(false);

                Log.d("DODO", "onActivityResult: I'm here, in if!!");
                final Uri selectedImageUri = data.getData();
             //   final StorageReference fileReference = FirebaseRepository.cre
               //         DBAccess.creatStorageChild("image/", System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));

                fileUri.put(System.currentTimeMillis() + "." + getFileExtension(selectedImageUri), selectedImageUri);
           //     selectedFiles += "image\n";

/*
                final StorageReference fileReference = FirebaseRepository.createImageStorageChild(System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));
                FirebaseRepository.putFileInStorage(fileReference, selectedImageUri, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        FirebaseRepository.getDownloadUrl(fileReference, new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mAttachedFile.addFile(uri.toString());
                                selectedFiles += "image\n";
                            }
                        });
                    }
                });


           /*     fileReference.putFile(selectedImageUri).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mAttachedFile.addFile(uri.toString());
                                        selectedFiles++;

                                    }
                                });
                            }
                        });*/
                mSelectedFilesName.setText(selectedFiles);


            }else if(requestCode == SELECT_VIDEO){
                mAddMusic.setEnabled(false);
                mAddImage.setEnabled(false);
                final Uri selectedVideoUri = data.getData();
                final StorageReference fileReference = DBAccess.creatStorageChild("video/", System.currentTimeMillis() + "." + getFileExtension(selectedVideoUri));
                fileReference.putFile(selectedVideoUri).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mAttachedFile.addFile(uri.toString());
                                        selectedFiles++;

                                    }
                                });
                            }
                        });
                mSelectedFilesName.setText(selectedFiles);

            }else if(requestCode == SELECT_AUDIO){
                mAddVideo.setEnabled(false);
                mAddImage.setEnabled(false);

                Uri selectedAudioUri = data.getData();
                Log.d("URL AUDIO", "onActivityResult: " + selectedAudioUri.toString());
                //TODO send url to firebase storage
//                final StorageReference fileReference = DBAccess.creatStorageChild("audio/", System.currentTimeMillis() + "." + getFileExtension(selectedAudioUri));
//                fileReference.putFile(selectedAudioUri).addOnSuccessListener(
//                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        mAttachedFile.addFile(uri.toString());
//                                        selectedFiles++;
//
//                                    }
//                                });
//                            }
//                        });
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
            case R.id.save_and_publish_post: {
                //TODO save into Firebase and close fragment
//                if(mAttachedFile.getFileType() != PostUploadType.NONE){
//                    DBAsyncTask.waitResponse("attachments", this, mAttachedFile);
//                }
//                savePost();
                savePost();
              //  getFragmentManager().beginTransaction().replace(R.id.layout_activity_app_container, new HomePageFragment()).addToBackStack(null).commit();


            }break;
                //TODO call UploadForegroundService
            /*    DBAccess.createChild("posts", mNewPost);
                FirebaseDatabase.getInstance().getReference().child("posts").child(postId)
                        .child("primaryKey").setValue(postId);
                User user = CurrentUser.getCurrentUser();
                user.addPostId(postId);
                CurrentUser.setCurrentUser(user);
               */

        }
        return true;
    }
    private void savePost(){
        mNewPost.setPostText(mPostText.getText().toString());
        mNewPost.setType(mType);
        DateFormat simple = new SimpleDateFormat("dd MMM HH:mm");
        Date date = new Date(System.currentTimeMillis());
        mNewPost.setPublishedTime( simple.format(date));
        mNewPost.setUserId(CurrentUser.getCurrentUser().getPrimaryKey());
        mNewPost.setUserName(CurrentUser.getCurrentUser().getNickName());
        startService();
        quitFragment();
        //TODO
      //  mNewPost.setAttachmentId(DBAccess.createChild("attachments", mAttachedFile));
      /*  FirebaseRepository.createAttachment(mAttachedFile, new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseRepository.setInnerAttachmentKey(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> lastChild = dataSnapshot.getChildren().iterator();
                        String attachmentId  = (lastChild.next().getKey());
                        mAttachedFile.setPrimaryKey(attachmentId);
                        mNewPost.setAttachmentId(attachmentId);
                        mNewPost.setUserId(CurrentUser.getCurrentUser().getPrimaryKey());
                        mNewPost.setUserName(CurrentUser.getCurrentUser().getNickName());
                        FirebaseRepository.setInnerAtachmentKeyToFirebase(attachmentId);
                        startService();
                        quitFragment();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void startService(){
        Intent intent = new Intent(getContext(), UploadForegroundService.class);
        intent.setAction(UploadForegroundService.ADD_POST_INTENT_ACTION);
        intent.putExtra(Post.class.getSimpleName(), mNewPost);
        intent.putExtra(AttachedFile.class.getSimpleName(), mAttachedFile);
        intent.putExtra(HashMap.class.getSimpleName(), (Serializable) fileUri);
        getActivity().startService(intent);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void quitFragment() {
       /* FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();*/
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .hide(this)
                .commit();
    }

    public boolean isStoragePermissionAccepted(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            isStoragePermissionAccepted = true;
        }else{
            isStoragePermissionAccepted = false;
        }
        return isStoragePermissionAccepted;
    }
}
