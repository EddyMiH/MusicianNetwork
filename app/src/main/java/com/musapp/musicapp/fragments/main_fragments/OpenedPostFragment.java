package com.musapp.musicapp.fragments.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.CommentRecyclerViewAdapter;
import com.musapp.musicapp.currentinformation.CurrentUser;

import com.musapp.musicapp.model.Comment;
import com.musapp.musicapp.model.Post;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;

import com.musapp.musicapp.utils.GlideUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.Iterator;
import java.util.List;

public class OpenedPostFragment extends Fragment {

    private ImageView mProfileImage;
    private TextView mFullName;
    private TextView mPublishedTime;
    private TextView mPostText;
    private EditText mCommentText;
    private Button mPublishCommentButton;
    private TextView mCommentCount;
    private RecyclerView mPostAttachmentsRecyclerView;
    private RecyclerView mPostCommentsRecyclerView;
    private CommentRecyclerViewAdapter mCommentAdapter;
    private Post mCurrentPost;

    private User mCurrentUser = CurrentUser.getCurrentUser();
    private String mCurrentUserImage;

    private List<Comment> mComments;

    private CommentRecyclerViewAdapter.OnCommentItemSelectedListener mOnCommentItemSelectedListener =
            new CommentRecyclerViewAdapter.OnCommentItemSelectedListener() {
                @Override
                public void onItemSelected(String id) {
                    //TODO open mCurrentUser page who write comment, by mCurrentUser id
                }
            };

//    private void loadUserProfileImage(){
//        FirebaseDatabase.getInstance().getReference().child("profession_and_bio").child(mCurrentUser.getProfessionAndInfoId()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String url = dataSnapshot.getValue(ProfessionAndInfo.class).getImageUri();
//                mCurrentUserImage = url;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //loadUserProfileImage();

        View view  = inflater.inflate(R.layout.fragment_post_single_opened_post, container, false);
        mProfileImage = view.findViewById(R.id.image_profile_image_post);
        mFullName = view.findViewById(R.id.text_post_item_user_name);
        mPublishedTime = view.findViewById(R.id.text_post_item_published_time);
        mPostText = view.findViewById(R.id.text_post_item_post_text);
        mCommentCount = view.findViewById(R.id.text_post_item_comment_count);
        mCommentText = view.findViewById(R.id.edit_text_write_comment_opened_post);
        mPublishCommentButton = view.findViewById(R.id.action_comment_view_send_comment);
        mPostCommentsRecyclerView = view.findViewById(R.id.recycler_view_comments_in_post);

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
//                    newComment.setCreatorId(mCurrentUser.getPrimaryKey());
//                    newComment.setUserCreatorNickName(mCurrentUser.getNickName());
//                    newComment.setUserProfileImageUrl(mCurrentUserImage);/////
//                    DBAsyncTask.waitResponse("comments",OpenedPostFragment.this, newComment);
//                    DBAsyncTask.waitResponse("posts",OpenedPostFragment.this, mCurrentPost);
                    newComment.setCreatorId(CurrentUser.getCurrentUser().getPrimaryKey());
                    newComment.setUserCreatorNickName(mCurrentPost.getUserName());
                    newComment.setUserProfileImageUrl(mCurrentPost.getProfileImage());
                    String commentId = "";//DBAccess.createChild("comments", newComment);
                    FirebaseRepository.createComment(newComment, new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            FirebaseRepository.setCommentInnerPrimaryKey(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Iterator<DataSnapshot> lastChild = dataSnapshot.getChildren().iterator();
                                    newComment.setPrimaryKey(lastChild.next().getKey());
                                    FirebaseRepository.setCommentInnerPrimaryKeyToFirebase(newComment);
                                    mCurrentPost.addCommentId(newComment.getPrimaryKey());
                                    FirebaseRepository.setCommentInnerPrimaryKeyToFirebasePost(mCurrentPost);
                                    mCommentAdapter.notifyDataSetChanged();
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
                  /*  FirebaseDatabase.getInstance().getReference().child("comments").child(commentId)
                            .child("primaryKey").setValue(commentId);
                    mCurrentPost.addCommentId(commentId);
                    FirebaseDatabase.getInstance().getReference().child("posts").child(mCurrentPost.getPrimaryKey())
                            .setValue(mCurrentPost);
                    mCommentAdapter.notifyDataSetChanged();*/
                }
            }
        });

        return view;
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
//            FirebaseDatabase.getInstance().getReference().child("comments").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                        Comment comment = snapshot.getValue(Comment.class);
//
//                        if(comment.getPrimaryKey() != null){
//                            if(comment.getPrimaryKey().equals(temp)){
//                                //mComments.add(comment);
//                                list.add(comment);
//                                //mCommentAdapter.addComment(comment);
//
//                            }
//                        }
//                    }
//                    setCommentsToAdapter(list);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.d("ERROR!!!", "onCancelled: cannot load comments");
//                }
//            });
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
        mCommentCount.setText(String.valueOf(mCurrentPost.getCommentsQuantity()));
        //Log.d("ERROR!!!", "comment 0 is: " + mCurrentPost.getCommentsId().get(0));



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public OpenedPostFragment() {
        mComments = new ArrayList<>();
    }

//    @Override
//    public void doOnResponse(String str, String type) {
//        if(type.equals("comments")){
//            FirebaseDatabase.getInstance().getReference().child("comments").child(str)
//                    .child("primaryKey").setValue(str);
//            mCurrentPost.addCommentId(str);
//            mCommentCount.setText(String.valueOf(mCurrentPost.getCommentsQuantity()));
//            loadCommentsFromDatabase();
//        }else if(type.equals("posts")){
//            FirebaseDatabase.getInstance().getReference().child("posts").child(mCurrentPost.getPrimaryKey())
//                    .setValue(mCurrentPost);
//        }
//    }
//
//    @Override
//    public void doForResponse(String str, Object obj) {
//        if(!str.equals("posts"))
//            DBAccess.createChild(str, obj);
//    }

}
