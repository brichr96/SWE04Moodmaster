package com.example.moodmaster.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.moodmaster.mood_algo.Mood;

import java.util.List;

@Dao
public interface MoodDao {
    @Insert
    void insert(Mood mood);

    @Delete
    void delete(Mood mood);

    @Query("SELECT * FROM moods")
    List<Mood> getAllMoods();
}
