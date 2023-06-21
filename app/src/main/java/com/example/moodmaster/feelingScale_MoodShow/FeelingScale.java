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
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moodmaster.mood_algo.Mood;
import com.example.moodmaster.DB.MoodDao;
import com.example.moodmaster.DB.RoomDB;
import com.example.moodmaster.R;
import com.google.android.gms.common.util.JsonUtils;

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

        //temp sensor test

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        SensorEventListener lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float lux = sensorEvent.values[0];

                System.out.println("----------LUXFEELI " + lux);

                if(lux < 10){
//                    System.out.println("DUNKEL");
                }
                else{
//                    System.out.println("HELL");
                }
                //System.out.println("LUX: " + lux);
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //temp sensor test end

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
                Mood newMood = new Mood(5);
                new InsertMoodAsyncTask().execute(newMood);
//                new GetAllMoodsAsyncTask().execute();
                Intent intent = new Intent(FeelingScale.this, moods_tabbed.class );
                startActivity(intent);
            }
        });

        mButtonH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mood newMood = new Mood(4);
                new InsertMoodAsyncTask().execute(newMood);
                Intent intent = new Intent(FeelingScale.this, moods_tabbed.class );
                startActivity(intent);
            }
        });

        mButtonM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mood newMood = new Mood(3);
                new InsertMoodAsyncTask().execute(newMood);
                Intent intent = new Intent(FeelingScale.this, moods_tabbed.class );
                startActivity(intent);
            }
        });

        mButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mood newMood = new Mood(2);
                new InsertMoodAsyncTask().execute(newMood);
                Intent intent = new Intent(FeelingScale.this, moods_tabbed.class );
                startActivity(intent);
            }
        });

        mButtonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mood newMood = new Mood(1);
                new InsertMoodAsyncTask().execute(newMood);
                Intent intent = new Intent(FeelingScale.this, moods_tabbed.class );
                startActivity(intent);
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

            for(Mood m : moods){
              System.out.println("-----------------------------------MOOD: " + m.getMood());
            }
        }
    }
}
