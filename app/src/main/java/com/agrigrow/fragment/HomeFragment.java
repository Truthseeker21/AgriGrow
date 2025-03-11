package com.agrigrow.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.adapter.GardeningGuideAdapter;
import com.agrigrow.adapter.PlantAdapter;
import com.agrigrow.database.GardeningTechniqueDao;
import com.agrigrow.database.PlantDao;
import com.agrigrow.model.GardeningTechnique;
import com.agrigrow.model.Plant;
import com.agrigrow.util.LocationHelper;
import com.agrigrow.util.PlantingScheduleHelper;
import com.agrigrow.util.WeatherHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Home fragment displaying personalized recommendations based on season, location,
 * and user preferences.
 */
public class HomeFragment extends Fragment implements 
        PlantAdapter.OnPlantClickListener,
        GardeningGuideAdapter.OnGuideClickListener {

    private TextView textViewGreeting;
    private TextView textViewDate;
    private TextView textViewLocation;
    private TextView textViewCurrentSeason;
    private TextView textViewWeather;
    private RecyclerView recyclerViewSeasonalPlants;
    private RecyclerView recyclerViewRecommendedGuides;
    
    private PlantAdapter plantAdapter;
    private GardeningGuideAdapter guideAdapter;
    
    private PlantDao plantDao;
    private GardeningTechniqueDao guideDao;
    
    private LocationHelper locationHelper;
    private WeatherHelper weatherHelper;
    private PlantingScheduleHelper scheduleHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        textViewGreeting = view.findViewById(R.id.textViewGreeting);
        textViewDate = view.findViewById(R.id.textViewDate);
        textViewLocation = view.findViewById(R.id.textViewLocation);
        textViewCurrentSeason = view.findViewById(R.id.textViewCurrentSeason);
        textViewWeather = view.findViewById(R.id.textViewWeather);
        recyclerViewSeasonalPlants = view.findViewById(R.id.recyclerViewSeasonalPlants);
        recyclerViewRecommendedGuides = view.findViewById(R.id.recyclerViewRecommendedGuides);
        
        // Initialize DAOs
        plantDao = new PlantDao(getContext());
        guideDao = new GardeningTechniqueDao(getContext());
        
        // Initialize helper classes
        locationHelper = new LocationHelper(getContext());
        weatherHelper = new WeatherHelper();
        scheduleHelper = new PlantingScheduleHelper();
        
        // Set up adapters and recycler views
        setupRecyclerViews();
        
        // Set current date
        setCurrentDate();
        
        // Load user location and weather
        loadLocationAndWeather();
        
        // Load seasonal plants and recommended guides
        loadSeasonalPlants();
        loadRecommendedGuides();
    }

    private void setupRecyclerViews() {
        // Set up plants RecyclerView
        recyclerViewSeasonalPlants.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        plantAdapter = new PlantAdapter(getContext(), new ArrayList<>(), this);
        recyclerViewSeasonalPlants.setAdapter(plantAdapter);
        
        // Set up guides RecyclerView
        recyclerViewRecommendedGuides.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        guideAdapter = new GardeningGuideAdapter(getContext(), new ArrayList<>(), this);
        recyclerViewRecommendedGuides.setAdapter(guideAdapter);
    }

    private void setCurrentDate() {
        // Set greeting based on time of day
        int hourOfDay = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        String greeting;
        
        if (hourOfDay >= 0 && hourOfDay < 12) {
            greeting = getString(R.string.good_morning);
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greeting = getString(R.string.good_afternoon);
        } else {
            greeting = getString(R.string.good_evening);
        }
        
        textViewGreeting.setText(greeting);
        
        // Set current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        textViewDate.setText(currentDate);
        
        // Set current season
        String currentSeason = scheduleHelper.getCurrentSeason();
        textViewCurrentSeason.setText(getString(R.string.current_season, currentSeason));
    }

    private void loadLocationAndWeather() {
        if (ActivityCompat.checkSelfPermission(getContext(), 
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            
            locationHelper.getLastLocation(new LocationHelper.LocationCallback() {
                @Override
                public void onLocationResult(Location location, String address) {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            textViewLocation.setText(address);
                            
                            // Get weather for this location
                            weatherHelper.getWeatherForLocation(location.getLatitude(), 
                                    location.getLongitude(), weather -> {
                                if (isAdded() && getActivity() != null) {
                                    getActivity().runOnUiThread(() -> {
                                        textViewWeather.setText(weather);
                                    });
                                }
                            });
                        });
                    }
                }

                @Override
                public void onError(String message) {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            textViewLocation.setText(R.string.location_unavailable);
                            textViewWeather.setText(R.string.weather_unavailable);
                        });
                    }
                }
            });
        } else {
            textViewLocation.setText(R.string.location_unavailable);
            textViewWeather.setText(R.string.weather_unavailable);
        }
    }

    private void loadSeasonalPlants() {
        String currentSeason = scheduleHelper.getCurrentSeason();
        List<Plant> seasonalPlants = plantDao.getSeasonalPlants(currentSeason);
        
        if (seasonalPlants != null && !seasonalPlants.isEmpty()) {
            plantAdapter.updatePlants(seasonalPlants);
        } else {
            // If no seasonal plants are found, show some default plants
            plantAdapter.updatePlants(plantDao.getDefaultPlants());
        }
    }

    private void loadRecommendedGuides() {
        List<GardeningTechnique> recommendedGuides = guideDao.getRecommendedGuides();
        
        if (recommendedGuides != null && !recommendedGuides.isEmpty()) {
            guideAdapter.updateGuides(recommendedGuides);
        } else {
            // If no recommended guides are found, show some default guides
            guideAdapter.updateGuides(guideDao.getDefaultGuides());
        }
    }

    @Override
    public void onPlantClick(Plant plant, int position) {
        // Open plant detail screen or dialog
        // Implementation depends on app navigation structure
    }

    @Override
    public void onBookmarkClick(Plant plant, int position) {
        // Update plant bookmark status in database
        plantDao.updatePlantBookmark(plant.getId(), plant.isBookmarked());
    }

    @Override
    public void onGuideClick(GardeningTechnique guide, int position) {
        // Open guide detail screen or dialog
        // Implementation depends on app navigation structure
    }

    @Override
    public void onFavoriteClick(GardeningTechnique guide, int position) {
        // Update guide favorite status in database
        guideDao.updateGuideFavorite(guide.getId(), guide.isFavorite());
    }
}
