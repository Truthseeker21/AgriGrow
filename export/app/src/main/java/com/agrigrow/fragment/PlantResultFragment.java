package com.agrigrow.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.adapter.AlternativePlantAdapter;
import com.agrigrow.database.DatabaseHelper;
import com.agrigrow.model.Plant;
import com.agrigrow.util.PlantIdentificationHelper;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display plant identification results
 */
public class PlantResultFragment extends Fragment implements AlternativePlantAdapter.OnAlternativePlantClickListener {

    private static final String TAG = "PlantResultFragment";
    
    // Static field to store last identification results
    private static List<PlantIdentificationHelper.PlantIdentificationResult> lastIdentificationResults;
    
    private Uri plantImageUri;
    private PlantIdentificationHelper.PlantIdentificationResult mainResult;
    private List<PlantIdentificationHelper.PlantIdentificationResult> alternativeResults;
    
    private ImageView imageViewPlant;
    private TextView textViewConfidence;
    private TextView textViewCommonName;
    private TextView textViewScientificName;
    private TextView textViewFamily;
    private TextView textViewDescription;
    private TextView textViewEdibleParts;
    private CardView cardViewEdibleParts;
    private Button buttonAddToGarden;
    private Button buttonTryAgain;
    private RecyclerView recyclerViewAlternatives;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    
    private AlternativePlantAdapter alternativePlantAdapter;
    private DatabaseHelper databaseHelper;

    public PlantResultFragment() {
        // Required empty public constructor
    }
    
    /**
     * Store identification results to be accessed by the results fragment
     * @param results The plant identification results
     */
    public static void setLastIdentificationResults(List<PlantIdentificationHelper.PlantIdentificationResult> results) {
        lastIdentificationResults = results;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get database helper
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        
        // Get arguments
        if (getArguments() != null) {
            plantImageUri = getArguments().getParcelable("PLANT_IMAGE_URI");
        }
        
        // Get identification results
        if (lastIdentificationResults != null && !lastIdentificationResults.isEmpty()) {
            mainResult = lastIdentificationResults.get(0); // First result is most likely match
            
            // If there are more results, add them to alternatives
            if (lastIdentificationResults.size() > 1) {
                alternativeResults = new ArrayList<>(lastIdentificationResults.subList(1, lastIdentificationResults.size()));
            } else {
                alternativeResults = new ArrayList<>();
            }
        } else {
            // No results, handle error
            Toast.makeText(getContext(), "Error: No identification results available", Toast.LENGTH_SHORT).show();
            // Go back to previous fragment
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plant_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        imageViewPlant = view.findViewById(R.id.imageViewPlant);
        textViewConfidence = view.findViewById(R.id.textViewConfidence);
        textViewCommonName = view.findViewById(R.id.textViewCommonName);
        textViewScientificName = view.findViewById(R.id.textViewScientificName);
        textViewFamily = view.findViewById(R.id.textViewFamily);
        textViewDescription = view.findViewById(R.id.textViewDescription);
        textViewEdibleParts = view.findViewById(R.id.textViewEdibleParts);
        cardViewEdibleParts = view.findViewById(R.id.cardViewEdibleParts);
        buttonAddToGarden = view.findViewById(R.id.buttonAddToGarden);
        buttonTryAgain = view.findViewById(R.id.buttonTryAgain);
        recyclerViewAlternatives = view.findViewById(R.id.recyclerViewAlternatives);
        collapsingToolbar = view.findViewById(R.id.collapsingToolbar);
        toolbar = view.findViewById(R.id.toolbar);
        
        // Setup toolbar
        if (getActivity() != null) {
            toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel);
            toolbar.setNavigationOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());
        }
        
        // Setup recycler view
        recyclerViewAlternatives.setLayoutManager(new LinearLayoutManager(getContext()));
        alternativePlantAdapter = new AlternativePlantAdapter(alternativeResults, this);
        recyclerViewAlternatives.setAdapter(alternativePlantAdapter);
        
        // Display plant image
        if (plantImageUri != null) {
            // Load from URI
            Glide.with(this)
                    .load(plantImageUri)
                    .centerCrop()
                    .into(imageViewPlant);
        } else if (mainResult != null && mainResult.getFirstImageUrl() != null) {
            // Load from URL
            Glide.with(this)
                    .load(mainResult.getFirstImageUrl())
                    .centerCrop()
                    .into(imageViewPlant);
        }
        
        // Display plant details
        if (mainResult != null) {
            displayPlantDetails(mainResult);
        }
        
        // Set click listeners
        buttonAddToGarden.setOnClickListener(v -> addPlantToGarden());
        buttonTryAgain.setOnClickListener(v -> tryAgain());
    }
    
    private void displayPlantDetails(PlantIdentificationHelper.PlantIdentificationResult result) {
        // Set title
        collapsingToolbar.setTitle(result.getDisplayName());
        
        // Set confidence
        textViewConfidence.setText("Confidence: " + result.getConfidenceString());
        
        // Set common name
        textViewCommonName.setText(result.getCommonNamesString());
        
        // Set scientific name
        textViewScientificName.setText(Html.fromHtml(result.getFormattedScientificName()));
        
        // Set family
        textViewFamily.setText(result.getTaxonomyString());
        
        // Set description
        if (result.description != null && !result.description.isEmpty()) {
            textViewDescription.setText(result.description);
        } else {
            textViewDescription.setText("No description available for this plant.");
        }
        
        // Set edible parts
        if (result.edibleParts != null && !result.edibleParts.isEmpty()) {
            textViewEdibleParts.setText(result.getEdiblePartsString());
            cardViewEdibleParts.setVisibility(View.VISIBLE);
        } else {
            cardViewEdibleParts.setVisibility(View.GONE);
        }
    }
    
    private void addPlantToGarden() {
        if (mainResult != null) {
            // Create a new Plant object
            Plant plant = new Plant();
            plant.setName(mainResult.getDisplayName());
            plant.setScientificName(mainResult.scientificName);
            plant.setDescription(mainResult.description);
            plant.setFamilyName(mainResult.family);
            plant.setImageUrl(mainResult.getFirstImageUrl());
            
            // TODO: Add plant care information based on identified plant
            plant.setWaterNeeds("Medium");
            plant.setSunlightNeeds("Full Sun");
            plant.setGrowthHabit("Upright");
            plant.setSeasonality("Perennial");
            
            // Save plant to database
            databaseHelper.getPlantDao().insertPlant(plant);
            
            Toast.makeText(getContext(), mainResult.getDisplayName() + " added to your garden!", Toast.LENGTH_SHORT).show();
            
            // Navigate to plants fragment
            if (getActivity() != null) {
                // Navigate back to plants fragment
                Fragment plantsFragment = new PlantsFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, plantsFragment);
                transaction.commit();
            }
        }
    }
    
    private void tryAgain() {
        if (getActivity() != null) {
            // Go back to identification fragment
            getActivity().getSupportFragmentManager().popBackStack();
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
    
    @Override
    public void onAlternativePlantClick(int position) {
        if (alternativeResults != null && position < alternativeResults.size()) {
            // Switch main result with alternative
            PlantIdentificationHelper.PlantIdentificationResult selected = alternativeResults.get(position);
            
            // Update UI with new selection
            displayPlantDetails(selected);
            
            // Update image if available
            if (selected.getFirstImageUrl() != null) {
                Glide.with(this)
                        .load(selected.getFirstImageUrl())
                        .centerCrop()
                        .into(imageViewPlant);
            }
            
            // Update main result with selected alternative
            mainResult = selected;
            
            Toast.makeText(getContext(), "Showing details for " + selected.getDisplayName(), Toast.LENGTH_SHORT).show();
        }
    }
}