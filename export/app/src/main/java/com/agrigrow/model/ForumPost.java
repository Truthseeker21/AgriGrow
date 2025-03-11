package com.agrigrow.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Entity class representing a forum post in the AgriGrow application.
 * This class contains information about user posts in the gardening community forum.
 */
@Entity(
    tableName = "forum_posts",
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "user_id",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("user_id")
    }
)
public class ForumPost {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "title")
    @NonNull
    private String title;
    
    @ColumnInfo(name = "content")
    @NonNull
    private String content;
    
    @ColumnInfo(name = "user_id")
    private int userId;
    
    @ColumnInfo(name = "category")
    private String category;
    
    @ColumnInfo(name = "image_url")
    private String imageUrl;
    
    @ColumnInfo(name = "created_at")
    private long createdAt;
    
    @ColumnInfo(name = "updated_at")
    private long updatedAt;
    
    @ColumnInfo(name = "like_count")
    private int likeCount;
    
    @ColumnInfo(name = "comment_count")
    private int commentCount;
    
    /**
     * Default constructor required by Room
     */
    public ForumPost() {
    }
    
    /**
     * Minimal constructor with required fields
     * @param title The post title
     * @param content The post content
     * @param userId The ID of the user who created the post
     */
    public ForumPost(@NonNull String title, @NonNull String content, int userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.likeCount = 0;
        this.commentCount = 0;
    }
    
    /**
     * Full constructor with all fields
     * @param id The post ID
     * @param title The post title
     * @param content The post content
     * @param userId The ID of the user who created the post
     * @param category The post category
     * @param imageUrl URL to an image for the post
     * @param createdAt Creation timestamp
     * @param updatedAt Last update timestamp
     * @param likeCount Number of likes
     * @param commentCount Number of comments
     */
    public ForumPost(int id, @NonNull String title, @NonNull String content, 
                     int userId, String category, String imageUrl,
                     long createdAt, long updatedAt, int likeCount, int commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.category = category;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @NonNull
    public String getTitle() {
        return title;
    }
    
    public void setTitle(@NonNull String title) {
        this.title = title;
    }
    
    @NonNull
    public String getContent() {
        return content;
    }
    
    public void setContent(@NonNull String content) {
        this.content = content;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public long getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public int getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    
    public int getCommentCount() {
        return commentCount;
    }
    
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    
    /**
     * Increment the like count by 1
     */
    public void incrementLikeCount() {
        this.likeCount++;
    }
    
    /**
     * Decrement the like count by 1, ensuring it doesn't go below 0
     */
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
    
    /**
     * Increment the comment count by 1
     */
    public void incrementCommentCount() {
        this.commentCount++;
    }
    
    /**
     * Decrement the comment count by 1, ensuring it doesn't go below 0
     */
    public void decrementCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }
    
    /**
     * Update the content and set updated_at to current time
     * @param newContent The new content for the post
     */
    public void updateContent(@NonNull String newContent) {
        this.content = newContent;
        this.updatedAt = System.currentTimeMillis();
    }
    
    /**
     * Get a formatted creation date string
     * @return Formatted date string
     */
    public String getFormattedCreatedAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy 'at' h:mm a", Locale.getDefault());
        return sdf.format(new Date(createdAt));
    }
    
    /**
     * Get a relative time string for the creation time (e.g., "2 hours ago")
     * @return Relative time string
     */
    public String getRelativeTimeString() {
        long now = System.currentTimeMillis();
        long diff = now - createdAt;
        
        // Convert to seconds
        long seconds = diff / 1000;
        if (seconds < 60) {
            return "Just now";
        }
        
        // Convert to minutes
        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + " minute" + (minutes == 1 ? "" : "s") + " ago";
        }
        
        // Convert to hours
        long hours = minutes / 60;
        if (hours < 24) {
            return hours + " hour" + (hours == 1 ? "" : "s") + " ago";
        }
        
        // Convert to days
        long days = hours / 24;
        if (days < 30) {
            return days + " day" + (days == 1 ? "" : "s") + " ago";
        }
        
        // Convert to months
        long months = days / 30;
        if (months < 12) {
            return months + " month" + (months == 1 ? "" : "s") + " ago";
        }
        
        // Convert to years
        long years = months / 12;
        return years + " year" + (years == 1 ? "" : "s") + " ago";
    }
    
    /**
     * Get a truncated preview of the content for display in lists
     * @param maxLength Maximum length of the preview
     * @return Truncated content with ellipsis if needed
     */
    public String getContentPreview(int maxLength) {
        if (content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength - 3) + "...";
    }
}