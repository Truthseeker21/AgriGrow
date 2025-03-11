package com.agrigrow.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Entity class representing a plant in the AgriGrow application.
 * This class contains information about plants for urban gardening.
 */
@Entity(tableName = "plants")
public class Plant {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "name")
    @NonNull
    private String name;
    
    @ColumnInfo(name = "description")
    private String description;
    
    @ColumnInfo(name = "category")
    private String category;
    
    @ColumnInfo(name = "image_url")
    private String imageUrl;
    
    @ColumnInfo(name = "planting_instructions")
    private String plantingInstructions;
    
    @ColumnInfo(name = "care_instructions")
    private String careInstructions;
    
    @ColumnInfo(name = "sunlight_requirement")
    private String sunlightRequirement;
    
    @ColumnInfo(name = "watering_frequency")
    private int wateringFrequency; // 1 = Low, 2 = Medium, 3 = High
    
    @ColumnInfo(name = "difficulty_level")
    private int difficultyLevel; // 1 = Easy, 2 = Moderate, 3 = Hard
    
    @ColumnInfo(name = "growing_season")
    private String growingSeason;
    
    @ColumnInfo(name = "time_to_harvest")
    private int timeToHarvest; // in days
    
    @ColumnInfo(name = "is_bookmarked")
    private boolean isBookmarked;
    
    @ColumnInfo(name = "is_in_garden")
    private boolean isInGarden;
    
    @ColumnInfo(name = "planting_date")
    private long plantingDate;
    
    /**
     * Default constructor required by Room
     */
    public Plant() {
    }
    
    /**
     * Minimal constructor with required fields
     * @param name The plant name
     */
    public Plant(@NonNull String name) {
        this.name = name;
        this.isBookmarked = false;
        this.isInGarden = false;
    }
    
    /**
     * Constructor with basic information
     * @param name The plant name
     * @param description Plant description
     * @param category Plant category (e.g., "Vegetables", "Herbs", "Flowers")
     */
    public Plant(@NonNull String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.isBookmarked = false;
        this.isInGarden = false;
    }
    
    /**
     * Full constructor with all fields
     * @param id The plant ID
     * @param name The plant name
     * @param description Plant description
     * @param category Plant category
     * @param imageUrl URL to the plant image
     * @param plantingInstructions Instructions for planting
     * @param careInstructions Instructions for caring
     * @param sunlightRequirement Amount of sunlight needed
     * @param wateringFrequency How often to water (1-3)
     * @param difficultyLevel How difficult to grow (1-3)
     * @param growingSeason Season when the plant grows best
     * @param timeToHarvest Days until harvest
     * @param isBookmarked Whether the plant is bookmarked
     * @param isInGarden Whether the plant is in the user's garden
     * @param plantingDate When the plant was/will be planted
     */
    public Plant(int id, @NonNull String name, String description, String category,
               String imageUrl, String plantingInstructions, String careInstructions,
               String sunlightRequirement, int wateringFrequency, int difficultyLevel,
               String growingSeason, int timeToHarvest, boolean isBookmarked,
               boolean isInGarden, long plantingDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.plantingInstructions = plantingInstructions;
        this.careInstructions = careInstructions;
        this.sunlightRequirement = sunlightRequirement;
        this.wateringFrequency = wateringFrequency;
        this.difficultyLevel = difficultyLevel;
        this.growingSeason = growingSeason;
        this.timeToHarvest = timeToHarvest;
        this.isBookmarked = isBookmarked;
        this.isInGarden = isInGarden;
        this.plantingDate = plantingDate;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @NonNull
    public String getName() {
        return name;
    }
    
    public void setName(@NonNull String name) {
        this.name = name;
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
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getPlantingInstructions() {
        return plantingInstructions;
    }
    
    public void setPlantingInstructions(String plantingInstructions) {
        this.plantingInstructions = plantingInstructions;
    }
    
    public String getCareInstructions() {
        return careInstructions;
    }
    
    public void setCareInstructions(String careInstructions) {
        this.careInstructions = careInstructions;
    }
    
    public String getSunlightRequirement() {
        return sunlightRequirement;
    }
    
    public void setSunlightRequirement(String sunlightRequirement) {
        this.sunlightRequirement = sunlightRequirement;
    }
    
    public int getWateringFrequency() {
        return wateringFrequency;
    }
    
    public void setWateringFrequency(int wateringFrequency) {
        this.wateringFrequency = wateringFrequency;
    }
    
    public int getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    public String getGrowingSeason() {
        return growingSeason;
    }
    
    public void setGrowingSeason(String growingSeason) {
        this.growingSeason = growingSeason;
    }
    
    public int getTimeToHarvest() {
        return timeToHarvest;
    }
    
    public void setTimeToHarvest(int timeToHarvest) {
        this.timeToHarvest = timeToHarvest;
    }
    
    public boolean isBookmarked() {
        return isBookmarked;
    }
    
    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
    
    public boolean isInGarden() {
        return isInGarden;
    }
    
    public void setInGarden(boolean inGarden) {
        isInGarden = inGarden;
    }
    
    public long getPlantingDate() {
        return plantingDate;
    }
    
    public void setPlantingDate(long plantingDate) {
        this.plantingDate = plantingDate;
    }
    
    /**
     * Get the difficulty level as a user-friendly string
     * @return String representation of difficulty
     */
    public String getDifficultyText() {
        switch (difficultyLevel) {
            case 1:
                return "Easy";
            case 2:
                return "Moderate";
            case 3:
                return "Difficult";
            default:
                return "Unknown";
        }
    }
    
    /**
     * Get the watering frequency as a user-friendly string
     * @return String representation of watering frequency
     */
    public String getWateringText() {
        switch (wateringFrequency) {
            case 1:
                return "Low";
            case 2:
                return "Medium";
            case 3:
                return "High";
            default:
                return "Unknown";
        }
    }
    
    /**
     * Get a short description of the planting schedule
     * @return Planting schedule description
     */
    public String getPlantingSchedule() {
        if (growingSeason == null || growingSeason.isEmpty()) {
            return "Can be planted year-round";
        }
        return "Best planted in " + growingSeason;
    }
    
    /**
     * Check if a plant matches the current season
     * @param currentSeason The current season ("Spring", "Summer", "Fall", "Winter")
     * @return true if the plant is suitable for the current season
     */
    public boolean isInSeason(String currentSeason) {
        if (growingSeason == null || growingSeason.isEmpty()) {
            return true; // Assume year-round if not specified
        }
        return growingSeason.contains(currentSeason);
    }
    
    /**
     * Calculate the days remaining until harvest based on planting date
     * @return Days remaining until harvest, or -1 if not planted
     */
    public int getDaysUntilHarvest() {
        if (plantingDate == 0 || !isInGarden) {
            return -1; // Not planted
        }
        
        long now = System.currentTimeMillis();
        long daysElapsed = (now - plantingDate) / (1000 * 60 * 60 * 24);
        
        int daysRemaining = timeToHarvest - (int)daysElapsed;
        return Math.max(0, daysRemaining);
    }
    
    /**
     * Get a progress percentage towards harvest
     * @return Percentage progress (0-100)
     */
    public int getHarvestProgressPercentage() {
        if (plantingDate == 0 || !isInGarden || timeToHarvest <= 0) {
            return 0;
        }
        
        long now = System.currentTimeMillis();
        long daysElapsed = (now - plantingDate) / (1000 * 60 * 60 * 24);
        
        int progress = (int)((daysElapsed / (float)timeToHarvest) * 100);
        return Math.min(100, Math.max(0, progress));
    }
}