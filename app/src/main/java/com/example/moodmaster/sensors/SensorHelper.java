package com.example.moodmaster.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class SensorHelper implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor accelerometerSensor;
    private List<Float> lightValues;
    private List<Float> accelerometerValues;

    public SensorHelper(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lightValues = new ArrayList<>();
        accelerometerValues = new ArrayList<>();
    }

    public void start() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            lightValues.add(lightValue);
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xAxis = event.values[0];
            float yAxis = event.values[1];
            float zAxis = event.values[2];
            accelerometerValues.add(xAxis);
            accelerometerValues.add(yAxis);
            accelerometerValues.add(zAxis);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    public List<Float> getLightValues() {
        return lightValues;
    }

    public List<Float> getAccelerometerValues() {
        return accelerometerValues;
    }
}
