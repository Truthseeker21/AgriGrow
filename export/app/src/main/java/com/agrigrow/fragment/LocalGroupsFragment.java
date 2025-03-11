package com.agrigrow.fragment;

import android.app.AlertDialog;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.agrigrow.R;
import com.agrigrow.adapter.GardeningGroupAdapter;
import com.agrigrow.database.AppDatabase;
import com.agrigrow.database.GardeningGroupDao;
import com.agrigrow.model.GardeningGroup;
import com.agrigrow.util.LocationHelper;
import com.agrigrow.util.SocialSharingHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Fragment for local gardening groups functionality
 */
public class LocalGroupsFragment extends Fragment implements GardeningGroupAdapter.GroupActionListener {

    private RecyclerView recyclerView;
    private GardeningGroupAdapter adapter;
    private GardeningGroupDao gardeningGroupDao;
    private EditText searchEditText;
    private Chip chipAllGroups, chipNearby, chipJoined, chipPopular, chipCommunityGardens, chipSeedExchange;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private CardView mapPreviewCard;
    private FloatingActionButton createGroupFab;
    private LocationHelper locationHelper;
    private SocialSharingHelper socialSharingHelper;
    private final Executor executor = Executors.newSingleThreadExecutor();
    
    // Current location data
    private Location currentLocation;
    private static final double NEARBY_RADIUS_KM = 15.0; // search radius in km
    
    public LocalGroupsFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_groups, container, false);
        
        initializeViews(view);
        setupListeners();
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        gardeningGroupDao = AppDatabase.getInstance(requireContext()).gardeningGroupDao();
        locationHelper = new LocationHelper(requireContext());
        socialSharingHelper = new SocialSharingHelper(requireContext());
        
        setupAdapter();
        loadGroups();
        getCurrentLocation();
    }
    
    private void initializeViews(View view) {
        // Main views
        recyclerView = view.findViewById(R.id.recycler_groups);
        searchEditText = view.findViewById(R.id.et_search_groups);
        progressBar = view.findViewById(R.id.progress_loading);
        emptyStateLayout = view.findViewById(R.id.layout_empty_state);
        mapPreviewCard = view.findViewById(R.id.card_map_preview);
        createGroupFab = view.findViewById(R.id.fab_create_group);
        
        // Filter chips
        chipAllGroups = view.findViewById(R.id.chip_all_groups);
        chipNearby = view.findViewById(R.id.chip_nearby);
        chipJoined = view.findViewById(R.id.chip_joined);
        chipPopular = view.findViewById(R.id.chip_popular);
        chipCommunityGardens = view.findViewById(R.id.chip_community_gardens);
        chipSeedExchange = view.findViewById(R.id.chip_seed_exchange);
        
        // Empty state button
        Button emptyCreateButton = view.findViewById(R.id.btn_create_group);
        emptyCreateButton.setOnClickListener(v -> showCreateGroupDialog());
        
        // Map preview button
        Button viewMapButton = view.findViewById(R.id.btn_view_map);
        viewMapButton.setOnClickListener(v -> {
            // In a real app, open full map view
            Toast.makeText(requireContext(), "Full map view would open here", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void setupAdapter() {
        adapter = new GardeningGroupAdapter(requireContext(), this);
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
                searchGroups(s.toString());
            }
        });
        
        // Filter chip listeners
        chipAllGroups.setOnClickListener(v -> {
            clearChipSelections();
            chipAllGroups.setChecked(true);
            loadGroups();
            mapPreviewCard.setVisibility(View.GONE);
        });
        
        chipNearby.setOnClickListener(v -> {
            clearChipSelections();
            chipNearby.setChecked(true);
            loadNearbyGroups();
            mapPreviewCard.setVisibility(View.VISIBLE);
        });
        
        chipJoined.setOnClickListener(v -> {
            clearChipSelections();
            chipJoined.setChecked(true);
            loadJoinedGroups();
            mapPreviewCard.setVisibility(View.GONE);
        });
        
        chipPopular.setOnClickListener(v -> {
            clearChipSelections();
            chipPopular.setChecked(true);
            loadPopularGroups();
            mapPreviewCard.setVisibility(View.GONE);
        });
        
        chipCommunityGardens.setOnClickListener(v -> {
            clearChipSelections();
            chipCommunityGardens.setChecked(true);
            loadGroupsByCategory("Community Garden");
            mapPreviewCard.setVisibility(View.GONE);
        });
        
        chipSeedExchange.setOnClickListener(v -> {
            clearChipSelections();
            chipSeedExchange.setChecked(true);
            loadGroupsByCategory("Seed Exchange");
            mapPreviewCard.setVisibility(View.GONE);
        });
        
        // FAB listener
        createGroupFab.setOnClickListener(v -> showCreateGroupDialog());
    }
    
    private void clearChipSelections() {
        chipAllGroups.setChecked(false);
        chipNearby.setChecked(false);
        chipJoined.setChecked(false);
        chipPopular.setChecked(false);
        chipCommunityGardens.setChecked(false);
        chipSeedExchange.setChecked(false);
    }
    
    private void loadGroups() {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        
        gardeningGroupDao.getAllGroups().observe(getViewLifecycleOwner(), groups -> {
            updateUI(groups);
        });
    }
    
    private void loadNearbyGroups() {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        
        if (currentLocation != null) {
            // Calculate coordinates for bounding box (approximate)
            double lat = currentLocation.getLatitude();
            double lon = currentLocation.getLongitude();
            
            // Roughly 1 degree latitude = 111 km, longitude varies by latitude
            double latDiff = NEARBY_RADIUS_KM / 111.0;
            double lonDiff = NEARBY_RADIUS_KM / (111.0 * Math.cos(Math.toRadians(lat)));
            
            double minLat = lat - latDiff;
            double maxLat = lat + latDiff;
            double minLon = lon - lonDiff;
            double maxLon = lon + lonDiff;
            
            gardeningGroupDao.getNearbyGroups(minLat, maxLat, minLon, maxLon).observe(getViewLifecycleOwner(), groups -> {
                updateUI(groups);
            });
        } else {
            // If location is not available, fallback to all groups
            Toast.makeText(requireContext(), "Location not available, showing all groups", Toast.LENGTH_SHORT).show();
            gardeningGroupDao.getAllGroups().observe(getViewLifecycleOwner(), groups -> {
                updateUI(groups);
            });
        }
    }
    
    private void loadJoinedGroups() {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        
        gardeningGroupDao.getJoinedGroups().observe(getViewLifecycleOwner(), groups -> {
            updateUI(groups);
        });
    }
    
    private void loadPopularGroups() {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        
        gardeningGroupDao.getPopularGroups().observe(getViewLifecycleOwner(), groups -> {
            updateUI(groups);
        });
    }
    
    private void loadGroupsByCategory(String category) {
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        
        gardeningGroupDao.getGroupsByCategory(category).observe(getViewLifecycleOwner(), groups -> {
            updateUI(groups);
        });
    }
    
    private void searchGroups(String query) {
        if (query.isEmpty()) {
            // If search query is empty, load based on current filter
            if (chipNearby.isChecked()) {
                loadNearbyGroups();
            } else if (chipJoined.isChecked()) {
                loadJoinedGroups();
            } else if (chipPopular.isChecked()) {
                loadPopularGroups();
            } else if (chipCommunityGardens.isChecked()) {
                loadGroupsByCategory("Community Garden");
            } else if (chipSeedExchange.isChecked()) {
                loadGroupsByCategory("Seed Exchange");
            } else {
                loadGroups();
            }
        } else {
            progressBar.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
            
            gardeningGroupDao.searchGroups(query).observe(getViewLifecycleOwner(), groups -> {
                updateUI(groups);
            });
        }
    }
    
    private void updateUI(List<GardeningGroup> groups) {
        progressBar.setVisibility(View.GONE);
        
        if (groups == null || groups.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            adapter.submitList(groups);
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
    
    private void getCurrentLocation() {
        locationHelper.requestSingleLocationUpdate(location -> {
            currentLocation = location;
            
            // If nearby filter is selected, refresh the data
            if (chipNearby.isChecked()) {
                loadNearbyGroups();
            }
        });
    }
    
    private void showCreateGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_group, null);
        builder.setView(dialogView);
        builder.setTitle("Create Gardening Group");
        
        // In a full implementation, we would populate and handle the dialog form here
        
        builder.setPositiveButton("Create", (dialog, which) -> {
            // Handle create group logic in a full implementation
            Toast.makeText(requireContext(), "Group creation would be implemented here", Toast.LENGTH_SHORT).show();
        });
        
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    private void showGroupDetailsDialog(GardeningGroup group) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_group_details, null);
        builder.setView(dialogView);
        
        // In a full implementation, we would populate the dialog with group details here
        
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        
        if (group.isJoined()) {
            builder.setNeutralButton("Leave Group", (dialog, which) -> {
                toggleJoinStatus(group, false);
            });
            
            builder.setNegativeButton("Share", (dialog, which) -> {
                shareGroup(group);
            });
        } else {
            builder.setNeutralButton("Join Group", (dialog, which) -> {
                toggleJoinStatus(group, true);
            });
        }
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    private void toggleJoinStatus(GardeningGroup group, boolean join) {
        executor.execute(() -> {
            gardeningGroupDao.updateJoinStatus(group.getId(), join);
            requireActivity().runOnUiThread(() -> {
                String message = join ? "Joined " + group.getName() : "Left " + group.getName();
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            });
        });
    }
    
    private void shareGroup(GardeningGroup group) {
        // In a real implementation, we would create a formatted sharing message
        String shareText = "Check out this gardening group: " + group.getName() +
                "\nLocation: " + group.getLocation() +
                "\n\nShared from AgriGrow - Urban Gardening App";
        
        // Launch share dialog using Android's share intent
        Toast.makeText(requireContext(), "Sharing functionality would be implemented here", Toast.LENGTH_SHORT).show();
    }
    
    // GardeningGroupAdapter.GroupActionListener implementation
    @Override
    public void onGroupClick(GardeningGroup group) {
        showGroupDetailsDialog(group);
    }

    @Override
    public void onJoinGroup(GardeningGroup group) {
        toggleJoinStatus(group, true);
    }

    @Override
    public void onLeaveGroup(GardeningGroup group) {
        toggleJoinStatus(group, false);
    }
}