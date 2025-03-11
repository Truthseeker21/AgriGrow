package com.agrigrow.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Model class for gardening tips
 */
@Entity(tableName = "garden_tips")
public class GardenTip {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @NonNull
    @ColumnInfo(name = "tip")
    private String tip;
    
    @ColumnInfo(name = "category")
    private String category;
    
    @ColumnInfo(name = "source")
    private String source;
    
    /**
     * Constructor with only tip
     * @param tip The gardening tip text
     */
    public GardenTip(@NonNull String tip) {
        this.tip = tip;
        this.category = "General";
        this.source = "AgriGrow";
    }
    
    /**
     * Full constructor
     * @param tip The gardening tip text
     * @param category The category of the tip
     * @param source The source of the tip
     */
    public GardenTip(@NonNull String tip, String category, String source) {
        this.tip = tip;
        this.category = category;
        this.source = source;
    }
    
    // Getters and setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @NonNull
    public String getTip() {
        return tip;
    }
    
    public void setTip(@NonNull String tip) {
        this.tip = tip;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
}