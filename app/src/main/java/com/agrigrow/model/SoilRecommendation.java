package com.agrigrow.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a soil recommendation linked to a soil test
 */
@Entity(tableName = "soil_recommendations",
        foreignKeys = @ForeignKey(
                entity = SoilTest.class,
                parentColumns = "id",
                childColumns = "soilTestId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("soilTestId")})
public class SoilRecommendation {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long soilTestId;
    private String category;
    private String title;
    private String description;
    private String actionItems;
    private int priority;  // 1-3, where 1 is highest priority
    private String productSuggestions;
    private String improvementTimeframe;
    private boolean isDismissed;
    private boolean isCompleted;

    // Constructors
    public SoilRecommendation() {
        // Required empty constructor for Room
    }

    public SoilRecommendation(long soilTestId, String category, String title, 
                             String description, String actionItems, int priority) {
        this.soilTestId = soilTestId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.actionItems = actionItems;
        this.priority = priority;
        this.isDismissed = false;
        this.isCompleted = false;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSoilTestId() {
        return soilTestId;
    }

    public void setSoilTestId(long soilTestId) {
        this.soilTestId = soilTestId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getActionItems() {
        return actionItems;
    }

    public void setActionItems(String actionItems) {
        this.actionItems = actionItems;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getProductSuggestions() {
        return productSuggestions;
    }

    public void setProductSuggestions(String productSuggestions) {
        this.productSuggestions = productSuggestions;
    }

    public String getImprovementTimeframe() {
        return improvementTimeframe;
    }

    public void setImprovementTimeframe(String improvementTimeframe) {
        this.improvementTimeframe = improvementTimeframe;
    }

    public boolean isDismissed() {
        return isDismissed;
    }

    public void setDismissed(boolean dismissed) {
        isDismissed = dismissed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    
    /**
     * Get a color code based on priority
     * @return Hex color code
     */
    public String getPriorityColor() {
        switch (priority) {
            case 1:
                return "#F44336"; // Red for high priority
            case 2:
                return "#FF9800"; // Orange for medium priority
            case 3:
                return "#4CAF50"; // Green for low priority
            default:
                return "#2196F3"; // Blue for undefined priority
        }
    }
    
    /**
     * Get a readable priority text
     * @return Text representation of priority
     */
    public String getPriorityText() {
        switch (priority) {
            case 1:
                return "High";
            case 2:
                return "Medium";
            case 3:
                return "Low";
            default:
                return "Undefined";
        }
    }
}