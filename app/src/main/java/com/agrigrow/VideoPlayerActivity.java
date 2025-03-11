package com.agrigrow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.adapter.VideoTutorialAdapter;
import com.agrigrow.database.AppDatabase;
import com.agrigrow.database.VideoTutorialDao;
import com.agrigrow.model.VideoTutorial;
import com.agrigrow.util.SocialSharingHelper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Activity for playing and viewing video tutorials
 */
public class VideoPlayerActivity extends AppCompatActivity implements VideoTutorialAdapter.VideoActionListener {

    private VideoTutorialDao videoTutorialDao;
    private SocialSharingHelper socialSharingHelper;
    private VideoTutorial currentVideo;
    private final Executor executor = Executors.newSingleThreadExecutor();
    
    // UI Elements
    private ImageView videoThumbnailImageView;
    private ImageView playButtonImageView;
    private ProgressBar videoLoadingProgressBar;
    private TextView videoTitleTextView;
    private TextView viewCountTextView;
    private TextView dateTextView;
    private ImageView favoriteImageView;
    private TextView authorNameTextView;
    private TextView subscriberCountTextView;
    private Button subscribeButton;
    private TextView videoDescriptionTextView;
    private RecyclerView relatedVideosRecyclerView;
    private VideoTutorialAdapter relatedVideosAdapter;
    private LinearLayout favoriteLayout;
    private LinearLayout shareLayout;
    private LinearLayout downloadLayout;
    
    private SimpleDateFormat dateFormatter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        
        // Initialize database access
        videoTutorialDao = AppDatabase.getInstance(this).videoTutorialDao();
        socialSharingHelper = new SocialSharingHelper(this);
        dateFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        
        initializeViews();
        setupToolbar();
        setupClickListeners();
        setupRelatedVideosAdapter();
        
        // Get video ID from intent
        long videoId = getIntent().getLongExtra("VIDEO_ID", -1);
        if (videoId != -1) {
            loadVideo(videoId);
        } else {
            // Handle error - invalid video ID
            Toast.makeText(this, "Error: Invalid video ID", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void initializeViews() {
        // Video player views
        videoThumbnailImageView = findViewById(R.id.iv_video_thumbnail);
        playButtonImageView = findViewById(R.id.iv_play_button);
        videoLoadingProgressBar = findViewById(R.id.progress_video_loading);
        
        // Video info views
        videoTitleTextView = findViewById(R.id.tv_video_title);
        viewCountTextView = findViewById(R.id.tv_view_count);
        dateTextView = findViewById(R.id.tv_date);
        
        // Action buttons
        favoriteImageView = findViewById(R.id.iv_favorite);
        favoriteLayout = findViewById(R.id.layout_favorite);
        shareLayout = findViewById(R.id.layout_share);
        downloadLayout = findViewById(R.id.layout_download);
        
        // Author info
        authorNameTextView = findViewById(R.id.tv_author_name);
        subscriberCountTextView = findViewById(R.id.tv_subscriber_count);
        subscribeButton = findViewById(R.id.btn_subscribe);
        
        // Description
        videoDescriptionTextView = findViewById(R.id.tv_video_description);
        
        // Related videos
        relatedVideosRecyclerView = findViewById(R.id.recycler_related_videos);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    
    private void setupClickListeners() {
        // Play button
        playButtonImageView.setOnClickListener(v -> {
            startVideoPlayback();
        });
        
        // Video thumbnail area
        videoThumbnailImageView.setOnClickListener(v -> {
            if (playButtonImageView.getVisibility() == View.VISIBLE) {
                startVideoPlayback();
            }
        });
        
        // Favorite button
        favoriteLayout.setOnClickListener(v -> {
            if (currentVideo != null) {
                toggleFavorite();
            }
        });
        
        // Share button
        shareLayout.setOnClickListener(v -> {
            if (currentVideo != null) {
                shareVideo();
            }
        });
        
        // Download button
        downloadLayout.setOnClickListener(v -> {
            if (currentVideo != null) {
                Toast.makeText(this, "Download functionality would be implemented here", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Subscribe button
        subscribeButton.setOnClickListener(v -> {
            if (subscribeButton.getText().toString().equals(getString(R.string.subscribe))) {
                subscribeButton.setText(R.string.subscribed);
                Toast.makeText(this, "Subscribed to " + currentVideo.getAuthor(), Toast.LENGTH_SHORT).show();
            } else {
                subscribeButton.setText(R.string.subscribe);
                Toast.makeText(this, "Unsubscribed from " + currentVideo.getAuthor(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setupRelatedVideosAdapter() {
        relatedVideosAdapter = new VideoTutorialAdapter(this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        relatedVideosRecyclerView.setLayoutManager(layoutManager);
        relatedVideosRecyclerView.setAdapter(relatedVideosAdapter);
    }
    
    private void loadVideo(long videoId) {
        videoTutorialDao.getVideoById(videoId).observe(this, video -> {
            if (video != null) {
                currentVideo = video;
                updateUI();
                loadRelatedVideos();
                
                // Mark as watched and increment view count
                executor.execute(() -> {
                    videoTutorialDao.updateWatchedStatus(videoId, true);
                    videoTutorialDao.incrementViewCount(videoId);
                });
            } else {
                // Handle error - video not found
                Toast.makeText(this, "Error: Video not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    
    private void updateUI() {
        // Set video info
        videoTitleTextView.setText(currentVideo.getTitle());
        viewCountTextView.setText(String.format(Locale.getDefault(), "%d views", currentVideo.getViewCount() + 1)); // +1 for current view
        
        if (currentVideo.getPublishedDate() != null) {
            dateTextView.setText(dateFormatter.format(currentVideo.getPublishedDate()));
        }
        
        // Set video thumbnail (in a real app, use Glide or Picasso)
        // Example: Glide.with(this).load(currentVideo.getThumbnailUrl()).into(videoThumbnailImageView);
        
        // Set favorite status
        updateFavoriteIcon();
        
        // Set author info
        authorNameTextView.setText(currentVideo.getAuthor());
        subscriberCountTextView.setText(String.format(Locale.getDefault(), "%s subscribers", formatNumber(5200))); // Example subscriber count
        
        // Set video description
        videoDescriptionTextView.setText(currentVideo.getDescription());
    }
    
    private void updateFavoriteIcon() {
        if (currentVideo.isFavorite()) {
            favoriteImageView.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            favoriteImageView.setImageResource(R.drawable.ic_favorite_outline);
        }
    }
    
    private void loadRelatedVideos() {
        // In a real app, get related videos based on category, author, or tags
        videoTutorialDao.getVideosByCategory(currentVideo.getCategory()).observe(this, videos -> {
            if (videos != null && !videos.isEmpty()) {
                // Filter out the current video
                List<VideoTutorial> relatedVideos = filterCurrentVideo(videos);
                relatedVideosAdapter.submitList(relatedVideos);
            }
        });
    }
    
    private List<VideoTutorial> filterCurrentVideo(List<VideoTutorial> videos) {
        videos.removeIf(video -> video.getId() == currentVideo.getId());
        return videos;
    }
    
    private void startVideoPlayback() {
        // Show loading indicator
        playButtonImageView.setVisibility(View.GONE);
        videoLoadingProgressBar.setVisibility(View.VISIBLE);
        
        // In a real app, initialize and start ExoPlayer or other video player
        // For this implementation, simulate loading delay and then show toast
        videoThumbnailImageView.postDelayed(() -> {
            videoLoadingProgressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Video playback would start here in a real implementation", Toast.LENGTH_SHORT).show();
            playButtonImageView.setVisibility(View.VISIBLE);
        }, 1500);
    }
    
    private void toggleFavorite() {
        boolean newStatus = !currentVideo.isFavorite();
        currentVideo.setFavorite(newStatus);
        updateFavoriteIcon();
        
        executor.execute(() -> {
            videoTutorialDao.updateFavoriteStatus(currentVideo.getId(), newStatus);
        });
        
        String message = newStatus ? 
                "Added to favorites" : 
                "Removed from favorites";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    private void shareVideo() {
        // In a real implementation, share video link and info
        String shareText = "Check out this gardening tutorial: " + currentVideo.getTitle() +
                "\nBy: " + currentVideo.getAuthor() +
                "\n\nShared from AgriGrow - Urban Gardening App";
        
        // Launch share dialog using Android's share intent or SocialSharingHelper
        Toast.makeText(this, "Sharing video", Toast.LENGTH_SHORT).show();
    }
    
    private String formatNumber(int number) {
        if (number < 1000) {
            return String.valueOf(number);
        } else if (number < 1000000) {
            return String.format(Locale.getDefault(), "%.1fK", number / 1000.0);
        } else {
            return String.format(Locale.getDefault(), "%.1fM", number / 1000000.0);
        }
    }
    
    // VideoTutorialAdapter.VideoActionListener implementation
    @Override
    public void onVideoClick(VideoTutorial video) {
        // Load the selected related video
        loadVideo(video.getId());
    }

    @Override
    public void onToggleFavorite(VideoTutorial video) {
        executor.execute(() -> {
            videoTutorialDao.updateFavoriteStatus(video.getId(), !video.isFavorite());
        });
        
        String message = !video.isFavorite() ? 
                "Added to favorites: " + video.getTitle() : 
                "Removed from favorites: " + video.getTitle();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShareVideo(VideoTutorial video) {
        // In a real implementation, share video link and info
        String shareText = "Check out this gardening tutorial: " + video.getTitle() +
                "\nBy: " + video.getAuthor() +
                "\n\nShared from AgriGrow - Urban Gardening App";
        
        // Launch share dialog using Android's share intent or SocialSharingHelper
        Toast.makeText(this, "Sharing: " + video.getTitle(), Toast.LENGTH_SHORT).show();
    }
}