package com.agrigrow.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Model class for gardening techniques and guides
 */
@Entity(tableName = "gardening_techniques")
public class GardeningTechnique {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    
    @NonNull
    @ColumnInfo(name = "description")
    private String description;
    
    @ColumnInfo(name = "category")
    private String category;
    
    @ColumnInfo(name = "difficulty")
    private String difficulty;
    
    @ColumnInfo(name = "image_url")
    private String imageUrl;
    
    @ColumnInfo(name = "video_url")
    private String videoUrl;
    
    @ColumnInfo(name = "steps")
    private String steps;
    
    @ColumnInfo(name = "materials")
    private String materials;
    
    @ColumnInfo(name = "estimated_time")
    private String estimatedTime;
    
    @ColumnInfo(name = "author")
    private String author;
    
    /**
     * Basic constructor
     * @param title The technique title
     * @param description Brief description
     */
    public GardeningTechnique(@NonNull String title, @NonNull String description) {
        this.title = title;
        this.description = description;
        this.category = "General";
        this.difficulty = "Beginner";
    }
    
    /**
     * Constructor with image
     * @param title Technique title
     * @param description Brief description
     * @param category Technique category
     * @param difficulty Difficulty level
     * @param imageUrl URL to technique image
     */
    public GardeningTechnique(@NonNull String title, @NonNull String description, 
                             String category, String difficulty, String imageUrl) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.imageUrl = imageUrl;
    }
    
    /**
     * Full constructor
     * @param title Technique title
     * @param description Brief description
     * @param category Technique category
     * @param difficulty Difficulty level
     * @param imageUrl URL to technique image
     * @param videoUrl URL to instructional video
     * @param steps Detailed steps (can include HTML formatting)
     * @param materials Required materials
     * @param estimatedTime Estimated time to complete
     * @param author Technique author/source
     */
    public GardeningTechnique(@NonNull String title, @NonNull String description, 
                             String category, String difficulty, String imageUrl,
                             String videoUrl, String steps, String materials,
                             String estimatedTime, String author) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.steps = steps;
        this.materials = materials;
        this.estimatedTime = estimatedTime;
        this.author = author;
    }
    
    // Getters and setters
    
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
    public String getDescription() {
        return description;
    }
    
    public void setDescription(@NonNull String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getVideoUrl() {
        return videoUrl;
    }
    
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
    public String getSteps() {
        return steps;
    }
    
    public void setSteps(String steps) {
        this.steps = steps;
    }
    
    public String getMaterials() {
        return materials;
    }
    
    public void setMaterials(String materials) {
        this.materials = materials;
    }
    
    public String getEstimatedTime() {
        return estimatedTime;
    }
    
    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
}