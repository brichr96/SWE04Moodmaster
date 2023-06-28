package com.example.moodmaster.feelingScale_MoodShow;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moodmaster.EmergencyCall;
import com.example.moodmaster.mood_algo.Mood;
import com.example.moodmaster.DB.MoodDao;
import com.example.moodmaster.DB.RoomDB;
import com.example.moodmaster.R;

import java.util.List;

/**
 * This activity displays a feeling scale and allows the user to select their current mood.
 * The user can swipe left or right on the mood image to change the selected mood.
 * Tapping on a mood image starts a new activity.
 */
public class FeelingScale extends AppCompatActivity {
    private MoodDao moodDao;
    private ImageView moodImageView;
    private int[] moods;
    private int currentMood;
    private GestureDetector gestureDetector;

    private boolean moodSelected;

    /**
     * Performs initialization of the activity.
     *
     * @param savedInstanceState The saved instance state Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feelingscale);

        // Temp sensor test
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Button btnNext  = findViewById(R.id.btnnext);
        ImageButton emergencyCall = findViewById(R.id.emergencyButton);

        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmergencyCall.showEmergencyCallConfirmationDialog(FeelingScale.this);
            }
        });

        // Instantiate your DBHandler
        RoomDB roomDb = RoomDB.getInstance(getApplicationContext());
        moodDao = roomDb.moodDao();

        moodImageView = findViewById(R.id.moodImageView);

        ImageButton mButtonVH = findViewById(R.id.veryHappy);
        ImageButton mButtonH = findViewById(R.id.happy);
        ImageButton mButtonM = findViewById(R.id.medium);
        ImageButton mButtonD = findViewById(R.id.disappointed);
        ImageButton mButtonS = findViewById(R.id.sad);


        mButtonVH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!moodSelected) { // Check if a mood has already been selected
                    Mood newMood = new Mood(5);
                    new InsertMoodAsyncTask().execute(newMood);
                    startNewActivity(5);
                    moodSelected = true; // Set the flag to true after selecting a mood
                }
            }
        });

        mButtonH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!moodSelected) { // Check if a mood has already been selected
                    Mood newMood = new Mood(4);
                    new InsertMoodAsyncTask().execute(newMood);
                    startNewActivity(4);
                    moodSelected = true; // Set the flag to true after selecting a mood
                }
            }
        });

        mButtonM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!moodSelected) { // Check if a mood has already been selected
                    Mood newMood = new Mood(3);
                    new InsertMoodAsyncTask().execute(newMood);
                    startNewActivity(3);
                    moodSelected = true; // Set the flag to true after selecting a mood
                }
            }
        });

        mButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!moodSelected) { // Check if a mood has already been selected
                    Mood newMood = new Mood(2);
                    new InsertMoodAsyncTask().execute(newMood);
                    startNewActivity(2);
                    moodSelected = true; // Set the flag to true after selecting a mood
                }
            }
        });

        mButtonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!moodSelected) { // Check if a mood has already been selected
                    Mood newMood = new Mood(1);
                    new InsertMoodAsyncTask().execute(newMood);
                    startNewActivity(1);
                    moodSelected = true; // Set the flag to true after selecting a mood
                }
            }

        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////////////////////TODO//////////////////////
                //check if no moods -> dialog, alert, toast
                //not possible

                Intent intent = new Intent(FeelingScale.this, moods_tabbed.class);
                startActivity(intent);
            }
        });

        // List of moods (replace with your actual drawable resources)
        moods = new int[]{
                R.drawable.ic_smiley_super_happy,
                R.drawable.ic_smiley_happy,
                R.drawable.ic_smiley_normal,
                R.drawable.ic_smiley_sad,
                R.drawable.ic_smiley_disappointed
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

        moodImageView.setOnTouchListener(new View.OnTouchListener() {
            private float startX;
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        float endY = event.getY();

                        float deltaX = endX - startX;
                        float deltaY = endY - startY;

                        // Check if the motion was a swipe (not a tap)
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            // Swipe to the right
                            if (deltaX > 0) {
                                currentMood = (currentMood + 1) % moods.length;
                            }
                            // Swipe to the left
                            else {
                                currentMood = (currentMood - 1 + moods.length) % moods.length;
                            }

                            moodImageView.setImageResource(moods[currentMood]);
                        } else {
                            // Perform the action when the image view is touched
                            if (!moodSelected) { // Check if a mood has already been selected
                                startNewActivity(currentMood + 1);
                                moodSelected = true; // Set the flag to true after selecting a mood
                            }
                        }

                        return true;
                }

                return false;
            }
        });
    }




    /**
     * Starts a new activity based on the selected mood.
     *
     * @param mood The mood selected by the user.
     */
    private void startNewActivity(int mood) {
        Intent intent = new Intent(FeelingScale.this, moods_tabbed.class);
        intent.putExtra("mood", mood);
        startActivity(intent);
    }

    /**
     * Inserts a new mood into the database asynchronously.
     */
    private class InsertMoodAsyncTask extends AsyncTask<Mood, Void, Void> {
        @Override
        protected Void doInBackground(Mood... moods) {
            moodDao.insert(moods[0]);
            return null;
        }
    }
}
