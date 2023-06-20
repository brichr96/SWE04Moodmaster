package com.example.moodmaster.feelingScale_MoodShow;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moodmaster.DB.MoodDao;
import com.example.moodmaster.R;
import com.github.mikephil.charting.charts.LineChart;

public class Mood_Show extends AppCompatActivity {
    private LineChart chart;
    private MoodDao moodDao;
    private Button login;

    private TextView tvAverageMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_show);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}


