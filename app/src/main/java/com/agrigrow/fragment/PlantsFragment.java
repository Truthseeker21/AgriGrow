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
        
        // Tropical Vegetables & Herbs for Urban Gardening
        Plant pechay = new Plant("Pechay", "Popular leafy vegetable that grows quickly in containers in tropical climates.", "Vegetables");
        pechay.setSunlightRequirement("Partial to Full Sun");
        pechay.setWateringFrequency(2); // Medium watering
        pechay.setDifficultyLevel(1); // Easy
        pechay.setImageUrl("https://example.com/pechay.jpg");
        pechay.setGrowingSeason("Year-round");
        pechay.setInGarden(true);
        plants.add(pechay);
        
        Plant kangkong = new Plant("Kangkong", "Water spinach that grows well in hot, humid climate and can be harvested multiple times.", "Vegetables");
        kangkong.setSunlightRequirement("Full Sun");
        kangkong.setWateringFrequency(3); // High watering
        kangkong.setDifficultyLevel(1); // Easy
        kangkong.setImageUrl("https://example.com/kangkong.jpg");
        kangkong.setGrowingSeason("Year-round");
        plants.add(kangkong);
        
        Plant sili = new Plant("Sili", "Hot chili pepper that grows as a perennial in tropical climates, good for container gardening.", "Vegetables");
        sili.setSunlightRequirement("Full Sun");
        sili.setWateringFrequency(2); // Medium watering
        sili.setDifficultyLevel(1); // Easy
        sili.setImageUrl("https://example.com/sili.jpg");
        sili.setGrowingSeason("Year-round");
        plants.add(sili);
        
        Plant malunggay = new Plant("Malunggay", "Nutritious tree with edible leaves, thrives in tropical climate and can be grown in large containers.", "Vegetables");
        malunggay.setSunlightRequirement("Full Sun");
        malunggay.setWateringFrequency(2); // Medium watering
        malunggay.setDifficultyLevel(1); // Easy
        malunggay.setImageUrl("https://example.com/malunggay.jpg");
        malunggay.setGrowingSeason("Year-round");
        malunggay.setBookmarked(true);
        plants.add(malunggay);
        
        Plant alugbati = new Plant("Alugbati", "Vine spinach that grows well in hot weather and is rich in vitamins and minerals.", "Vegetables");
        alugbati.setSunlightRequirement("Partial to Full Sun");
        alugbati.setWateringFrequency(2); // Medium watering
        alugbati.setDifficultyLevel(1); // Easy
        alugbati.setImageUrl("https://example.com/alugbati.jpg");
        alugbati.setGrowingSeason("Year-round");
        plants.add(alugbati);
        
        Plant kamoteTops = new Plant("Kamote Tops", "Sweet potato leaves that are easy to grow and can be harvested repeatedly.", "Vegetables");
        kamoteTops.setSunlightRequirement("Full Sun");
        kamoteTops.setWateringFrequency(2); // Medium watering
        kamoteTops.setDifficultyLevel(1); // Easy
        kamoteTops.setImageUrl("https://example.com/kamote_tops.jpg");
        kamoteTops.setGrowingSeason("Year-round");
        plants.add(kamoteTops);
        
        Plant okra = new Plant("Okra", "Tropical vegetable that grows well in hot weather and requires minimal care.", "Vegetables");
        okra.setSunlightRequirement("Full Sun");
        okra.setWateringFrequency(2); // Medium watering
        okra.setDifficultyLevel(1); // Easy
        okra.setImageUrl("https://example.com/okra.jpg");
        okra.setGrowingSeason("Year-round");
        plants.add(okra);
        
        Plant calamansi = new Plant("Calamansi", "Tropical citrus that can be grown in containers and used for cooking and beverages.", "Fruits");
        calamansi.setSunlightRequirement("Full Sun");
        calamansi.setWateringFrequency(2); // Medium watering
        calamansi.setDifficultyLevel(2); // Moderate
        calamansi.setImageUrl("https://example.com/calamansi.jpg");
        calamansi.setGrowingSeason("Year-round");
        calamansi.setInGarden(true);
        plants.add(calamansi);
        
        // Tropical Ornamental Flowers
        Plant sampaguita = new Plant("Sampaguita", "A jasmine variety with sweet fragrance and small white blooms, popular in tropical gardens.", "Flowers");
        sampaguita.setSunlightRequirement("Full Sun to Partial Shade");
        sampaguita.setWateringFrequency(2); // Medium watering
        sampaguita.setDifficultyLevel(1); // Easy
        sampaguita.setImageUrl("https://example.com/sampaguita.jpg");
        sampaguita.setGrowingSeason("Year-round");
        sampaguita.setBookmarked(true);
        plants.add(sampaguita);
        
        Plant gumamela = new Plant("Gumamela", "Popular tropical hibiscus flower with large, colorful blooms perfect for urban gardens.", "Flowers");
        gumamela.setSunlightRequirement("Full Sun");
        gumamela.setWateringFrequency(2); // Medium watering
        gumamela.setDifficultyLevel(1); // Easy
        gumamela.setImageUrl("https://example.com/gumamela.jpg");
        gumamela.setGrowingSeason("Year-round");
        plants.add(gumamela);
        
        Plant bougainvillea = new Plant("Bougainvillea", "Drought-tolerant flowering vine with vibrant paper-like bracts, perfect for urban gardens.", "Flowers");
        bougainvillea.setSunlightRequirement("Full Sun");
        bougainvillea.setWateringFrequency(1); // Low watering
        bougainvillea.setDifficultyLevel(1); // Easy
        bougainvillea.setImageUrl("https://example.com/bougainvillea.jpg");
        bougainvillea.setGrowingSeason("Year-round");
        plants.add(bougainvillea);
        
        Plant santan = new Plant("Santan", "Common tropical garden shrub (Ixora) with clusters of star-shaped flowers in red, orange, or yellow.", "Flowers");
        santan.setSunlightRequirement("Full Sun to Partial Shade");
        santan.setWateringFrequency(2); // Medium watering
        santan.setDifficultyLevel(1); // Easy
        santan.setImageUrl("https://example.com/santan.jpg");
        santan.setGrowingSeason("Year-round");
        plants.add(santan);
        
        Plant rosal = new Plant("Rosal", "Fragrant white gardenia flowers with glossy green leaves, perfect for small garden spaces.", "Flowers");
        rosal.setSunlightRequirement("Partial Shade");
        rosal.setWateringFrequency(2); // Medium watering
        rosal.setDifficultyLevel(2); // Moderate
        rosal.setImageUrl("https://example.com/rosal.jpg");
        rosal.setGrowingSeason("Year-round");
        rosal.setBookmarked(true);
        plants.add(rosal);
        
        Plant marigold = new Plant("Marigold", "Easy to grow flowers that thrive in warm climates and help repel garden pests.", "Flowers");
        marigold.setSunlightRequirement("Full Sun");
        marigold.setWateringFrequency(2); // Medium watering
        marigold.setDifficultyLevel(1); // Easy
        marigold.setImageUrl("https://example.com/marigold.jpg");
        marigold.setGrowingSeason("Year-round");
        plants.add(marigold);
        
        // Common Urban Plants
        Plant peaceLily = new Plant("Peace Lily", "Popular indoor plant with white flowers. Cleans the air and thrives in low light.", "Indoor");
        peaceLily.setSunlightRequirement("Low to Indirect");
        peaceLily.setWateringFrequency(1); // Low watering
        peaceLily.setDifficultyLevel(1); // Easy
        peaceLily.setImageUrl("https://example.com/peace_lily.jpg");
        peaceLily.setGrowingSeason("Year-round");
        peaceLily.setBookmarked(true);
        plants.add(peaceLily);
        
        Plant basil = new Plant("Basil", "An aromatic herb used in various cuisines around the world. Grows well in containers.", "Herbs");
        basil.setSunlightRequirement("Partial to Full Sun");
        basil.setWateringFrequency(2); // Medium watering
        basil.setDifficultyLevel(1); // Easy
        basil.setImageUrl("https://example.com/basil.jpg");
        basil.setGrowingSeason("Year-round");
        plants.add(basil);
        
        return plants;
    }
}