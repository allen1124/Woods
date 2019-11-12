package com.hkucs.woods.ui;

import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hkucs.woods.Comment;
import com.hkucs.woods.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.hkucs.woods.R;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private List<Post> postList;
    Context context;
    private String uid;

    public PostsAdapter(Context context) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.uid = mAuth.getUid();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView event;
        public TextView thought;
        public TextView action;
        public Button comment;
        public Button commentSend;
        public EditText commentContent;
        public Group commentGroup;
        public RecyclerView commentRecyclerView;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.textView_username);
            event = (TextView) itemView.findViewById(R.id.textView_event);
            thought = (TextView) itemView.findViewById(R.id.textView_thought);
            action = (TextView) itemView.findViewById(R.id.textView_action);
            comment = (Button) itemView.findViewById(R.id.button_comment);
            commentSend = (Button) itemView.findViewById(R.id.button_send);
            commentContent = (EditText) itemView.findViewById(R.id.editText_comment);
            commentRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView_comment);
            commentGroup = (Group) itemView.findViewById(R.id.group_comment);
            commentGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_post, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Post post = postList.get(position);
        List<Comment> commentList = new ArrayList<Comment>();
        commentList.add(new Comment("Joker", "HAHAHA", new Date()));
        holder.username.setText(post.getUsername());
        holder.event.setText(post.getEvent());
        holder.thought.setText(post.getThought());
        holder.action.setText(post.getAction());
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.commentGroup.getVisibility() == View.GONE) {
                    holder.commentGroup.setVisibility(View.VISIBLE);
                }else {
                    holder.commentGroup.setVisibility(View.GONE);
                }
            }
        });
        LinearLayoutManager commentLayoutManager = new LinearLayoutManager(holder.commentRecyclerView.getContext());
        commentLayoutManager.setInitialPrefetchItemCount(5);
        holder.commentRecyclerView.setLayoutManager(commentLayoutManager);
        holder.commentRecyclerView.setAdapter(new CommentsAdapter(commentList));
        holder.commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add comment to post
            }
        });
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public int getItemCount() {
        if(postList == null)
            return 0;
        return postList.size();
    }

    public String getLastItemDate(){
        return postList.get(postList.size()-1).getRemindDate();
    }
}