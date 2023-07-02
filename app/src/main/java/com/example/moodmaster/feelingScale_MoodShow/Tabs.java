package com.example.moodmaster.feelingScale_MoodShow;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.moodmaster.R;
import com.example.moodmaster.feelingScale_MoodShow.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * Called when the activity is starting. This is where most initialization should occur.
 * Set the layout for the activity, and create a new SectionsPagerAdapter.
 * Then, set up a ViewPager with the sections adapter to handle page swiping.
 * Finally, link the ViewPager to a TabLayout to synchronize page selection with tab selection.
 *
 */

public class Tabs extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}


