package com.example.moodmaster.DB;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.moodmaster.mood_algo.Mood;

/**
 * RoomDB is an abstract class that extends RoomDatabase,
 * main access point for SQLite database.
 * The database class provides DAO methods for performing database operations.
 * It's annotated with @Database, lists the entities contained in the database, and the database version.
 */
@Database(entities = {Mood.class}, version = 1)
public abstract class RoomDB extends RoomDatabase {

    // Holds the instance of the Room database.
    private static RoomDB moodDBInstance;

    /**
     * Abstract method with no parameters or body.
     * Room will generate the implementation code for this method.
     *
     * @return The DAO object.
     */
    public abstract MoodDao moodDao();

    /**
     * Returns the singleton instance of the Room database.
     * If the database has not been created yet, it creates a new one.
     *
     * @param context The context used to access the application resources and services.
     * @return The singleton instance of the Room database.
     */
    public static RoomDB getInstance(Context context){
        if(RoomDB.moodDBInstance == null){
            synchronized(RoomDB.class){
                if(RoomDB.moodDBInstance == null){
                    RoomDB.moodDBInstance = Room.databaseBuilder(context, RoomDB.class, "mood_db").build();
                }
            }
        }
        return(RoomDB.moodDBInstance);
    }
}
