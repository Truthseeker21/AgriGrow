package com.agrigrow.util;

import android.content.Context;
import android.util.Log;

import com.agrigrow.BuildConfig;

/**
 * Utility class to securely access API keys stored in the BuildConfig.
 * These keys are defined in the local.properties file and accessed 
 * through the build.gradle configuration.
 */
public class SecretKeys {
    private static final String TAG = "SecretKeys";
    
    /**
     * Retrieves the OpenWeatherMap API key
     * @param context Application context (not used with BuildConfig but kept for API consistency)
     * @return The API key or empty string if not found
     */
    public static String getOpenWeatherMapApiKey(Context context) {
        return BuildConfig.OPENWEATHERMAP_API_KEY;
    }
    
    /**
     * Retrieves the Plant ID API key
     * @param context Application context (not used with BuildConfig but kept for API consistency)
     * @return The API key or empty string if not found
     */
    public static String getPlantIdApiKey(Context context) {
        return BuildConfig.PLANT_ID_API_KEY;
    }
}