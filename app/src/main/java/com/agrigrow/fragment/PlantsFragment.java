package com.agrigrow.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.agrigrow.R;
import com.agrigrow.adapter.PlantAdapter;
import com.agrigrow.database.PlantDao;
import com.agrigrow.model.Plant;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Fragment for displaying and interacting with plants in the application.
 * This fragment shows a catalog of plants with filtering options, search, and bookmark functionality.
 */
public class PlantsFragment extends Fragment implements PlantAdapter.OnPlantClickListener {

    private RecyclerView recyclerViewPlants;
    private PlantAdapter plantAdapter;
    private ProgressBar progressBar;
    private TextView textViewEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private ChipGroup chipGroupCategories;
    private TabLayout tabLayout;
    private FloatingActionButton fabAddPlant;

    private List<Plant> allPlants = new ArrayList<>();
    private List<Plant> filteredPlants = new ArrayList<>();
    private String currentSearchQuery = "";
    private String currentCategory = "";
    private int currentTabPosition = 0;

    private PlantDao plantDao;  // This would be injected in a real app
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupRecyclerView();
        setupListeners();
        loadPlants();
    }

    private void initViews(View view) {
        recyclerViewPlants = view.findViewById(R.id.recyclerViewPlants);
        progressBar = view.findViewById(R.id.progressBar);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        searchView = view.findViewById(R.id.searchView);
        chipGroupCategories = view.findViewById(R.id.chipGroupCategories);
        tabLayout = view.findViewById(R.id.tabLayout);
        fabAddPlant = view.findViewById(R.id.fabAddPlant);
    }

    private void setupRecyclerView() {
        // Use a grid layout with 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewPlants.setLayoutManager(layoutManager);
        
        plantAdapter = new PlantAdapter(getContext(), this);
        recyclerViewPlants.setAdapter(plantAdapter);
    }

    private void setupListeners() {
        // Set up swipe refresh
        swipeRefreshLayout.setOnRefreshListener(this::refreshPlants);
        
        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                filterPlants();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                filterPlants();
                return true;
            }
        });
        
        // Set up category filter
        chipGroupCategories.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipAll) {
                currentCategory = "";
            } else if (checkedId == R.id.chipVegetables) {
                currentCategory = "Vegetables";
            } else if (checkedId == R.id.chipHerbs) {
                currentCategory = "Herbs";
            } else if (checkedId == R.id.chipFruits) {
                currentCategory = "Fruits";
            } else if (checkedId == R.id.chipFlowers) {
                currentCategory = "Flowers";
            } else if (checkedId == R.id.chipIndoor) {
                currentCategory = "Indoor";
            }
            filterPlants();
        });
        
        // Set up tab selection (All Plants, Bookmarked, My Garden)
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
                filterPlants();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed
            }
        });
        
        // Set up add plant button
        fabAddPlant.setOnClickListener(v -> showAddPlantDialog());
    }

    private void showAddPlantDialog() {
        // This would open the add plant dialog in a real implementation
        Toast.makeText(getContext(), "Add Plant feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    private void loadPlants() {
        showLoading(true);
        
        // In a real app, this would use LiveData or RxJava with Room
        executor.execute(() -> {
            // Simulating database fetch with a delay
            try {
                Thread.sleep(1000);
                
                // In a real app, this would be fetched from a database
                List<Plant> plants = generateSamplePlants();
                
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        allPlants = plants;
                        filterPlants();
                        showLoading(false);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        showError("Failed to load plants");
                    });
                }
            }
        });
    }
    
    private void refreshPlants() {
        loadPlants();
    }
    
    private void filterPlants() {
        filteredPlants = new ArrayList<>();
        
        for (Plant plant : allPlants) {
            // Apply tab filter
            if (currentTabPosition == 1 && !plant.isBookmarked()) {
                continue;  // Skip non-bookmarked plants when on Bookmarked tab
            } else if (currentTabPosition == 2 && !plant.isInGarden()) {
                continue;  // Skip plants not in garden when on My Garden tab
            }
            
            // Apply category filter
            if (!currentCategory.isEmpty() && !plant.getCategory().equals(currentCategory)) {
                continue;  // Skip plants not matching the selected category
            }
            
            // Apply search filter
            if (!currentSearchQuery.isEmpty()) {
                String lowerCaseQuery = currentSearchQuery.toLowerCase();
                if (!plant.getName().toLowerCase().contains(lowerCaseQuery) && 
                    !plant.getDescription().toLowerCase().contains(lowerCaseQuery)) {
                    continue;  // Skip plants not matching the search query
                }
            }
            
            // Plant passed all filters, add it to the filtered list
            filteredPlants.add(plant);
        }
        
        // Update UI with filtered plants
        plantAdapter.setPlants(filteredPlants);
        
        // Show empty state if no plants match the filters
        if (filteredPlants.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
        }
        
        // Stop refresh animation if it's running
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
    
    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        textViewEmpty.setText("Error: " + message);
        textViewEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPlantClick(Plant plant) {
        // Open plant detail screen
        Toast.makeText(getContext(), "Selected: " + plant.getName(), Toast.LENGTH_SHORT).show();
        // In a real app, this would navigate to a plant detail fragment/activity
    }

    @Override
    public void onBookmarkClick(Plant plant, boolean isBookmarked) {
        // Update plant bookmark status in database
        executor.execute(() -> {
            // In a real app, this would update the database
            plant.setBookmarked(isBookmarked);
            
            // Show confirmation
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    String message = isBookmarked ? 
                            "Added " + plant.getName() + " to bookmarks" : 
                            "Removed " + plant.getName() + " from bookmarks";
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    
                    // If on the Bookmarked tab, refresh the list
                    if (currentTabPosition == 1) {
                        filterPlants();
                    }
                });
            }
        });
    }
    
    /**
     * Generates a list of sample plant data for testing the UI
     * This would be replaced with actual database queries in a production app
     */
    private List<Plant> generateSamplePlants() {
        List<Plant> plants = new ArrayList<>();
        
        // Sample plant data for UI development
        Plant tomato = new Plant("Tomato", "A popular garden vegetable that produces red, juicy fruits. Perfect for salads and cooking.", "Vegetables");
        tomato.setSunlightRequirement("Full Sun");
        tomato.setWateringFrequency(2); // Medium watering
        tomato.setDifficultyLevel(2); // Easy to moderate
        tomato.setImageUrl("https://example.com/tomato.jpg");
        plants.add(tomato);
        
        Plant basil = new Plant("Basil", "An aromatic herb used in Italian and Thai cooking. Grows well in containers.", "Herbs");
        basil.setSunlightRequirement("Partial to Full Sun");
        basil.setWateringFrequency(2); // Medium watering
        basil.setDifficultyLevel(1); // Easy
        basil.setImageUrl("https://example.com/basil.jpg");
        plants.add(basil);
        
        Plant rose = new Plant("Rose", "Beautiful flowering plant with fragrant blooms. Available in many colors.", "Flowers");
        rose.setSunlightRequirement("Full Sun");
        rose.setWateringFrequency(2); // Medium watering
        rose.setDifficultyLevel(3); // Moderate
        rose.setImageUrl("https://example.com/rose.jpg");
        rose.setBookmarked(true);
        plants.add(rose);
        
        Plant strawberry = new Plant("Strawberry", "Sweet red berries that grow on small plants. Great for containers or garden beds.", "Fruits");
        strawberry.setSunlightRequirement("Full Sun");
        strawberry.setWateringFrequency(2); // Medium watering
        strawberry.setDifficultyLevel(2); // Easy to moderate
        strawberry.setImageUrl("https://example.com/strawberry.jpg");
        strawberry.setInGarden(true);
        plants.add(strawberry);
        
        Plant peaceLily = new Plant("Peace Lily", "Popular indoor plant with white flowers. Cleans the air and thrives in low light.", "Indoor");
        peaceLily.setSunlightRequirement("Low to Indirect");
        peaceLily.setWateringFrequency(1); // Low watering
        peaceLily.setDifficultyLevel(1); // Easy
        peaceLily.setImageUrl("https://example.com/peace_lily.jpg");
        peaceLily.setBookmarked(true);
        plants.add(peaceLily);
        
        Plant cucumber = new Plant("Cucumber", "Refreshing vegetable that grows on vines. Best grown in summer.", "Vegetables");
        cucumber.setSunlightRequirement("Full Sun");
        cucumber.setWateringFrequency(3); // High watering
        cucumber.setDifficultyLevel(2); // Easy to moderate
        cucumber.setImageUrl("https://example.com/cucumber.jpg");
        cucumber.setInGarden(true);
        plants.add(cucumber);
        
        Plant mint = new Plant("Mint", "Fragrant herb that spreads quickly. Great for teas and desserts.", "Herbs");
        mint.setSunlightRequirement("Partial Sun");
        mint.setWateringFrequency(2); // Medium watering
        mint.setDifficultyLevel(1); // Easy
        mint.setImageUrl("https://example.com/mint.jpg");
        plants.add(mint);
        
        Plant sunflower = new Plant("Sunflower", "Tall flowers with bright yellow petals. Attracts birds and bees.", "Flowers");
        sunflower.setSunlightRequirement("Full Sun");
        sunflower.setWateringFrequency(2); // Medium watering
        sunflower.setDifficultyLevel(1); // Easy
        sunflower.setImageUrl("https://example.com/sunflower.jpg");
        plants.add(sunflower);
        
        return plants;
    }
}