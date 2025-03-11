package com.agrigrow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.model.UserReward;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Adapter for displaying user rewards in a RecyclerView
 */
public class RewardAdapter extends ListAdapter<UserReward, RewardAdapter.RewardViewHolder> {
    
    private final Context context;
    private final OnRewardClickListener listener;
    private final SimpleDateFormat dateFormat;
    
    public interface OnRewardClickListener {
        void onRewardClick(UserReward reward);
    }
    
    public RewardAdapter(Context context, OnRewardClickListener listener) {
        super(new RewardDiffCallback());
        this.context = context;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reward, parent, false);
        return new RewardViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        UserReward reward = getItem(position);
        holder.bind(reward);
    }
    
    public class RewardViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final ImageView rewardImageView;
        private final TextView rewardNameTextView;
        private final TextView rewardTypeTextView;
        private final TextView dateEarnedTextView;
        private final TextView pointsEarnedTextView;
        private final Button redeemButton;
        private final ImageView featuredIconView;
        
        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            rewardImageView = itemView.findViewById(R.id.iv_reward);
            rewardNameTextView = itemView.findViewById(R.id.tv_reward_name);
            rewardTypeTextView = itemView.findViewById(R.id.tv_reward_type);
            dateEarnedTextView = itemView.findViewById(R.id.tv_date_earned);
            pointsEarnedTextView = itemView.findViewById(R.id.tv_points_earned);
            redeemButton = itemView.findViewById(R.id.btn_redeem);
            featuredIconView = itemView.findViewById(R.id.iv_featured);
        }
        
        public void bind(UserReward reward) {
            rewardNameTextView.setText(reward.getRewardName());
            
            // Set reward type with appropriate styling
            rewardTypeTextView.setText(formatRewardType(reward.getRewardType()));
            
            int typeColor;
            switch (reward.getRewardType()) {
                case "BADGE":
                    typeColor = R.color.badge_blue;
                    break;
                case "CHALLENGE":
                    typeColor = R.color.challenge_green;
                    break;
                case "LEVEL_UP":
                    typeColor = R.color.level_purple;
                    break;
                case "POINTS":
                default:
                    typeColor = R.color.points_orange;
                    break;
            }
            rewardTypeTextView.setTextColor(context.getResources().getColor(typeColor));
            
            // Format and set date earned
            if (reward.getDateEarned() != null) {
                dateEarnedTextView.setText(
                        context.getString(R.string.earned_on, dateFormat.format(reward.getDateEarned())));
            } else {
                dateEarnedTextView.setVisibility(View.GONE);
            }
            
            // Set points earned
            pointsEarnedTextView.setText(
                    context.getString(R.string.points_earned, reward.getPointsEarned()));
            
            // Set featured icon visibility
            featuredIconView.setVisibility(reward.isFeatured() ? View.VISIBLE : View.GONE);
            
            // Set redeem button visibility and state
            if (reward.isRedeemed()) {
                redeemButton.setText(R.string.redeemed);
                redeemButton.setEnabled(false);
            } else {
                redeemButton.setText(R.string.redeem);
                redeemButton.setEnabled(true);
                redeemButton.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onRewardClick(reward);
                    }
                });
            }
            
            // In a real app, load reward image
            // Example: Glide.with(context).load(reward.getRewardImagePath()).into(rewardImageView);
            
            // Set click listener on the card
            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRewardClick(reward);
                }
            });
        }
        
        private String formatRewardType(String rewardType) {
            switch (rewardType) {
                case "BADGE":
                    return context.getString(R.string.reward_type_badge);
                case "CHALLENGE":
                    return context.getString(R.string.reward_type_challenge);
                case "LEVEL_UP":
                    return context.getString(R.string.reward_type_level_up);
                case "POINTS":
                    return context.getString(R.string.reward_type_points);
                default:
                    return rewardType;
            }
        }
    }
    
    /**
     * DiffUtil callback for optimizing RecyclerView updates
     */
    private static class RewardDiffCallback extends DiffUtil.ItemCallback<UserReward> {
        @Override
        public boolean areItemsTheSame(@NonNull UserReward oldItem, @NonNull UserReward newItem) {
            return oldItem.getId() == newItem.getId();
        }
        
        @Override
        public boolean areContentsTheSame(@NonNull UserReward oldItem, @NonNull UserReward newItem) {
            return oldItem.getRewardName().equals(newItem.getRewardName()) &&
                    oldItem.getPointsEarned() == newItem.getPointsEarned() &&
                    oldItem.isRedeemed() == newItem.isRedeemed() &&
                    oldItem.isFeatured() == newItem.isFeatured();
        }
    }
}