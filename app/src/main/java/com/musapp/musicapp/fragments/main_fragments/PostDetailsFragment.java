package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

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
import com.musapp.musicapp.fragments.main_fragments.toolbar.SetToolBarTitle;
import com.musapp.musicapp.model.Comment;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;

import com.musapp.musicapp.pattern.UploadTypeFactory;
import com.musapp.musicapp.pattern.UploadsAdapterFactory;
import com.musapp.musicapp.uploads.BaseUpload;
import com.musapp.musicapp.utils.FragmentShowUtils;
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
    private Button mPublishCommentButton;
//    private TextView mCommentCount;
    private RecyclerView mPostAttachmentsRecyclerView;
    private RecyclerView mPostCommentsRecyclerView;
    private CommentRecyclerViewAdapter mCommentAdapter;
    private Post mCurrentPost;
    private SetToolBarTitle mSetToolBarTitle;
    private RecyclerView mRecyclerView;
    private BaseUploadsAdapter<BaseUpload, BasePostViewHolder> mAdapter;

    public void setSetToolBarTitle(SetToolBarTitle setToolBarTitle) {
        mSetToolBarTitle = setToolBarTitle;
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
//        mSetToolBarTitle.setTitle(R.string.title_activity_opened_post);
    }

    private void initCommentsRecyclerView(){
        mCommentAdapter = new CommentRecyclerViewAdapter();
        mCommentAdapter.setOnCommentItemSelectedListener(mOnCommentItemSelectedListener);
        mPostCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPostCommentsRecyclerView.setAdapter(mCommentAdapter);
        mCommentAdapter.setData(mComments);

    }

    private void loadCommentsFromDatabase(){

        List<String> commentsId = mCurrentPost.getCommentsId();
        final List<Comment> list = new ArrayList<>();
        for(String id : commentsId){
            final String temp = id;

            FirebaseDatabase.getInstance().getReference().child("comments").child(id).addValueEventListener(new ValueEventListener() {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        }
        //TODO in else if statement set listeners for image and video types (fullscreen feature)
         mAdapter.setUploads(uploads);
    }

    private void initRecyclerView(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), mCurrentPost.getType() == PostUploadType.IMAGE ? 2 : 1));
        initUploadsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        checkMediaPlayerState();
    }

    private void checkMediaPlayerState(){
        for(String url: mCurrentPost.getAttachment().getFilesUrls()){
            if (mPlayerServiceConnection.isPlayerPlaying(getSongUri(url))){
                mPlayerServiceConnection.play(getSongUri(url));
                return;
            }

        }
    }

}
