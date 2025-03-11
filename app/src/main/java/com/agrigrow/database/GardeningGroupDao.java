package com.agrigrow.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.agrigrow.model.GardeningGroup;

import java.util.List;

/**
 * Data Access Object for Gardening Groups
 */
@Dao
public interface GardeningGroupDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(GardeningGroup group);
    
    @Update
    void update(GardeningGroup group);
    
    @Delete
    void delete(GardeningGroup group);
    
    @Query("SELECT * FROM gardening_groups ORDER BY name ASC")
    LiveData<List<GardeningGroup>> getAllGroups();
    
    @Query("SELECT * FROM gardening_groups WHERE id = :id")
    LiveData<GardeningGroup> getGroupById(long id);
    
    @Query("SELECT * FROM gardening_groups WHERE isJoined = 1 ORDER BY name ASC")
    LiveData<List<GardeningGroup>> getJoinedGroups();
    
    @Query("SELECT * FROM gardening_groups WHERE category = :category ORDER BY name ASC")
    LiveData<List<GardeningGroup>> getGroupsByCategory(String category);
    
    @Query("SELECT * FROM gardening_groups WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%'")
    LiveData<List<GardeningGroup>> searchGroups(String query);
    
    @Query("UPDATE gardening_groups SET isJoined = :joined WHERE id = :groupId")
    void updateJoinStatus(long groupId, boolean joined);
    
    @Query("SELECT COUNT(*) FROM gardening_groups WHERE isJoined = 1")
    LiveData<Integer> getJoinedGroupCount();
    
    @Query("SELECT * FROM gardening_groups ORDER BY memberCount DESC LIMIT 5")
    LiveData<List<GardeningGroup>> getPopularGroups();
    
    @Query("SELECT * FROM gardening_groups WHERE " +
            "((latitude BETWEEN :minLat AND :maxLat) AND " +
            "(longitude BETWEEN :minLong AND :maxLong))")
    LiveData<List<GardeningGroup>> getNearbyGroups(double minLat, double maxLat, double minLong, double maxLong);
}