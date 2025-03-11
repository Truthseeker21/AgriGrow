package com.agrigrow.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.agrigrow.model.JournalEntry;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object for gardening journal entries
 */
@Dao
public interface JournalEntryDao {
    
    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    List<JournalEntry> getAllEntries();
    
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    JournalEntry getEntryById(int id);
    
    @Query("SELECT * FROM journal_entries WHERE plant_id = :plantId ORDER BY date DESC")
    List<JournalEntry> getEntriesByPlantId(int plantId);
    
    @Query("SELECT * FROM journal_entries WHERE entry_type = :entryType ORDER BY date DESC")
    List<JournalEntry> getEntriesByType(String entryType);
    
    @Query("SELECT * FROM journal_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    List<JournalEntry> getEntriesByDateRange(Date startDate, Date endDate);
    
    @Query("SELECT * FROM journal_entries WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    List<JournalEntry> searchEntries(String searchQuery);
    
    @Insert
    long insertEntry(JournalEntry entry);
    
    @Update
    void updateEntry(JournalEntry entry);
    
    @Delete
    void deleteEntry(JournalEntry entry);
    
    @Query("DELETE FROM journal_entries WHERE id = :id")
    void deleteEntryById(int id);
    
    @Query("SELECT COUNT(*) FROM journal_entries")
    int getEntryCount();
    
    @Query("SELECT COUNT(*) FROM journal_entries WHERE entry_type = :entryType")
    int getEntryCountByType(String entryType);
    
    @Query("SELECT * FROM journal_entries WHERE reminder_date IS NOT NULL AND reminder_date <= :currentDate")
    List<JournalEntry> getEntriesWithDueReminders(Date currentDate);
}