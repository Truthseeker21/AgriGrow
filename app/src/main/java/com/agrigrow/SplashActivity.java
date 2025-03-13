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
            // Normally these keys would be fetched from secure storage
            String weatherApiKey = SecretKeys.getWeatherApiKey();
            String plantIdApiKey = SecretKeys.getPlantIdApiKey();
            
            // Set API keys in helper classes
            WeatherHelper.setApiKey(weatherApiKey);
            PlantIdentificationHelper.setApiKey(plantIdApiKey);
            
            Log.d(TAG, "API keys initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing API keys", e);
        }
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