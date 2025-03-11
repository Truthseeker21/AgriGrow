package com.agrigrow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.model.GardeningChallenge;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Adapter for displaying gardening challenges in a RecyclerView
 */
public class GardeningChallengeAdapter extends ListAdapter<GardeningChallenge, GardeningChallengeAdapter.ChallengeViewHolder> {
    
    private final Context context;
    private final OnChallengeClickListener listener;
    private final SimpleDateFormat dateFormat;
    
    public interface OnChallengeClickListener {
        void onChallengeClick(GardeningChallenge challenge);
    }
    
    public GardeningChallengeAdapter(Context context, OnChallengeClickListener listener) {
        super(new ChallengeDiffCallback());
        this.context = context;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gardening_challenge, parent, false);
        return new ChallengeViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        GardeningChallenge challenge = getItem(position);
        holder.bind(challenge);
    }
    
    public class ChallengeViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView titleTextView;
        private final TextView pointsTextView;
        private final TextView difficultyTextView;
        private final TextView deadlineTextView;
        private final TextView participantsTextView;
        private final ImageView badgeImageView;
        private final ProgressBar timeProgressBar;
        private final TextView timeRemainingTextView;
        
        public ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            titleTextView = itemView.findViewById(R.id.tv_challenge_title);
            pointsTextView = itemView.findViewById(R.id.tv_points_value);
            difficultyTextView = itemView.findViewById(R.id.tv_difficulty);
            deadlineTextView = itemView.findViewById(R.id.tv_deadline);
            participantsTextView = itemView.findViewById(R.id.tv_participants);
            badgeImageView = itemView.findViewById(R.id.iv_challenge_badge);
            timeProgressBar = itemView.findViewById(R.id.progress_time);
            timeRemainingTextView = itemView.findViewById(R.id.tv_time_remaining);
        }
        
        public void bind(GardeningChallenge challenge) {
            titleTextView.setText(challenge.getTitle());
            pointsTextView.setText(String.valueOf(challenge.getPointsValue()));
            difficultyTextView.setText(challenge.getDifficulty());
            
            // Set difficulty color
            int difficultyColor;
            switch (challenge.getDifficulty().toLowerCase()) {
                case "easy":
                    difficultyColor = R.color.beginner_green;
                    break;
                case "medium":
                    difficultyColor = R.color.intermediate_orange;
                    break;
                case "hard":
                    difficultyColor = R.color.advanced_red;
                    break;
                default:
                    difficultyColor = R.color.beginner_green;
                    break;
            }
            difficultyTextView.setTextColor(context.getResources().getColor(difficultyColor));
            
            // Format deadline
            if (challenge.getEndDate() != null) {
                deadlineTextView.setText(dateFormat.format(challenge.getEndDate()));
                
                // Calculate and display time remaining
                updateTimeRemaining(challenge.getEndDate());
            } else {
                deadlineTextView.setText(R.string.no_deadline);
                timeRemainingTextView.setText(R.string.no_time_limit);
                timeProgressBar.setVisibility(View.GONE);
            }
            
            // Set participants count
            participantsTextView.setText(
                    context.getString(R.string.participants_count, challenge.getParticipantCount()));
            
            // In a real app, load badge image
            // Example: Glide.with(context).load(challenge.getBadgeImagePath()).into(badgeImageView);
            
            // Set click listener
            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onChallengeClick(challenge);
                }
            });
        }
        
        private void updateTimeRemaining(Date endDate) {
            Date currentDate = Calendar.getInstance().getTime();
            long diffInMillis = endDate.getTime() - currentDate.getTime();
            
            if (diffInMillis <= 0) {
                // Challenge has ended
                timeRemainingTextView.setText(R.string.challenge_ended);
                timeProgressBar.setVisibility(View.GONE);
                return;
            }
            
            // Calculate days, hours remaining
            long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
            long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) - TimeUnit.DAYS.toHours(days);
            
            if (days > 0) {
                timeRemainingTextView.setText(context.getString(R.string.days_remaining, days));
            } else if (hours > 0) {
                timeRemainingTextView.setText(context.getString(R.string.hours_remaining, hours));
            } else {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
                timeRemainingTextView.setText(context.getString(R.string.minutes_remaining, minutes));
            }
            
            // Calculate progress
            Date startDate = challenge.getStartDate();
            if (startDate != null) {
                long totalDuration = endDate.getTime() - startDate.getTime();
                long elapsed = currentDate.getTime() - startDate.getTime();
                int progress = (int) (100 - (elapsed * 100 / totalDuration));
                timeProgressBar.setProgress(progress);
            } else {
                timeProgressBar.setVisibility(View.GONE);
            }
        }
    }
    
    /**
     * DiffUtil callback for optimizing RecyclerView updates
     */
    private static class ChallengeDiffCallback extends DiffUtil.ItemCallback<GardeningChallenge> {
        @Override
        public boolean areItemsTheSame(@NonNull GardeningChallenge oldItem, @NonNull GardeningChallenge newItem) {
            return oldItem.getId() == newItem.getId();
        }
        
        @Override
        public boolean areContentsTheSame(@NonNull GardeningChallenge oldItem, @NonNull GardeningChallenge newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getPointsValue() == newItem.getPointsValue() &&
                    oldItem.isActive() == newItem.isActive() &&
                    oldItem.isCompleted() == newItem.isCompleted() &&
                    oldItem.getParticipantCount() == newItem.getParticipantCount();
        }
    }
}