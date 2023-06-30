package com.example.moodmaster.DB;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.moodmaster.mood_algo.Mood;

@Database(entities = {Mood.class}, version = 1)
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
