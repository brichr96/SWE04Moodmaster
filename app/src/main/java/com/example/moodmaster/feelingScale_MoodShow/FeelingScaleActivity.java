package com.example.moodmaster.feelingScale_MoodShow;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moodmaster.EmergencyCall;
import com.example.moodmaster.SettingsActivity;
import com.example.moodmaster.mood_algo.Mood;
import com.example.moodmaster.DB.MoodDao;
import com.example.moodmaster.DB.RoomDB;
import com.example.moodmaster.R;
import java.util.List;

public class FeelingScaleActivity extends AppCompatActivity {
    private MoodDao moodDao;
    private ImageView moodImageView;
    private int[] moods;
    private int currentMood;
    private GestureDetector gestureDetector;
    private boolean moodSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_scale);

        Button btnNext  = findViewById(R.id.btnnext);
        ImageButton emergencyCall = findViewById(R.id.emergencyButton);
        ImageButton settingsBtn = findViewById(R.id.settingsButton);

        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmergencyCall.showEmergencyCallConfirmationDialog(FeelingScaleActivity.this, getApplicationContext());
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeelingScaleActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

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
                if (!moodSelected) {
                    Mood newMood = new Mood(5);
                    new InsertMoodAsyncTask().execute(newMood);
                    startNewActivity(5);
                    moodSelected = true;
                }
            }
        });

        mButtonH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!moodSelected) {
                    Mood newMood = new Mood(4);
                    new InsertMoodAsyncTask().execute(newMood);
                    startNewActivity(4);
                    moodSelected = true;
                }
            }
        });

        mButtonM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!moodSelected) {
                    Mood newMood = new Mood(3);
                    new InsertMoodAsyncTask().execute(newMood);
                    startNewActivity(3);
                    moodSelected = true;
                }
            }
        });

        mButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!moodSelected) {
                    Mood newMood = new Mood(2);
                    new InsertMoodAsyncTask().execute(newMood);
                    startNewActivity(2);
                    moodSelected = true;
                }
            }
        });

        mButtonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!moodSelected) {
                    Mood newMood = new Mood(1);
                    new InsertMoodAsyncTask().execute(newMood);
                    startNewActivity(1);
                    moodSelected = true;
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetAllMoodsAsyncTask(){
                    @Override
                    protected void onPostExecute(List<Mood> moods_check){
                        if(moods_check.isEmpty()){
                            Toast.makeText(FeelingScaleActivity.this, R.string.choose_mood, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent = new Intent(FeelingScaleActivity.this, Tabs.class);
                            startActivity(intent);
                        }
                    }
                }.execute();
            }
        });

        moods = new int[]{
                R.drawable.ic_smiley_super_happy,
                R.drawable.ic_smiley_happy,
                R.drawable.ic_smiley_normal,
                R.drawable.ic_smiley_sad,
                R.drawable.ic_smiley_disappointed
        };

        currentMood = 0;
        moodImageView.setImageResource(moods[currentMood]);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())) {
                    if (e2.getX() > e1.getX()) {
                        currentMood = (currentMood + 1) % moods.length;
                    }
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

                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            if (deltaX > 0) {
                                currentMood = (currentMood + 1) % moods.length;
                            }
                            else {
                                currentMood = (currentMood - 1 + moods.length) % moods.length;
                            }

                            moodImageView.setImageResource(moods[currentMood]);
                        } else {
                            if (!moodSelected) {
                                startNewActivity(currentMood + 1);
                                moodSelected = true;
                            }
                        }

                        return true;
                }

                return false;
            }
        });
    }

    private void startNewActivity(int mood) {
        Intent intent = new Intent(FeelingScaleActivity.this, Tabs.class);
        intent.putExtra("mood", mood);
        startActivity(intent);
    }

    private class InsertMoodAsyncTask extends AsyncTask<Mood, Void, Void> {
        @Override
        protected Void doInBackground(Mood... moods) {
            moodDao.insert(moods[0]);
            return null;
        }
    }

    private class GetAllMoodsAsyncTask extends AsyncTask<Void, Void, List<Mood>> {
        @Override
        protected List<Mood> doInBackground(Void... voids) {
            return moodDao.getAllMoods();
        }
    }
}
