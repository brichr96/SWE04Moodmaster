package com.example.moodmaster.feelingScale_MoodShow.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moodmaster.databinding.FragmentStepCounterBinding;

/**
 * PlaceholderFragment is a class that extends Fragment,
 * serving as a host for the View and manages lifecycle of the corresponding PageViewModel.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentStepCounterBinding binding;

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param index The section number this fragment represents.
     * @return A new instance of PlaceholderFragment.
     */
    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    /**
     * The system calls this when it's time for the fragment to draw its UI for the first time.
     * To draw a UI for the fragment, a View component must be returned from this method which is the root of the fragment's layout.
     * It can return null if the fragment does not provide a UI.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container This is the parent view that the fragment's UI is attached to.
     * @param savedInstanceState This fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentStepCounterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.stepCountText;
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
