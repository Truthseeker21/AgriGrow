package com.agrigrow.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.adapter.GardeningGuideAdapter;
import com.agrigrow.database.DatabaseHelper;
import com.agrigrow.model.GardeningTechnique;
import com.agrigrow.util.PlantingScheduleHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying gardening guides and techniques
 */
public class GuidesFragment extends Fragment {

    private RecyclerView recyclerViewGuides;
    private TextView textViewSeasonalRecommendation;
    private ChipGroup chipGroupCategories;
    private TextView textViewNoGuides;
    
    private DatabaseHelper databaseHelper;
    private PlantingScheduleHelper plantingScheduleHelper;
    private GardeningGuideAdapter guideAdapter;
    
    private List<GardeningTechnique> allGuides = new ArrayList<>();
    private List<GardeningTechnique> filteredGuides = new ArrayList<>();
    private String currentCategory = "All";
    
    public GuidesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize helpers
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        plantingScheduleHelper = PlantingScheduleHelper.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guides, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        recyclerViewGuides = view.findViewById(R.id.recyclerViewGuides);
        textViewSeasonalRecommendation = view.findViewById(R.id.textViewSeasonalRecommendation);
        chipGroupCategories = view.findViewById(R.id.chipGroupCategories);
        textViewNoGuides = view.findViewById(R.id.textViewNoGuides);
        
        // Setup RecyclerView
        recyclerViewGuides.setLayoutManager(new LinearLayoutManager(getContext()));
        guideAdapter = new GardeningGuideAdapter(filteredGuides, guide -> {
            // Handle guide selection
            Toast.makeText(getContext(), "Selected: " + guide.getTitle(), Toast.LENGTH_SHORT).show();
            // In a real app, this would open a detailed view of the gardening technique
        });
        recyclerViewGuides.setAdapter(guideAdapter);
        
        // Setup category chips
        setupCategoryChips();
        
        // Load seasonal recommendations
        loadSeasonalRecommendations();
        
        // Load guides
        loadGuides();
    }
    
    private void setupCategoryChips() {
        // Get all available categories (in a real app this would come from database)
        List<String> categories = new ArrayList<>();
        categories.add("All");
        categories.add("Planting");
        categories.add("Watering");
        categories.add("Pruning");
        categories.add("Pest Control");
        categories.add("Composting");
        categories.add("Urban");
        categories.add("Indoor");
        categories.add("Seasonal");
        
        // Create chips for each category
        for (String category : categories) {
            Chip chip = new Chip(requireContext());
            chip.setText(category);
            chip.setCheckable(true);
            chip.setClickable(true);
            
            // Set the "All" chip as initially checked
            if (category.equals("All")) {
                chip.setChecked(true);
            }
            
            // Add click listener
            chip.setOnClickListener(v -> {
                // Update current category
                currentCategory = category;
                
                // Filter guides
                filterGuides();
            });
            
            // Add to chip group
            chipGroupCategories.addView(chip);
        }
    }
    
    private void loadSeasonalRecommendations() {
        // Get seasonal planting recommendations
        plantingScheduleHelper.getPlantingRecommendations(new PlantingScheduleHelper.PlantingRecommendationsCallback() {
            @Override
            public void onPlantingRecommendations(List<String> recommendations, String currentSeason) {
                if (recommendations != null && !recommendations.isEmpty()) {
                    String recommendation = recommendations.get(0); // Get the first recommendation
                    textViewSeasonalRecommendation.setText(recommendation);
                } else {
                    textViewSeasonalRecommendation.setText("No seasonal recommendations available at this time.");
                }
            }
            
            @Override
            public void onPlantingRecommendationsError(String errorMessage) {
                textViewSeasonalRecommendation.setText("Unable to get seasonal recommendations.");
            }
        });
    }
    
    private void loadGuides() {
        // In a real app, this would load from database
        // Here we'll add some sample data
        allGuides.clear();
        initSampleGuides();
        
        // Filter guides based on current category
        filterGuides();
    }
    
    private void initSampleGuides() {
        // In a real app, this data would come from a database or API
        allGuides.add(new GardeningTechnique(
                "Container Gardening Basics",
                "Learn how to grow vegetables, herbs and flowers in containers, perfect for urban gardening.",
                "Urban",
                "Beginner",
                "https://example.com/images/container_gardening.jpg"
        ));
        
        allGuides.add(new GardeningTechnique(
                "Proper Watering Techniques",
                "Discover the right way to water different types of plants to ensure healthy growth.",
                "Watering",
                "Beginner",
                "https://example.com/images/watering.jpg"
        ));
        
        allGuides.add(new GardeningTechnique(
                "Composting for Apartment Dwellers",
                "Learn how to create rich compost even in small spaces.",
                "Composting",
                "Intermediate",
                "https://example.com/images/composting.jpg"
        ));
        
        allGuides.add(new GardeningTechnique(
                "Pruning Fruit Trees",
                "Proper pruning techniques to increase fruit yield and maintain healthy trees.",
                "Pruning",
                "Advanced",
                "https://example.com/images/pruning.jpg"
        ));
        
        allGuides.add(new GardeningTechnique(
                "Starting Seeds Indoors",
                "A complete guide to starting seeds indoors to get a jump on the growing season.",
                "Indoor",
                "Beginner",
                "https://example.com/images/seeds.jpg"
        ));
        
        allGuides.add(new GardeningTechnique(
                "Natural Pest Control",
                "Using companion plants and natural remedies to keep pests away from your garden.",
                "Pest Control",
                "Intermediate",
                "https://example.com/images/pest_control.jpg"
        ));
        
        allGuides.add(new GardeningTechnique(
                "Vertical Gardening",
                "Maximize your space by growing up instead of out, perfect for balconies and small yards.",
                "Urban",
                "Intermediate",
                "https://example.com/images/vertical.jpg"
        ));
        
        allGuides.add(new GardeningTechnique(
                "Winter Garden Preparation",
                "How to prepare your garden for winter to ensure healthy plants next spring.",
                "Seasonal",
                "Intermediate",
                "https://example.com/images/winter_prep.jpg"
        ));
        
        allGuides.add(new GardeningTechnique(
                "Succession Planting",
                "Maximize your harvest by planning and planting crops in succession throughout the season.",
                "Planting",
                "Advanced",
                "https://example.com/images/succession.jpg"
        ));
        
        allGuides.add(new GardeningTechnique(
                "Growing Herbs Indoors",
                "A complete guide to growing a variety of herbs indoors year-round.",
                "Indoor",
                "Beginner",
                "https://example.com/images/herbs.jpg"
        ));
    }
    
    private void filterGuides() {
        filteredGuides.clear();
        
        if (currentCategory.equals("All")) {
            // Show all guides
            filteredGuides.addAll(allGuides);
        } else {
            // Filter by category
            for (GardeningTechnique guide : allGuides) {
                if (guide.getCategory().equals(currentCategory)) {
                    filteredGuides.add(guide);
                }
            }
        }
        
        // Update UI
        if (filteredGuides.isEmpty()) {
            textViewNoGuides.setVisibility(View.VISIBLE);
            recyclerViewGuides.setVisibility(View.GONE);
        } else {
            textViewNoGuides.setVisibility(View.GONE);
            recyclerViewGuides.setVisibility(View.VISIBLE);
        }
        
        // Notify adapter of change
        guideAdapter.notifyDataSetChanged();
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