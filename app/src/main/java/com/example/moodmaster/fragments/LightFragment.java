package com.example.moodmaster.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.moodmaster.EmergencyCall;
import com.example.moodmaster.R;
import com.example.moodmaster.feelingScale_MoodShow.BreathingActivity;

public class LightFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView displayLight;
    private float lux;
    private SharedPreferences algoValues;
    private final String KEY = "luxValue";

    public LightFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        algoValues = getContext().getSharedPreferences("algo", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_light, container, false);
        displayLight = rootView.findViewById(R.id.lightValueTextView);

        ImageButton emergencyCall = rootView.findViewById(R.id.emergencyButton);

        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmergencyCall.showEmergencyCallConfirmationDialog(getActivity(), getActivity().getApplicationContext());
            }
        });

        Button breathingButton = rootView.findViewById(R.id.startBreathingButton);

        breathingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BreathingActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lux = event.values[0];
        saveLux(lux);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(lux < 50){
                    displayLight.setText(R.string.dark);
                }
                else if(lux >= 50 && lux < 100){
                    displayLight.setText(R.string.dim);
                }
                else if(lux >= 100 && lux < 10000){
                    displayLight.setText(R.string.bright);
                }
                else{
                    displayLight.setText(R.string.sunlight);
                }
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void saveLux(float luxValue){
        SharedPreferences.Editor editor = algoValues.edit();
        editor.putFloat(KEY, luxValue);
        editor.apply();
    }
}
