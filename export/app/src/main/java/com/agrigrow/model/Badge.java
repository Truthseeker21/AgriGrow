package com.agrigrow.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity representing a badge or certificate that can be earned
 */
@Entity(tableName = "badges")
public class Badge {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String name;
    private String description;
    private String imagePath;
    private String category;
    private int levelRequired;
    private String achievementCriteria;
    private boolean isUnlocked;
    private String unlockedDate;
    private int pointsAwarded;
    private boolean isCertificate; // true for certificates, false for badges
    
    // Getters and Setters
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public void setLevelRequired(int levelRequired) {
        this.levelRequired = levelRequired;
    }

    public String getAchievementCriteria() {
        return achievementCriteria;
    }

    public void setAchievementCriteria(String achievementCriteria) {
        this.achievementCriteria = achievementCriteria;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public String getUnlockedDate() {
        return unlockedDate;
    }

    public void setUnlockedDate(String unlockedDate) {
        this.unlockedDate = unlockedDate;
    }

    public int getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(int pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public boolean isCertificate() {
        return isCertificate;
    }

    public void setCertificate(boolean certificate) {
        isCertificate = certificate;
    }
}