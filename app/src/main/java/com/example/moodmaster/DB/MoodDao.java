package com.example.moodmaster.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.moodmaster.mood_algo.Mood;
import java.util.List;

/**
 * MoodDao is an interface that serves as the DAO (Data Access Object) in the Room persistence library.
 * It defines methods to manipulate `Mood` objects in the database, such as insert, delete and query all moods.
 */
@Dao
public interface MoodDao {

    /**
     * Inserts a new mood into the database.
     *
     * @param mood the mood object to be inserted.
     */
    @Insert
    void insert(Mood mood);

    /**
     * Deletes a specific mood from the database.
     *
     * @param mood the mood object to be deleted.
     */
    @Delete
    void delete(Mood mood);

    /**
     * Retrieves all moods from the database.
     *
     * @return a list of all moods.
     */
    @Query("SELECT * FROM moods")
    List<Mood> getAllMoods();
}
