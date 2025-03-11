package com.agrigrow.model;

import java.util.List;

/**
 * Model class representing different gardening techniques such as
 * container gardening, vertical gardening, hydroponics, etc.
 */
public class GardeningTechnique {
    private int id;
    private String name;
    private String description;
    private String detailedGuide;
    private List<String> steps;
    private String difficultyLevel; // e.g., "beginner", "intermediate", "advanced"
    private String spaceRequirement; // e.g., "minimal", "moderate", "large"
    private String costEstimate; // e.g., "low", "medium", "high"
    private String maintenanceLevel; // e.g., "low", "medium", "high"
    private List<String> requiredMaterials;
    private List<String> recommendedPlants;
    private String imageUrl;
    private String videoUrl;
    private boolean isFavorite;

    // Default constructor
    public GardeningTechnique() {
    }

    // Parameterized constructor with essential fields
    public GardeningTechnique(int id, String name, String description, String difficultyLevel,
                             String spaceRequirement, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.difficultyLevel = difficultyLevel;
        this.spaceRequirement = spaceRequirement;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailedGuide() {
        return detailedGuide;
    }

    public void setDetailedGuide(String detailedGuide) {
        this.detailedGuide = detailedGuide;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getSpaceRequirement() {
        return spaceRequirement;
    }

    public void setSpaceRequirement(String spaceRequirement) {
        this.spaceRequirement = spaceRequirement;
    }

    public String getCostEstimate() {
        return costEstimate;
    }

    public void setCostEstimate(String costEstimate) {
        this.costEstimate = costEstimate;
    }

    public String getMaintenanceLevel() {
        return maintenanceLevel;
    }

    public void setMaintenanceLevel(String maintenanceLevel) {
        this.maintenanceLevel = maintenanceLevel;
    }

    public List<String> getRequiredMaterials() {
        return requiredMaterials;
    }

    public void setRequiredMaterials(List<String> requiredMaterials) {
        this.requiredMaterials = requiredMaterials;
    }

    public List<String> getRecommendedPlants() {
        return recommendedPlants;
    }

    public void setRecommendedPlants(List<String> recommendedPlants) {
        this.recommendedPlants = recommendedPlants;
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
