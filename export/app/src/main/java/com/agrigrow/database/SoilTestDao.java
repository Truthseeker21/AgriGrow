package com.agrigrow.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.agrigrow.model.SoilRecommendation;
import com.agrigrow.model.SoilTest;

import java.util.List;
import java.util.Date;

/**
 * Data Access Object for soil test and recommendation operations
 */
@Dao
public interface SoilTestDao {

    // Soil Test operations
    @Insert
    long insertSoilTest(SoilTest soilTest);

    @Update
    void updateSoilTest(SoilTest soilTest);

    @Delete
    void deleteSoilTest(SoilTest soilTest);

    @Query("SELECT * FROM soil_tests ORDER BY testDate DESC")
    LiveData<List<SoilTest>> getAllSoilTests();

    @Query("SELECT * FROM soil_tests WHERE id = :id")
    LiveData<SoilTest> getSoilTestById(long id);

    @Query("SELECT * FROM soil_tests WHERE id = :id")
    SoilTest getSoilTestByIdSync(long id);

    @Query("SELECT * FROM soil_tests ORDER BY testDate DESC LIMIT :limit")
    LiveData<List<SoilTest>> getRecentSoilTests(int limit);
    
    @Query("SELECT COUNT(*) FROM soil_tests")
    LiveData<Integer> getSoilTestCount();
    
    @Query("SELECT * FROM soil_tests WHERE phLevel BETWEEN :minPh AND :maxPh ORDER BY testDate DESC")
    LiveData<List<SoilTest>> getSoilTestsByPhRange(double minPh, double maxPh);
    
    @Query("SELECT * FROM soil_tests WHERE testDate BETWEEN :startDate AND :endDate ORDER BY testDate DESC")
    LiveData<List<SoilTest>> getSoilTestsByDateRange(Date startDate, Date endDate);
    
    @Query("SELECT * FROM soil_tests WHERE soilType = :soilType ORDER BY testDate DESC")
    LiveData<List<SoilTest>> getSoilTestsBySoilType(String soilType);
    
    @Query("SELECT * FROM soil_tests WHERE name LIKE '%' || :searchTerm || '%' OR location LIKE '%' || :searchTerm || '%' OR notes LIKE '%' || :searchTerm || '%' ORDER BY testDate DESC")
    LiveData<List<SoilTest>> searchSoilTests(String searchTerm);
    
    @Query("SELECT AVG(phLevel) FROM soil_tests")
    LiveData<Double> getAveragePhLevel();
    
    @Query("SELECT AVG(nitrogenLevel) FROM soil_tests")
    LiveData<Double> getAverageNitrogenLevel();
    
    @Query("SELECT AVG(phosphorusLevel) FROM soil_tests")
    LiveData<Double> getAveragePhosphorusLevel();
    
    @Query("SELECT AVG(potassiumLevel) FROM soil_tests")
    LiveData<Double> getAveragePotassiumLevel();
    
    @Query("SELECT AVG(organicMatterPercentage) FROM soil_tests")
    LiveData<Double> getAverageOrganicMatter();
    
    @Query("SELECT AVG(soilHealthScore) FROM soil_tests")
    LiveData<Integer> getAverageHealthScore();
    
    // Soil Recommendation operations
    @Insert
    long insertSoilRecommendation(SoilRecommendation recommendation);
    
    @Insert
    void insertSoilRecommendations(List<SoilRecommendation> recommendations);

    @Update
    void updateSoilRecommendation(SoilRecommendation recommendation);

    @Delete
    void deleteSoilRecommendation(SoilRecommendation recommendation);

    @Query("SELECT * FROM soil_recommendations WHERE soilTestId = :soilTestId ORDER BY priority ASC")
    LiveData<List<SoilRecommendation>> getRecommendationsForSoilTest(long soilTestId);
    
    @Query("SELECT * FROM soil_recommendations WHERE isCompleted = 0 AND isDismissed = 0 ORDER BY priority ASC")
    LiveData<List<SoilRecommendation>> getActiveRecommendations();
    
    @Query("SELECT * FROM soil_recommendations WHERE category = :category AND isCompleted = 0 AND isDismissed = 0 ORDER BY priority ASC")
    LiveData<List<SoilRecommendation>> getActiveRecommendationsByCategory(String category);
    
    @Query("UPDATE soil_recommendations SET isCompleted = 1 WHERE id = :id")
    void markRecommendationAsComplete(long id);
    
    @Query("UPDATE soil_recommendations SET isDismissed = 1 WHERE id = :id")
    void dismissRecommendation(long id);
    
    @Query("DELETE FROM soil_recommendations WHERE soilTestId = :soilTestId")
    void deleteRecommendationsForSoilTest(long soilTestId);
    
    @Transaction
    default void deleteSoilTestWithRecommendations(SoilTest soilTest) {
        deleteRecommendationsForSoilTest(soilTest.getId());
        deleteSoilTest(soilTest);
    }
    
    @Transaction
    default void updateSoilTestWithRecommendations(SoilTest soilTest, List<SoilRecommendation> recommendations) {
        updateSoilTest(soilTest);
        deleteRecommendationsForSoilTest(soilTest.getId());
        for (SoilRecommendation recommendation : recommendations) {
            recommendation.setSoilTestId(soilTest.getId());
            insertSoilRecommendation(recommendation);
        }
    }
}