package com.hkucs.woods.ui;

import android.content.Context;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hkucs.woods.Comment;
import com.hkucs.woods.LoadActivity;
import com.hkucs.woods.MessageActivity;
import com.hkucs.woods.Post;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hkucs.woods.PostActivity;
import com.hkucs.woods.R;
import com.hkucs.woods.User;
import com.squareup.picasso.Picasso;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

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
        public Button editPost;
        public EditText commentContent;
        public CircleImageView avatar;
        public Group commentGroup;
        public RecyclerView commentRecyclerView;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.textView_username);
            avatar = (CircleImageView) itemView.findViewById(R.id.circleImageView_avatar);
            event = (TextView) itemView.findViewById(R.id.textView_event);
            thought = (TextView) itemView.findViewById(R.id.textView_thought);
            action = (TextView) itemView.findViewById(R.id.textView_action);
            comment = (Button) itemView.findViewById(R.id.button_comment);
            editPost = (Button) itemView.findViewById(R.id.button_edit);
            commentSend = (Button) itemView.findViewById(R.id.button_send);
            commentContent = (EditText) itemView.findViewById(R.id.editText_comment);
            commentRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView_comment);
            commentGroup = (Group) itemView.findViewById(R.id.group_comment);
            commentGroup.setVisibility(View.GONE);
        }

        private void resetComment(boolean enabled) {
            commentSend.setEnabled(enabled);
            commentContent.setEnabled(enabled);
            commentContent.setText(null);
        }

        private void fetchComment(String pid, ChildEventListener listener){
            Query ref = mDatabase.child("post-comments").child(pid);
            ref.addChildEventListener(listener);
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
        final Post post = postList.get(position);
        final List<Comment> commentList = new ArrayList<>();
        holder.username.setText(post.getUsername());
        holder.event.setText(post.getEvent());
        holder.thought.setText(post.getThought());
        holder.action.setText(post.getAction());
        Picasso.get().load(post.getAvatar_url()).into(holder.avatar);
        if(post.getUid().matches(uid)){
            holder.editPost.setVisibility(View.VISIBLE);
        }else{
            holder.editPost.setVisibility(View.GONE);
        }
        holder.editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra("post", (Serializable) post.toMap());
                context.startActivity(intent);
            }
        });
        /*holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", post.getUid());
                context.startActivity(intent);
            }
        });*/
        /*holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", post.getUid());
                context.startActivity(intent);
            }
        });*/

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
        final LinearLayoutManager commentLayoutManager = new LinearLayoutManager(holder.commentRecyclerView.getContext());
        commentLayoutManager.setInitialPrefetchItemCount(5);
        final CommentsAdapter commentsAdapter = new CommentsAdapter(commentList, context);
        holder.commentRecyclerView.setLayoutManager(commentLayoutManager);
        holder.commentRecyclerView.setAdapter(commentsAdapter);
        holder.commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = holder.commentContent.getText().toString();
                if(commentContent.matches(""))
                    return;
                else {
                    holder.resetComment(false);
                    sendComment(post.getPid(), commentContent);
                    holder.resetComment(true);
                }
            }
        });
        holder.fetchComment(post.getPid(), new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                commentsAdapter.addItem(comment);
                commentsAdapter.notifyDataSetChanged();
                commentLayoutManager.scrollToPosition(commentsAdapter.getItemCount()-1);
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
        String lastItemDate;
        try {
            lastItemDate = postList.get(postList.size()-1).getRemindDate();
        } catch (Exception e) {
            return "";
        }
        return postList.get(postList.size()-1).getRemindDate();
    }

    public void sendComment(final String pid, final String content){
        final String key = mDatabase.child("comments").push().getKey();
        final User[] current = new User[1];
        Log.d("COMMENT", uid);
        final String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current[0] = dataSnapshot.getValue(User.class);
                Comment comment = new Comment(key, uid, current[0].getUsername(), current[0].getAvatarImageUrl(), content, timeStamp);
                Map<String, Object> commentValues = comment.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/post-comments/" + pid + "/" + key, commentValues);
                mDatabase.updateChildren(childUpdates);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}