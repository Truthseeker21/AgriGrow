package com.agrigrow.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.agrigrow.model.Badge;

import java.util.List;

/**
 * Data Access Object for Badges and Certificates
 */
@Dao
public interface BadgeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBadge(Badge badge);
    
    @Update
    void updateBadge(Badge badge);
    
    @Delete
    void deleteBadge(Badge badge);
    
    @Query("SELECT * FROM badges WHERE id = :badgeId")
    LiveData<Badge> getBadgeById(long badgeId);
    
    @Query("SELECT * FROM badges ORDER BY name ASC")
    LiveData<List<Badge>> getAllBadges();
    
    @Query("SELECT * FROM badges WHERE isUnlocked = 1 ORDER BY unlockedDate DESC")
    LiveData<List<Badge>> getUnlockedBadges();
    
    @Query("SELECT * FROM badges WHERE isUnlocked = 0 ORDER BY levelRequired ASC")
    LiveData<List<Badge>> getLockedBadges();
    
    @Query("SELECT * FROM badges WHERE category = :category ORDER BY levelRequired ASC")
    LiveData<List<Badge>> getBadgesByCategory(String category);
    
    @Query("SELECT * FROM badges WHERE levelRequired <= :userLevel AND isUnlocked = 0")
    LiveData<List<Badge>> getAvailableBadgesForLevel(int userLevel);
    
    @Query("SELECT * FROM badges WHERE isCertificate = 1 ORDER BY levelRequired ASC")
    LiveData<List<Badge>> getAllCertificates();
    
    @Query("SELECT * FROM badges WHERE isCertificate = 0 ORDER BY levelRequired ASC")
    LiveData<List<Badge>> getAllBadgesOnly();
    
    @Query("UPDATE badges SET isUnlocked = 1, unlockedDate = :currentDate WHERE id = :badgeId")
    void unlockBadge(long badgeId, String currentDate);
    
    @Query("SELECT COUNT(*) FROM badges WHERE isUnlocked = 1")
    LiveData<Integer> getUnlockedBadgesCount();
    
    @Query("SELECT SUM(pointsAwarded) FROM badges WHERE isUnlocked = 1")
    LiveData<Integer> getTotalBadgePoints();
    
    @Query("SELECT * FROM badges WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    LiveData<List<Badge>> searchBadges(String query);
    
    @Query("SELECT * FROM badges WHERE isUnlocked = 0 AND levelRequired = " +
           "(SELECT MIN(levelRequired) FROM badges WHERE isUnlocked = 0)")
    LiveData<List<Badge>> getNextAvailableBadges();
    
    @Query("SELECT * FROM badges WHERE isUnlocked = 1 ORDER BY unlockedDate DESC LIMIT 5")
    LiveData<List<Badge>> getRecentlyUnlockedBadges();
}