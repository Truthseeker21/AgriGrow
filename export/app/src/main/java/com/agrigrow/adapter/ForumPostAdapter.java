package com.agrigrow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.model.ForumPost;
import com.agrigrow.model.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying forum posts in a RecyclerView.
 * This adapter handles the display of forum posts and user interactions.
 */
public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.PostViewHolder> {
    
    private final Context context;
    private List<ForumPost> posts;
    private final OnPostClickListener listener;
    private List<User> users; // For display of user info
    
    /**
     * Interface for handling post item click events
     */
    public interface OnPostClickListener {
        void onPostClick(ForumPost post);
        void onUserClick(User user);
        void onLikeClick(ForumPost post, boolean isLiked);
        void onCommentClick(ForumPost post);
        void onShareClick(ForumPost post);
    }
    
    /**
     * Constructor initializes the adapter with a context and click listener
     * @param context The application context
     * @param listener Listener for post item interactions
     */
    public ForumPostAdapter(Context context, OnPostClickListener listener) {
        this.context = context;
        this.posts = new ArrayList<>();
        this.users = new ArrayList<>();
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_post, parent, false);
        return new PostViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ForumPost post = posts.get(position);
        User postUser = findUserById(post.getUserId());
        holder.bind(post, postUser, listener);
    }
    
    @Override
    public int getItemCount() {
        return posts.size();
    }
    
    /**
     * Find user by ID from the loaded users list
     * @param userId The ID of the user to find
     * @return The user with the given ID, or null if not found
     */
    private User findUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        // Return a default user if not found
        User defaultUser = new User("Unknown User", "unknown@email.com");
        defaultUser.setId(userId);
        return defaultUser;
    }
    
    /**
     * Updates the adapter with new posts and users
     * @param posts New list of posts to display
     * @param users List of users for associating with posts
     */
    public void setPosts(List<ForumPost> posts, List<User> users) {
        this.posts = posts;
        this.users = users;
        notifyDataSetChanged();
    }
    
    /**
     * Updates only the posts, keeping existing users
     * @param posts New list of posts to display
     */
    public void setPosts(List<ForumPost> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }
    
    /**
     * Add a single post to the adapter's list
     * @param post Post to add
     */
    public void addPost(ForumPost post) {
        posts.add(0, post); // Add to the beginning of the list
        notifyItemInserted(0);
    }
    
    /**
     * Remove a post from the adapter's list
     * @param post Post to remove
     */
    public void removePost(ForumPost post) {
        int position = posts.indexOf(post);
        if (position >= 0) {
            posts.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    /**
     * Update a post in the adapter's list
     * @param updatedPost Updated post
     */
    public void updatePost(ForumPost updatedPost) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == updatedPost.getId()) {
                posts.set(i, updatedPost);
                notifyItemChanged(i);
                break;
            }
        }
    }
    
    /**
     * Filter posts by category or search query
     * @param query Search query
     * @param category Post category to filter by, or empty string for all
     * @param originalList Original list of posts before filtering
     */
    public void filterPosts(String query, String category, List<ForumPost> originalList) {
        query = query.toLowerCase().trim();
        
        List<ForumPost> filteredList = new ArrayList<>();
        
        for (ForumPost post : originalList) {
            // Apply category filter if specified
            if (!category.isEmpty() && !post.getCategory().equals(category)) {
                continue;
            }
            
            // Apply search filter
            if (!query.isEmpty() && !post.getTitle().toLowerCase().contains(query) && 
                !post.getContent().toLowerCase().contains(query)) {
                continue;
            }
            
            // Post passed all filters, add it to the filtered list
            filteredList.add(post);
        }
        
        setPosts(filteredList);
    }
    
    /**
     * ViewHolder class for forum post items
     */
    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewUserProfile;
        private final TextView textViewUserName;
        private final TextView textViewPostTime;
        private final TextView textViewPostCategory;
        private final TextView textViewPostTitle;
        private final TextView textViewPostContent;
        private final ImageView imageViewPostImage;
        private final LinearLayout layoutLike;
        private final LinearLayout layoutComment;
        private final LinearLayout layoutShare;
        private final ImageView imageViewLike;
        private final TextView textViewLikeCount;
        private final TextView textViewCommentCount;
        private boolean isLiked = false;
        
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            
            imageViewUserProfile = itemView.findViewById(R.id.imageViewUserProfile);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewPostTime = itemView.findViewById(R.id.textViewPostTime);
            textViewPostCategory = itemView.findViewById(R.id.textViewPostCategory);
            textViewPostTitle = itemView.findViewById(R.id.textViewPostTitle);
            textViewPostContent = itemView.findViewById(R.id.textViewPostContent);
            imageViewPostImage = itemView.findViewById(R.id.imageViewPostImage);
            layoutLike = itemView.findViewById(R.id.layoutLike);
            layoutComment = itemView.findViewById(R.id.layoutComment);
            layoutShare = itemView.findViewById(R.id.layoutShare);
            imageViewLike = itemView.findViewById(R.id.imageViewLike);
            textViewLikeCount = itemView.findViewById(R.id.textViewLikeCount);
            textViewCommentCount = itemView.findViewById(R.id.textViewCommentCount);
        }
        
        /**
         * Bind post data to the ViewHolder
         * @param post Forum post to display
         * @param user User who created the post
         * @param listener Click listener for interactions
         */
        public void bind(final ForumPost post, final User user, final OnPostClickListener listener) {
            // Set post details
            textViewPostTitle.setText(post.getTitle());
            textViewPostContent.setText(post.getContent());
            textViewPostTime.setText(post.getRelativeTimeString());
            textViewPostCategory.setText(post.getCategory());
            textViewLikeCount.setText(String.valueOf(post.getLikeCount()));
            textViewCommentCount.setText(String.valueOf(post.getCommentCount()));
            
            // Set user details
            textViewUserName.setText(user.getName());
            
            // Load user profile image
            if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(user.getProfileImageUrl())
                        .placeholder(R.drawable.ic_person) // Replace with your placeholder
                        .error(R.drawable.ic_person) // Replace with your error image
                        .circleCrop()
                        .into(imageViewUserProfile);
            } else {
                // Use a default profile image
                imageViewUserProfile.setImageResource(R.drawable.ic_person); // Replace with default profile image
            }
            
            // Handle post image if present
            if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                imageViewPostImage.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(post.getImageUrl())
                        .centerCrop()
                        .into(imageViewPostImage);
            } else {
                imageViewPostImage.setVisibility(View.GONE);
            }
            
            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPostClick(post);
                }
            });
            
            imageViewUserProfile.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUserClick(user);
                }
            });
            
            textViewUserName.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUserClick(user);
                }
            });
            
            layoutLike.setOnClickListener(v -> {
                if (listener != null) {
                    isLiked = !isLiked;
                    // In a real app, we'd check if the user has already liked the post
                    updateLikeUI(isLiked);
                    listener.onLikeClick(post, isLiked);
                }
            });
            
            layoutComment.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCommentClick(post);
                }
            });
            
            layoutShare.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShareClick(post);
                }
            });
        }
        
        /**
         * Update the like button UI based on like status
         * @param isLiked Whether the post is liked
         */
        private void updateLikeUI(boolean isLiked) {
            if (isLiked) {
                imageViewLike.setImageResource(R.drawable.ic_favorite); // Replace with filled heart
                // Update like count
                int currentLikes = Integer.parseInt(textViewLikeCount.getText().toString());
                textViewLikeCount.setText(String.valueOf(currentLikes + 1));
            } else {
                imageViewLike.setImageResource(R.drawable.ic_favorite_border); // Replace with outline heart
                // Update like count
                int currentLikes = Integer.parseInt(textViewLikeCount.getText().toString());
                if (currentLikes > 0) {
                    textViewLikeCount.setText(String.valueOf(currentLikes - 1));
                }
            }
        }
    }
}