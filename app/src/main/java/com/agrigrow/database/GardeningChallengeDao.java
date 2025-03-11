package com.agrigrow.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.agrigrow.model.GardeningChallenge;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object for Gardening Challenges
 */
@Dao
public interface GardeningChallengeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertChallenge(GardeningChallenge challenge);
    
    @Update
    void updateChallenge(GardeningChallenge challenge);
    
    @Delete
    void deleteChallenge(GardeningChallenge challenge);
    
    @Query("SELECT * FROM gardening_challenges WHERE id = :challengeId")
    LiveData<GardeningChallenge> getChallengeById(long challengeId);
    
    @Query("SELECT * FROM gardening_challenges ORDER BY startDate DESC")
    LiveData<List<GardeningChallenge>> getAllChallenges();
    
    @Query("SELECT * FROM gardening_challenges WHERE isActive = 1 ORDER BY startDate DESC")
    LiveData<List<GardeningChallenge>> getActiveChallenges();
    
    @Query("SELECT * FROM gardening_challenges WHERE isCompleted = 1 ORDER BY completedDate DESC")
    LiveData<List<GardeningChallenge>> getCompletedChallenges();
    
    @Query("SELECT * FROM gardening_challenges WHERE category = :category ORDER BY startDate DESC")
    LiveData<List<GardeningChallenge>> getChallengesByCategory(String category);
    
    @Query("SELECT * FROM gardening_challenges WHERE difficulty = :difficulty ORDER BY startDate DESC")
    LiveData<List<GardeningChallenge>> getChallengesByDifficulty(String difficulty);
    
    @Query("SELECT * FROM gardening_challenges WHERE startDate > :currentDate AND isActive = 1 ORDER BY startDate ASC LIMIT 5")
    LiveData<List<GardeningChallenge>> getUpcomingChallenges(Date currentDate);
    
    @Query("SELECT * FROM gardening_challenges WHERE isActive = 1 AND endDate < :currentDate ORDER BY endDate ASC")
    LiveData<List<GardeningChallenge>> getEndingSoonChallenges(Date currentDate);
    
    @Query("SELECT * FROM gardening_challenges WHERE sponsorName = :sponsorName ORDER BY startDate DESC")
    LiveData<List<GardeningChallenge>> getChallengesBySponsor(String sponsorName);
    
    @Query("SELECT * FROM gardening_challenges WHERE pointsValue >= :minPoints ORDER BY pointsValue DESC")
    LiveData<List<GardeningChallenge>> getChallengesByMinimumPoints(int minPoints);
    
    @Query("UPDATE gardening_challenges SET isCompleted = 1, completedDate = :completionDate WHERE id = :challengeId")
    void markChallengeAsCompleted(long challengeId, Date completionDate);
    
    @Query("UPDATE gardening_challenges SET participantCount = participantCount + 1 WHERE id = :challengeId")
    void incrementParticipantCount(long challengeId);
    
    @Query("SELECT SUM(pointsValue) FROM gardening_challenges WHERE isCompleted = 1")
    LiveData<Integer> getTotalPointsEarned();
    
    @Query("SELECT * FROM gardening_challenges WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    LiveData<List<GardeningChallenge>> searchChallenges(String query);
    
    @Query("SELECT COUNT(*) FROM gardening_challenges WHERE isCompleted = 1")
    LiveData<Integer> getCompletedChallengesCount();
}