package com.agrigrow.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.agrigrow.model.GardeningTechnique;

import java.util.List;

/**
 * Data Access Object for gardening techniques
 */
@Dao
public interface GardeningTechniqueDao {
    
    @Query("SELECT * FROM gardening_techniques")
    List<GardeningTechnique> getAllTechniques();
    
    @Query("SELECT * FROM gardening_techniques WHERE category = :category")
    List<GardeningTechnique> getTechniquesByCategory(String category);
    
    @Query("SELECT * FROM gardening_techniques WHERE difficulty = :difficulty")
    List<GardeningTechnique> getTechniquesByDifficulty(String difficulty);
    
    @Query("SELECT * FROM gardening_techniques WHERE id = :id")
    GardeningTechnique getTechniqueById(int id);
    
    @Insert
    void insertTechnique(GardeningTechnique technique);
    
    @Insert
    void insertAllTechniques(List<GardeningTechnique> techniques);
    
    @Update
    void updateTechnique(GardeningTechnique technique);
    
    @Delete
    void deleteTechnique(GardeningTechnique technique);
    
    @Query("DELETE FROM gardening_techniques")
    void deleteAllTechniques();
    
    @Query("SELECT COUNT(*) FROM gardening_techniques")
    int getTechniqueCount();
}