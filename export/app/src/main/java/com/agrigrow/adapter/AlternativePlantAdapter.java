package com.agrigrow.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.util.PlantIdentificationHelper;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Adapter for displaying alternative plant identification matches
 */
public class AlternativePlantAdapter extends RecyclerView.Adapter<AlternativePlantAdapter.ViewHolder> {

    private final List<PlantIdentificationHelper.PlantIdentificationResult> plants;
    private final OnAlternativePlantClickListener listener;

    /**
     * Constructor
     * @param plants List of alternative plant matches
     * @param listener Callback for item clicks
     */
    public AlternativePlantAdapter(List<PlantIdentificationHelper.PlantIdentificationResult> plants, 
                                  OnAlternativePlantClickListener listener) {
        this.plants = plants;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alternative_plant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantIdentificationHelper.PlantIdentificationResult plant = plants.get(position);
        
        // Set plant name
        holder.textViewPlantName.setText(plant.getDisplayName());
        
        // Set scientific name
        holder.textViewScientificName.setText(Html.fromHtml(plant.getFormattedScientificName()));
        
        // Set confidence
        holder.textViewConfidence.setText("Confidence: " + plant.getConfidenceString());
        
        // Load image
        if (plant.getFirstImageUrl() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(plant.getFirstImageUrl())
                    .centerCrop()
                    .into(holder.imageViewPlant);
        } else {
            // Default image if none available
            holder.imageViewPlant.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        
        // Set click listener for detailed view
        holder.buttonViewDetails.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAlternativePlantClick(position);
            }
        });
        
        // Set click listener for the whole item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAlternativePlantClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plants != null ? plants.size() : 0;
    }

    /**
     * ViewHolder for alternative plant items
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewPlant;
        final TextView textViewPlantName;
        final TextView textViewScientificName;
        final TextView textViewConfidence;
        final ImageButton buttonViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPlant = itemView.findViewById(R.id.imageViewPlant);
            textViewPlantName = itemView.findViewById(R.id.textViewPlantName);
            textViewScientificName = itemView.findViewById(R.id.textViewScientificName);
            textViewConfidence = itemView.findViewById(R.id.textViewConfidence);
            buttonViewDetails = itemView.findViewById(R.id.buttonViewDetails);
        }
    }

    /**
     * Interface for alternative plant item click events
     */
    public interface OnAlternativePlantClickListener {
        void onAlternativePlantClick(int position);
    }
}