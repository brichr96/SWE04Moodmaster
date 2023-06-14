package com.example.moodmaster.feelingScale_MoodShow;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moodmaster.mood_algo.Mood;
import com.example.moodmaster.DB.MoodDao;
import com.example.moodmaster.R;
import com.example.moodmaster.DB.RoomDB;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Mood_Show extends AppCompatActivity {
    private LineChart chart;
    private MoodDao moodDao;

    private TextView tvAverageMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_show);

        RoomDB roomDb = RoomDB.getInstance(getApplicationContext());
        moodDao = roomDb.moodDao();

        chart = findViewById(R.id.chart);

        new GetAllMoodsAsyncTask().execute();
    }

    private void generateLineChart(List<Mood> moods) {
        List<Entry> entries = new ArrayList<>();

        int moodIndex = 0;
        for (Mood mood : moods) {
            entries.add(new Entry(moodIndex, mood.getMood()));
            moodIndex++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Mood over time");
        dataSet.setDrawCircles(false);
        dataSet.setColor(Color.GREEN);
        dataSet.setDrawValues(false);





        LineData lineData = new LineData(dataSet);
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

    private class GetAllMoodsAsyncTask extends AsyncTask<Void, Void, List<Mood>> {
        @Override
        protected List<Mood> doInBackground(Void... voids) {
            return moodDao.getAllMoods();
        }

        @Override
        protected void onPostExecute(List<Mood> moods) {
            super.onPostExecute(moods);
            generateLineChart(moods);
        }
    }
}
