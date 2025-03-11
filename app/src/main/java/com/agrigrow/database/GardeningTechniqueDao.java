package com.agrigrow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.agrigrow.model.GardeningTechnique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Data Access Object for GardeningTechnique related database operations.
 */
public class GardeningTechniqueDao {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    /**
     * Constructor initializing the database helper.
     */
    public GardeningTechniqueDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Open database connection.
     */
    private void open() {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Close database connection.
     */
    private void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    /**
     * Get all gardening techniques from the database.
     */
    public List<GardeningTechnique> getAllGuides() {
        List<GardeningTechnique> guides = new ArrayList<>();
        open();

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_GARDENING_TECHNIQUES,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.KEY_NAME + " ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                GardeningTechnique guide = cursorToGuide(cursor);
                guides.add(guide);
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return guides;
    }

    /**
     * Get gardening technique by ID from the database.
     */
    public GardeningTechnique getGuide(int id) {
        open();
        GardeningTechnique guide = null;

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_GARDENING_TECHNIQUES,
                null,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            guide = cursorToGuide(cursor);
            cursor.close();
        }

        close();
        return guide;
    }

    /**
     * Get guides by a list of IDs.
     */
    public List<GardeningTechnique> getGuidesByIds(List<Integer> ids) {
        List<GardeningTechnique> guides = new ArrayList<>();
        
        if (ids == null || ids.isEmpty()) {
            return guides;
        }
        
        open();
        
        // Create a string of question marks for the SQL IN clause
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            placeholders.append("?");
            if (i < ids.size() - 1) {
                placeholders.append(",");
            }
        }
        
        // Convert Integer list to String array for the selection args
        String[] selectionArgs = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            selectionArgs[i] = String.valueOf(ids.get(i));
        }
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_GARDENING_TECHNIQUES,
                null,
                DatabaseHelper.KEY_ID + " IN (" + placeholders.toString() + ")",
                selectionArgs,
                null,
                null,
                DatabaseHelper.KEY_NAME + " ASC"
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                GardeningTechnique guide = cursorToGuide(cursor);
                guides.add(guide);
            } while (cursor.moveToNext());
            
            cursor.close();
        }
        
        close();
        return guides;
    }

    /**
     * Get recommended gardening guides from the database.
     */
    public List<GardeningTechnique> getRecommendedGuides() {
        List<GardeningTechnique> guides = new ArrayList<>();
        open();

        // Get beginner-friendly guides first
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_GARDENING_TECHNIQUES,
                null,
                DatabaseHelper.KEY_DIFFICULTY_LEVEL + " = ?",
                new String[]{"Beginner"},
                null,
                null,
                "RANDOM()",
                "3"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                GardeningTechnique guide = cursorToGuide(cursor);
                guides.add(guide);
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return guides;
    }

    /**
     * Get default guides to display when no recommendations are available.
     */
    public List<GardeningTechnique> getDefaultGuides() {
        List<GardeningTechnique> guides = new ArrayList<>();
        open();

        // Limit to 5 default guides
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_GARDENING_TECHNIQUES,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.KEY_DIFFICULTY_LEVEL + " ASC",
                "5"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                GardeningTechnique guide = cursorToGuide(cursor);
                guides.add(guide);
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return guides;
    }

    /**
     * Update guide favorite status.
     */
    public void updateGuideFavorite(int id, boolean isFavorite) {
        open();
        
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_IS_FAVORITE, isFavorite ? 1 : 0);
        
        database.update(
                DatabaseHelper.TABLE_GARDENING_TECHNIQUES,
                values,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        
        close();
    }

    /**
     * Add a new gardening technique to the database.
     */
    public long addGuide(GardeningTechnique guide) {
        open();
        
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_NAME, guide.getName());
        values.put(DatabaseHelper.KEY_DESCRIPTION, guide.getDescription());
        values.put(DatabaseHelper.KEY_DETAILED_GUIDE, guide.getDetailedGuide());
        
        // Convert list to comma-separated string
        if (guide.getSteps() != null) {
            values.put(DatabaseHelper.KEY_STEPS, TextUtils.join(",", guide.getSteps()));
        }
        
        values.put(DatabaseHelper.KEY_DIFFICULTY_LEVEL, guide.getDifficultyLevel());
        values.put(DatabaseHelper.KEY_SPACE_REQUIREMENT, guide.getSpaceRequirement());
        values.put(DatabaseHelper.KEY_COST_ESTIMATE, guide.getCostEstimate());
        values.put(DatabaseHelper.KEY_MAINTENANCE_LEVEL, guide.getMaintenanceLevel());
        
        // Convert list to comma-separated string
        if (guide.getRequiredMaterials() != null) {
            values.put(DatabaseHelper.KEY_REQUIRED_MATERIALS, TextUtils.join(",", guide.getRequiredMaterials()));
        }
        
        // Convert list to comma-separated string
        if (guide.getRecommendedPlants() != null) {
            values.put(DatabaseHelper.KEY_RECOMMENDED_PLANTS, TextUtils.join(",", guide.getRecommendedPlants()));
        }
        
        values.put(DatabaseHelper.KEY_IMAGE_URL, guide.getImageUrl());
        values.put(DatabaseHelper.KEY_VIDEO_URL, guide.getVideoUrl());
        values.put(DatabaseHelper.KEY_IS_FAVORITE, guide.isFavorite() ? 1 : 0);
        
        long id = database.insert(DatabaseHelper.TABLE_GARDENING_TECHNIQUES, null, values);
        close();
        
        return id;
    }

    /**
     * Convert a database cursor to a GardeningTechnique object.
     */
    private GardeningTechnique cursorToGuide(Cursor cursor) {
        GardeningTechnique guide = new GardeningTechnique();
        
        int idIndex = cursor.getColumnIndex(DatabaseHelper.KEY_ID);
        int nameIndex = cursor.getColumnIndex(DatabaseHelper.KEY_NAME);
        int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.KEY_DESCRIPTION);
        int detailedGuideIndex = cursor.getColumnIndex(DatabaseHelper.KEY_DETAILED_GUIDE);
        int stepsIndex = cursor.getColumnIndex(DatabaseHelper.KEY_STEPS);
        int difficultyLevelIndex = cursor.getColumnIndex(DatabaseHelper.KEY_DIFFICULTY_LEVEL);
        int spaceRequirementIndex = cursor.getColumnIndex(DatabaseHelper.KEY_SPACE_REQUIREMENT);
        int costEstimateIndex = cursor.getColumnIndex(DatabaseHelper.KEY_COST_ESTIMATE);
        int maintenanceLevelIndex = cursor.getColumnIndex(DatabaseHelper.KEY_MAINTENANCE_LEVEL);
        int requiredMaterialsIndex = cursor.getColumnIndex(DatabaseHelper.KEY_REQUIRED_MATERIALS);
        int recommendedPlantsIndex = cursor.getColumnIndex(DatabaseHelper.KEY_RECOMMENDED_PLANTS);
        int imageUrlIndex = cursor.getColumnIndex(DatabaseHelper.KEY_IMAGE_URL);
        int videoUrlIndex = cursor.getColumnIndex(DatabaseHelper.KEY_VIDEO_URL);
        int isFavoriteIndex = cursor.getColumnIndex(DatabaseHelper.KEY_IS_FAVORITE);
        
        if (idIndex != -1) guide.setId(cursor.getInt(idIndex));
        if (nameIndex != -1) guide.setName(cursor.getString(nameIndex));
        if (descriptionIndex != -1) guide.setDescription(cursor.getString(descriptionIndex));
        if (detailedGuideIndex != -1) guide.setDetailedGuide(cursor.getString(detailedGuideIndex));
        
        // Convert comma-separated string to list
        if (stepsIndex != -1 && !cursor.isNull(stepsIndex)) {
            String stepsStr = cursor.getString(stepsIndex);
            if (!TextUtils.isEmpty(stepsStr)) {
                guide.setSteps(Arrays.asList(stepsStr.split(",")));
            }
        }
        
        if (difficultyLevelIndex != -1) guide.setDifficultyLevel(cursor.getString(difficultyLevelIndex));
        if (spaceRequirementIndex != -1) guide.setSpaceRequirement(cursor.getString(spaceRequirementIndex));
        if (costEstimateIndex != -1) guide.setCostEstimate(cursor.getString(costEstimateIndex));
        if (maintenanceLevelIndex != -1) guide.setMaintenanceLevel(cursor.getString(maintenanceLevelIndex));
        
        // Convert comma-separated string to list
        if (requiredMaterialsIndex != -1 && !cursor.isNull(requiredMaterialsIndex)) {
            String materialsStr = cursor.getString(requiredMaterialsIndex);
            if (!TextUtils.isEmpty(materialsStr)) {
                guide.setRequiredMaterials(Arrays.asList(materialsStr.split(",")));
            }
        }
        
        // Convert comma-separated string to list
        if (recommendedPlantsIndex != -1 && !cursor.isNull(recommendedPlantsIndex)) {
            String plantsStr = cursor.getString(recommendedPlantsIndex);
            if (!TextUtils.isEmpty(plantsStr)) {
                guide.setRecommendedPlants(Arrays.asList(plantsStr.split(",")));
            }
        }
        
        if (imageUrlIndex != -1) guide.setImageUrl(cursor.getString(imageUrlIndex));
        if (videoUrlIndex != -1) guide.setVideoUrl(cursor.getString(videoUrlIndex));
        if (isFavoriteIndex != -1) guide.setFavorite(cursor.getInt(isFavoriteIndex) == 1);
        
        return guide;
    }
}
