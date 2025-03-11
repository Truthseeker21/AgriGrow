package com.agrigrow.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Helper class for identifying plants from images using a plant recognition API.
 * This class handles image processing and API communication.
 */
public class PlantIdentificationHelper {
    
    private static final String TAG = "PlantIdHelper";
    
    // Plant.id API endpoint - for real use, you would need to sign up at https://plant.id/
    private static final String PLANT_ID_API_URL = "https://api.plant.id/v2/identify";
    private static String API_KEY = "YOUR_API_KEY"; // This should be stored securely
    
    private final Context context;
    private final Executor executor;
    private static PlantIdentificationHelper instance;
    
    /**
     * Private constructor (singleton pattern)
     * @param context Application context
     */
    private PlantIdentificationHelper(Context context) {
        this.context = context.getApplicationContext();
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    /**
     * Get singleton instance
     * @param context Application context
     * @return PlantIdentificationHelper instance
     */
    public static synchronized PlantIdentificationHelper getInstance(Context context) {
        if (instance == null) {
            instance = new PlantIdentificationHelper(context);
        }
        return instance;
    }
    
    /**
     * Set the API key (should be called before using any API methods)
     * @param apiKey The Plant.id API key
     */
    public static void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }
    
    /**
     * Identify a plant from a local image file URI
     * @param imageUri URI of the image file
     * @param listener Callback to receive identification results
     */
    public void identifyPlantFromUri(Uri imageUri, OnPlantIdentificationListener listener) {
        executor.execute(() -> {
            try {
                // Load the image from URI
                InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                
                if (bitmap == null) {
                    final String errorMessage = "Failed to decode image";
                    android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                    mainHandler.post(() -> listener.onIdentificationError(errorMessage));
                    return;
                }
                
                // Convert bitmap to base64
                String base64Image = bitmapToBase64(bitmap);
                
                // Send to API
                identifyPlantWithBase64(base64Image, listener);
                
            } catch (IOException e) {
                Log.e(TAG, "Error loading image", e);
                final String errorMessage = "Error loading image: " + e.getMessage();
                android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                mainHandler.post(() -> listener.onIdentificationError(errorMessage));
            }
        });
    }
    
    /**
     * Identify a plant from a bitmap image
     * @param bitmap Bitmap image
     * @param listener Callback to receive identification results
     */
    public void identifyPlantFromBitmap(Bitmap bitmap, OnPlantIdentificationListener listener) {
        executor.execute(() -> {
            try {
                // Convert bitmap to base64
                String base64Image = bitmapToBase64(bitmap);
                
                // Send to API
                identifyPlantWithBase64(base64Image, listener);
                
            } catch (Exception e) {
                Log.e(TAG, "Error processing image", e);
                final String errorMessage = "Error processing image: " + e.getMessage();
                android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                mainHandler.post(() -> listener.onIdentificationError(errorMessage));
            }
        });
    }
    
    /**
     * Identify a plant from a file path
     * @param filePath Path to the image file
     * @param listener Callback to receive identification results
     */
    public void identifyPlantFromFile(String filePath, OnPlantIdentificationListener listener) {
        executor.execute(() -> {
            try {
                // Load the image from file
                File imageFile = new File(filePath);
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                
                if (bitmap == null) {
                    final String errorMessage = "Failed to decode image";
                    android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                    mainHandler.post(() -> listener.onIdentificationError(errorMessage));
                    return;
                }
                
                // Convert bitmap to base64
                String base64Image = bitmapToBase64(bitmap);
                
                // Send to API
                identifyPlantWithBase64(base64Image, listener);
                
            } catch (Exception e) {
                Log.e(TAG, "Error loading image", e);
                final String errorMessage = "Error loading image: " + e.getMessage();
                android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                mainHandler.post(() -> listener.onIdentificationError(errorMessage));
            }
        });
    }
    
    /**
     * Convert a bitmap to base64 string
     * @param bitmap Image bitmap
     * @return Base64 encoded string
     */
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
    }
    
    /**
     * Send base64 encoded image to plant identification API
     * @param base64Image Base64 encoded image data
     * @param listener Callback to receive identification results
     */
    private void identifyPlantWithBase64(String base64Image, OnPlantIdentificationListener listener) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        
        try {
            // Prepare API request
            URL url = new URL(PLANT_ID_API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Api-Key", API_KEY);
            connection.setDoOutput(true);
            
            // Create JSON payload
            JSONObject requestJson = new JSONObject();
            JSONArray imagesArray = new JSONArray();
            imagesArray.put(base64Image);
            
            requestJson.put("images", imagesArray);
            requestJson.put("organs", new JSONArray(new String[]{"leaf", "flower", "fruit", "bark", "habit"}));
            requestJson.put("include_related_images", true);
            
            // Write request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestJson.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // Read the response
            int responseCode = connection.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                
                // Parse JSON response
                JSONObject response = new JSONObject(buffer.toString());
                List<PlantIdentificationResult> results = parseIdentificationResults(response);
                
                // Return results on main thread
                android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                mainHandler.post(() -> listener.onIdentificationResults(results));
                
            } else {
                InputStream errorStream = connection.getErrorStream();
                reader = new BufferedReader(new InputStreamReader(errorStream));
                StringBuilder buffer = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                
                final String errorMessage = "API Error (" + responseCode + "): " + buffer.toString();
                android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                mainHandler.post(() -> listener.onIdentificationError(errorMessage));
            }
            
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error communicating with API", e);
            final String errorMessage = "Error communicating with API: " + e.getMessage();
            android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
            mainHandler.post(() -> listener.onIdentificationError(errorMessage));
        } finally {
            // Close connections
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing reader", e);
                }
            }
        }
    }
    
    /**
     * Parse identification results from API response
     * @param jsonResponse API response in JSON format
     * @return List of plant identification results
     */
    private List<PlantIdentificationResult> parseIdentificationResults(JSONObject jsonResponse) throws JSONException {
        List<PlantIdentificationResult> results = new ArrayList<>();
        
        // Check if API call was successful
        if (jsonResponse.has("result") && jsonResponse.has("suggestions")) {
            JSONArray suggestions = jsonResponse.getJSONArray("suggestions");
            
            // Process each suggestion
            for (int i = 0; i < suggestions.length(); i++) {
                JSONObject suggestion = suggestions.getJSONObject(i);
                
                PlantIdentificationResult result = new PlantIdentificationResult();
                
                // Parse basic info
                if (suggestion.has("id")) {
                    result.id = suggestion.getInt("id");
                }
                
                if (suggestion.has("probability")) {
                    result.probability = suggestion.getDouble("probability");
                }
                
                // Parse plant name info
                if (suggestion.has("plant_name")) {
                    result.plantName = suggestion.getString("plant_name");
                }
                
                if (suggestion.has("plant_details")) {
                    JSONObject details = suggestion.getJSONObject("plant_details");
                    
                    if (details.has("common_names")) {
                        JSONArray commonNames = details.getJSONArray("common_names");
                        for (int j = 0; j < commonNames.length(); j++) {
                            result.commonNames.add(commonNames.getString(j));
                        }
                    }
                    
                    if (details.has("scientific_name")) {
                        result.scientificName = details.getString("scientific_name");
                    }
                    
                    if (details.has("structured_name")) {
                        JSONObject structuredName = details.getJSONObject("structured_name");
                        if (structuredName.has("genus")) {
                            result.genus = structuredName.getString("genus");
                        }
                        if (structuredName.has("species")) {
                            result.species = structuredName.getString("species");
                        }
                    }
                    
                    if (details.has("taxonomy")) {
                        JSONObject taxonomy = details.getJSONObject("taxonomy");
                        if (taxonomy.has("class")) {
                            result.taxonomyClass = taxonomy.getString("class");
                        }
                        if (taxonomy.has("family")) {
                            result.family = taxonomy.getString("family");
                        }
                        if (taxonomy.has("kingdom")) {
                            result.kingdom = taxonomy.getString("kingdom");
                        }
                    }
                    
                    if (details.has("url")) {
                        result.infoUrl = details.getString("url");
                    }
                    
                    if (details.has("wiki_description") && !details.isNull("wiki_description")) {
                        JSONObject wikiDesc = details.getJSONObject("wiki_description");
                        if (wikiDesc.has("value")) {
                            result.description = wikiDesc.getString("value");
                        }
                    }
                    
                    if (details.has("edible_parts") && !details.isNull("edible_parts")) {
                        JSONArray edibleParts = details.getJSONArray("edible_parts");
                        for (int j = 0; j < edibleParts.length(); j++) {
                            result.edibleParts.add(edibleParts.getString(j));
                        }
                    }
                }
                
                // Parse images
                if (suggestion.has("similar_images")) {
                    JSONArray similarImages = suggestion.getJSONArray("similar_images");
                    for (int j = 0; j < similarImages.length(); j++) {
                        JSONObject image = similarImages.getJSONObject(j);
                        if (image.has("url")) {
                            result.imageUrls.add(image.getString("url"));
                        }
                    }
                }
                
                results.add(result);
            }
        }
        
        return results;
    }
    
    /**
     * Class to hold plant identification results
     */
    public static class PlantIdentificationResult {
        public int id; // Plant ID in API database
        public double probability; // Confidence score (0-1)
        public String plantName; // Primary plant name
        public List<String> commonNames = new ArrayList<>(); // Common names
        public String scientificName; // Full scientific name
        public String genus; // Genus name
        public String species; // Species name
        public String taxonomyClass; // Taxonomic class
        public String family; // Plant family
        public String kingdom; // Kingdom
        public String description; // Plant description
        public String infoUrl; // URL to more information
        public List<String> edibleParts = new ArrayList<>(); // Edible parts list
        public List<String> imageUrls = new ArrayList<>(); // Reference image URLs
        
        /**
         * Get the confidence percentage as a string
         * @return Formatted percentage string
         */
        public String getConfidenceString() {
            int percentage = (int) (probability * 100);
            return percentage + "%";
        }
        
        /**
         * Get first common name or scientific name if no common names exist
         * @return Best display name for the plant
         */
        public String getDisplayName() {
            if (!commonNames.isEmpty()) {
                return commonNames.get(0);
            }
            return scientificName != null ? scientificName : plantName;
        }
        
        /**
         * Get a comma-separated list of common names
         * @return Formatted common names string
         */
        public String getCommonNamesString() {
            if (commonNames.isEmpty()) {
                return "No common names available";
            }
            
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < commonNames.size(); i++) {
                builder.append(commonNames.get(i));
                if (i < commonNames.size() - 1) {
                    builder.append(", ");
                }
            }
            return builder.toString();
        }
        
        /**
         * Get a formatted scientific name with genus and species
         * @return Formatted scientific name
         */
        public String getFormattedScientificName() {
            if (genus != null && species != null) {
                return "<i>" + genus + " " + species + "</i>";
            }
            return scientificName != null ? "<i>" + scientificName + "</i>" : "Unknown";
        }
        
        /**
         * Get a short taxonomy string
         * @return Formatted taxonomy
         */
        public String getTaxonomyString() {
            if (family != null) {
                return "Family: " + family;
            }
            return "";
        }
        
        /**
         * Get a comma-separated list of edible parts
         * @return Formatted edible parts string
         */
        public String getEdiblePartsString() {
            if (edibleParts.isEmpty()) {
                return "No information available";
            }
            
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < edibleParts.size(); i++) {
                builder.append(edibleParts.get(i));
                if (i < edibleParts.size() - 1) {
                    builder.append(", ");
                }
            }
            return builder.toString();
        }
        
        /**
         * Get the first image URL or null if none exist
         * @return URL to a reference image
         */
        public String getFirstImageUrl() {
            return !imageUrls.isEmpty() ? imageUrls.get(0) : null;
        }
    }
    
    /**
     * Interface for plant identification callbacks
     */
    public interface OnPlantIdentificationListener {
        void onIdentificationResults(List<PlantIdentificationResult> results);
        void onIdentificationError(String errorMessage);
    }
}