package com.hkucs.woods.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hkucs.woods.Comment;
import com.hkucs.woods.Post;
import com.hkucs.woods.R;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    private List<Comment> commentList;

    public CommentsAdapter(List<Comment> commentList) {
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
        Comment comment = commentList.get(position);
        holder.username.setText(comment.getAuthor_username());
        holder.commentContent.setText(comment.getContent());
//        holder.avatar.setImageBitmap();
    }

    @Override
    public int getItemCount() {
        if(commentList == null)
            return 0;
        return commentList.size();
    }

}