package com.agrigrow.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.adapter.VideoTutorialAdapter;
import com.agrigrow.database.AppDatabase;
import com.agrigrow.database.VideoTutorialDao;
import com.agrigrow.model.VideoTutorial;
import com.agrigrow.util.SocialSharingHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Fragment for video tutorials functionality
 */
public class VideoTutorialsFragment extends Fragment implements VideoTutorialAdapter.VideoActionListener {

    private RecyclerView recyclerView;
    private VideoTutorialAdapter adapter;
    private VideoTutorialDao videoTutorialDao;
    private EditText searchEditText;
    private Chip chipAllVideos, chipFavorites, chipWatched, chipBeginner, chipIntermediate, chipAdvanced;
    private TabLayout tabLayoutCategories;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private TextView emptyStateMessageTextView;
    private CardView featuredVideoCard;
    private TextView featuredTitleTextView, featuredDescriptionTextView;
    private ImageView featuredThumbnailImageView;
    private SocialSharingHelper socialSharingHelper;
    private final Executor executor = Executors.newSingleThreadExecutor();
    
    // Featured video reference
    private VideoTutorial featuredVideo;
    
    // Currently selected category
    private String currentCategory = "All";
    
    public VideoTutorialsFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_tutorials, container, false);
        
        initializeViews(view);
        setupTabLayout();
        setupListeners();
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        videoTutorialDao = AppDatabase.getInstance(requireContext()).videoTutorialDao();
        socialSharingHelper = new SocialSharingHelper(requireContext());
        
        setupAdapter();
        loadVideos();
        loadFeaturedVideo();
    }
    
    private void initializeViews(View view) {
        // Main views
        recyclerView = view.findViewById(R.id.recycler_videos);
        searchEditText = view.findViewById(R.id.et_search_videos);
        progressBar = view.findViewById(R.id.progress_loading);
        emptyStateLayout = view.findViewById(R.id.layout_empty_state);
        emptyStateMessageTextView = view.findViewById(R.id.tv_empty_state_message);
        
        // Filter chips
        chipAllVideos = view.findViewById(R.id.chip_all_videos);
        chipFavorites = view.findViewById(R.id.chip_favorites);
        chipWatched = view.findViewById(R.id.chip_watched);
        chipBeginner = view.findViewById(R.id.chip_beginner);
        chipIntermediate = view.findViewById(R.id.chip_intermediate);
        chipAdvanced = view.findViewById(R.id.chip_advanced);
        
        // Category tabs
        tabLayoutCategories = view.findViewById(R.id.tab_layout_categories);
        
        // Featured video card
        featuredVideoCard = view.findViewById(R.id.card_featured_video);
        featuredTitleTextView = view.findViewById(R.id.tv_featured_title);
        featuredDescriptionTextView = view.findViewById(R.id.tv_featured_description);
        featuredThumbnailImageView = view.findViewById(R.id.iv_featured_thumbnail);
        
        // Empty state button
        Button clearFiltersButton = view.findViewById(R.id.btn_clear_filters);
        clearFiltersButton.setOnClickListener(v -> {
            clearAllFilters();
            loadVideos();
        });
    }
    
    private void setupAdapter() {
        adapter = new VideoTutorialAdapter(requireContext(), this);
        recyclerView.setAdapter(adapter);
    }
    
    private void setupTabLayout() {
        tabLayoutCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String category;
                switch (tab.getPosition()) {
                    case 0:
                        category = "All";
                        break;
                    case 1:
                        category = "Planting";
                        break;
                    case 2:
                        category = "Seed Starting";
                        break;
                    case 3:
                        category = "Watering";
                        break;
                    case 4:
                        category = "Pruning";
                        break;
                    case 5:
                        category = "Pests Control";
                        break;
                    case 6:
                        category = "Harvesting";
                        break;
                    default:
                        category = "All";
                        break;
                }
                
                currentCategory = category;
                if (category.equals("All")) {
                    loadVideos();
                } else {
                    loadVideosByCategory(category);
                }
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
    }
    
    private void setupListeners() {
        // Search box listener
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchVideos(s.toString());
            }
        });
        
        // Filter chip listeners
        chipAllVideos.setOnClickListener(v -> {
            clearChipSelections();
            chipAllVideos.setChecked(true);
            if (currentCategory.equals("All")) {
                loadVideos();
            } else {
                loadVideosByCategory(currentCategory);
            }
        });
        
        chipFavorites.setOnClickListener(v -> {
            clearChipSelections();
            chipFavorites.setChecked(true);
            loadFavoriteVideos();
        });
        
        chipWatched.setOnClickListener(v -> {
            clearChipSelections();
            chipWatched.setChecked(true);
            loadWatchedVideos();
        });
        
        chipBeginner.setOnClickListener(v -> {
            clearChipSelections();
            chipBeginner.setChecked(true);
            loadVideosByDifficulty("Beginner");
        });
        
        chipIntermediate.setOnClickListener(v -> {
            clearChipSelections();
            chipIntermediate.setChecked(true);
            loadVideosByDifficulty("Intermediate");
        });
        
        chipAdvanced.setOnClickListener(v -> {
            clearChipSelections();
            chipAdvanced.setChecked(true);
            loadVideosByDifficulty("Advanced");
        });
        
        // Featured video card
        featuredVideoCard.setOnClickListener(v -> {
            if (featuredVideo != null) {
                playVideo(featuredVideo);
            }
        });
    }
    
    private void clearChipSelections() {
        chipAllVideos.setChecked(false);
        chipFavorites.setChecked(false);
        chipWatched.setChecked(false);
        chipBeginner.setChecked(false);
        chipIntermediate.setChecked(false);
        chipAdvanced.setChecked(false);
    }
    
    private void clearAllFilters() {
        clearChipSelections();
        chipAllVideos.setChecked(true);
        tabLayoutCategories.getTabAt(0).select();
        currentCategory = "All";
        searchEditText.setText("");
    }
    
    private void loadVideos() {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        
        videoTutorialDao.getAllVideos().observe(getViewLifecycleOwner(), videos -> {
            updateUI(videos);
        });
    }
    
    private void loadFavoriteVideos() {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        
        videoTutorialDao.getFavoriteVideos().observe(getViewLifecycleOwner(), videos -> {
            updateUI(videos);
            if (videos == null || videos.isEmpty()) {
                emptyStateMessageTextView.setText(R.string.no_favorite_videos);
            }
        });
    }
    
    private void loadWatchedVideos() {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        
        videoTutorialDao.getWatchedVideos().observe(getViewLifecycleOwner(), videos -> {
            updateUI(videos);
            if (videos == null || videos.isEmpty()) {
                emptyStateMessageTextView.setText(R.string.no_watched_videos);
            }
        });
    }
    
    private void loadVideosByCategory(String category) {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        
        videoTutorialDao.getVideosByCategory(category).observe(getViewLifecycleOwner(), videos -> {
            updateUI(videos);
            if (videos == null || videos.isEmpty()) {
                emptyStateMessageTextView.setText(getString(R.string.no_videos_in_category, category));
            }
        });
    }
    
    private void loadVideosByDifficulty(String difficulty) {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        
        videoTutorialDao.getVideosByDifficulty(difficulty).observe(getViewLifecycleOwner(), videos -> {
            updateUI(videos);
            if (videos == null || videos.isEmpty()) {
                emptyStateMessageTextView.setText(getString(R.string.no_videos_with_difficulty, difficulty));
            }
        });
    }
    
    private void searchVideos(String query) {
        if (query.isEmpty()) {
            // If search query is empty, load based on current filter
            if (chipFavorites.isChecked()) {
                loadFavoriteVideos();
            } else if (chipWatched.isChecked()) {
                loadWatchedVideos();
            } else if (chipBeginner.isChecked()) {
                loadVideosByDifficulty("Beginner");
            } else if (chipIntermediate.isChecked()) {
                loadVideosByDifficulty("Intermediate");
            } else if (chipAdvanced.isChecked()) {
                loadVideosByDifficulty("Advanced");
            } else if (!currentCategory.equals("All")) {
                loadVideosByCategory(currentCategory);
            } else {
                loadVideos();
            }
        } else {
            progressBar.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
            
            videoTutorialDao.searchVideos(query).observe(getViewLifecycleOwner(), videos -> {
                updateUI(videos);
                if (videos == null || videos.isEmpty()) {
                    emptyStateMessageTextView.setText(getString(R.string.no_search_results, query));
                }
            });
        }
    }
    
    private void loadFeaturedVideo() {
        videoTutorialDao.getPopularVideos().observe(getViewLifecycleOwner(), videos -> {
            if (videos != null && !videos.isEmpty()) {
                featuredVideo = videos.get(0);
                updateFeaturedVideoUI();
            }
        });
    }
    
    private void updateFeaturedVideoUI() {
        if (featuredVideo != null) {
            featuredTitleTextView.setText(featuredVideo.getTitle());
            featuredDescriptionTextView.setText(featuredVideo.getDescription());
            
            // In a real app, load the thumbnail image using Glide or Picasso
            // Example: Glide.with(requireContext()).load(featuredVideo.getThumbnailUrl()).into(featuredThumbnailImageView);
            
            featuredVideoCard.setVisibility(View.VISIBLE);
        } else {
            featuredVideoCard.setVisibility(View.GONE);
        }
    }
    
    private void updateUI(List<VideoTutorial> videos) {
        progressBar.setVisibility(View.GONE);
        
        if (videos == null || videos.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            adapter.submitList(videos);
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
    
    private void playVideo(VideoTutorial video) {
        // Mark video as watched and increment view count
        executor.execute(() -> {
            videoTutorialDao.updateWatchedStatus(video.getId(), true);
            videoTutorialDao.incrementViewCount(video.getId());
        });
        
        // In a real app, start VideoPlayerActivity with the video ID
        Toast.makeText(requireContext(), "Playing video: " + video.getTitle(), Toast.LENGTH_SHORT).show();
        
        // Example of starting video player activity:
        // Intent intent = new Intent(requireActivity(), VideoPlayerActivity.class);
        // intent.putExtra("VIDEO_ID", video.getId());
        // startActivity(intent);
    }
    
    private void toggleFavorite(VideoTutorial video) {
        boolean newStatus = !video.isFavorite();
        executor.execute(() -> {
            videoTutorialDao.updateFavoriteStatus(video.getId(), newStatus);
        });
        
        String message = newStatus ? 
                "Added to favorites: " + video.getTitle() : 
                "Removed from favorites: " + video.getTitle();
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    
    private void shareVideo(VideoTutorial video) {
        // In a real implementation, share video link and info
        String shareText = "Check out this gardening tutorial: " + video.getTitle() +
                "\nBy: " + video.getAuthor() +
                "\n\nShared from AgriGrow - Urban Gardening App";
        
        // Launch share dialog using Android's share intent
        Toast.makeText(requireContext(), "Sharing: " + video.getTitle(), Toast.LENGTH_SHORT).show();
    }
    
    // VideoTutorialAdapter.VideoActionListener implementation
    @Override
    public void onVideoClick(VideoTutorial video) {
        playVideo(video);
    }

    @Override
    public void onToggleFavorite(VideoTutorial video) {
        toggleFavorite(video);
    }

    @Override
    public void onShareVideo(VideoTutorial video) {
        shareVideo(video);
    }
}