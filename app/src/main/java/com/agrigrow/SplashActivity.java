package com.agrigrow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Splash screen activity that shows the app logo and name with animations
 * before redirecting to the main activity.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Get references to views
        ImageView logoImageView = findViewById(R.id.imageViewLogo);
        TextView titleTextView = findViewById(R.id.textViewAppTitle);
        TextView subtitleTextView = findViewById(R.id.textViewAppSubtitle);

        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        
        // Apply animations
        logoImageView.startAnimation(fadeIn);
        titleTextView.startAnimation(slideUp);
        subtitleTextView.startAnimation(fadeIn);

        // Navigate to Home activity after delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
