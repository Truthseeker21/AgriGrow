package com.agrigrow.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.agrigrow.model.UserReward;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object for User Rewards
 */
@Dao
public interface UserRewardDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUserReward(UserReward userReward);
    
    @Update
    void updateUserReward(UserReward userReward);
    
    @Delete
    void deleteUserReward(UserReward userReward);
    
    @Query("SELECT * FROM user_rewards WHERE id = :rewardId")
    LiveData<UserReward> getUserRewardById(long rewardId);
    
    @Query("SELECT * FROM user_rewards WHERE userId = :userId ORDER BY dateEarned DESC")
    LiveData<List<UserReward>> getUserRewardsByUserId(long userId);
    
    @Query("SELECT * FROM user_rewards WHERE userId = :userId AND rewardType = :rewardType ORDER BY dateEarned DESC")
    LiveData<List<UserReward>> getUserRewardsByType(long userId, String rewardType);
    
    @Query("SELECT * FROM user_rewards WHERE userId = :userId AND isRedeemed = 0 ORDER BY dateEarned DESC")
    LiveData<List<UserReward>> getUnredeemedRewards(long userId);
    
    @Query("SELECT * FROM user_rewards WHERE userId = :userId AND isRedeemed = 1 ORDER BY redeemedDate DESC")
    LiveData<List<UserReward>> getRedeemedRewards(long userId);
    
    @Query("SELECT SUM(pointsEarned) FROM user_rewards WHERE userId = :userId")
    LiveData<Integer> getTotalPointsEarned(long userId);
    
    @Query("SELECT * FROM user_rewards WHERE userId = :userId AND dateEarned >= :startDate ORDER BY dateEarned DESC")
    LiveData<List<UserReward>> getRewardsEarnedSince(long userId, Date startDate);
    
    @Query("SELECT * FROM user_rewards WHERE userId = :userId AND isFeatured = 1 ORDER BY dateEarned DESC")
    LiveData<List<UserReward>> getFeaturedRewards(long userId);
    
    @Query("UPDATE user_rewards SET isRedeemed = 1, redeemedDate = :redemptionDate WHERE id = :rewardId")
    void markRewardAsRedeemed(long rewardId, Date redemptionDate);
    
    @Query("UPDATE user_rewards SET isFeatured = :isFeatured WHERE id = :rewardId")
    void updateFeaturedStatus(long rewardId, boolean isFeatured);
    
    @Query("SELECT COUNT(*) FROM user_rewards WHERE userId = :userId AND rewardType = :rewardType")
    LiveData<Integer> getRewardCountByType(long userId, String rewardType);
    
    @Query("SELECT * FROM user_rewards WHERE userId = :userId ORDER BY dateEarned DESC LIMIT :limit")
    LiveData<List<UserReward>> getRecentRewards(long userId, int limit);
    
    @Query("SELECT * FROM user_rewards WHERE userId = :userId AND rewardName LIKE '%' || :query || '%' ORDER BY dateEarned DESC")
    LiveData<List<UserReward>> searchUserRewards(long userId, String query);
    
    @Query("SELECT * FROM user_rewards WHERE userId = :userId AND rewardReferenceId = :referenceId AND rewardType = :rewardType LIMIT 1")
    LiveData<UserReward> checkIfRewardExists(long userId, long referenceId, String rewardType);
}