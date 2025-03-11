package com.agrigrow.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

/**
 * Entity class representing a plant in the database
 */
@Entity(tableName = "plants")
public class Plant {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "scientific_name")
    private String scientificName;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "watering_frequency")
    private int wateringFrequency; // in days

    @ColumnInfo(name = "sunlight_requirement")
    private String sunlightRequirement;

    @ColumnInfo(name = "temperature_min")
    private float temperatureMin;

    @ColumnInfo(name = "temperature_max")
    private float temperatureMax;

    @ColumnInfo(name = "growing_season")
    private String growingSeason;

    @ColumnInfo(name = "difficulty_level")
    private int difficultyLevel; // 1-5 scale

    @ColumnInfo(name = "planting_depth")
    private float plantingDepth; // in inches

    @ColumnInfo(name = "days_to_germination")
    private int daysToGermination;

    @ColumnInfo(name = "days_to_harvest")
    private int daysToHarvest;

    @ColumnInfo(name = "is_in_user_garden")
    private boolean isInUserGarden;

    @ColumnInfo(name = "planting_date")
    private long plantingDate; // timestamp
    
    @ColumnInfo(name = "is_bookmarked")
    private boolean isBookmarked;

    // Constructor
    public Plant(String name, String scientificName, String description, String imageUrl,
                int wateringFrequency, String sunlightRequirement, float temperatureMin,
                float temperatureMax, String growingSeason, int difficultyLevel,
                float plantingDepth, int daysToGermination, int daysToHarvest) {
        this.name = name;
        this.scientificName = scientificName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.wateringFrequency = wateringFrequency;
        this.sunlightRequirement = sunlightRequirement;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.growingSeason = growingSeason;
        this.difficultyLevel = difficultyLevel;
        this.plantingDepth = plantingDepth;
        this.daysToGermination = daysToGermination;
        this.daysToHarvest = daysToHarvest;
        this.isInUserGarden = false;
        this.plantingDate = 0;
        this.isBookmarked = false;
    }

    // Getters and Setters
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getWateringFrequency() {
        return wateringFrequency;
    }

    public void setWateringFrequency(int wateringFrequency) {
        this.wateringFrequency = wateringFrequency;
    }

    public String getSunlightRequirement() {
        return sunlightRequirement;
    }

    public void setSunlightRequirement(String sunlightRequirement) {
        this.sunlightRequirement = sunlightRequirement;
    }

    public float getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(float temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public float getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(float temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public String getGrowingSeason() {
        return growingSeason;
    }

    public void setGrowingSeason(String growingSeason) {
        this.growingSeason = growingSeason;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public float getPlantingDepth() {
        return plantingDepth;
    }

    public void setPlantingDepth(float plantingDepth) {
        this.plantingDepth = plantingDepth;
    }

    public int getDaysToGermination() {
        return daysToGermination;
    }

    public void setDaysToGermination(int daysToGermination) {
        this.daysToGermination = daysToGermination;
    }

    public int getDaysToHarvest() {
        return daysToHarvest;
    }

    public void setDaysToHarvest(int daysToHarvest) {
        this.daysToHarvest = daysToHarvest;
    }

    public boolean isInUserGarden() {
        return isInUserGarden;
    }

    public void setInUserGarden(boolean inUserGarden) {
        isInUserGarden = inUserGarden;
    }

    public long getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(long plantingDate) {
        this.plantingDate = plantingDate;
    }
    
    public boolean isBookmarked() {
        return isBookmarked;
    }
    
    public void setBookmarked(boolean bookmarked) {
        this.isBookmarked = bookmarked;
    }
}