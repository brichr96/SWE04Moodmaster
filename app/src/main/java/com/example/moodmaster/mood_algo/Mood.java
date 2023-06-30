package com.example.moodmaster.mood_algo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "moods")
public class Mood {
    @PrimaryKey(autoGenerate = true)
    private int moodID;

    private int mood;

    public int getMoodID() {
        return moodID;
    }

    public int getMood() {
        return mood;
    }

    public void setMoodID(int moodID) {
        this.moodID = moodID;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public Mood(int mood){
        this.mood = mood;
    }
}
