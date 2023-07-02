package com.example.moodmaster.feelingScale_MoodShow.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * PageViewModel is a class that extends ViewModel,
 * reparation of the data needed for an Activity or a Fragment.
 */
public class PageViewModel extends ViewModel {

    // Mutable LiveData holding the index.
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    // LiveData holding the transformed text.
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}
