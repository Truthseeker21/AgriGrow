package com.agrigrow.model;

/**
 * Model class representing a gardening tip or piece of advice.
 */
public class GardenTip {
    private int id;
    private String title;
    private String description;
    private String category; // e.g., "watering", "pest control", "soil preparation"
    private String difficultyLevel; // e.g., "beginner", "intermediate", "advanced"
    private boolean isSeasonSpecific;
    private String applicableSeason; // e.g., "spring", "summer", "all"
    private String imageUrl;
    private boolean isFavorite;

    // Default constructor
    public GardenTip() {
    }

    // Parameterized constructor
    public GardenTip(int id, String title, String description, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.isFavorite = false;
    }

    // Full parameterized constructor
    public GardenTip(int id, String title, String description, String category,
                     String difficultyLevel, boolean isSeasonSpecific,
                     String applicableSeason, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.difficultyLevel = difficultyLevel;
        this.isSeasonSpecific = isSeasonSpecific;
        this.applicableSeason = applicableSeason;
        this.imageUrl = imageUrl;
        this.isFavorite = false;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public boolean isSeasonSpecific() {
        return isSeasonSpecific;
    }

    public void setSeasonSpecific(boolean seasonSpecific) {
        isSeasonSpecific = seasonSpecific;
    }

    public String getApplicableSeason() {
        return applicableSeason;
    }

    public void setApplicableSeason(String applicableSeason) {
        this.applicableSeason = applicableSeason;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
