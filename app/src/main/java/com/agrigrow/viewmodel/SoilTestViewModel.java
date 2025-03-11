package com.agrigrow.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.agrigrow.database.AppDatabase;
import com.agrigrow.database.SoilTestDao;
import com.agrigrow.model.SoilRecommendation;
import com.agrigrow.model.SoilTest;
import com.agrigrow.util.LocationHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * ViewModel for soil testing and recommendation functionality
 */
public class SoilTestViewModel extends AndroidViewModel {

    private final SoilTestDao soilTestDao;
    private final Executor executor;
    private final LocationHelper locationHelper;
    
    // LiveData objects
    private final LiveData<List<SoilTest>> allSoilTests;
    private final LiveData<Integer> soilTestCount;
    private final LiveData<Double> averagePhLevel;
    private final LiveData<Double> averageNitrogenLevel;
    private final LiveData<Double> averagePhosphorusLevel;
    private final LiveData<Double> averagePotassiumLevel;
    private final LiveData<Double> averageOrganicMatter;
    private final LiveData<Integer> averageHealthScore;
    private final LiveData<List<SoilRecommendation>> activeRecommendations;
    
    // For storing current location
    private final MutableLiveData<Location> currentLocation = new MutableLiveData<>();
    private final MutableLiveData<String> currentAddress = new MutableLiveData<>();
    
    // For search functionality
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<List<SoilTest>> filteredSoilTests = new MutableLiveData<>(new ArrayList<>());

    public SoilTestViewModel(@NonNull Application application) {
        super(application);
        
        // Initialize database access
        AppDatabase database = AppDatabase.getInstance(application);
        soilTestDao = database.soilTestDao();
        
        // Initialize executor for background operations
        executor = Executors.newFixedThreadPool(4);
        
        // Initialize location helper
        locationHelper = new LocationHelper(application);
        
        // Initialize LiveData objects
        allSoilTests = soilTestDao.getAllSoilTests();
        soilTestCount = soilTestDao.getSoilTestCount();
        averagePhLevel = soilTestDao.getAveragePhLevel();
        averageNitrogenLevel = soilTestDao.getAverageNitrogenLevel();
        averagePhosphorusLevel = soilTestDao.getAveragePhosphorusLevel();
        averagePotassiumLevel = soilTestDao.getAveragePotassiumLevel();
        averageOrganicMatter = soilTestDao.getAverageOrganicMatter();
        averageHealthScore = soilTestDao.getAverageHealthScore();
        activeRecommendations = soilTestDao.getActiveRecommendations();
    }

    // Location methods
    public void getCurrentLocation() {
        locationHelper.getCurrentLocation(location -> {
            currentLocation.postValue(location);
            if (location != null) {
                locationHelper.getAddressFromLocation(location, address -> 
                    currentAddress.postValue(address));
            } else {
                currentAddress.postValue(null);
            }
        });
    }

    public LiveData<Location> getLocation() {
        return currentLocation;
    }

    public LiveData<String> getAddress() {
        return currentAddress;
    }

    // Soil Test CRUD operations
    public void insert(SoilTest soilTest) {
        executor.execute(() -> {
            long soilTestId = soilTestDao.insertSoilTest(soilTest);
            // Generate recommendations based on soil test data
            List<SoilRecommendation> recommendations = generateRecommendations(soilTest, soilTestId);
            soilTestDao.insertSoilRecommendations(recommendations);
            // Update the soil test to mark that it has recommendations
            if (!recommendations.isEmpty()) {
                soilTest.setId(soilTestId);
                soilTest.setHasRecommendations(true);
                soilTestDao.updateSoilTest(soilTest);
            }
        });
    }

    public void update(SoilTest soilTest) {
        executor.execute(() -> {
            soilTestDao.updateSoilTest(soilTest);
            
            // Optionally regenerate recommendations
            List<SoilRecommendation> recommendations = generateRecommendations(soilTest, soilTest.getId());
            soilTestDao.deleteRecommendationsForSoilTest(soilTest.getId());
            soilTestDao.insertSoilRecommendations(recommendations);
            
            // Update the soil test to mark that it has recommendations
            if (!recommendations.isEmpty()) {
                soilTest.setHasRecommendations(true);
                soilTestDao.updateSoilTest(soilTest);
            }
        });
    }

    public void delete(SoilTest soilTest) {
        executor.execute(() -> soilTestDao.deleteSoilTestWithRecommendations(soilTest));
    }
    
    public LiveData<SoilTest> getSoilTestById(long id) {
        return soilTestDao.getSoilTestById(id);
    }
    
    public void getSoilTestByIdAsync(long id, SoilTestCallback callback) {
        executor.execute(() -> {
            SoilTest soilTest = soilTestDao.getSoilTestByIdSync(id);
            callback.onSoilTestLoaded(soilTest);
        });
    }
    
    public interface SoilTestCallback {
        void onSoilTestLoaded(SoilTest soilTest);
    }
    
    // Recommendation operations
    public LiveData<List<SoilRecommendation>> getRecommendationsForSoilTest(long soilTestId) {
        return soilTestDao.getRecommendationsForSoilTest(soilTestId);
    }
    
    public void markRecommendationAsComplete(long id) {
        executor.execute(() -> soilTestDao.markRecommendationAsComplete(id));
    }
    
    public void dismissRecommendation(long id) {
        executor.execute(() -> soilTestDao.dismissRecommendation(id));
    }
    
    // Search and filter functionality
    public void searchSoilTests(String query) {
        searchQuery.setValue(query);
        executor.execute(() -> {
            List<SoilTest> results = soilTestDao.searchSoilTests(query).getValue();
            filteredSoilTests.postValue(results);
        });
    }
    
    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }
    
    public LiveData<List<SoilTest>> getFilteredSoilTests() {
        return filteredSoilTests;
    }
    
    // Analysis and statistics getters
    public LiveData<List<SoilTest>> getAllSoilTests() {
        return allSoilTests;
    }
    
    public LiveData<Integer> getSoilTestCount() {
        return soilTestCount;
    }
    
    public LiveData<Double> getAveragePhLevel() {
        return averagePhLevel;
    }
    
    public LiveData<Double> getAverageNitrogenLevel() {
        return averageNitrogenLevel;
    }
    
    public LiveData<Double> getAveragePhosphorusLevel() {
        return averagePhosphorusLevel;
    }
    
    public LiveData<Double> getAveragePotassiumLevel() {
        return averagePotassiumLevel;
    }
    
    public LiveData<Double> getAverageOrganicMatter() {
        return averageOrganicMatter;
    }
    
    public LiveData<Integer> getAverageHealthScore() {
        return averageHealthScore;
    }
    
    public LiveData<List<SoilRecommendation>> getActiveRecommendations() {
        return activeRecommendations;
    }
    
    // Helper method to generate recommendations based on soil test data
    private List<SoilRecommendation> generateRecommendations(SoilTest soilTest, long soilTestId) {
        List<SoilRecommendation> recommendations = new ArrayList<>();
        
        // Check pH level
        if (soilTest.getPhLevel() < 5.5) {
            // Acidic soil recommendation
            recommendations.add(new SoilRecommendation(
                soilTestId,
                "pH Adjustment",
                "Raise pH Level",
                "Your soil is too acidic for most plants. Consider adding lime to raise the pH.",
                "1. Add agricultural lime at recommended rates\n2. Retest soil in 3-6 months\n3. Consider wood ash for organic gardens",
                1 // High priority
            ));
        } else if (soilTest.getPhLevel() > 7.5) {
            // Alkaline soil recommendation
            recommendations.add(new SoilRecommendation(
                soilTestId,
                "pH Adjustment",
                "Lower pH Level",
                "Your soil is too alkaline for most plants. Consider adding sulfur to lower the pH.",
                "1. Add agricultural sulfur at recommended rates\n2. Add acidic organic matter like pine needles\n3. Use acidifying fertilizers for alkaline-sensitive plants",
                1 // High priority
            ));
        }
        
        // Check nitrogen levels
        if (soilTest.getNitrogenLevel() < 0.10) {
            // Low nitrogen recommendation
            recommendations.add(new SoilRecommendation(
                soilTestId,
                "Nutrient Management",
                "Increase Nitrogen",
                "Your soil is low in nitrogen, which may cause yellowing leaves and stunted growth.",
                "1. Add nitrogen-rich fertilizer\n2. Plant nitrogen-fixing cover crops like legumes\n3. Add composted manure or blood meal",
                soilTest.getNitrogenLevel() < 0.05 ? 1 : 2 // Priority based on severity
            ));
        }
        
        // Check phosphorus levels
        if (soilTest.getPhosphorusLevel() < 20) {
            // Low phosphorus recommendation
            recommendations.add(new SoilRecommendation(
                soilTestId,
                "Nutrient Management",
                "Increase Phosphorus",
                "Your soil is low in phosphorus, which may affect root development and flowering.",
                "1. Add rock phosphate or bone meal\n2. Apply phosphorus-rich organic fertilizer\n3. Consider companion planting with phosphorus-accumulating plants",
                soilTest.getPhosphorusLevel() < 10 ? 1 : 2 // Priority based on severity
            ));
        }
        
        // Check potassium levels
        if (soilTest.getPotassiumLevel() < 150) {
            // Low potassium recommendation
            recommendations.add(new SoilRecommendation(
                soilTestId,
                "Nutrient Management",
                "Increase Potassium",
                "Your soil is low in potassium, which may affect plant vigor and disease resistance.",
                "1. Add wood ash (if pH allows)\n2. Use composted banana peels\n3. Apply potassium-rich organic fertilizer like greensand",
                soilTest.getPotassiumLevel() < 80 ? 1 : 2 // Priority based on severity
            ));
        }
        
        // Check organic matter
        if (soilTest.getOrganicMatterPercentage() < 3.0) {
            // Low organic matter recommendation
            recommendations.add(new SoilRecommendation(
                soilTestId,
                "Soil Structure",
                "Increase Organic Matter",
                "Your soil has low organic matter content, which affects water retention and nutrient availability.",
                "1. Add compost regularly\n2. Use cover crops and mulch\n3. Avoid tilling which destroys organic matter",
                soilTest.getOrganicMatterPercentage() < 1.5 ? 1 : 2 // Priority based on severity
            ));
        }
        
        // If we have very good soil with no issues, add a maintenance recommendation
        if (recommendations.isEmpty() && soilTest.getSoilHealthScore() > 75) {
            recommendations.add(new SoilRecommendation(
                soilTestId,
                "Maintenance",
                "Maintain Soil Health",
                "Your soil is in excellent condition. Focus on maintenance practices.",
                "1. Add compost yearly\n2. Practice crop rotation\n3. Use mulch to suppress weeds and retain moisture",
                3 // Low priority since soil is already healthy
            ));
        }
        
        return recommendations;
    }
}