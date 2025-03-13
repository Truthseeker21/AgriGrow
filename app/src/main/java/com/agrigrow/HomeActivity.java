package com.agrigrow;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.agrigrow.fragment.ForumFragment;
import com.agrigrow.fragment.GuidesFragment;
import com.agrigrow.fragment.HomeFragment;
import com.agrigrow.fragment.PlantIdentifyFragment;
import com.agrigrow.fragment.PlantsFragment;
import com.agrigrow.fragment.ProfileFragment;
import com.agrigrow.fragment.WeatherFragment;
import com.agrigrow.util.PlantIdentificationHelper;
import com.agrigrow.util.WeatherHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Main home activity containing the main features of the app via fragments
 */
public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fabIdentify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        bottomNavigation = findViewById(R.id.bottomNavigation);
        fabIdentify = findViewById(R.id.fabIdentify);

        // Set up navigation
        setupBottomNavigation();

        // Set up plant identification FAB
        fabIdentify.setOnClickListener(v -> {
            loadFragment(new PlantIdentifyFragment());
        });

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
        
        // Initialize API keys - in a real app, these would be stored securely
        // and retrieved from a secure storage location
        setupAPIKeys();
    }
    
    private void setupAPIKeys() {
        // In a production app, you would retrieve these from secure storage
        // This is just a placeholder - real API keys would be needed for production use
        // WeatherHelper.setApiKey("YOUR_OPENWEATHER_API_KEY");
        // PlantIdentificationHelper.setApiKey("YOUR_PLANT_ID_API_KEY");
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                
                // Create appropriate fragment based on selected item
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.nav_plants:
                        fragment = new PlantsFragment();
                        break;
                    case R.id.nav_weather:
                        fragment = new WeatherFragment();
                        break;
                    case R.id.nav_forum:
                        fragment = new ForumFragment();
                        break;
                    case R.id.nav_guides:
                        fragment = new GuidesFragment();
                        break;
                }
                
                // Load the selected fragment
                if (fragment != null) {
                    loadFragment(fragment);
                    return true;
                }
                
                return false;
            }
        });
    }

    /**
     * Load a fragment into the container
     * @param fragment Fragment to load
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}