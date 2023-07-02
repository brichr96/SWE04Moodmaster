package com.example.moodmaster.fragments;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.moodmaster.EmergencyCall;
import com.example.moodmaster.R;
import com.example.moodmaster.feelingScale_MoodShow.MapsActivity;
import com.example.moodmaster.feelingScale_MoodShow.StepCountService;

/**
 * This is a Fragment class that manages and displays a step counter for the user.
 * The number of steps is updated in real time, and the UI is updated accordingly.
 * The fragment has two key components:
 * 1. A TextView to display the step count
 * 2. A ProgressBar to visualize the user's progress towards a step goal
 */

public class StepCounterFragment extends Fragment {

    private TextView stepCountTextView;
    private ProgressBar progressBar;
    private int steps;

    private final String KEY = "steps";
    private static final int STEP_GOAL = 10000;

    public StepCounterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction("com.example.moodmaster.STEPS_UPDATE");
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_counter, container, false);

        stepCountTextView = rootView.findViewById(R.id.step_count_text);
        progressBar = rootView.findViewById(R.id.your_progress_bar_id);

        ImageButton emergencyCall = rootView.findViewById(R.id.emergencyButton);

        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmergencyCall.showEmergencyCallConfirmationDialog(getActivity(), getActivity().getApplicationContext());
            }
        });

        Button walk_button = rootView.findViewById(R.id.buttonWalk);
        walk_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isServiceRunning(StepCountService.class)) {
            Intent intent = new Intent(getContext(), StepCountService.class);
            getActivity().startService(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    /**
     * Checks whether a service of the specified class is currently running.
     *
     * @param serviceClass The class of the service to check.
     * @return {@code true} if a service of the specified class is running, {@code false} otherwise.
     */
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * BroadcastReceiver responsible for receiving and handling broadcast intents.
     * It listens for two types of actions: ACTION_BOOT_COMPLETED and "com.example.moodmaster.STEPS_UPDATE".
     * - When ACTION_BOOT_COMPLETED is received, it starts the StepCountService.
     * - When "com.example.moodmaster.STEPS_UPDATE" is received, it retrieves the updated steps count from the intent
     *   and updates the steps value. It then calls the loadSteps() method to update the UI with the new steps count.
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                Intent serviceIntent = new Intent(context, StepCountService.class);
                context.startService(serviceIntent);
            }
            else if (intent.getAction().equals("com.example.moodmaster.STEPS_UPDATE")) {
                steps = intent.getIntExtra("steps", 0);
                loadSteps();
            }
        }
    };

    /**
     * Loads and displays the steps count and progress.
     * It updates the step count TextView with the current steps value
     * and sets the progress of the progressBar based on the steps count.
     */
    private void loadSteps() {
        if (stepCountTextView != null) {
            stepCountTextView.setText(String.valueOf(steps));
        }
        if (progressBar != null) {
            int progress = (int) (((float) steps / STEP_GOAL) * 100);
            progressBar.setProgress(progress);
        }
    }
}
