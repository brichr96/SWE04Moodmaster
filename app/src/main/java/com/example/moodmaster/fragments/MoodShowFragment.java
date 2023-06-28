package com.example.moodmaster.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.moodmaster.DB.MoodDao;
import com.example.moodmaster.DB.RoomDB;
import com.example.moodmaster.EmergencyCall;
import com.example.moodmaster.R;
import com.example.moodmaster.mood_algo.Mood;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class MoodShowFragment extends Fragment {
    private LineChart chart;
    private MoodDao moodDao;

    private SharedPreferences algoValues;

    private final String KEY = "luxValue";
    private final String KEY2 = "steps";

    private int moodScore;

    private View colorCircle;
    private int colorStep;

    private TextView valueText;

    public MoodShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mood_show, container, false);

        ImageButton emergencyCall = view.findViewById(R.id.emergencyButton);

        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmergencyCall.showEmergencyCallConfirmationDialog(getActivity());
            }
        });

        chart = view.findViewById(R.id.chart);

        RoomDB roomDb = RoomDB.getInstance(getContext().getApplicationContext());
        moodDao = roomDb.moodDao();

        colorCircle = view.findViewById(R.id.colorCircle);

        valueText = view.findViewById(R.id.valueText);

        // Calculate color step based on the value (1 to 100)


        // Set the background color of the circle based on the color step

        new GetAllMoodsAsyncTask().execute();



        return view;
    }

    private void setColorCircleBackground(int value) {
        // Calculate the color based on the color step
        value = moodScore; // Replace with your value
        colorStep = value / 10;
        int color = Color.rgb(255 - (colorStep * 25), colorStep * 25, 0);

        // Set the background color of the circle
        colorCircle.setBackgroundColor(color);
    }

    private void updateValueText(int value) {
        // Update the text of the value textview
        String text = String.valueOf(value) + "%";

        valueText.setText(text);
    }

    private void generateLineChart(List<Mood> moods) {
        List<Entry> entries = new ArrayList<>();
        ArrayList<String> xLabels = new ArrayList<>();
        int moodIndex = 0;
        for (Mood mood : moods) {
            entries.add(new Entry(moodIndex, mood.getMood()));
            xLabels.add("" + (moodIndex + 1));
            moodIndex++;
        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.mood_over_time));
        dataSet.setDrawCircles(false);
        dataSet.setColor(Color.GREEN);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);

        if (chart != null) {
            chart.setData(lineData);
            chart.invalidate();

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(xLabels.size());
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int intValue = (int) value;
                    if (intValue < xLabels.size()) {
                        return xLabels.get(intValue);
                    }
                    return "";
                }
            });

            YAxis yAxisLeft = chart.getAxisLeft();
            yAxisLeft.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
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

            chart.getAxisRight().setEnabled(false);
            chart.getDescription().setEnabled(false);
        }
    }

    public int calculateOverallMoodScore(int[] moodValues, float light, int steps) {
        // Define weightage
        float maxLightValue = 10000f;
        float maxStepsCount = 10000f;
        float moodWeightage = 0.8f;
        float lightWeightage = 0.1f;
        float stepsWeightage = 0.1f;

        // Calculate average mood value
        float sumMood = 0;
        for (int i = 0; i < moodValues.length; i++) {
            sumMood += moodValues[i];
            System.out.println("mood " + moodValues[i]);
        }
        float averageMood = sumMood / moodValues.length;
        System.out.println("avg mood " + averageMood);

        // Normalize average mood value
        float normalizedMood = (averageMood - 1) / 4;
        System.out.println("norm mood " + normalizedMood);

        // Normalize other inputs
        float normalizedLight = light / maxLightValue;
        System.out.println("light " + light);
        System.out.println("norm light " + normalizedLight);
        float normalizedSteps = (float) steps / maxStepsCount;
        System.out.println("steps " + steps);
        System.out.println("norm steps " + normalizedSteps);

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
            }
        } else {
            if(moods.size() > 0) {
                List<Mood> lastSevenMoods = moods.subList(0, moods.size());
                lastSevenMoodsValues = new int[moods.size()]; // Initialize with default values of 0
                for (int i = 0; i < lastSevenMoods.size(); i++) {
                    lastSevenMoodsValues[i] = lastSevenMoods.get(i).getMood();
                }
            }
            else{
                System.out.println("Moods empty, should not happen.");
                return null;
            }
        }

        return lastSevenMoodsValues;
    }

    private float getLux() {
        return algoValues.getFloat(KEY, 0);
    }

    private int getSteps() {
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
//            updateValueText(moodScore);
//            setColorCircleBackground();
            moodScore = calculateOverallMoodScore(getLastSevenMoodValues(moods), getLux(), getSteps());
            updateValueText(moodScore);
            setColorCircleBackground(moodScore);
            System.out.println("MOOD SCORE----------" + moodScore);
        }
    }
}
