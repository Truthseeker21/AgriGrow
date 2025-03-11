package com.agrigrow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.model.SoilTest;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Adapter for displaying soil tests in a RecyclerView
 */
public class SoilTestAdapter extends ListAdapter<SoilTest, SoilTestAdapter.SoilTestViewHolder> {

    private final OnSoilTestClickListener listener;
    private final Context context;
    private final SimpleDateFormat dateFormat;

    public SoilTestAdapter(Context context, OnSoilTestClickListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public SoilTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_soil_test, parent, false);
        return new SoilTestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SoilTestViewHolder holder, int position) {
        SoilTest currentTest = getItem(position);
        
        // Set basic test information
        holder.textViewTestName.setText(currentTest.getName());
        holder.textViewDate.setText(dateFormat.format(currentTest.getTestDate()));
        holder.textViewLocation.setText(currentTest.getLocation());
        
        // Set soil summary
        holder.textViewSummary.setText(currentTest.getSummary());
        
        // Set health score and status
        int healthScore = currentTest.getSoilHealthScore();
        holder.textViewHealthScore.setText(String.valueOf(healthScore));
        
        String healthStatus = currentTest.getHealthStatus();
        holder.textViewHealthStatus.setText(healthStatus);
        
        // Set card color based on health status
        int cardColorResId;
        switch (healthStatus) {
            case "Excellent":
                cardColorResId = R.color.colorHealthy;
                break;
            case "Good":
                cardColorResId = R.color.colorModerate;
                break;
            case "Fair":
                cardColorResId = R.color.colorWarning;
                break;
            case "Poor":
                cardColorResId = R.color.colorCritical;
                break;
            default:
                cardColorResId = R.color.colorAccent;
                break;
        }
        
        // Apply color as border to the health score card
        holder.cardViewHealthScore.setCardBackgroundColor(
                context.getResources().getColor(cardColorResId));
                
        // Indicate if recommendations exist
        if (currentTest.isHasRecommendations()) {
            holder.textViewRecommendations.setVisibility(View.VISIBLE);
        } else {
            holder.textViewRecommendations.setVisibility(View.GONE);
        }
        
        // Set click listener
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSoilTestClick(currentTest);
            }
        });
    }

    public static class SoilTestViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView textViewTestName;
        private final TextView textViewDate;
        private final TextView textViewLocation;
        private final TextView textViewSummary;
        private final TextView textViewHealthScore;
        private final TextView textViewHealthStatus;
        private final TextView textViewRecommendations;
        private final CardView cardViewHealthScore;

        public SoilTestViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_soil_test);
            textViewTestName = itemView.findViewById(R.id.tv_test_name);
            textViewDate = itemView.findViewById(R.id.tv_test_date);
            textViewLocation = itemView.findViewById(R.id.tv_test_location);
            textViewSummary = itemView.findViewById(R.id.tv_test_summary);
            textViewHealthScore = itemView.findViewById(R.id.tv_health_score);
            textViewHealthStatus = itemView.findViewById(R.id.tv_health_status);
            textViewRecommendations = itemView.findViewById(R.id.tv_recommendations_available);
            cardViewHealthScore = itemView.findViewById(R.id.card_health_score);
        }
    }

    public interface OnSoilTestClickListener {
        void onSoilTestClick(SoilTest soilTest);
    }

    private static final DiffUtil.ItemCallback<SoilTest> DIFF_CALLBACK = new DiffUtil.ItemCallback<SoilTest>() {
        @Override
        public boolean areItemsTheSame(@NonNull SoilTest oldItem, @NonNull SoilTest newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SoilTest oldItem, @NonNull SoilTest newItem) {
            return oldItem.getName().equals(newItem.getName())
                    && oldItem.getTestDate().equals(newItem.getTestDate())
                    && oldItem.getLocation().equals(newItem.getLocation())
                    && oldItem.getPhLevel() == newItem.getPhLevel()
                    && oldItem.getNitrogenLevel() == newItem.getNitrogenLevel()
                    && oldItem.getPhosphorusLevel() == newItem.getPhosphorusLevel()
                    && oldItem.getPotassiumLevel() == newItem.getPotassiumLevel()
                    && oldItem.getOrganicMatterPercentage() == newItem.getOrganicMatterPercentage()
                    && oldItem.getSoilHealthScore() == newItem.getSoilHealthScore()
                    && oldItem.isHasRecommendations() == newItem.isHasRecommendations();
        }
    };
}