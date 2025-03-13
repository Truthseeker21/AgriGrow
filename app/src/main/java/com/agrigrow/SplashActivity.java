package com.agrigrow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.agrigrow.util.PlantIdentificationHelper;
import com.agrigrow.util.SecretKeys;
import com.agrigrow.util.WeatherHelper;

/**
 * Splash screen activity that appears at app startup
 * This activity initializes essential components like API keys
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final long SPLASH_DURATION = 2000; // 2 seconds

    private ImageView imageViewLogo;
    private TextView textViewTitle;
    private TextView textViewSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Make the activity fullscreen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        setContentView(R.layout.activity_splash);

        // Initialize views
        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewSlogan = findViewById(R.id.textViewSlogan);

        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        
        // Apply animations
        imageViewLogo.startAnimation(fadeIn);
        textViewTitle.startAnimation(slideUp);
        textViewSlogan.startAnimation(fadeIn);

        // Initialize API Keys and Services
        initializeApiKeys();
        
        // Navigate to the main activity after a delay
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToMain();
            }
        }, SPLASH_DURATION);
    }
    
    /**
     * Initialize API keys for weather and plant identification services
     */
    private void initializeApiKeys() {
        try {
            // Get API keys from SecretKeys class which loads them from BuildConfig
            String weatherApiKey = SecretKeys.getOpenWeatherMapApiKey(this);
            String plantIdApiKey = SecretKeys.getPlantIdApiKey(this);
            
            // Validate API keys
            validateApiKeys(weatherApiKey, plantIdApiKey);
            
            // Set API keys in helper classes
            WeatherHelper.setApiKey(weatherApiKey);
            PlantIdentificationHelper.setApiKey(plantIdApiKey);
            
            Log.d(TAG, "API keys initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing API keys", e);
        }
    }
    
    /**
     * Validate that the API keys are properly set
     * @param weatherApiKey The OpenWeatherMap API key
     * @param plantIdApiKey The Plant.id API key
     */
    private void validateApiKeys(String weatherApiKey, String plantIdApiKey) {
        boolean hasValidKeys = true;
        StringBuilder errorMessage = new StringBuilder("Missing API keys: ");
        
        // Check if the OpenWeatherMap API key is valid
        if (weatherApiKey == null || weatherApiKey.isEmpty() || weatherApiKey.equals("${OPENWEATHERMAP_API_KEY}")) {
            hasValidKeys = false;
            errorMessage.append("OpenWeatherMap");
            Log.e(TAG, "OpenWeatherMap API key is missing or invalid");
        }
        
        // Check if the Plant.id API key is valid
        if (plantIdApiKey == null || plantIdApiKey.isEmpty() || plantIdApiKey.equals("${PLANT_ID_API_KEY}")) {
            if (!hasValidKeys) {
                errorMessage.append(", ");
            }
            hasValidKeys = false;
            errorMessage.append("Plant.id");
            Log.e(TAG, "Plant.id API key is missing or invalid");
        }
        
        // If any API key is invalid, log an error and show a dialog
        if (!hasValidKeys) {
            Log.e(TAG, errorMessage.toString());
            Log.i(TAG, "To set API keys, add them to your local.properties file or as environment variables");
            
            // Show a dialog notifying the user about missing API keys
            showApiKeyErrorDialog(errorMessage.toString());
        }
    }

    /**
     * Show a dialog informing the user about missing API keys
     * @param errorMessage The error message to display
     */
    private void showApiKeyErrorDialog(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle("API Keys Required")
                .setMessage(errorMessage + "\n\nSome features of the app may not work properly without valid API keys. " +
                        "Please add the required API keys to your local.properties file.")
                .setPositiveButton("Continue Anyway", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    /**
     * Navigate to the MainActivity
     */
    private void navigateToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close the splash activity
    }
}