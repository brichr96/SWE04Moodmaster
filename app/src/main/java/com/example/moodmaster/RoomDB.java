package com.example.moodmaster;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Mood.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB moodDBInstance;

    public abstract MoodDao moodDao();

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
