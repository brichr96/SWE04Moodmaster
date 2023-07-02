package com.example.moodmaster.feelingScale_MoodShow.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.moodmaster.R;
import com.example.moodmaster.fragments.LightFragment;
import com.example.moodmaster.fragments.StepCounterFragment;
import com.example.moodmaster.fragments.MoodShowFragment;



/**
 * A FragmentPagerAdapter that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    /**
     * Constructor for SectionsPagerAdapter. Initializes the context and the FragmentManager.
     *
     * @param context The context used to access the application resources and services.
     * @param fm      The FragmentManager for interacting with fragment objects inside of this class.
     */
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Returns the Fragment associated with a specified position.
     *
     * @param position The position of the item in the adapter.
     * @return The Fragment associated with a specified position.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MoodShowFragment();
            case 1:
                return new StepCounterFragment();
            case 2:
                return new LightFragment();
            default:
                return null;
        }
    }

    /**
     * Returns the page title for the top indicator.
     *
     * @param position The position of the title requested.
     * @return A CharSequence containing the page's title.
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }


    @Override
    public int getCount() {
        return 3;
    }
}
