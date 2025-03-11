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

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying Plant items in a RecyclerView.
 * This adapter handles the display of plant information in the plant catalog
 * and allows for interaction with individual plants.
 */
public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {
    
    private final Context context;
    private List<Plant> plants;
    private final OnPlantClickListener listener;
    
    /**
     * Interface for handling plant item click events
     */
    public interface OnPlantClickListener {
        void onPlantClick(Plant plant);
        void onBookmarkClick(Plant plant, boolean isBookmarked);
    }
    
    /**
     * Constructor initializes the adapter with a context and click listener
     * @param context The application context
     * @param listener Listener for plant item interactions
     */
    public PlantAdapter(Context context, OnPlantClickListener listener) {
        this.context = context;
        this.plants = new ArrayList<>();
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plants.get(position);
        holder.bind(plant, listener);
    }
    
    @Override
    public int getItemCount() {
        return plants.size();
    }
    
    /**
     * Updates the adapter with a new list of plants
     * @param plants New list of plants to display
     */
    public void setPlants(List<Plant> plants) {
        this.plants = plants;
        notifyDataSetChanged();
    }
    
    /**
     * Add a single plant to the adapter's list
     * @param plant Plant to add
     */
    public void addPlant(Plant plant) {
        plants.add(plant);
        notifyItemInserted(plants.size() - 1);
    }
    
    /**
     * Remove a plant from the adapter's list
     * @param plant Plant to remove
     */
    public void removePlant(Plant plant) {
        int position = plants.indexOf(plant);
        if (position >= 0) {
            plants.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    /**
     * Filter plants by name, category, or other attributes
     * @param query Search query
     * @param originalList Original list of plants before filtering
     */
    public void filterPlants(String query, List<Plant> originalList) {
        query = query.toLowerCase().trim();
        
        List<Plant> filteredList = new ArrayList<>();
        
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (Plant plant : originalList) {
                // Filter by name or category
                if (plant.getName().toLowerCase().contains(query) || 
                    (plant.getCategory() != null && plant.getCategory().toLowerCase().contains(query))) {
                    filteredList.add(plant);
                }
            }
        }
        
        setPlants(filteredList);
    }
    
    /**
     * ViewHolder class for Plant items
     */
    static class PlantViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewPlant;
        private final TextView textViewPlantName;
        private final TextView textViewPlantDescription;
        private final ImageView imageViewBookmark;
        private final TextView textViewDifficultyLevel;
        private final TextView textViewSunlight;
        private final TextView textViewWater;
        
        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            
            imageViewPlant = itemView.findViewById(R.id.imageViewPlant);
            textViewPlantName = itemView.findViewById(R.id.textViewPlantName);
            textViewPlantDescription = itemView.findViewById(R.id.textViewPlantDescription);
            imageViewBookmark = itemView.findViewById(R.id.imageViewBookmark);
            textViewDifficultyLevel = itemView.findViewById(R.id.textViewDifficultyLevel);
            textViewSunlight = itemView.findViewById(R.id.textViewSunlight);
            textViewWater = itemView.findViewById(R.id.textViewWater);
        }
        
        /**
         * Bind plant data to the ViewHolder
         * @param plant Plant to display
         * @param listener Click listener for interactions
         */
        public void bind(final Plant plant, final OnPlantClickListener listener) {
            textViewPlantName.setText(plant.getName());
            textViewPlantDescription.setText(plant.getDescription());
            textViewDifficultyLevel.setText(plant.getDifficultyText());
            textViewSunlight.setText(plant.getSunlightRequirement());
            textViewWater.setText(plant.getWateringText());
            
            // Load plant image using Glide
            if (plant.getImageUrl() != null && !plant.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(plant.getImageUrl())
                        .placeholder(R.drawable.ic_sun) // Replace with your placeholder
                        .error(R.drawable.ic_sun) // Replace with your error image
                        .into(imageViewPlant);
            } else {
                // Use a default plant image if no image URL is available
                imageViewPlant.setImageResource(R.drawable.ic_sun); // Replace with default plant image
            }
            
            // Set bookmark icon based on bookmarked status
            imageViewBookmark.setImageResource(
                    plant.isBookmarked() ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_outline);
            
            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPlantClick(plant);
                }
            });
            
            imageViewBookmark.setOnClickListener(v -> {
                if (listener != null) {
                    boolean newBookmarkState = !plant.isBookmarked();
                    plant.setBookmarked(newBookmarkState);
                    
                    // Update UI
                    imageViewBookmark.setImageResource(
                            newBookmarkState ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_outline);
                    
                    // Notify listener
                    listener.onBookmarkClick(plant, newBookmarkState);
                }
            });
        }
    }
}