package com.hkucs.woods.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hkucs.woods.Comment;
import com.hkucs.woods.MessageActivity;
import com.hkucs.woods.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    private List<Comment> commentList;
    Context context;

    public CommentsAdapter(List<Comment> commentList, Context context) {
        this.context = context;
        this.commentList = commentList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public CircleImageView avatar;
        public TextView commentContent;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.textView_comment_username);
            avatar = (CircleImageView) itemView.findViewById(R.id.circleImageView_comment_avatar);
            commentContent = (TextView) itemView.findViewById(R.id.textView_comment_content);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Comment comment = commentList.get(position);
        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", comment.getAuthor_uid());
                context.startActivity(intent);
            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", comment.getAuthor_uid());
                context.startActivity(intent);
            }
        });
        holder.username.setText(comment.getAuthor_username());
        holder.commentContent.setText(comment.getContent());
        Picasso.get().load(comment.getAvatar_url()).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        if(commentList == null)
            return 0;
        return commentList.size();
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void addItem(Comment item) {
        this.commentList.add(item);
    }
}
