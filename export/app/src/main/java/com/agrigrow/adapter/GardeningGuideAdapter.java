package com.agrigrow.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.model.GardeningTechnique;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import java.util.List;

/**
 * Adapter for displaying gardening guides/techniques in a RecyclerView
 */
public class GardeningGuideAdapter extends RecyclerView.Adapter<GardeningGuideAdapter.ViewHolder> {

    private final List<GardeningTechnique> guides;
    private final OnGuideClickListener listener;

    public interface OnGuideClickListener {
        void onGuideClick(GardeningTechnique guide);
    }

    public GardeningGuideAdapter(List<GardeningTechnique> guides, OnGuideClickListener listener) {
        this.guides = guides;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gardening_guide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GardeningTechnique guide = guides.get(position);
        holder.bind(guide, listener);
    }

    @Override
    public int getItemCount() {
        return guides.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final ImageView imageViewGuide;
        private final Chip chipDifficulty;
        private final Chip chipCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewGuideTitle);
            textViewDescription = itemView.findViewById(R.id.textViewGuideDescription);
            imageViewGuide = itemView.findViewById(R.id.imageViewGuide);
            chipDifficulty = itemView.findViewById(R.id.chipDifficulty);
            chipCategory = itemView.findViewById(R.id.chipCategory);
        }

        public void bind(GardeningTechnique guide, OnGuideClickListener listener) {
            textViewTitle.setText(guide.getTitle());
            textViewDescription.setText(guide.getDescription());
            
            // Set difficulty chip
            chipDifficulty.setText(guide.getDifficulty());
            
            // Set different colors based on difficulty
            int difficultyColor;
            switch (guide.getDifficulty()) {
                case "Beginner":
                    difficultyColor = R.color.colorBeginner;
                    break;
                case "Intermediate":
                    difficultyColor = R.color.colorIntermediate;
                    break;
                case "Advanced":
                    difficultyColor = R.color.colorAdvanced;
                    break;
                default:
                    difficultyColor = R.color.colorBeginner;
            }
            chipDifficulty.setChipBackgroundColorResource(difficultyColor);
            
            // Set category chip
            chipCategory.setText(guide.getCategory());
            
            // Load image using Glide if available
            if (guide.getImageUrl() != null && !guide.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                     .load(guide.getImageUrl())
                     .placeholder(R.drawable.placeholder_guide)
                     .error(R.drawable.placeholder_guide)
                     .into(imageViewGuide);
            } else {
                imageViewGuide.setImageResource(R.drawable.placeholder_guide);
            }
            
            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onGuideClick(guide);
                }
            });
        }
    }
}