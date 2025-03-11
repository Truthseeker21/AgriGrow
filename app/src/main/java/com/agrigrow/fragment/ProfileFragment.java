package com.agrigrow.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.agrigrow.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that displays the user's profile, saved items, and achievements.
 */
public class ProfileFragment extends Fragment implements 
        PlantAdapter.OnPlantClickListener,
        GardeningGuideAdapter.OnGuideClickListener {

    private ImageView imageViewProfile;
    private TextView textViewName;
    private TextView textViewLocation;
    private TextView textViewBio;
    private TextView textViewExperienceLevel;
    private TextView textViewPoints;
    private TextView textViewGardenType;
    private Button buttonEditProfile;
    
    private TabLayout tabLayout;
    private RecyclerView recyclerViewContent;
    private TextView textViewEmptyState;
    
    private PlantAdapter plantAdapter;
    private GardeningGuideAdapter guideAdapter;
    
    private PlantDao plantDao;
    private GardeningTechniqueDao guideDao;
    
    private User currentUser;
    private List<Plant> savedPlants;
    private List<GardeningTechnique> savedGuides;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        textViewName = view.findViewById(R.id.textViewName);
        textViewLocation = view.findViewById(R.id.textViewLocation);
        textViewBio = view.findViewById(R.id.textViewBio);
        textViewExperienceLevel = view.findViewById(R.id.textViewExperienceLevel);
        textViewPoints = view.findViewById(R.id.textViewPoints);
        textViewGardenType = view.findViewById(R.id.textViewGardenType);
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
        
        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerViewContent = view.findViewById(R.id.recyclerViewContent);
        textViewEmptyState = view.findViewById(R.id.textViewEmptyState);
        
        // Initialize DAOs
        plantDao = new PlantDao(getContext());
        guideDao = new GardeningTechniqueDao(getContext());
        
        // Initialize lists
        savedPlants = new ArrayList<>();
        savedGuides = new ArrayList<>();
        
        // Set up RecyclerView
        recyclerViewContent.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Set up tab listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadTabContent(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }
        });
        
        // Set up button listener
        buttonEditProfile.setOnClickListener(v -> {
            // Implementation to open profile edit dialog or screen
            Toast.makeText(getContext(), R.string.edit_profile_not_implemented, Toast.LENGTH_SHORT).show();
        });
        
        // Load user profile
        loadUserProfile();
        
        // Load initial tab content
        loadTabContent(0);
    }

    private void loadUserProfile() {
        // In a real app, this would come from a user repository or database
        // For now, we'll create a sample user
        currentUser = new User("1", "gardener123", "user@example.com", "Urban Gardener");
        currentUser.setLocation("General Santos City");
        currentUser.setBio("Passionate about growing vegetables and herbs in my small balcony garden.");
        currentUser.setExperienceLevel(3);
        currentUser.setPoints(150);
        currentUser.setGardenType("Balcony");
        currentUser.setProfileImageUrl("https://via.placeholder.com/150");
        
        // Update UI with user data
        textViewName.setText(currentUser.getName());
        textViewLocation.setText(currentUser.getLocation());
        textViewBio.setText(currentUser.getBio());
        textViewExperienceLevel.setText(getString(R.string.experience_level_format, currentUser.getExperienceLevel()));
        textViewPoints.setText(getString(R.string.points_format, currentUser.getPoints()));
        textViewGardenType.setText(currentUser.getGardenType());
        
        // Load profile image with Glide
        if (currentUser.getProfileImageUrl() != null && !currentUser.getProfileImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(currentUser.getProfileImageUrl())
                    .apply(new RequestOptions()
                            .circleCrop()
                            .placeholder(R.drawable.placeholder_user)
                            .error(R.drawable.placeholder_user))
                    .into(imageViewProfile);
        } else {
            imageViewProfile.setImageResource(R.drawable.placeholder_user);
        }
    }

    private void loadTabContent(int tabPosition) {
        switch (tabPosition) {
            case 0: // Saved Plants
                loadSavedPlants();
                break;
            case 1: // Saved Guides
                loadSavedGuides();
                break;
            case 2: // Achievements
                loadAchievements();
                break;
        }
    }

    private void loadSavedPlants() {
        // Load saved plants
        if (currentUser != null) {
            savedPlants = plantDao.getPlantsByIds(currentUser.getSavedPlants());
        }
        
        // Set up adapter if needed
        if (!(recyclerViewContent.getAdapter() instanceof PlantAdapter)) {
            plantAdapter = new PlantAdapter(getContext(), savedPlants, this);
            recyclerViewContent.setAdapter(plantAdapter);
        } else {
            plantAdapter.updatePlants(savedPlants);
        }
        
        // Show/hide empty state
        if (savedPlants.isEmpty()) {
            textViewEmptyState.setText(R.string.no_saved_plants);
            textViewEmptyState.setVisibility(View.VISIBLE);
            recyclerViewContent.setVisibility(View.GONE);
        } else {
            textViewEmptyState.setVisibility(View.GONE);
            recyclerViewContent.setVisibility(View.VISIBLE);
        }
    }

    private void loadSavedGuides() {
        // Load saved guides
        if (currentUser != null) {
            savedGuides = guideDao.getGuidesByIds(currentUser.getSavedGuides());
        }
        
        // Set up adapter if needed
        if (!(recyclerViewContent.getAdapter() instanceof GardeningGuideAdapter)) {
            guideAdapter = new GardeningGuideAdapter(getContext(), savedGuides, this);
            recyclerViewContent.setAdapter(guideAdapter);
        } else {
            guideAdapter.updateGuides(savedGuides);
        }
        
        // Show/hide empty state
        if (savedGuides.isEmpty()) {
            textViewEmptyState.setText(R.string.no_saved_guides);
            textViewEmptyState.setVisibility(View.VISIBLE);
            recyclerViewContent.setVisibility(View.GONE);
        } else {
            textViewEmptyState.setVisibility(View.GONE);
            recyclerViewContent.setVisibility(View.VISIBLE);
        }
    }

    private void loadAchievements() {
        // In a real app, this would show user achievements
        // For now, just show a message
        textViewEmptyState.setText(R.string.achievements_coming_soon);
        textViewEmptyState.setVisibility(View.VISIBLE);
        recyclerViewContent.setVisibility(View.GONE);
    }

    @Override
    public void onPlantClick(Plant plant, int position) {
        // Implementation to open plant details or show a detail dialog
    }

    @Override
    public void onBookmarkClick(Plant plant, int position) {
        // Update plant bookmark status
        if (plant.isBookmarked()) {
            // Add to saved plants
            currentUser.savePlant(plant.getId());
        } else {
            // Remove from saved plants
            currentUser.removeSavedPlant(plant.getId());
            
            // Remove from displayed list
            savedPlants.remove(position);
            plantAdapter.updatePlants(savedPlants);
            
            // Show empty state if needed
            if (savedPlants.isEmpty()) {
                textViewEmptyState.setText(R.string.no_saved_plants);
                textViewEmptyState.setVisibility(View.VISIBLE);
                recyclerViewContent.setVisibility(View.GONE);
            }
        }
        
        // Update in database
        plantDao.updatePlantBookmark(plant.getId(), plant.isBookmarked());
    }

    @Override
    public void onGuideClick(GardeningTechnique guide, int position) {
        // Implementation to open guide details or show a detail dialog
    }

    @Override
    public void onFavoriteClick(GardeningTechnique guide, int position) {
        // Update guide favorite status
        if (guide.isFavorite()) {
            // Add to saved guides
            currentUser.saveGuide(guide.getId());
        } else {
            // Remove from saved guides
            currentUser.removeSavedGuide(guide.getId());
            
            // Remove from displayed list
            savedGuides.remove(position);
            guideAdapter.updateGuides(savedGuides);
            
            // Show empty state if needed
            if (savedGuides.isEmpty()) {
                textViewEmptyState.setText(R.string.no_saved_guides);
                textViewEmptyState.setVisibility(View.VISIBLE);
                recyclerViewContent.setVisibility(View.GONE);
            }
        }
        
        // Update in database
        guideDao.updateGuideFavorite(guide.getId(), guide.isFavorite());
    }
}
