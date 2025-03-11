package com.agrigrow.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Entity representing a reward earned by a user
 */
@Entity(tableName = "user_rewards",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("userId")}
)
public class UserReward {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private long userId;
    private String rewardType; // "BADGE", "CHALLENGE", "LEVEL_UP", "POINTS"
    private long rewardReferenceId; // Badge ID or Challenge ID if applicable
    private String rewardName;
    private String rewardDescription;
    private int pointsEarned;
    private Date dateEarned;
    private boolean isRedeemed;
    private Date redeemedDate;
    private String rewardImagePath;
    private boolean isFeatured;
    
    // Getters and Setters
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public long getRewardReferenceId() {
        return rewardReferenceId;
    }

    public void setRewardReferenceId(long rewardReferenceId) {
        this.rewardReferenceId = rewardReferenceId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardDescription() {
        return rewardDescription;
    }

    public void setRewardDescription(String rewardDescription) {
        this.rewardDescription = rewardDescription;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public Date getDateEarned() {
        return dateEarned;
    }

    public void setDateEarned(Date dateEarned) {
        this.dateEarned = dateEarned;
    }

    public boolean isRedeemed() {
        return isRedeemed;
    }

    public void setRedeemed(boolean redeemed) {
        isRedeemed = redeemed;
    }

    public Date getRedeemedDate() {
        return redeemedDate;
    }

    public void setRedeemedDate(Date redeemedDate) {
        this.redeemedDate = redeemedDate;
    }

    public String getRewardImagePath() {
        return rewardImagePath;
    }

    public void setRewardImagePath(String rewardImagePath) {
        this.rewardImagePath = rewardImagePath;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }
}