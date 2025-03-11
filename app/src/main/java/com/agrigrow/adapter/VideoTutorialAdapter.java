package com.agrigrow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.model.VideoTutorial;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Adapter for displaying video tutorials in a RecyclerView
 */
public class VideoTutorialAdapter extends ListAdapter<VideoTutorial, VideoTutorialAdapter.VideoViewHolder> {
    
    private final Context context;
    private final VideoActionListener listener;
    private final SimpleDateFormat dateFormat;
    
    /**
     * Interface for handling video actions
     */
    public interface VideoActionListener {
        void onVideoClick(VideoTutorial video);
        void onToggleFavorite(VideoTutorial video);
        void onShareVideo(VideoTutorial video);
    }
    
    public VideoTutorialAdapter(Context context, VideoActionListener listener) {
        super(new VideoTutorialDiffCallback());
        this.context = context;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_tutorial, parent, false);
        return new VideoViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoTutorial video = getItem(position);
        holder.bind(video);
    }
    
    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumbnailImageView;
        private final TextView titleTextView;
        private final TextView authorTextView;
        private final TextView durationTextView;
        private final TextView viewCountTextView;
        private final ImageView favoriteImageView;
        private final ImageView shareImageView;
        private final TextView difficultyTextView;
        private final ImageView watchedBadgeImageView;
        
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.iv_video_thumbnail);
            titleTextView = itemView.findViewById(R.id.tv_video_title);
            authorTextView = itemView.findViewById(R.id.tv_author);
            durationTextView = itemView.findViewById(R.id.tv_duration);
            viewCountTextView = itemView.findViewById(R.id.tv_view_count);
            favoriteImageView = itemView.findViewById(R.id.iv_favorite);
            shareImageView = itemView.findViewById(R.id.iv_share);
            difficultyTextView = itemView.findViewById(R.id.tv_difficulty);
            watchedBadgeImageView = itemView.findViewById(R.id.iv_watched_badge);
        }
        
        public void bind(VideoTutorial video) {
            titleTextView.setText(video.getTitle());
            authorTextView.setText(video.getAuthor());
            durationTextView.setText(video.getFormattedDuration());
            
            // Format view count
            String viewCountText = formatViewCount(video.getViewCount());
            viewCountTextView.setText(viewCountText);
            
            // Set difficulty
            difficultyTextView.setText(video.getDifficulty());
            
            // Set color based on difficulty
            int difficultyColor;
            switch (video.getDifficulty()) {
                case "Beginner":
                    difficultyColor = R.color.beginner_green;
                    break;
                case "Intermediate":
                    difficultyColor = R.color.intermediate_orange;
                    break;
                case "Advanced":
                    difficultyColor = R.color.advanced_red;
                    break;
                default:
                    difficultyColor = R.color.beginner_green;
                    break;
            }
            difficultyTextView.setTextColor(context.getResources().getColor(difficultyColor));
            
            // Set favorite icon
            if (video.isFavorite()) {
                favoriteImageView.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                favoriteImageView.setImageResource(R.drawable.ic_favorite_outline);
            }
            
            // Set watched badge visibility
            watchedBadgeImageView.setVisibility(video.isWatched() ? View.VISIBLE : View.GONE);
            
            // In a real app, load the thumbnail image using Glide or Picasso
            // Example: Glide.with(context).load(video.getThumbnailUrl()).into(thumbnailImageView);
            
            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVideoClick(video);
                }
            });
            
            favoriteImageView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onToggleFavorite(video);
                    // Update the UI (we're not waiting for the DB update notification)
                    video.setFavorite(!video.isFavorite());
                    if (video.isFavorite()) {
                        favoriteImageView.setImageResource(R.drawable.ic_favorite_filled);
                    } else {
                        favoriteImageView.setImageResource(R.drawable.ic_favorite_outline);
                    }
                }
            });
            
            shareImageView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShareVideo(video);
                }
            });
        }
        
        private String formatViewCount(int viewCount) {
            if (viewCount < 1000) {
                return context.getString(R.string.view_count, String.valueOf(viewCount));
            } else if (viewCount < 1000000) {
                return context.getString(R.string.view_count, 
                        String.format(Locale.getDefault(), "%.1fK", viewCount / 1000.0));
            } else {
                return context.getString(R.string.view_count,
                        String.format(Locale.getDefault(), "%.1fM", viewCount / 1000000.0));
            }
        }
    }
    
    /**
     * DiffUtil callback for optimizing RecyclerView updates
     */
    private static class VideoTutorialDiffCallback extends DiffUtil.ItemCallback<VideoTutorial> {
        @Override
        public boolean areItemsTheSame(@NonNull VideoTutorial oldItem, @NonNull VideoTutorial newItem) {
            return oldItem.getId() == newItem.getId();
        }
        
        @Override
        public boolean areContentsTheSame(@NonNull VideoTutorial oldItem, @NonNull VideoTutorial newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getViewCount() == newItem.getViewCount() &&
                    oldItem.isWatched() == newItem.isWatched() &&
                    oldItem.isFavorite() == newItem.isFavorite();
        }
    }
}