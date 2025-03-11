package com.agrigrow.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.adapter.PlantAdapter;
import com.agrigrow.database.PlantDao;
import com.agrigrow.model.Plant;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that displays various plants suitable for urban gardening.
 */
public class PlantsFragment extends Fragment implements 
        PlantAdapter.OnPlantClickListener {

    private RecyclerView recyclerViewPlants;
    private SearchView searchViewPlants;
    private Spinner spinnerFilterDifficulty;
    private Spinner spinnerFilterSunlight;
    private Spinner spinnerFilterContainer;
    private TextView textViewNoPlants;
    
    private PlantAdapter plantAdapter;
    private PlantDao plantDao;
    
    private List<Plant> allPlants;
    private List<Plant> filteredPlants;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        recyclerViewPlants = view.findViewById(R.id.recyclerViewPlants);
        searchViewPlants = view.findViewById(R.id.searchViewPlants);
        spinnerFilterDifficulty = view.findViewById(R.id.spinnerFilterDifficulty);
        spinnerFilterSunlight = view.findViewById(R.id.spinnerFilterSunlight);
        spinnerFilterContainer = view.findViewById(R.id.spinnerFilterContainer);
        textViewNoPlants = view.findViewById(R.id.textViewNoPlants);
        
        // Initialize DAO
        plantDao = new PlantDao(getContext());
        
        // Initialize lists
        allPlants = new ArrayList<>();
        filteredPlants = new ArrayList<>();
        
        // Set up RecyclerView
        recyclerViewPlants.setLayoutManager(new GridLayoutManager(getContext(), 2));
        plantAdapter = new PlantAdapter(getContext(), filteredPlants, this);
        recyclerViewPlants.setAdapter(plantAdapter);
        
        // Set up filters
        setupFilters();
        
        // Set up search
        setupSearch();
        
        // Load plants
        loadPlants();
    }

    private void setupFilters() {
        // Set up difficulty filter
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.difficulty_levels, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterDifficulty.setAdapter(difficultyAdapter);
        
        // Set up sunlight filter
        ArrayAdapter<CharSequence> sunlightAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sunlight_requirements, android.R.layout.simple_spinner_item);
        sunlightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterSunlight.setAdapter(sunlightAdapter);
        
        // Set up container filter
        ArrayAdapter<CharSequence> containerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.container_options, android.R.layout.simple_spinner_item);
        containerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterContainer.setAdapter(containerAdapter);
        
        // Set up filter listeners
        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        };
        
        spinnerFilterDifficulty.setOnItemSelectedListener(filterListener);
        spinnerFilterSunlight.setOnItemSelectedListener(filterListener);
        spinnerFilterContainer.setOnItemSelectedListener(filterListener);
    }

    private void setupSearch() {
        searchViewPlants.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilters();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyFilters();
                return true;
            }
        });
    }

    private void loadPlants() {
        // Load all plants from database
        allPlants = plantDao.getAllPlants();
        
        // Apply initial filters
        applyFilters();
    }

    private void applyFilters() {
        // Get filter values
        String difficulty = spinnerFilterDifficulty.getSelectedItem().toString();
        String sunlight = spinnerFilterSunlight.getSelectedItem().toString();
        String container = spinnerFilterContainer.getSelectedItem().toString();
        String query = searchViewPlants.getQuery().toString().toLowerCase().trim();
        
        // Apply filters
        filteredPlants.clear();
        
        for (Plant plant : allPlants) {
            boolean difficultyMatch = difficulty.equals(getString(R.string.all)) || 
                    plant.getDifficultyLevel().equals(difficulty);
            boolean sunlightMatch = sunlight.equals(getString(R.string.all)) || 
                    plant.getSunlightRequirements().equals(sunlight);
            boolean containerMatch = container.equals(getString(R.string.all)) || 
                    (container.equals(getString(R.string.container_yes)) && plant.isSuitableForContainers()) ||
                    (container.equals(getString(R.string.container_no)) && !plant.isSuitableForContainers());
            boolean queryMatch = query.isEmpty() || 
                    plant.getName().toLowerCase().contains(query) || 
                    (plant.getScientificName() != null && plant.getScientificName().toLowerCase().contains(query)) ||
                    plant.getDescription().toLowerCase().contains(query);
            
            if (difficultyMatch && sunlightMatch && containerMatch && queryMatch) {
                filteredPlants.add(plant);
            }
        }
        
        // Update adapter
        plantAdapter.updatePlants(filteredPlants);
        
        // Show/hide empty state
        if (filteredPlants.isEmpty()) {
            textViewNoPlants.setVisibility(View.VISIBLE);
            recyclerViewPlants.setVisibility(View.GONE);
        } else {
            textViewNoPlants.setVisibility(View.GONE);
            recyclerViewPlants.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPlantClick(Plant plant, int position) {
        // Implementation to open plant details or show a detail dialog
    }

    @Override
    public void onBookmarkClick(Plant plant, int position) {
        // Update plant bookmark status in database
        plantDao.updatePlantBookmark(plant.getId(), plant.isBookmarked());
    }
}
