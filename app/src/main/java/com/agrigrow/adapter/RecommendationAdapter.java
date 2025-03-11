package com.agrigrow.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;

import java.util.List;

/**
 * Adapter for displaying gardening recommendations
 */
public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {

    private final List<String> recommendations;

    public RecommendationAdapter(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommendation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String recommendation = recommendations.get(position);
        holder.textViewRecommendation.setText(recommendation);
    }

    @Override
    public int getItemCount() {
        return recommendations != null ? recommendations.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewRecommendation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRecommendation = itemView.findViewById(R.id.textViewRecommendation);
        }
    }
}