package com.capstoneproject.pedromarco.eventapp.eventdetails.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capstoneproject.pedromarco.eventapp.R;
import com.capstoneproject.pedromarco.eventapp.entities.Comment;
import com.capstoneproject.pedromarco.eventapp.lib.GlideImageLoader;
import com.capstoneproject.pedromarco.eventapp.lib.base.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter class for the recyclerview of the comments of the event
 */
public class CommentListRecyclerviewAdapter extends RecyclerView.Adapter<CommentListRecyclerviewAdapter.ViewHolder> {
    private List<Comment> comments;
    private ImageLoader loader;
    Context context;

    public CommentListRecyclerviewAdapter(List<Comment> comments, ImageLoader loader, Context context) {
        this.comments = comments;
        this.loader = loader;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvComment.setText(comment.getUsername() + ": " + comment.getComment());
        Log.d("Loading ", "" + comment.getPictureURL());
        GlideImageLoader.loadWithContextSmall(holder.ivProfilePic, comment.getPictureURL(), context);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<Comment> newComments) {
        comments.clear();
        comments.addAll(newComments);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivProfilePic)
        CircleImageView ivProfilePic;
        @Bind(R.id.tvComment)
        TextView tvComment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
