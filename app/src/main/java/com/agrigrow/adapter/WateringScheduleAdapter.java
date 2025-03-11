package com.agrigrow.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Adapter for displaying plant watering schedule based on weather
 */
public class WateringScheduleAdapter extends RecyclerView.Adapter<WateringScheduleAdapter.ViewHolder> {

    private final List<String> plantTypes = new ArrayList<>();
    private final List<Integer> wateringDays = new ArrayList<>();
    private Map<String, Integer> wateringSchedule;

    public WateringScheduleAdapter(Map<String, Integer> wateringSchedule) {
        this.wateringSchedule = wateringSchedule;
        updateData();
    }

    private void updateData() {
        plantTypes.clear();
        wateringDays.clear();
        
        for (Map.Entry<String, Integer> entry : wateringSchedule.entrySet()) {
            plantTypes.add(entry.getKey());
            wateringDays.add(entry.getValue());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_watering_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String plantType = plantTypes.get(position);
        int daysUntilWatering = wateringDays.get(position);
        
        // Format plant type for display (capitalize first letter)
        String displayPlantType = plantType.substring(0, 1).toUpperCase() + plantType.substring(1);
        holder.textViewPlantType.setText(displayPlantType);
        
        // Set watering day message
        String wateringMessage;
        switch (daysUntilWatering) {
            case 0:
                wateringMessage = "Water today";
                break;
            case 1:
                wateringMessage = "Water tomorrow";
                break;
            default:
                wateringMessage = "Water in " + daysUntilWatering + " days";
                break;
        }
        holder.textViewWateringDay.setText(wateringMessage);
        
        // Set icon based on plant type
        int iconResource = getIconForPlantType(plantType);
        holder.imageViewPlantIcon.setImageResource(iconResource);
        
        // Set indicator color based on urgency
        int indicatorColor;
        if (daysUntilWatering == 0) {
            indicatorColor = android.R.color.holo_red_light; // Today - urgent
        } else if (daysUntilWatering == 1) {
            indicatorColor = android.R.color.holo_orange_light; // Tomorrow - important
        } else {
            indicatorColor = android.R.color.holo_green_light; // Later - not urgent
        }
        holder.indicatorView.setBackgroundResource(indicatorColor);
    }

    @Override
    public int getItemCount() {
        return plantTypes.size();
    }
    
    /**
     * Update the watering schedule data
     * @param wateringSchedule New watering schedule data
     */
    public void updateSchedule(Map<String, Integer> wateringSchedule) {
        this.wateringSchedule = wateringSchedule;
        updateData();
        notifyDataSetChanged();
    }
    
    /**
     * Get appropriate icon resource based on plant type
     * @param plantType The type of plant
     * @return Icon resource ID
     */
    private int getIconForPlantType(String plantType) {
        // In a real app, you would use custom drawable resources for each plant type
        // Here we're using system icons as placeholders
        switch (plantType.toLowerCase()) {
            case "vegetables":
                return android.R.drawable.ic_menu_compass;
            case "herbs":
                return android.R.drawable.ic_menu_upload;
            case "flowers":
                return android.R.drawable.ic_menu_gallery;
            case "shrubs":
                return android.R.drawable.ic_menu_share;
            case "trees":
                return android.R.drawable.ic_menu_mapmode;
            case "succulents":
                return android.R.drawable.ic_menu_edit;
            case "cacti":
                return android.R.drawable.ic_menu_today;
            default:
                return android.R.drawable.ic_menu_add;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewPlantType;
        final TextView textViewWateringDay;
        final ImageView imageViewPlantIcon;
        final View indicatorView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPlantType = itemView.findViewById(R.id.textViewPlantType);
            textViewWateringDay = itemView.findViewById(R.id.textViewWateringDay);
            imageViewPlantIcon = itemView.findViewById(R.id.imageViewPlantIcon);
            indicatorView = itemView.findViewById(R.id.viewIndicator);
        }
    }
}