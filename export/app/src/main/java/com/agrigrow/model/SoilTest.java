package com.agrigrow.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.agrigrow.util.DateConverter;

import java.util.Date;

/**
 * Entity class representing a soil test entry in the database
 */
@Entity(tableName = "soil_tests")
@TypeConverters(DateConverter.class)
public class SoilTest {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private Date testDate;
    private String location;
    private double latitude;
    private double longitude;
    private String notes;
    private String imagePath;

    // Soil composition details
    private double phLevel;
    private double nitrogenLevel;
    private double phosphorusLevel;
    private double potassiumLevel;
    private double organicMatterPercentage;
    private String soilType;
    private String soilColor;
    private String soilTexture;
    private double soilMoisture;

    // Additional properties
    private boolean hasRecommendations;
    private int soilHealthScore;

    // Constructors
    public SoilTest() {
        // Required empty constructor for Room
    }

    public SoilTest(String name, Date testDate, String location, double latitude, double longitude,
                   double phLevel, double nitrogenLevel, double phosphorusLevel, 
                   double potassiumLevel, double organicMatterPercentage) {
        this.name = name;
        this.testDate = testDate;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phLevel = phLevel;
        this.nitrogenLevel = nitrogenLevel;
        this.phosphorusLevel = phosphorusLevel;
        this.potassiumLevel = potassiumLevel;
        this.organicMatterPercentage = organicMatterPercentage;
        
        // Calculate the soil health score based on the provided data
        calculateSoilHealthScore();
    }

    // Calculate soil health score based on test values
    private void calculateSoilHealthScore() {
        int score = 0;
        
        // pH level (optimum: 6.0-7.0)
        if (phLevel >= 6.0 && phLevel <= 7.0) {
            score += 20;
        } else if (phLevel >= 5.5 && phLevel < 6.0 || phLevel > 7.0 && phLevel <= 7.5) {
            score += 15;
        } else if (phLevel >= 5.0 && phLevel < 5.5 || phLevel > 7.5 && phLevel <= 8.0) {
            score += 10;
        } else {
            score += 5;
        }
        
        // Nitrogen level (optimum: 0.15-0.25)
        if (nitrogenLevel >= 0.15 && nitrogenLevel <= 0.25) {
            score += 20;
        } else if (nitrogenLevel >= 0.1 && nitrogenLevel < 0.15 || nitrogenLevel > 0.25 && nitrogenLevel <= 0.3) {
            score += 15;
        } else if (nitrogenLevel >= 0.05 && nitrogenLevel < 0.1 || nitrogenLevel > 0.3 && nitrogenLevel <= 0.4) {
            score += 10;
        } else {
            score += 5;
        }
        
        // Phosphorus level (optimum: 25-50 ppm)
        if (phosphorusLevel >= 25 && phosphorusLevel <= 50) {
            score += 20;
        } else if (phosphorusLevel >= 15 && phosphorusLevel < 25 || phosphorusLevel > 50 && phosphorusLevel <= 60) {
            score += 15;
        } else if (phosphorusLevel >= 10 && phosphorusLevel < 15 || phosphorusLevel > 60 && phosphorusLevel <= 70) {
            score += 10;
        } else {
            score += 5;
        }
        
        // Potassium level (optimum: 170-250 ppm)
        if (potassiumLevel >= 170 && potassiumLevel <= 250) {
            score += 20;
        } else if (potassiumLevel >= 120 && potassiumLevel < 170 || potassiumLevel > 250 && potassiumLevel <= 300) {
            score += 15;
        } else if (potassiumLevel >= 80 && potassiumLevel < 120 || potassiumLevel > 300 && potassiumLevel <= 350) {
            score += 10;
        } else {
            score += 5;
        }
        
        // Organic matter (optimum: 4-6%)
        if (organicMatterPercentage >= 4 && organicMatterPercentage <= 6) {
            score += 20;
        } else if (organicMatterPercentage >= 3 && organicMatterPercentage < 4 || organicMatterPercentage > 6 && organicMatterPercentage <= 8) {
            score += 15;
        } else if (organicMatterPercentage >= 2 && organicMatterPercentage < 3 || organicMatterPercentage > 8 && organicMatterPercentage <= 10) {
            score += 10;
        } else {
            score += 5;
        }
        
        // Set the score
        this.soilHealthScore = score;
    }

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

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPhLevel() {
        return phLevel;
    }

    public void setPhLevel(double phLevel) {
        this.phLevel = phLevel;
        calculateSoilHealthScore();
    }

    public double getNitrogenLevel() {
        return nitrogenLevel;
    }

    public void setNitrogenLevel(double nitrogenLevel) {
        this.nitrogenLevel = nitrogenLevel;
        calculateSoilHealthScore();
    }

    public double getPhosphorusLevel() {
        return phosphorusLevel;
    }

    public void setPhosphorusLevel(double phosphorusLevel) {
        this.phosphorusLevel = phosphorusLevel;
        calculateSoilHealthScore();
    }

    public double getPotassiumLevel() {
        return potassiumLevel;
    }

    public void setPotassiumLevel(double potassiumLevel) {
        this.potassiumLevel = potassiumLevel;
        calculateSoilHealthScore();
    }

    public double getOrganicMatterPercentage() {
        return organicMatterPercentage;
    }

    public void setOrganicMatterPercentage(double organicMatterPercentage) {
        this.organicMatterPercentage = organicMatterPercentage;
        calculateSoilHealthScore();
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public String getSoilColor() {
        return soilColor;
    }

    public void setSoilColor(String soilColor) {
        this.soilColor = soilColor;
    }

    public String getSoilTexture() {
        return soilTexture;
    }

    public void setSoilTexture(String soilTexture) {
        this.soilTexture = soilTexture;
    }

    public double getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(double soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public boolean isHasRecommendations() {
        return hasRecommendations;
    }

    public void setHasRecommendations(boolean hasRecommendations) {
        this.hasRecommendations = hasRecommendations;
    }

    public int getSoilHealthScore() {
        return soilHealthScore;
    }
    
    // This method should only be called after manual calculation, or from Room
    public void setSoilHealthScore(int soilHealthScore) {
        this.soilHealthScore = soilHealthScore;
    }
    
    /**
     * Get a summary text of the soil test
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        
        // Add pH information
        summary.append("pH: ").append(String.format("%.1f", phLevel));
        
        // Add NPK values
        summary.append(", NPK: ").append(String.format("%.1f-%.1f-%.1f", 
                nitrogenLevel, phosphorusLevel, potassiumLevel));
        
        // Add organic matter
        summary.append(", OM: ").append(String.format("%.1f%%", organicMatterPercentage));
        
        return summary.toString();
    }
    
    /**
     * Get a human-readable health status
     */
    public String getHealthStatus() {
        if (soilHealthScore >= 80) {
            return "Excellent";
        } else if (soilHealthScore >= 60) {
            return "Good";
        } else if (soilHealthScore >= 40) {
            return "Fair";
        } else {
            return "Poor";
        }
    }
}