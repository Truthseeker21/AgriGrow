package com.agrigrow.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Model class for gardening journal entries
 */
@Entity(tableName = "journal_entries")
public class JournalEntry {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    
    @ColumnInfo(name = "description")
    private String description;
    
    @ColumnInfo(name = "date")
    private Date date;
    
    @ColumnInfo(name = "image_path")
    private String imagePath;
    
    @ColumnInfo(name = "plant_id")
    private int plantId; // ID of the associated plant, if any
    
    @ColumnInfo(name = "entry_type")
    private String entryType; // e.g., "planting", "harvest", "observation", "treatment", etc.
    
    @ColumnInfo(name = "weather_conditions")
    private String weatherConditions;
    
    @ColumnInfo(name = "temperature")
    private float temperature;
    
    @ColumnInfo(name = "mood_rating")
    private int moodRating; // 1-5 scale for how the user feels about their garden that day
    
    @ColumnInfo(name = "reminder_date")
    private Date reminderDate; // Optional date for a follow-up reminder
    
    /**
     * Simple constructor with required fields
     * @param title Entry title
     * @param date Date of the entry
     */
    public JournalEntry(@NonNull String title, Date date) {
        this.title = title;
        this.date = date;
        this.entryType = "observation";
        this.moodRating = 3; // Neutral by default
    }
    
    /**
     * Full constructor with all fields
     * @param title Entry title
     * @param description Entry description
     * @param date Entry date
     * @param imagePath Path to attached image
     * @param plantId Associated plant ID
     * @param entryType Type of entry
     * @param weatherConditions Weather conditions
     * @param temperature Temperature
     * @param moodRating Mood rating (1-5)
     * @param reminderDate Follow-up reminder date
     */
    public JournalEntry(@NonNull String title, String description, Date date, 
                       String imagePath, int plantId, String entryType,
                       String weatherConditions, float temperature, 
                       int moodRating, Date reminderDate) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imagePath = imagePath;
        this.plantId = plantId;
        this.entryType = entryType;
        this.weatherConditions = weatherConditions;
        this.temperature = temperature;
        this.moodRating = moodRating;
        this.reminderDate = reminderDate;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public int getPlantId() {
        return plantId;
    }
    
    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }
    
    public String getEntryType() {
        return entryType;
    }
    
    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }
    
    public String getWeatherConditions() {
        return weatherConditions;
    }
    
    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }
    
    public float getTemperature() {
        return temperature;
    }
    
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
    
    public int getMoodRating() {
        return moodRating;
    }
    
    public void setMoodRating(int moodRating) {
        this.moodRating = moodRating;
    }
    
    public Date getReminderDate() {
        return reminderDate;
    }
    
    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }
}