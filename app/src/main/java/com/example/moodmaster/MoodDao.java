package com.example.moodmaster;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.sql.Date;
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
