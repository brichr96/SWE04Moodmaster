package com.example.moodmaster.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.moodmaster.DB.MoodDao;
import com.example.moodmaster.DB.RoomDB;
import com.example.moodmaster.R;
import com.example.moodmaster.mood_algo.Mood;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MoodTabbedFragment extends Fragment {
    private LineChart chart;
    private MoodDao moodDao;

    private SharedPreferences algoValues;

    private final String KEY = "luxValue";

    private final String KEY2 = "steps";

    private int moodScore;

    private View colorCircle;
    private int colorStep;

    public MoodTabbedFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mood_show, container, false);

        chart = view.findViewById(R.id.chart);

        RoomDB roomDb = RoomDB.getInstance(getContext().getApplicationContext());
        moodDao = roomDb.moodDao();

        colorCircle = view.findViewById(R.id.colorCircle);

        // Calculate color step based on the value (1 to 100)
        int value = moodScore; // Replace with your value
        colorStep = value / 10;

        // Set the background color of the circle based on the color step


        new GetAllMoodsAsyncTask().execute();

        return view;
    }

    private void setColorCircleBackground() {
        // Calculate the color based on the color step
        int color = Color.rgb(255 - (colorStep * 25), colorStep * 25, 0);

        // Set the background color of the circle
        colorCircle.setBackgroundColor(color);
    }

    private void generateLineChart(List<Mood> moods) {
        List<Entry> entries = new ArrayList<>();
        int moodIndex = 0;
        for (Mood mood : moods) {
            entries.add(new Entry(moodIndex, mood.getMood()));
            moodIndex++;
//            System.out.println("-----------------------------------MOOD: " + mood.getMood());
        }

        LineDataSet dataSet = new LineDataSet(entries, "Mood over time");
        dataSet.setDrawCircles(false);
        dataSet.setColor(Color.GREEN);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);

        if (chart != null) {
            chart.setData(lineData);
            chart.invalidate();

            chart.getAxisLeft().setAxisMaximum(5);
            chart.getAxisLeft().setAxisMinimum(1);
            chart.getAxisLeft().setGranularity(1f);
            chart.getAxisRight().setEnabled(false);
            chart.getDescription().setEnabled(false);

            // Set custom Y-Axis labels
            chart.getAxisLeft().setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int intValue = (int) value;
                    switch (intValue) {
                        case 1:
                            return "\uD83D\uDE1E";
                        case 2:
                            return "\uD83D\uDE14";
                        case 3:
                            return "\uD83D\uDE10";
                        case 4:
                            return "\uD83D\uDE00";
                        case 5:
                            return "\uD83D\uDE01";
                        default:
                            return "";
                    }
                }
            });

            chart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.format(Locale.getDefault(), "Day %d", (int) value + 1);
                }
            });
        }
    }

    public void calcMood(){
//        try {
//            LightTabFragment light = new LightTabFragment();
//            float lux = light.getLux();
//
//            System.out.println("-----------------LUCHS " + lux);
//        } catch (Exception e) {
////            throw new RuntimeException(e);
//        }

        System.out.println("CALC");

        try {
            float lux = getLux();
            int steps = getSteps();
            System.out.println("----------LUCHSTEPS  " + lux + " - " + steps);
        } catch (Exception e) {

        }

    }

    public int calculateOverallMoodScore(int[] moodValues, float light, int steps) {
        // Define weightage

        float maxLightValue = 150f;
        float maxStepsCount = 10000f;
        float moodWeightage = 0.8f;
        float lightWeightage = 0.1f;
        float stepsWeightage = 0.1f;

        // Calculate average mood value
        float sumMood = 0;
        for (int i = 0; i < moodValues.length; i++) {
            sumMood += moodValues[i];
        }
        float averageMood = sumMood / moodValues.length;

        // Normalize average mood value
        float normalizedMood = (averageMood - 1) / 4;

        // Normalize other inputs
        float normalizedLight = light / maxLightValue;
        float normalizedSteps = (float) steps / maxStepsCount;

        // Calculate weighted sum
        float weightedMood = normalizedMood * moodWeightage;
        float weightedLight = normalizedLight * lightWeightage;
        float weightedSteps = normalizedSteps * stepsWeightage;

        // Calculate overall mood score
        float overallMoodScore = (weightedMood + weightedLight + weightedSteps) * 100;

        // Round the overall mood score
        int roundedMoodScore = Math.round(overallMoodScore);

        return roundedMoodScore;
    }


    public int[] getLastSevenMoodValues(List<Mood> moods) {
        int[] lastSevenMoodsValues;

        // Ensure that the moodList has at least 7 entries
        if (moods.size() >= 7) {
            // Get the sublist containing the last 7 entries
            List<Mood> lastSevenMoods = moods.subList(moods.size() - 7, moods.size());

            // Create a new int array with size 7
            lastSevenMoodsValues = new int[7];

            // Copy the elements from the sublist to the int array
            for (int i = 0; i < lastSevenMoods.size(); i++) {
                lastSevenMoodsValues[i] = lastSevenMoods.get(i).getMood();
//                System.out.println("MOOD: ------------ " + lastSevenMoodsValues[i]);
            }

//            for (Mood mood : lastSevenMoods) {
//                mood.getMood()
//            }


        } else {
            // Handle the case when moodList does not have enough entries
            // For example, you could throw an exception, return null, or initialize the array with default values
            lastSevenMoodsValues = new int[7]; // Initialize with default values of 0
        }

        return lastSevenMoodsValues;
    }


    private float getLux(){
        return algoValues.getFloat(KEY, 0);
    }

    private int getSteps(){
        return algoValues.getInt(KEY2, 0);
    }

    private class GetAllMoodsAsyncTask extends AsyncTask<Void, Void, List<Mood>> {
        @Override
        protected List<Mood> doInBackground(Void... voids) {
            return moodDao.getAllMoods();
        }

        @Override
        protected void onPostExecute(List<Mood> moods) {
            super.onPostExecute(moods);
            algoValues = getContext().getSharedPreferences("algo", Context.MODE_PRIVATE);
            generateLineChart(moods);
            getLastSevenMoodValues(moods);
            setColorCircleBackground();
            moodScore = calculateOverallMoodScore(getLastSevenMoodValues(moods), getLux(), getSteps());
            System.out.println("MOOD SCORE----------" + moodScore);


        }
    }
}
