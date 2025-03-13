package com.agrigrow.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.adapter.PlantAdapter;
import com.agrigrow.database.DatabaseHelper;
import com.agrigrow.model.GardenTip;
import com.agrigrow.model.Plant;
import com.agrigrow.util.PlantingScheduleHelper;
import com.agrigrow.util.WeatherHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Home screen fragment displaying a dashboard with various garden information.
 */
public class HomeFragment extends Fragment {

    private TextView textViewDate;
    private TextView textViewGreeting;
    private TextView textViewWeatherStatus;
    private TextView textViewTemperature;
    private TextView textViewTodayTip;
    private TextView textViewNoPlants;
    private ImageView imageViewWeatherIcon;
    private CardView cardViewWeather;
    private CardView cardViewTip;
    private RecyclerView recyclerViewRecentPlants;
    private CardView cardViewAddPlant;
    private CardView cardViewIdentifyPlant;
    private CardView cardViewViewAllPlants;
    private CardView cardViewCommunity;
    
    private DatabaseHelper databaseHelper;
    private WeatherHelper weatherHelper;
    private PlantingScheduleHelper plantingScheduleHelper;
    private PlantAdapter recentPlantsAdapter;
    
    private List<Plant> recentPlants = new ArrayList<>();
    private List<GardenTip> gardenTips = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize helpers
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        weatherHelper = WeatherHelper.getInstance(requireContext());
        plantingScheduleHelper = PlantingScheduleHelper.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        textViewDate = view.findViewById(R.id.textViewDate);
        textViewGreeting = view.findViewById(R.id.textViewGreeting);
        textViewWeatherStatus = view.findViewById(R.id.textViewWeatherStatus);
        textViewTemperature = view.findViewById(R.id.textViewTemperature);
        textViewTodayTip = view.findViewById(R.id.textViewTodayTip);
        textViewNoPlants = view.findViewById(R.id.textViewNoPlants);
        imageViewWeatherIcon = view.findViewById(R.id.imageViewWeatherIcon);
        cardViewWeather = view.findViewById(R.id.cardViewWeather);
        cardViewTip = view.findViewById(R.id.cardViewTip);
        recyclerViewRecentPlants = view.findViewById(R.id.recyclerViewRecentPlants);
        cardViewAddPlant = view.findViewById(R.id.cardViewAddPlant);
        cardViewIdentifyPlant = view.findViewById(R.id.cardViewIdentifyPlant);
        cardViewViewAllPlants = view.findViewById(R.id.cardViewViewAllPlants);
        cardViewCommunity = view.findViewById(R.id.cardViewCommunity);
        
        // Setup RecyclerView
        recyclerViewRecentPlants.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recentPlantsAdapter = new PlantAdapter(recentPlants, plant -> {
            // Handle plant click - would navigate to plant detail in a real app
            Toast.makeText(getContext(), "Selected: " + plant.getName(), Toast.LENGTH_SHORT).show();
        });
        recyclerViewRecentPlants.setAdapter(recentPlantsAdapter);
        
        // Set up date and greeting
        setupDateAndGreeting();
        
        // Set up weather summary
        loadWeatherData();
        
        // Set up garden tip of the day
        loadGardenTip();
        
        // Load recent plants
        loadRecentPlants();
        
        // Set up card click listeners
        setupCardClickListeners();
    }
    
    private void setupDateAndGreeting() {
        // Get current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date());
        textViewDate.setText(formattedDate);
        
        // Set greeting based on time of day
        int hourOfDay = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        String greeting;
        
        if (hourOfDay < 12) {
            greeting = "Good Morning";
        } else if (hourOfDay < 18) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }
        
        textViewGreeting.setText(greeting);
    }
    
    private void loadWeatherData() {
        // Show loading state
        textViewWeatherStatus.setText("Loading weather...");
        textViewTemperature.setText("");
        
        // Get weather data
        weatherHelper.getCurrentWeather(new WeatherHelper.OnWeatherDataListener() {
            @Override
            public void onWeatherData(WeatherHelper.WeatherData weatherData) {
                // Update UI with weather data
                textViewWeatherStatus.setText(weatherData.conditionDescription);
                textViewTemperature.setText(weatherData.getTemperatureString());
                
                // Load weather icon
                com.bumptech.glide.Glide.with(HomeFragment.this)
                        .load(weatherData.getIconUrl())
                        .into(imageViewWeatherIcon);
                
                // Make the weather card clickable to navigate to weather detail
                cardViewWeather.setOnClickListener(v -> {
                    navigateToWeather();
                });
            }
            
            @Override
            public void onWeatherError(String errorMessage) {
                textViewWeatherStatus.setText("Weather unavailable");
                // In a real app, we'd handle the error gracefully
            }
        });
    }
    
    private void loadGardenTip() {
        // In a real app, tips would come from a database or API
        // Here we'll create a few hardcoded tips
        initGardenTips();
        
        // Display a random tip
        if (!gardenTips.isEmpty()) {
            int randomIndex = new Random().nextInt(gardenTips.size());
            GardenTip tip = gardenTips.get(randomIndex);
            textViewTodayTip.setText(tip.getTip());
            
            // Make the tip card clickable to navigate to guides
            cardViewTip.setOnClickListener(v -> {
                navigateToGuides();
            });
        }
    }
    
    private void initGardenTips() {
        // General gardening tips
        gardenTips.add(new GardenTip("Water your plants at the base, not from above, to prevent disease."));
        gardenTips.add(new GardenTip("Rotate your plants each season to prevent soil depletion and disease."));
        gardenTips.add(new GardenTip("Companion planting can help deter pests naturally without chemicals."));
        gardenTips.add(new GardenTip("Mulching helps retain soil moisture and suppresses weeds."));
        gardenTips.add(new GardenTip("Save seeds from your strongest, most productive plants for next season."));
        gardenTips.add(new GardenTip("Coffee grounds make excellent fertilizer for acid-loving plants."));
        gardenTips.add(new GardenTip("Planting herbs among vegetables can help repel pests naturally."));
        gardenTips.add(new GardenTip("Early morning is the best time to water plants to minimize evaporation."));
        gardenTips.add(new GardenTip("Add compost to improve soil structure and fertility."));
        gardenTips.add(new GardenTip("Plant native species to attract beneficial insects and promote biodiversity."));
        
        // Tropical urban gardening tips
        gardenTips.add(new GardenTip("Use rainwater collection systems during rainy seasons to conserve water for dry periods."));
        gardenTips.add(new GardenTip("Plant marigolds alongside vegetables to help repel harmful insects common in tropical climates."));
        gardenTips.add(new GardenTip("Grow leafy greens for quick harvests - many varieties are ready in just 3-4 weeks in tropical climates."));
        gardenTips.add(new GardenTip("Create shade cloth shelters for plants during the hottest hours in summer months."));
        gardenTips.add(new GardenTip("Use protective netting as barriers for plants, especially during rainy season to prevent pest infestations."));
        gardenTips.add(new GardenTip("Moringa (Malunggay) cuttings root easily in water - a simple way to propagate this nutritious plant."));
        gardenTips.add(new GardenTip("Hibiscus can be propagated easily through stem cuttings in warm tropical climates."));
        gardenTips.add(new GardenTip("Keep containers elevated during rainy season to prevent waterlogging and root rot in heavy rainfall."));
        gardenTips.add(new GardenTip("Bougainvillea thrives in heat and requires less water, perfect for hot urban areas with limited space."));
        gardenTips.add(new GardenTip("Citrus trees can be kept small through regular pruning, making them perfect for container gardening."));
        gardenTips.add(new GardenTip("Plant tropical flowering plants to attract beneficial pollinators to your urban garden."));
        gardenTips.add(new GardenTip("Coconut coir is an excellent growing medium that retains moisture well in hot conditions."));
        gardenTips.add(new GardenTip("Rotate your leafy greens every 3-4 weeks for continuous harvest in tropical gardens."));
        gardenTips.add(new GardenTip("Used coffee grounds make excellent fertilizer for acid-loving plants like gardenia."));
    }
    
    private void loadRecentPlants() {
        // Load plants from database
        List<Plant> allPlants = databaseHelper.getPlantDao().getAllPlants();
        
        if (allPlants != null && !allPlants.isEmpty()) {
            // Show the plants
            textViewNoPlants.setVisibility(View.GONE);
            recyclerViewRecentPlants.setVisibility(View.VISIBLE);
            
            // Take the most recent plants (up to 5)
            recentPlants.clear();
            int count = Math.min(allPlants.size(), 5);
            for (int i = 0; i < count; i++) {
                recentPlants.add(allPlants.get(i));
            }
            
            // Notify adapter
            recentPlantsAdapter.notifyDataSetChanged();
        } else {
            // No plants, show empty state
            textViewNoPlants.setVisibility(View.VISIBLE);
            recyclerViewRecentPlants.setVisibility(View.GONE);
        }
    }
    
    private void setupCardClickListeners() {
        // Add plant card
        cardViewAddPlant.setOnClickListener(v -> {
            navigateToPlants();
        });
        
        // Identify plant card
        cardViewIdentifyPlant.setOnClickListener(v -> {
            navigateToIdentify();
        });
        
        // View all plants card
        cardViewViewAllPlants.setOnClickListener(v -> {
            navigateToPlants();
        });
        
        // Community card
        cardViewCommunity.setOnClickListener(v -> {
            navigateToForum();
        });
    }
    
    private void navigateToWeather() {
        Fragment fragment = new WeatherFragment();
        replaceFragment(fragment);
    }
    
    private void navigateToPlants() {
        Fragment fragment = new PlantsFragment();
        replaceFragment(fragment);
    }
    
    private void navigateToIdentify() {
        Fragment fragment = new PlantIdentifyFragment();
        replaceFragment(fragment);
    }
    
    private void navigateToForum() {
        Fragment fragment = new ForumFragment();
        replaceFragment(fragment);
    }
    
    private void navigateToGuides() {
        Fragment fragment = new GuidesFragment();
        replaceFragment(fragment);
    }
    
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
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