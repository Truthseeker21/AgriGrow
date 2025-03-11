package com.agrigrow.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.agrigrow.R;
import com.agrigrow.model.GardeningTechnique;
import com.agrigrow.model.JournalEntry;
import com.agrigrow.model.Plant;
import com.agrigrow.model.SoilTest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Helper class for sharing garden-related content to social media and messaging platforms
 */
public class SocialSharingHelper {

    private final Context context;
    private final SimpleDateFormat dateFormatter;

    public SocialSharingHelper(Context context) {
        this.context = context;
        this.dateFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    }

    /**
     * Share a plant with image and care instructions
     */
    public void sharePlant(Plant plant) {
        if (plant == null) {
            Toast.makeText(context, "No plant data to share", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create share text
        StringBuilder shareText = new StringBuilder();
        shareText.append("Check out this plant from my garden!\n\n");
        shareText.append("Name: ").append(plant.getName()).append("\n");
        shareText.append("Type: ").append(plant.getType()).append("\n");
        
        if (plant.getCareInstructions() != null && !plant.getCareInstructions().isEmpty()) {
            shareText.append("\nCare Instructions:\n").append(plant.getCareInstructions()).append("\n");
        }
        
        shareText.append("\nShared from AgriGrow - Urban Gardening App");

        // Create and launch share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this plant: " + plant.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());

        // If plant has an image, include it
        if (plant.getImagePath() != null && !plant.getImagePath().isEmpty()) {
            try {
                File imageFile = new File(plant.getImagePath());
                if (imageFile.exists()) {
                    Uri imageUri = FileProvider.getUriForFile(
                            context,
                            context.getPackageName() + ".fileprovider",
                            imageFile);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Continue without image if there's an error
            }
        }

        // Launch share dialog
        context.startActivity(Intent.createChooser(shareIntent, "Share Plant"));
    }

    /**
     * Share a journal entry with optional image
     */
    public void shareJournalEntry(JournalEntry entry) {
        if (entry == null) {
            Toast.makeText(context, "No journal entry to share", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create share text
        StringBuilder shareText = new StringBuilder();
        shareText.append("From my garden journal on ").append(dateFormatter.format(entry.getDate())).append("\n\n");
        shareText.append(entry.getTitle()).append("\n\n");
        shareText.append(entry.getContent()).append("\n\n");
        
        if (entry.getWeather() != null && !entry.getWeather().isEmpty()) {
            shareText.append("Weather: ").append(entry.getWeather()).append("\n");
        }
        
        shareText.append("\nShared from AgriGrow - Urban Gardening App");

        // Create and launch share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Garden Journal: " + entry.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());

        // If entry has an image, include it
        if (entry.getImagePath() != null && !entry.getImagePath().isEmpty()) {
            try {
                File imageFile = new File(entry.getImagePath());
                if (imageFile.exists()) {
                    Uri imageUri = FileProvider.getUriForFile(
                            context,
                            context.getPackageName() + ".fileprovider",
                            imageFile);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Continue without image if there's an error
            }
        }

        // Launch share dialog
        context.startActivity(Intent.createChooser(shareIntent, "Share Journal Entry"));
    }

    /**
     * Share soil test results
     */
    public void shareSoilTest(SoilTest soilTest) {
        if (soilTest == null) {
            Toast.makeText(context, "No soil test data to share", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create share text
        StringBuilder shareText = new StringBuilder();
        shareText.append("My Soil Test Results from ").append(dateFormatter.format(soilTest.getTestDate())).append("\n\n");
        shareText.append("Location: ").append(soilTest.getLocation()).append("\n\n");
        shareText.append("Results:\n");
        shareText.append("pH Level: ").append(String.format(Locale.US, "%.1f", soilTest.getPhLevel())).append("\n");
        shareText.append("Nitrogen: ").append(String.format(Locale.US, "%.2f%%", soilTest.getNitrogenLevel())).append("\n");
        shareText.append("Phosphorus: ").append(String.format(Locale.US, "%.1f ppm", soilTest.getPhosphorusLevel())).append("\n");
        shareText.append("Potassium: ").append(String.format(Locale.US, "%.1f ppm", soilTest.getPotassiumLevel())).append("\n");
        shareText.append("Organic Matter: ").append(String.format(Locale.US, "%.1f%%", soilTest.getOrganicMatterPercentage())).append("\n\n");
        shareText.append("Soil Health Score: ").append(soilTest.getSoilHealthScore()).append(" (").append(soilTest.getHealthStatus()).append(")\n\n");
        
        if (soilTest.getNotes() != null && !soilTest.getNotes().isEmpty()) {
            shareText.append("Notes: ").append(soilTest.getNotes()).append("\n\n");
        }
        
        shareText.append("Shared from AgriGrow - Urban Gardening App");

        // Create and launch share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Soil Test Results: " + soilTest.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());

        // Launch share dialog
        context.startActivity(Intent.createChooser(shareIntent, "Share Soil Test Results"));
    }

    /**
     * Share gardening technique or guide
     */
    public void shareGardeningTechnique(GardeningTechnique technique) {
        if (technique == null) {
            Toast.makeText(context, "No gardening technique to share", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create share text
        StringBuilder shareText = new StringBuilder();
        shareText.append("Gardening Technique: ").append(technique.getTitle()).append("\n\n");
        shareText.append("Category: ").append(technique.getCategory()).append("\n\n");
        shareText.append(technique.getDescription()).append("\n\n");
        
        if (technique.getSteps() != null && !technique.getSteps().isEmpty()) {
            shareText.append("Steps:\n").append(technique.getSteps()).append("\n\n");
        }
        
        if (technique.getTips() != null && !technique.getTips().isEmpty()) {
            shareText.append("Tips: ").append(technique.getTips()).append("\n\n");
        }
        
        shareText.append("Shared from AgriGrow - Urban Gardening App");

        // Create and launch share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Gardening Technique: " + technique.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());

        // If technique has an image, include it
        if (technique.getImagePath() != null && !technique.getImagePath().isEmpty()) {
            try {
                File imageFile = new File(technique.getImagePath());
                if (imageFile.exists()) {
                    Uri imageUri = FileProvider.getUriForFile(
                            context,
                            context.getPackageName() + ".fileprovider",
                            imageFile);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Continue without image if there's an error
            }
        }

        // Launch share dialog
        context.startActivity(Intent.createChooser(shareIntent, "Share Gardening Technique"));
    }

    /**
     * Share garden achievement or milestone
     */
    public void shareAchievement(String title, String description, Bitmap screenshot) {
        // Create share text
        StringBuilder shareText = new StringBuilder();
        shareText.append("I achieved: ").append(title).append("\n\n");
        
        if (description != null && !description.isEmpty()) {
            shareText.append(description).append("\n\n");
        }
        
        shareText.append("Shared from AgriGrow - Urban Gardening App");

        // Create and launch share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Garden Achievement: " + title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());

        // If screenshot provided, include it
        if (screenshot != null) {
            try {
                // Save bitmap to cache directory
                File cachePath = new File(context.getCacheDir(), "images");
                cachePath.mkdirs();
                File imageFile = new File(cachePath, "achievement_screenshot.png");
                
                FileOutputStream stream = new FileOutputStream(imageFile);
                screenshot.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();

                Uri imageUri = FileProvider.getUriForFile(
                        context,
                        context.getPackageName() + ".fileprovider",
                        imageFile);
                
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (IOException e) {
                e.printStackTrace();
                // Continue without image if there's an error
            }
        }

        // Launch share dialog
        context.startActivity(Intent.createChooser(shareIntent, "Share Achievement"));
    }

    /**
     * Share app invitation with friends
     */
    public void shareAppInvitation() {
        String shareText = "Join me on AgriGrow - Urban Gardening App!\n\n" +
                "I've been using AgriGrow to manage my garden and connect with other urban gardeners. " +
                "It has personalized gardening plans, plant identification, soil testing, and a helpful community.\n\n" +
                "Download it and let's grow together! [App Store Link]";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Join me on AgriGrow Urban Gardening App!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        context.startActivity(Intent.createChooser(shareIntent, "Invite Friends"));
    }
}