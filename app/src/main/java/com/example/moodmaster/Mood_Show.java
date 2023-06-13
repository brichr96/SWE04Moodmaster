package com.example.moodmaster;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Mood_Show extends AppCompatActivity {
    private TextView tvMoodTest;  // TextView for mood entries
    private MoodDao moodDao;  // DAO for database operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_show);

        tvMoodTest = findViewById(R.id.mood_test);

        // Instantiate your MoodDao
        RoomDB roomDb = RoomDB.getInstance(getApplicationContext());
        moodDao = roomDb.moodDao();

        // Fetch mood entries from the database and display them
        new GetAllMoodsAsyncTask().execute();
    }

    private class GetAllMoodsAsyncTask extends AsyncTask<Void, Void, List<Mood>> {
        @Override
        protected List<Mood> doInBackground(Void... voids) {
            return moodDao.getAllMoods();
        }

        @Override
        protected void onPostExecute(List<Mood> moods) {
            super.onPostExecute(moods);

            StringBuilder moodSb = new StringBuilder();
            for(Mood m : moods){
                moodSb.append(m.getMood()).append("\n");
            }
            tvMoodTest.setText(moodSb.toString());
        }
    }
}
