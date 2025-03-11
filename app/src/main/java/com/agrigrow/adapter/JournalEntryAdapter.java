package com.agrigrow.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.model.JournalEntry;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying journal entries in a RecyclerView
 */
public class JournalEntryAdapter extends RecyclerView.Adapter<JournalEntryAdapter.ViewHolder> {

    private final List<JournalEntry> entries;
    private final OnEntryClickListener listener;

    public interface OnEntryClickListener {
        void onEntryClick(JournalEntry entry);
    }

    public JournalEntryAdapter(List<JournalEntry> entries, OnEntryClickListener listener) {
        this.entries = entries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_journal_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JournalEntry entry = entries.get(position);
        holder.bind(entry, listener);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewDate;
        private final TextView textViewDescription;
        private final ImageView imageViewEntry;
        private final Chip chipEntryType;
        private final ImageView imageViewWeather;
        private final TextView textViewTemperature;
        private final ImageView[] moodStars;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewJournalEntryTitle);
            textViewDate = itemView.findViewById(R.id.textViewJournalEntryDate);
            textViewDescription = itemView.findViewById(R.id.textViewJournalEntryDescription);
            imageViewEntry = itemView.findViewById(R.id.imageViewJournalEntry);
            chipEntryType = itemView.findViewById(R.id.chipEntryType);
            imageViewWeather = itemView.findViewById(R.id.imageViewWeatherIcon);
            textViewTemperature = itemView.findViewById(R.id.textViewTemperature);
            
            // Initialize mood stars
            moodStars = new ImageView[5];
            moodStars[0] = itemView.findViewById(R.id.imageViewStar1);
            moodStars[1] = itemView.findViewById(R.id.imageViewStar2);
            moodStars[2] = itemView.findViewById(R.id.imageViewStar3);
            moodStars[3] = itemView.findViewById(R.id.imageViewStar4);
            moodStars[4] = itemView.findViewById(R.id.imageViewStar5);
        }

        public void bind(JournalEntry entry, OnEntryClickListener listener) {
            // Set title and description
            textViewTitle.setText(entry.getTitle());
            textViewDescription.setText(entry.getDescription());
            
            // Format and set date
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(entry.getDate());
            textViewDate.setText(formattedDate);
            
            // Set entry type chip with appropriate styling
            chipEntryType.setText(capitalizeFirstLetter(entry.getEntryType()));
            
            // Set chip color based on entry type
            int chipColor;
            switch (entry.getEntryType()) {
                case "planting":
                    chipColor = R.color.colorPlanting;
                    break;
                case "harvest":
                    chipColor = R.color.colorHarvest;
                    break;
                case "observation":
                    chipColor = R.color.colorObservation;
                    break;
                case "maintenance":
                    chipColor = R.color.colorMaintenance;
                    break;
                case "treatment":
                    chipColor = R.color.colorTreatment;
                    break;
                default:
                    chipColor = R.color.colorObservation;
            }
            chipEntryType.setChipBackgroundColorResource(chipColor);
            
            // Set weather icon and temperature if available
            if (entry.getWeatherConditions() != null && !entry.getWeatherConditions().isEmpty()) {
                imageViewWeather.setVisibility(View.VISIBLE);
                textViewTemperature.setVisibility(View.VISIBLE);
                
                // Set weather icon based on condition
                int weatherIconResource;
                String condition = entry.getWeatherConditions().toLowerCase();
                if (condition.contains("rain") || condition.contains("shower")) {
                    weatherIconResource = R.drawable.ic_weather_rainy;
                } else if (condition.contains("cloud")) {
                    weatherIconResource = R.drawable.ic_weather_cloudy;
                } else if (condition.contains("sun") || condition.contains("clear")) {
                    weatherIconResource = R.drawable.ic_weather_sunny;
                } else if (condition.contains("snow")) {
                    weatherIconResource = R.drawable.ic_weather_snowy;
                } else {
                    weatherIconResource = R.drawable.ic_weather_sunny;
                }
                imageViewWeather.setImageResource(weatherIconResource);
                
                // Set temperature
                if (entry.getTemperature() > 0) {
                    textViewTemperature.setText(Math.round(entry.getTemperature()) + "°F");
                } else {
                    textViewTemperature.setVisibility(View.GONE);
                }
            } else {
                imageViewWeather.setVisibility(View.GONE);
                textViewTemperature.setVisibility(View.GONE);
            }
            
            // Set mood rating (1-5 stars)
            int moodRating = Math.min(5, Math.max(1, entry.getMoodRating()));
            for (int i = 0; i < 5; i++) {
                if (i < moodRating) {
                    moodStars[i].setImageResource(R.drawable.ic_star_filled);
                } else {
                    moodStars[i].setImageResource(R.drawable.ic_star_empty);
                }
            }
            
            // Load image if available
            if (entry.getImagePath() != null && !entry.getImagePath().isEmpty()) {
                imageViewEntry.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                     .load(entry.getImagePath())
                     .placeholder(R.drawable.placeholder_image)
                     .error(R.drawable.placeholder_image)
                     .into(imageViewEntry);
            } else {
                imageViewEntry.setVisibility(View.GONE);
            }
            
            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEntryClick(entry);
                }
            });
        }
        
        private String capitalizeFirstLetter(String input) {
            if (input == null || input.isEmpty()) {
                return input;
            }
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }
    }
}