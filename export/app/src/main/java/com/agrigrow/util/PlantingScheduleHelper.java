package com.agrigrow.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Helper class for managing planting schedules and seasonal recommendations
 */
public class PlantingScheduleHelper {
    
    private static PlantingScheduleHelper instance;
    private final Context context;
    private final WeatherHelper weatherHelper;
    
    private PlantingScheduleHelper(Context context) {
        this.context = context.getApplicationContext();
        this.weatherHelper = WeatherHelper.getInstance(context);
    }
    
    public static synchronized PlantingScheduleHelper getInstance(Context context) {
        if (instance == null) {
            instance = new PlantingScheduleHelper(context);
        }
        return instance;
    }
    
    /**
     * Get the current season based on the date
     * @return String representing the season (Spring, Summer, Fall, Winter)
     */
    public String getCurrentSeason() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        
        // Northern hemisphere seasons
        if (month >= Calendar.MARCH && month < Calendar.JUNE) {
            return "Spring";
        } else if (month >= Calendar.JUNE && month < Calendar.SEPTEMBER) {
            return "Summer";
        } else if (month >= Calendar.SEPTEMBER && month < Calendar.DECEMBER) {
            return "Fall";
        } else {
            return "Winter";
        }
    }
    
    /**
     * Get planting recommendations based on current season and weather
     * @param callback Callback to return recommendations
     */
    public void getPlantingRecommendations(PlantingRecommendationsCallback callback) {
        // Get current season
        String currentSeason = getCurrentSeason();
        
        // Get recommendations based on season
        List<String> recommendations = getSeasonalRecommendations(currentSeason);
        
        // Return recommendations
        callback.onPlantingRecommendations(recommendations, currentSeason);
    }
    
    /**
     * Get recommendations specific to a season
     * @param season The season to get recommendations for
     * @return List of recommendation strings
     */
    private List<String> getSeasonalRecommendations(String season) {
        List<String> recommendations = new ArrayList<>();
        
        switch (season) {
            case "Spring":
                recommendations.add("Spring is a great time to plant cool-season vegetables like lettuce, spinach, and peas.");
                recommendations.add("Start seedlings indoors for tomatoes, peppers, and other summer crops.");
                recommendations.add("Prune fruit trees before new growth begins.");
                break;
            case "Summer":
                recommendations.add("Summer is perfect for heat-loving plants like tomatoes, peppers, and cucumbers.");
                recommendations.add("Water deeply but infrequently to encourage deep root growth.");
                recommendations.add("Mulch around plants to conserve moisture and suppress weeds.");
                break;
            case "Fall":
                recommendations.add("Fall is ideal for planting garlic, onions, and cool-season greens.");
                recommendations.add("Plant trees and shrubs now so they can establish roots before winter.");
                recommendations.add("Start cleaning up garden beds and adding compost to prepare for next season.");
                break;
            case "Winter":
                recommendations.add("Winter is a good time to plan your garden for the upcoming season.");
                recommendations.add("Start indoor seedlings for early spring planting.");
                recommendations.add("Protect sensitive plants from frost with covers or bring them indoors.");
                break;
        }
        
        return recommendations;
    }
    
    /**
     * Get the best plants to grow in the current season
     * @return List of plant names
     */
    public List<String> getBestPlantsForCurrentSeason() {
        String currentSeason = getCurrentSeason();
        List<String> plants = new ArrayList<>();
        
        switch (currentSeason) {
            case "Spring":
                plants.add("Lettuce");
                plants.add("Spinach");
                plants.add("Peas");
                plants.add("Radishes");
                plants.add("Carrots");
                break;
            case "Summer":
                plants.add("Tomatoes");
                plants.add("Peppers");
                plants.add("Cucumbers");
                plants.add("Zucchini");
                plants.add("Basil");
                break;
            case "Fall":
                plants.add("Kale");
                plants.add("Brussels Sprouts");
                plants.add("Broccoli");
                plants.add("Garlic");
                plants.add("Onions");
                break;
            case "Winter":
                plants.add("Microgreens");
                plants.add("Indoor Herbs");
                plants.add("Sprouts");
                plants.add("Winter Squash (storage)");
                plants.add("Root Vegetables (storage)");
                break;
        }
        
        return plants;
    }
    
    /**
     * Get watering schedule recommendations based on plant type and weather
     * @param plantType Type of plant
     * @param callback Callback for watering schedule
     */
    public void getWateringSchedule(String plantType, WateringScheduleCallback callback) {
        // In a real app, this would take into account recent rainfall and forecasted weather
        // For now, we'll provide generic recommendations
        
        weatherHelper.getCurrentWeather(new WeatherHelper.OnWeatherDataListener() {
            @Override
            public void onWeatherData(WeatherHelper.WeatherData weatherData) {
                // Determine watering frequency based on plant type and current conditions
                int daysUntilNextWatering = calculateDaysUntilNextWatering(plantType, weatherData);
                String recommendation = getWateringRecommendation(plantType, weatherData);
                
                callback.onWateringSchedule(daysUntilNextWatering, recommendation);
            }
            
            @Override
            public void onWeatherError(String errorMessage) {
                // Default recommendations if weather data is unavailable
                callback.onWateringScheduleError("Unable to get weather data. Using default recommendations.");
                callback.onWateringSchedule(3, "Water when the top inch of soil feels dry.");
            }
        });
    }
    
    /**
     * Calculate days until next watering based on plant type and weather
     * @param plantType Type of plant
     * @param weatherData Current weather data
     * @return Number of days until next watering
     */
    private int calculateDaysUntilNextWatering(String plantType, WeatherHelper.WeatherData weatherData) {
        // Default values
        int baseDays = 3;
        
        // Adjust based on plant type
        switch (plantType.toLowerCase()) {
            case "cactus":
            case "succulent":
                baseDays = 14;
                break;
            case "herb":
                baseDays = 2;
                break;
            case "vegetable":
                baseDays = 2;
                break;
            case "flower":
                baseDays = 3;
                break;
            case "tree":
            case "shrub":
                baseDays = 7;
                break;
        }
        
        // Adjust based on weather
        double temperatureF = weatherData.temperatureF;
        
        // Hot weather = more frequent watering
        if (temperatureF > 85) {
            baseDays = Math.max(1, baseDays - 1);
        } 
        // Cold weather = less frequent watering
        else if (temperatureF < 50) {
            baseDays += 1;
        }
        
        // Recent rain = less frequent watering
        if (weatherData.conditionDescription.toLowerCase().contains("rain")) {
            baseDays += 1;
        }
        
        return baseDays;
    }
    
    /**
     * Get specific watering recommendations based on plant type and weather
     * @param plantType Type of plant
     * @param weatherData Current weather data
     * @return Recommendation string
     */
    private String getWateringRecommendation(String plantType, WeatherHelper.WeatherData weatherData) {
        StringBuilder recommendation = new StringBuilder();
        
        // Base recommendation by plant type
        switch (plantType.toLowerCase()) {
            case "cactus":
            case "succulent":
                recommendation.append("Water sparingly. Allow soil to completely dry between waterings. ");
                break;
            case "herb":
                recommendation.append("Keep soil consistently moist but not soggy. ");
                break;
            case "vegetable":
                recommendation.append("Water at the base of the plant to prevent leaf diseases. ");
                break;
            case "flower":
                recommendation.append("Water in the morning to allow foliage to dry during the day. ");
                break;
            case "tree":
            case "shrub":
                recommendation.append("Water deeply but infrequently to encourage deep root growth. ");
                break;
            default:
                recommendation.append("Water when the top inch of soil feels dry. ");
        }
        
        // Add weather-specific advice
        double temperatureF = weatherData.temperatureF;
        
        if (temperatureF > 85) {
            recommendation.append("Due to high temperatures, check soil moisture more frequently.");
        } else if (temperatureF < 50) {
            recommendation.append("In cooler weather, reduce watering frequency to prevent root rot.");
        }
        
        if (weatherData.conditionDescription.toLowerCase().contains("rain")) {
            recommendation.append("Recent rainfall may reduce the need for watering.");
        }
        
        return recommendation.toString();
    }
    
    /**
     * Interface for planting recommendations callback
     */
    public interface PlantingRecommendationsCallback {
        void onPlantingRecommendations(List<String> recommendations, String currentSeason);
        void onPlantingRecommendationsError(String errorMessage);
    }
    
    /**
     * Interface for watering schedule callback
     */
    public interface WateringScheduleCallback {
        void onWateringSchedule(int daysUntilNextWatering, String recommendation);
        void onWateringScheduleError(String errorMessage);
    }
}