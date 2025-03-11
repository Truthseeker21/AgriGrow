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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.adapter.GardeningGuideAdapter;
import com.agrigrow.database.GardeningTechniqueDao;
import com.agrigrow.model.GardeningTechnique;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that displays different gardening techniques and guides.
 */
public class GuidesFragment extends Fragment implements 
        GardeningGuideAdapter.OnGuideClickListener {

    private RecyclerView recyclerViewGuides;
    private SearchView searchViewGuides;
    private Spinner spinnerFilterDifficulty;
    private Spinner spinnerFilterSpace;
    private TextView textViewNoGuides;
    
    private GardeningGuideAdapter guideAdapter;
    private GardeningTechniqueDao guideDao;
    
    private List<GardeningTechnique> allGuides;
    private List<GardeningTechnique> filteredGuides;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guides, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        recyclerViewGuides = view.findViewById(R.id.recyclerViewGuides);
        searchViewGuides = view.findViewById(R.id.searchViewGuides);
        spinnerFilterDifficulty = view.findViewById(R.id.spinnerFilterDifficulty);
        spinnerFilterSpace = view.findViewById(R.id.spinnerFilterSpace);
        textViewNoGuides = view.findViewById(R.id.textViewNoGuides);
        
        // Initialize DAO
        guideDao = new GardeningTechniqueDao(getContext());
        
        // Initialize lists
        allGuides = new ArrayList<>();
        filteredGuides = new ArrayList<>();
        
        // Set up RecyclerView
        recyclerViewGuides.setLayoutManager(new LinearLayoutManager(getContext()));
        guideAdapter = new GardeningGuideAdapter(getContext(), filteredGuides, this);
        recyclerViewGuides.setAdapter(guideAdapter);
        
        // Set up filters
        setupFilters();
        
        // Set up search
        setupSearch();
        
        // Load guides
        loadGuides();
    }

    private void setupFilters() {
        // Set up difficulty filter
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.difficulty_levels, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterDifficulty.setAdapter(difficultyAdapter);
        
        // Set up space filter
        ArrayAdapter<CharSequence> spaceAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.space_requirements, android.R.layout.simple_spinner_item);
        spaceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterSpace.setAdapter(spaceAdapter);
        
        // Set up filter listeners
        spinnerFilterDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        
        spinnerFilterSpace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupSearch() {
        searchViewGuides.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void loadGuides() {
        // Load all gardening guides from database
        allGuides = guideDao.getAllGuides();
        
        // Apply initial filters
        applyFilters();
    }

    private void applyFilters() {
        // Get filter values
        String difficulty = spinnerFilterDifficulty.getSelectedItem().toString();
        String space = spinnerFilterSpace.getSelectedItem().toString();
        String query = searchViewGuides.getQuery().toString().toLowerCase().trim();
        
        // Apply filters
        filteredGuides.clear();
        
        for (GardeningTechnique guide : allGuides) {
            boolean difficultyMatch = difficulty.equals(getString(R.string.all)) || 
                    guide.getDifficultyLevel().equals(difficulty);
            boolean spaceMatch = space.equals(getString(R.string.all)) || 
                    guide.getSpaceRequirement().equals(space);
            boolean queryMatch = query.isEmpty() || 
                    guide.getName().toLowerCase().contains(query) || 
                    guide.getDescription().toLowerCase().contains(query);
            
            if (difficultyMatch && spaceMatch && queryMatch) {
                filteredGuides.add(guide);
            }
        }
        
        // Update adapter
        guideAdapter.updateGuides(filteredGuides);
        
        // Show/hide empty state
        if (filteredGuides.isEmpty()) {
            textViewNoGuides.setVisibility(View.VISIBLE);
            recyclerViewGuides.setVisibility(View.GONE);
        } else {
            textViewNoGuides.setVisibility(View.GONE);
            recyclerViewGuides.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGuideClick(GardeningTechnique guide, int position) {
        // Implementation to open guide details or show a detail dialog
    }

    @Override
    public void onFavoriteClick(GardeningTechnique guide, int position) {
        // Update guide favorite status in database
        guideDao.updateGuideFavorite(guide.getId(), guide.isFavorite());
    }
}
