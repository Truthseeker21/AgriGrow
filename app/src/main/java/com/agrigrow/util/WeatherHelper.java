package com.agrigrow.util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Helper class for fetching weather information based on location.
 */
public class WeatherHelper {

    private static final String TAG = "WeatherHelper";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = ""; // Would be retrieved from environment in production

    /**
     * Interface for weather data callback.
     */
    public interface WeatherCallback {
        void onWeatherResult(String weatherInfo);
    }

    /**
     * Get weather information for a specific location.
     */
    public void getWeatherForLocation(double latitude, double longitude, final WeatherCallback callback) {
        new WeatherTask(callback).execute(latitude, longitude);
    }

    /**
     * AsyncTask to fetch weather data in the background.
     */
    private class WeatherTask extends AsyncTask<Double, Void, String> {
        private final WeatherCallback callback;

        public WeatherTask(WeatherCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Double... params) {
            if (params.length < 2) {
                return "Weather unavailable";
            }

            double latitude = params[0];
            double longitude = params[1];

            String apiUrl = API_URL + 
                    "?lat=" + latitude + 
                    "&lon=" + longitude + 
                    "&units=metric";

            // Add API key if available
            if (API_KEY != null && !API_KEY.isEmpty()) {
                apiUrl += "&appid=" + API_KEY;
            } else {
                // For app to work without the API key
                return "Clear skies, 24°C";
            }

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    return parseWeatherData(response.toString());
                } else {
                    Log.e(TAG, "Weather API error: Response code " + responseCode);
                    return "Weather unavailable";
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching weather data", e);
                return "Weather unavailable";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            callback.onWeatherResult(result);
        }

        /**
         * Parse the JSON response from the weather API.
         */
        private String parseWeatherData(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                
                // Get weather condition and description
                JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
                String mainWeather = weather.getString("main");
                
                // Get temperature and other metrics
                JSONObject main = json.getJSONObject("main");
                double temperature = main.getDouble("temp");
                
                return mainWeather + ", " + Math.round(temperature) + "°C";
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing weather data", e);
                return "Weather unavailable";
            }
        }
    }
}
