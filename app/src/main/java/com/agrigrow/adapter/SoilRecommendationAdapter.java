package com.agrigrow.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.model.SoilRecommendation;

/**
 * Adapter for displaying soil recommendations in a RecyclerView
 */
public class SoilRecommendationAdapter extends ListAdapter<SoilRecommendation, SoilRecommendationAdapter.RecommendationViewHolder> {

    private final RecommendationActionListener actionListener;
    private final Context context;

    public SoilRecommendationAdapter(Context context, RecommendationActionListener actionListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_soil_recommendation, parent, false);
        return new RecommendationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
        SoilRecommendation recommendation = getItem(position);
        
        // Set basic recommendation information
        holder.textViewCategory.setText(recommendation.getCategory());
        holder.textViewTitle.setText(recommendation.getTitle());
        holder.textViewDescription.setText(recommendation.getDescription());
        
        // Process and display action items as bullet points if needed
        String actionItems = recommendation.getActionItems();
        holder.textViewActionItems.setText(actionItems);
        
        // Set priority indication
        holder.textViewPriority.setText(recommendation.getPriorityText());
        String priorityColor = recommendation.getPriorityColor();
        holder.cardViewPriority.setCardBackgroundColor(Color.parseColor(priorityColor));
        
        // Update card border color based on priority
        holder.cardView.setStrokeColor(Color.parseColor(priorityColor));
        
        // Set up buttons based on state
        if (recommendation.isCompleted()) {
            holder.buttonComplete.setVisibility(View.GONE);
            holder.buttonDismiss.setVisibility(View.GONE);
            holder.textViewCompleted.setVisibility(View.VISIBLE);
        } else if (recommendation.isDismissed()) {
            holder.buttonComplete.setVisibility(View.GONE);
            holder.buttonDismiss.setVisibility(View.GONE);
            holder.textViewCompleted.setVisibility(View.GONE);
        } else {
            holder.buttonComplete.setVisibility(View.VISIBLE);
            holder.buttonDismiss.setVisibility(View.VISIBLE);
            holder.textViewCompleted.setVisibility(View.GONE);
            
            // Set click listeners
            holder.buttonComplete.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onMarkComplete(recommendation);
                }
            });
            
            holder.buttonDismiss.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onDismiss(recommendation);
                }
            });
        }
        
        // Set click listener on whole card
        holder.cardView.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onRecommendationClick(recommendation);
            }
        });
    }

    public static class RecommendationViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView textViewCategory;
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final TextView textViewActionItems;
        private final TextView textViewPriority;
        private final CardView cardViewPriority;
        private final Button buttonComplete;
        private final Button buttonDismiss;
        private final TextView textViewCompleted;

        public RecommendationViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_recommendation);
            textViewCategory = itemView.findViewById(R.id.tv_recommendation_category);
            textViewTitle = itemView.findViewById(R.id.tv_recommendation_title);
            textViewDescription = itemView.findViewById(R.id.tv_recommendation_description);
            textViewActionItems = itemView.findViewById(R.id.tv_action_items);
            textViewPriority = itemView.findViewById(R.id.tv_priority);
            cardViewPriority = itemView.findViewById(R.id.card_priority);
            buttonComplete = itemView.findViewById(R.id.btn_mark_complete);
            buttonDismiss = itemView.findViewById(R.id.btn_dismiss);
            textViewCompleted = itemView.findViewById(R.id.tv_completed);
        }
    }

    public interface RecommendationActionListener {
        void onRecommendationClick(SoilRecommendation recommendation);
        void onMarkComplete(SoilRecommendation recommendation);
        void onDismiss(SoilRecommendation recommendation);
    }

    private static final DiffUtil.ItemCallback<SoilRecommendation> DIFF_CALLBACK = new DiffUtil.ItemCallback<SoilRecommendation>() {
        @Override
        public boolean areItemsTheSame(@NonNull SoilRecommendation oldItem, @NonNull SoilRecommendation newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SoilRecommendation oldItem, @NonNull SoilRecommendation newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getActionItems().equals(newItem.getActionItems())
                    && oldItem.getPriority() == newItem.getPriority()
                    && oldItem.isCompleted() == newItem.isCompleted()
                    && oldItem.isDismissed() == newItem.isDismissed();
        }
    };
}