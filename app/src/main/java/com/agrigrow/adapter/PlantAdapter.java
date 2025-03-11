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
import com.agrigrow.model.Plant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Adapter for displaying a list of plants in a RecyclerView.
 */
public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private final List<Plant> plants;
    private final Context context;
    private final OnPlantClickListener listener;

    // Interface for click events
    public interface OnPlantClickListener {
        void onPlantClick(Plant plant, int position);
        void onBookmarkClick(Plant plant, int position);
    }

    // Constructor
    public PlantAdapter(Context context, List<Plant> plants, OnPlantClickListener listener) {
        this.context = context;
        this.plants = plants;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plants.get(position);
        holder.bind(plant);
    }

    @Override
    public int getItemCount() {
        return plants != null ? plants.size() : 0;
    }

    // Update plants data
    public void updatePlants(List<Plant> newPlants) {
        this.plants.clear();
        this.plants.addAll(newPlants);
        notifyDataSetChanged();
    }

    // ViewHolder class
    class PlantViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewPlant;
        private final TextView textViewName;
        private final TextView textViewDescription;
        private final TextView textViewDifficultyLevel;
        private final TextView textViewSunlight;
        private final TextView textViewWater;
        private final ImageView imageViewBookmark;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPlant = itemView.findViewById(R.id.imageViewPlant);
            textViewName = itemView.findViewById(R.id.textViewPlantName);
            textViewDescription = itemView.findViewById(R.id.textViewPlantDescription);
            textViewDifficultyLevel = itemView.findViewById(R.id.textViewDifficultyLevel);
            textViewSunlight = itemView.findViewById(R.id.textViewSunlight);
            textViewWater = itemView.findViewById(R.id.textViewWater);
            imageViewBookmark = itemView.findViewById(R.id.imageViewBookmark);
        }

        public void bind(final Plant plant) {
            textViewName.setText(plant.getName());
            textViewDescription.setText(plant.getDescription());
            textViewDifficultyLevel.setText(plant.getDifficultyLevel());
            textViewSunlight.setText(plant.getSunlightRequirements());
            textViewWater.setText(plant.getWateringNeeds());

            // Load image with Glide
            if (plant.getImageUrl() != null && !plant.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(plant.getImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.placeholder_plant)
                                .error(R.drawable.placeholder_plant))
                        .into(imageViewPlant);
            } else {
                imageViewPlant.setImageResource(R.drawable.placeholder_plant);
            }

            // Set bookmark icon state
            imageViewBookmark.setImageResource(
                    plant.isBookmarked() ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline);

            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onPlantClick(plant, position);
                    }
                }
            });

            imageViewBookmark.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        plant.setBookmarked(!plant.isBookmarked());
                        imageViewBookmark.setImageResource(
                                plant.isBookmarked() ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline);
                        listener.onBookmarkClick(plant, position);
                    }
                }
            });
        }
    }
}
