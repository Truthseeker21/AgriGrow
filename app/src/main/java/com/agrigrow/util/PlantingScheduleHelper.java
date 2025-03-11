package com.agrigrow.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for determining planting schedules based on season and location.
 */
public class PlantingScheduleHelper {

    // Seasonal boundaries by month for Northern Hemisphere
    private static final Map<String, int[]> NORTHERN_SEASONS = new HashMap<>();
    // Seasonal boundaries by month for Southern Hemisphere
    private static final Map<String, int[]> SOUTHERN_SEASONS = new HashMap<>();
    
    // Initialize season boundaries
    static {
        // Northern Hemisphere seasons
        NORTHERN_SEASONS.put("Spring", new int[]{2, 3, 4}); // March, April, May
        NORTHERN_SEASONS.put("Summer", new int[]{5, 6, 7}); // June, July, August
        NORTHERN_SEASONS.put("Fall", new int[]{8, 9, 10}); // September, October, November
        NORTHERN_SEASONS.put("Winter", new int[]{11, 0, 1}); // December, January, February
        
        // Southern Hemisphere seasons (reversed)
        SOUTHERN_SEASONS.put("Spring", new int[]{8, 9, 10}); // September, October, November
        SOUTHERN_SEASONS.put("Summer", new int[]{11, 0, 1}); // December, January, February
        SOUTHERN_SEASONS.put("Fall", new int[]{2, 3, 4}); // March, April, May
        SOUTHERN_SEASONS.put("Winter", new int[]{5, 6, 7}); // June, July, August
    }

    private boolean isNorthernHemisphere = true; // Default to Northern Hemisphere

    /**
     * Set the hemisphere based on latitude.
     */
    public void setHemisphere(double latitude) {
        isNorthernHemisphere = latitude >= 0;
    }

    /**
     * Get the current season based on date and hemisphere.
     */
    public String getCurrentSeason() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        
        Map<String, int[]> seasons = isNorthernHemisphere ? NORTHERN_SEASONS : SOUTHERN_SEASONS;
        
        for (Map.Entry<String, int[]> entry : seasons.entrySet()) {
            int[] months = entry.getValue();
            for (int m : months) {
                if (m == month) {
                    return entry.getKey();
                }
            }
        }
        
        // Default to Spring if no match is found (shouldn't happen)
        return "Spring";
    }

    /**
     * Get plants suitable for planting in the current season.
     */
    public String[] getCurrentSeasonPlants() {
        String season = getCurrentSeason();
        return getPlantsForSeason(season);
    }

    /**
     * Get plants suitable for planting in a specific season.
     */
    public String[] getPlantsForSeason(String season) {
        switch (season) {
            case "Spring":
                return new String[]{
                    "Lettuce", "Peas", "Radishes", "Spinach", "Carrots",
                    "Beets", "Broccoli", "Cauliflower", "Kale", "Onions"
                };
            case "Summer":
                return new String[]{
                    "Tomatoes", "Peppers", "Eggplant", "Cucumbers", "Zucchini",
                    "Corn", "Beans", "Basil", "Cilantro", "Sunflowers"
                };
            case "Fall":
                return new String[]{
                    "Kale", "Collards", "Spinach", "Lettuce", "Radishes",
                    "Carrots", "Beets", "Broccoli", "Cauliflower", "Garlic"
                };
            case "Winter":
                return new String[]{
                    "Garlic", "Onions", "Shallots", "Winter Squash (storage)", 
                    "Microgreens (indoor)", "Sprouts (indoor)", "Herbs (indoor)"
                };
            default:
                return new String[0];
        }
    }

    /**
     * Get recommended gardening tasks for the current season.
     */
    public String[] getCurrentSeasonTasks() {
        String season = getCurrentSeason();
        return getTasksForSeason(season);
    }

    /**
     * Get recommended gardening tasks for a specific season.
     */
    public String[] getTasksForSeason(String season) {
        switch (season) {
            case "Spring":
                return new String[]{
                    "Prepare soil and beds", 
                    "Start seeds indoors", 
                    "Plant cool-weather crops",
                    "Prune winter damage",
                    "Fertilize perennials",
                    "Set up irrigation systems"
                };
            case "Summer":
                return new String[]{
                    "Regular watering", 
                    "Mulch to retain moisture",
                    "Harvest produce regularly",
                    "Monitor for pests",
                    "Succession planting",
                    "Provide shade for delicate plants"
                };
            case "Fall":
                return new String[]{
                    "Plant cool-weather crops", 
                    "Collect seeds",
                    "Clean up garden beds",
                    "Compost spent plants",
                    "Plant cover crops",
                    "Protect perennials"
                };
            case "Winter":
                return new String[]{
                    "Plan next season's garden", 
                    "Order seeds",
                    "Maintain tools",
                    "Start winter indoor gardening",
                    "Monitor stored produce",
                    "Protect containers from freezing"
                };
            default:
                return new String[0];
        }
    }

    /**
     * Check if a plant is suitable for planting in the current month.
     */
    public boolean isPlantingSuitable(String plantName) {
        String season = getCurrentSeason();
        String[] seasonalPlants = getPlantsForSeason(season);
        
        for (String plant : seasonalPlants) {
            if (plant.equalsIgnoreCase(plantName)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Get next planting date for a specific plant.
     */
    public String getNextPlantingDate(String plantName) {
        // This is a simplified implementation
        // In a real app, this would use a more sophisticated algorithm
        // based on plant data, location, and climate
        
        Calendar now = Calendar.getInstance();
        String currentSeason = getCurrentSeason();
        boolean suitable = isPlantingSuitable(plantName);
        
        if (suitable) {
            return "Now is a good time to plant " + plantName;
        } else {
            // Suggest the next season when it might be suitable
            switch (currentSeason) {
                case "Winter":
                    return "Consider planting in Spring (March)";
                case "Spring":
                    return "Consider planting in Summer (June)";
                case "Summer":
                    return "Consider planting in Fall (September)";
                case "Fall":
                    return "Consider planting next Spring (March)";
                default:
                    return "Check planting guide for specific timing";
            }
        }
    }
}
