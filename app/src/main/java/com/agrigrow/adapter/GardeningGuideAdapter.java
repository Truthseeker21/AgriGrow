package com.agrigrow.adapter;

import android.content.Context;
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
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Adapter for displaying a list of gardening techniques/guides in a RecyclerView.
 */
public class GardeningGuideAdapter extends RecyclerView.Adapter<GardeningGuideAdapter.GuideViewHolder> {

    private final List<GardeningTechnique> guides;
    private final Context context;
    private final OnGuideClickListener listener;

    // Interface for click events
    public interface OnGuideClickListener {
        void onGuideClick(GardeningTechnique guide, int position);
        void onFavoriteClick(GardeningTechnique guide, int position);
    }

    // Constructor
    public GardeningGuideAdapter(Context context, List<GardeningTechnique> guides, OnGuideClickListener listener) {
        this.context = context;
        this.guides = guides;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gardening_guide, parent, false);
        return new GuideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideViewHolder holder, int position) {
        GardeningTechnique guide = guides.get(position);
        holder.bind(guide);
    }

    @Override
    public int getItemCount() {
        return guides != null ? guides.size() : 0;
    }

    // Update guides data
    public void updateGuides(List<GardeningTechnique> newGuides) {
        this.guides.clear();
        this.guides.addAll(newGuides);
        notifyDataSetChanged();
    }

    // ViewHolder class
    class GuideViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewGuide;
        private final TextView textViewName;
        private final TextView textViewDescription;
        private final TextView textViewDifficulty;
        private final TextView textViewSpace;
        private final ImageView imageViewFavorite;

        public GuideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewGuide = itemView.findViewById(R.id.imageViewGuide);
            textViewName = itemView.findViewById(R.id.textViewGuideName);
            textViewDescription = itemView.findViewById(R.id.textViewGuideDescription);
            textViewDifficulty = itemView.findViewById(R.id.textViewDifficulty);
            textViewSpace = itemView.findViewById(R.id.textViewSpace);
            imageViewFavorite = itemView.findViewById(R.id.imageViewFavorite);
        }

        public void bind(final GardeningTechnique guide) {
            textViewName.setText(guide.getName());
            textViewDescription.setText(guide.getDescription());
            textViewDifficulty.setText(guide.getDifficultyLevel());
            textViewSpace.setText(guide.getSpaceRequirement());

            // Load image with Glide
            if (guide.getImageUrl() != null && !guide.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(guide.getImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.placeholder_guide)
                                .error(R.drawable.placeholder_guide))
                        .into(imageViewGuide);
            } else {
                imageViewGuide.setImageResource(R.drawable.placeholder_guide);
            }

            // Set favorite icon state
            imageViewFavorite.setImageResource(
                    guide.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_outline);

            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onGuideClick(guide, position);
                    }
                }
            });

            imageViewFavorite.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        guide.setFavorite(!guide.isFavorite());
                        imageViewFavorite.setImageResource(
                                guide.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_outline);
                        listener.onFavoriteClick(guide, position);
                    }
                }
            });
        }
    }
}
