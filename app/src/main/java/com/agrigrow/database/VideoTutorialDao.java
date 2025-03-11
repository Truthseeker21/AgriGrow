package com.agrigrow.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.agrigrow.model.VideoTutorial;

import java.util.List;

/**
 * Data Access Object for Video Tutorials
 */
@Dao
public interface VideoTutorialDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertVideo(VideoTutorial videoTutorial);
    
    @Update
    void updateVideo(VideoTutorial videoTutorial);
    
    @Delete
    void deleteVideo(VideoTutorial videoTutorial);
    
    @Query("SELECT * FROM video_tutorials WHERE id = :videoId")
    LiveData<VideoTutorial> getVideoById(long videoId);
    
    @Query("SELECT * FROM video_tutorials ORDER BY publishedDate DESC")
    LiveData<List<VideoTutorial>> getAllVideos();
    
    @Query("SELECT * FROM video_tutorials WHERE isFavorite = 1 ORDER BY publishedDate DESC")
    LiveData<List<VideoTutorial>> getFavoriteVideos();
    
    @Query("SELECT * FROM video_tutorials WHERE isWatched = 1 ORDER BY publishedDate DESC")
    LiveData<List<VideoTutorial>> getWatchedVideos();
    
    @Query("SELECT * FROM video_tutorials WHERE category = :category ORDER BY publishedDate DESC")
    LiveData<List<VideoTutorial>> getVideosByCategory(String category);
    
    @Query("SELECT * FROM video_tutorials WHERE difficulty = :difficulty ORDER BY publishedDate DESC")
    LiveData<List<VideoTutorial>> getVideosByDifficulty(String difficulty);
    
    @Query("SELECT * FROM video_tutorials WHERE author = :author ORDER BY publishedDate DESC")
    LiveData<List<VideoTutorial>> getVideosByAuthor(String author);
    
    @Query("SELECT * FROM video_tutorials WHERE tags LIKE '%' || :tag || '%' ORDER BY publishedDate DESC")
    LiveData<List<VideoTutorial>> getVideosByTag(String tag);
    
    @Query("SELECT * FROM video_tutorials ORDER BY viewCount DESC LIMIT 5")
    LiveData<List<VideoTutorial>> getPopularVideos();
    
    @Query("SELECT * FROM video_tutorials ORDER BY publishedDate DESC LIMIT 5")
    LiveData<List<VideoTutorial>> getRecentVideos();
    
    @Query("UPDATE video_tutorials SET isWatched = :isWatched WHERE id = :videoId")
    void updateWatchedStatus(long videoId, boolean isWatched);
    
    @Query("UPDATE video_tutorials SET isFavorite = :isFavorite WHERE id = :videoId")
    void updateFavoriteStatus(long videoId, boolean isFavorite);
    
    @Query("UPDATE video_tutorials SET viewCount = viewCount + 1 WHERE id = :videoId")
    void incrementViewCount(long videoId);
    
    @Query("UPDATE video_tutorials SET likesCount = likesCount + 1 WHERE id = :videoId")
    void incrementLikesCount(long videoId);
    
    @Query("SELECT * FROM video_tutorials WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY publishedDate DESC")
    LiveData<List<VideoTutorial>> searchVideos(String query);
    
    @Query("SELECT COUNT(*) FROM video_tutorials WHERE isWatched = 1")
    LiveData<Integer> getWatchedCount();
    
    @Query("SELECT COUNT(*) FROM video_tutorials")
    LiveData<Integer> getTotalVideoCount();
    
    @Query("SELECT * FROM video_tutorials WHERE difficulty = :difficulty AND isWatched = 0 ORDER BY publishedDate DESC")
    LiveData<List<VideoTutorial>> getUnwatchedVideosByDifficulty(String difficulty);
}