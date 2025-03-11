package com.agrigrow.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.adapter.SoilRecommendationAdapter;
import com.agrigrow.adapter.SoilTestAdapter;
import com.agrigrow.model.SoilRecommendation;
import com.agrigrow.model.SoilTest;
import com.agrigrow.viewmodel.SoilTestViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment for soil testing functionality
 */
public class SoilTestFragment extends Fragment implements
        SoilTestAdapter.OnSoilTestClickListener,
        SoilRecommendationAdapter.RecommendationActionListener {

    private SoilTestViewModel viewModel;
    private RecyclerView recyclerView;
    private SoilTestAdapter adapter;
    private EditText searchEditText;
    private Chip chipAll, chipRecent, chipAcidic, chipAlkaline;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private CardView analysisButtonCard;
    private FloatingActionButton addTestFab;
    
    // Dialog views
    private AlertDialog addTestDialog;
    private AlertDialog soilTestDetailDialog;
    private AlertDialog soilAnalysisDialog;
    
    // Date formatter for displaying
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

    public SoilTestFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soil_test, container, false);
        
        initializeViews(view);
        setupRecyclerView();
        setupListeners();
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(SoilTestViewModel.class);
        observeViewModel();
    }
    
    private void initializeViews(View view) {
        // Initialize main views
        recyclerView = view.findViewById(R.id.recycler_soil_tests);
        searchEditText = view.findViewById(R.id.et_search);
        chipAll = view.findViewById(R.id.chip_all);
        chipRecent = view.findViewById(R.id.chip_recent);
        chipAcidic = view.findViewById(R.id.chip_acidic);
        chipAlkaline = view.findViewById(R.id.chip_alkaline);
        progressBar = view.findViewById(R.id.progress_loading);
        emptyStateLayout = view.findViewById(R.id.layout_empty_state);
        analysisButtonCard = view.findViewById(R.id.card_analysis_button);
        addTestFab = view.findViewById(R.id.fab_add_test);
        
        // Set the empty state button's action
        Button emptyAddButton = view.findViewById(R.id.btn_empty_add_test);
        emptyAddButton.setOnClickListener(v -> showAddTestDialog());
        
        // Set the analyze button's action
        Button analyzeButton = view.findViewById(R.id.btn_analyze);
        analyzeButton.setOnClickListener(v -> showSoilAnalysisDialog());
    }
    
    private void setupRecyclerView() {
        adapter = new SoilTestAdapter(requireContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupListeners() {
        // Search box listener
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.searchSoilTests(s.toString());
            }
        });
        
        // Filter chip listeners
        chipAll.setOnClickListener(v -> {
            clearChipSelections();
            chipAll.setChecked(true);
            viewModel.getAllSoilTests().observe(getViewLifecycleOwner(), soilTests -> {
                updateUI(soilTests);
            });
        });
        
        chipRecent.setOnClickListener(v -> {
            clearChipSelections();
            chipRecent.setChecked(true);
            viewModel.getRecentSoilTests(5).observe(getViewLifecycleOwner(), soilTests -> {
                updateUI(soilTests);
            });
        });
        
        chipAcidic.setOnClickListener(v -> {
            clearChipSelections();
            chipAcidic.setChecked(true);
            viewModel.getSoilTestsByPhRange(0, 6.0).observe(getViewLifecycleOwner(), soilTests -> {
                updateUI(soilTests);
            });
        });
        
        chipAlkaline.setOnClickListener(v -> {
            clearChipSelections();
            chipAlkaline.setChecked(true);
            viewModel.getSoilTestsByPhRange(7.5, 14.0).observe(getViewLifecycleOwner(), soilTests -> {
                updateUI(soilTests);
            });
        });
        
        // FAB listener
        addTestFab.setOnClickListener(v -> showAddTestDialog());
    }
    
    private void clearChipSelections() {
        chipAll.setChecked(false);
        chipRecent.setChecked(false);
        chipAcidic.setChecked(false);
        chipAlkaline.setChecked(false);
    }
    
    private void observeViewModel() {
        // Observe soil tests
        viewModel.getAllSoilTests().observe(getViewLifecycleOwner(), soilTests -> {
            updateUI(soilTests);
        });
        
        // Observe search results if search is active
        viewModel.getSearchQuery().observe(getViewLifecycleOwner(), query -> {
            if (query != null && !query.isEmpty()) {
                viewModel.getFilteredSoilTests().observe(getViewLifecycleOwner(), searchResults -> {
                    updateUI(searchResults);
                });
            }
        });
        
        // Observe location for the add test dialog
        viewModel.getLocation().observe(getViewLifecycleOwner(), location -> {
            if (addTestDialog != null && addTestDialog.isShowing()) {
                EditText locationEditText = addTestDialog.findViewById(R.id.et_location);
                if (location != null && locationEditText != null) {
                    viewModel.getAddress().observe(getViewLifecycleOwner(), address -> {
                        if (address != null) {
                            locationEditText.setText(address);
                        } else {
                            locationEditText.setText(String.format(Locale.US, "%.4f, %.4f", 
                                    location.getLatitude(), location.getLongitude()));
                        }
                    });
                }
            }
        });
    }
    
    private void updateUI(List<SoilTest> soilTests) {
        progressBar.setVisibility(View.GONE);
        
        if (soilTests == null || soilTests.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
            analysisButtonCard.setVisibility(View.GONE);
        } else {
            adapter.submitList(soilTests);
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
            analysisButtonCard.setVisibility(View.VISIBLE);
        }
    }
    
    // SoilTestAdapter.OnSoilTestClickListener implementation
    @Override
    public void onSoilTestClick(SoilTest soilTest) {
        showSoilTestDetailDialog(soilTest);
    }
    
    // SoilRecommendationAdapter.RecommendationActionListener implementation
    @Override
    public void onRecommendationClick(SoilRecommendation recommendation) {
        // Show more details about recommendation if needed
        Toast.makeText(requireContext(), recommendation.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkComplete(SoilRecommendation recommendation) {
        viewModel.markRecommendationAsComplete(recommendation.getId());
        Toast.makeText(requireContext(), "Recommendation marked as complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(SoilRecommendation recommendation) {
        viewModel.dismissRecommendation(recommendation.getId());
        Toast.makeText(requireContext(), "Recommendation dismissed", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Show dialog to add a new soil test
     */
    private void showAddTestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_soil_test, null);
        builder.setView(dialogView);
        
        // Initialize dialog views
        EditText nameEditText = dialogView.findViewById(R.id.et_test_name);
        EditText dateEditText = dialogView.findViewById(R.id.et_test_date);
        EditText locationEditText = dialogView.findViewById(R.id.et_location);
        Button getLocationButton = dialogView.findViewById(R.id.btn_get_location);
        Slider phSlider = dialogView.findViewById(R.id.slider_ph);
        EditText nitrogenEditText = dialogView.findViewById(R.id.et_nitrogen);
        EditText phosphorusEditText = dialogView.findViewById(R.id.et_phosphorus);
        EditText potassiumEditText = dialogView.findViewById(R.id.et_potassium);
        EditText organicMatterEditText = dialogView.findViewById(R.id.et_organic_matter);
        AutoCompleteTextView soilTypeDropdown = dialogView.findViewById(R.id.dropdown_soil_type);
        EditText notesEditText = dialogView.findViewById(R.id.et_notes);
        LinearLayout addPhotoLayout = dialogView.findViewById(R.id.layout_add_photo);
        ImageView soilPhotoImageView = dialogView.findViewById(R.id.iv_soil_photo);
        Button saveButton = dialogView.findViewById(R.id.btn_save);
        Button cancelButton = dialogView.findViewById(R.id.btn_cancel);
        
        // Set up soil type dropdown
        String[] soilTypes = {"Clay", "Sandy", "Loamy", "Silty", "Peaty", "Chalky", "Saline"};
        ArrayAdapter<String> soilTypeAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, soilTypes);
        soilTypeDropdown.setAdapter(soilTypeAdapter);
        
        // Set up date picker
        final Calendar calendar = Calendar.getInstance();
        dateEditText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dateEditText.setText(dateFormatter.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
        
        // Set today's date by default
        dateEditText.setText(dateFormatter.format(calendar.getTime()));
        
        // Set up location button
        getLocationButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            viewModel.getCurrentLocation();
        });
        
        // Set up photo selection
        addPhotoLayout.setOnClickListener(v -> {
            // For this implementation we're not handling actual photo capture
            // In a real app, you would launch camera or gallery intent
            Toast.makeText(requireContext(), "Photo capture functionality would be implemented here", Toast.LENGTH_SHORT).show();
        });
        
        // Set up save button
        saveButton.setOnClickListener(v -> {
            if (validateInputs(nameEditText, dateEditText, locationEditText, nitrogenEditText,
                    phosphorusEditText, potassiumEditText, organicMatterEditText)) {
                
                // Create new soil test
                SoilTest soilTest = new SoilTest();
                soilTest.setName(nameEditText.getText().toString());
                soilTest.setTestDate(calendar.getTime());
                soilTest.setLocation(locationEditText.getText().toString());
                
                // Get location if available
                Location location = viewModel.getLocation().getValue();
                if (location != null) {
                    soilTest.setLatitude(location.getLatitude());
                    soilTest.setLongitude(location.getLongitude());
                }
                
                // Set soil composition values
                soilTest.setPhLevel(phSlider.getValue());
                soilTest.setNitrogenLevel(Double.parseDouble(nitrogenEditText.getText().toString()));
                soilTest.setPhosphorusLevel(Double.parseDouble(phosphorusEditText.getText().toString()));
                soilTest.setPotassiumLevel(Double.parseDouble(potassiumEditText.getText().toString()));
                soilTest.setOrganicMatterPercentage(Double.parseDouble(organicMatterEditText.getText().toString()));
                
                // Set additional info
                if (soilTypeDropdown.getText() != null && !soilTypeDropdown.getText().toString().isEmpty()) {
                    soilTest.setSoilType(soilTypeDropdown.getText().toString());
                }
                if (notesEditText.getText() != null && !notesEditText.getText().toString().isEmpty()) {
                    soilTest.setNotes(notesEditText.getText().toString());
                }
                
                // Save to database
                viewModel.insert(soilTest);
                addTestDialog.dismiss();
                Toast.makeText(requireContext(), "Soil test added", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Set up cancel button
        cancelButton.setOnClickListener(v -> addTestDialog.dismiss());
        
        // Create and show dialog
        addTestDialog = builder.create();
        addTestDialog.show();
    }
    
    /**
     * Show dialog with soil test details
     */
    private void showSoilTestDetailDialog(SoilTest soilTest) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_soil_test_detail, null);
        builder.setView(dialogView);
        
        // Basic information
        TextView testNameTextView = dialogView.findViewById(R.id.tv_test_name);
        TextView testDateTextView = dialogView.findViewById(R.id.tv_test_date);
        TextView healthScoreTextView = dialogView.findViewById(R.id.tv_health_score);
        TextView healthStatusTextView = dialogView.findViewById(R.id.tv_health_status);
        CardView healthScoreCardView = dialogView.findViewById(R.id.card_health_score);
        
        // Soil composition
        TextView phValueTextView = dialogView.findViewById(R.id.tv_ph_value);
        ProgressBar phProgressBar = dialogView.findViewById(R.id.progress_ph);
        TextView nitrogenValueTextView = dialogView.findViewById(R.id.tv_nitrogen_value);
        TextView phosphorusValueTextView = dialogView.findViewById(R.id.tv_phosphorus_value);
        TextView potassiumValueTextView = dialogView.findViewById(R.id.tv_potassium_value);
        TextView organicMatterValueTextView = dialogView.findViewById(R.id.tv_organic_matter_value);
        ProgressBar organicMatterProgressBar = dialogView.findViewById(R.id.progress_organic_matter);
        TextView soilTypeTextView = dialogView.findViewById(R.id.tv_soil_type);
        
        // Location
        TextView locationValueTextView = dialogView.findViewById(R.id.tv_location_value);
        
        // Notes
        TextView notesTextView = dialogView.findViewById(R.id.tv_notes);
        TextView noNotesTextView = dialogView.findViewById(R.id.tv_no_notes);
        
        // Recommendations
        RecyclerView recommendationsRecyclerView = dialogView.findViewById(R.id.recycler_recommendations);
        TextView noRecommendationsTextView = dialogView.findViewById(R.id.tv_no_recommendations);
        
        // Action buttons
        Button editButton = dialogView.findViewById(R.id.btn_edit);
        Button closeButton = dialogView.findViewById(R.id.btn_close);
        
        // Populate basic information
        testNameTextView.setText(soilTest.getName());
        testDateTextView.setText(dateFormatter.format(soilTest.getTestDate()));
        healthScoreTextView.setText(String.valueOf(soilTest.getSoilHealthScore()));
        healthStatusTextView.setText(soilTest.getHealthStatus());
        
        // Set health score color
        int healthScoreColorResId;
        switch (soilTest.getHealthStatus()) {
            case "Excellent":
                healthScoreColorResId = R.color.colorHealthy;
                break;
            case "Good":
                healthScoreColorResId = R.color.colorModerate;
                break;
            case "Fair":
                healthScoreColorResId = R.color.colorWarning;
                break;
            case "Poor":
                healthScoreColorResId = R.color.colorCritical;
                break;
            default:
                healthScoreColorResId = R.color.colorAccent;
                break;
        }
        healthScoreCardView.setCardBackgroundColor(requireContext().getResources()
                .getColor(healthScoreColorResId));
        
        // Populate soil composition
        phValueTextView.setText(String.format(Locale.US, "%.1f", soilTest.getPhLevel()));
        phProgressBar.setProgress((int) (soilTest.getPhLevel() * 10));
        nitrogenValueTextView.setText(String.format(Locale.US, "%.2f%%", soilTest.getNitrogenLevel()));
        phosphorusValueTextView.setText(String.format(Locale.US, "%.1f ppm", soilTest.getPhosphorusLevel()));
        potassiumValueTextView.setText(String.format(Locale.US, "%.1f ppm", soilTest.getPotassiumLevel()));
        organicMatterValueTextView.setText(String.format(Locale.US, "%.1f%%", soilTest.getOrganicMatterPercentage()));
        organicMatterProgressBar.setProgress((int) (soilTest.getOrganicMatterPercentage() * 10));
        
        // Set soil type if available
        if (soilTest.getSoilType() != null && !soilTest.getSoilType().isEmpty()) {
            soilTypeTextView.setText(soilTest.getSoilType());
        } else {
            soilTypeTextView.setText("Not specified");
        }
        
        // Populate location
        locationValueTextView.setText(soilTest.getLocation());
        
        // Populate notes
        if (soilTest.getNotes() != null && !soilTest.getNotes().isEmpty()) {
            notesTextView.setText(soilTest.getNotes());
            notesTextView.setVisibility(View.VISIBLE);
            noNotesTextView.setVisibility(View.GONE);
        } else {
            notesTextView.setVisibility(View.GONE);
            noNotesTextView.setVisibility(View.VISIBLE);
        }
        
        // Set up recommendations
        recommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SoilRecommendationAdapter recommendationAdapter = new SoilRecommendationAdapter(requireContext(), this);
        recommendationsRecyclerView.setAdapter(recommendationAdapter);
        
        viewModel.getRecommendationsForSoilTest(soilTest.getId()).observe(getViewLifecycleOwner(), recommendations -> {
            if (recommendations != null && !recommendations.isEmpty()) {
                recommendationAdapter.submitList(recommendations);
                recommendationsRecyclerView.setVisibility(View.VISIBLE);
                noRecommendationsTextView.setVisibility(View.GONE);
            } else {
                recommendationsRecyclerView.setVisibility(View.GONE);
                noRecommendationsTextView.setVisibility(View.VISIBLE);
            }
        });
        
        // Set up edit button
        editButton.setOnClickListener(v -> {
            soilTestDetailDialog.dismiss();
            // In a real implementation, show edit dialog with prefilled data
            Toast.makeText(requireContext(), "Edit functionality would be implemented here", Toast.LENGTH_SHORT).show();
        });
        
        // Set up close button
        closeButton.setOnClickListener(v -> soilTestDetailDialog.dismiss());
        
        // Create and show dialog
        soilTestDetailDialog = builder.create();
        soilTestDetailDialog.show();
    }
    
    /**
     * Show soil analysis dialog with insights from all tests
     */
    private void showSoilAnalysisDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_soil_analysis, null);
        builder.setView(dialogView);
        
        // Initialize analytics views
        TextView testsAnalyzedTextView = dialogView.findViewById(R.id.tv_tests_analyzed);
        ProgressBar overallHealthProgress = dialogView.findViewById(R.id.progress_overall_health);
        TextView phRecommendationTextView = dialogView.findViewById(R.id.tv_ph_recommendation);
        TextView npkRecommendationTextView = dialogView.findViewById(R.id.tv_npk_recommendation);
        TextView healthSummaryTextView = dialogView.findViewById(R.id.tv_health_summary);
        Button exportButton = dialogView.findViewById(R.id.btn_export_data);
        Button closeButton = dialogView.findViewById(R.id.btn_close);
        
        // Get test count
        viewModel.getSoilTestCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                testsAnalyzedTextView.setText(String.valueOf(count));
            }
        });
        
        // Get average health score
        viewModel.getAverageHealthScore().observe(getViewLifecycleOwner(), avgScore -> {
            if (avgScore != null) {
                overallHealthProgress.setProgress(avgScore);
            }
        });
        
        // Get average pH
        viewModel.getAveragePhLevel().observe(getViewLifecycleOwner(), avgPh -> {
            if (avgPh != null) {
                if (avgPh < 5.5) {
                    phRecommendationTextView.setText("Your soil is generally acidic. Consider adding lime to raise pH for most plants.");
                } else if (avgPh > 7.5) {
                    phRecommendationTextView.setText("Your soil is generally alkaline. Consider adding sulfur or organic matter to lower pH for acid-loving plants.");
                } else {
                    phRecommendationTextView.setText("Your soil pH is in the optimal range for most plants (6.0-7.0). Continue monitoring for any changes.");
                }
            }
        });
        
        // Get average NPK
        viewModel.getAverageNitrogenLevel().observe(getViewLifecycleOwner(), avgN -> {
            viewModel.getAveragePhosphorusLevel().observe(getViewLifecycleOwner(), avgP -> {
                viewModel.getAveragePotassiumLevel().observe(getViewLifecycleOwner(), avgK -> {
                    if (avgN != null && avgP != null && avgK != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("NPK Analysis: ");
                        
                        if (avgN < 0.1) {
                            sb.append("Nitrogen is low. ");
                        } else {
                            sb.append("Nitrogen is adequate. ");
                        }
                        
                        if (avgP < 20) {
                            sb.append("Phosphorus is low. ");
                        } else {
                            sb.append("Phosphorus is adequate. ");
                        }
                        
                        if (avgK < 150) {
                            sb.append("Potassium is low.");
                        } else {
                            sb.append("Potassium is adequate.");
                        }
                        
                        npkRecommendationTextView.setText(sb.toString());
                    }
                });
            });
        });
        
        // Get average organic matter
        viewModel.getAverageOrganicMatter().observe(getViewLifecycleOwner(), avgOm -> {
            if (avgOm != null) {
                StringBuilder summary = new StringBuilder();
                summary.append("Based on your soil tests, your garden soil is ");
                
                viewModel.getAverageHealthScore().observe(getViewLifecycleOwner(), avgScore -> {
                    if (avgScore != null) {
                        if (avgScore >= 80) {
                            summary.append("in excellent condition. ");
                        } else if (avgScore >= 60) {
                            summary.append("in good condition, with room for improvement. ");
                        } else if (avgScore >= 40) {
                            summary.append("in fair condition, needing attention in several areas. ");
                        } else {
                            summary.append("in poor condition, requiring significant improvements. ");
                        }
                    }
                    
                    summary.append("Your organic matter content is ");
                    if (avgOm < 2) {
                        summary.append("low. Adding compost is recommended to improve soil structure and fertility.");
                    } else if (avgOm < 4) {
                        summary.append("moderate. Continue adding organic matter to reach the ideal range of 4-6%.");
                    } else {
                        summary.append("good. Maintain current practices to preserve soil health.");
                    }
                    
                    healthSummaryTextView.setText(summary.toString());
                });
            }
        });
        
        // Set up action buttons
        exportButton.setOnClickListener(v -> {
            // In a real implementation, would export data to CSV or PDF
            Toast.makeText(requireContext(), "Export functionality would be implemented here", Toast.LENGTH_SHORT).show();
        });
        
        closeButton.setOnClickListener(v -> soilAnalysisDialog.dismiss());
        
        // Create and show dialog
        soilAnalysisDialog = builder.create();
        soilAnalysisDialog.show();
    }
    
    /**
     * Validate inputs from add test dialog
     */
    private boolean validateInputs(EditText nameEditText, EditText dateEditText, EditText locationEditText,
                                  EditText nitrogenEditText, EditText phosphorusEditText,
                                  EditText potassiumEditText, EditText organicMatterEditText) {
        
        // Check required fields
        if (nameEditText.getText().toString().isEmpty()) {
            nameEditText.setError("Name is required");
            return false;
        }
        
        if (dateEditText.getText().toString().isEmpty()) {
            dateEditText.setError("Date is required");
            return false;
        }
        
        if (locationEditText.getText().toString().isEmpty()) {
            locationEditText.setError("Location is required");
            return false;
        }
        
        // Check nutrient values
        if (nitrogenEditText.getText().toString().isEmpty()) {
            nitrogenEditText.setError("Nitrogen value is required");
            return false;
        }
        
        if (phosphorusEditText.getText().toString().isEmpty()) {
            phosphorusEditText.setError("Phosphorus value is required");
            return false;
        }
        
        if (potassiumEditText.getText().toString().isEmpty()) {
            potassiumEditText.setError("Potassium value is required");
            return false;
        }
        
        if (organicMatterEditText.getText().toString().isEmpty()) {
            organicMatterEditText.setError("Organic matter value is required");
            return false;
        }
        
        // All validations passed
        return true;
    }
}