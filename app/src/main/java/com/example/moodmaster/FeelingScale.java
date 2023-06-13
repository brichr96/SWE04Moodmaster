package com.example.moodmaster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class FeelingScale extends AppCompatActivity {
    private MoodDao moodDao;
    private ImageView moodImageView;
    private int[] moods;
    private int currentMood;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feelingscale);

        // Instantiate your DBHandler
        RoomDB roomDb = RoomDB.getInstance(getApplicationContext());
        moodDao = roomDb.moodDao();

        moodImageView = findViewById(R.id.moodImageView);
        Button mButton = findViewById(R.id.veryHappy);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moodClick();
            }
        });

        // List of moods (replace with your actual drawable resources)
        moods = new int[] {
                R.drawable.ic_smiley_happy,
                R.drawable.ic_smiley_super_happy,
                R.drawable.ic_smiley_sad,
                R.drawable.ic_smiley_disappointed,
        };

        currentMood = 0; // Start with the first mood
        moodImageView.setImageResource(moods[currentMood]);

        // Create a gesture detector
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // Detect horizontal swipes
                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())) {
                    // Swipe to the right (next mood)
                    if (e2.getX() > e1.getX()) {
                        currentMood = (currentMood + 1) % moods.length;
                    }
                    // Swipe to the left (previous mood)
                    else {
                        currentMood = (currentMood - 1 + moods.length) % moods.length;
                    }

                    moodImageView.setImageResource(moods[currentMood]);

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void moodClick() {
        // Create a new Mood object with the current mood value
        Mood newMood = new Mood(1);

        // Insert the new mood into the database
        new InsertMoodAsyncTask().execute(newMood);

        Intent intent = new Intent(FeelingScale.this, Mood_Show.class );
        startActivity(intent);
    }

    private class InsertMoodAsyncTask extends AsyncTask<Mood, Void, Void> {
        @Override
        protected Void doInBackground(Mood... moods) {
            moodDao.insert(moods[0]);
            return null;
        }
    }

    public class GetAllMoodsAsyncTask extends AsyncTask<Void, Void, List<Mood>>{
        @Override
        protected List<Mood> doInBackground(Void... voids){
            return moodDao.getAllMoods();
        }

        @Override
        protected void onPostExecute(List<Mood> moods){
            super.onPostExecute(moods);

            //for(Mood m : moods){
            //  System.out.println("-----------------------------------MOOD: " + m.getMood()
        }
    }
}
