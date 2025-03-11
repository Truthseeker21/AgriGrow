package com.agrigrow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.agrigrow.model.Plant;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Plant related database operations.
 */
public class PlantDao {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    /**
     * Constructor initializing the database helper.
     */
    public PlantDao(Context context) {
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
     * Get all plants from the database.
     */
    public List<Plant> getAllPlants() {
        List<Plant> plants = new ArrayList<>();
        open();

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_PLANTS,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.KEY_NAME + " ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Plant plant = cursorToPlant(cursor);
                plants.add(plant);
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return plants;
    }

    /**
     * Get plant by ID from the database.
     */
    public Plant getPlant(int id) {
        open();
        Plant plant = null;

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_PLANTS,
                null,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            plant = cursorToPlant(cursor);
            cursor.close();
        }

        close();
        return plant;
    }

    /**
     * Get plants by a list of IDs.
     */
    public List<Plant> getPlantsByIds(List<Integer> ids) {
        List<Plant> plants = new ArrayList<>();
        
        if (ids == null || ids.isEmpty()) {
            return plants;
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
                DatabaseHelper.TABLE_PLANTS,
                null,
                DatabaseHelper.KEY_ID + " IN (" + placeholders.toString() + ")",
                selectionArgs,
                null,
                null,
                DatabaseHelper.KEY_NAME + " ASC"
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Plant plant = cursorToPlant(cursor);
                plants.add(plant);
            } while (cursor.moveToNext());
            
            cursor.close();
        }
        
        close();
        return plants;
    }

    /**
     * Get seasonal plants from the database.
     */
    public List<Plant> getSeasonalPlants(String season) {
        List<Plant> plants = new ArrayList<>();
        open();

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_PLANTS,
                null,
                DatabaseHelper.KEY_SEASON + " LIKE ?",
                new String[]{"%" + season + "%"},
                null,
                null,
                DatabaseHelper.KEY_NAME + " ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Plant plant = cursorToPlant(cursor);
                plants.add(plant);
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return plants;
    }

    /**
     * Get default plants to display when no seasonal plants are available.
     */
    public List<Plant> getDefaultPlants() {
        List<Plant> plants = new ArrayList<>();
        open();

        // Limit to 5 default plants
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_PLANTS,
                null,
                DatabaseHelper.KEY_SUITABLE_CONTAINERS + " = ?",
                new String[]{"1"},
                null,
                null,
                DatabaseHelper.KEY_DIFFICULTY_LEVEL + " ASC",
                "5"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Plant plant = cursorToPlant(cursor);
                plants.add(plant);
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return plants;
    }

    /**
     * Update plant bookmark status.
     */
    public void updatePlantBookmark(int id, boolean isBookmarked) {
        open();
        
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_IS_BOOKMARKED, isBookmarked ? 1 : 0);
        
        database.update(
                DatabaseHelper.TABLE_PLANTS,
                values,
                DatabaseHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        
        close();
    }

    /**
     * Add a new plant to the database.
     */
    public long addPlant(Plant plant) {
        open();
        
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_NAME, plant.getName());
        values.put(DatabaseHelper.KEY_SCIENTIFIC_NAME, plant.getScientificName());
        values.put(DatabaseHelper.KEY_DESCRIPTION, plant.getDescription());
        values.put(DatabaseHelper.KEY_CARE_INSTRUCTIONS, plant.getCareInstructions());
        values.put(DatabaseHelper.KEY_WATERING_NEEDS, plant.getWateringNeeds());
        values.put(DatabaseHelper.KEY_SUNLIGHT_REQUIREMENTS, plant.getSunlightRequirements());
        values.put(DatabaseHelper.KEY_SOIL_TYPE, plant.getSoilType());
        values.put(DatabaseHelper.KEY_GROWTH_DURATION, plant.getGrowthDurationDays());
        values.put(DatabaseHelper.KEY_HARVEST_INSTRUCTIONS, plant.getHarvestInstructions());
        values.put(DatabaseHelper.KEY_SUITABLE_CONTAINERS, plant.isSuitableForContainers() ? 1 : 0);
        values.put(DatabaseHelper.KEY_SUITABLE_INDOOR, plant.isSuitableForIndoor() ? 1 : 0);
        values.put(DatabaseHelper.KEY_TEMPERATURE_RANGE, plant.getIdealTemperatureRange());
        values.put(DatabaseHelper.KEY_SEASON, plant.getSeasonToPlant());
        values.put(DatabaseHelper.KEY_DIFFICULTY_LEVEL, plant.getDifficultyLevel());
        values.put(DatabaseHelper.KEY_IMAGE_URL, plant.getImageUrl());
        values.put(DatabaseHelper.KEY_IS_BOOKMARKED, plant.isBookmarked() ? 1 : 0);
        
        long id = database.insert(DatabaseHelper.TABLE_PLANTS, null, values);
        close();
        
        return id;
    }

    /**
     * Convert a database cursor to a Plant object.
     */
    private Plant cursorToPlant(Cursor cursor) {
        Plant plant = new Plant();
        
        int idIndex = cursor.getColumnIndex(DatabaseHelper.KEY_ID);
        int nameIndex = cursor.getColumnIndex(DatabaseHelper.KEY_NAME);
        int scientificNameIndex = cursor.getColumnIndex(DatabaseHelper.KEY_SCIENTIFIC_NAME);
        int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.KEY_DESCRIPTION);
        int careInstructionsIndex = cursor.getColumnIndex(DatabaseHelper.KEY_CARE_INSTRUCTIONS);
        int wateringNeedsIndex = cursor.getColumnIndex(DatabaseHelper.KEY_WATERING_NEEDS);
        int sunlightRequirementsIndex = cursor.getColumnIndex(DatabaseHelper.KEY_SUNLIGHT_REQUIREMENTS);
        int soilTypeIndex = cursor.getColumnIndex(DatabaseHelper.KEY_SOIL_TYPE);
        int growthDurationIndex = cursor.getColumnIndex(DatabaseHelper.KEY_GROWTH_DURATION);
        int harvestInstructionsIndex = cursor.getColumnIndex(DatabaseHelper.KEY_HARVEST_INSTRUCTIONS);
        int suitableContainersIndex = cursor.getColumnIndex(DatabaseHelper.KEY_SUITABLE_CONTAINERS);
        int suitableIndoorIndex = cursor.getColumnIndex(DatabaseHelper.KEY_SUITABLE_INDOOR);
        int temperatureRangeIndex = cursor.getColumnIndex(DatabaseHelper.KEY_TEMPERATURE_RANGE);
        int seasonIndex = cursor.getColumnIndex(DatabaseHelper.KEY_SEASON);
        int difficultyLevelIndex = cursor.getColumnIndex(DatabaseHelper.KEY_DIFFICULTY_LEVEL);
        int imageUrlIndex = cursor.getColumnIndex(DatabaseHelper.KEY_IMAGE_URL);
        int isBookmarkedIndex = cursor.getColumnIndex(DatabaseHelper.KEY_IS_BOOKMARKED);
        
        if (idIndex != -1) plant.setId(cursor.getInt(idIndex));
        if (nameIndex != -1) plant.setName(cursor.getString(nameIndex));
        if (scientificNameIndex != -1) plant.setScientificName(cursor.getString(scientificNameIndex));
        if (descriptionIndex != -1) plant.setDescription(cursor.getString(descriptionIndex));
        if (careInstructionsIndex != -1) plant.setCareInstructions(cursor.getString(careInstructionsIndex));
        if (wateringNeedsIndex != -1) plant.setWateringNeeds(cursor.getString(wateringNeedsIndex));
        if (sunlightRequirementsIndex != -1) plant.setSunlightRequirements(cursor.getString(sunlightRequirementsIndex));
        if (soilTypeIndex != -1) plant.setSoilType(cursor.getString(soilTypeIndex));
        if (growthDurationIndex != -1) plant.setGrowthDurationDays(cursor.getInt(growthDurationIndex));
        if (harvestInstructionsIndex != -1) plant.setHarvestInstructions(cursor.getString(harvestInstructionsIndex));
        if (suitableContainersIndex != -1) plant.setSuitableForContainers(cursor.getInt(suitableContainersIndex) == 1);
        if (suitableIndoorIndex != -1) plant.setSuitableForIndoor(cursor.getInt(suitableIndoorIndex) == 1);
        if (temperatureRangeIndex != -1) plant.setIdealTemperatureRange(cursor.getString(temperatureRangeIndex));
        if (seasonIndex != -1) plant.setSeasonToPlant(cursor.getString(seasonIndex));
        if (difficultyLevelIndex != -1) plant.setDifficultyLevel(cursor.getString(difficultyLevelIndex));
        if (imageUrlIndex != -1) plant.setImageUrl(cursor.getString(imageUrlIndex));
        if (isBookmarkedIndex != -1) plant.setBookmarked(cursor.getInt(isBookmarkedIndex) == 1);
        
        return plant;
    }
}
