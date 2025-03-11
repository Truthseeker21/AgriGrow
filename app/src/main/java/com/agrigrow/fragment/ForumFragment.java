package com.agrigrow.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.agrigrow.R;
import com.agrigrow.adapter.ForumPostAdapter;
import com.agrigrow.database.ForumPostDao;
import com.agrigrow.model.ForumPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Fragment that displays the community forum where users can share posts and comment.
 */
public class ForumFragment extends Fragment implements 
        ForumPostAdapter.OnPostClickListener {

    private RecyclerView recyclerViewPosts;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabAddPost;
    private RadioGroup radioGroupFilter;
    
    private ForumPostAdapter postAdapter;
    private ForumPostDao postDao;
    
    private List<ForumPost> allPosts;
    private String currentFilter = "all"; // Default filter

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        fabAddPost = view.findViewById(R.id.fabAddPost);
        radioGroupFilter = view.findViewById(R.id.radioGroupFilter);
        
        // Initialize DAO
        postDao = new ForumPostDao(getContext());
        
        // Initialize list
        allPosts = new ArrayList<>();
        
        // Set up RecyclerView
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new ForumPostAdapter(getContext(), allPosts, this);
        recyclerViewPosts.setAdapter(postAdapter);
        
        // Set up swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(this::refreshPosts);
        
        // Set up filter listener
        radioGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonAll) {
                currentFilter = "all";
            } else if (checkedId == R.id.radioButtonQuestions) {
                currentFilter = "question";
            } else if (checkedId == R.id.radioButtonTips) {
                currentFilter = "tip";
            } else if (checkedId == R.id.radioButtonShowcase) {
                currentFilter = "showcase";
            }
            loadPosts();
        });
        
        // Set up FAB
        fabAddPost.setOnClickListener(v -> showAddPostDialog());
        
        // Load posts
        loadPosts();
    }

    private void loadPosts() {
        // Load posts based on current filter
        if (currentFilter.equals("all")) {
            allPosts = postDao.getAllPosts();
        } else {
            allPosts = postDao.getPostsByTag(currentFilter);
        }
        
        // Update adapter
        postAdapter.updatePosts(allPosts);
    }

    private void refreshPosts() {
        // Reload posts
        loadPosts();
        
        // Stop the refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }

    private void showAddPostDialog() {
        // Create a dialog for adding a new post
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_post, null);
        builder.setView(dialogView);
        
        // Get dialog views
        EditText editTextTitle = dialogView.findViewById(R.id.editTextPostTitle);
        EditText editTextContent = dialogView.findViewById(R.id.editTextPostContent);
        RadioGroup radioGroupPostType = dialogView.findViewById(R.id.radioGroupPostType);
        Button buttonPost = dialogView.findViewById(R.id.buttonPost);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        
        // Create dialog
        AlertDialog dialog = builder.create();
        
        // Set button listeners
        buttonPost.setOnClickListener(v -> {
            // Get post data
            String title = editTextTitle.getText().toString().trim();
            String content = editTextContent.getText().toString().trim();
            
            // Get post type
            String postType;
            int checkedId = radioGroupPostType.getCheckedRadioButtonId();
            if (checkedId == R.id.radioButtonPostQuestion) {
                postType = "question";
            } else if (checkedId == R.id.radioButtonPostTip) {
                postType = "tip";
            } else if (checkedId == R.id.radioButtonPostShowcase) {
                postType = "showcase";
            } else {
                postType = "general";
            }
            
            // Validate input
            if (title.isEmpty()) {
                Toast.makeText(getContext(), R.string.error_empty_title, Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (content.isEmpty()) {
                Toast.makeText(getContext(), R.string.error_empty_content, Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Create post
            ForumPost newPost = new ForumPost();
            newPost.setTitle(title);
            newPost.setContent(content);
            // Normally we would get the current user ID from auth system
            newPost.setAuthorId("current_user_id");
            newPost.setAuthorName("Current User"); // Would get from user profile
            newPost.setTimestamp(new Date());
            
            // Add tag based on post type
            ArrayList<String> tags = new ArrayList<>();
            tags.add(postType);
            newPost.setTags(tags);
            
            // Save post
            long newPostId = postDao.addPost(newPost);
            
            if (newPostId > 0) {
                // Post added successfully
                Toast.makeText(getContext(), R.string.post_added_successfully, Toast.LENGTH_SHORT).show();
                
                // Refresh posts
                loadPosts();
                
                // Dismiss dialog
                dialog.dismiss();
            } else {
                // Failed to add post
                Toast.makeText(getContext(), R.string.error_adding_post, Toast.LENGTH_SHORT).show();
            }
        });
        
        buttonCancel.setOnClickListener(v -> dialog.dismiss());
        
        // Show dialog
        dialog.show();
    }

    @Override
    public void onPostClick(ForumPost post, int position) {
        // Implementation to open post details or show comments
    }

    @Override
    public void onLikeClick(ForumPost post, int position) {
        // Update post like status in database
        postDao.updatePostLike(post.getId(), post.isLikedByCurrentUser(), post.getLikeCount());
    }

    @Override
    public void onCommentClick(ForumPost post, int position) {
        // Implementation to show comment dialog or screen
    }
}
