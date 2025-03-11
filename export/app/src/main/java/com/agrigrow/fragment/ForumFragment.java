package com.agrigrow.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.agrigrow.R;
import com.agrigrow.adapter.ForumPostAdapter;
import com.agrigrow.model.ForumPost;
import com.agrigrow.model.User;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Fragment for the forum/community section of the application.
 * This fragment allows users to view, create, and interact with forum posts.
 */
public class ForumFragment extends Fragment implements ForumPostAdapter.OnPostClickListener {

    private RecyclerView recyclerViewPosts;
    private ForumPostAdapter postAdapter;
    private ProgressBar progressBar;
    private TextView textViewEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private ChipGroup chipGroupCategories;
    private TabLayout tabLayout;
    private FloatingActionButton fabAddPost;

    private List<ForumPost> allPosts = new ArrayList<>();
    private List<User> allUsers = new ArrayList<>();
    private String currentSearchQuery = "";
    private String currentCategory = "";
    private int currentTabPosition = 0;
    private int currentUserId = 1; // This would be the logged-in user's ID in a real app

    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupRecyclerView();
        setupListeners();
        loadPosts();
    }

    private void initViews(View view) {
        recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);
        progressBar = view.findViewById(R.id.progressBar);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        searchView = view.findViewById(R.id.searchView);
        chipGroupCategories = view.findViewById(R.id.chipGroupCategories);
        tabLayout = view.findViewById(R.id.tabLayout);
        fabAddPost = view.findViewById(R.id.fabAddPost);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewPosts.setLayoutManager(layoutManager);
        
        postAdapter = new ForumPostAdapter(getContext(), this);
        recyclerViewPosts.setAdapter(postAdapter);
    }

    private void setupListeners() {
        // Set up swipe refresh
        swipeRefreshLayout.setOnRefreshListener(this::refreshPosts);
        
        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                filterPosts();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                filterPosts();
                return true;
            }
        });
        
        // Set up category filter
        chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipAll) {
                currentCategory = "";
            } else if (checkedId == R.id.chipQuestions) {
                currentCategory = "Questions";
            } else if (checkedId == R.id.chipTips) {
                currentCategory = "Tips";
            } else if (checkedId == R.id.chipShowcase) {
                currentCategory = "Showcase";
            } else if (checkedId == R.id.chipNews) {
                currentCategory = "News";
            }
            filterPosts();
        });
        
        // Set up tab selection (Recent, Popular, My Posts)
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
                sortPosts();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed
            }
        });
        
        // Set up add post button
        fabAddPost.setOnClickListener(v -> showAddPostDialog());
    }

    private void loadPosts() {
        showLoading(true);
        
        // In a real app, this would fetch from a database or API
        executor.execute(() -> {
            try {
                // Simulate network delay
                Thread.sleep(1000);
                
                // Generate sample data for UI testing
                List<ForumPost> posts = generateSamplePosts();
                List<User> users = generateSampleUsers();
                
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        allPosts = posts;
                        allUsers = users;
                        sortPosts();
                        showLoading(false);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        showError("Failed to load posts");
                    });
                }
            }
        });
    }
    
    private void refreshPosts() {
        loadPosts();
    }
    
    private void sortPosts() {
        // Create a copy of the posts to sort
        List<ForumPost> sortedPosts = new ArrayList<>(allPosts);
        
        // Apply sorting based on the selected tab
        if (currentTabPosition == 0) {
            // Recent tab - sort by creation time (newest first)
            Collections.sort(sortedPosts, (p1, p2) -> Long.compare(p2.getCreatedAt(), p1.getCreatedAt()));
        } else if (currentTabPosition == 1) {
            // Popular tab - sort by number of likes and comments
            Collections.sort(sortedPosts, (p1, p2) -> {
                int p1Score = p1.getLikeCount() + p1.getCommentCount();
                int p2Score = p2.getLikeCount() + p2.getCommentCount();
                return Integer.compare(p2Score, p1Score);
            });
        } else if (currentTabPosition == 2) {
            // My Posts tab - filter to show only the user's posts
            sortedPosts = new ArrayList<>();
            for (ForumPost post : allPosts) {
                if (post.getUserId() == currentUserId) {
                    sortedPosts.add(post);
                }
            }
            // Sort by creation time (newest first)
            Collections.sort(sortedPosts, (p1, p2) -> Long.compare(p2.getCreatedAt(), p1.getCreatedAt()));
        }
        
        // Apply category and search filters to the sorted posts
        postAdapter.filterPosts(currentSearchQuery, currentCategory, sortedPosts);
        
        // Show empty state if no posts match the filters
        if (sortedPosts.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
        }
        
        // Stop refresh animation if it's running
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    
    private void filterPosts() {
        // Create a copy of allPosts to work with
        List<ForumPost> filteredPosts = new ArrayList<>(allPosts);
        
        // Apply tab-based filtering
        if (currentTabPosition == 2) { // My Posts tab
            List<ForumPost> userPosts = new ArrayList<>();
            for (ForumPost post : filteredPosts) {
                if (post.getUserId() == currentUserId) {
                    userPosts.add(post);
                }
            }
            filteredPosts = userPosts;
        }
        
        // Apply additional filters (category and search)
        postAdapter.filterPosts(currentSearchQuery, currentCategory, filteredPosts);
        
        // Show empty state if no posts match the filters
        if (filteredPosts.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
        }
    }
    
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
    
    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        textViewEmpty.setText("Error: " + message);
        textViewEmpty.setVisibility(View.VISIBLE);
    }
    
    private void showAddPostDialog() {
        if (getContext() == null) return;
        
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_post);
        dialog.setCancelable(true);
        
        // Get views from dialog
        EditText editTextTitle = dialog.findViewById(R.id.editTextPostTitle);
        EditText editTextContent = dialog.findViewById(R.id.editTextPostContent);
        Spinner spinnerCategory = dialog.findViewById(R.id.spinnerPostCategory);
        Button buttonAddImage = dialog.findViewById(R.id.buttonAddImage);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        Button buttonPost = dialog.findViewById(R.id.buttonPost);
        LinearLayout layoutImagePreview = dialog.findViewById(R.id.layoutImagePreview);
        ImageView imageViewPreview = dialog.findViewById(R.id.imageViewPostPreview);
        Button buttonRemoveImage = dialog.findViewById(R.id.buttonRemoveImage);
        
        // Set up category dropdown
        String[] categories = {"Questions", "Tips", "Showcase", "News"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        
        // Set up button click listeners
        buttonCancel.setOnClickListener(v -> dialog.dismiss());
        
        buttonPost.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String content = editTextContent.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            
            if (title.isEmpty()) {
                editTextTitle.setError("Please enter a title");
                return;
            }
            
            if (content.isEmpty()) {
                editTextContent.setError("Please enter content");
                return;
            }
            
            // Create a new post (in a real app, this would be saved to a database)
            ForumPost newPost = new ForumPost(title, content, currentUserId);
            newPost.setCategory(category);
            newPost.setCreatedAt(System.currentTimeMillis());
            newPost.setUpdatedAt(System.currentTimeMillis());
            
            // Add post to adapter (in a real app, this would happen after server confirmation)
            postAdapter.addPost(newPost);
            allPosts.add(0, newPost);
            
            // Show success message and dismiss dialog
            Toast.makeText(getContext(), "Post created successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        
        // Show dialog
        dialog.show();
    }
    
    // ForumPostAdapter.OnPostClickListener implementation
    
    @Override
    public void onPostClick(ForumPost post) {
        // In a real app, this would navigate to a post detail screen
        Toast.makeText(getContext(), "Viewing post: " + post.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserClick(User user) {
        // In a real app, this would navigate to a user profile screen
        Toast.makeText(getContext(), "Viewing profile: " + user.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLikeClick(ForumPost post, boolean isLiked) {
        // Update like count in the post model
        if (isLiked) {
            post.incrementLikeCount();
        } else {
            post.decrementLikeCount();
        }
        
        // In a real app, this would send the like/unlike request to the server
        String message = isLiked ? "Liked post" : "Unliked post";
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentClick(ForumPost post) {
        // In a real app, this would open the comments section
        Toast.makeText(getContext(), "Viewing comments for: " + post.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShareClick(ForumPost post) {
        // Share the post using Android's native share functionality
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
        
        String shareText = post.getTitle() + "\n\n" + post.getContent() + 
                "\n\nShared from AgriGrow Urban Gardening App";
        
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    
    // Sample data generation for UI testing
    
    private List<ForumPost> generateSamplePosts() {
        List<ForumPost> posts = new ArrayList<>();
        
        // Sample post 1
        ForumPost post1 = new ForumPost(
                "Best practices for growing tomatoes in small urban spaces",
                "I've been experimenting with different methods for growing tomatoes in my small balcony garden, and I wanted to share some tips that have worked really well for me this season. First, choosing the right variety is crucial - cherry tomatoes and determinate varieties like Roma have worked best in my containers. Second, I've found that using a quality potting mix with added compost makes a huge difference in yield. And lastly, consistent watering with a drip system has saved me a lot of headaches. What varieties and methods have worked for you?",
                2); // User ID 2
        post1.setCategory("Tips");
        post1.setCreatedAt(System.currentTimeMillis() - 86400000); // 1 day ago
        post1.setLikeCount(24);
        post1.setCommentCount(8);
        post1.setImageUrl("https://example.com/tomato_plants.jpg");
        posts.add(post1);
        
        // Sample post 2
        ForumPost post2 = new ForumPost(
                "Help! My basil plant leaves are turning yellow",
                "I've been growing basil for about 3 weeks now, and the leaves are starting to turn yellow from the bottom up. I water it every other day and it gets about 6 hours of direct sunlight. It's in a medium-sized pot with drainage holes. Does anyone know what might be causing this and how I can save my plant?",
                3); // User ID 3
        post2.setCategory("Questions");
        post2.setCreatedAt(System.currentTimeMillis() - 43200000); // 12 hours ago
        post2.setLikeCount(5);
        post2.setCommentCount(12);
        posts.add(post2);
        
        // Sample post 3
        ForumPost post3 = new ForumPost(
                "My first successful harvest of the season!",
                "After months of work, I finally harvested my first batch of vegetables from my urban garden! I'm so excited to share the results with you all. I managed to grow kale, radishes, and lettuce in my small apartment balcony using vertical gardening techniques. It's amazing how much you can grow in a small space if you plan carefully!",
                1); // Current user
        post3.setCategory("Showcase");
        post3.setCreatedAt(System.currentTimeMillis() - 259200000); // 3 days ago
        post3.setLikeCount(47);
        post3.setCommentCount(15);
        post3.setImageUrl("https://example.com/harvest.jpg");
        posts.add(post3);
        
        // Sample post 4
        ForumPost post4 = new ForumPost(
                "New urban gardening initiative in local community",
                "I wanted to share that our local community center is starting a new urban gardening initiative next month! They'll be offering free workshops on container gardening, composting, and seed starting. They're also creating a community garden space where residents can rent small plots. I think this is a great opportunity for beginners to learn and connect with other gardeners.",
                4); // User ID 4
        post4.setCategory("News");
        post4.setCreatedAt(System.currentTimeMillis() - 172800000); // 2 days ago
        post4.setLikeCount(32);
        post4.setCommentCount(7);
        posts.add(post4);
        
        // Sample post 5
        ForumPost post5 = new ForumPost(
                "Companion planting guide for small spaces",
                "I've put together a quick guide on companion planting that's worked well in my small urban garden. Tomatoes + Basil: Basil improves tomato flavor and repels pests. Carrots + Onions: Onions deter carrot fly. Lettuce + Radishes: Radishes mark rows and deter pests. Beans + Corn: Beans fix nitrogen that corn needs. Marigolds everywhere: These beautiful flowers deter many common garden pests. Hope this helps some of you maximize your limited space!",
                2); // User ID 2
        post5.setCategory("Tips");
        post5.setCreatedAt(System.currentTimeMillis() - 604800000); // 7 days ago
        post5.setLikeCount(64);
        post5.setCommentCount(21);
        posts.add(post5);
        
        // Sample post 6
        ForumPost post6 = new ForumPost(
                "DIY self-watering container system",
                "Just wanted to share my DIY self-watering container setup that I created for under $20! I used a large plastic container, a smaller pot, some PVC pipe, and a bit of garden fabric. It's been working great for my pepper plants while I'm away at work all day. Let me know if anyone wants a detailed tutorial!",
                1); // Current user
        post6.setCategory("Showcase");
        post6.setCreatedAt(System.currentTimeMillis() - 345600000); // 4 days ago
        post6.setLikeCount(28);
        post6.setCommentCount(14);
        post6.setImageUrl("https://example.com/self_watering.jpg");
        posts.add(post6);
        
        return posts;
    }
    
    private List<User> generateSampleUsers() {
        List<User> users = new ArrayList<>();
        
        // Current user (ID: 1)
        User currentUser = new User("Alex Gardener", "alex@example.com");
        currentUser.setId(1);
        currentUser.setProfileImageUrl("https://example.com/alex.jpg");
        currentUser.setBio("Urban gardening enthusiast in a small apartment");
        currentUser.setLocation("New York City");
        currentUser.setGardeningLevel(4);
        users.add(currentUser);
        
        // Other sample users
        User user2 = new User("Sarah Green", "sarah@example.com");
        user2.setId(2);
        user2.setProfileImageUrl("https://example.com/sarah.jpg");
        user2.setBio("Master gardener specializing in vegetables");
        user2.setLocation("Chicago");
        user2.setGardeningLevel(8);
        users.add(user2);
        
        User user3 = new User("Mike Johnson", "mike@example.com");
        user3.setId(3);
        user3.setProfileImageUrl("https://example.com/mike.jpg");
        user3.setBio("Beginner gardener learning the ropes");
        user3.setLocation("Seattle");
        user3.setGardeningLevel(2);
        users.add(user3);
        
        User user4 = new User("Lisa Wong", "lisa@example.com");
        user4.setId(4);
        user4.setProfileImageUrl("https://example.com/lisa.jpg");
        user4.setBio("Community garden organizer and educator");
        user4.setLocation("Boston");
        user4.setGardeningLevel(7);
        users.add(user4);
        
        return users;
    }
}