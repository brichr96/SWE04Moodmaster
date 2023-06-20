package com.example.moodmaster.feelingScale_MoodShow;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.moodmaster.DB.MoodDao;
import com.example.moodmaster.DB.RoomDB;
import com.example.moodmaster.R;
import com.example.moodmaster.mood_algo.Mood;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.moodmaster.feelingScale_MoodShow.ui.main.SectionsPagerAdapter;
import com.example.moodmaster.databinding.ActivityMoodsTabbedBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class moods_tabbed extends AppCompatActivity {

    private ActivityMoodsTabbedBinding binding;
    private MoodDao moodDao;

    private LineChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moods_tabbed);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }}


