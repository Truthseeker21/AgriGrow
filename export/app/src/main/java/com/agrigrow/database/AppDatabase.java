package com.agrigrow.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.agrigrow.model.Badge;
import com.agrigrow.model.ForumPost;
import com.agrigrow.model.GardeningChallenge;
import com.agrigrow.model.GardeningGroup;
import com.agrigrow.model.GardeningTechnique;
import com.agrigrow.model.GardenTip;
import com.agrigrow.model.JournalEntry;
import com.agrigrow.model.Plant;
import com.agrigrow.model.SoilRecommendation;
import com.agrigrow.model.SoilTest;
import com.agrigrow.model.User;
import com.agrigrow.model.UserReward;
import com.agrigrow.model.VideoTutorial;
import com.agrigrow.util.DateConverter;

/**
 * Room database for the app
 */
@Database(entities = {
        Plant.class, 
        GardeningTechnique.class, 
        GardenTip.class, 
        ForumPost.class, 
        User.class, 
        JournalEntry.class,
        SoilTest.class,
        SoilRecommendation.class,
        GardeningGroup.class,
        VideoTutorial.class,
        GardeningChallenge.class,
        Badge.class,
        UserReward.class
}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "agrigrow_db";
    
    private static volatile AppDatabase INSTANCE;
    
    // DAOs
    public abstract PlantDao plantDao();
    public abstract GardeningTechniqueDao gardeningTechniqueDao();
    public abstract ForumPostDao forumPostDao();
    public abstract JournalEntryDao journalEntryDao();
    public abstract SoilTestDao soilTestDao();
    public abstract GardeningGroupDao gardeningGroupDao();
    public abstract VideoTutorialDao videoTutorialDao();
    public abstract GardeningChallengeDao gardeningChallengeDao();
    public abstract BadgeDao badgeDao();
    public abstract UserRewardDao userRewardDao();
    
    /**
     * Get database instance (singleton pattern)
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}