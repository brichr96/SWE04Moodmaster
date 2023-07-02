package com.example.moodmaster.feelingScale_MoodShow;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;


/**
 * StepCountService is a Service that uses an accelerometer sensor to count the user's steps.
 * It implements SensorEventListener to receive updates from the accelerometer sensor.
 *
 * The service registers itself as a listener to the accelerometer sensor upon creation and unregisters when it's destroyed.
 *
 * It calculates the acceleration of the device based on sensor values, and determines whether a step has been taken
 * based on changes in acceleration. When a step is detected, the service increments the step count, saves the new count
 * in shared preferences, and broadcasts the new count to any registered receivers.
 *
 * The service also implements the onAccuracyChanged callback from SensorEventListener, but it doesn't take any action
 * in this method.
 */

public class StepCountService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float accel;
    private float accelCurrent;
    private float accelLast;
    private int steps;
    private long lastStepTime;
    private long stepDelay = 500;
    private SharedPreferences algoValues;
    private final String KEY = "steps";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accel = 0.00f;
        accelCurrent = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;

        algoValues = getSharedPreferences("algo", Context.MODE_PRIVATE);
        steps = algoValues.getInt(KEY, 0);

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.8f;
        accelLast = accelCurrent;
        accelCurrent = event.values[0] * alpha + (accelCurrent * (1.0f - alpha));
        accel = Math.abs(accelCurrent - accelLast);

        long currentTime = System.currentTimeMillis();
        if (accel > 2.3f && (currentTime - lastStepTime) > stepDelay) {
            steps++;
            lastStepTime = currentTime;
            saveSteps(steps);
            broadcastSteps(steps);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void saveSteps(int steps) {
        SharedPreferences.Editor editor = algoValues.edit();
        editor.putInt(KEY, steps);
        editor.apply();
    }

    /**
     * Broadcasts the updated step count to registered receivers.
     *
     * @param steps The updated step count to broadcast.
     */
    private void broadcastSteps(int steps) {
        Intent intent = new Intent();
        intent.setAction("com.example.moodmaster.STEPS_UPDATE");
        intent.putExtra("steps", steps);
        sendBroadcast(intent);
    }
}
