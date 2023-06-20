package com.example.moodmaster.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.moodmaster.R;

public class StepCounterFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float accel; // acceleration apart from gravity
    private float accelCurrent; // current acceleration including gravity
    private float accelLast; // last acceleration including gravity

    private int steps;

    private long lastStepTime; // Timestamp of the last step
    private long stepDelay = 250; // Delay (in ms) to avoid counting steps too close together

    private TextView stepCountTextView;
    private ProgressBar progressBar;

    private static final int STEP_GOAL = 10000;

    public StepCounterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accel = 0.00f;
        accelCurrent = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moods_tabbed, container, false);
        stepCountTextView = rootView.findViewById(R.id.step_count_text);
        progressBar = rootView.findViewById(R.id.progress_bar);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        accelLast = accelCurrent;
        accelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = accelCurrent - accelLast;
        accel = accel * 0.9f + delta; // perform low-cut filter

        if (accel > 2.5f) { // this threshold is for a significant step, adjust as needed
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastStepTime) > stepDelay) { // avoid counting steps too close together
                steps++;
                lastStepTime = currentTime;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stepCountTextView.setText(String.valueOf(steps));
                        progressBar.setProgress(steps); // Update the ProgressBar progress

                        if (steps >= STEP_GOAL) {
                            // Display the toast message
                            Toast.makeText(getActivity(), "Congratulations! You've reached your step goal!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // We don't need this in the current context.
    }
}
