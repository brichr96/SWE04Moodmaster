package com.example.moodmaster.feelingScale_MoodShow;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.moodmaster.DB.MoodDao;
import com.example.moodmaster.R;
import com.example.moodmaster.databinding.ActivityMoodsTabbedBinding;
import com.example.moodmaster.feelingScale_MoodShow.ui.main.SectionsPagerAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.tabs.TabLayout;

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


