package com.example.moodmaster;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "moods")
@TypeConverters({Converters.class})
public class Mood {
    @PrimaryKey(autoGenerate = true)
    private int moodID;

    private int mood;
   // public Date date;

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

   /* public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

*/

    public Mood(int mood){
        this.mood = mood;
    }
}
