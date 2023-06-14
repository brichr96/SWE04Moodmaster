package com.example.moodmaster.sensors;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import android.widget.TextView;

import com.example.moodmaster.R;

/**
 *
 */
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
        sensorHelper.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorHelper.stop();
    }

    private void processSensorData() {
        List<Float> lightValues = sensorHelper.getLightValues();
        List<Float> accelerometerValues = sensorHelper.getAccelerometerValues();


        updateTextViewWithValues(tvLightValues, lightValues);

        StringBuilder accelerometerSb = new StringBuilder();
        for (float value : accelerometerValues) {
            accelerometerSb.append(value).append("\n");
        }
        tvAccelerometerValues.setText(accelerometerSb.toString());
    }

    private void updateTextViewWithValues(TextView t, List<Float> values){
        StringBuilder lightSb = new StringBuilder();
        for (float value : values) {
            lightSb.append(value).append("\n");
        }
        t.setText(lightSb.toString());
    }
}