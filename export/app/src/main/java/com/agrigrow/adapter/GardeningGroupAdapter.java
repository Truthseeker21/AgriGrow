package com.agrigrow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.model.GardeningGroup;

import java.util.Locale;

/**
 * Adapter for displaying gardening groups in a RecyclerView
 */
public class GardeningGroupAdapter extends ListAdapter<GardeningGroup, GardeningGroupAdapter.GroupViewHolder> {

    private final Context context;
    private final GroupActionListener listener;

    public GardeningGroupAdapter(Context context, GroupActionListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<GardeningGroup> DIFF_CALLBACK = new DiffUtil.ItemCallback<GardeningGroup>() {
        @Override
        public boolean areItemsTheSame(@NonNull GardeningGroup oldItem, @NonNull GardeningGroup newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull GardeningGroup oldItem, @NonNull GardeningGroup newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getLocation().equals(newItem.getLocation()) &&
                    oldItem.getMemberCount() == newItem.getMemberCount() &&
                    oldItem.isJoined() == newItem.isJoined();
        }
    };

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gardening_group, parent, false);
        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GardeningGroup group = getItem(position);
        
        // Set group info
        holder.nameTextView.setText(group.getName());
        holder.descriptionTextView.setText(group.getDescription());
        holder.locationTextView.setText(group.getLocation());
        holder.categoryTextView.setText(group.getCategory());
        holder.memberCountTextView.setText(String.format(Locale.getDefault(), "%d members", group.getMemberCount()));
        
        // Set image if available (in a real app, use Glide or Picasso for image loading)
        if (group.getImagePath() != null && !group.getImagePath().isEmpty()) {
            // Load image using image loading library
            // Example: Glide.with(context).load(group.getImagePath()).into(holder.groupImageView);
            holder.groupImageView.setVisibility(View.VISIBLE);
        } else {
            holder.groupImageView.setVisibility(View.GONE);
        }
        
        // Set join/leave button state
        if (group.isJoined()) {
            holder.joinButton.setText(R.string.leave_group);
            holder.joinButton.setBackgroundResource(R.drawable.button_outline);
        } else {
            holder.joinButton.setText(R.string.join_group);
            holder.joinButton.setBackgroundResource(R.drawable.button_primary);
        }
        
        // Set click listeners
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onGroupClick(group);
            }
        });
        
        holder.joinButton.setOnClickListener(v -> {
            if (listener != null) {
                if (group.isJoined()) {
                    listener.onLeaveGroup(group);
                } else {
                    listener.onJoinGroup(group);
                }
            }
        });
    }

    /**
     * ViewHolder for group items
     */
    static class GroupViewHolder extends RecyclerView.ViewHolder {
        final CardView cardView;
        final ImageView groupImageView;
        final TextView nameTextView;
        final TextView descriptionTextView;
        final TextView locationTextView;
        final TextView categoryTextView;
        final TextView memberCountTextView;
        final Button joinButton;

        GroupViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_group);
            groupImageView = itemView.findViewById(R.id.iv_group_image);
            nameTextView = itemView.findViewById(R.id.tv_group_name);
            descriptionTextView = itemView.findViewById(R.id.tv_group_description);
            locationTextView = itemView.findViewById(R.id.tv_group_location);
            categoryTextView = itemView.findViewById(R.id.tv_group_category);
            memberCountTextView = itemView.findViewById(R.id.tv_member_count);
            joinButton = itemView.findViewById(R.id.btn_join_group);
        }
    }

    /**
     * Interface for group item interactions
     */
    public interface GroupActionListener {
        void onGroupClick(GardeningGroup group);
        void onJoinGroup(GardeningGroup group);
        void onLeaveGroup(GardeningGroup group);
    }
}