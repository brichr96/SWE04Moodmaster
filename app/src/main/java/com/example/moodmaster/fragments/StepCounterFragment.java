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

public class StepCounterFragment extends Fragment {

    private TextView stepCountTextView;
    private ProgressBar progressBar;
    private int steps;
    private final String KEY = "steps";
    private static final int STEP_GOAL = 10000;

    public StepCounterFragment() {
        // Required empty public constructor
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
                EmergencyCall.showEmergencyCallConfirmationDialog(getActivity());
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

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Receiving the broadcast when the device finishes rebooting
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                // Start the StepCountService again after reboot
                Intent serviceIntent = new Intent(context, StepCountService.class);
                context.startService(serviceIntent);
            } else if (intent.getAction().equals("com.example.moodmaster.STEPS_UPDATE")) {
                // Update steps count when broadcast received
                steps = intent.getIntExtra("steps", 0);
                loadSteps();
            }
        }
    };

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
