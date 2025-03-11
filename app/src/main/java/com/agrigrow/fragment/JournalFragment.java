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
import com.agrigrow.adapter.JournalEntryAdapter;
import com.agrigrow.database.AppDatabase;
import com.agrigrow.model.JournalEntry;
import com.agrigrow.util.WeatherHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

/**
 * Fragment for the gardening journal feature
 */
public class JournalFragment extends Fragment {

    private RecyclerView recyclerViewJournal;
    private TextView textViewJournalTitle;
    private TextView textViewJournalSubtitle;
    private TextView textViewNoEntries;
    private FloatingActionButton fabAddEntry;
    private ChipGroup chipGroupEntryTypes;
    
    private AppDatabase database;
    private WeatherHelper weatherHelper;
    private JournalEntryAdapter journalAdapter;
    
    private List<JournalEntry> allEntries = new ArrayList<>();
    private List<JournalEntry> filteredEntries = new ArrayList<>();
    private String currentFilter = "all";

    public JournalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize database and helper
        database = AppDatabase.getInstance(requireContext());
        weatherHelper = WeatherHelper.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        recyclerViewJournal = view.findViewById(R.id.recyclerViewJournal);
        textViewJournalTitle = view.findViewById(R.id.textViewJournalTitle);
        textViewJournalSubtitle = view.findViewById(R.id.textViewJournalSubtitle);
        textViewNoEntries = view.findViewById(R.id.textViewNoEntries);
        fabAddEntry = view.findViewById(R.id.fabAddEntry);
        chipGroupEntryTypes = view.findViewById(R.id.chipGroupEntryTypes);
        
        // Setup RecyclerView
        recyclerViewJournal.setLayoutManager(new LinearLayoutManager(getContext()));
        journalAdapter = new JournalEntryAdapter(filteredEntries, entry -> {
            // Handle entry click
            // In a real app, this would open a detailed view of the journal entry
            Toast.makeText(getContext(), "Selected: " + entry.getTitle(), Toast.LENGTH_SHORT).show();
        });
        recyclerViewJournal.setAdapter(journalAdapter);
        
        // Set up title with current date
        setupJournalHeader();
        
        // Set up entry type filters
        setupEntryTypeFilters();
        
        // Set up floating action button
        setupFabButton();
        
        // Load journal entries
        loadJournalEntries();
    }
    
    private void setupJournalHeader() {
        // Set title with current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date());
        textViewJournalTitle.setText("Garden Journal");
        textViewJournalSubtitle.setText(formattedDate);
    }
    
    private void setupEntryTypeFilters() {
        // Add entry type filter chips
        String[] entryTypes = {
            "all", "planting", "harvest", "observation", "maintenance", "treatment"
        };
        
        String[] entryTypeLabels = {
            "All", "Planting", "Harvest", "Observation", "Maintenance", "Treatment"
        };
        
        for (int i = 0; i < entryTypes.length; i++) {
            String type = entryTypes[i];
            String label = entryTypeLabels[i];
            
            Chip chip = new Chip(requireContext());
            chip.setText(label);
            chip.setCheckable(true);
            chip.setClickable(true);
            
            // Set "All" as initially checked
            if (type.equals("all")) {
                chip.setChecked(true);
            }
            
            // Add click listener
            chip.setOnClickListener(v -> {
                // Update filter
                currentFilter = type;
                
                // Apply filter
                filterEntries();
            });
            
            // Add to chip group
            chipGroupEntryTypes.addView(chip);
        }
    }
    
    private void setupFabButton() {
        // Set up floating action button to add a new journal entry
        fabAddEntry.setOnClickListener(v -> {
            // In a real app, this would open a dialog or activity to add a new entry
            showAddEntryDialog();
        });
    }
    
    private void showAddEntryDialog() {
        // In a real app, this would show a dialog to add a new entry
        Toast.makeText(getContext(), "Add new journal entry", Toast.LENGTH_SHORT).show();
        
        // For demonstration purposes, we'll add a sample entry
        addSampleEntry();
    }
    
    private void addSampleEntry() {
        // Create a sample entry
        Calendar calendar = Calendar.getInstance();
        JournalEntry newEntry = new JournalEntry("New Garden Observation", calendar.getTime());
        newEntry.setDescription("I noticed the tomato plants are growing well!");
        newEntry.setEntryType("observation");
        newEntry.setWeatherConditions("Sunny");
        newEntry.setTemperature(78.5f);
        newEntry.setMoodRating(5);
        
        // Add to database
        Executors.newSingleThreadExecutor().execute(() -> {
            long id = database.journalEntryDao().insertEntry(newEntry);
            
            // Update UI on main thread
            requireActivity().runOnUiThread(() -> {
                if (id > 0) {
                    // Success - refresh entries
                    Toast.makeText(getContext(), "Entry added successfully!", Toast.LENGTH_SHORT).show();
                    loadJournalEntries();
                } else {
                    // Error
                    Toast.makeText(getContext(), "Error adding entry", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    private void loadJournalEntries() {
        // Show loading state
        textViewNoEntries.setText("Loading entries...");
        textViewNoEntries.setVisibility(View.VISIBLE);
        recyclerViewJournal.setVisibility(View.GONE);
        
        // Load entries from database
        Executors.newSingleThreadExecutor().execute(() -> {
            List<JournalEntry> entries = database.journalEntryDao().getAllEntries();
            
            // Update UI on main thread
            requireActivity().runOnUiThread(() -> {
                allEntries.clear();
                
                if (entries != null && !entries.isEmpty()) {
                    allEntries.addAll(entries);
                    filterEntries();
                } else {
                    // No entries, show empty state
                    textViewNoEntries.setText("No journal entries yet. Click the + button to add your first entry!");
                    textViewNoEntries.setVisibility(View.VISIBLE);
                    recyclerViewJournal.setVisibility(View.GONE);
                }
            });
        });
    }
    
    private void filterEntries() {
        filteredEntries.clear();
        
        if (currentFilter.equals("all")) {
            // Show all entries
            filteredEntries.addAll(allEntries);
        } else {
            // Filter by entry type
            for (JournalEntry entry : allEntries) {
                if (entry.getEntryType().equals(currentFilter)) {
                    filteredEntries.add(entry);
                }
            }
        }
        
        // Update UI
        if (filteredEntries.isEmpty()) {
            if (allEntries.isEmpty()) {
                textViewNoEntries.setText("No journal entries yet. Click the + button to add your first entry!");
            } else {
                textViewNoEntries.setText("No entries found for this filter. Try a different filter.");
            }
            textViewNoEntries.setVisibility(View.VISIBLE);
            recyclerViewJournal.setVisibility(View.GONE);
        } else {
            textViewNoEntries.setVisibility(View.GONE);
            recyclerViewJournal.setVisibility(View.VISIBLE);
        }
        
        // Notify adapter
        journalAdapter.notifyDataSetChanged();
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