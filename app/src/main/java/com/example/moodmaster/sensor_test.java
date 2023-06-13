package com.example.moodmaster;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class sensor_test extends AppCompatActivity {
    private SensorHelper sensorHelper;
    private TextView tvLightValues;
    private TextView tvAccelerometerValues; // Add TextView for accelerometer values

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_test);

        tvLightValues = findViewById(R.id.tvLightValues);
        tvAccelerometerValues = findViewById(R.id.tvLightValues);

        // Create an instance of SensorHelper
        sensorHelper = new SensorHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start listening for sensor changes
        sensorHelper.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop listening for sensor changes
        sensorHelper.stop();
    }

    private void processSensorData() {
        List<Float> lightValues = sensorHelper.getLightValues();
        List<Float> accelerometerValues = sensorHelper.getAccelerometerValues();

        // Update the TextViews with the sensor values
        StringBuilder lightSb = new StringBuilder();
        for (float value : lightValues) {
            lightSb.append(value).append("\n");
        }
        tvLightValues.setText(lightSb.toString());

        StringBuilder accelerometerSb = new StringBuilder();
        for (float value : accelerometerValues) {
            accelerometerSb.append(value).append("\n");
        }
        tvAccelerometerValues.setText(accelerometerSb.toString());
    }
}