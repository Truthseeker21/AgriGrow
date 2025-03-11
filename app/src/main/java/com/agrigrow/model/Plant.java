package com.agrigrow.model;

import java.util.List;

/**
 * Model class representing a plant with its attributes and growing requirements.
 */
public class Plant {
    private int id;
    private String name;
    private String scientificName;
    private String description;
    private String careInstructions;
    private String wateringNeeds; // e.g., "daily", "weekly"
    private String sunlightRequirements; // e.g., "full sun", "partial shade"
    private String soilType; // e.g., "well-draining", "loamy"
    private int growthDurationDays;
    private String harvestInstructions;
    private boolean suitableForContainers;
    private boolean suitableForIndoor;
    private String idealTemperatureRange;
    private String seasonToPlant;
    private String difficultyLevel; // e.g., "beginner", "intermediate", "advanced"
    private List<String> companionPlants;
    private List<String> pestThreats;
    private List<String> diseaseThreats;
    private String imageUrl;
    private boolean isBookmarked;

    // Default constructor
    public Plant() {
    }

    // Parameterized constructor with essential fields
    public Plant(int id, String name, String description, String wateringNeeds, 
                String sunlightRequirements, String soilType, int growthDurationDays, 
                String difficultyLevel, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.wateringNeeds = wateringNeeds;
        this.sunlightRequirements = sunlightRequirements;
        this.soilType = soilType;
        this.growthDurationDays = growthDurationDays;
        this.difficultyLevel = difficultyLevel;
        this.imageUrl = imageUrl;
        this.isBookmarked = false;
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

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCareInstructions() {
        return careInstructions;
    }

    public void setCareInstructions(String careInstructions) {
        this.careInstructions = careInstructions;
    }

    public String getWateringNeeds() {
        return wateringNeeds;
    }

    public void setWateringNeeds(String wateringNeeds) {
        this.wateringNeeds = wateringNeeds;
    }

    public String getSunlightRequirements() {
        return sunlightRequirements;
    }

    public void setSunlightRequirements(String sunlightRequirements) {
        this.sunlightRequirements = sunlightRequirements;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public int getGrowthDurationDays() {
        return growthDurationDays;
    }

    public void setGrowthDurationDays(int growthDurationDays) {
        this.growthDurationDays = growthDurationDays;
    }

    public String getHarvestInstructions() {
        return harvestInstructions;
    }

    public void setHarvestInstructions(String harvestInstructions) {
        this.harvestInstructions = harvestInstructions;
    }

    public boolean isSuitableForContainers() {
        return suitableForContainers;
    }

    public void setSuitableForContainers(boolean suitableForContainers) {
        this.suitableForContainers = suitableForContainers;
    }

    public boolean isSuitableForIndoor() {
        return suitableForIndoor;
    }

    public void setSuitableForIndoor(boolean suitableForIndoor) {
        this.suitableForIndoor = suitableForIndoor;
    }

    public String getIdealTemperatureRange() {
        return idealTemperatureRange;
    }

    public void setIdealTemperatureRange(String idealTemperatureRange) {
        this.idealTemperatureRange = idealTemperatureRange;
    }

    public String getSeasonToPlant() {
        return seasonToPlant;
    }

    public void setSeasonToPlant(String seasonToPlant) {
        this.seasonToPlant = seasonToPlant;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public List<String> getCompanionPlants() {
        return companionPlants;
    }

    public void setCompanionPlants(List<String> companionPlants) {
        this.companionPlants = companionPlants;
    }

    public List<String> getPestThreats() {
        return pestThreats;
    }

    public void setPestThreats(List<String> pestThreats) {
        this.pestThreats = pestThreats;
    }

    public List<String> getDiseaseThreats() {
        return diseaseThreats;
    }

    public void setDiseaseThreats(List<String> diseaseThreats) {
        this.diseaseThreats = diseaseThreats;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}
