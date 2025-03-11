package com.agrigrow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main entry point for the AgriGrow application.
 * This activity serves as a splash screen and redirects to the home activity.
 */
public class MainActivity extends AppCompatActivity {

    // Splash screen duration in milliseconds
    private static final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use a handler to delay loading the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the home activity after the splash screen
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Close this activity
            }
        }, SPLASH_DURATION);
    }
}
