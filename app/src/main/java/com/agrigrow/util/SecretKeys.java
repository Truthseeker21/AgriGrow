package com.agrigrow.util;

/**
 * Utility class to store and retrieve API keys securely.
 * In a production environment, these keys should be stored using more secure methods
 * like Android KeyStore or fetched from a secure server.
 */
public class SecretKeys {
    
    // Weather API key from OpenWeatherMap
    private static String WEATHER_API_KEY = "${OPENWEATHERMAP_API_KEY}";
    
    // Plant Identification API key from Plant.id
    private static String PLANT_ID_API_KEY = "${PLANT_ID_API_KEY}";
    
    /**
     * Get the OpenWeatherMap API key
     * @return API key string
     */
    public static String getWeatherApiKey() {
        return WEATHER_API_KEY;
    }
    
    /**
     * Set the OpenWeatherMap API key
     * @param apiKey API key string
     */
    public static void setWeatherApiKey(String apiKey) {
        WEATHER_API_KEY = apiKey;
    }
    
    /**
     * Get the Plant.id API key
     * @return API key string
     */
    public static String getPlantIdApiKey() {
        return PLANT_ID_API_KEY;
    }
    
    /**
     * Set the Plant.id API key
     * @param apiKey API key string
     */
    public static void setPlantIdApiKey(String apiKey) {
        PLANT_ID_API_KEY = apiKey;
    }
}