package com.agrigrow.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.model.ForumPost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Adapter for displaying a list of forum posts in a RecyclerView.
 */
public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.PostViewHolder> {

    private final List<ForumPost> posts;
    private final Context context;
    private final OnPostClickListener listener;

    // Interface for click events
    public interface OnPostClickListener {
        void onPostClick(ForumPost post, int position);
        void onLikeClick(ForumPost post, int position);
        void onCommentClick(ForumPost post, int position);
    }

    // Constructor
    public ForumPostAdapter(Context context, List<ForumPost> posts, OnPostClickListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forum_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ForumPost post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }

    // Update posts data
    public void updatePosts(List<ForumPost> newPosts) {
        this.posts.clear();
        this.posts.addAll(newPosts);
        notifyDataSetChanged();
    }

    // ViewHolder class
    class PostViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewAuthor;
        private final TextView textViewAuthorName;
        private final TextView textViewTimestamp;
        private final TextView textViewTitle;
        private final TextView textViewContent;
        private final ImageView imageViewPostImage;
        private final TextView textViewLikes;
        private final TextView textViewComments;
        private final ImageView imageViewLike;
        private final ImageView imageViewComment;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAuthor = itemView.findViewById(R.id.imageViewAuthor);
            textViewAuthorName = itemView.findViewById(R.id.textViewAuthorName);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            textViewTitle = itemView.findViewById(R.id.textViewPostTitle);
            textViewContent = itemView.findViewById(R.id.textViewPostContent);
            imageViewPostImage = itemView.findViewById(R.id.imageViewPostImage);
            textViewLikes = itemView.findViewById(R.id.textViewLikes);
            textViewComments = itemView.findViewById(R.id.textViewComments);
            imageViewLike = itemView.findViewById(R.id.imageViewLike);
            imageViewComment = itemView.findViewById(R.id.imageViewComment);
        }

        public void bind(final ForumPost post) {
            textViewAuthorName.setText(post.getAuthorName());
            textViewTitle.setText(post.getTitle());
            textViewContent.setText(post.getContent());
            textViewLikes.setText(String.valueOf(post.getLikeCount()));
            textViewComments.setText(String.valueOf(post.getCommentCount()));

            // Format relative time
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    post.getTimestamp().getTime(),
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS);
            textViewTimestamp.setText(timeAgo);

            // Load author image with Glide
            if (post.getAuthorImageUrl() != null && !post.getAuthorImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(post.getAuthorImageUrl())
                        .apply(new RequestOptions()
                                .circleCrop()
                                .placeholder(R.drawable.placeholder_user)
                                .error(R.drawable.placeholder_user))
                        .into(imageViewAuthor);
            } else {
                imageViewAuthor.setImageResource(R.drawable.placeholder_user);
            }

            // Load post image with Glide if available
            if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                imageViewPostImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(post.getImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.placeholder_image)
                                .error(R.drawable.placeholder_image))
                        .into(imageViewPostImage);
            } else {
                imageViewPostImage.setVisibility(View.GONE);
            }

            // Set like icon state
            imageViewLike.setImageResource(
                    post.isLikedByCurrentUser() ? R.drawable.ic_like_filled : R.drawable.ic_like_outline);

            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onPostClick(post, position);
                    }
                }
            });

            imageViewLike.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        boolean wasLiked = post.isLikedByCurrentUser();
                        post.setLikedByCurrentUser(!wasLiked);
                        imageViewLike.setImageResource(
                                post.isLikedByCurrentUser() ? R.drawable.ic_like_filled : R.drawable.ic_like_outline);
                        
                        // Update like count
                        int newLikeCount = post.getLikeCount() + (wasLiked ? -1 : 1);
                        post.setLikeCount(newLikeCount);
                        textViewLikes.setText(String.valueOf(newLikeCount));
                        
                        listener.onLikeClick(post, position);
                    }
                }
            });

            imageViewComment.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCommentClick(post, position);
                    }
                }
            });
        }
    }
}
