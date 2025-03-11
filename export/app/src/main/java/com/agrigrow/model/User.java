package com.agrigrow.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Entity class representing a user in the AgriGrow application.
 * This class contains user profile information and gardening preferences.
 */
@Entity(tableName = "users")
public class User {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "name")
    @NonNull
    private String name;
    
    @ColumnInfo(name = "email")
    @NonNull
    private String email;
    
    @ColumnInfo(name = "profile_image_url")
    private String profileImageUrl;
    
    @ColumnInfo(name = "bio")
    private String bio;
    
    @ColumnInfo(name = "location")
    private String location;
    
    @ColumnInfo(name = "gardening_level")
    private int gardeningLevel;
    
    @ColumnInfo(name = "experience_points")
    private int experiencePoints;
    
    @ColumnInfo(name = "joined_date")
    private long joinedDate;
    
    @ColumnInfo(name = "climate_zone")
    private String climateZone;
    
    @ColumnInfo(name = "gardening_style")
    private String gardeningStyle;
    
    /**
     * Default constructor required by Room
     */
    public User() {
    }
    
    /**
     * Minimal constructor with required fields
     * @param name The user's name
     * @param email The user's email
     */
    public User(@NonNull String name, @NonNull String email) {
        this.name = name;
        this.email = email;
        this.gardeningLevel = 1;
        this.experiencePoints = 0;
        this.joinedDate = System.currentTimeMillis();
    }
    
    /**
     * Full constructor with all fields
     * @param id The user ID
     * @param name The user's name
     * @param email The user's email
     * @param profileImageUrl URL to user's profile image
     * @param bio User's bio/about me text
     * @param location User's location
     * @param gardeningLevel User's gardening experience level (1-10)
     * @param experiencePoints User's experience points
     * @param joinedDate When the user joined the app
     * @param climateZone User's climate zone
     * @param gardeningStyle User's preferred gardening style
     */
    public User(int id, @NonNull String name, @NonNull String email, 
               String profileImageUrl, String bio, String location,
               int gardeningLevel, int experiencePoints, long joinedDate,
               String climateZone, String gardeningStyle) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.location = location;
        this.gardeningLevel = gardeningLevel;
        this.experiencePoints = experiencePoints;
        this.joinedDate = joinedDate;
        this.climateZone = climateZone;
        this.gardeningStyle = gardeningStyle;
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
    
    @NonNull
    public String getEmail() {
        return email;
    }
    
    public void setEmail(@NonNull String email) {
        this.email = email;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public int getGardeningLevel() {
        return gardeningLevel;
    }
    
    public void setGardeningLevel(int gardeningLevel) {
        this.gardeningLevel = gardeningLevel;
    }
    
    public int getExperiencePoints() {
        return experiencePoints;
    }
    
    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }
    
    public long getJoinedDate() {
        return joinedDate;
    }
    
    public void setJoinedDate(long joinedDate) {
        this.joinedDate = joinedDate;
    }
    
    public String getClimateZone() {
        return climateZone;
    }
    
    public void setClimateZone(String climateZone) {
        this.climateZone = climateZone;
    }
    
    public String getGardeningStyle() {
        return gardeningStyle;
    }
    
    public void setGardeningStyle(String gardeningStyle) {
        this.gardeningStyle = gardeningStyle;
    }
    
    /**
     * Add experience points and potentially level up the user
     * @param points Number of points to add
     * @return true if user leveled up, false otherwise
     */
    public boolean addExperiencePoints(int points) {
        this.experiencePoints += points;
        
        // Check if user should level up
        int newLevel = calculateLevelFromExperience();
        if (newLevel > gardeningLevel) {
            gardeningLevel = newLevel;
            return true;
        }
        
        return false;
    }
    
    /**
     * Calculate the user level based on experience points
     * Level formula: level = 1 + Math.floor(sqrt(exp / 100))
     * @return The calculated level
     */
    private int calculateLevelFromExperience() {
        return 1 + (int) Math.floor(Math.sqrt(experiencePoints / 100.0));
    }
    
    /**
     * Get experience points needed for the next level
     * @return Experience points needed for next level
     */
    public int getExperienceForNextLevel() {
        int nextLevel = gardeningLevel + 1;
        return (nextLevel * nextLevel) * 100;
    }
    
    /**
     * Get the progress percentage toward the next level
     * @return Progress percentage (0-100)
     */
    public int getLevelProgressPercentage() {
        int currentLevelExp = (gardeningLevel * gardeningLevel) * 100;
        int nextLevelExp = getExperienceForNextLevel();
        int levelDiff = nextLevelExp - currentLevelExp;
        int currentProgress = experiencePoints - currentLevelExp;
        
        return (int) ((currentProgress / (float) levelDiff) * 100);
    }
    
    /**
     * Get the user's gardening level as a descriptive string
     * @return String representation of gardening level
     */
    public String getGardeningLevelString() {
        switch (gardeningLevel) {
            case 1:
                return "Seedling";
            case 2:
                return "Sprout";
            case 3:
                return "Growing Gardener";
            case 4:
                return "Green Thumb";
            case 5:
                return "Garden Enthusiast";
            case 6:
                return "Garden Maestro";
            case 7:
                return "Plant Whisperer";
            case 8:
                return "Gardening Guru";
            case 9:
                return "Master Gardener";
            case 10:
                return "Gardening Legend";
            default:
                if (gardeningLevel > 10) {
                    return "Gardening Expert";
                }
                return "Beginner";
        }
    }
}