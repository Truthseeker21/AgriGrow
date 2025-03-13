package com.agrigrow.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite database helper for managing the app's local database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "AgriGrow.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_PLANTS = "plants";
    public static final String TABLE_GARDENING_TECHNIQUES = "gardening_techniques";
    public static final String TABLE_FORUM_POSTS = "forum_posts";
    public static final String TABLE_COMMENTS = "comments";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_USER_SAVED_PLANTS = "user_saved_plants";
    public static final String TABLE_USER_SAVED_GUIDES = "user_saved_guides";

    // Common Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_DIFFICULTY_LEVEL = "difficulty_level";
    
    // Plants Table Columns
    public static final String KEY_SCIENTIFIC_NAME = "scientific_name";
    public static final String KEY_CARE_INSTRUCTIONS = "care_instructions";
    public static final String KEY_WATERING_NEEDS = "watering_needs";
    public static final String KEY_SUNLIGHT_REQUIREMENTS = "sunlight_requirements";
    public static final String KEY_SOIL_TYPE = "soil_type";
    public static final String KEY_GROWTH_DURATION = "growth_duration";
    public static final String KEY_HARVEST_INSTRUCTIONS = "harvest_instructions";
    public static final String KEY_SUITABLE_CONTAINERS = "suitable_containers";
    public static final String KEY_SUITABLE_INDOOR = "suitable_indoor";
    public static final String KEY_TEMPERATURE_RANGE = "temperature_range";
    public static final String KEY_SEASON = "season";
    public static final String KEY_IS_BOOKMARKED = "is_bookmarked";
    
    // Gardening Techniques Table Columns
    public static final String KEY_DETAILED_GUIDE = "detailed_guide";
    public static final String KEY_STEPS = "steps";
    public static final String KEY_SPACE_REQUIREMENT = "space_requirement";
    public static final String KEY_COST_ESTIMATE = "cost_estimate";
    public static final String KEY_MAINTENANCE_LEVEL = "maintenance_level";
    public static final String KEY_REQUIRED_MATERIALS = "required_materials";
    public static final String KEY_RECOMMENDED_PLANTS = "recommended_plants";
    public static final String KEY_VIDEO_URL = "video_url";
    public static final String KEY_IS_FAVORITE = "is_favorite";
    
    // Forum Posts Table Columns
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_AUTHOR_ID = "author_id";
    public static final String KEY_AUTHOR_NAME = "author_name";
    public static final String KEY_AUTHOR_IMAGE_URL = "author_image_url";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_TAGS = "tags";
    public static final String KEY_LIKE_COUNT = "like_count";
    public static final String KEY_COMMENT_COUNT = "comment_count";
    public static final String KEY_IS_LIKED = "is_liked";
    
    // Comments Table Columns
    public static final String KEY_POST_ID = "post_id";
    
    // Users Table Columns
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_BIO = "bio";
    public static final String KEY_EXPERIENCE_LEVEL = "experience_level";
    public static final String KEY_AREAS_OF_INTEREST = "areas_of_interest";
    public static final String KEY_GARDEN_TYPE = "garden_type";
    public static final String KEY_GARDENING_SPACE = "gardening_space";
    public static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    public static final String KEY_REGISTRATION_DATE = "registration_date";
    public static final String KEY_POST_COUNT = "post_count";
    public static final String KEY_COMMENT_COUNT = "comment_count";
    public static final String KEY_POINTS = "points";
    
    // User Saved Items Tables Columns
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_PLANT_ID = "plant_id";
    public static final String KEY_GUIDE_ID = "guide_id";

    // Singleton instance
    private static DatabaseHelper sInstance;

    /**
     * Get singleton instance of database helper
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Private constructor to prevent direct instantiation
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Plants table
        String CREATE_PLANTS_TABLE = "CREATE TABLE " + TABLE_PLANTS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT," +
                KEY_SCIENTIFIC_NAME + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_CARE_INSTRUCTIONS + " TEXT," +
                KEY_WATERING_NEEDS + " TEXT," +
                KEY_SUNLIGHT_REQUIREMENTS + " TEXT," +
                KEY_SOIL_TYPE + " TEXT," +
                KEY_GROWTH_DURATION + " INTEGER," +
                KEY_HARVEST_INSTRUCTIONS + " TEXT," +
                KEY_SUITABLE_CONTAINERS + " INTEGER," +
                KEY_SUITABLE_INDOOR + " INTEGER," +
                KEY_TEMPERATURE_RANGE + " TEXT," +
                KEY_SEASON + " TEXT," +
                KEY_DIFFICULTY_LEVEL + " TEXT," +
                KEY_IMAGE_URL + " TEXT," +
                KEY_IS_BOOKMARKED + " INTEGER" +
                ")";
        db.execSQL(CREATE_PLANTS_TABLE);

        // Create Gardening Techniques table
        String CREATE_GARDENING_TECHNIQUES_TABLE = "CREATE TABLE " + TABLE_GARDENING_TECHNIQUES +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_DETAILED_GUIDE + " TEXT," +
                KEY_STEPS + " TEXT," +
                KEY_DIFFICULTY_LEVEL + " TEXT," +
                KEY_SPACE_REQUIREMENT + " TEXT," +
                KEY_COST_ESTIMATE + " TEXT," +
                KEY_MAINTENANCE_LEVEL + " TEXT," +
                KEY_REQUIRED_MATERIALS + " TEXT," +
                KEY_RECOMMENDED_PLANTS + " TEXT," +
                KEY_IMAGE_URL + " TEXT," +
                KEY_VIDEO_URL + " TEXT," +
                KEY_IS_FAVORITE + " INTEGER" +
                ")";
        db.execSQL(CREATE_GARDENING_TECHNIQUES_TABLE);

        // Create Forum Posts table
        String CREATE_FORUM_POSTS_TABLE = "CREATE TABLE " + TABLE_FORUM_POSTS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_TITLE + " TEXT," +
                KEY_CONTENT + " TEXT," +
                KEY_AUTHOR_ID + " TEXT," +
                KEY_AUTHOR_NAME + " TEXT," +
                KEY_AUTHOR_IMAGE_URL + " TEXT," +
                KEY_TIMESTAMP + " INTEGER," +
                KEY_TAGS + " TEXT," +
                KEY_IMAGE_URL + " TEXT," +
                KEY_LIKE_COUNT + " INTEGER," +
                KEY_COMMENT_COUNT + " INTEGER," +
                KEY_IS_LIKED + " INTEGER" +
                ")";
        db.execSQL(CREATE_FORUM_POSTS_TABLE);

        // Create Comments table
        String CREATE_COMMENTS_TABLE = "CREATE TABLE " + TABLE_COMMENTS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_POST_ID + " INTEGER," +
                KEY_CONTENT + " TEXT," +
                KEY_AUTHOR_ID + " TEXT," +
                KEY_AUTHOR_NAME + " TEXT," +
                KEY_AUTHOR_IMAGE_URL + " TEXT," +
                KEY_TIMESTAMP + " INTEGER," +
                KEY_LIKE_COUNT + " INTEGER," +
                KEY_IS_LIKED + " INTEGER," +
                "FOREIGN KEY(" + KEY_POST_ID + ") REFERENCES " + TABLE_FORUM_POSTS + "(" + KEY_ID + ")" +
                ")";
        db.execSQL(CREATE_COMMENTS_TABLE);

        // Create Users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_ID + " TEXT PRIMARY KEY," +
                KEY_USERNAME + " TEXT UNIQUE," +
                KEY_EMAIL + " TEXT UNIQUE," +
                KEY_NAME + " TEXT," +
                KEY_LOCATION + " TEXT," +
                KEY_IMAGE_URL + " TEXT," +
                KEY_BIO + " TEXT," +
                KEY_EXPERIENCE_LEVEL + " INTEGER," +
                KEY_AREAS_OF_INTEREST + " TEXT," +
                KEY_GARDEN_TYPE + " TEXT," +
                KEY_GARDENING_SPACE + " INTEGER," +
                KEY_NOTIFICATIONS_ENABLED + " INTEGER," +
                KEY_REGISTRATION_DATE + " TEXT," +
                KEY_POST_COUNT + " INTEGER," +
                KEY_COMMENT_COUNT + " INTEGER," +
                KEY_POINTS + " INTEGER" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create User Saved Plants table
        String CREATE_USER_SAVED_PLANTS_TABLE = "CREATE TABLE " + TABLE_USER_SAVED_PLANTS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_ID + " TEXT," +
                KEY_PLANT_ID + " INTEGER," +
                "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")," +
                "FOREIGN KEY(" + KEY_PLANT_ID + ") REFERENCES " + TABLE_PLANTS + "(" + KEY_ID + ")" +
                ")";
        db.execSQL(CREATE_USER_SAVED_PLANTS_TABLE);

        // Create User Saved Guides table
        String CREATE_USER_SAVED_GUIDES_TABLE = "CREATE TABLE " + TABLE_USER_SAVED_GUIDES +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_ID + " TEXT," +
                KEY_GUIDE_ID + " INTEGER," +
                "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")," +
                "FOREIGN KEY(" + KEY_GUIDE_ID + ") REFERENCES " + TABLE_GARDENING_TECHNIQUES + "(" + KEY_ID + ")" +
                ")";
        db.execSQL(CREATE_USER_SAVED_GUIDES_TABLE);

        // Populate the database with sample data
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Drop older tables if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_SAVED_GUIDES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_SAVED_PLANTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORUM_POSTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_GARDENING_TECHNIQUES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

            // Create new tables
            onCreate(db);
        }
    }

    /**
     * Insert sample data into the database for initial usage
     */
    private void insertSampleData(SQLiteDatabase db) {
        // Tropical Vegetables & Herbs for Urban Gardens
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (1, 'Bok Choy', 'Brassica rapa var. chinensis', " +
                "'Popular leafy vegetable that grows quickly in containers in tropical climates', 'Regular', 'Partial to full sun', " +
                "'Rich, well-draining', 30, 1, 1, 'Year-round', 'Beginner', " +
                "'https://example.com/bokchoy.jpg', 0)");
        
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (2, 'Water Spinach', 'Ipomoea aquatica', " +
                "'Water spinach that grows well in hot, humid climate and can be harvested multiple times', 'High', 'Full sun', " +
                "'Moist, fertile', 30, 1, 0, 'Year-round', 'Beginner', " +
                "'https://example.com/water_spinach.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (9, 'Bird's Eye Chili', 'Capsicum frutescens', " +
                "'Hot chili pepper that grows as a perennial in tropical climates, good for container gardening', 'Moderate', 'Full sun', " +
                "'Well-draining, fertile', 70, 1, 0, 'Year-round', 'Beginner', " +
                "'https://example.com/chili.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (10, 'Moringa', 'Moringa oleifera', " +
                "'Nutritious tree with edible leaves, thrives in tropical climate and can be grown in large containers', 'Moderate', 'Full sun', " +
                "'Well-draining', 90, 1, 0, 'Year-round', 'Beginner', " +
                "'https://example.com/moringa.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (11, 'Malabar Spinach', 'Basella alba', " +
                "'Vine spinach that grows well in hot weather and is rich in vitamins and minerals', 'Moderate', 'Partial to full sun', " +
                "'Fertile, well-draining', 45, 1, 0, 'Year-round', 'Beginner', " +
                "'https://example.com/malabar_spinach.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (12, 'Sweet Potato Leaves', 'Ipomoea batatas', " +
                "'Sweet potato leaves that are easy to grow and can be harvested repeatedly', 'Moderate', 'Full sun', " +
                "'Sandy, well-draining', 30, 1, 0, 'Year-round', 'Beginner', " +
                "'https://example.com/sweet_potato_leaves.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (13, 'Okra', 'Abelmoschus esculentus', " +
                "'Tropical vegetable that grows well in hot weather and requires minimal care', 'Moderate', 'Full sun', " +
                "'Fertile, well-draining', 60, 1, 0, 'Year-round', 'Beginner', " +
                "'https://example.com/okra.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (14, 'Calamondin', 'Citrofortunella microcarpa', " +
                "'Citrus fruit that can be grown in containers and used for cooking and beverages in tropical climates', 'Moderate', 'Full sun', " +
                "'Well-draining, slightly acidic', 730, 1, 0, 'Year-round', 'Intermediate', " +
                "'https://example.com/calamondin.jpg', 0)");
        
        // Ornamental Flowers for Urban Gardens
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (3, 'Arabian Jasmine', 'Jasminum sambac', " +
                "'Jasmine variety with sweet fragrance and small white blooms, popular in tropical gardens', 'Moderate', 'Full sun to partial shade', " +
                "'Well-draining, fertile', 90, 1, 1, 'Year-round', 'Beginner', " +
                "'https://example.com/arabian_jasmine.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (4, 'Tropical Hibiscus', 'Hibiscus rosa-sinensis', " +
                "'Popular hibiscus variety with large, colorful blooms ideal for tropical urban gardens', 'Moderate', 'Full sun', " +
                "'Rich, well-draining', 60, 1, 1, 'Year-round', 'Beginner', " +
                "'https://example.com/tropical_hibiscus.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (5, 'Bougainvillea', 'Bougainvillea glabra', " +
                "'Drought-tolerant flowering vine with vibrant paper-like bracts, perfect for urban gardens', 'Low', 'Full sun', " +
                "'Well-draining', 120, 1, 0, 'Year-round', 'Beginner', " +
                "'https://example.com/bougainvillea.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (6, 'Jungle Flame', 'Ixora coccinea', " +
                "'Tropical garden shrub with clusters of star-shaped flowers in red, orange, or yellow', 'Moderate', 'Full sun to partial shade', " +
                "'Rich, slightly acidic', 75, 1, 1, 'Year-round', 'Beginner', " +
                "'https://example.com/jungle_flame.jpg', 0)");
        
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (7, 'Gardenia', 'Gardenia jasminoides', " +
                "'Fragrant white gardenia flowers with glossy green leaves, ideal for urban container gardens', 'Moderate', 'Partial shade', " +
                "'Acidic, well-draining', 120, 1, 1, 'Year-round', 'Intermediate', " +
                "'https://example.com/gardenia.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_PLANTS + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_SCIENTIFIC_NAME + ", " + 
                KEY_DESCRIPTION + ", " + KEY_WATERING_NEEDS + ", " + KEY_SUNLIGHT_REQUIREMENTS + ", " +
                KEY_SOIL_TYPE + ", " + KEY_GROWTH_DURATION + ", " + KEY_SUITABLE_CONTAINERS + ", " +
                KEY_SUITABLE_INDOOR + ", " + KEY_SEASON + ", " + KEY_DIFFICULTY_LEVEL + ", " + 
                KEY_IMAGE_URL + ", " + KEY_IS_BOOKMARKED + ") " +
                "VALUES (8, 'Marigold', 'Tagetes', " +
                "'Easy to grow flowers that thrive in warm climates and help repel garden pests', 'Moderate', 'Full sun', " +
                "'Well-draining', 45, 1, 1, 'Year-round', 'Beginner', " +
                "'https://example.com/marigold.jpg', 0)");
        
        // Urban Gardening Techniques for Small Spaces
        db.execSQL("INSERT INTO " + TABLE_GARDENING_TECHNIQUES + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_DESCRIPTION + ", " + 
                KEY_DIFFICULTY_LEVEL + ", " + KEY_SPACE_REQUIREMENT + ", " +
                KEY_IMAGE_URL + ", " + KEY_IS_FAVORITE + ") " +
                "VALUES (1, 'Container Gardening', " +
                "'Growing plants in containers like plastic buckets, old tires, or recycled containers - perfect for limited spaces in urban areas', " +
                "'Beginner', 'Minimal', " +
                "'https://example.com/container_gardening.jpg', 0)");
        
        db.execSQL("INSERT INTO " + TABLE_GARDENING_TECHNIQUES + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_DESCRIPTION + ", " + 
                KEY_DIFFICULTY_LEVEL + ", " + KEY_SPACE_REQUIREMENT + ", " +
                KEY_IMAGE_URL + ", " + KEY_IS_FAVORITE + ") " +
                "VALUES (2, 'Vertical Gardening', " +
                "'Growing plants upward on supports to maximize space in small urban areas using trellises or recycled materials', " +
                "'Intermediate', 'Minimal', " +
                "'https://example.com/vertical_gardening.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_GARDENING_TECHNIQUES + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_DESCRIPTION + ", " + 
                KEY_DIFFICULTY_LEVEL + ", " + KEY_SPACE_REQUIREMENT + ", " +
                KEY_IMAGE_URL + ", " + KEY_IS_FAVORITE + ") " +
                "VALUES (3, 'Urban Hydroponics', " +
                "'Soil-less cultivation system using water and nutrients, suitable for tropical climates and ideal for urban spaces', " +
                "'Advanced', 'Minimal', " +
                "'https://example.com/hydroponics.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_GARDENING_TECHNIQUES + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_DESCRIPTION + ", " + 
                KEY_DIFFICULTY_LEVEL + ", " + KEY_SPACE_REQUIREMENT + ", " +
                KEY_IMAGE_URL + ", " + KEY_IS_FAVORITE + ") " +
                "VALUES (4, 'Balcony Gardening', " +
                "'Maximizing balcony spaces in urban condos and apartments using hanging planters and strategic placement', " +
                "'Beginner', 'Small', " +
                "'https://example.com/balcony_gardening.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_GARDENING_TECHNIQUES + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_DESCRIPTION + ", " + 
                KEY_DIFFICULTY_LEVEL + ", " + KEY_SPACE_REQUIREMENT + ", " +
                KEY_IMAGE_URL + ", " + KEY_IS_FAVORITE + ") " +
                "VALUES (5, 'Bottle Gardening', " +
                "'Using recycled plastic bottles as planters, a sustainable approach for eco-friendly urban gardening', " +
                "'Beginner', 'Minimal', " +
                "'https://example.com/bottle_gardening.jpg', 0)");
                
        db.execSQL("INSERT INTO " + TABLE_GARDENING_TECHNIQUES + 
                " (" + KEY_ID + ", " + KEY_NAME + ", " + KEY_DESCRIPTION + ", " + 
                KEY_DIFFICULTY_LEVEL + ", " + KEY_SPACE_REQUIREMENT + ", " +
                KEY_IMAGE_URL + ", " + KEY_IS_FAVORITE + ") " +
                "VALUES (6, 'Rain Shelter Gardening', " +
                "'Protecting plants from heavy rain in tropical areas using simple DIY shelters while allowing proper sunlight', " +
                "'Intermediate', 'Variable', " +
                "'https://example.com/rain_shelter.jpg', 0)");
        
        // Sample Forum Posts
        db.execSQL("INSERT INTO " + TABLE_FORUM_POSTS + 
                " (" + KEY_ID + ", " + KEY_TITLE + ", " + KEY_CONTENT + ", " + 
                KEY_AUTHOR_ID + ", " + KEY_AUTHOR_NAME + ", " + KEY_TIMESTAMP + ", " +
                KEY_TAGS + ", " + KEY_LIKE_COUNT + ", " + KEY_COMMENT_COUNT + ", " +
                KEY_IS_LIKED + ") " +
                "VALUES (1, 'Tips for starting container gardening', " +
                "'I just started container gardening and wanted to share some tips that worked for me...', " +
                "'user1', 'John Gardener', " + System.currentTimeMillis() + ", " +
                "'tip', 5, 2, 0)");
    }
}
