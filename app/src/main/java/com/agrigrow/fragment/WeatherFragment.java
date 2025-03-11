package com.agrigrow.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.adapter.RecommendationAdapter;
import com.agrigrow.adapter.WateringScheduleAdapter;
import com.agrigrow.database.DatabaseHelper;
import com.agrigrow.model.Plant;
import com.agrigrow.util.WeatherHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment displaying weather information and gardening recommendations
 */
public class WeatherFragment extends Fragment {

    private static final String TAG = "WeatherFragment";
    
    // UI components
    private TextView textViewLocation;
    private TextView textViewTemperature;
    private TextView textViewWeatherDescription;
    private TextView textViewHumidity;
    private TextView textViewWind;
    private TextView textViewSunrise;
    private TextView textViewSunset;
    private TextView textViewWateringInfo;
    private ImageView imageViewWeatherIcon;
    private ImageView backgroundImage;
    private RecyclerView recyclerViewRecommendations;
    private RecyclerView recyclerViewWateringSchedule;
    private LinearLayout linearLayoutForecast;
    private Button buttonUpdateWateringSchedule;
    private ProgressBar progressBar;
    
    // Helpers and Adapters
    private WeatherHelper weatherHelper;
    private DatabaseHelper databaseHelper;
    private RecommendationAdapter recommendationAdapter;
    private WateringScheduleAdapter wateringScheduleAdapter;
    
    // Data
    private WeatherHelper.WeatherData currentWeather;
    private List<WeatherHelper.ForecastData> forecastList;
    private List<String> recommendationsList = new ArrayList<>();
    private Map<String, Integer> wateringSchedule = new HashMap<>();
    
    // Permission launcher
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    
    public WeatherFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize helpers
        weatherHelper = WeatherHelper.getInstance(requireContext());
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        
        // Initialize permission launcher
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean allGranted = true;
                    for (Boolean granted : permissions.values()) {
                        if (!granted) {
                            allGranted = false;
                            break;
                        }
                    }
                    
                    if (allGranted) {
                        loadWeatherData();
                    } else {
                        Toast.makeText(getContext(), 
                                "Location permission is required for weather data", 
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        textViewLocation = view.findViewById(R.id.textViewLocation);
        textViewTemperature = view.findViewById(R.id.textViewTemperature);
        textViewWeatherDescription = view.findViewById(R.id.textViewWeatherDescription);
        textViewHumidity = view.findViewById(R.id.textViewHumidity);
        textViewWind = view.findViewById(R.id.textViewWind);
        textViewSunrise = view.findViewById(R.id.textViewSunrise);
        textViewSunset = view.findViewById(R.id.textViewSunset);
        textViewWateringInfo = view.findViewById(R.id.textViewWateringInfo);
        imageViewWeatherIcon = view.findViewById(R.id.imageViewWeatherIcon);
        backgroundImage = view.findViewById(R.id.backgroundImage);
        recyclerViewRecommendations = view.findViewById(R.id.recyclerViewRecommendations);
        recyclerViewWateringSchedule = view.findViewById(R.id.recyclerViewWateringSchedule);
        linearLayoutForecast = view.findViewById(R.id.linearLayoutForecast);
        buttonUpdateWateringSchedule = view.findViewById(R.id.buttonUpdateWateringSchedule);
        progressBar = view.findViewById(R.id.progressBar);
        
        // Setup recycler views
        recyclerViewRecommendations.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendationAdapter = new RecommendationAdapter(recommendationsList);
        recyclerViewRecommendations.setAdapter(recommendationAdapter);
        
        recyclerViewWateringSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        wateringScheduleAdapter = new WateringScheduleAdapter(wateringSchedule);
        recyclerViewWateringSchedule.setAdapter(wateringScheduleAdapter);
        
        // Set click listeners
        buttonUpdateWateringSchedule.setOnClickListener(v -> updateWateringSchedule());
        
        // Check location permission and load weather
        if (checkAndRequestLocationPermission()) {
            loadWeatherData();
        }
    }
    
    private boolean checkAndRequestLocationPermission() {
        if (getContext() != null) {
            if (ContextCompat.checkSelfPermission(getContext(), 
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissionLauncher.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
                return false;
            }
        }
        return false;
    }
    
    private void loadWeatherData() {
        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);
        
        // Get weather data
        weatherHelper.getCurrentWeather(new WeatherHelper.OnWeatherDataListener() {
            @Override
            public void onWeatherData(WeatherHelper.WeatherData weatherData) {
                currentWeather = weatherData;
                updateWeatherUI(weatherData);
                loadForecastData(weatherData.cityName);
                
                // Get watering recommendations
                updateWateringSchedule();
                
                // Hide loading indicator
                progressBar.setVisibility(View.GONE);
            }
            
            @Override
            public void onWeatherError(String errorMessage) {
                Toast.makeText(getContext(), "Weather error: " + errorMessage, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    
    private void loadForecastData(String cityName) {
        // Get forecast data based on current location
        weatherHelper.getCurrentLocation(new WeatherHelper.LocationCallback() {
            @Override
            public void onLocationResult(android.location.Location location) {
                weatherHelper.fetchForecast(location.getLatitude(), location.getLongitude(), 
                        new WeatherHelper.OnForecastDataListener() {
                    @Override
                    public void onForecastData(List<WeatherHelper.ForecastData> forecastData) {
                        forecastList = forecastData;
                        updateForecastUI(forecastData);
                    }
                    
                    @Override
                    public void onForecastError(String errorMessage) {
                        Toast.makeText(getContext(), 
                                "Forecast error: " + errorMessage, 
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            
            @Override
            public void onLocationError(String errorMessage) {
                Toast.makeText(getContext(), 
                        "Location error: " + errorMessage, 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateWeatherUI(WeatherHelper.WeatherData weatherData) {
        // Update location
        textViewLocation.setText(weatherData.getLocationString());
        
        // Update temperature
        textViewTemperature.setText(weatherData.getTemperatureString());
        
        // Update weather description
        textViewWeatherDescription.setText(weatherData.conditionDescription);
        
        // Update humidity
        textViewHumidity.setText("Humidity: " + weatherData.humidity + "%");
        
        // Update wind
        textViewWind.setText("Wind: " + weatherData.getWindSpeedString());
        
        // Update sunrise/sunset
        textViewSunrise.setText(WeatherHelper.getFormattedSunriseTime(weatherData.sunrise));
        textViewSunset.setText(WeatherHelper.getFormattedSunsetTime(weatherData.sunset));
        
        // Update weather icon
        Glide.with(this)
                .load(weatherData.getIconUrl())
                .into(imageViewWeatherIcon);
        
        // Update background based on weather condition
        updateBackgroundImage(weatherData.simplifiedCondition);
        
        // Update recommendations
        recommendationsList.clear();
        recommendationsList.addAll(weatherData.recommendations);
        recommendationAdapter.notifyDataSetChanged();
    }
    
    private void updateForecastUI(List<WeatherHelper.ForecastData> forecastData) {
        // Clear existing forecast views
        linearLayoutForecast.removeAllViews();
        
        // Process forecast data to get daily forecasts (not hourly)
        Map<String, WeatherHelper.ForecastData> dailyForecasts = new HashMap<>();
        
        for (WeatherHelper.ForecastData forecast : forecastData) {
            // Get the date as a string (without time)
            String dateString = forecast.getDateString();
            
            // If we don't have a forecast for this day yet, or if this is a daytime forecast 
            // (between 10am and 4pm), use it
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(forecast.timestamp);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            
            if (!dailyForecasts.containsKey(dateString) || (hour >= 10 && hour <= 16)) {
                dailyForecasts.put(dateString, forecast);
            }
        }
        
        // Create a view for each daily forecast
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (WeatherHelper.ForecastData forecast : dailyForecasts.values()) {
            View forecastItemView = inflater.inflate(R.layout.item_forecast, linearLayoutForecast, false);
            
            TextView dayTextView = forecastItemView.findViewById(R.id.textViewDay);
            TextView dateTextView = forecastItemView.findViewById(R.id.textViewDate);
            TextView tempTextView = forecastItemView.findViewById(R.id.textViewTemp);
            ImageView iconImageView = forecastItemView.findViewById(R.id.imageViewIcon);
            
            dayTextView.setText(forecast.getDayString());
            dateTextView.setText(forecast.getDateString());
            tempTextView.setText(forecast.getTemperatureString());
            
            // Load weather icon
            Glide.with(this)
                    .load(forecast.getIconUrl())
                    .into(iconImageView);
            
            linearLayoutForecast.addView(forecastItemView);
        }
    }
    
    private void updateBackgroundImage(int condition) {
        int backgroundResource;
        
        switch (condition) {
            case WeatherHelper.CONDITION_CLEAR:
                backgroundResource = android.R.color.holo_blue_light;
                break;
            case WeatherHelper.CONDITION_CLOUDY:
                backgroundResource = android.R.color.darker_gray;
                break;
            case WeatherHelper.CONDITION_RAINY:
                backgroundResource = android.R.color.holo_blue_dark;
                break;
            case WeatherHelper.CONDITION_STORMY:
                backgroundResource = android.R.color.black;
                break;
            case WeatherHelper.CONDITION_SNOWY:
                backgroundResource = android.R.color.white;
                break;
            case WeatherHelper.CONDITION_WINDY:
                backgroundResource = android.R.color.holo_blue_bright;
                break;
            default:
                backgroundResource = android.R.color.holo_green_light;
                break;
        }
        
        backgroundImage.setImageResource(backgroundResource);
    }
    
    private void updateWateringSchedule() {
        // Check if we have plants in the database
        List<Plant> plants = databaseHelper.getPlantDao().getAllPlants();
        if (plants == null || plants.isEmpty()) {
            textViewWateringInfo.setText("Add plants to your garden to get watering recommendations");
            wateringSchedule.clear();
            wateringScheduleAdapter.notifyDataSetChanged();
            return;
        }
        
        // Get plant types from database
        List<String> plantTypes = new ArrayList<>();
        for (Plant plant : plants) {
            // Extract the plant type/category - in a real app this would be a field in the Plant class
            String plantType = extractPlantType(plant.getName());
            if (!plantTypes.contains(plantType)) {
                plantTypes.add(plantType);
            }
        }
        
        // Convert to array for the weather helper
        String[] plantTypesArray = plantTypes.toArray(new String[0]);
        
        // Get watering recommendations
        weatherHelper.getWateringRecommendations(plantTypesArray, 
                new WeatherHelper.OnWateringRecommendationListener() {
            @Override
            public void onWateringRecommendations(Map<String, Integer> schedule) {
                wateringSchedule.clear();
                wateringSchedule.putAll(schedule);
                wateringScheduleAdapter.notifyDataSetChanged();
                
                textViewWateringInfo.setText("Based on current and forecasted weather conditions, here's when to water your plants:");
            }
            
            @Override
            public void onWateringRecommendationError(String errorMessage) {
                Toast.makeText(getContext(), 
                        "Error getting watering recommendations: " + errorMessage, 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Extract plant type from plant name - in a real app this would be a field in the Plant class
     */
    private String extractPlantType(String plantName) {
        // This is a simplified implementation - in a real app, you would have a proper categorization
        String lowercaseName = plantName.toLowerCase();
        
        if (lowercaseName.contains("tomato") || lowercaseName.contains("pepper") || 
                lowercaseName.contains("cucumber") || lowercaseName.contains("zucchini")) {
            return "vegetables";
        } else if (lowercaseName.contains("rose") || lowercaseName.contains("tulip") || 
                lowercaseName.contains("daisy") || lowercaseName.contains("lily")) {
            return "flowers";
        } else if (lowercaseName.contains("basil") || lowercaseName.contains("mint") || 
                lowercaseName.contains("thyme") || lowercaseName.contains("oregano")) {
            return "herbs";
        } else if (lowercaseName.contains("cactus") || lowercaseName.contains("agave")) {
            return "cacti";
        } else if (lowercaseName.contains("aloe") || lowercaseName.contains("jade") || 
                lowercaseName.contains("echeveria")) {
            return "succulents";
        } else if (lowercaseName.contains("oak") || lowercaseName.contains("maple") || 
                lowercaseName.contains("pine")) {
            return "trees";
        } else if (lowercaseName.contains("hydrangea") || lowercaseName.contains("azalea") || 
                lowercaseName.contains("boxwood")) {
            return "shrubs";
        } else {
            return "plants"; // Default category
        }
    }
    
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
    }
}