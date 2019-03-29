package com.musapp.musicapp.fragments.main_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.adapters.CommentRecyclerViewAdapter;
import com.musapp.musicapp.adapters.inner_post_adapter.BaseUploadsAdapter;
import com.musapp.musicapp.adapters.viewholders.post_viewholder.BasePostViewHolder;
import com.musapp.musicapp.currentinformation.CurrentUser;

import com.musapp.musicapp.enums.PostUploadType;
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarAndNavigationBarState;
import com.musapp.musicapp.model.Comment;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;

import com.musapp.musicapp.pattern.UploadTypeFactory;
import com.musapp.musicapp.pattern.UploadsAdapterFactory;
import com.musapp.musicapp.uploads.BaseUpload;
import com.musapp.musicapp.utils.GlideUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.Iterator;
import java.util.List;

import static com.musapp.musicapp.utils.StringUtils.getSongUri;

public class PostDetailsFragment extends Fragment {

    private ImageView mProfileImage;
    private TextView mFullName;
    private TextView mPublishedTime;
    private TextView mPostText;
    private EditText mCommentText;
    private ImageView mPostSetting;
    private Button mPublishCommentButton;
//    private TextView mCommentCount;
    private RecyclerView mPostAttachmentsRecyclerView;
    private RecyclerView mPostCommentsRecyclerView;
    private CommentRecyclerViewAdapter mCommentAdapter;
    private Post mCurrentPost;
    private SetToolBarAndNavigationBarState mSetToolBarAndNavigationBarState;
    private RecyclerView mRecyclerView;
    private BaseUploadsAdapter<BaseUpload, BasePostViewHolder> mAdapter;
    private AppMainActivity.ClickListener mClickListener;

    public void setClickListener(AppMainActivity.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setSetToolBarAndNavigationBarState(SetToolBarAndNavigationBarState setToolBarAndNavigationBarState) {
        mSetToolBarAndNavigationBarState = setToolBarAndNavigationBarState;
    }

    private User mCurrentUser = CurrentUser.getCurrentUser();
    private String mCurrentUserImage;

    private List<Comment> mComments;

    private AppMainActivity.MusicPlayerServiceConnection mPlayerServiceConnection;

    private BaseUploadsAdapter.OnItemSelectedListener mInnerMusicItemOnClickListener = new BaseUploadsAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(String uri) {
            mPlayerServiceConnection.play(uri);
        }
    };

    private BaseUploadsAdapter.OnMusicSeekBarListener mMusicSeekBarListener =
            new BaseUploadsAdapter.OnMusicSeekBarListener() {
                @Override
                public void onSeekBarChanged(int position) {
                    mPlayerServiceConnection.seekTo(position);
                }

                @Override
                public void onStartHandle(View view, View view2) {
                    mPlayerServiceConnection.handleSeekBar( (SeekBar) view, (Button) view2);
                }
            };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(mPostSetting.getContext(), mPostSetting);
            popupMenu.inflate(R.menu.menu_pop_up_post_item);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.favorite_pop_up_menu_item:
                            CurrentUser.getCurrentUser().addFavouritePostId(mCurrentPost.getPrimaryKey());
                            updateUsersFavouritePosts();
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    };

    private BaseUploadsAdapter.OnItemSelectedListener mInnerImageItemOnClickListener = new BaseUploadsAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(String uri) {
            //TODO open full screen fragment and showToolBar images
            FullScreenImageFragment fragment = new FullScreenImageFragment();
            List<String> arr = new ArrayList<>();
            arr = mCurrentPost.getAttachment().getFilesUrls();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(FullScreenImageFragment.IMAGE_DATA,(ArrayList<String>) arr);
            bundle.putInt(FullScreenImageFragment.IMAGE_POSITION, arr.indexOf(uri));
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.layout_activity_app_container, fragment).addToBackStack(null)
                    .commit();
            //mTransaction.openFragment(fragment);
            Log.d("Image click:", "onItemSelected: uri = " + uri);
        }
    };

    private void updateUsersFavouritePosts(){
        FirebaseRepository.updateCurrentUserFavouritePosts();
    }

    public void setPlayerServiceConnection(AppMainActivity.MusicPlayerServiceConnection connection){
        mPlayerServiceConnection = connection;
    }

    private CommentRecyclerViewAdapter.OnCommentItemSelectedListener mOnCommentItemSelectedListener =
            new CommentRecyclerViewAdapter.OnCommentItemSelectedListener() {
                @Override
                public void onItemSelected(String id) {
                    //TODO open mCurrentUser page who write comment, by mCurrentUser id
                }
            };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //loadUserProfileImage();

        View view  = inflater.inflate(R.layout.fragment_post_single_opened_post, container, false);
        mProfileImage = view.findViewById(R.id.image_profile_image_post);
        mFullName = view.findViewById(R.id.text_post_item_user_name);
        mPublishedTime = view.findViewById(R.id.text_post_item_published_time);
        mPostText = view.findViewById(R.id.text_post_item_post_text);
        mCommentText = view.findViewById(R.id.edit_text_write_comment_opened_post);
        mPublishCommentButton = view.findViewById(R.id.action_comment_view_send_comment);
        mPostCommentsRecyclerView = view.findViewById(R.id.recycler_view_comments_in_post);
        mRecyclerView = view.findViewById(R.id.recycler_view_post_uploads);
        mPostSetting = view.findViewById(R.id.image_post_item_setting);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null && !getArguments().isEmpty()){
            mCurrentPost = getArguments().getParcelable(HomePageFragment.ARG_POST);
            setPostPage();
        }

        initCommentsRecyclerView();
        loadCommentsFromDatabase();
        loadNewComments();

        mPostSetting.setOnClickListener(onClickListener);

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.userImageClickListener(mCurrentPost);
            }
        });

        mPublishCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postText = mCommentText.getText().toString();
                mCommentText.setText("");
                if (!postText.isEmpty()){
                    final Comment newComment = new Comment();
                    DateFormat simple = new SimpleDateFormat("dd MMM HH:mm");
                    Date date = new Date(System.currentTimeMillis());
                    newComment.setCreationTime(simple.format(date));
                    newComment.setCommentText(postText);
                    newComment.setCreatorId(CurrentUser.getCurrentUser().getPrimaryKey());
                    newComment.setUserCreatorNickName(CurrentUser.getCurrentUser().getNickName());
                    newComment.setUserProfileImageUrl(CurrentUser.getCurrentUser().getUserInfo().getImageUri());
                    FirebaseRepository.createComment(newComment, new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            FirebaseRepository.setCommentInnerPrimaryKey(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Iterator<DataSnapshot> lastChild = dataSnapshot.getChildren().iterator();
                                    newComment.setPrimaryKey(lastChild.next().getKey());
                                    FirebaseRepository.setCommentInnerPrimaryKeyToFirebase(newComment.getPrimaryKey());
                                    mCurrentPost.addCommentId(newComment.getPrimaryKey());
                                    FirebaseRepository.setCommentInnerPrimaryKeyToFirebasePost(mCurrentPost);
                                    mCommentAdapter.addComment(newComment);
                                   mPostCommentsRecyclerView.scrollToPosition(mCommentAdapter.getItemCount() - 1);
  //                                  int count  = Integer.getInteger(mCommentCount.getText().toString());
//                                    mCommentCount.setText(++count);
                                    //   mCommentAdapter.notifyDataSetChanged();
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
        });
        mSetToolBarAndNavigationBarState.setTitle(R.string.title_activity_opened_post);
    }

    private void initCommentsRecyclerView(){
        mCommentAdapter = new CommentRecyclerViewAdapter();
        mCommentAdapter.setOnCommentItemSelectedListener(mOnCommentItemSelectedListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
     //   linearLayoutManager.setStackFromEnd(true);
        mPostCommentsRecyclerView.setLayoutManager(linearLayoutManager);
        mPostCommentsRecyclerView.setAdapter(mCommentAdapter);
        mCommentAdapter.setData(mComments);

    }

    private void loadCommentsFromDatabase(){

        List<String> commentsId = mCurrentPost.getCommentsId();
        final List<Comment> list = new ArrayList<>();
        for(String id : commentsId){
            final String temp = id;

            FirebaseDatabase.getInstance().getReference().child("comments").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    list.add(comment);
                    if(comment != null){
                        mCommentAdapter.addComment(comment);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void setCommentsToAdapter(List<Comment> com){
        if(!com.isEmpty()){
            mComments.clear();
            mComments.addAll(com);
            mCommentAdapter.setData(mComments);
        }

    }


    private void setPostPage(){
        mFullName.setText(mCurrentPost.getUserName());
        GlideUtil.setImageGlide(mCurrentPost.getProfileImage(), mProfileImage);
        mPublishedTime.setText(mCurrentPost.getPublishedTime());
        mPostText.setText(mCurrentPost.getPostText());
        initRecyclerView();
     //   mCommentCount.setText(String.valueOf(mCurrentPost.getCommentsQuantity()));
    }

   private void loadNewComments(){
        FirebaseRepository.getNewComments(mCurrentPost.getPrimaryKey(), new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("loadnewcom", dataSnapshot.toString());
                FirebaseRepository.getCommentById(dataSnapshot.getValue(String.class), new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mCommentAdapter.addComment(dataSnapshot.getValue(Comment.class));
                        if(mCommentAdapter.getItemCount() > 0){
                           mRecyclerView.scrollToPosition(mCommentAdapter.getItemCount() - 1);}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
   }

    public PostDetailsFragment() {
        mComments = new ArrayList<>();
    }

    private void initUploadsAdapter(){
        mAdapter = UploadsAdapterFactory.setAdapterTypeByInputType(mCurrentPost.getType());
        List<BaseUpload> uploads = new ArrayList<>();
        if(mCurrentPost.getType() == PostUploadType.NONE)
            return;
        for(String url: mCurrentPost.getAttachment().getFilesUrls()){
            uploads.add(UploadTypeFactory.setUploadByType(mCurrentPost.getType(), url));
        }
        if(mCurrentPost.getType() == PostUploadType.MUSIC){
            mAdapter.setOnItemSelectedListener(mInnerMusicItemOnClickListener);
            mAdapter.setOnSeekBarListner(mMusicSeekBarListener);
        }else if(mCurrentPost.getType() == PostUploadType.IMAGE){
            mAdapter.setOnItemSelectedListener(mInnerImageItemOnClickListener);
        }
        //TODO in else if statement set listeners for image and video types (fullscreen feature)
         mAdapter.setUploads(uploads);
    }

    private void initRecyclerView(){
        setRecyclerViewLayoutManager(mCurrentPost, getContext());
        //mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), mCurrentPost.getType() == PostUploadType.IMAGE ? 2 : 1));
        initUploadsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        checkMediaPlayerState();
    }
    private void setRecyclerViewLayoutManager(Post post, Context context){
        if (post.getType() == PostUploadType.IMAGE){
            if (post.getAttachment().getFilesUrls().size() > 2){
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                return;
            }
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 1));
    }

    private void checkMediaPlayerState(){
        if(mCurrentPost.getType() == PostUploadType.NONE)
            return;
        for(String url: mCurrentPost.getAttachment().getFilesUrls()){
            if (mPlayerServiceConnection.isPlayerPlaying(getSongUri(url))){
                mPlayerServiceConnection.play(getSongUri(url));
                return;
            }

        }
    }

}
