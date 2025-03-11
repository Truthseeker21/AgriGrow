package com.agrigrow.util;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Type converter for Room database to convert between Date objects and Long timestamps
 */
public class DateConverter {
    
    /**
     * Convert Date to Long timestamp for storage in database
     * @param date Date object
     * @return Long timestamp (milliseconds since epoch)
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
    
    /**
     * Convert Long timestamp to Date for use in app
     * @param timestamp Long timestamp (milliseconds since epoch)
     * @return Date object
     */
    @TypeConverter
    public static Date timestampToDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}