package com.agrigrow.adapter;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.model.Badge;

/**
 * Adapter for displaying badges in a RecyclerView
 */
public class BadgeAdapter extends ListAdapter<Badge, BadgeAdapter.BadgeViewHolder> {
    
    private final Context context;
    private final OnBadgeClickListener listener;
    
    public interface OnBadgeClickListener {
        void onBadgeClick(Badge badge);
    }
    
    public BadgeAdapter(Context context, OnBadgeClickListener listener) {
        super(new BadgeDiffCallback());
        this.context = context;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_badge, parent, false);
        return new BadgeViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        Badge badge = getItem(position);
        holder.bind(badge);
    }
    
    public class BadgeViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final ImageView badgeImageView;
        private final TextView badgeNameTextView;
        private final TextView badgeCategoryTextView;
        private final TextView pointsTextView;
        private final TextView levelRequiredTextView;
        private final ImageView certificateIconView;
        private final ImageView lockIconView;
        
        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            badgeImageView = itemView.findViewById(R.id.iv_badge);
            badgeNameTextView = itemView.findViewById(R.id.tv_badge_name);
            badgeCategoryTextView = itemView.findViewById(R.id.tv_badge_category);
            pointsTextView = itemView.findViewById(R.id.tv_points_value);
            levelRequiredTextView = itemView.findViewById(R.id.tv_level_required);
            certificateIconView = itemView.findViewById(R.id.iv_certificate_icon);
            lockIconView = itemView.findViewById(R.id.iv_lock_icon);
        }
        
        public void bind(Badge badge) {
            badgeNameTextView.setText(badge.getName());
            badgeCategoryTextView.setText(badge.getCategory());
            pointsTextView.setText(String.valueOf(badge.getPointsAwarded()));
            
            // Show level required if badge is locked
            if (!badge.isUnlocked()) {
                levelRequiredTextView.setVisibility(View.VISIBLE);
                levelRequiredTextView.setText(
                        context.getString(R.string.level_required, badge.getLevelRequired()));
                lockIconView.setVisibility(View.VISIBLE);
                
                // Apply grayscale filter to badge image to indicate locked status
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0); // 0 = grayscale
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                badgeImageView.setColorFilter(filter);
            } else {
                levelRequiredTextView.setVisibility(View.GONE);
                lockIconView.setVisibility(View.GONE);
                badgeImageView.clearColorFilter();
            }
            
            // Show certificate icon if this is a certificate
            certificateIconView.setVisibility(badge.isCertificate() ? View.VISIBLE : View.GONE);
            
            // In a real app, load badge image
            // Example: Glide.with(context).load(badge.getImagePath()).into(badgeImageView);
            
            // Set click listener
            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBadgeClick(badge);
                }
            });
        }
    }
    
    /**
     * DiffUtil callback for optimizing RecyclerView updates
     */
    private static class BadgeDiffCallback extends DiffUtil.ItemCallback<Badge> {
        @Override
        public boolean areItemsTheSame(@NonNull Badge oldItem, @NonNull Badge newItem) {
            return oldItem.getId() == newItem.getId();
        }
        
        @Override
        public boolean areContentsTheSame(@NonNull Badge oldItem, @NonNull Badge newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPointsAwarded() == newItem.getPointsAwarded() &&
                    oldItem.isUnlocked() == newItem.isUnlocked() &&
                    oldItem.getLevelRequired() == newItem.getLevelRequired();
        }
    }
}