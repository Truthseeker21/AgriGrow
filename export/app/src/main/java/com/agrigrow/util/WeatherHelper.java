package com.agrigrow.util;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Helper class for fetching weather data and providing gardening recommendations
 * based on current and forecasted weather conditions.
 */
public class WeatherHelper {
    
    private static final String TAG = "WeatherHelper";
    private static final String WEATHER_API_BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static String WEATHER_API_KEY = "YOUR_API_KEY"; // This should be stored securely
    
    private final Context context;
    private final Executor executor;
    private LocationHelper locationHelper;
    private static WeatherHelper instance;
    
    // Weather condition constants
    public static final int CONDITION_CLEAR = 0;
    public static final int CONDITION_CLOUDY = 1;
    public static final int CONDITION_RAINY = 2;
    public static final int CONDITION_STORMY = 3;
    public static final int CONDITION_SNOWY = 4;
    public static final int CONDITION_WINDY = 5;
    
    /**
     * Private constructor (singleton pattern)
     * @param context Application context
     */
    private WeatherHelper(Context context) {
        this.context = context.getApplicationContext();
        this.executor = Executors.newSingleThreadExecutor();
        this.locationHelper = LocationHelper.getInstance(context);
    }
    
    /**
     * Get singleton instance
     * @param context Application context
     * @return WeatherHelper instance
     */
    public static synchronized WeatherHelper getInstance(Context context) {
        if (instance == null) {
            instance = new WeatherHelper(context);
        }
        return instance;
    }
    
    /**
     * Set the API key (should be called before using any API methods)
     * @param apiKey The OpenWeatherMap API key
     */
    public static void setApiKey(String apiKey) {
        WEATHER_API_KEY = apiKey;
    }
    
    /**
     * Fetch current weather data for the user's location
     * @param listener Callback for weather data
     */
    public void getCurrentWeather(OnWeatherDataListener listener) {
        locationHelper.getCurrentLocation(new LocationHelper.LocationCallback() {
            @Override
            public void onLocationResult(Location location) {
                if (location != null) {
                    fetchWeatherData(location.getLatitude(), location.getLongitude(), listener);
                } else {
                    listener.onWeatherError("Unable to determine location");
                }
            }
            
            @Override
            public void onLocationError(String errorMessage) {
                listener.onWeatherError("Location error: " + errorMessage);
            }
        });
    }
    
    /**
     * Fetch current weather data for a specific location
     * @param latitude Location latitude
     * @param longitude Location longitude
     * @param listener Callback for weather data
     */
    public void fetchWeatherData(double latitude, double longitude, OnWeatherDataListener listener) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            
            try {
                // Build API URL
                String url = WEATHER_API_BASE_URL + "weather?lat=" + latitude + "&lon=" + longitude + 
                        "&units=metric&appid=" + WEATHER_API_KEY;
                
                URL weatherUrl = new URL(url);
                connection = (HttpURLConnection) weatherUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                
                // Read the response
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                
                // Parse JSON response
                JSONObject response = new JSONObject(buffer.toString());
                WeatherData weatherData = parseWeatherData(response);
                
                // Add gardening recommendations
                weatherData.recommendations = generateRecommendations(weatherData);
                
                // Return result on main thread
                final WeatherData finalWeatherData = weatherData;
                android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                mainHandler.post(() -> listener.onWeatherData(finalWeatherData));
                
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error fetching weather data", e);
                final String errorMessage = "Error fetching weather data: " + e.getMessage();
                android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                mainHandler.post(() -> listener.onWeatherError(errorMessage));
            } finally {
                // Close connections
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing reader", e);
                    }
                }
            }
        });
    }
    
    /**
     * Fetch weather forecast for the next 5 days
     * @param latitude Location latitude
     * @param longitude Location longitude
     * @param listener Callback for forecast data
     */
    public void fetchForecast(double latitude, double longitude, OnForecastDataListener listener) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            
            try {
                // Build API URL for 5-day forecast
                String url = WEATHER_API_BASE_URL + "forecast?lat=" + latitude + "&lon=" + longitude + 
                        "&units=metric&appid=" + WEATHER_API_KEY;
                
                URL forecastUrl = new URL(url);
                connection = (HttpURLConnection) forecastUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                
                // Read the response
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                
                // Parse JSON response
                JSONObject response = new JSONObject(buffer.toString());
                List<ForecastData> forecastList = parseForecastData(response);
                
                // Return result on main thread
                android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                mainHandler.post(() -> listener.onForecastData(forecastList));
                
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error fetching forecast data", e);
                final String errorMessage = "Error fetching forecast data: " + e.getMessage();
                android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                mainHandler.post(() -> listener.onForecastError(errorMessage));
            } finally {
                // Close connections
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing reader", e);
                    }
                }
            }
        });
    }
    
    /**
     * Parse current weather data from JSON response
     * @param json JSON response from weather API
     * @return Parsed WeatherData object
     */
    private WeatherData parseWeatherData(JSONObject json) throws JSONException {
        WeatherData weatherData = new WeatherData();
        
        // Get main weather data
        JSONObject main = json.getJSONObject("main");
        weatherData.temperature = main.getDouble("temp");
        weatherData.humidity = main.getInt("humidity");
        weatherData.pressure = main.getInt("pressure");
        
        // Get weather condition
        JSONArray weatherArray = json.getJSONArray("weather");
        JSONObject weather = weatherArray.getJSONObject(0);
        weatherData.conditionId = weather.getInt("id");
        weatherData.conditionMain = weather.getString("main");
        weatherData.conditionDescription = weather.getString("description");
        weatherData.iconCode = weather.getString("icon");
        
        // Get wind data
        JSONObject wind = json.getJSONObject("wind");
        weatherData.windSpeed = wind.getDouble("speed");
        if (wind.has("deg")) {
            weatherData.windDirection = wind.getInt("deg");
        }
        
        // Get location data
        weatherData.cityName = json.getString("name");
        if (json.has("sys")) {
            JSONObject sys = json.getJSONObject("sys");
            if (sys.has("country")) {
                weatherData.countryCode = sys.getString("country");
            }
            if (sys.has("sunrise")) {
                weatherData.sunrise = sys.getLong("sunrise") * 1000; // Convert to milliseconds
            }
            if (sys.has("sunset")) {
                weatherData.sunset = sys.getLong("sunset") * 1000; // Convert to milliseconds
            }
        }
        
        // Get rain data if available
        if (json.has("rain")) {
            JSONObject rain = json.getJSONObject("rain");
            if (rain.has("1h")) {
                weatherData.rainLastHour = rain.getDouble("1h");
            }
            if (rain.has("3h")) {
                weatherData.rainLast3Hours = rain.getDouble("3h");
            }
        }
        
        // Get timestamp
        weatherData.timestamp = json.getLong("dt") * 1000; // Convert to milliseconds
        
        // Set simplified condition
        weatherData.simplifiedCondition = getSimplifiedCondition(weatherData.conditionId, weatherData.windSpeed);
        
        return weatherData;
    }
    
    /**
     * Parse forecast data from JSON response
     * @param json JSON response from forecast API
     * @return List of ForecastData objects
     */
    private List<ForecastData> parseForecastData(JSONObject json) throws JSONException {
        List<ForecastData> forecastList = new ArrayList<>();
        
        JSONArray list = json.getJSONArray("list");
        String cityName = "";
        String countryCode = "";
        
        if (json.has("city")) {
            JSONObject city = json.getJSONObject("city");
            if (city.has("name")) {
                cityName = city.getString("name");
            }
            if (city.has("country")) {
                countryCode = city.getString("country");
            }
        }
        
        // Process each forecast time point
        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            ForecastData forecast = new ForecastData();
            
            // Get timestamp
            forecast.timestamp = item.getLong("dt") * 1000; // Convert to milliseconds
            
            // Get main weather data
            JSONObject main = item.getJSONObject("main");
            forecast.temperature = main.getDouble("temp");
            forecast.humidity = main.getInt("humidity");
            
            // Get weather condition
            JSONArray weatherArray = item.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);
            forecast.conditionId = weather.getInt("id");
            forecast.conditionMain = weather.getString("main");
            forecast.iconCode = weather.getString("icon");
            
            // Get wind data
            JSONObject wind = item.getJSONObject("wind");
            forecast.windSpeed = wind.getDouble("speed");
            
            // Set simplified condition
            forecast.simplifiedCondition = getSimplifiedCondition(forecast.conditionId, forecast.windSpeed);
            
            // Set location data
            forecast.cityName = cityName;
            forecast.countryCode = countryCode;
            
            // Add to list
            forecastList.add(forecast);
        }
        
        return forecastList;
    }
    
    /**
     * Convert OpenWeatherMap condition codes to simplified conditions
     * @param conditionId Weather condition ID from API
     * @param windSpeed Wind speed in meters per second
     * @return Simplified condition constant
     */
    private int getSimplifiedCondition(int conditionId, double windSpeed) {
        // Check for windy conditions first
        if (windSpeed >= 10.0) { // 10 m/s is about 22 mph
            return CONDITION_WINDY;
        }
        
        // Group by first digit of condition code
        int firstDigit = conditionId / 100;
        
        switch (firstDigit) {
            case 2: // Thunderstorm
                return CONDITION_STORMY;
            case 3: // Drizzle
            case 5: // Rain
                return CONDITION_RAINY;
            case 6: // Snow
                return CONDITION_SNOWY;
            case 7: // Atmosphere (fog, haze, etc.)
                return CONDITION_CLOUDY;
            case 8: // Clear or clouds
                if (conditionId == 800) {
                    return CONDITION_CLEAR;
                } else {
                    return CONDITION_CLOUDY;
                }
            default:
                return CONDITION_CLEAR;
        }
    }
    
    /**
     * Generate gardening recommendations based on weather conditions
     * @param weatherData Current weather data
     * @return List of gardening recommendations
     */
    private List<String> generateRecommendations(WeatherData weatherData) {
        List<String> recommendations = new ArrayList<>();
        
        // Temperature-based recommendations
        if (weatherData.temperature < 5) {
            recommendations.add("Cold weather alert! Protect tender plants from freezing temperatures.");
            recommendations.add("Hold off on planting seedlings until temperatures are warmer.");
        } else if (weatherData.temperature > 30) {
            recommendations.add("High heat alert! Water plants thoroughly early in the morning.");
            recommendations.add("Provide shade for sensitive plants during peak sun hours.");
            recommendations.add("Avoid fertilizing plants during extreme heat.");
        }
        
        // Condition-based recommendations
        switch (weatherData.simplifiedCondition) {
            case CONDITION_CLEAR:
                if (weatherData.temperature > 25) {
                    recommendations.add("Perfect day for harvesting herbs and vegetables.");
                    recommendations.add("Consider adding mulch to retain soil moisture in the heat.");
                } else {
                    recommendations.add("Great day for planting and garden maintenance.");
                    recommendations.add("Good opportunity to inspect plants for pests and diseases.");
                }
                break;
                
            case CONDITION_CLOUDY:
                recommendations.add("Cloudy conditions are ideal for transplanting seedlings.");
                recommendations.add("Good day for applying foliar fertilizers.");
                break;
                
            case CONDITION_RAINY:
                recommendations.add("No need to water your garden today!");
                recommendations.add("Check drainage in container plants to prevent waterlogging.");
                recommendations.add("Wait for soil to dry before walking in garden beds to prevent compaction.");
                break;
                
            case CONDITION_STORMY:
                recommendations.add("Secure lightweight pots and garden structures against strong winds.");
                recommendations.add("Stake tall plants to prevent storm damage.");
                recommendations.add("Harvest ripe produce before the storm hits.");
                break;
                
            case CONDITION_SNOWY:
                recommendations.add("Snow provides insulation for perennials and root systems.");
                recommendations.add("Brush heavy snow off shrubs and tree branches to prevent breakage.");
                recommendations.add("Indoor gardening day! Start planning your spring garden.");
                break;
                
            case CONDITION_WINDY:
                recommendations.add("Avoid spraying pesticides or herbicides in windy conditions.");
                recommendations.add("Check that climbing plants are securely attached to their supports.");
                recommendations.add("Wind can dry out soil quickly - check moisture levels more frequently.");
                break;
        }
        
        // Humidity-based recommendations
        if (weatherData.humidity > 80) {
            recommendations.add("High humidity increases disease risk. Ensure good air circulation around plants.");
            recommendations.add("Avoid overhead watering to reduce fungal disease risk.");
        } else if (weatherData.humidity < 30) {
            recommendations.add("Low humidity can stress plants. Mist sensitive plants to increase humidity.");
        }
        
        // Time of day recommendations
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        
        if (hourOfDay < 10) {
            recommendations.add("Morning is the best time to water plants for maximum absorption and disease prevention.");
        } else if (hourOfDay >= 10 && hourOfDay < 16) {
            recommendations.add("Avoid watering during midday heat as water evaporates quickly.");
        } else {
            recommendations.add("Evening watering allows for good absorption but can increase fungal disease risk.");
        }
        
        return recommendations;
    }
    
    /**
     * Get current season based on hemisphere and current date
     * @param latitude Latitude to determine hemisphere
     * @return Season name (Spring, Summer, Fall, Winter)
     */
    public String getCurrentSeason(double latitude) {
        // Determine if northern or southern hemisphere
        boolean isNorthernHemisphere = latitude >= 0;
        
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        // Calculate the meteorological season
        if (isNorthernHemisphere) {
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
        } else {
            // Southern hemisphere (seasons reversed)
            if (month >= Calendar.MARCH && month < Calendar.JUNE) {
                return "Fall";
            } else if (month >= Calendar.JUNE && month < Calendar.SEPTEMBER) {
                return "Winter";
            } else if (month >= Calendar.SEPTEMBER && month < Calendar.DECEMBER) {
                return "Spring";
            } else {
                return "Summer";
            }
        }
    }
    
    /**
     * Get watering recommendations based on weather forecast and plant types
     * @param plantTypes Array of plant types (e.g., "vegetables", "herbs", "flowers")
     * @param listener Callback for watering recommendations
     */
    public void getWateringRecommendations(String[] plantTypes, OnWateringRecommendationListener listener) {
        locationHelper.getCurrentLocation(new LocationHelper.LocationCallback() {
            @Override
            public void onLocationResult(Location location) {
                if (location != null) {
                    fetchForecast(location.getLatitude(), location.getLongitude(), new OnForecastDataListener() {
                        @Override
                        public void onForecastData(List<ForecastData> forecastList) {
                            // Generate watering recommendations based on forecast
                            Map<String, Integer> wateringSchedule = generateWateringSchedule(forecastList, plantTypes);
                            listener.onWateringRecommendations(wateringSchedule);
                        }
                        
                        @Override
                        public void onForecastError(String errorMessage) {
                            listener.onWateringRecommendationError(errorMessage);
                        }
                    });
                } else {
                    listener.onWateringRecommendationError("Unable to determine location");
                }
            }
            
            @Override
            public void onLocationError(String errorMessage) {
                listener.onWateringRecommendationError("Location error: " + errorMessage);
            }
        });
    }
    
    /**
     * Generate watering schedule based on forecast and plant types
     * @param forecastList 5-day weather forecast
     * @param plantTypes Array of plant types
     * @return Map of plant types to days until watering needed (0 = today)
     */
    private Map<String, Integer> generateWateringSchedule(List<ForecastData> forecastList, String[] plantTypes) {
        Map<String, Integer> wateringSchedule = new HashMap<>();
        
        // Default watering frequencies for different plant types (in days)
        Map<String, Integer> baseWateringFrequency = new HashMap<>();
        baseWateringFrequency.put("vegetables", 2);
        baseWateringFrequency.put("herbs", 2);
        baseWateringFrequency.put("flowers", 3);
        baseWateringFrequency.put("shrubs", 5);
        baseWateringFrequency.put("trees", 7);
        baseWateringFrequency.put("succulents", 10);
        baseWateringFrequency.put("cacti", 14);
        
        // Check if rain is expected in the next few days
        boolean rainExpectedToday = false;
        boolean rainExpectedTomorrow = false;
        
        // Get today's date at midnight for comparison
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long todayMidnight = calendar.getTimeInMillis();
        
        // Calculate tomorrow's date
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        long tomorrowMidnight = calendar.getTimeInMillis();
        
        // Check each forecast for rain
        for (ForecastData forecast : forecastList) {
            // Check if this forecast is for today
            if (forecast.timestamp >= todayMidnight && forecast.timestamp < tomorrowMidnight) {
                if (forecast.simplifiedCondition == CONDITION_RAINY || 
                    forecast.simplifiedCondition == CONDITION_STORMY) {
                    rainExpectedToday = true;
                }
            }
            // Check if this forecast is for tomorrow
            else if (forecast.timestamp >= tomorrowMidnight && 
                     forecast.timestamp < tomorrowMidnight + 24*60*60*1000) {
                if (forecast.simplifiedCondition == CONDITION_RAINY || 
                    forecast.simplifiedCondition == CONDITION_STORMY) {
                    rainExpectedTomorrow = true;
                }
            }
        }
        
        // Generate watering schedule for each plant type
        for (String plantType : plantTypes) {
            String plantTypeLower = plantType.toLowerCase();
            
            // Get base watering frequency
            int frequency = baseWateringFrequency.containsKey(plantTypeLower) ? 
                    baseWateringFrequency.get(plantTypeLower) : 3; // Default to 3 days
            
            // Adjust based on forecast
            if (rainExpectedToday) {
                // If it's going to rain today, postpone watering
                wateringSchedule.put(plantType, 1);
            } else if (rainExpectedTomorrow && frequency > 1) {
                // If it's going to rain tomorrow and frequency is greater than 1 day, 
                // we can wait
                wateringSchedule.put(plantType, 2);
            } else {
                // Otherwise, water today
                wateringSchedule.put(plantType, 0);
            }
        }
        
        return wateringSchedule;
    }
    
    /**
     * Get formatted sunrise time string
     * @param timestamp Sunrise timestamp in milliseconds
     * @return Formatted time string
     */
    public static String getFormattedSunriseTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    /**
     * Get formatted sunset time string
     * @param timestamp Sunset timestamp in milliseconds
     * @return Formatted time string
     */
    public static String getFormattedSunsetTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    /**
     * Get weather icon URL from icon code
     * @param iconCode Weather icon code from API
     * @return Full URL to weather icon
     */
    public static String getWeatherIconUrl(String iconCode) {
        return "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
    }
    
    /**
     * Get formatted day of week from timestamp
     * @param timestamp Timestamp in milliseconds
     * @return Day of week (e.g., "Monday")
     */
    public static String getDayOfWeek(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    /**
     * Get formatted date string from timestamp
     * @param timestamp Timestamp in milliseconds
     * @return Formatted date (e.g., "Jun 15")
     */
    public static String getFormattedDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    /**
     * Get formatted time string from timestamp
     * @param timestamp Timestamp in milliseconds
     * @return Formatted time (e.g., "3:30 PM")
     */
    public static String getFormattedTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    /**
     * Class to hold current weather data
     */
    public static class WeatherData {
        public double temperature; // Temperature in Celsius
        public int humidity; // Humidity percentage
        public int pressure; // Atmospheric pressure in hPa
        public int conditionId; // Weather condition ID
        public String conditionMain; // Main condition (e.g., "Rain", "Clear")
        public String conditionDescription; // Detailed condition description
        public String iconCode; // Weather icon code
        public double windSpeed; // Wind speed in meters per second
        public int windDirection; // Wind direction in degrees
        public String cityName; // City name
        public String countryCode; // Country code
        public long sunrise; // Sunrise time (timestamp in milliseconds)
        public long sunset; // Sunset time (timestamp in milliseconds)
        public double rainLastHour; // Rainfall in last hour (mm)
        public double rainLast3Hours; // Rainfall in last 3 hours (mm)
        public long timestamp; // Data timestamp
        public int simplifiedCondition; // Simplified condition constant
        public List<String> recommendations; // Gardening recommendations
        
        /**
         * Get a user-friendly temperature string with unit
         * @return Formatted temperature string
         */
        public String getTemperatureString() {
            return Math.round(temperature) + "°C";
        }
        
        /**
         * Get a user-friendly wind speed string with unit
         * @return Formatted wind speed string
         */
        public String getWindSpeedString() {
            return Math.round(windSpeed) + " m/s";
        }
        
        /**
         * Get a user-friendly location string
         * @return Formatted location string
         */
        public String getLocationString() {
            if (countryCode != null && !countryCode.isEmpty()) {
                return cityName + ", " + countryCode;
            }
            return cityName;
        }
        
        /**
         * Get icon URL for this weather condition
         * @return Weather icon URL
         */
        public String getIconUrl() {
            return getWeatherIconUrl(iconCode);
        }
    }
    
    /**
     * Class to hold forecast data
     */
    public static class ForecastData {
        public long timestamp; // Forecast timestamp
        public double temperature; // Temperature in Celsius
        public int humidity; // Humidity percentage
        public int conditionId; // Weather condition ID
        public String conditionMain; // Main condition (e.g., "Rain", "Clear")
        public String iconCode; // Weather icon code
        public double windSpeed; // Wind speed in meters per second
        public int simplifiedCondition; // Simplified condition constant
        public String cityName; // City name
        public String countryCode; // Country code
        
        /**
         * Get a user-friendly temperature string with unit
         * @return Formatted temperature string
         */
        public String getTemperatureString() {
            return Math.round(temperature) + "°C";
        }
        
        /**
         * Get a user-friendly time string
         * @return Formatted time string
         */
        public String getTimeString() {
            return getFormattedTime(timestamp);
        }
        
        /**
         * Get a user-friendly date string
         * @return Formatted date string
         */
        public String getDateString() {
            return getFormattedDate(timestamp);
        }
        
        /**
         * Get a user-friendly day of week string
         * @return Day of week string
         */
        public String getDayString() {
            return getDayOfWeek(timestamp);
        }
        
        /**
         * Get icon URL for this weather condition
         * @return Weather icon URL
         */
        public String getIconUrl() {
            return getWeatherIconUrl(iconCode);
        }
    }
    
    /**
     * Interface for weather data callbacks
     */
    public interface OnWeatherDataListener {
        void onWeatherData(WeatherData weatherData);
        void onWeatherError(String errorMessage);
    }
    
    /**
     * Interface for forecast data callbacks
     */
    public interface OnForecastDataListener {
        void onForecastData(List<ForecastData> forecastList);
        void onForecastError(String errorMessage);
    }
    
    /**
     * Interface for watering recommendation callbacks
     */
    public interface OnWateringRecommendationListener {
        void onWateringRecommendations(Map<String, Integer> wateringSchedule);
        void onWateringRecommendationError(String errorMessage);
    }
}