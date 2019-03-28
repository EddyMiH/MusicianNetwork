package com.musapp.musicapp.fragments.main_fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageMetadata;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarTitle;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.service.UploadForegroundService;
import com.musapp.musicapp.uploads.AttachedFile;
import com.musapp.musicapp.utils.FragmentShowUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
    private int selectedFiles = 1;
    private SetToolBarTitle mSetToolBarTitle;

    private boolean isStoragePermissionAccepted = false;
    private final int SELECT_IMAGE = 12;
    private final int SELECT_VIDEO = 13;
    private final int SELECT_AUDIO = 14;
    public static final String SONG_ARTIST = "Artist";
    public static final String SONG_TITLE = "Title";
    public static final String SONG_DURATION = "Duration";

    private AttachedFile mAttachedFile;
    private User user = CurrentUser.getCurrentUser();

    private Map<String, Uri> fileUri  = new HashMap<>();

    private Bundle bundle;
    private boolean typeIsChoosed = false;

    public AddPostFragment() {

        mNewPost = new Post();
     mNewPost.setProfileImage(CurrentUser.getCurrentUser().getUserInfo().getImageUri());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        mPostText = view.findViewById(R.id.text_fragment_add_post_text);
 //       mPostAttachment = view.findViewById(R.id.recycler_view_fragment_add_post_file);
        mAddImage = view.findViewById(R.id.image_fragment_add_post_attach_image);
        mAddMusic = view.findViewById(R.id.image_fragment_add_post_attach_music);
        mAddVideo = view.findViewById(R.id.image_fragment_add_post_attach_video);
        mSelectedFilesName = view.findViewById(R.id.text_add_post_fragment_selected_file_names);

        mType = PostUploadType.NONE;
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
//        mSetToolBarTitle.setTitle(R.string.add_post_action_bar_title_menu);
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
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
            startActivityForResult(intent, SELECT_VIDEO);
        }
    }

    private void selectAudio(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "audio/*");
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

                final Uri selectedImageUri = data.getData();
                fileUri.put(System.currentTimeMillis() + "." + getFileExtension(selectedImageUri), selectedImageUri);

                }


            else if(requestCode == SELECT_VIDEO){
                mAddMusic.setEnabled(false);
                mAddImage.setEnabled(false);
                final Uri selectedVideoUri = data.getData();
                fileUri.put(System.currentTimeMillis() + "." + getFileExtension(selectedVideoUri), selectedVideoUri);

               mSelectedFilesName.setText(String.valueOf(selectedFiles));

            }else if(requestCode == SELECT_AUDIO){
                mAddVideo.setEnabled(false);
                mAddImage.setEnabled(false);

                Uri selectedAudioUri = data.getData();
                //String[] metadata = getSongCustomName(selectedAudioUri);
                fileUri.put(getSongCustomName(selectedAudioUri) + "." + getFileExtension(selectedAudioUri), selectedAudioUri);

//                StorageMetadata storageMetadata = new StorageMetadata.Builder()
//                        .setCustomMetadata(SONG_ARTIST, metadata[0])
//                        .setCustomMetadata(SONG_TITLE, metadata[1])
//                        .setCustomMetadata(SONG_DURATION, metadata[2])
//                        .build();
//                final StorageReference fileReference = FirebaseRepository.createMusicStorageChild(System.currentTimeMillis() + "." + getFileExtension(selectedAudioUri));
//                FirebaseRepository.putFileInStorageWithMetadata(fileReference, selectedAudioUri, storageMetadata ,new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        FirebaseRepository.getDownloadUrl(fileReference, new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                mAttachedFile.addFile(uri.toString());
//                                selectedFiles++;
//                            }
//                        });
//                    }
//                });

                mSelectedFilesName.setText(String.valueOf(selectedFiles));

//                String audioCustomName = getAudioCustomName(selectedAudioUri);
//                fileUri.put(audioCustomName + "." + getFileExtension(selectedAudioUri), selectedAudioUri);
//                final StorageReference fileReference = FirebaseRepository.createAudioStorageChild(audioCustomName + "." + getFileExtension(selectedAudioUri));
//                FirebaseRepository.putFileInStorage(fileReference, selectedAudioUri, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        FirebaseRepository.getDownloadUrl(fileReference, new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                mAttachedFile.addFile(uri.toString());
//                                selectedFiles++;
//                            }
//                        });
//                    }
//                });
                Log.d("URL AUDIO", "onActivityResult: " + selectedAudioUri.toString());

            }

        }
    }

    public String getSongCustomName(Uri uri){

        String[] metadata = new String[3];
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(getContext(), uri);
        try{
            //get artist name
           metadata[0] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
           if(metadata[0] == null || metadata[0].equals("")){
               metadata[0] = "Unknown";
           }
           //get song title
           metadata[1] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if(metadata[1] == null || metadata[1].equals("")){
                metadata[1] = "Unknown";
            }
           //get song duration
           metadata[2] = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if(metadata[2] == null || metadata[2].equals("")){
                metadata[2] = "9999";
            }

        }catch (Exception ex){
            metadata[0] = "Unknown";
            metadata[1] = "Unknown";
            metadata[2] = "9999";
            ex.printStackTrace();
        }

        return metadata[0] + "$" + metadata[1] + "$" + metadata[2] + "$" ;
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
                savePost();
            }break;
        }
        return true;
    }
    private void savePost(){
        mNewPost.setPostText(mPostText.getText().toString());
        mNewPost.setType(mType);
        DateFormat simple = new SimpleDateFormat("dd MMM HH:mm", Locale.US);
        Date date = new Date(System.currentTimeMillis());
        mNewPost.setPublishedTime( simple.format(date));
        mNewPost.setUserId(CurrentUser.getCurrentUser().getPrimaryKey());
        mNewPost.setUserName(CurrentUser.getCurrentUser().getNickName());
        startService();
        quitFragment();

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

           getFragmentManager().beginTransaction()
                   .remove(this)
                 //  .replace(R.id.layout_activity_app_container, FragmentShowUtils.getPreviousFragment())
                   .commit();
           getFragmentManager().popBackStack();
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
    public void setSetToolBarTitle(SetToolBarTitle toolBarTitle){
        mSetToolBarTitle = toolBarTitle;
    }
}
